package com.bh.api.proxy.gateway.controller;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bh.api.proxy.gateway.ui.request.ProxyRequest;

@RestController
public class RendererController {

	@RequestMapping(value = "/gateway/proxy/fire", method = RequestMethod.POST)
	public Object getProxyResponse(@RequestBody ProxyRequest proxyRequest) throws Exception{
		
		
		if("POST".equals(proxyRequest.getMethod())){
			
			RestTemplate restTemplate= new RestTemplate();
			ResponseEntity<String> proxyResponse = restTemplate.exchange(
					proxyRequest.getApi().trim(),
					HttpMethod.POST, getHttpEntity(getProxyHeaders(proxyRequest), proxyRequest.getJsonBody().trim()), String.class);
			return proxyResponse.getBody();
			
		}else if("GET".equals(proxyRequest.getMethod())){
			
			RestTemplate restTemplate= new RestTemplate();
			ResponseEntity<String> proxyResponse = restTemplate.exchange(
					proxyRequest.getApi(),
					HttpMethod.GET, getHttpEntity(getProxyHeaders(proxyRequest), null), String.class);
			return proxyResponse.getBody();
		
		}else if("PUT".equals(proxyRequest.getMethod())) {
			
			RestTemplate restTemplate= new RestTemplate();
			ResponseEntity<String> proxyResponse = restTemplate.exchange(
					proxyRequest.getApi().trim(),
					HttpMethod.PUT, getHttpEntity(getProxyHeaders(proxyRequest), proxyRequest.getJsonBody().trim()), String.class);
			return proxyResponse.getBody();
		}else {
			throw new Exception("Method not supported");
		}
	}
	
	private HttpEntity getHttpEntity(MultiValueMap<String, String> httpHeaders, String jsonRequest) {
		if(null != jsonRequest) {
			return new HttpEntity<>(jsonRequest, httpHeaders);
		}else {
			return new HttpEntity<>( httpHeaders);
		}
		
	}

	private MultiValueMap<String, String> getProxyHeaders(ProxyRequest proxyRequest) {
		MultiValueMap<String, String> proxyHeaders = new LinkedMultiValueMap<String, String>();
		if(!CollectionUtils.isEmpty(proxyRequest.getHeaderKey())){
			for(int i = 0; i<proxyRequest.getHeaderKey().size(); i++) {
				proxyHeaders.add(proxyRequest.getHeaderKey().get(i).trim(), proxyRequest.getHeaderVal().get(i).trim());
			}
		}
	return proxyHeaders;
	}
}
