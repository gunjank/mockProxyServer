package com.bh.api.proxy.gateway.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bh.api.proxy.gateway.model.ObjectCacheKey;
import com.bh.api.proxy.gateway.service.ModellerService;
import com.bh.api.proxy.gateway.ui.ValidationException;
import com.bh.api.proxy.gateway.ui.request.MockViewRequest;
import com.bh.api.proxy.gateway.ui.response.CollectionMockResponse;
import com.bh.api.proxy.gateway.ui.response.CompositeMockResponse;

@RestController
public class DataController {
	
	@Autowired
	ModellerService modellerService;
	
	@RequestMapping(value = "/gateway/mocks/search/{api}", method = RequestMethod.GET)
	public Object searchApi(@PathVariable("api") String api) throws IOException {
		
		try {
			String decodedApi = new String(Base64.getDecoder().decode(api));
			
				try {
					URL url = new URL(decodedApi);
					decodedApi = url.getPath();
				}catch(Exception e) {
					//do nothing, as the search string need not be an uri..
				}
			
			if(!StringUtils.isEmpty(decodedApi) && decodedApi.length() > 3) {
				List<ObjectCacheKey> response = modellerService.searchMocks(decodedApi);
				if(!CollectionUtils.isEmpty(response)) {
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
			}
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"Search String is empty or less then 3 characters or no mocks found\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"No mocks availble to process\"}", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/gateway/mocks/view", method = RequestMethod.POST)
	public ResponseEntity<?> viewMocks(@RequestBody MockViewRequest mockViewRequest) {
		try {
			String api = "";
			try {
				URL url = new URL(mockViewRequest.getApi());
				api = url.getPath();
			}catch(Exception e) {
				//do nothing, as the search string need not be an uri..
			}
			
			return new ResponseEntity<Object>(modellerService.viewMocks(api, mockViewRequest.getMethod()), HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<String>("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"No mocks availble to process\"}", HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/gateway/mocks/view/defaultTemplate", method = RequestMethod.POST)
	public ResponseEntity<?> viewDefaultTemplate(@RequestBody MockViewRequest mockViewRequest) {
		try {
			mockViewRequest.validate_Api_Method_present();
			return new ResponseEntity<Object>(modellerService.viewWhetherMockTemplateExist(mockViewRequest.getApi(), mockViewRequest.getMethod()),
					HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<String>("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"No mocks availble to process\"}", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value = "/gateway/mocks/export/single", method = RequestMethod.POST)
	public Object exportData(@RequestBody MockViewRequest mockViewRequest) throws IOException {
		
		try {
			CollectionMockResponse collectionMockResponse = new CollectionMockResponse();
			List<CompositeMockResponse> compositeMockResponseList = new ArrayList<CompositeMockResponse>();
			
			mockViewRequest.validate_Api_Method_present();
			CompositeMockResponse compositeMockResponse= modellerService.viewMocks(mockViewRequest.getApi(), mockViewRequest.getMethod());
			
			compositeMockResponseList.add(compositeMockResponse);
			collectionMockResponse.setListOfCompositeMockResponse(compositeMockResponseList);
			
			ByteArrayOutputStream byteArrayOutPutStream = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutPutStream);
			oos.writeObject(compositeMockResponseList);
			byte[] byteArray = byteArrayOutPutStream.toByteArray();
			String response = Base64.getEncoder().encodeToString(byteArray);
			byteArrayOutPutStream.close();
			oos.close();
						
			return new ResponseEntity<String>(response, HttpStatus.OK);
			
		} catch (ValidationException e) {
			return new ResponseEntity<String>("{\"code\":\"" + e.getCode() + "\", \"description\":\"" + e.getMessage() + "\"}", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"No mocks availble to process\"}", HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/gateway/mocks/export/collection", method = RequestMethod.POST)
	public Object exportData(@RequestBody List<MockViewRequest> mockViewRequestList) throws IOException {
		
		try {
			CollectionMockResponse collectionMockResponse = new CollectionMockResponse();
			List<CompositeMockResponse> compositeMockResponseList = new ArrayList<CompositeMockResponse>();
			
			if(!CollectionUtils.isEmpty(mockViewRequestList)) {
				mockViewRequestList.forEach(mockViewRequest -> {
					try {
						mockViewRequest.validate_Api_Method_present();
						compositeMockResponseList.add(modellerService.viewMocks(mockViewRequest.getApi(), mockViewRequest.getMethod()));
					}catch(Exception e) {
						
					}
				});
				collectionMockResponse.setListOfCompositeMockResponse(compositeMockResponseList);
			}
			ByteArrayOutputStream byteArrayOutPutStream = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutPutStream);
			oos.writeObject(collectionMockResponse);
			byte[] byteArray = byteArrayOutPutStream.toByteArray();
			String response = Base64.getEncoder().encodeToString(byteArray);
			byteArrayOutPutStream.close();
			oos.close();
						
			return new ResponseEntity<String>(response, HttpStatus.OK);
			
		} catch (Exception ex) {
			return new ResponseEntity<String>("{\"code\":\"InvalidRequest\", \"description\":\"No mocks availble to process\"}", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value = "/gateway/mocks/upload/view", method = RequestMethod.POST, headers = "content-type=multipart/form-data", produces = "application/json")
	public ResponseEntity<Object> uploadAndView(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
		
		try {
			byte[] encodedBytes = IOUtils.toByteArray(file.getInputStream());
			byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
			 ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(decodedBytes));
					 
			 CollectionMockResponse collectionMockResponse = (CollectionMockResponse)is.readObject();
			
			 is.close();
			 return new ResponseEntity<Object>(collectionMockResponse, HttpStatus.OK);
			 
		}catch(Exception e) {
			return new ResponseEntity<Object>("{\"code\":\"InvalidRequest\", \"description\":\"No mocks availble to process\"}", HttpStatus.BAD_REQUEST);
		}

	}
	
	@RequestMapping(value = "/gateway/mocks/import/override", method = RequestMethod.POST, headers = "content-type=multipart/form-data", produces = "application/json")
	public Object importData(@RequestBody CompositeMockResponse compositeMockResponse) throws IOException {
		
		if(null != compositeMockResponse && !CollectionUtils.isEmpty(compositeMockResponse.getMockCriteriaList())) {
			compositeMockResponse.getMockCriteriaList().forEach(mockSetupRequest -> {
				try {
					mockSetupRequest.validateRequest();
					modellerService.updateMocks(mockSetupRequest);
				}catch(Exception e) {
					
				}
			});
			return new ResponseEntity<Object>(HttpStatus.OK);
		}else {
			return new ResponseEntity<Object>("{\"code\":\"InvalidRequest\", \"description\":\"No mocks availble to process\"}", HttpStatus.BAD_REQUEST);
		}
		}
		
	
	//TODO:: to be implemented...
	@RequestMapping(value = "/gateway/mocks/import/selective", method = RequestMethod.POST, headers = "content-type=multipart/form-data", produces = "application/json")
	public Object importDataSelective(@RequestBody CompositeMockResponse compositeMockResponse) throws IOException {
		
		return null;
	}
}
