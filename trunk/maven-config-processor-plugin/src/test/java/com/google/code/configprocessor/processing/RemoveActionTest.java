package com.google.code.configprocessor.processing;

import org.junit.*;

public class RemoveActionTest {

	@Test(expected = ActionValidationException.class)
	public void nameIsRequired() throws Exception {
		RemoveAction action = new RemoveAction();
		action.validate();
	}
}
