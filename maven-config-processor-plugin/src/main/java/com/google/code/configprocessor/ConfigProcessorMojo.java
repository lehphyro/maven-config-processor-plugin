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

import java.io.*;
import java.util.*;

import org.apache.maven.execution.*;
import org.apache.maven.plugin.*;
import org.apache.maven.project.*;
import org.apache.maven.project.path.*;
import org.codehaus.plexus.logging.*;
import org.codehaus.plexus.logging.console.*;

import com.google.code.configprocessor.log.*;

/**
 * Generates modified configuration files according to configuration. Includes, excludes, modify, comment and uncomment properties.
 * 
 * @phase process-resources
 * @goal process
 * @author Leandro Aparecido
 */
public class ConfigProcessorMojo extends AbstractMojo {

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
	 * Namespace contexts for XPath expressions. Mapping in the form prefix => url
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

	/**
	 * {@inheritDoc}
	 */
	public void execute() throws MojoExecutionException {
		ConfigProcessor processor = new ConfigProcessor(encoding, indentSize, lineWidth, namespaceContexts, outputDirectory, useOutputDirectory, new LogMaven(getLog()));
		for (Transformation transformation : transformations) {
			try {
				ExpressionResolver resolver = getExpressionResolver(transformation.isReplacePlaceholders(), ConfigProcessor.getAdditionalProperties(specificProperties));
				processor.execute(resolver, transformation);
			} catch (ConfigProcessException e) {
				throw new MojoExecutionException("Error during config processing", e);
			}
		}
	}

	/**
	 * Creates a expression resolver to replace placeholders.
	 * 
	 * @param replacePlaceholders True if placeholders must be replaced on output files.
	 * @param additionalProperties
	 * @return Created ExpressionResolver.
	 * @throws MojoExecutionException If processing cannot be performed.
	 */
	protected ExpressionResolver getExpressionResolver(boolean replacePlaceholders, Properties additionalProperties) throws MojoExecutionException {
		return new ExpressionResolver(new PluginParameterExpressionEvaluator(mavenSession, mojoExecution, new DefaultPathTranslator(), new ConsoleLogger(Logger.LEVEL_INFO, "ConfigProcessorMojo"),
			mavenProject, additionalProperties), replacePlaceholders);
	}

}
