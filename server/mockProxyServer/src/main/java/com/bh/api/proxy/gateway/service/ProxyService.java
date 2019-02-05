package com.bh.api.proxy.gateway.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ProxyService {

	public boolean validateIfProxyIsRequired() {
		return false;
	}
	
	public String fireProxyService(String mockRequest, HttpServletRequest request, HttpMethod method, String api) throws Exception{
		
		if(HttpMethod.POST.equals(method)){
			
			RestTemplate restTemplate= new RestTemplate();
			ResponseEntity<String> proxyResponse = restTemplate.exchange(
					api,
					HttpMethod.POST, getHttpEntity(getProxyHeaders(request), mockRequest.trim()), String.class);
			return proxyResponse.getBody();
			
		}else if(HttpMethod.GET.equals(method)){
			
			RestTemplate restTemplate= new RestTemplate();
			ResponseEntity<String> proxyResponse = restTemplate.exchange(
					api,
					HttpMethod.GET, getHttpEntity(getProxyHeaders(request), null), String.class);
			return proxyResponse.getBody();
		
		}else if(HttpMethod.POST.equals(method)) {
			
			RestTemplate restTemplate= new RestTemplate();
			ResponseEntity<String> proxyResponse = restTemplate.exchange(
					api,
					HttpMethod.PUT, getHttpEntity(getProxyHeaders(request), mockRequest.trim()), String.class);
			return proxyResponse.getBody();
		}else {
			throw new Exception("Method not supported");
		}
	}
	
	private String resolveEurekaOrUrl() {
		return null;
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	private HttpHeaders getProxyHeaders(HttpServletRequest request) {
		return null;
	}
	
	/**
	 * 
	 * @param httpHeaders
	 * @param jsonRequest
	 * @return
	 */
	private HttpEntity getHttpEntity(MultiValueMap<String, String> httpHeaders, String jsonRequest) {
		if(null != jsonRequest) {
			return new HttpEntity<>(jsonRequest, httpHeaders);
		}else {
			return new HttpEntity<>( httpHeaders);
		}
		
	}
}
