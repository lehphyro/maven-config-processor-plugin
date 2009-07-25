package com.google.code.configprocessor.processing;

import org.junit.*;

public class UncommentActionTest {

	@Test(expected = ActionValidationException.class)
	public void nameIsRequired() throws Exception {
		UncommentAction action = new UncommentAction();
		action.validate();
	}
}
