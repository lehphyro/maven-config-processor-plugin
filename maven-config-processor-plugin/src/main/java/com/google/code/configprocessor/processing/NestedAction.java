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

	public NestedAction() {
		actions = new ArrayList<Action>();
	}
	
	public void validate() throws ActionValidationException {
		for (Action action : actions) {
			action.validate();
		}
	}

	public List<Action> getActions() {
		return Collections.unmodifiableList(actions);
	}

	public void addAction(Action action) {
		actions.add(action);
	}
}
