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

import com.google.code.configprocessor.log.LogAdapter;


/**
 * Configuration of a file transformation.
 * 
 * @author Leandro Aparecido
 */
public class Transformation {
	
	public static final String PROPERTIES_TYPE = "properties";
	public static final String XML_TYPE = "xml";
	
	/**
	 * File to process.
	 * 
	 * @parameter
	 * @required
	 */
	private String input;
	
	/**
	 * Output file to generate the result of processing.
	 * 
	 * @parameter
	 * @required
	 */
	private String output;
	
	/**
	 * Configuration file describing the processing to be performed.
	 * 
	 * @parameter
	 * @required
	 */
	private String config;
	
	/**
	 * Type of the file to transform. If not specified, the plugin will try to auto-detect.
	 * Possible values: properties, xml.
	 * 
	 * @parameter
	 * @required
	 */
	private String type;
	
	/**
	 * Indicates if the plugin should replace values in ${} with properties of the maven
	 * environment.
	 * 
	 * @parameter default-value="true"
	 */
	private boolean replacePlaceholders;
	
	private LogAdapter log = null;

	public LogAdapter getLog() {
		return log;
	}

	public Transformation(LogAdapter log) {
		this.log = log;
	}

	public String getInput() {
		return input;
	}
	
	public String getOutput() {
		return output;
	}
	
	public String getConfig() {
		return config;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isReplacePlaceholders() {
		return replacePlaceholders;
	}
	
	public void setInput(String input) {
		this.input = input;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setReplacePlaceholders(boolean replacePlaceholders) {
		this.replacePlaceholders = replacePlaceholders;
	}

	/**
	 * Detects input file type.
	 * 
	 * @param input File to read from.
	 * @param specifiedType Type specified by user, will be used if set, can be null.
	 * @return Input file type.
	 */
	protected String getInputType() {
		String type;
		
		if (getType() == null) {
			if (getInput().endsWith(".properties")) {
				type = Transformation.PROPERTIES_TYPE;
			} else if (getInput().endsWith(".xml")) {
				type = Transformation.XML_TYPE;
			} else {
				if (getLog()!=null) {
					getLog().warn("Could not auto-detect type of input [" + input + "], trying XML. It is recommended that you configure it in your pom.xml (tag: transformations/transformation/type) to avoid errors");
				}
				type = Transformation.XML_TYPE;
			}
		} else {
			type = getType();
		}
		
		return type;
	}
}
