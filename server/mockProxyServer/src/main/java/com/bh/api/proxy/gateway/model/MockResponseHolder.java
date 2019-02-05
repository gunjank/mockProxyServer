package com.bh.api.proxy.gateway.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class MockResponseHolder {

	private Map<String, Object> extractedPaths;
	private Map<String, Object> extractedPathForTemplateKey;
	private Object jsonObjectForMatcher;
	private DocumentContext jsonDocumentContext;
	private Map<String, String> requestHeaders;
	
	public MockResponseHolder(String jsonString, HttpServletRequest request){
		this.jsonObjectForMatcher = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
		this.jsonDocumentContext = JsonPath.parse(jsonString);
		this.extractedPaths = new HashMap<String, Object>();
		this.extractedPathForTemplateKey = new HashMap<String, Object>();
		populateRequestHeaders(request);
	}
	
	private void populateRequestHeaders(HttpServletRequest request) {
		this.requestHeaders = new HashMap<String,String>();
		if(null != request.getHeaderNames() && Collections.list(request.getHeaderNames()).size()>0) {
			this.requestHeaders = Collections.list(((HttpServletRequest) request).getHeaderNames()).stream().collect(Collectors.toMap(header-> header,  request::getHeader));
		}
	}
	
	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Map<String, String> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public Map<String, Object> getExtractedPaths() {
		return extractedPaths;
	}
	public void setExtractedPaths(Map<String, Object> extractedPaths) {
		this.extractedPaths = extractedPaths;
	}
	public Object getJsonObjectForMatcher() {
		return jsonObjectForMatcher;
	}
	public void setJsonObjectForMatcher(Object jsonObjectForMatcher) {
		this.jsonObjectForMatcher = jsonObjectForMatcher;
	}
	public DocumentContext getJsonDocumentContext() {
		return jsonDocumentContext;
	}
	public void setJsonDocumentContext(DocumentContext jsonDocumentContext) {
		this.jsonDocumentContext = jsonDocumentContext;
	}
	
	public Map<String, Object> getExtractedPathForTemplateKey() {
		return extractedPathForTemplateKey;
	}

	public void setExtractedPathForTemplateKey(Map<String, Object> extractedPathForTemplateKey) {
		this.extractedPathForTemplateKey = extractedPathForTemplateKey;
	}
	
	
}
