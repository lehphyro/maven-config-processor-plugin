package com.google.code.configprocessor.processing;

import org.junit.*;

public class AddActionTest {

	@Test(expected = ActionValidationException.class)
	public void fileIsRequiredIfNameIsNotDefined() throws Exception {
		AddAction action = new AddAction();
		action.validate();
	}

	@Test(expected = ActionValidationException.class)
	public void valueCannotBeDefinedIfFileIsDefined() throws Exception {
		AddAction action = new AddAction();
		action.setValue("value");
		action.setFile("file");
		action.validate();
	}
	
	@Test(expected = ActionValidationException.class)
	public void fileCannotBeDefinedIfNameIsDefined() throws Exception {
		AddAction action = new AddAction();
		action.setFile("file");
		action.setName("name");
		action.validate();
	}

	@Test(expected = ActionValidationException.class)
	public void valueIsRequiredIfNameIsDefined() throws Exception {
		AddAction action = new AddAction();
		action.setName("name");
		action.validate();
	}
	
}
