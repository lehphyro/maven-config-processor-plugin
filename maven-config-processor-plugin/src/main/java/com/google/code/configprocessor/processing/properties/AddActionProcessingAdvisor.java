package com.google.code.configprocessor.processing.properties;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.processing.*;
import com.google.code.configprocessor.processing.properties.model.*;

public class AddActionProcessingAdvisor extends AbstractActionProcessingAdvisor {

	private AddAction action;
	
	public AddActionProcessingAdvisor(AddAction action, ExpressionResolver expressionResolver) {
		super(expressionResolver);
		this.action = action;
	}
	
	@Override
	public PropertiesFileItem process(PropertiesFileItem item) {
		if (item instanceof PropertyMapping) {
			PropertyMapping mapping = (PropertyMapping)item;
			
			if (mapping.getPropertyName().trim().equals(action.getBefore()) ||
				mapping.getPropertyName().trim().equals(action.getAfter())) {
				CompositePropertiesFileItem composite = new CompositePropertiesFileItem();
				
				if (mapping.getPropertyName().trim().equals(action.getBefore())) {
					composite.addPropertiesFileItem(createPropertyMapping(action.getName(), action.getValue()));
					composite.addPropertiesFileItem(item);
				} else {
					composite.addPropertiesFileItem(item);
					composite.addPropertiesFileItem(createPropertyMapping(action.getName(), action.getValue()));
				}
				
				return composite;
			}
		}
		
		return super.process(item);
	}
	
}
