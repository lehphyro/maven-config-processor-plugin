/*
 * Copyright (C) 2009 Leandro de Oliveira Aparecido <lehphyro@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.code.configprocessor.parsing;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.processing.*;
import com.thoughtworks.xstream.*;

public class ProcessingConfigurationParserTest {

	@Test
	public void parsingOk() {
		InputStream is = getClass().getResourceAsStream("/com/google/code/configprocessor/data/xml-processing-configuration.xml");
		
		ProcessingConfigurationParser parser = new ProcessingConfigurationParser();
		NestedAction action = null;
		try {
			action = parser.parse(is);
		} catch (ParsingException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		assertEquals(5, action.getActions().size());
		assertEquals(new AddAction(null, "<test-property>test-value</test-property>", "/root/property3", null), action.getActions().get(0));
		assertEquals(new ModifyAction("/root/property1", "<modified-property1>modified-value</modified-property1>"), action.getActions().get(1));
		assertEquals(new RemoveAction("/root/property2"), action.getActions().get(2));
		assertEquals(new CommentAction("property-to-comment"), action.getActions().get(3));
		assertEquals(new UncommentAction("property-to-uncomment"), action.getActions().get(4));
	}

	@Test(expected = ParsingException.class)
	public void parsingInexistentInput() throws Exception {
		ProcessingConfigurationParser parser = new ProcessingConfigurationParser();
		parser.parse(getClass().getResourceAsStream("inexistent"));
	}

	@Test(expected = ParsingException.class)
	public void parsingInvalidInput() throws Exception {
		ProcessingConfigurationParser parser = new ProcessingConfigurationParser();
		parser.parse(getClass().getResourceAsStream("/com/google/code/configprocessor/data/xml-target-config.xml"));
	}
	
	@Test
	@Ignore
	public void generationExample() {
		XStream xstream = new ProcessingConfigurationParser().getXStream();
		
		NestedAction config = new NestedAction();
		config.addAction(new AddAction("/root", "<property5 attribute=\"value5\"></property3>", "/root/property2", null));
		config.addAction(new ModifyAction("/root/property1", "new-value1"));
		config.addAction(new ModifyAction("/root/property4", ""));
		config.addAction(new RemoveAction("/root/property3"));
		config.addAction(new RemoveAction("/root/property4[@attribute]"));
		config.addAction(new CommentAction("property-to-comment"));
		config.addAction(new UncommentAction("property-to-uncomment"));
		
		System.out.println(xstream.toXML(config));
	}
}
