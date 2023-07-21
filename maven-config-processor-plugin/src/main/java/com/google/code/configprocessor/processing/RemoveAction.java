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

import org.apache.commons.lang3.StringUtils;

public class RemoveAction extends AbstractAction {

	private static final long serialVersionUID = 6999363851417248906L;

	private String nodeSetPolicy;

	public RemoveAction() {
		this(null);
	}

	public RemoveAction(String name) {
		this(name, NodeSetPolicy.SINGLE);
	}

	public RemoveAction(String name, NodeSetPolicy nodeSetPolicy) {
		super(name, name);
		this.nodeSetPolicy = nodeSetPolicy.toString();
	}

	@Override
    public void validate() throws ActionValidationException {
		if (getName() == null) {
			throw new ActionValidationException("Name is required", this);
		}
		if (StringUtils.isBlank(getNodeSetPolicy())) {
			throw new ActionValidationException("NodeSetPolicy is required", this);
		}
	}

	public String getNodeSetPolicy() {
		return nodeSetPolicy;
	}

	public NodeSetPolicy getNodeSetPolicyAsEnum() {
		return NodeSetPolicy.valueOfName(getNodeSetPolicy());
	}

	public void setNodeSetPolicy(String nodeSetPolicy) {
		this.nodeSetPolicy = nodeSetPolicy;
	}

	@Override
	protected String getActionName() {
		return "Remove";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + getNodeSetPolicyAsEnum().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RemoveAction other = (RemoveAction) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		if (getNodeSetPolicyAsEnum() != other.getNodeSetPolicyAsEnum()) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getActionName() + " [name=" + getName() + "]";
	}
}
