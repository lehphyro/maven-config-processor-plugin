package com.google.code.configprocessor.processing;

import org.junit.*;

public class ModifyActionTest {

	@Test(expected = ActionValidationException.class)
	public void findReplaceAreRequiredIfNameIsNotDefined() throws Exception {
		ModifyAction action = new ModifyAction();
		action.setFind("find");
		action.validate();
	}
	
	@Test(expected = ActionValidationException.class)
	public void valueIsNotAllowedIfNameIsNotDefined() throws Exception {
		ModifyAction action = new ModifyAction();
		action.setValue("value");
		action.validate();
	}
	
	@Test(expected = ActionValidationException.class)
	public void findReplaceAreNotAllowedIfNameIsDefined() throws Exception {
		ModifyAction action = new ModifyAction();
		action.setName("name");
		action.setFind("find");
		action.validate();
	}
}
