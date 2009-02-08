/*
 * Copyright (C) 2009 Leandro de Oliveira Aparecido <lehphyro@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
