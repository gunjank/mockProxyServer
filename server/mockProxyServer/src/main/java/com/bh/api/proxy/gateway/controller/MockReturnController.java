package com.bh.api.proxy.gateway.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bh.api.proxy.gateway.model.MockCriteriaList;
import com.bh.api.proxy.gateway.model.ObjectCache;
import com.bh.api.proxy.gateway.model.ResponseCache;

@RestController
public class MockReturnController {

	@Autowired
	ResponseCache responseCache;

	@Autowired
	ObjectCache objectCache;

	@Autowired
	HttpServletRequest request;

	@RequestMapping(value = "/mock/*/**", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getMockResponseForGET(HttpServletRequest request) {
		try {
			String requestUri = request.getRequestURI().substring("/mock".length());
			return objectCache.getCriteriaList(requestUri, request.getMethod()).processMockCriteriaWithDelayIfPresent(null).getResponseEntity();
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Mock setup is not available for the uri\"}",
					HttpStatus.NOT_FOUND);
		}

	}

	@RequestMapping(value = "/mock/*/**", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> getMockResponseForPOST(@RequestBody String mockRequest, HttpServletRequest request) throws Exception {
		try {
			String requestUri = request.getRequestURI().substring("/mock".length());
			MockCriteriaList mockCriteriaList = objectCache.getCriteriaList(requestUri, request.getMethod());
			return mockCriteriaList.processMockCriteriaWithDelayIfPresent(mockCriteriaList.setupMockResponseHolder(mockRequest, request)).getResponseEntity();

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Mock setup is not available for the uri\"}",
					HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/mock/*/**", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<?> getMockResponseForPUT(@RequestBody String mockRequest, HttpServletRequest request) throws Exception {
		try {
			String requestUri = request.getRequestURI().substring("/mock".length());
			MockCriteriaList mockCriteriaList = objectCache.getCriteriaList(requestUri, request.getMethod());
			return mockCriteriaList.processMockCriteriaWithDelayIfPresent(mockCriteriaList.setupMockResponseHolder(mockRequest, request)).getResponseEntity();

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Mock setup is not available for the uri\"}",
					HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/mock/*/**", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<?> getMockResponseForDELETE(@RequestBody String mockRequest, HttpServletRequest request) throws Exception {
		try {
			String requestUri = request.getRequestURI().substring("/mock".length());
			MockCriteriaList mockCriteriaList = objectCache.getCriteriaList(requestUri, request.getMethod());
			return mockCriteriaList.processMockCriteriaWithDelayIfPresent(mockCriteriaList.setupMockResponseHolder(mockRequest, request)).getResponseEntity();

		} catch (Exception e) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Mock setup is not available for the uri\"}",
					HttpStatus.NOT_FOUND);
		}
	}
}
