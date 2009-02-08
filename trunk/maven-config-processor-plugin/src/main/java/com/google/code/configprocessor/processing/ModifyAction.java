package com.google.code.configprocessor.processing;

public class ModifyAction extends AbstractAction {

	public ModifyAction() {
		this(null, null);
	}
	
	public ModifyAction(String name, String value) {
		super(name, value);
	}

	@Override
	protected String getActionName() {
		return "Modify";
	}
}
