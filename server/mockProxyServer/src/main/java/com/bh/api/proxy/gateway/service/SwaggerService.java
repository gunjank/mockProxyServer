package com.bh.api.proxy.gateway.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.bh.api.proxy.gateway.ui.response.MockSwaggerResponse;
import com.bh.api.proxy.gateway.ui.response.ResponseModel;
import com.bh.api.proxy.gateway.ui.response.SwaggerResponse;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.DateSchema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.PasswordSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.media.UUIDSchema;
import io.swagger.v3.parser.OpenAPIV3Parser;

@Component
public class SwaggerService {

	/**
	 * 
	 * @param swaggerYaml
	 * @return
	 */
	public MockSwaggerResponse getMockSwaggerResponses(String swaggerYaml) {
		
		MockSwaggerResponse mockSwaggerResponse = new MockSwaggerResponse();
		try {
			File temp = File.createTempFile("swaggerYml" + new Random().nextInt(999999) + "", ".yml");
			Files.write(Paths.get(temp.getAbsolutePath()), swaggerYaml.getBytes());

			OpenAPI openAPI = new OpenAPIV3Parser().read(temp.getPath());

			
			openAPI.getPaths().entrySet().forEach(item -> {
			
				if (null != item.getValue().getGet()) {
					try {
						SwaggerResponse swaggerResponse = new SwaggerResponse();
						swaggerResponse.setApi(item.getKey());
						swaggerResponse.setMethod("GET");
						processSwaggerNodes("GET", openAPI, item, swaggerResponse);
						mockSwaggerResponse.addToList(swaggerResponse);
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				}

				if (null != item.getValue().getPost()) {
					try {
					SwaggerResponse swaggerResponse = new SwaggerResponse();
					swaggerResponse.setApi(item.getKey());
					swaggerResponse.setMethod("POST");
					processSwaggerNodes("POST", openAPI, item, swaggerResponse);
					mockSwaggerResponse.addToList(swaggerResponse);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				if (null != item.getValue().getPut()) {
					try {
					SwaggerResponse swaggerResponse = new SwaggerResponse();
					swaggerResponse.setApi(item.getKey());
					swaggerResponse.setMethod("PUT");
					processSwaggerNodes("PUT", openAPI, item, swaggerResponse);
					mockSwaggerResponse.addToList(swaggerResponse);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				if (null != item.getValue().getDelete()) {
					try {
					SwaggerResponse swaggerResponse = new SwaggerResponse();
					swaggerResponse.setApi(item.getKey());
					swaggerResponse.setMethod("DELETE");
					processSwaggerNodes("DELETE", openAPI, item, swaggerResponse);
					mockSwaggerResponse.addToList(swaggerResponse);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mockSwaggerResponse;
	}

	/**
	 * 
	 * @param method
	 * @param openAPI
	 * @param item
	 * @param swaggerResponse
	 */
	@SuppressWarnings("all")
	private void processSwaggerNodes(String method, OpenAPI openAPI, Entry<String, PathItem> item, SwaggerResponse swaggerResponse) {

		PathItem pathItem = item.getValue();
		Operation operation = getOperation(method, pathItem);
		if (null != operation) {
			// setting up the request model
			if (null != operation.getRequestBody() && !MapUtils.isEmpty(operation.getRequestBody().getContent())) {
				operation.getRequestBody().getContent().entrySet().forEach(content -> {
					StringBuilder ref = new StringBuilder(content.getValue().getSchema().get$ref());
					StringBuilder newBuilder = new StringBuilder();
					newBuilder.append("{");
					openAPI.getComponents().getSchemas().entrySet().forEach(schema -> {
						String tempSchema = ref.toString().substring("#/components/schemas/".length());
						if (tempSchema.toString().equals(schema.getKey())) {
							schema.getValue().getProperties().entrySet().forEach(prop -> {
								createRecursiveModel(newBuilder, openAPI, ((Entry<String, Schema>) prop).getValue(), ((Entry<String, Schema>) prop).getKey());
							});
						}
					});
					newBuilder.append("}");
					String request = newBuilder.toString().replaceAll("\\s{2,}", "");
					request = request.replaceAll("\\{{1,}", "{");

					request = processJsonCorrectly(request);
					List<String> requestList = new ArrayList<String>();
					requestList.add(request);
					swaggerResponse.setRequestModel(requestList);
				});
			} else {
				List<String> requestList = new ArrayList<String>();
				swaggerResponse.setRequestModel(requestList);
			}
			// setting up the response model

			if (!MapUtils.isEmpty(operation.getResponses())) {
				List<ResponseModel> responseList = new ArrayList<ResponseModel>();
				operation.getResponses().entrySet().forEach(response -> {
					if (!MapUtils.isEmpty(response.getValue().getContent())) {
						response.getValue().getContent().entrySet().forEach(content -> {

							StringBuilder ref = new StringBuilder(content.getValue().getSchema().get$ref());
							StringBuilder newBuilder = new StringBuilder();
							newBuilder.append("{");
							openAPI.getComponents().getSchemas().entrySet().forEach(schema -> {
								String tempSchema = ref.toString().substring("#/components/schemas/".length());
								if (tempSchema.toString().equals(schema.getKey())) {
									schema.getValue().getProperties().entrySet().forEach(prop -> {
										createRecursiveModel(newBuilder, openAPI, ((Entry<String, Schema>) prop).getValue(),
												((Entry<String, Schema>) prop).getKey());
									});
								}
							});
							newBuilder.append("}");
							String responses = newBuilder.toString().replaceAll("\\s{2,}", "");
							responses = responses.replaceAll("\\{{1,}", "{");
							responses = processJsonCorrectly(responses);
							ResponseModel responseModel = new ResponseModel();
							responseModel.setStatusCode(response.getKey());
							responseModel.setResponse(responses);
							responseList.add(responseModel);
						});
					}
				});
				swaggerResponse.setResponseModel(responseList);
			} else {
				List<String> responseList = new ArrayList<String>();
				swaggerResponse.setRequestModel(responseList);
			}

		} else {
			List<String> responseList = new ArrayList<String>();
			swaggerResponse.setRequestModel(responseList); // adding empty request in case of empty request body/not supported operations.
		}
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	private String processJsonCorrectly(String request) {
		request = request.replaceAll("}\"", "},\"");
		request = request.replaceAll("]\"", "],\"");
		request = request.replaceAll(",,,", ",");
		request = request.replaceAll(",,", ",");
		request = request.replaceAll(",}", "}");
		request = request.replaceAll("},}", "}");
		request = request.replaceAll("},},", "},");
		request = request.replaceAll(",]", "]");
		//request = request.replaceAll("\\[}\\]", "[]");
		//request = request.replaceAll("\\[{\\]", "[]");
		return request;
	}

	/**
	 * 
	 * @param method
	 * @param pathItem
	 * @return
	 */
	private Operation getOperation(String method, PathItem pathItem) {
		Operation operation = null;
		if ("POST".equals(method)) {
			operation = pathItem.getPost();
		}
		if ("PUT".equals(method)) {
			operation = pathItem.getPut();
		}
		if ("DELETE".equals(method)) {
			operation = pathItem.getDelete();
		}
		if ("GET".equals(method)) {
			operation = pathItem.getGet();
		}
		return operation;
	}

	/**
	 * @param builder
	 * @param openAPI
	 * @param schema
	 * @param key
	 */
	@SuppressWarnings("unchecked")
	private void createRecursiveModel(StringBuilder builder, OpenAPI openAPI, Schema schema, String key) {

		builder.append("\"" + key + "\":");
		if (schema instanceof ObjectSchema) {
			builder.append("{");
			if (null != schema.getProperties()) {
				String ref = schema.get$ref();
				if (StringUtils.isEmpty(ref)) {
					builder.append("}");
					return;
				}
				openAPI.getComponents().getSchemas().entrySet().forEach(openAPIschemes -> {
					String tempSchema = ref.toString().substring("#/components/schemas/".length());
					if (tempSchema.equals(openAPIschemes.getKey())) {
						if (!MapUtils.isEmpty(openAPIschemes.getValue().getProperties())) {
							builder.append("{");
							openAPIschemes.getValue().getProperties().entrySet().forEach(prop -> {
								createRecursiveModel(builder, openAPI, ((Entry<String, Schema>) prop).getValue(), ((Entry<String, Schema>) prop).getKey());
								return;
							});
							builder.append("}");
						}
						return;
					} else {
						return;
					}
				});
			} else {
				builder.append("},");
				return;
			}
		} else if (schema instanceof ArraySchema) {
			ArraySchema arraySchema = (ArraySchema) schema;
			builder.append("[");
			if (null != arraySchema.getItems()) {
				String ref = arraySchema.getItems().get$ref();
				if (ref == null) {
					builder.append("],");
					return;
				}
				openAPI.getComponents().getSchemas().entrySet().forEach(openAPIschemes -> {
					String tempSchema = ref.toString().substring("#/components/schemas/".length());
					if (tempSchema.equals(openAPIschemes.getKey())) {
						if (!MapUtils.isEmpty(openAPIschemes.getValue().getProperties())) {
							builder.append("{");
							openAPIschemes.getValue().getProperties().entrySet().forEach(prop -> {
								createRecursiveModel(builder, openAPI, ((Entry<String, Schema>) prop).getValue(), ((Entry<String, Schema>) prop).getKey());
								return;
							});
							builder.append("}");
							builder.append("]");
							return;
						}
						return;
					} else {
						return;
					}
				});
			} else {
				builder.append("]");
				return;
			}
		} else if (schema instanceof StringSchema) {
			builder.append("\"string\",");
			return;
		} else if (schema instanceof BooleanSchema) {
			builder.append("false,");
			return;
		} else if (schema instanceof NumberSchema) {
			builder.append("0,");
			return;
		} else if (schema instanceof IntegerSchema) {
			builder.append("0,");
			return;
		} else if (schema instanceof DateTimeSchema) {
			builder.append("\"YYYY.MM.DD-HH.MM.SS\",");
			return;
		} else if (schema instanceof PasswordSchema) {
			builder.append("\"string\",");
			return;
		} else if (schema instanceof DateSchema) {
			builder.append("\"YYYY.MM.DD\",");
			return;
		} else if (schema instanceof DateSchema) {
			builder.append("\"abc@aa.com\",");
			return;
		} else if (schema instanceof UUIDSchema) {
			builder.append("\"12345566789\",");
			return;
		} else if (schema instanceof Schema) {

			String ref = schema.get$ref();
			builder.append("{");
			ref = schema.get$ref();
			if (StringUtils.isEmpty(ref)) {
				builder.append("}");
				return;
			}
			openAPI.getComponents().getSchemas().entrySet().forEach(openAPIschemes -> {
				String tempSchema = schema.get$ref().substring("#/components/schemas/".length());
				if (tempSchema.equals(openAPIschemes.getKey())) {
					if (!MapUtils.isEmpty(openAPIschemes.getValue().getProperties())) {
						builder.append("{");
						openAPIschemes.getValue().getProperties().entrySet().forEach(prop -> {
							createRecursiveModel(builder, openAPI, ((Entry<String, Schema>) prop).getValue(), ((Entry<String, Schema>) prop).getKey());
							return;
						});
						builder.append("}");
					}
					return;
				} else {
					return;
				}
			});

		} else {
			builder.append("\"string\",");
			return;
		}
		return;
	}
}
