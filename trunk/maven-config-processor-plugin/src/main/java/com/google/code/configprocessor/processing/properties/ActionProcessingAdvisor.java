package com.google.code.configprocessor.processing.properties;

import com.google.code.configprocessor.processing.properties.model.*;

public interface ActionProcessingAdvisor {
	/**
	 * Called when starting processing of a properties file.
	 * 
	 * @return PropertiesFileItem to add at the start of the file or null if should do nothing.
	 */
	PropertiesFileItem onStartProcessing();
	
	/**
	 * Process a properties file item.
	 * 
	 * @param item Item that has been read.
	 * @return Item passed as argument, new item to use or null if the item must be removed.
	 */
	PropertiesFileItem process(PropertiesFileItem item);
	
	/**
	 * Called when processing of a properties file has been finished.
	 * 
	 * @return PropertiesFileItem to add at the end of the file or null if should do nothing.
	 */
	PropertiesFileItem onEndProcessing();
}
