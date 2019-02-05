package com.bh.api.proxy.gateway.model;

import java.util.function.Function;

import com.bh.api.proxy.gateway.ui.request.AllMatcherType.MatcherType;

public class Node extends MockModeller{

	private static final long serialVersionUID = 1L;
	
	private int nodeId;
	private String path;
	private AttributeType attributeType;
	private Type requestType;
	private Type responseType;
	private String toBeMatchedValue;
	private MatcherType matcherType;
	private String toBeMatchedValueForViewer;
	private Function<Object, Boolean> ruleFunction;
	private String templateKey;
	
	public Node(String path, String templateKey, AttributeType attributeType, Type requestType, Type responseType, MatcherType matcherType, String tobeMatchedValue) {

		this.path = path;
		this.requestType = requestType;
		this.responseType = responseType;
		this.toBeMatchedValue = tobeMatchedValue;
		this.matcherType=matcherType;
		this.attributeType=attributeType;
		this.templateKey = templateKey;
	}
	
	/**
	 * 
	 * @param mockResponseHolder
	 * @return
	 */
	public boolean runRule(MockResponseHolder mockResponseHolder) {

		try {
			if (null == mockResponseHolder.getExtractedPaths().get(this.path)) {
				mockResponseHolder.getExtractedPaths().put(this.path, mockResponseHolder.getJsonDocumentContext().read(this.path));
			}
			mockResponseHolder.getExtractedPathForTemplateKey().put(templateKey, mockResponseHolder.getExtractedPaths().get(this.path));
			return ruleFunction.apply(mockResponseHolder.getExtractedPaths().get(this.path));
		}catch(Exception e) {
			return false;
		}
	}
	
	public AttributeType getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public String getToBeMatchedValueForViewer() {
		return toBeMatchedValueForViewer;
	}

	public void setToBeMatchedValueForViewer(String toBeMatchedValueForViewer) {
		this.toBeMatchedValueForViewer = toBeMatchedValueForViewer;
	}

	public MatcherType getMatcherType() {
		return matcherType;
	}

	public void setMatcherType(MatcherType matcherType) {
		this.matcherType = matcherType;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Type getRequestType() {
		return requestType;
	}

	public void setRequestType(Type requestType) {
		this.requestType = requestType;
	}

	public Type getResponseType() {
		return responseType;
	}

	public void setResponseType(Type responseType) {
		this.responseType = responseType;
	}

	public String getToBeMatchedValue() {
		return toBeMatchedValue;
	}

	public void setToBeMatchedValue(String toBeMatchedValue) {
		this.toBeMatchedValue = toBeMatchedValue;
	}
	
	public Function<Object, Boolean> getRuleFunction() {
		return ruleFunction;
	}

	public void setRuleFunction(Function<Object, Boolean> ruleFunction) {
		this.ruleFunction = ruleFunction;
	}
	
	public String getTemplateKey() {
		return templateKey;
	}

	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}
}
