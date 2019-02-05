package com.bh.api.proxy.gateway.model;

import java.io.Serializable;

public class ObjectCacheKey implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String api;
	private String method;
	
	public ObjectCacheKey(String api, String method) {
		this.api = api;
		this.method = method;
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
	
	@Override
	public String toString() {
		return "ObjectCacheKey { api:" + api + ", method:" + method + "}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((api == null) ? 0 : api.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectCacheKey other = (ObjectCacheKey) obj;
		if (api == null) {
			if (other.api != null)
				return false;
		} else if (!api.equals(other.api))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	
	
}
