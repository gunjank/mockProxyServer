package com.bh.api.proxy.gateway.ui.request;

import java.util.List;

public class ProxyRequest {

	String api;
	List<String> headerKey;
	List<String>  headerVal;
	List<String> pathVarKey;
	List<String> pathVarValue;
	String jsonBody;
	String method;
	
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	
	public List<String> getHeaderKey() {
		return headerKey;
	}
	public void setHeaderKey(List<String> headerKey) {
		this.headerKey = headerKey;
	}
	public List<String> getHeaderVal() {
		return headerVal;
	}
	public void setHeaderVal(List<String> headerVal) {
		this.headerVal = headerVal;
	}
	public List<String> getPathVarKey() {
		return pathVarKey;
	}
	public void setPathVarKey(List<String> pathVarKey) {
		this.pathVarKey = pathVarKey;
	}
	public List<String> getPathVarValue() {
		return pathVarValue;
	}
	public void setPathVarValue(List<String> pathVarValue) {
		this.pathVarValue = pathVarValue;
	}
	public String getJsonBody() {
		return jsonBody;
	}
	public void setJsonBody(String jsonBody) {
		this.jsonBody = jsonBody;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
}
