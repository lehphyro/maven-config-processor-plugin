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
package com.google.code.configprocessor.ant;

import java.io.*;
import java.util.*;

import org.apache.tools.ant.*;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.log.*;

/**
 * Ant task Generates modified configuration files according to configuration. Includes, excludes, modify, comment and uncomment properties.
 */
public class ConfigProcessorTask extends Task {

	private List<Transformation> transforms = new ArrayList<Transformation>();
	private String encoding;
	private int indentSize;
	private int lineWidth;
	private List<NamespaceContext> namespaceContexts = new ArrayList<NamespaceContext>();
	private File outputDirectory;
	private boolean useOutputDirectory;
	private File specificProperties;
	private LogAdapter log = new LogAnt(this);

	@Override
	public void execute() {
		Map<String, String> namespaceContextsMap = new HashMap<String, String>();
		for (NamespaceContext nsContext : namespaceContexts) {
			namespaceContextsMap.put(nsContext.getPrefix(), nsContext.getUrl());
		}
		ConfigProcessor processor = new ConfigProcessor(encoding, indentSize, lineWidth, namespaceContextsMap, outputDirectory, useOutputDirectory, log);
		for (Transformation transformation : transforms) {
			try {
				ExpressionResolver resolver = new ExpressionResolver(new ExpressionEvaluatorAnt(getProject(), ConfigProcessor.getAdditionalProperties(specificProperties)), transformation
						.isReplacePlaceholders());
				processor.execute(resolver, transformation);
			} catch (ConfigProcessException e) {
				throw new BuildException("Error during config processing", e);
			}
		}
	}

	public Transformation createTransformation() {
		Transformation transformation = new Transformation();
		transforms.add(transformation);
		return transformation;
	}

	public NamespaceContext createNamespaceContext() {
		NamespaceContext namespaceContext = new NamespaceContext();
		namespaceContexts.add(namespaceContext);
		return namespaceContext;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setIndentSize(int indentSize) {
		this.indentSize = indentSize;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = new File(outputDirectory);
	}

	public void setUseOutputDirectory(boolean useOutputDirectory) {
		this.useOutputDirectory = useOutputDirectory;
	}

	public void setSpecificProperties(File specificProperties) {
		this.specificProperties = specificProperties;
	}

	public class NamespaceContext {
		private String prefix;
		private String url;

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

}
