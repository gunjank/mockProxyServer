package com.bh.api.proxy.gateway.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.bh.api.proxy.gateway.model.MockCriteria;
import com.bh.api.proxy.gateway.model.MockCriteriaList;
import com.bh.api.proxy.gateway.model.MockModeller;
import com.bh.api.proxy.gateway.model.ObjectCache;
import com.bh.api.proxy.gateway.model.ObjectCacheKey;
import com.bh.api.proxy.gateway.ui.ValidationException;
import com.bh.api.proxy.gateway.ui.request.DmnSetupRequest;
import com.bh.api.proxy.gateway.ui.request.JsonPathRequest;
import com.bh.api.proxy.gateway.ui.request.MockDefaultTemplateRequest;
import com.bh.api.proxy.gateway.ui.request.MockSetupRequest;
import com.bh.api.proxy.gateway.ui.response.CompositeMockResponse;

@Component
public class ModellerService {

	@Autowired
	ObjectCache objectCache;

	/**
	 * Insert or update a default template with default headers, http status, http headers.
	 * @param mockSetupRequest
	 */
	public void insertOrUpdateDefaultTemplate(MockDefaultTemplateRequest mockSetupRequest) {

		try {
			MockCriteriaList mockCriteriaList = objectCache.getAvailableCriteriaList(mockSetupRequest.getApi(), mockSetupRequest.getMethod());
			mockCriteriaList.setDefaultResponse(mockSetupRequest.getTemplate());
			mockCriteriaList.setOrUpdateDefaultParams(mockSetupRequest.getDefaultResponseHeaders(), mockSetupRequest.getDelayTime(), mockSetupRequest.getDelayPercentage(), mockSetupRequest.getHttpStatusCode());
			objectCache.updateCriteriaList(mockCriteriaList);
			objectCache.syncCriteriaListToDB(mockCriteriaList);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert mock
	 * @param mockSetupRequest
	 */
	public int insertMocks(MockSetupRequest mockSetupRequest) throws Exception{

		try {
			MockCriteriaList mockCriteriaList = objectCache.getAvailableCriteriaList(mockSetupRequest.getApi(), mockSetupRequest.getMethod());
			MockCriteria mockCriteria = mockCriteriaList.buildAMockCriteria(mockCriteriaList.getMockCriteriaList().size()).setupTemplate(mockSetupRequest.getTemplate());
			mockCriteria.setOrUpdateRequestAndResponseHeaders(mockSetupRequest.getResponseHeaders(), mockSetupRequest.getHeaderMatchers(), mockSetupRequest.getHttpStatusCode());
			
			MockModeller modeller = new MockModeller();
			// 1.2.3 setup the nodes
			if (!CollectionUtils.isEmpty(mockSetupRequest.getJsonPathRequestList())) {
				mockSetupRequest.getJsonPathRequestList().forEach(item -> {
					mockCriteria.addToNodeMap(item.getJsonPath(),
							modeller.buildNode(item.getJsonPath(), item.getTemplateKey(), item.getAttributeType(), MockModeller.Type.JSON, MockModeller.Type.JSON, item.getType(),
									item.getToBeMatchedString()));
				});
			}

			// 1.3 update cache and DB..
			objectCache.updateCriteriaList(mockCriteriaList);
			objectCache.syncCriteriaListToDB(mockCriteriaList);
			return mockCriteria.getMockCriteriaId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Mock insert failed");
		}
	}

	/**
	 * Update mock
	 * @param mockSetupRequest
	 */
	public int updateMocks(MockSetupRequest mockSetupRequest) throws Exception{

		try {
			MockCriteriaList mockCriteriaList = objectCache.getAvailableCriteriaList(mockSetupRequest.getApi(), mockSetupRequest.getMethod());
			Optional<MockCriteria> toBeRemovedMockCriteria = mockCriteriaList.getMockCriteriaList().stream()
					.filter(mockCritieria -> (mockCritieria.getMockCriteriaId() == mockSetupRequest.getMockCriteriaId())).findFirst();
			if (toBeRemovedMockCriteria.isPresent()) {
				mockCriteriaList.getMockCriteriaList().remove(toBeRemovedMockCriteria.get());
			}

			MockCriteria mockCriteria = mockCriteriaList.buildAMockCriteria(mockCriteriaList.getMockCriteriaList().size()).setupTemplate(mockSetupRequest.getTemplate());
			mockCriteria.setOrUpdateRequestAndResponseHeaders(mockSetupRequest.getResponseHeaders(),mockSetupRequest.getHeaderMatchers(), mockSetupRequest.getHttpStatusCode());
			MockModeller modeller = new MockModeller();
			// 1.2.3 setup the nodes
			if (!CollectionUtils.isEmpty(mockSetupRequest.getJsonPathRequestList())) {
				mockSetupRequest.getJsonPathRequestList().forEach(item -> {
					mockCriteria.addToNodeMap(item.getJsonPath(),
							modeller.buildNode(item.getJsonPath(), item.getTemplateKey(), item.getAttributeType(), MockModeller.Type.JSON, MockModeller.Type.JSON, item.getType(),
									item.getToBeMatchedString()));
				});
			}

			// 1.3 update cache and DB.
			objectCache.updateCriteriaList(mockCriteriaList);
			objectCache.syncCriteriaListToDB(mockCriteriaList);
			return mockCriteria.getMockCriteriaId();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Mock insert failed");
		}
	}

	/**
	 * 
	 * @param mockSetupRequest
	 */
	public void deleteMocks(MockSetupRequest mockSetupRequest) {
		try {
			MockCriteriaList mockCriteriaList = objectCache.getAvailableCriteriaList(mockSetupRequest.getApi(), mockSetupRequest.getMethod());
			Optional<MockCriteria> toBeRemovedMockCriteria = mockCriteriaList.getMockCriteriaList().stream()
					.filter(mockCritieria -> (mockCritieria.getMockCriteriaId() == mockSetupRequest.getMockCriteriaId())).findFirst();
			if (toBeRemovedMockCriteria.isPresent()) {
				mockCriteriaList.getMockCriteriaList().remove(toBeRemovedMockCriteria.get());
			}
			// 1.3 update cache and DB.
			objectCache.updateCriteriaList(mockCriteriaList);
			objectCache.syncCriteriaListToDB(mockCriteriaList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param api
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public CompositeMockResponse viewMocks(String api, String method) throws Exception {
		
		CompositeMockResponse compositeMockResponse = new CompositeMockResponse();
		Optional<Entry<ObjectCacheKey, MockCriteriaList>>  optionalMockCriteriaList= objectCache.getObjectCacheMap().entrySet().stream().filter(item-> {
			return (item.getValue().getApi().equals(api) && item.getValue().getMethod().equals(method));
		}).findFirst();
		
		if(optionalMockCriteriaList.isPresent()) {
			MockCriteriaList mockCriteriaList = optionalMockCriteriaList.get().getValue();
			
			compositeMockResponse.setApi(mockCriteriaList.getApi());
			compositeMockResponse.setMethod(mockCriteriaList.getMethod());
			compositeMockResponse.setDefaultResponse(mockCriteriaList.getDefaultResponse());
			compositeMockResponse.setDelayTime(mockCriteriaList.getDelayTime());
			compositeMockResponse.setDelayPercentage(mockCriteriaList.getDelayPercentage());
			compositeMockResponse.setHttpStatus(mockCriteriaList.getHttpResponseDefaultStatus());
			
			Map<String,String> defaultHttpHeaders = new HashMap<String,String>();
			try {
				if(!MapUtils.isEmpty(mockCriteriaList.getHttpResponseDefaultHeaders()))
					mockCriteriaList.getHttpResponseDefaultHeaders().entrySet().stream().forEach(item-> {
						defaultHttpHeaders.put(item.getKey(),item.getValue().get(0));
					});
			}catch(Exception e) {
				//do nothing, since worst case, header value will not be shown..
			}
			compositeMockResponse.setDefaultHeaders(defaultHttpHeaders);
			
			List<MockSetupRequest> listOfCriterias = new ArrayList<MockSetupRequest>();
			
			if(!CollectionUtils.isEmpty(mockCriteriaList.getMockCriteriaList())){
				mockCriteriaList.getMockCriteriaList().forEach(criteria -> {
					
					MockSetupRequest mockSetupRequest = new MockSetupRequest();
					mockSetupRequest.setApi(mockCriteriaList.getApi());
					mockSetupRequest.setMethod(mockCriteriaList.getMethod());
					mockSetupRequest.setTemplate(criteria.getTemplateString());
					mockSetupRequest.setMockCriteriaId(criteria.getMockCriteriaId());
					mockSetupRequest.setHttpStatusCode(criteria.getHttpResponseStatus());
					Map<String,String> criteriaHttpHeaders = new HashMap<String,String>();
					try {
						if(!MapUtils.isEmpty(criteria.getHttpResponseHeaders()))
							criteria.getHttpResponseHeaders().entrySet().stream().forEach(item-> {
								criteriaHttpHeaders.put(item.getKey(),item.getValue().get(0));
							});
					}catch(Exception e) {
						//do nothing, since worst case, header value will not be shown..
					}
					mockSetupRequest.setResponseHeaders(criteriaHttpHeaders);
					mockSetupRequest.setHeaderMatchers(criteria.getRequestHeaderMatchers());
					
					List<JsonPathRequest> jsonPathRequestList = new ArrayList<JsonPathRequest>();
						
					if(!MapUtils.isEmpty(criteria.getNodeMap())){
						criteria.getNodeMap().values().stream().forEach(node -> {
							
							JsonPathRequest jsonPathRequest = new JsonPathRequest();
							jsonPathRequest.setAttributeType(node.getAttributeType());
							jsonPathRequest.setJsonPath(node.getPath());
							jsonPathRequest.setTemplateKey(node.getTemplateKey());
							jsonPathRequest.setToBeMatchedString(node.getToBeMatchedValue());
							jsonPathRequest.setType(node.getMatcherType().toString());
							
							jsonPathRequestList.add(jsonPathRequest);
						});
					}
					mockSetupRequest.setJsonPathRequestList(jsonPathRequestList);
					listOfCriterias.add(mockSetupRequest);
				});
			}
			compositeMockResponse.setMockCriteriaList(listOfCriterias);
			return compositeMockResponse;
		}else {
			throw new Exception("Mocks not found");
		}
		
		
	}
	
	/**
	 * 
	 * @param api
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public List<ObjectCacheKey> searchMocks(String api) throws Exception {
		
		List<ObjectCacheKey> listOfObjectCacheKey = new ArrayList<ObjectCacheKey>();
		objectCache.getObjectCacheMap().keySet().stream().forEach(objectCacheKey -> {
			if(objectCacheKey.getApi().contains(api)){
				listOfObjectCacheKey.add(objectCacheKey);
			}
		});
		
		return listOfObjectCacheKey;
	}
	
	/**
	 * 
	 * @param api
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public CompositeMockResponse viewWhetherMockTemplateExist(String api, String method) throws Exception {
		MockCriteriaList mockCriteriaList = objectCache.getCriteriaList(api, method);
		if(!StringUtils.isBlank(mockCriteriaList.getDefaultResponse())) {
			
			CompositeMockResponse compositeMockResponse = new CompositeMockResponse();
			compositeMockResponse.setDefaultResponse(mockCriteriaList.getDefaultResponse());
			compositeMockResponse.setDelayTime(mockCriteriaList.getDelayTime());
			compositeMockResponse.setDelayPercentage(mockCriteriaList.getDelayPercentage());
			compositeMockResponse.setHttpStatus(mockCriteriaList.getHttpResponseDefaultStatus());
			
			Map<String,String> defaultHttpHeaders = new HashMap<String,String>();
			try {
				if(!MapUtils.isEmpty(mockCriteriaList.getHttpResponseDefaultHeaders()))
					mockCriteriaList.getHttpResponseDefaultHeaders().entrySet().stream().forEach(item-> {
						defaultHttpHeaders.put(item.getKey(),item.getValue().get(0));
					});
			}catch(Exception e) {
				//do nothing, since worst case, header value will not be shown..
			}
			compositeMockResponse.setDefaultHeaders(defaultHttpHeaders);
			return compositeMockResponse;
		}else {
			throw new ValidationException("Default Template does not exist for the key:" + new ObjectCacheKey(api, method), "CRITERIA_LIST_NOT_EXISTS_EXCEPTION");
		}
		
	}
	
	/**
	 * 
	 * @param file
	 * @param api
	 * @param method
	 * @throws Exception
	 */
	public void insertDMNCriteria(MultipartFile file, String api, String method) throws Exception{
		try {
			MockCriteriaList mockCriteriaList = objectCache.getCriteriaList(api, method);
			mockCriteriaList.getDmnCriteria().setupDmnEngineAndDecision(file);
		}catch(Exception e) {
			throw new ValidationException("Unable to insert dmn criteria:" + new ObjectCacheKey(api, method), "DMN_CRITERIA_EXCEPTION");
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param api
	 * @param method
	 * @throws Exception
	 */
	public void insertDMNTemplateMapping(MultipartFile file, String api, String method) throws Exception{
		try {
			MockCriteriaList mockCriteriaList = objectCache.getAvailableCriteriaList(api, method);
			mockCriteriaList.getDmnCriteria().setupDmnEngineAndDecision(file);
		}catch(Exception e) {
			throw new ValidationException("Unable to insert dmn criteria:" + new ObjectCacheKey(api, method), "DMN_CRITERIA_EXCEPTION");
		}
	}

	/**
	 * 
	 * @param dmnSetupRequest
	 * @throws Exception
	 */
	public void insertDmnTemplateAndJsonPathMapping(DmnSetupRequest dmnSetupRequest) throws Exception{
		try {
			MockCriteriaList mockCriteriaList = objectCache.getAvailableCriteriaList(dmnSetupRequest.getApi(), dmnSetupRequest.getMethod()); 
			mockCriteriaList.getDmnCriteria().setupTemplateAndJsonPathAndTemplateMapping(dmnSetupRequest.getTemplate(), dmnSetupRequest.getDmnJsonPathKeyMapping());
		}catch(Exception e) {
			throw new ValidationException("Unable to insert dmn criteria:" + new ObjectCacheKey(dmnSetupRequest.getApi(), dmnSetupRequest.getMethod()), "DMN_CRITERIA_EXCEPTION");
		}
	}
}
