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
package com.google.code.configprocessor.ant;

import java.io.*;
import java.util.*;

import org.apache.tools.ant.*;
import org.codehaus.plexus.component.configurator.expression.*;

public class ExpressionEvaluatorAnt implements ExpressionEvaluator {

	private Project project;
	private Hashtable<Object, Object> properties;

	public ExpressionEvaluatorAnt(Project project, Hashtable<Object, Object> properties) {
		this.project = project;
		this.properties = properties;
	}

	public File alignToBaseDirectory(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object evaluate(String expression) throws ExpressionEvaluationException {
		PropertyHelper ph = PropertyHelper.getPropertyHelper(project);
		return ph.replaceProperties(null, expression, properties);
	}

}
