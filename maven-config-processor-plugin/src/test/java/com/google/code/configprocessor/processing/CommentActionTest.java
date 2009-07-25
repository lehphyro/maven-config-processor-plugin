package com.google.code.configprocessor.processing;

import org.junit.*;

public class CommentActionTest {

	@Test(expected = ActionValidationException.class)
	public void nameIsRequired() throws Exception {
		CommentAction action = new CommentAction();
		action.validate();
	}
}
