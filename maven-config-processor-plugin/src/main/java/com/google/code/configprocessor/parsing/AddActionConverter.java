package com.google.code.configprocessor.parsing;

import com.google.code.configprocessor.processing.*;
import com.thoughtworks.xstream.converters.*;
import com.thoughtworks.xstream.io.*;

public class AddActionConverter implements Converter {

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class type) {
		return AddAction.class.isAssignableFrom(type);
	}

	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		throw new UnsupportedOperationException();
	}
	
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		AddAction action = new AddAction();
		
		int i = 0;
		for (; reader.hasMoreChildren(); i++) {
			reader.moveDown();
			setValue(reader, action);
			reader.moveUp();
		}
		
		return action;
	}
	
	protected void setValue(HierarchicalStreamReader reader, AddAction action) {
		String name = reader.getNodeName();
		
		if ("name".equals(name)) {
			action.setName(reader.getValue());
		} else if ("value".equals(name)) {
			action.setValue(reader.getValue());
		} else if ("strict".equals(name)) {
			action.setStrict(Boolean.valueOf(reader.getValue()));
		} else if ("after".equals(name)) {
			action.setAfter(reader.getValue());
		} else if ("before".equals(name)) {
			action.setBefore(reader.getValue());
		} else if ("inside".equals(name)) {
			action.setInside(reader.getValue());
		} else if ("file".equals(name)) {
			if (reader.getAttributeCount() > 0) {
				action.setIgnoreRoot(Boolean.valueOf(reader.getAttribute("ignore-root")));
			}
			action.setFile(reader.getValue());
		}
	}

}
