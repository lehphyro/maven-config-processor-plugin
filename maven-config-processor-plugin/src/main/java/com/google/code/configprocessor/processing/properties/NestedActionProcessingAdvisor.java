package com.google.code.configprocessor.processing.properties;

import java.util.*;

import com.google.code.configprocessor.processing.properties.model.*;

public class NestedActionProcessingAdvisor implements ActionProcessingAdvisor {

	private List<ActionProcessingAdvisor> advisors;
	
	public NestedActionProcessingAdvisor(List<ActionProcessingAdvisor> advisors) {
		this.advisors = advisors;
	}
	
	public PropertiesFileItem onStartProcessing() {
		CompositePropertiesFileItem composite = new CompositePropertiesFileItem();
		
		for (ActionProcessingAdvisor advisor : advisors) {
			PropertiesFileItem aux = advisor.onStartProcessing();
			if (aux != null) {
				composite.addPropertiesFileItem(aux);
			}
		}
		
		return getItemToReturn(null, composite);
	}
	
	public PropertiesFileItem process(PropertiesFileItem item) {
		CompositePropertiesFileItem composite = new CompositePropertiesFileItem();
		
		for (ActionProcessingAdvisor advisor : advisors) {
			PropertiesFileItem aux = advisor.process(item);
			if (aux == null) {
				composite.removePropertiesFileItem(item);
			} else {
				// TODO Make process return a value different from the specified item to
				// indicate do nothing and test for it, instead of using instanceof
				if (!(advisor instanceof RemoveActionProcessingAdvisor)) {
					addPropertiesFileItem(aux, composite);
				}
			}
		}
		
		return getItemToReturn(null, composite);
	}
	
	public PropertiesFileItem onEndProcessing() {
		CompositePropertiesFileItem composite = new CompositePropertiesFileItem();
		
		for (ActionProcessingAdvisor advisor : advisors) {
			PropertiesFileItem aux = advisor.onEndProcessing();
			if (aux != null) {
				composite.addPropertiesFileItem(aux);
			}
		}
		
		return getItemToReturn(null, composite);
	}

	protected PropertiesFileItem getItemToReturn(PropertiesFileItem fallback, CompositePropertiesFileItem item) {
		if (item.getNestedItems().isEmpty()) {
			return fallback;
		}
		
		return item;
	}
	
	protected void addPropertiesFileItem(PropertiesFileItem item, CompositePropertiesFileItem composite) {
		if (item instanceof CompositePropertiesFileItem) {
			for (PropertiesFileItem aux : ((CompositePropertiesFileItem)item).getNestedItems()) {
				addPropertiesFileItem(aux, composite);
			}
		} else {
			int index = composite.getNestedItems().indexOf(item);
			if (index >= 0) {
				composite.getNestedItems().set(index, item);
			} else {
				composite.addPropertiesFileItem(item);
			}
		}
	}
}
