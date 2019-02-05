package com.bh.api.proxy.gateway.model;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.MapUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.bh.api.proxy.gateway.velocityExtensions.BankingFormatTool;

public class MockCriteria extends MockModeller {

	private static final long serialVersionUID = 1L;

	// this will serve as the primary key for the mock criteria..
	private int mockCriteriaId;

	private int httpResponseStatus;
	private MultiValueMap<String, String> httpResponseHeaders;

	private Map<String, String> requestHeaderMatchers;
	private Map<String, Node> nodeMap;
	private Template template;
	private StringResourceRepository stringResourceRepository;
	private VelocityEngine velocityEngine;
	private String templateString;

	
	/**
	 * 
	 * @param defaultHeaders
	 * @param delayTime
	 * @param delayPercentage
	 */
	public synchronized void setOrUpdateRequestAndResponseHeaders(Map<String,String> defaultResponseHeaders, Map<String,String> requestHeaderMatchers, int httpResponseStatus) {
		this.httpResponseHeaders = new LinkedMultiValueMap<String, String>();
		this.requestHeaderMatchers = new HashMap<String,String>();
		
		defaultResponseHeaders.entrySet().stream().forEach(entryItem -> {
				this.httpResponseHeaders.add(entryItem.getKey(),entryItem.getValue());
		});
		requestHeaderMatchers.entrySet().stream().forEach(entryItem -> {
			this.requestHeaderMatchers.put(entryItem.getKey(),entryItem.getValue());
	});
		this.httpResponseStatus= httpResponseStatus;
	}
	
	/**
	 * 
	 * @param templateString
	 * @return
	 */
	public synchronized MockCriteria setupTemplate(String templateString) {
		this.templateString = templateString;
		this.stringResourceRepository.putStringResource(this.hashCode() + "", templateString);
		this.template = velocityEngine.getTemplate(this.hashCode() + "");
		return this;
	}

	public int getMockCriteriaId() {
		return mockCriteriaId;
	}

	public void setMockCriteriaId(int mockCriteriaId) {
		this.mockCriteriaId = mockCriteriaId;
	}

	public MockCriteria(VelocityEngine velocityEngine, StringResourceRepository stringResourceRepository) {
		this.stringResourceRepository = stringResourceRepository;
		this.velocityEngine = velocityEngine;
	}

	public void addToNodeMap(String jsonPath, Node Node) {
		if (null == this.nodeMap) {
			nodeMap = new HashMap<String, Node>();
		}
		nodeMap.put(jsonPath, Node);
	}

	/**
	 * 
	 * @param mockResponseHolder
	 * @return
	 */
	public boolean hasAllPassed(MockResponseHolder mockResponseHolder) {

		// check the headers, if there needs to run anything from headers..
		try {
			if (!MapUtils.isEmpty(this.getRequestHeaderMatchers())) {
				if (this.getRequestHeaderMatchers().entrySet().stream().filter(headerMatcherEntry -> {
					return !(headerMatcherEntry.getValue().equals(mockResponseHolder.getRequestHeaders().get(headerMatcherEntry.getValue())));
				}).findFirst().isPresent()) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}

		// then run each nodes to check..
		Optional<Node> processedItems = nodeMap.values().stream().filter(node -> {
			return !node.runRule(mockResponseHolder);
		}).findFirst();

		if (processedItems.isPresent()) {
			Node node = processedItems.get();
			System.out.println("First Match failed for Path:: " + node.getPath() + " ,for value::" + node.getToBeMatchedValue());
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Build the Mock Return response
	 * @param mockResponseHolder
	 * @return
	 */
	public MockReturnResponse buildResponse(MockResponseHolder mockResponseHolder) {
		
		MockReturnResponse mockReturnResponse = new MockReturnResponse();
		// 1. get the template
		Template template = getTemplate();

		VelocityContext context = new VelocityContext();
		mockResponseHolder.getExtractedPathForTemplateKey().entrySet().forEach(entrySet -> {
			context.put(entrySet.getKey(), entrySet.getValue());
		});

		// 3. create the return string.
		context.put("mock_math", new MathTool());
		context.put("mock_banking", new BankingFormatTool());
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		String response = writer.toString().replaceAll("\\s{2,}", "");
		response = response.replaceAll(",,,", ",");
		response = response.replaceAll(",,", ",");
		response = response.replaceAll(",}", "}");
		response = response.replaceAll(",]", "]");
		mockReturnResponse.setResponseBody(response);
		mockReturnResponse.setHttpStatus(this.httpResponseStatus);
		mockReturnResponse.setResponseHeaders(httpResponseHeaders);
		
		return mockReturnResponse;
	}

	
	public Map<String, String> getRequestHeaderMatchers() {
		return requestHeaderMatchers;
	}

	public void setRequestHeaderMatchers(Map<String, String> requestHeaderMatchers) {
		this.requestHeaderMatchers = requestHeaderMatchers;
	}

	private Template getTemplate() {
		return this.template;
	}

	public String getTemplateString() {
		return templateString;
	}

	public void setTemplateString(String templateString) {
		this.templateString = templateString;
	}

	public Map<String, Node> getNodeMap() {
		return nodeMap;
	}
	
	public int getHttpResponseStatus() {
		return httpResponseStatus;
	}

	public void setHttpResponseStatus(int httpResponseStatus) {
		this.httpResponseStatus = httpResponseStatus;
	}

	public MultiValueMap<String, String> getHttpResponseHeaders() {
		return httpResponseHeaders;
	}

	public void setHttpResponseHeaders(MultiValueMap<String, String> httpResponseHeaders) {
		this.httpResponseHeaders = httpResponseHeaders;
	}
}
