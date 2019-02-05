package com.bh.api.proxy.gateway.model;

import java.util.Map;

import com.bh.api.proxy.gateway.ui.response.CompositeMockResponse;

/**
 * will providce flexibility to create collection of apis..
 * @author sb63576
 *
 */
public class CollectionCache {

	Map<ObjectCacheKey,CompositeMockResponse> collectionMap;
	
}
