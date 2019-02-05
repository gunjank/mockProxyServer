package com.bh.api.proxy.gateway.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MockReturnResponse {

	String responseBody;
	int httpStatus;
	MultiValueMap<String, String> responseHeaders;
	
	public ResponseEntity<Object> getResponseEntity(){
		if(this.httpStatus == 0) {
			return new ResponseEntity<Object>(responseBody, responseHeaders, HttpStatus.OK);
		}else 
		return new ResponseEntity<Object>(responseBody, responseHeaders, HttpStatus.valueOf(httpStatus));
	}
	
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		try {
			HttpStatus.valueOf(httpStatus);
			this.httpStatus = httpStatus;
		}catch(Throwable t) {
			//setting http status to 200 in case of unknown http statuses.
			this.httpStatus = 200;
		}
	}

	public MultiValueMap<String, String> getResponseHeaders() {
		if(this.responseHeaders == null) {
			return new LinkedMultiValueMap<String, String>();
		}
		return responseHeaders;
	}

	public void setResponseHeaders(MultiValueMap<String, String> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	
}
