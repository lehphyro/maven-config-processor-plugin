package com.google.code.configprocessor.processing;

import com.google.code.configprocessor.*;

public class ActionValidationException extends ConfigProcessorException {

	private static final long serialVersionUID = 4425159927533088541L;

	private Action action;
	
	public ActionValidationException(String message, Action action) {
		super(message + " - Transformation: " + action);
		
		this.action = action;
	}
	
	public Action getAction() {
		return action;
	}
}
