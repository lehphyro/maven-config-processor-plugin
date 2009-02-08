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
