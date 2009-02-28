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

import javax.xml.namespace.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;

import com.google.code.configprocessor.*;

public abstract class AbstractXmlActionProcessingAdvisor implements XmlActionProcessingAdvisor {
	
	private ExpressionResolver expressionResolver;
	private NamespaceContext namespaceContext;
	private String textExpression;
	private XPathExpression xpathExpression;
	
	public AbstractXmlActionProcessingAdvisor(ExpressionResolver expressionResolver, NamespaceContext namespaceContext) {
		this.expressionResolver = expressionResolver;
		this.namespaceContext = namespaceContext;
	}
	
	protected void compile(String expression) throws ParsingException {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(namespaceContext);
		try {
			this.xpathExpression = xpath.compile(resolve(expression));
			this.textExpression = expression;
		} catch (XPathExpressionException e) {
			throw new ParsingException(e);
		}
	}
	
	protected Node evaluateForSingleNode(Document document, boolean orphanOK, boolean attributeOk) throws ParsingException {
		Node node = evaluateForNode(document);

		if (!orphanOK) {
			Node parent = node.getParentNode();
			if (parent == null) {
				throw new ParsingException("Cannot manipulate node without a parent");
			}
		}
		
		if (!attributeOk && node instanceof Attr) {
			throw new ParsingException("Expression resolved to attribute. It must resolve to node element: " + textExpression);
		}
		
		return node;
	}
	
	private Node evaluateForNode(Document document) throws ParsingException {
		try {
			Node node = (Node)getXPathExpression().evaluate(document, XPathConstants.NODE);

			if (node == null) {
				throw new ParsingException("XPath expression did not find nodes: " + textExpression);
			}
			
			return node;
		} catch (XPathExpressionException e) {
			throw new ParsingException(e);
		}
	}
	
	protected String resolve(String value) {
		return expressionResolver.resolve(value);
	}
	
	protected XPathExpression getXPathExpression() {
		return xpathExpression;
	}
}
