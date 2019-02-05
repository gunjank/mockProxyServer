package com.bh.api.proxy.gateway.ui.request;

import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.bh.api.proxy.gateway.ui.ValidationException;

public class MockSetupRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	String api;
	String method;
	String template;
	List<JsonPathRequest> jsonPathRequestList;
	int mockCriteriaId;
	Map<String,String> headerMatchers;
	Map<String,String> responseHeaders;
	int httpStatusCode;
	
	public Map<String, String> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Map<String, String> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public int getMockCriteriaId() {
		return mockCriteriaId;
	}

	public void setMockCriteriaId(int mockCriteriaId) {
		this.mockCriteriaId = mockCriteriaId;
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

	public List<JsonPathRequest> getJsonPathRequestList() {
		return jsonPathRequestList;
	}

	public void setJsonPathRequestList(List<JsonPathRequest> jsonPathRequestList) {
		this.jsonPathRequestList = jsonPathRequestList;
	}
	
	public Map<String, String> getHeaderMatchers() {
		return headerMatchers;
	}

	public void setHeaderMatchers(Map<String, String> headerMatchers) {
		this.headerMatchers = headerMatchers;
	}

	public enum RequestType {
		STRING, BOOLEAN, ARRAY, OBJECT
	}
	
	public void validateRequest() throws ValidationException {

		// check url
		try {
			URL url = new URL(this.api);
			this.api = url.getPath();
		}catch(Exception e) {
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

	public void validateIfMockCriteriaId_Api_Method_present() throws ValidationException {
		
		// check url
		try {
			URL url = new URL(this.api);
			this.api = url.getPath();
		}catch(Exception e) {
			throw new ValidationException("API is not valid, use 'http://domain_name/path1/path2' | supported protocols are http, https",
					"URI_NOT_VALID_EXCEPTION");
		}
		// check method
		String[] methods = { "GET", "POST", "PUT", "DELETE" };
		if (!Arrays.asList(methods).contains(this.method)) {
			throw new ValidationException("METHOD is not valid,use either of GET,POST,PUT,DELETE", "METHOD_NOT_VALID_EXCEPTION");
		}
		if (this.mockCriteriaId > 0) {
			throw new ValidationException("Mock CriteriaId cannot be 0 or empty", "ID_NOT_VALID_EXCEPTION");
		}
	}


}
