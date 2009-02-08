package com.google.code.configprocessor.processing;

public class RemoveAction extends AbstractAction {

	public RemoveAction() {
		this(null);
	}
	
	public RemoveAction(String name) {
		super(name, null);
	}
	
	@Override
	protected String getActionName() {
		return "Remove";
	}
	
	@Override
	public String toString() {
		return getActionName() + " [name=" + getName() + "]";
	}
}
