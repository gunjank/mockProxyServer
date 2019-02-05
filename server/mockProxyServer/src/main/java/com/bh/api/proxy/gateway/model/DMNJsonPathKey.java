package com.bh.api.proxy.gateway.model;

public class DMNJsonPathKey {
	
	String jsonPath;
	String templateKey;
	DMNKeyType type;

	public String getJsonPath() {
		return jsonPath;
	}

	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}

	public String getTemplateKey() {
		return templateKey;
	}

	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}

	public DMNKeyType getType() {
		return type;
	}

	public void setType(DMNKeyType type) {
		this.type = type;
	}

	public enum DMNKeyType{
		INPUT, OUTPUT
	}
}
