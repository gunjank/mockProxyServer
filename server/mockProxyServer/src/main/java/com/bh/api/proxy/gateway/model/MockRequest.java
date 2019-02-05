package com.bh.api.proxy.gateway.model;

import java.util.HashMap;
import java.util.Map;

public class MockRequest {

	public MockRequest(String request, Map<String, Object> processedJsonPaths) {
		this.request = request;
		this.processedJsonPaths = processedJsonPaths;
	}
	
	public MockRequest(String request) {
		this.request = request;
		this.processedJsonPaths = new HashMap<String,Object>();
	}
	
	String request;
	Map<String, Object> processedJsonPaths;
	
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public Map<String, Object> getProcessedJsonPaths() {
		return processedJsonPaths;
	}
	public void setProcessedJsonPaths(Map<String, Object> processedJsonPaths) {
		this.processedJsonPaths = processedJsonPaths;
	}
	
	
}
