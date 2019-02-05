package com.bh.api.proxy.gateway.ui.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.bh.api.proxy.gateway.ui.request.MockSetupRequest;

public class CompositeMockResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	String api;
	String method;
	String defaultResponse;
	List<MockSetupRequest> mockCriteriaList;
	int httpStatus;
	Map<String,String> defaultHeaders;
	int delayTime;
	int delayPercentage;
	
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getDefaultResponse() {
		return defaultResponse;
	}
	public void setDefaultResponse(String defaultResponse) {
		this.defaultResponse = defaultResponse;
	}
	public List<MockSetupRequest> getMockCriteriaList() {
		return mockCriteriaList;
	}
	public void setMockCriteriaList(List<MockSetupRequest> mockCriteriaList) {
		this.mockCriteriaList = mockCriteriaList;
	}
	public Map<String, String> getDefaultHeaders() {
		return defaultHeaders;
	}
	public void setDefaultHeaders(Map<String, String> defaultHeaders) {
		this.defaultHeaders = defaultHeaders;
	}
	public int getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	public int getDelayPercentage() {
		return delayPercentage;
	}
	public void setDelayPercentage(int delayPercentage) {
		this.delayPercentage = delayPercentage;
	}
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

}
