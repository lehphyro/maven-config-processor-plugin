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

import org.apache.maven.execution.*;
import org.apache.maven.plugin.*;
import org.apache.maven.project.*;
import org.apache.maven.project.path.*;
import org.codehaus.plexus.logging.*;
import org.codehaus.plexus.logging.console.*;
import org.codehaus.plexus.util.*;

import com.google.code.configprocessor.parsing.*;
import com.google.code.configprocessor.processing.*;
import com.google.code.configprocessor.processing.properties.*;
import com.google.code.configprocessor.processing.xml.*;

import java.io.*;
import java.util.*;

/**
 * Generates modified configuration files according to configuration.
 * Includes, excludes and modify properties.
 *  
 * @phase process-resources
 * @goal process
 */
public class ConfigProcessorMojo extends AbstractMojo {
	
	private static final String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * Output directory of the generated files.
	 * 
	 * @parameter default-value="${project.build.directory}"
	 * @required
	 * @since 1.0
	 */
	private File outputDirectory;
	
	/**
	 * Indicate if should prefix file paths with the outputDirectory configuration property.
	 * 
	 * @parameter default-value="true"
	 * @required
	 * @since 1.0
	 */
	private boolean useOutputDirectory;

	/**
	 * Encoding to use when reading or writing files.
	 * 
	 * @parameter default-value="${project.build.sourceEncoding}"
	 * @since 1.1
	 */
	private String encoding;

	/**
	 * Maximum line width of the generated files to use when formatting.
	 * 
	 * @parameter default-value="80"
	 * @since 1.2
	 */
	private Integer lineWidth;
	
	/**
	 * Indentation size as the number of whitespaces to use when formatting.
	 * 
	 * @parameter default-value="4"
	 * @since 1.2
	 */
	private Integer indentSize;
	
	/**
	 * File to load aditional specific properties for plugin execution.
	 * 
	 * @parameter
	 * @since 1.0
	 */
	private File specificProperties;
	
	/**
	 * File transformations to be performed.
	 * 
	 * @parameter
	 * @required
	 * @since 1.0
	 */
	private List<Transformation> transformations;
	
	/**
	 * Namespace contexts for XPath expressions.
	 * Mapping in the form prefix => url
	 * 
	 * @parameter
	 * @since 1.2
	 */
	private Map<String, String> namespaceContexts;

    /**
     * The Maven Project Object.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
	private MavenProject mavenProject;
	
    /**
     * The Maven Session Object.
     *
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    private MavenSession mavenSession;
    
    /**
     * The Mojo Execution Object.
     * 
     * @parameter expression="${mojoExecution}"
     * @required
     * @readonly
     */
    private MojoExecution mojoExecution;

	public void execute() throws MojoExecutionException {
		File actualOutputDirectory = null;
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
		
		for (Transformation transformation : transformations) {
			File input = new File(transformation.getInput());
			File output = new File(actualOutputDirectory, transformation.getOutput());
			File config = new File(transformation.getConfig());
			String type = transformation.getType();
			
			if (!input.exists()) {
				throw new MojoExecutionException("Input file [" + input + "] does not exist");
			}
			if (!config.exists()) {
				throw new MojoExecutionException("Configuration file [" + config + "] does not exist");
			}
			createOutputFile(output);
			
			process(input, output, config, type, transformation.isReplacePlaceholders());
		}
	}
	
	protected void process(File input, File output, File config, String type, boolean replacePlaceholders) throws MojoExecutionException {
		getLog().info("Processing file [" + input + "], outputing to [" + output + "]");
		
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

			ActionProcessor processor = getActionProcessor(input, type, replacePlaceholders);
			processor.process(inputStreamReader, outputStreamWriter, action);
		} catch (ParsingException e) {
			throw new MojoExecutionException("Error processing file [" + input + "] using configuration [" + config + "]", e);
		} catch (IOException e) {
			throw new MojoExecutionException("Error reading/writing files. Input is [" + input + "], configuration is [" + config + "]", e);
		}
	}
	
	protected ActionProcessor getActionProcessor(File input, String specifiedType, boolean replacePlaceholders) throws MojoExecutionException {
		String type = getInputType(input, specifiedType);
		
		if (Transformation.XML_TYPE.equals(type)) {
			return new XmlActionProcessor(encoding, lineWidth, indentSize, getExpressionResolver(replacePlaceholders), namespaceContexts);
		} else if (Transformation.PROPERTIES_TYPE.equals(type)) {
			return new PropertiesActionProcessor(getExpressionResolver(replacePlaceholders));
		} else {
			throw new MojoExecutionException("Unknown file type [" + type + "]");
		}
	}
	
	protected String getInputType(File input, String specifiedType) {
		String type;
		
		if (specifiedType == null) {
			if (input.getName().endsWith(".properties")) {
				type = Transformation.PROPERTIES_TYPE;
			} else if (input.getName().endsWith(".xml")) {
				type = Transformation.XML_TYPE;
			} else {
				getLog().warn("Could not auto-detect type of input [" + input + "], trying XML. It is recommended that you configure it in your pom.xml (tag: transformations/transformation/type) to avoid errors");
				type = Transformation.XML_TYPE;
			}
		} else {
			type = specifiedType;
		}
		
		return type;
	}
	
	protected ExpressionResolver getExpressionResolver(boolean replacePlaceholders) throws MojoExecutionException {
		return new ExpressionResolver(
			new PluginParameterExpressionEvaluator(mavenSession,
												   mojoExecution,
												   new DefaultPathTranslator(),
												   new ConsoleLogger(Logger.LEVEL_INFO, "ConfigProcessorMojo"),
												   mavenProject,
												   getAdditionalProperties()),
			replacePlaceholders);
	}
	
	protected Properties getAdditionalProperties() throws MojoExecutionException {
		Properties additional = new Properties();
		if (specificProperties == null) {
			return additional;
		}
		
		if (!specificProperties.exists()) {
			throw new MojoExecutionException("Additional properties file [" + specificProperties + "] does not exist");
		}
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(specificProperties);
			additional.load(fis);
			return additional;
		} catch (Exception e) {
			throw new MojoExecutionException("Error loading additional properties", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new MojoExecutionException("Error closing additional properties file", e);
				}
			}
		}
	}
	
	protected void createOutputFile(File output) throws MojoExecutionException {
		try {
			File directory = output.getParentFile();
			if (!directory.exists()) {
				FileUtils.forceMkdir(output.getParentFile());
			}
		} catch (IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}
}
