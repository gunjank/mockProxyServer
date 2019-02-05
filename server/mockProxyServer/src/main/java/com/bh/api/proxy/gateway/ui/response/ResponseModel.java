package com.bh.api.proxy.gateway.ui.response;

public class ResponseModel{
	String statusCode;
	String response;
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	@Override
	public String toString() {
		return "ResponseModel [statusCode=" + statusCode + ", response=" + response + "]";
	}
}