package org.camunda.bpm.dmn.xlsx.override;

import java.util.List;

import org.camunda.bpm.dmn.xlsx.BaseAdapter;
import org.camunda.bpm.dmn.xlsx.InputOutputColumns;
import org.camunda.bpm.dmn.xlsx.api.Spreadsheet;
import org.camunda.bpm.dmn.xlsx.api.SpreadsheetCell;
import org.camunda.bpm.dmn.xlsx.api.SpreadsheetRow;
import org.camunda.bpm.dmn.xlsx.elements.HeaderValuesContainer;
import org.camunda.bpm.model.dmn.HitPolicy;

public class CustomInputOutputDetectionStrategy extends BaseAdapter {

	List<String> inputKeys;
	List<String> outputKeys;
	
	public CustomInputOutputDetectionStrategy(List<String> inputKeys, List<String> outputKeys){
		this.inputKeys = inputKeys;
		this.outputKeys= outputKeys;
	}
	
	public InputOutputColumns determineInputOutputs(Spreadsheet context) {

	    SpreadsheetRow headerRow = context.getRows().get(0);

	    if (!headerRow.hasCells()) {
	      throw new RuntimeException("A dmn table requires at least one output; the header row contains no entries");
	    }

	    InputOutputColumns ioColumns = new InputOutputColumns();
	    List<SpreadsheetCell> cells = headerRow.getCells();
	    for (SpreadsheetCell inputCell : cells) {
	    	HeaderValuesContainer hvc = new HeaderValuesContainer();
	    	fillHvc(inputCell, context, hvc);
	    	if(inputKeys.contains(inputCell.getColumn())) {
	    		hvc.setId("Input" + inputCell.getColumn());
	    		ioColumns.addInputHeader(hvc);
	    	}else if(outputKeys.contains(inputCell.getColumn())) {
	    		hvc.setId("Output" + inputCell.getColumn());
	    		ioColumns.addOutputHeader(hvc);
	    	}
	    }
	    return ioColumns;
	  }

	  @Override
	  public HitPolicy determineHitPolicy(Spreadsheet context) {
	    return HitPolicy.FIRST;
	  }

	  private void fillHvc(SpreadsheetCell cell, Spreadsheet context, HeaderValuesContainer hvc) {
	    hvc.setText(context.resolveCellContent(cell));
	    hvc.setColumn(cell.getColumn());
	  }

	  @Override
	  public List<SpreadsheetRow> determineRuleRows(Spreadsheet context) {
	    List<SpreadsheetRow> rows = context.getRows();
	    return rows.subList(1, rows.size());
	  }
}
