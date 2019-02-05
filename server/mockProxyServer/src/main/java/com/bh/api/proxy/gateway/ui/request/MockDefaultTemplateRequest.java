package com.bh.api.proxy.gateway.ui.request;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.bh.api.proxy.gateway.ui.ValidationException;

public class MockDefaultTemplateRequest {

	String api;
	String method;
	String template;
	Map<String,String> defaultResponseHeaders;
	int httpStatusCode;
	int delayTime;
	int delayPercentage;
	
	public Map<String, String> getDefaultResponseHeaders() {
		return defaultResponseHeaders;
	}

	public void setDefaultResponseHeaders(Map<String, String> defaultResponseHeaders) {
		this.defaultResponseHeaders = defaultResponseHeaders;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

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

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
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
	

	public void validateRequest() throws ValidationException {

		//making sure no junk percentage is entered..
		if(delayPercentage>100) {
			this.delayPercentage=100;
		}
		
		if(delayTime>6) {
			this.delayTime=6;
		}
		
		try {
			URL url = new URL(this.api);
			this.api = url.getPath();
		} catch (Exception e) {
			throw new ValidationException("API is not valid, use 'http://domain_name/path1/path2' | supported protocols are http, https",
					"URI_NOT_VALID_EXCEPTION");
		}
		// check method
		String[] methods = { "GET", "POST", "PUT", "DELETE" };
		if (!Arrays.asList(methods).contains(this.method)) {
			throw new ValidationException("METHOD is not valid,use either of GET,POST,PUT,DELETE", "METHOD_NOT_VALID_EXCEPTION");
		}
		// check template
		if (StringUtils.isBlank(this.template)) {
			throw new ValidationException("Response template cannot be blank", "TEMPLATE_NOT_VALID_EXCEPTION");
		}
	}

}
