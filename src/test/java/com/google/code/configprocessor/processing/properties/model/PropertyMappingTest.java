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
package com.google.code.configprocessor.processing.properties.model;

import static org.junit.Assert.*;

import org.junit.*;

public class PropertyMappingTest {

	@Test
	public void parseWithEqualsSeparator() {
		String text = "prop=value";
		executeTest(text, "prop", "value");
	}
	
	@Test
	public void parseWithColonSeparator() {
		String text = "prop:value";
		executeTest(text, "prop", "value");
	}

	@Test
	public void parseWithDoubleEquals() {
		String text = "prop==value";
		executeTest(text, "prop", "=value");
	}
	
	@Test
	public void parseWithTripleColon() {
		String text = "prop:::value";
		executeTest(text, "prop", "::value");
	}

	@Test
	public void parseWithDoubleEqualsTrimming() {
		String text = "prop = ==value";
		executeTest(text, "prop", " ==value", true);
	}
	
	@Test
	public void parseWithEscapedKey() {
		String text = "prop\\=prop:::value";
		executeTest(text, "prop=prop", "::value");
		
		text = "prop\\=prop\\:===:value";
		executeTest(text, "prop=prop:", "==:value");
		
		text = "\\:\\=:value";
		executeTest(text, ":=", "value");
	}
	
	@Test
	public void parseWithLineBreak() {
		String text = "fruits                           =apple, banana, pear, \\\ncantaloupe, watermelon, \\\nkiwi, mango";
		executeTest(text, "fruits", "apple, banana, pear, \\\ncantaloupe, watermelon, \\\nkiwi, mango", true);
	}

	protected void executeTest(String text, String key, String value) {
		executeTest(text, key, value, false);
	}

	protected void executeTest(String text, String key, String value, boolean trim) {
		PropertyMapping propertyMapping = new PropertyMapping();
		propertyMapping.parse(text, trim);

		assertEquals(key, propertyMapping.getPropertyName());
		assertEquals(value, propertyMapping.getPropertyValue());
	}
}
