package com.google.code.configprocessor.processing.properties;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.processing.*;
import com.google.code.configprocessor.processing.properties.model.*;

public class ModifyActionProcessingAdvisor extends AbstractActionProcessingAdvisor {

	private ModifyAction action;
	
	public ModifyActionProcessingAdvisor(ModifyAction action, ExpressionResolver expressionResolver) {
		super(expressionResolver);
		this.action = action;
	}
	
	@Override
	public PropertiesFileItem process(PropertiesFileItem item) {
		if (item instanceof PropertyMapping) {
			PropertyMapping mapping = (PropertyMapping)item;
			if (mapping.getPropertyName().trim().equals(action.getName())) {
				return createPropertyMapping(mapping.getPropertyName(), action.getValue());
			}
		}
		
		return super.process(item);
	}
	
}
