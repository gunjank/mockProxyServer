package com.bh.api.proxy.gateway.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;

public class ResponseCache {

	private static ResponseCache responseCache;
	private Map<ResponseCacheKey, Map<MockRequest, MockResponse>> responseCacheMap;

	private ResponseCache() {
		responseCacheMap = new ConcurrentHashMap<ResponseCacheKey, Map<MockRequest, MockResponse>>();
	}
	
	public static ResponseCache initialize() {
		if (null == responseCache) {
			responseCache = new ResponseCache();
			responseCache.refreshDataFromDB();
			return responseCache;
		} else {
			return responseCache;
		}
	}

	public synchronized void refreshDataFromDB() {
		// refresh the data from the Data base..
	}

	public void syncCacheToDB() {
		// refresh the cache data to the Data base..
	}

	public void syncCriteriaListToDB(MockCriteriaList mockCriteriaList) {
		// refresh the criteria list to DB
	}
	
	public void addOrUpdateDataToResponseCache(String api, String method, MockRequest mockRequest, MockResponse mockResponse) {
		if(MapUtils.isEmpty(responseCacheMap.get(new ResponseCacheKey(api,method)))){
			Map<MockRequest, MockResponse> requestResponseMapping = new HashMap<MockRequest, MockResponse>();
			requestResponseMapping.put(mockRequest, mockResponse);
			responseCacheMap.put(new ResponseCacheKey(api,method), requestResponseMapping);
		}else {
			responseCacheMap.get(new ResponseCacheKey(api,method)).put(mockRequest, mockResponse);
		}
	} 
	
	public Optional<MockResponse> getAMockResponseFromCacheIfExist(String api, String method, MockRequest mockRequest) {
		Optional<MockResponse> optionalResponse = Optional.empty(); 
		if(!MapUtils.isEmpty(responseCacheMap.get(new ResponseCacheKey(api,method)))){
			if(responseCacheMap.get(new ResponseCacheKey(api,method)).containsKey(mockRequest)) {
				optionalResponse =  Optional.of(responseCacheMap.get(new ResponseCacheKey(api,method)).get(mockRequest));
			}
		}
		return optionalResponse;
	}
	
}
