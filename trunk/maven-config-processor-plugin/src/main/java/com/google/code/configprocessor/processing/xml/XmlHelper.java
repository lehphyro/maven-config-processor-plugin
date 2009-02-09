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
package com.google.code.configprocessor.processing.xml;

import java.io.*;

import javax.xml.parsers.*;

import org.apache.xml.serialize.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class XmlHelper {

	public static final String ROOT_TAG = "root";
	
	public static Document parse(String text, boolean prefixAndSuffix) throws SAXException, ParserConfigurationException {
		String textToParse;
		
		if (prefixAndSuffix) {
			StringBuilder sb = new StringBuilder();
			sb.append('<').append(ROOT_TAG).append('>');
			sb.append(text);
			sb.append("</").append(ROOT_TAG).append('>');
			
			textToParse = sb.toString();
		} else {
			textToParse = text;
		}
		
		try {
			return newDocumentBuilder().parse(new InputSource(new StringReader(textToParse)));
		} catch (IOException e) {
			// Should never happen
			throw new RuntimeException(e);
		}
	}
	
	public static Document parse(InputStreamReader reader) throws SAXException, ParserConfigurationException {
		try {
			return newDocumentBuilder().parse(new InputSource(reader));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Document parse(InputStream is) throws SAXException, ParserConfigurationException {
		try {
			return newDocumentBuilder().parse(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String write(Document document, String encoding, int lineWidth, int indentSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        write(new OutputStreamWriter(baos), document, encoding, lineWidth, indentSize);

        try {
			return new String(baos.toByteArray(), encoding);
		} catch (UnsupportedEncodingException e) {
			// Should never happen
			throw new RuntimeException(e);
		}
	}
	
	public static void write(OutputStreamWriter writer, Document document, String encoding, int lineWidth, int indentSize) {
        OutputFormat format = new OutputFormat(document, encoding, true);
        format.setLineSeparator(XmlActionProcessor.LINE_SEPARATOR);
        format.setLineWidth(lineWidth);
        format.setIndent(indentSize);
        XMLSerializer serializer = new XMLSerializer(writer, format);
        try {
			serializer.serialize(document);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		return domFactory.newDocumentBuilder();
	}
}
