package com.bh.api.proxy.gateway.model;

import java.util.HashMap;
import java.util.Map;

public class MockResponse {

	String response;
	String template;
	Map<String, Object> templateContextMappings;
	
	public MockResponse(String response) {
		this.response = response;
		this.template = null;
		this.templateContextMappings = new HashMap<String,Object>();
	}
	
	public MockResponse(String response, String template, Map<String, Object> templateContextMappings) {
		this.response = response;
		this.template = template;
		this.templateContextMappings = templateContextMappings;
	}
	
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public Map<String, Object> getTemplateContextMappings() {
		return templateContextMappings;
	}
	public void setTemplateContextMappings(Map<String, Object> templateContextMappings) {
		this.templateContextMappings = templateContextMappings;
	}
	
}
