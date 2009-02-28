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
package com.google.code.configprocessor.processing.properties.model;

import java.util.*;

public class NestedPropertiesFileItemAdvice extends PropertiesFileItemAdvice {

	private PropertiesFileItem currentItem;
	private List<PropertiesFileItem> items;
	
	private boolean removeAdviced;
	
	public NestedPropertiesFileItemAdvice(PropertiesFileItem currentItem) {
		super(null, null);
		
		this.currentItem = currentItem;
		this.items = new ArrayList<PropertiesFileItem>();
		
		if (currentItem != null) {
			this.items.add(currentItem);
		}
	}

	public void addAdvice(PropertiesFileItemAdvice advice) {
		int index;
		switch (advice.getType()) {
			case DO_NOTHING:
				break;
			case REMOVE:
				removeAdviced = true;
				items.remove(currentItem);
				break;
			case MODIFY:
				index = items.indexOf(currentItem);
				if (index < 0) {
					items.add(advice.getItem());
				} else {
					items.set(index, advice.getItem());
				}
				break;
			case ADD_AFTER:
				items.add(advice.getItem());
				break;
			case ADD_BEFORE:
				index = items.indexOf(currentItem);
				if (index < 0) {
					items.add(0, advice.getItem());
				} else {
					items.add(index, advice.getItem());
				}
				break;
		}
	}
	
	@Override
	public PropertiesFileItemAdviceType getType() {
		if (items.isEmpty()) {
			if (removeAdviced) {
				return PropertiesFileItemAdviceType.REMOVE;
			}
			return PropertiesFileItemAdviceType.DO_NOTHING;
		}
		return PropertiesFileItemAdviceType.MODIFY;
	}
	
	@Override
	public PropertiesFileItem getItem() {
		CompositePropertiesFileItem composite = new CompositePropertiesFileItem();
		composite.addAllPropertiesFileItems(items);
		
		return composite;
	}
}
