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
package com.google.code.configprocessor.processing;

/**
 * Defines what to expect from XPath expressions in terms of nodes returned and how to process them.
 */
public enum NodeSetPolicy {

	/**
	 * XPath expressions should find only one node, default behaviour.
	 */
	SINGLE,

	/**
	 * XPath expressions will find more than one node and all of them must be processed.
	 */
	ALL,

	/**
	 * XPath expressions will find more than one node and only the first one must be processed.
	 */
	FIRST,

	/**
	 * XPath expressions will find more than one node and only the last one must be processed.
	 */
	LAST
	;

	public static NodeSetPolicy valueOfName(String name) {
		for (NodeSetPolicy policy : values()) {
			if (policy.toString().equalsIgnoreCase(name)) {
				return policy;
			}
		}
		throw new IllegalStateException("Unknown NodeSet policy: " + name);
	}
}
