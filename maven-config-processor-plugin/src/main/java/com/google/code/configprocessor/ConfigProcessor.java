/*
 * Copyright (C) 2009 Leandro de Oliveira Aparecido <lehphyro@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.configprocessor;

import static com.google.code.configprocessor.util.IOUtils.*;

import java.io.*;
import java.util.*;

import com.google.code.configprocessor.expression.*;
import com.google.code.configprocessor.io.*;
import com.google.code.configprocessor.log.*;
import com.google.code.configprocessor.parsing.*;
import com.google.code.configprocessor.processing.*;
import com.google.code.configprocessor.processing.properties.*;
import com.google.code.configprocessor.processing.xml.*;

public class ConfigProcessor {

	private static final String DEFAULT_ENCODING = "UTF-8";

	private String encoding;
	private int lineWidth;
	private int indentSize;
	private Map<String, String> namespaceContexts;
	private boolean useOutputDirectory;
	private File outputDirectory;
	private LogAdapter log;
	private FileResolver fileResolver;
	
	private File actualOutputDirectory;

	public ConfigProcessor(String encoding, int indentSize, int lineWidth, Map<String, String> namespaceContexts, File outputDirectory, boolean useOutputDirectory, LogAdapter log, FileResolver fileResolver) {
		this.encoding = encoding;
		this.indentSize = indentSize;
		this.lineWidth = lineWidth;
		this.namespaceContexts = namespaceContexts;
		this.outputDirectory = outputDirectory;
		this.useOutputDirectory = useOutputDirectory;
		this.log = log;
		this.fileResolver = fileResolver;
	}
	
	public void init() throws IOException {
		if (useOutputDirectory) {
			if (!outputDirectory.exists()) {
				outputDirectory.mkdirs();
			}
			actualOutputDirectory = outputDirectory;
		}
		if (encoding == null) {
			getLog().warn("Encoding has not been set, using default [" + DEFAULT_ENCODING + "].");
			encoding = DEFAULT_ENCODING;
		}

		getLog().debug("Using output directory [" + actualOutputDirectory + "]");
		getLog().debug("File encodig is [" + encoding + "]");
	}

	public void execute(ExpressionResolver resolver, Transformation transformation) throws ConfigProcessorException, IOException {
		File input = fileResolver.resolve(transformation.getInput());
		File config = fileResolver.resolve(transformation.getConfig());
		File output = new File(actualOutputDirectory, transformation.getOutput());
		String type = getInputType(transformation);

		if (!input.exists()) {
			throw new ConfigProcessorException("Input file [" + input + "] does not exist");
		}
		if (!config.exists()) {
			throw new ConfigProcessorException("Configuration file [" + config + "] does not exist");
		}
		createOutputFile(output);

		process(resolver, transformation.getInput(), input, output, transformation.getConfig(), config, type, transformation.isReplacePlaceholders());
	}

	/**
	 * Detects input file type.
	 * 
	 * @param input File to read from.
	 * @param specifiedType Type specified by user, will be used if set, can be null.
	 * @return Input file type.
	 */
	protected String getInputType(Transformation transformation) {
		String type;

		if (transformation.getType() == null) {
			if (transformation.getInput().endsWith(".properties")) {
				type = Transformation.PROPERTIES_TYPE;
			} else if (transformation.getInput().endsWith(".xml")) {
				type = Transformation.XML_TYPE;
			} else {
				if (getLog() != null) {
					getLog().warn(
						"Could not auto-detect type of input [" + transformation.getInput() +
							"], assuming it is XML. It is recommended that you configure it in your pom.xml (tag: transformations/transformation/type) to avoid errors");
				}
				type = Transformation.XML_TYPE;
			}
		} else {
			type = transformation.getType();
		}

		return type;
	}

	/**
	 * Processes a file.
	 * 
	 * @param resolver
	 * 
	 * @param inputName Symbolic name of the input file to read from.
	 * @param input Input file to read from.
	 * @param output Output file to write to.
	 * @param configName Symbolic name of the file containing rules to process the input.
	 * @param config File containing rules to process the input.
	 * @param type Type of the input file. Properties, XML or null if it is to be auto-detected.
	 * @param replacePlaceholders True if placeholders must be replaced on output files.
	 * @throws ConfigProcessorException If processing cannot be performed.
	 */
	protected void process(ExpressionResolver resolver, String inputName, File input, File output, String configName, File config, String type, boolean replacePlaceholders) throws ConfigProcessorException {
		getLog().info("Processing file [" + inputName + "] using config [" + configName + "], outputing to [" + output + "]");

		InputStream configStream = null;
		InputStream inputStream = null;
		ByteArrayOutputStream outputStream = null;

		InputStreamReader configStreamReader = null;
		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;
		try {
			configStream = new FileInputStream(config);
			inputStream = new FileInputStream(input);
			outputStream = new ByteArrayOutputStream();

			inputStreamReader = new InputStreamReader(inputStream, encoding);
			configStreamReader = new InputStreamReader(configStream, encoding);
			outputStreamWriter = new OutputStreamWriter(outputStream, encoding);

			ProcessingConfigurationParser parser = new ProcessingConfigurationParser();
			Action action = parser.parse(configStreamReader);
			action.validate();

			ActionProcessor processor = getActionProcessor(resolver, input, type, replacePlaceholders);
			processor.process(inputStreamReader, outputStreamWriter, action);
		} catch (ParsingException e) {
			throw new ConfigProcessorException("Error processing file [" + inputName + "] using configuration [" + configName + "]", e);
		} catch (IOException e) {
			throw new ConfigProcessorException("Error reading/writing files. Input is [" + inputName + "], configuration is [" + configName + "]", e);
		} finally {
			close(configStreamReader, getLog());
			close(inputStreamReader, getLog());
		}
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(output);
			outputStream.writeTo(fileOut);
		} catch (FileNotFoundException e) {
			getLog().error("Error opening file [" + output + "]", e);
		} catch (IOException e) {
			getLog().error("Error writing file [" + output + "]", e);
		} finally {
			close(outputStreamWriter, getLog());
			close(fileOut, getLog());
		}
	}

	/**
	 * Obtain the action processor for the input.
	 * 
	 * @param expressionResolver
	 * 
	 * @param input Input file to read from.
	 * @param type Type of the input file. Properties or XML.
	 * @param replacePlaceholders True if placeholders must be replaced on output files.
	 * @return ActionProcessor for the input file.
	 * @throws ConfigProcessorException If processing cannot be performed.
	 */
	protected ActionProcessor getActionProcessor(ExpressionResolver expressionResolver, File input, String type, boolean replacePlaceholders) throws ConfigProcessorException {
		if (Transformation.XML_TYPE.equals(type)) {
			return new XmlActionProcessor(encoding, lineWidth, indentSize, fileResolver, expressionResolver, namespaceContexts);
		} else if (Transformation.PROPERTIES_TYPE.equals(type)) {
			return new PropertiesActionProcessor(encoding, fileResolver, expressionResolver);
		} else {
			throw new ConfigProcessorException("Unknown file type [" + type + "]");
		}
	}

	/**
	 * Creates output file and required directories.
	 * 
	 * @param output Output file to create.
	 * @throws ConfigProcessorException If processing cannot be performed.
	 */
	protected void createOutputFile(File output) throws ConfigProcessorException {
		try {
			File directory = output.getParentFile();
			getLog().debug(output.toString());
			if (!directory.exists()) {
				forceMkdirs(output.getParentFile());
			}
		} catch (IOException e) {
			throw new ConfigProcessorException(e.getMessage(), e);
		}
	}

	public LogAdapter getLog() {
		return log;
	}
}
