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

import org.apache.commons.lang.*;

public class AddAction extends AbstractAction {

	private static final long serialVersionUID = -5444028483023476286L;
	
	private String after;
	private String before;
	private String inside;
	private String file;

	public AddAction() {
		this(null, null);
	}

	public AddAction(String name, String value) {
		this(name, value, null, null);
	}

	public AddAction(String file, String after, String before) {
		super(null, null);
		
		this.file = file;
		this.after = after;
		this.before = before;
	}

	public AddAction(String name, String value, String after, String before) {
		super(name, value);

		this.after = after;
		this.before = before;
	}
	
	public void validate() throws ActionValidationException {
		if (getName() == null) {
			if (getFile() == null) {
				throw new ActionValidationException("File is required when name is not provided", this);
			}
			if (getValue() != null) {
				throw new ActionValidationException("Cannot define both file and value", this);
			}
		} else {
			if (getFile() != null) {
				throw new ActionValidationException("Cannot define both name and file", this);
			}
			if (getValue() == null) {
				throw new ActionValidationException("Value is required when name is provided", this);
			}
		}
	}

	@Override
	protected String getActionName() {
		return "Add";
	}

	public String getAfter() {
		return StringUtils.trimToNull(after);
	}
	
	public void setAfter(String after) {
		this.after = after;
	}

	public String getBefore() {
		return StringUtils.trimToNull(before);
	}
	
	public void setBefore(String before) {
		this.before = before;
	}
	
	public String getInside() {
		return StringUtils.trimToNull(inside);
	}
	
	public void setInside(String inside) {
		this.inside = inside;
	}
	
	public String getFile() {
		return StringUtils.trimToNull(file);
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getAfter() == null) ? 0 : getAfter().hashCode());
		result = prime * result + ((getBefore() == null) ? 0 : getBefore().hashCode());
		result = prime * result + ((getInside() == null) ? 0 : getInside().hashCode());
		result = prime * result + ((getFile() == null) ? 0 : getFile().hashCode());
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

		if (getInside() == null) {
			if (other.getInside() != null) {
				return false;
			}
		} else if (!getInside().equals(other.getInside())) {
			return false;
		}

		if (getFile() == null) {
			if (other.getFile() != null) {
				return false;
			}
		} else if (!getFile().equals(other.getFile())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return getActionName() + " [name=" + getName() + ";value=" + getValue() + ";after=" + getAfter() + ";before=" + getBefore() + ";inside=" + getInside() + ";file=" + getFile() + "]";
	}
}
