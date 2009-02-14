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
import com.google.code.configprocessor.processing.*;

public class XmlModifyActionProcessingAdvisor extends AbstractXmlActionProcessingAdvisor {

	private String textFragment;
	
	public XmlModifyActionProcessingAdvisor(ModifyAction action, ExpressionResolver expressionResolver, NamespaceContext namespaceContext)
	throws ParsingException {
		super(expressionResolver, namespaceContext);
		
		compile(action.getName());
		this.textFragment = resolve(action.getValue());
	}
	
	public void process(Document document) throws ParsingException {
		try {
			Node node = evaluateForSingleNode(document, true, true);
			
			if (node instanceof Attr) {
				modifyAttribute(document, (Attr)node);
			} else {
				modifyNode(document, node);
			}
		} catch (SAXException e) {
			throw new ParsingException(e);
		} catch (ParserConfigurationException e) {
			throw new ParsingException(e);
		}
	}
	
	protected void modifyNode(Document document, Node oldNode) throws SAXException, ParserConfigurationException {
		Document fragment = XmlHelper.parse(textFragment, false);
		Node parent = oldNode.getParentNode();
		Node importedNode = document.importNode(fragment.getDocumentElement(), true);
		parent.replaceChild(importedNode, oldNode);
	}
	
	protected void modifyAttribute(Document document, Attr oldAttr) throws SAXException, ParserConfigurationException {
		List<Attr> attributes = XmlHelper.parseAttributes(textFragment);
		Node node = oldAttr.getOwnerElement();
		NamedNodeMap nodeMap = node.getAttributes();
		
		for (int i = 0; i < nodeMap.getLength();) {
			Node aux = nodeMap.item(i);
			if (aux.equals(oldAttr)) {
				nodeMap.removeNamedItemNS(aux.getNamespaceURI(), aux.getLocalName());
			} else {
				i++;
			}
		}
		
		for (Attr attr : attributes) {
			Attr importedAttr = (Attr)document.importNode(attr, false);
			nodeMap.setNamedItemNS(importedAttr);
		}
	}
}
