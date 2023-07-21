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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;

import com.google.code.configprocessor.expression.ExpressionResolver;

public class AntExpressionResolver implements ExpressionResolver {

	/**
	 * True if placeholders must be replaced.
	 */
	private boolean replacePlaceholders;

	/**
	 * Ant project.
	 */
	private Project project;

	/**
	 * Properties to use when resolving.
	 */
	private Properties specificProperties;

	public AntExpressionResolver(Project project, Properties specificProperties, boolean replacePlaceholders) {
		this.project = project;
		this.specificProperties = specificProperties == null ? new Properties() : specificProperties;
		this.replacePlaceholders = replacePlaceholders;
	}

	@Override
    public String resolve(String value, boolean isPropertiesValue) {
		String resolvedValue;

		if (replacePlaceholders) {
			PropertyHelper ph = PropertyHelper.getPropertyHelper(project);
			// #issue 31
			Enumeration<?> names = specificProperties.propertyNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				ph.setUserProperty(null, name, specificProperties.getProperty(name));
			}
            resolvedValue = ph.replaceProperties(null, value, loopConvert(specificProperties));
		} else {
			resolvedValue = value;
		}

		return resolvedValue;
	}

    private Hashtable<String, Object> loopConvert(Properties prop)
    {
        Hashtable<String, Object> retMap = new Hashtable<>();
        for (Map.Entry<Object, Object> entry : prop.entrySet())
        {
            retMap.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        return retMap;
    }

}
