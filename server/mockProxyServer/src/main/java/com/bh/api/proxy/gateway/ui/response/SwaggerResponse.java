package com.bh.api.proxy.gateway.ui.response;

import java.util.List;

public class SwaggerResponse{
	
	public String api;
	public String method;
	public List<String> requestModel;
	public List<ResponseModel> responseModel;
	
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
	public List<String> getRequestModel() {
		return requestModel;
	}
	public void setRequestModel(List<String> requestModel) {
		this.requestModel = requestModel;
	}
	public List<ResponseModel> getResponseModel() {
		return responseModel;
	}
	public void setResponseModel(List<ResponseModel> responseModel) {
		this.responseModel = responseModel;
	}
	
	@Override
	public String toString() {
		return "SwaggerResponse [api=" + api + ", method=" + method + ", requestModel=" + requestModel + ", responseModel=" + responseModel + "]";
	}
}
