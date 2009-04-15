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

import java.util.*;

import javax.xml.namespace.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.expression.*;
import com.google.code.configprocessor.processing.*;

public class XmlAddActionProcessingAdvisor extends AbstractXmlActionProcessingAdvisor {

	private AddAction action;
	private String textFragment;

	public XmlAddActionProcessingAdvisor(AddAction action, ExpressionResolver expressionResolver, NamespaceContext namespaceContext) throws ParsingException {
		super(expressionResolver, namespaceContext);

		this.action = action;
		textFragment = resolve(action.getValue());

		if (action.getBefore() != null) {
			compile(action.getBefore());
		} else if (action.getAfter() != null) {
			compile(action.getAfter());
		} else {
			if (XmlHelper.representsNodeElement(textFragment)) {
				throw new ParsingException("Add action must specify [before] or [after] attribute");
			}
			if (action.getName() == null) {
				throw new ParsingException("Add action must specify [name] when appending attributes");
			}
			compile(action.getName());
		}
	}

	public void process(Document document) throws ParsingException {
		if (XmlHelper.representsNodeElement(textFragment)) {
			Node node = evaluateForSingleNode(document, false, false);
			addNode(document, node);
		} else {
			Node node = evaluateForSingleNode(document, true, false);
			addAttribute(document, node);
		}
	}

	protected void addNode(Document document, Node node) throws ParsingException {
		Node parent = node.getParentNode();

		try {
			Document fragment = XmlHelper.parse(textFragment, true);

			Node referenceNode;
			if (action.getBefore() != null) {
				referenceNode = node;
			} else {
				referenceNode = node.getNextSibling();
				if (referenceNode == null) {
					referenceNode = node;
				}
			}

			NodeList nodeList = fragment.getFirstChild().getChildNodes();
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node importedNode = document.importNode(nodeList.item(i), true);
				parent.insertBefore(importedNode, referenceNode);
			}
		} catch (SAXException e) {
			throw new ParsingException(e);
		} catch (ParserConfigurationException e) {
			throw new ParsingException(e);
		}
	}

	protected void addAttribute(Document document, Node node) throws ParsingException {
		try {
			List<Attr> attributes = XmlHelper.parseAttributes(textFragment);

			NamedNodeMap nodeMap = node.getAttributes();
			for (Attr attr : attributes) {
				Attr importedAttr = (Attr) document.importNode(attr, false);
				nodeMap.setNamedItemNS(importedAttr);
			}
		} catch (SAXException e) {
			throw new ParsingException(e);
		} catch (ParserConfigurationException e) {
			throw new ParsingException(e);
		}
	}
}
