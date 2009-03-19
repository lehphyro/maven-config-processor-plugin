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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Properties;

import org.codehaus.plexus.util.FileUtils;

import com.google.code.configprocessor.log.LogAdapter;
import com.google.code.configprocessor.parsing.ProcessingConfigurationParser;
import com.google.code.configprocessor.processing.Action;
import com.google.code.configprocessor.processing.ActionProcessor;
import com.google.code.configprocessor.processing.properties.PropertiesActionProcessor;
import com.google.code.configprocessor.processing.xml.XmlActionProcessor;

public class ConfigProcessor {

	private static final String DEFAULT_ENCODING = "UTF-8";

	private String encoding;
	private int lineWidth;
	private int indentSize;
	private Map<String, String> namespaceContexts;
	private boolean useOutputDirectory;
	private File outputDirectory;
	private LogAdapter log;

	public ConfigProcessor(String encoding, int indentSize, int lineWidth,
			Map<String, String> namespaceContexts, File outputDirectory,
			boolean useOutputDirectory, LogAdapter log) {
		super();
		this.encoding = encoding;
		this.indentSize = indentSize;
		this.lineWidth = lineWidth;
		this.namespaceContexts = namespaceContexts;
		this.outputDirectory = outputDirectory;
		this.useOutputDirectory = useOutputDirectory;
		this.log = log;
	}

	public LogAdapter getLog() {
		return log;
	}

	public void execute(ExpressionResolver resolver, Transformation transformation)
			throws ConfigProcessException {
		File actualOutputDirectory = null;
		if (useOutputDirectory) {
			if (!outputDirectory.exists()) {
				outputDirectory.mkdirs();
			}
			actualOutputDirectory = outputDirectory;
		}
		if (encoding == null) {
			 getLog().warn("Encoding has not been set, using default [" +
			 DEFAULT_ENCODING + "].");
			encoding = DEFAULT_ENCODING;
		}

		 getLog().debug("Using output directory [" + actualOutputDirectory + "]");
		 getLog().debug("File encodig is [" + encoding + "]");

		File input = new File(transformation.getInput());
		File output = new File(actualOutputDirectory, transformation
				.getOutput());
		File config = new File(transformation.getConfig());
		String type = transformation.getInputType();

		if (!input.exists()) {
			throw new ConfigProcessException("Input file [" + input
					+ "] does not exist");
		}
		if (!config.exists()) {
			throw new ConfigProcessException("Configuration file [" + config
					+ "] does not exist");
		}
		createOutputFile(output);

		process(resolver, input, output, config, type, transformation
				.isReplacePlaceholders());
	}

	/**
	 * Processes a file.
	 * @param resolver 
	 * 
	 * @param input
	 *            Input file to read from.
	 * @param output
	 *            Output file to write to.
	 * @param config
	 *            File containing rules to process the input.
	 * @param type
	 *            Type of the input file. Properties, XML or null if it is to be
	 *            auto-detected.
	 * @param replacePlaceholders
	 *            True if placeholders must be replaced on output files.
	 * @throws ConfigProcessException
	 *             If processing cannot be performed.
	 */
	protected void process(ExpressionResolver resolver, File input, File output, File config, String type,
			boolean replacePlaceholders) throws ConfigProcessException {
		 getLog().info("Processing file [" + input + "], outputing to [" +
		 output + "]");

		InputStream configStream = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		InputStreamReader configStreamReader = null;
		InputStreamReader inputStreamReader = null;
		OutputStreamWriter outputStreamWriter = null;
		try {
			configStream = new FileInputStream(config);
			inputStream = new FileInputStream(input);
			outputStream = new FileOutputStream(output);

			inputStreamReader = new InputStreamReader(inputStream, encoding);
			configStreamReader = new InputStreamReader(configStream, encoding);
			outputStreamWriter = new OutputStreamWriter(outputStream, encoding);

			ProcessingConfigurationParser parser = new ProcessingConfigurationParser();
			Action action = parser.parse(configStreamReader);

			ActionProcessor processor = getActionProcessor(resolver, input, type,
					replacePlaceholders);
			processor.process(inputStreamReader, outputStreamWriter, action);
		} catch (ParsingException e) {
			throw new ConfigProcessException("Error processing file [" + input
					+ "] using configuration [" + config + "]", e);
		} catch (IOException e) {
			throw new ConfigProcessException(
					"Error reading/writing files. Input is [" + input
							+ "], configuration is [" + config + "]", e);
		}
	}

	/**
	 * Obtain the action processor for the input.
	 * @param resolver 
	 * 
	 * @param input
	 *            Input file to read from.
	 * @param type
	 *            Type of the input file. Properties or XML.
	 * @param replacePlaceholders
	 *            True if placeholders must be replaced on output files.
	 * @return ActionProcessor for the input file.
	 * @throws ConfigProcessException
	 *             If processing cannot be performed.
	 */
	protected ActionProcessor getActionProcessor(ExpressionResolver resolver, File input, String type,
			boolean replacePlaceholders) throws ConfigProcessException {
		if (Transformation.XML_TYPE.equals(type)) {
			return new XmlActionProcessor(encoding, lineWidth, indentSize,
					resolver, namespaceContexts);
		} else if (Transformation.PROPERTIES_TYPE.equals(type)) {
			return new PropertiesActionProcessor(resolver);
		} else {
			throw new ConfigProcessException("Unknown file type [" + type + "]");
		}
	}

	/**
	 * Read additional properties file if specified.
	 * @param specificProperties 
	 * 
	 * @return Properties read or empty properties if not specified.
	 * @throws ConfigProcessException
	 *             If processing cannot be performed.
	 */
	static public Properties getAdditionalProperties(File specificProperties)
			throws ConfigProcessException {
		Properties additional = new Properties();
		if (specificProperties == null) {
			return additional;
		}

		if (!specificProperties.exists()) {
			throw new ConfigProcessException("Additional properties file ["
					+ specificProperties + "] does not exist");
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(specificProperties);
			additional.load(fis);
			return additional;
		} catch (Exception e) {
			throw new ConfigProcessException(
					"Error loading additional properties", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new ConfigProcessException(
							"Error closing additional properties file", e);
				}
			}
		}
	}

	/**
	 * Creates output file and required directories.
	 * 
	 * @param output
	 *            Output file to create.
	 * @throws ConfigProcessException
	 *             If processing cannot be performed.
	 */
	protected void createOutputFile(File output) throws ConfigProcessException {
		try {
			File directory = output.getParentFile();
			getLog().debug(output.toString());
			if (!directory.exists()) {
				FileUtils.forceMkdir(output.getParentFile());
			}
		} catch (IOException e) {
			throw new ConfigProcessException(e.getMessage(), e);
		}
	}

}
