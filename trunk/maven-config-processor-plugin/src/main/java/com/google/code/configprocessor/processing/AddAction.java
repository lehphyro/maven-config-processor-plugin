package com.google.code.configprocessor.processing;

import org.apache.commons.lang.*;

public class AddAction extends AbstractAction {

	private String after;
	private String before;
	
	public AddAction() {
		this(null, null);
	}
	
	public AddAction(String name, String value) {
		this(name, value, null, null);
	}

	public AddAction(String name, String value, String after, String before) {
		super(name, value);
		
		this.after = after;
		this.before = before;
		
		if (getAfter() == null && getBefore() == null) {
			throw new IllegalArgumentException("Either before or after is required for property: " + name);
		} else if (getAfter() != null && getBefore() != null) {
			throw new IllegalArgumentException("Choose only one of either before or after to set for property: " + name);
		}
	}

	@Override
	protected String getActionName() {
		return "Add";
	}
	
	public String getAfter() {
		return StringUtils.trimToNull(after);
	}
	
	public String getBefore() {
		return StringUtils.trimToNull(before);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getAfter() == null) ? 0 : getAfter().hashCode());
		result = prime * result + ((getBefore() == null) ? 0 : getBefore().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AddAction other = (AddAction) obj;
		if (getAfter() == null) {
			if (other.getAfter() != null) {
				return false;
			}
		} else if (!getAfter().equals(other.getAfter())) {
			return false;
		}

		if (getBefore() == null) {
			if (other.getBefore() != null) {
				return false;
			}
		} else if (!getBefore().equals(other.getBefore())) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return getActionName() + " [name=" + getName() + ";value=" + getValue() + ";after=" + getAfter() + ";before=" + getBefore() + "]";
	}
}
