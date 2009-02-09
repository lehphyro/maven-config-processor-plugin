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
}
