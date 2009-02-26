package com.google.code.configprocessor.processing.properties;

import java.util.regex.*;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.processing.*;
import com.google.code.configprocessor.processing.properties.model.*;

public class PropertiesUncommentActionProcessingAdvisor extends AbstractPropertiesActionProcessingAdvisor {

	private UncommentAction action;
	private Pattern commentPrefixPattern;
	
	public PropertiesUncommentActionProcessingAdvisor(UncommentAction action, ExpressionResolver expressionResolver) {
		super(expressionResolver);
		this.action = action;
		
		this.commentPrefixPattern = Pattern.compile(Comment.PREFIX_1 + "|" + Comment.PREFIX_2);
	}
	
	@Override
	public PropertiesFileItemAdvice process(PropertiesFileItem item) {
		if (item instanceof Comment) {
			Comment comment = (Comment)item;
			String text = commentPrefixPattern.matcher(comment.getAsText()).replaceAll("");
			text = resolve(text);
			
			PropertyMapping mapping = new PropertyMapping();
			mapping.parse(text, true);
			
			if (mapping.getPropertyName().trim().equals(action.getName())) {
				return new PropertiesFileItemAdvice(PropertiesFileItemAdviceType.MODIFY, mapping);
			}
		}
		return super.process(item);
	}
}
