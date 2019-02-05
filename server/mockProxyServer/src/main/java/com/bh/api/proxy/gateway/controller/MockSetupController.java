package com.bh.api.proxy.gateway.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bh.api.proxy.gateway.service.ModellerService;
import com.bh.api.proxy.gateway.ui.ValidationException;
import com.bh.api.proxy.gateway.ui.request.DmnSetupRequest;
import com.bh.api.proxy.gateway.ui.request.MockDefaultTemplateRequest;
import com.bh.api.proxy.gateway.ui.request.MockSetupRequest;

@RestController
public class MockSetupController {

	@Autowired
	ModellerService modellerService;

	@RequestMapping(value = "/gateway/mocks/insert/defaultTemplate", method = RequestMethod.POST)
	public ResponseEntity<?> insertDefaultTemplate(@RequestBody MockDefaultTemplateRequest mockDefaultTemplateRequest) {
		try {
			mockDefaultTemplateRequest.validateRequest();
			modellerService.insertOrUpdateDefaultTemplate(mockDefaultTemplateRequest);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<String>("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Request could not be processed\"}", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gateway/mocks/insert/dmn", method = RequestMethod.POST, headers = "content-type=multipart/form-data", produces = "application/json")
	public ResponseEntity<?> insertDMNFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		try {
			String api = validateAPI(request.getHeader("api"));
			validateMethod(request.getHeader("method"));
			modellerService.insertDMNCriteria(file, api, request.getHeader("method"));
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<String>("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Request could not be processed\"}", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gateway/mocks/insert/dmn/mapping", method = RequestMethod.POST)
	public ResponseEntity<?> insertDMNCriteria(@RequestBody DmnSetupRequest dmnSetupRequest) {
		try {
			dmnSetupRequest.validateRequest();
			modellerService.insertDmnTemplateAndJsonPathMapping(dmnSetupRequest);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<String>("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Request could not be processed\"}", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gateway/mocks/insert", method = RequestMethod.POST)
	public ResponseEntity<?> insertMocks(@RequestBody MockSetupRequest mockSetupRequest) {
		try {
			mockSetupRequest.validateRequest();
			int scenarioId= modellerService.insertMocks(mockSetupRequest);
			return new ResponseEntity<Object>("{\"mockScenarioId\":"+ scenarioId+"}", HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<String>("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Request could not be processed\"}", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gateway/mocks/insert/list", method = RequestMethod.POST)
	public ResponseEntity<?> insertMockList(@RequestBody List<MockSetupRequest> mockSetupRequestList) {
		List<String> errorResponseMap = new ArrayList<String>();
		List<Integer> scenarioIdList= new ArrayList<Integer>();
		mockSetupRequestList.forEach(mockSetupRequest -> {
			try {
				mockSetupRequest.validateRequest();
				int scenarioId = modellerService.insertMocks(mockSetupRequest);
				scenarioIdList.add(scenarioId);
			} catch (ValidationException e) {
				errorResponseMap.add("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}");
			} catch (Exception ex) {
				errorResponseMap.add("{\"code\":\"InvalidRequest\", \"description\":\"Request could not be processed\"}");
			}
		});
		if (CollectionUtils.isEmpty(errorResponseMap)) {
			return new ResponseEntity<Object>("{\"scenarioIdList\":"+scenarioIdList+"}",HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(errorResponseMap, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gateway/mocks/update", method = RequestMethod.POST)
	public ResponseEntity<?> updateMocks(@RequestBody MockSetupRequest mockSetupRequest) {
		try {
			mockSetupRequest.validateRequest();
			int mockScenarioId = modellerService.updateMocks(mockSetupRequest);
			return new ResponseEntity<Object>("{\"mockScenarioId\":"+ mockScenarioId+"}", HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<String>("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Request could not be processed\"}", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gateway/mocks/update/list", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMocksList(@RequestBody List<MockSetupRequest> mockSetupRequestList) {
		List<String> errorResponseMap = new ArrayList<String>();
		List<Integer> scenarioIdList= new ArrayList<Integer>();
		mockSetupRequestList.forEach(mockSetupRequest -> {
			try {
				mockSetupRequest.validateRequest();
				int scenarioId = modellerService.updateMocks(mockSetupRequest);
				scenarioIdList.add(scenarioId);
			} catch (ValidationException e) {
				errorResponseMap.add("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}");
			} catch (Exception ex) {
				errorResponseMap.add("{\"code\":\"InvalidRequest\", \"description\":\"Request could not be processed\"}");
			}
		});
		if (CollectionUtils.isEmpty(errorResponseMap)) {
			return new ResponseEntity<String>("{\"scenarioIdList\":"+scenarioIdList+"}", HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(errorResponseMap, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gateway/mocks/delete", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMocks(@RequestBody MockSetupRequest mockSetupRequest) {
		try {
			mockSetupRequest.validateIfMockCriteriaId_Api_Method_present();
			modellerService.deleteMocks(mockSetupRequest);
			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<String>("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Request could not be processed\"}", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gateway/mocks/delete/lists", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMocksList(@RequestBody List<MockSetupRequest> mockSetupRequestList) {
		List<String> errorResponseMap = new ArrayList<String>();
		mockSetupRequestList.forEach(mockSetupRequest -> {
			try {
				mockSetupRequest.validateIfMockCriteriaId_Api_Method_present();
				modellerService.deleteMocks(mockSetupRequest);
			} catch (ValidationException e) {
				errorResponseMap.add("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}");
			} catch (Exception ex) {
				errorResponseMap.add("{\"code\":\"InvalidRequest\", \"description\":\"Request could not be processed\"}");
			}
		});
		if (CollectionUtils.isEmpty(errorResponseMap)) {
			return new ResponseEntity<String>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Object>(errorResponseMap, HttpStatus.BAD_REQUEST);
		}
	}

	private String validateAPI(String api) throws ValidationException {

		try {
			// check URL
			URL url = new URL(api);
			api = url.getPath();
			return api;
		} catch (Exception e) {
			throw new ValidationException("API is not valid, use 'http://domain_name/path1/path2' | supported protocols are http, https",
					"URI_NOT_VALID_EXCEPTION");
		}
	}

	private void validateMethod(String method) throws ValidationException {

		// check method
		String[] methods = { "GET", "POST", "PUT", "DELETE" };
		if (!Arrays.asList(methods).contains(method)) {
			throw new ValidationException("METHOD is not valid,use either of GET,POST,PUT,DELETE", "METHOD_NOT_VALID_EXCEPTION");
		}
	}

}
