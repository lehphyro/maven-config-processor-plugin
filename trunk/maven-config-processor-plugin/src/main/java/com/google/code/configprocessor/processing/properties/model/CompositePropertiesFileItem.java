package com.google.code.configprocessor.processing.properties.model;

import java.util.*;

import com.google.code.configprocessor.processing.properties.*;

public class CompositePropertiesFileItem implements PropertiesFileItem {

	private List<PropertiesFileItem> nestedItems;
	
	public CompositePropertiesFileItem() {
		nestedItems = new ArrayList<PropertiesFileItem>();
	}

	public String getAsText() {
		StringBuilder sb = new StringBuilder();

		Iterator<PropertiesFileItem> it = nestedItems.iterator();
		while (it.hasNext()) {
			PropertiesFileItem item = it.next();
			sb.append(item.getAsText());
			
			if (it.hasNext()) {
				sb.append(PropertiesActionProcessor.LINE_SEPARATOR);
			}
		}
		
		return sb.toString();
	}

	public void addPropertiesFileItem(PropertiesFileItem item) {
		nestedItems.add(item);
	}
	
	public void removePropertiesFileItem(PropertiesFileItem item) {
		nestedItems.remove(item);
	}
	
	public List<PropertiesFileItem> getNestedItems() {
		return nestedItems;
	}
	
	@Override
	public String toString() {
		return "Composite:\n" + nestedItems;
	}
}
