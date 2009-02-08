package com.google.code.configprocessor.processing;

import org.apache.commons.lang.*;

public abstract class AbstractAction implements Action {

	private String name;
	private String value;

	public AbstractAction(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return StringUtils.trimToNull(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return StringUtils.trimToNull(value);
	}

	public void setValue(String value) {
		this.value = value;
	}

	protected abstract String getActionName();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
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
		AbstractAction other = (AbstractAction) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		if (getValue() == null) {
			if (other.getValue() != null) {
				return false;
			}
		} else if (!getValue().equals(other.getValue())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getActionName() + " [name=" + getName() + ";value=" + getValue() + "]";
	}
}
