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
package com.google.code.configprocessor.processing.properties;

import java.util.regex.*;

import org.apache.commons.lang.*;

import com.google.code.configprocessor.expression.*;
import com.google.code.configprocessor.processing.*;
import com.google.code.configprocessor.processing.properties.model.*;

public class PropertiesUncommentActionProcessingAdvisor extends AbstractPropertiesActionProcessingAdvisor {

	private UncommentAction action;
	private Pattern commentPrefixPattern;

	public PropertiesUncommentActionProcessingAdvisor(UncommentAction action, ExpressionResolver expressionResolver) {
		super(expressionResolver);
		this.action = action;

		commentPrefixPattern = Pattern.compile("(\\s*)" + Comment.PREFIX_1 + "|" + Comment.PREFIX_2);
	}

	@Override
	public PropertiesFileItemAdvice process(PropertiesFileItem item) {
		if (item instanceof Comment) {
			Comment comment = (Comment) item;
			String[] parts = comment.getAsText().split(PropertiesActionProcessor.LINE_SEPARATOR);
			for(int i = 0; i < parts.length; ++i)
			{
				Matcher matcher = commentPrefixPattern.matcher(parts[i]);
				if (matcher.find())
				{
					String replace = matcher.group(1);
					if (replace == null)
					{
						replace = "";
					}
					parts[i] = matcher.replaceFirst(replace);
				}
			}
			String text = resolve(StringUtils.join(parts, PropertiesActionProcessor.LINE_SEPARATOR));

			if (!StringUtils.isBlank(text)) {
				PropertyMapping mapping = new PropertyMapping();
				mapping.parse(text, true);
	
				if (mapping.getPropertyName().trim().equals(action.getName())) {
					return new PropertiesFileItemAdvice(PropertiesFileItemAdviceType.MODIFY, mapping);
				}
			}
		}
		return super.process(item);
	}
}
