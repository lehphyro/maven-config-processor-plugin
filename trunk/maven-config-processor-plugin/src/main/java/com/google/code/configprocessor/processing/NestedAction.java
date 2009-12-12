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
package com.google.code.configprocessor.processing;

import java.util.*;

public class NestedAction implements Action {

	private static final long serialVersionUID = -3485933297518351307L;

	private List<Action> actions;
	private boolean strict;

	public NestedAction() {
		actions = new ArrayList<Action>();
	}
	
	public NestedAction(Action action, boolean strict) {
		this();
		if (action instanceof NestedAction) {
			actions.addAll(((NestedAction)action).getActions());
		} else {
			actions.add(action);
		}
		this.strict = strict;
	}
	
	public void validate() throws ActionValidationException {
		if (actions != null) {
			for (Action action : actions) {
				action.validate();
			}
		}
	}

	public boolean isStrict() {
		return strict;
	}
	
	public List<Action> getActions() {
		if (actions == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(actions);
	}

	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void removeAction(Action action) {
		actions.remove(action);
	}
	
}
