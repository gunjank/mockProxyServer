package com.bh.api.proxy.gateway.controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bh.api.proxy.gateway.service.SwaggerService;
import com.bh.api.proxy.gateway.ui.request.SwaggerPayload;
import com.bh.api.proxy.gateway.ui.response.MockSwaggerResponse;

@RestController
public class SwaggerController {
	
	@Autowired
	SwaggerService swaggerService;
	
	@RequestMapping(value = "/swager/render", method = RequestMethod.POST)
	public ResponseEntity<MockSwaggerResponse> renderSwagger(@RequestBody SwaggerPayload payload) {
		return new ResponseEntity<MockSwaggerResponse>(swaggerService.getMockSwaggerResponses(new String(Base64.getDecoder().decode(payload.getSwagger()))), HttpStatus.OK);
	}
}
