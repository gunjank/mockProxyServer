package com.bh.api.proxy.gateway.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.apache.velocity.tools.generic.MathTool;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.xlsx.XlsxConverter;
import org.camunda.bpm.dmn.xlsx.override.CustomInputOutputDetectionStrategy;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.springframework.web.multipart.MultipartFile;

import com.bh.api.proxy.gateway.model.DMNJsonPathKey.DMNKeyType;
import com.bh.api.proxy.gateway.velocityExtensions.BankingFormatTool;
import com.jayway.jsonpath.DocumentContext;

public class DmnCriteria implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	// this will serve as the primary key for the dmn criteria..
	private int dmnCriteriaId;
	private Template template;
	private Map<String, DMNJsonPathKey> dmnJsonPathKeyMapping;
	private StringResourceRepository stringResourceRepository;
	private VelocityEngine velocityEngine;
	private String templateString;

	private DmnEngine dmnEngine;
	private DmnDecision dmnDecision;

	public DmnCriteria(VelocityEngine velocityEngine, StringResourceRepository stringResourceRepository) {
		this.stringResourceRepository = stringResourceRepository;
		this.velocityEngine=velocityEngine;
	}
	
	public synchronized DmnCriteria setupTemplateAndJsonPathAndTemplateMapping(String templateString, Map<String, DMNJsonPathKey> dmnJsonPathKeyMapping) {
		this.templateString = templateString;
		this.stringResourceRepository.putStringResource(this.hashCode() + "", templateString);
		this.template = velocityEngine.getTemplate(this.hashCode() + "");

		if (!MapUtils.isEmpty(dmnJsonPathKeyMapping)) {
			this.dmnJsonPathKeyMapping = dmnJsonPathKeyMapping;
		} else {
			this.dmnJsonPathKeyMapping = new HashMap<String, DMNJsonPathKey>();
		}
		return this;
	}

	public synchronized DmnCriteria setupDmnEngineAndDecision(MultipartFile file) throws Exception {

		if (MapUtils.isEmpty(dmnJsonPathKeyMapping)) {
			throw new Exception("dmnJsonPathKeyMapping is not being set or empty for the DMN criteria");
		} else {
			String dmnDecisionName = "";
			File xlFile = File.createTempFile(Math.floor(Math.random() * 10000) + "" + file.getOriginalFilename(), ".xlsx");
			InputStream initialStreamFromClient = file.getInputStream();
			java.nio.file.Files.copy(initialStreamFromClient, xlFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			IOUtils.closeQuietly(initialStreamFromClient);

			InputStream xlsxInputStream = new FileInputStream(xlFile);

			List<String> inputKeys = new ArrayList<String>();
			List<String> outputKeys = new ArrayList<String>();
			dmnJsonPathKeyMapping.entrySet().forEach(entrySet -> {
				if (entrySet.getValue().getType().equals(DMNKeyType.INPUT)) {
					inputKeys.add(entrySet.getValue().getTemplateKey());
				} else if (entrySet.getValue().getType().equals(DMNKeyType.OUTPUT)) {
					outputKeys.add(entrySet.getValue().getTemplateKey());
				}
			});

			List<String> inputColumnIds = new ArrayList<String>();
			List<String> outputColumnIds = new ArrayList<String>();

			Workbook workbook = WorkbookFactory.create(xlFile);

			// always use the first sheet for DMN processing..
			Sheet sheet = workbook.getSheetAt(0);
			System.out.println("=> " + sheet.getSheetName());
			// setting the sheet name as the dmn decision name..
			dmnDecisionName = sheet.getSheetName();
			// the first row, should hold the header keys..
			Row row = sheet.getRow(0);
			row.iterator().forEachRemaining(cell -> {
				if (inputKeys.contains(cell.getStringCellValue())) {
					inputColumnIds.add(CellReference.convertNumToColString(cell.getColumnIndex()));
				} else if (outputKeys.contains(cell.getStringCellValue())) {
					outputColumnIds.add(CellReference.convertNumToColString(cell.getColumnIndex()));
				}
			});
			workbook.close();

			XlsxConverter converter = new XlsxConverter();

			CustomInputOutputDetectionStrategy strategy = new CustomInputOutputDetectionStrategy(inputColumnIds, outputColumnIds);
			converter.setIoDetectionStrategy(strategy);
			DmnModelInstance dmnModelInstance = converter.convert(xlsxInputStream);

			ByteArrayOutputStream dmnOutputStream = new ByteArrayOutputStream();
			Dmn.writeModelToStream(dmnOutputStream, dmnModelInstance);
			byte[] dmnOutput = dmnOutputStream.toByteArray();

			ByteArrayInputStream decisionInputStream = new ByteArrayInputStream(dmnOutput);
			DmnEngineConfiguration configuration = DmnEngineConfiguration.createDefaultDmnEngineConfiguration();
			this.dmnEngine = configuration.buildEngine();
			this.dmnDecision = dmnEngine.parseDecision(dmnDecisionName, decisionInputStream);

			dmnOutputStream.close();
			xlsxInputStream.close();
			xlFile.delete();
		}

		return this;
	}
	
	public boolean hasDmnDecision() {
		return null != this.dmnDecision;
	}

	/**
	 * Runner method to process DMN criteria..
	 * 
	 * @param jsonString
	 * @return
	 * @throws Exception
	 */
	public String buildResponseIfDMNRunIsSuccessfull(MockResponseHolder mockResponseHolder) throws Exception {
		try {
			return buildResponse(setupNodeWithRawRequestToPerformDMNOperation(mockResponseHolder.getJsonDocumentContext()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Could not proces DMN criteria");
		}

	}

	/**
	 * process the extracted path, right now supports only integer or string. rest
	 * to be implemented
	 * 
	 * @param jsonString
	 * @return
	 */
	private Map<String, Object> setupNodeWithRawRequestToPerformDMNOperation(DocumentContext jsonDocumentContext) {
		Map<String, Object> extractedPaths = new HashMap<String, Object>();
		dmnJsonPathKeyMapping.entrySet().forEach(entrySet -> {
			if (entrySet.getValue().getType().equals(DMNKeyType.INPUT)) {

				List<String> ob = jsonDocumentContext.read(entrySet.getValue().getJsonPath());
				try {
					Integer.parseInt(ob.get(0) + "");
					extractedPaths.put(entrySet.getValue().getTemplateKey(), Integer.parseInt(ob.get(0) + ""));
				} catch (Exception e) {
					extractedPaths.put(entrySet.getValue().getTemplateKey(), ob.get(0));
				}
			}
		});
		return extractedPaths;
	}

	private Map<String, Object> runDecisions(Map<String, Object> extractedPaths) throws Exception {

		try {
			Map<String, Object> dmnOutput = new HashMap<String, Object>();

			VariableMap variables = Variables.createVariables();
			dmnJsonPathKeyMapping.entrySet().forEach(entrySet -> {
				if (entrySet.getValue().getType().equals(DMNKeyType.INPUT)) {
					variables.put(entrySet.getValue().getTemplateKey(), extractedPaths.get(entrySet.getKey()));
				}
			});

			DmnDecisionTableResult dmnResult = dmnEngine.evaluateDecisionTable(dmnDecision, variables);
			if (null != dmnResult && null != dmnResult.getSingleResult()) {
				dmnJsonPathKeyMapping.entrySet().forEach(entrySet -> {
					if (entrySet.getValue().getType().equals(DMNKeyType.OUTPUT)) {
						dmnOutput.put(entrySet.getValue().getTemplateKey(), dmnResult.getSingleResult().get(entrySet.getValue().getTemplateKey()));
					}
				});
				return dmnOutput;
			} else {
				throw new Exception("Could not find a single result from DMN response");
			}
		} catch (Exception e) {
			throw new Exception("Could not find process DMN decisions");
		}
	}

	public String buildResponse(Map<String, Object> extractedPaths) throws Exception {
		// 1. get the template
		Template template = getTemplate();

		VelocityContext context = new VelocityContext();
		dmnJsonPathKeyMapping.entrySet().forEach(entrySet -> {
			context.put(entrySet.getValue().getTemplateKey(), extractedPaths.get(entrySet.getKey()));
		});
		// 1.1 run the decision table and set the values in context
		runDecisions(extractedPaths).entrySet().forEach(decisionOutput -> {
			context.put(decisionOutput.getKey(), decisionOutput.getValue());
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
		return response;
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

	public Map<String, DMNJsonPathKey> getDmnJsonPathKeyMapping() {
		return dmnJsonPathKeyMapping;
	}

	public int getDmnCriteriaId() {
		return dmnCriteriaId;
	}

	public void setDmnCriteriaId(int dmnCriteriaId) {
		this.dmnCriteriaId = dmnCriteriaId;
	}
}
