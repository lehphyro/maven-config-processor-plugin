package com.google.code.configprocessor.processing.properties;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.processing.properties.model.*;

public abstract class AbstractActionProcessingAdvisor implements ActionProcessingAdvisor {

	private ExpressionResolver expressionResolver;
	
	public AbstractActionProcessingAdvisor(ExpressionResolver expressionResolver) {
		this.expressionResolver = expressionResolver;
	}
	
	/**
	 * Default implementation to indicate do nothing.
	 */
	public PropertiesFileItem onStartProcessing() {
		return null;
	}

	/**
	 * Default implementation to indicate do nothing.
	 */
	public PropertiesFileItem process(PropertiesFileItem item) {
		return item;
	}
	
	/**
	 * Default implementation to indicate do nothing.
	 */
	public PropertiesFileItem onEndProcessing() {
		return null;
	}
	
	protected PropertyMapping createPropertyMapping(String name, String value) {
		return new PropertyMapping(name, expressionResolver.resolve(value));
	}

}
