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
import java.util.*;

import javax.xml.namespace.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.expression.*;
import com.google.code.configprocessor.processing.*;

public class XmlActionProcessor implements ActionProcessor {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");

	private String encoding;
	private int lineWidth;
	private int indentSize;
	private ExpressionResolver expressionResolver;
	private NamespaceContext namespaceContext;

	public XmlActionProcessor(String encoding, int lineWidth, int indentSize, ExpressionResolver expressionResolver, Map<String, String> contextMappings) {
		this.encoding = encoding;
		this.lineWidth = lineWidth;
		this.indentSize = indentSize;
		this.expressionResolver = expressionResolver;
		namespaceContext = new MapBasedNamespaceContext(contextMappings);
	}

	public void process(InputStreamReader input, OutputStreamWriter output, Action action) throws ParsingException, IOException {
		XmlActionProcessingAdvisor advisor = getAdvisorFor(action);
		try {
			Document document = XmlHelper.parse(input);
			advisor.process(document);
			XmlHelper.write(output, document, encoding, lineWidth, indentSize);
		} catch (SAXException e) {
			throw new ParsingException(e);
		} catch (ParserConfigurationException e) {
			throw new ParsingException(e);
		}
	}

	protected XmlActionProcessingAdvisor getAdvisorFor(Action action) throws ParsingException {
		if (action instanceof AddAction) {
			return new XmlAddActionProcessingAdvisor((AddAction) action, expressionResolver, namespaceContext);
		} else if (action instanceof ModifyAction) {
			return new XmlModifyActionProcessingAdvisor((ModifyAction) action, expressionResolver, namespaceContext);
		} else if (action instanceof RemoveAction) {
			return new XmlRemoveActionProcessingAdvisor((RemoveAction) action, expressionResolver, namespaceContext);
		} else if (action instanceof NestedAction) {
			List<XmlActionProcessingAdvisor> advisors = new ArrayList<XmlActionProcessingAdvisor>();
			NestedAction nestedAction = (NestedAction) action;
			for (Action nested : nestedAction.getActions()) {
				advisors.add(getAdvisorFor(nested));
			}
			return new NestedXmlActionProcessingAdvisor(advisors);
		}
		throw new IllegalArgumentException("Unknown action: " + action);
	}
}
