package com.bh.api.proxy.gateway.model;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.bh.api.proxy.gateway.ui.ValidationException;

public class ObjectCache implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private static ObjectCache objectCache;
	private Map<ObjectCacheKey, MockCriteriaList> objectCacheMap;

	private ObjectCache() {
		objectCacheMap = new ConcurrentHashMap<ObjectCacheKey, MockCriteriaList>();
	}

	public static ObjectCache initialize() {
		if (null == objectCache) {
			objectCache = new ObjectCache();
			objectCache.refreshDataFromDB();
			return objectCache;
		} else {
			return objectCache;
		}
	}
	
	public  Map<ObjectCacheKey, MockCriteriaList> getObjectCacheMap() {
		return this.objectCacheMap;
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

	public synchronized void addCriteriaList(MockCriteriaList mockCriteriaList) throws ValidationException {
		ObjectCacheKey key = new ObjectCacheKey(mockCriteriaList.getApi(), mockCriteriaList.getMethod());
		if (this.objectCacheMap.containsKey(key) && null != this.objectCacheMap.get(key)) {
			throw new ValidationException("Criteria List already exist for key:" + key, "CRITERIA_LIST_ALREADY_EXISTS_EXCEPTION");
		} else {
			this.objectCacheMap.put(key, mockCriteriaList);
		}
	}

	public synchronized MockCriteriaList getAvailableCriteriaList(String api, String method) throws ValidationException {
		ObjectCacheKey key = new ObjectCacheKey(api, method);
		if (!this.objectCacheMap.containsKey(key)) {
			MockCriteriaList mockCriteriaList = new MockCriteriaList(method, api);
			this.objectCacheMap.put(key, mockCriteriaList);
		}
		return this.objectCacheMap.get(key);
	}
	
	public synchronized MockCriteriaList getCriteriaList(String api, String method) throws ValidationException {
		ObjectCacheKey key = new ObjectCacheKey(api, method);
		if( !this.objectCacheMap.containsKey(key)) {
			throw new ValidationException("Criteria List does not exist for the key:" + key, "CRITERIA_LIST_NOT_EXISTS_EXCEPTION");
		}else {
			return this.objectCacheMap.get(key);
		}
	}
	

	public synchronized void updateCriteriaList(MockCriteriaList mockCriteriaList) throws ValidationException {
		ObjectCacheKey key = new ObjectCacheKey(mockCriteriaList.getApi(), mockCriteriaList.getMethod());
		if (!this.objectCacheMap.containsKey(key)) {
			throw new ValidationException("Criteria List does not exist for key:" + key, "CRITERIA_LIST_ALREADY_EXISTS_EXCEPTION");
		} else {
			this.objectCacheMap.put(key, mockCriteriaList);
		}
	}

	public synchronized void deleteACriteriaFromExistingList(int mockCriteriaId, String api, String method) throws ValidationException {
		ObjectCacheKey key = new ObjectCacheKey(api, method);
		if (!this.objectCacheMap.containsKey(key) && null != this.objectCacheMap.get(key)) {
			throw new ValidationException("Criteria not found", "CRITERIA_LIST_ALREADY_EXISTS_EXCEPTION");
		} else {
			this.objectCacheMap.put(key, (MockCriteriaList) this.objectCacheMap.get(key).getMockCriteriaList().stream()
					.filter(item -> !(item.getMockCriteriaId() == mockCriteriaId)).collect(Collectors.toList()));
		}
	}
}
