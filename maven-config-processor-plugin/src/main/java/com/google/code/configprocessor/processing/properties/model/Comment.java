package com.google.code.configprocessor.processing.properties.model;

public class Comment implements PropertiesFileItem {
	
	private String text;
	
	public Comment(String text) {
		this.text = text;
	}
	
	public String getAsText() {
		return text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		Comment other = (Comment) obj;
		if (text == null) {
			if (other.text != null) {
				return false;
			}
		} else if (!text.equals(other.text)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return "Comment [" + text + "]";
	}
}
