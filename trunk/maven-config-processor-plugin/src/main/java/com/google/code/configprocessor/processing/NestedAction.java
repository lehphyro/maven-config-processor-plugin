package com.google.code.configprocessor.processing;

import java.util.*;

public class NestedAction implements Action {

	private List<Action> actions;
	
	public NestedAction() {
		actions = new ArrayList<Action>();
	}
	
	public List<Action> getActions() {
		return Collections.unmodifiableList(actions);
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
}
