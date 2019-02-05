package com.bh.api.proxy.gateway.ui.response;

import java.io.Serializable;
import java.util.List;

public class CollectionMockResponse implements Serializable{

	private static final long serialVersionUID = 1L;
	
	List<CompositeMockResponse> listOfCompositeMockResponse;

	public List<CompositeMockResponse> getListOfCompositeMockResponse() {
		return listOfCompositeMockResponse;
	}

	public void setListOfCompositeMockResponse(List<CompositeMockResponse> listOfCompositeMockResponse) {
		this.listOfCompositeMockResponse = listOfCompositeMockResponse;
	}
}
