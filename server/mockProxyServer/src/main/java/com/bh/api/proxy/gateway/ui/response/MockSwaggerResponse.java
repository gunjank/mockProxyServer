package com.bh.api.proxy.gateway.ui.response;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public class MockSwaggerResponse {

	List<SwaggerResponse> swaggerResponseList;

	public List<SwaggerResponse> getSwaggerResponseList() {
		return swaggerResponseList;
	}

	public void setSwaggerResponseList(List<SwaggerResponse> swaggerResponseList) {
		this.swaggerResponseList = swaggerResponseList;
	}

	public void addToList(SwaggerResponse swaggerResponse) {
		if (CollectionUtils.isEmpty(this.swaggerResponseList)) {
			this.swaggerResponseList = new ArrayList<SwaggerResponse>();
		}
		this.swaggerResponseList.add(swaggerResponse);
	}

	@Override
	public String toString() {
		return "MockSwaggerResponse [swaggerResponseList=" + swaggerResponseList + "]";
	}


}
