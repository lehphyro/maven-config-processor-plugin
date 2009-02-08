package com.google.code.configprocessor.parsing;

import java.io.*;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.processing.*;
import com.thoughtworks.xstream.*;

public class ProcessingConfigurationParser {

	public NestedAction parse(InputStream is) throws ParsingException {
		if (is == null) {
			throw new ParsingException("Invalid inputstream");
		}
		return parse(new InputStreamReader(is));
	}

	public NestedAction parse(Reader is) throws ParsingException {
		XStream xstream = new XStream();
		
		xstream.alias("processor", NestedAction.class);
		xstream.alias("add", AddAction.class);
		xstream.alias("modify", ModifyAction.class);
		xstream.alias("remove", RemoveAction.class);
		xstream.addImplicitCollection(NestedAction.class, "actions");
		
		try {
			return (NestedAction)xstream.fromXML(is);
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	

}
