package com.google.code.configprocessor;

import org.codehaus.plexus.component.configurator.expression.*;

public class ExpressionResolver {

	private ExpressionEvaluator evaluator;
	
	public ExpressionResolver(ExpressionEvaluator evaluator) {
		this.evaluator = evaluator;
	}
	
	public String resolve(String value) {
		try {
			Object resolvedValue = evaluator.evaluate(value);
			if (!(resolvedValue instanceof String)) {
				throw new IllegalArgumentException("Expression [" + value + "] did not resolve to String");
			}
			return (String)resolvedValue;
		} catch (ExpressionEvaluationException e) {
			throw new RuntimeException("Error resolving expression [" + value + "]", e);
		}
	}
	
}
