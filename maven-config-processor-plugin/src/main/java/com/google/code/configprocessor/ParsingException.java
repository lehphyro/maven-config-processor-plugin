package com.google.code.configprocessor;

public class ParsingException extends Exception {

	private static final long serialVersionUID = 8201051595655822844L;

	public ParsingException(String message) {
		super(message);
	}
	
	public ParsingException(Exception e) {
		super(e);
	}
	
}
