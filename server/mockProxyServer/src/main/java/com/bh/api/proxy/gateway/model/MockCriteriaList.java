package com.bh.api.proxy.gateway.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class MockCriteriaList extends MockModeller {

	private static final long serialVersionUID = 1L;

	private String method;
	private String api;
	private String defaultResponse;

	private int httpResponseDefaultStatus;
	private MultiValueMap<String, String> httpResponseDefaultHeaders;
	private int delayPercentage;
	private int delayTime;

	private List<MockCriteria> mockCriteriaList;
	private DmnCriteria dmnCriteria;

	// engine variables
	private VelocityEngine velocityEngine;
	private StringResourceRepository stringResourceRepository;

	/**
	 * 
	 * @param jsonString
	 * @param request
	 * @return
	 */
	public MockResponseHolder setupMockResponseHolder(String jsonString, HttpServletRequest request) {
		return new MockResponseHolder(jsonString, request);
	}
	
	/**
	 * 
	 * @param defaultHeaders
	 * @param delayTime
	 * @param delayPercentage
	 */
	public void setOrUpdateDefaultParams(Map<String,String> defaultHeaders, int delayTime, int delayPercentage, int defaultResponse) {
		
	if(!MapUtils.isEmpty(defaultHeaders)){
		this.httpResponseDefaultHeaders = new LinkedMultiValueMap<String, String>();
		defaultHeaders.entrySet().stream().forEach(entryItem -> {
			this.httpResponseDefaultHeaders.add(entryItem.getKey(),entryItem.getValue());
		});
	}
		if(defaultResponse>0) {
			this.httpResponseDefaultStatus=defaultResponse;
		}else {
			this.httpResponseDefaultStatus=200;
		}
		this.delayTime=delayTime;
		this.delayPercentage=delayPercentage;
	}
	
	/**
	 * 
	 * @param method
	 * @param api
	 */
	public MockCriteriaList(String method, String api) {
		this.method = method;
		this.api = api;
		mockCriteriaList = new ArrayList<MockCriteria>();
		// extractedPaths = new HashMap<String, Object>();
		velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "string");
		velocityEngine.addProperty("string.resource.loader.repository.static", "false");
		velocityEngine.init();
		this.stringResourceRepository = (StringResourceRepository) this.velocityEngine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
		this.dmnCriteria = new DmnCriteria(this.velocityEngine, this.stringResourceRepository);
	}

	public synchronized MockCriteria buildAMockCriteria(int mockScenarioId) {
		MockCriteria mockCriteria = new MockCriteria(this.velocityEngine, this.stringResourceRepository);
		mockScenarioId = mockScenarioId + 1;
		mockCriteria.setMockCriteriaId(mockScenarioId);
		mockCriteriaList.add(mockCriteria);
		return mockCriteria;
	}

	/**
	 * 
	 * @return
	 */
	private boolean isDelayRequired() {
		return new Random().nextInt(100) <= delayPercentage;
	}

	/**
	 * 
	 * @param mockResponseHolder
	 * @return
	 */
	public MockReturnResponse processMockCriteriaWithDelayIfPresent(MockResponseHolder mockResponseHolder) {
		long startTime = System.currentTimeMillis();
		MockReturnResponse mockReturnResponse = processMockCriteria(mockResponseHolder);
		long endTime = System.currentTimeMillis();
		if (isDelayRequired()) {
			try {
				int processedTime = Math.toIntExact(endTime - startTime);
				int delayTime = getDerivedDelayTime() * 1000;
				if (delayTime > processedTime) {
					ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
					ScheduledFuture<Object> future = scheduledExecutorService.schedule(new Callable<Object>() {
						public Object call() {
							return "delay time executed";
						}
					}, (delayTime - processedTime), TimeUnit.MILLISECONDS);
					Object ob = future.get();
					scheduledExecutorService.shutdown();
					return mockReturnResponse;
				} else {
					return mockReturnResponse;
				}
			} catch (Exception e) {
				return mockReturnResponse;
			}
		} else {
			return mockReturnResponse;
		}

	}

	/**
	 * 
	 * @param mockResponseHolder
	 * @return
	 */
	private MockReturnResponse processMockCriteria(MockResponseHolder mockResponseHolder) {

		try {
			// 1. first for DMN..
			if (this.getDmnCriteria().hasDmnDecision()) {
				try {
					MockReturnResponse mockReturnResponse = new MockReturnResponse();
					mockReturnResponse.setHttpStatus(httpResponseDefaultStatus == 0 ? 200 : httpResponseDefaultStatus);
					mockReturnResponse.setResponseBody(this.getDmnCriteria().buildResponseIfDMNRunIsSuccessfull(mockResponseHolder));
					mockReturnResponse
							.setResponseHeaders(MapUtils.isEmpty(httpResponseDefaultHeaders) ? new LinkedMultiValueMap<>() : httpResponseDefaultHeaders);
					return mockReturnResponse;
				} catch (Exception e) {
					// eating the exception
				}
			}
			// 2. try for mock criterias,
			if (!CollectionUtils.isEmpty(mockCriteriaList)) {
				Optional<MockCriteria> optionalMockCriteria = mockCriteriaList.stream().filter(item -> {
					return item.hasAllPassed(mockResponseHolder);
				}).findFirst();
				if (optionalMockCriteria.isPresent()) {
					return addDefaultParamsToResponse(optionalMockCriteria.get().buildResponse(mockResponseHolder));
				}
			}
			// 3. else return response..
			return getDefaultMockReturnResponse();

		} catch (Exception e) {
			return getDefaultMockReturnResponse();
		}

	}

	/**
	 * 
	 * @param mockReturnResponse
	 * @return
	 */
	private MockReturnResponse addDefaultParamsToResponse(MockReturnResponse mockReturnResponse) {
		// set up the default response headers..
		if (MapUtils.isEmpty(mockReturnResponse.getResponseHeaders())) {
			mockReturnResponse.setResponseHeaders(this.httpResponseDefaultHeaders);
		} else {
			this.httpResponseDefaultHeaders.entrySet().stream().forEach(item -> {
				if (!mockReturnResponse.getResponseHeaders().containsKey(item.getKey())) {
					mockReturnResponse.getResponseHeaders().put(item.getKey(), item.getValue());
				}
			});
		}
		return mockReturnResponse;
	}

	/**
	 * create default response
	 * 
	 * @return
	 */
	private MockReturnResponse getDefaultMockReturnResponse() {
		MockReturnResponse mockReturnResponse = new MockReturnResponse();
		mockReturnResponse.setHttpStatus(httpResponseDefaultStatus == 0 ? 200 : httpResponseDefaultStatus);
		mockReturnResponse.setResponseBody(this.defaultResponse);
		mockReturnResponse.setResponseHeaders(MapUtils.isEmpty(httpResponseDefaultHeaders) ? new LinkedMultiValueMap<>() : httpResponseDefaultHeaders);
		return mockReturnResponse;
	}

	private int getDerivedDelayTime() {
		// get a random delay from 50% to 100% of the delay time
		return (new Random().nextInt(this.delayTime - this.delayTime / 2) + this.delayTime / 2);
	}
	
	public int getDelayTime() {
		return this.delayTime;
	}
	
	public class FlowCriteria {
		// class for child criteria
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getDefaultResponse() {
		return defaultResponse;
	}

	public void setDefaultResponse(String defaultResponse) {
		this.defaultResponse = defaultResponse;
	}

	public List<MockCriteria> getMockCriteriaList() {
		return mockCriteriaList;
	}

	public void setMockCriteriaList(List<MockCriteria> mockCriteriaList) {
		this.mockCriteriaList = mockCriteriaList;
	}

	public DmnCriteria getDmnCriteria() {
		return dmnCriteria;
	}

	public void setDmnCriteria(DmnCriteria dmnCriteria) {
		this.dmnCriteria = dmnCriteria;
	}
	public int getHttpResponseDefaultStatus() {
		return httpResponseDefaultStatus;
	}


	public MultiValueMap<String, String> getHttpResponseDefaultHeaders() {
		return httpResponseDefaultHeaders;
	}

	public int getDelayPercentage() {
		return delayPercentage;
	}
}
