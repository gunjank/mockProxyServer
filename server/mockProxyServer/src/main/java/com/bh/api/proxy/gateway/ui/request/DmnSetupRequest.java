package com.bh.api.proxy.gateway.ui.request;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.bh.api.proxy.gateway.model.DMNJsonPathKey;
import com.bh.api.proxy.gateway.ui.ValidationException;

public class DmnSetupRequest {

	private Map<String, DMNJsonPathKey> dmnJsonPathKeyMapping;
	private String api;
	private String method;
	private String template;

	public Map<String, DMNJsonPathKey> getDmnJsonPathKeyMapping() {
		return dmnJsonPathKeyMapping;
	}

	public void setDmnJsonPathKeyMapping(Map<String, DMNJsonPathKey> dmnJsonPathKeyMapping) {
		this.dmnJsonPathKeyMapping = dmnJsonPathKeyMapping;
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

	public void validateRequest() throws ValidationException {
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
		if (StringUtils.isEmpty(this.template)) {
			throw new ValidationException("Response mapping cannot be blank", "TEMPLATE_NOT_VALID_EXCEPTION");
		}

		// check jsonaApping
		if (MapUtils.isEmpty(this.dmnJsonPathKeyMapping)) {
			throw new ValidationException("Response mapping cannot be blank", "JSONPATH_MAPPING_NOT_VALID_EXCEPTION");
		}

	}
}
