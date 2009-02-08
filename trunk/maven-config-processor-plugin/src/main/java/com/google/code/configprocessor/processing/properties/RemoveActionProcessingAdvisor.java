package com.google.code.configprocessor.processing.properties;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.processing.*;
import com.google.code.configprocessor.processing.properties.model.*;

public class RemoveActionProcessingAdvisor extends AbstractActionProcessingAdvisor {

	private RemoveAction action;
	
	public RemoveActionProcessingAdvisor(RemoveAction action, ExpressionResolver expressionResolver) {
		super(expressionResolver);
		this.action = action;
	}
	
	@Override
	public PropertiesFileItem process(PropertiesFileItem item) {
		if (item instanceof PropertyMapping) {
			PropertyMapping mapping = (PropertyMapping)item;
			if (mapping.getPropertyName().trim().equals(action.getName())) {
				return null;
			}
		}
		
		return super.process(item);
	}

}
