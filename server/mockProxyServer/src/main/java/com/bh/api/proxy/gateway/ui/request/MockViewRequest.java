package com.bh.api.proxy.gateway.ui.request;

import java.net.URL;
import java.util.Arrays;

import com.bh.api.proxy.gateway.ui.ValidationException;

public class MockViewRequest {

	String api;
	String method;
	
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
	
public void validate_Api_Method_present() throws ValidationException {
		
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
	}
}
