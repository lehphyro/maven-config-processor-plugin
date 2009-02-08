package com.google.code.configprocessor.processing.properties;

import java.io.*;
import java.util.*;

import org.apache.commons.lang.*;

import com.google.code.configprocessor.*;
import com.google.code.configprocessor.processing.*;
import com.google.code.configprocessor.processing.properties.model.*;

public class PropertiesActionProcessor implements ActionProcessor {

	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String PROPERTY_VALUE_SEPARATOR = "\\";
	
	private ExpressionResolver expressionResolver;
	
	public PropertiesActionProcessor(ExpressionResolver expressionResolver) {
		this.expressionResolver = expressionResolver;
	}
	
	public void process(InputStreamReader input, OutputStreamWriter output, Action action) throws ParsingException, IOException {
		ActionProcessingAdvisor advisor = getAdvisorFor(action);
		BufferedReader reader = new BufferedReader(input);
		BufferedWriter writer = new BufferedWriter(output);

		// Start
		PropertiesFileItem currentItem = advisor.onStartProcessing();
		if (currentItem != null) {
			writer.append(currentItem.getAsText());
		}
		currentItem = null;
		
		// Process
		String line;
		while ((line = reader.readLine()) != null) {
			if (isBlankLine(line)) {
				writer.append(line);
			} else if (isComment(line)) {
				currentItem = new Comment(line);
			} else {
				StringBuilder sb = new StringBuilder(line);
				while (line.endsWith(PROPERTY_VALUE_SEPARATOR)) {
					line = reader.readLine();
					if (line == null) {
						break;
					}
					sb.append(LINE_SEPARATOR);
					sb.append(line);
				}
				currentItem = parsePropertyMapping(sb.toString());
			}
			
			currentItem = advisor.process(currentItem);
			if (currentItem != null) {
				writer.append(currentItem.getAsText());
				writer.append(LINE_SEPARATOR);
			}
		}

		// End
		currentItem = advisor.onEndProcessing();
		if (currentItem != null) {
			writer.append(currentItem.getAsText());
		}
		
		writer.flush();
	}
	
	protected ActionProcessingAdvisor getAdvisorFor(Action action) {
		if (action instanceof AddAction) {
			return new AddActionProcessingAdvisor((AddAction)action, expressionResolver);
		} else if (action instanceof ModifyAction) {
			return new ModifyActionProcessingAdvisor((ModifyAction)action, expressionResolver);
		} else if (action instanceof RemoveAction) {
			return new RemoveActionProcessingAdvisor((RemoveAction)action, expressionResolver);
		} else if (action instanceof NestedAction) {
			List<ActionProcessingAdvisor> advisors = new ArrayList<ActionProcessingAdvisor>();
			NestedAction nestedAction = (NestedAction)action;
			for (Action nested : nestedAction.getActions()) {
				advisors.add(getAdvisorFor(nested));
			}
			return new NestedActionProcessingAdvisor(advisors);
		}
		throw new IllegalArgumentException("Unknown action: " + action);
	}
	
	protected boolean isBlankLine(String line) {
		return line.trim().length() == 0;
	}
	
	protected boolean isComment(String line) {
		String trimmedLine = line.trim();
		return trimmedLine.startsWith("#") || trimmedLine.startsWith("!");
	}
	
	protected PropertyMapping parsePropertyMapping(String line) {
		String[] splitted = StringUtils.split(line, "=:");
		
		if (splitted.length == 1) {
			return new PropertyMapping(splitted[0], null);
		}
		
		return new PropertyMapping(splitted[0], splitted[1]);
	}
}
