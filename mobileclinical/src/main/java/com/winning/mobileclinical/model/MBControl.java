package com.winning.mobileclinical.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.winning.mobileclinical.form.CustomForm;



public class MBControl {

	private String origvalue;
	private String value;
	private String indexStart;
	private String indexEnd;
	private String controlType;   //Checkbox  Lable Textbox NumericUpDown DateTimePicker combobox
	private String valueAlignment;
	private String dataSourse;
	private List<CustomForm> inputs = new ArrayList<CustomForm>();
	private String node;
	
	private LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
	
	public void addMapValue(String name,String value){
		map.put(name, value);
	}
	
	public void setValue(){
//		if(controlType.equals(""))
	}
	
	public String getXMLString(){
		String xml = "<InputControl>"+
		"<Value Value=\""+value+"\" />"+
		"<IndexStart Value=\""+indexStart+"\" />"+
		"<IndexEnd Value=\""+indexEnd+"\" />"+
		"<ControlType Value=\""+controlType+"\" />"+
		"<ValueAlignment Value=\""+valueAlignment+"\" />";
		if(dataSourse==null){
			xml += "<DataSource />";
		}else{
			xml += "<DataSource><ValueMember Value=\""+dataSourse+"\" /></DataSource>";
		}
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			xml += "<"+key+" Value=\""+map.get(key)+"\"/>";
		}
		xml +="</InputControl>";
		return xml;
	}
	
	public String getOrigXMLString(){
		String xml = "<InputControl>"+
		"<Value Value=\""+origvalue+"\" />"+
		"<IndexStart Value=\""+indexStart+"\" />"+
		"<IndexEnd Value=\""+indexEnd+"\" />"+
		"<ControlType Value=\""+controlType+"\" />"+
		"<ValueAlignment Value=\""+valueAlignment+"\" />";
		if(dataSourse==null){
			xml += "<DataSource />";
		}else{
			xml += "<DataSource><ValueMember Value=\""+dataSourse+"\" /></DataSource>";
		}
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			xml += "<"+key+" Value=\""+map.get(key)+"\"/>";
		}
		xml +="</InputControl>";
		return xml;
	}
	
	public void addInput(CustomForm input){
		inputs.add(input);
	}
	
//	public void setValue(String value){
//		
//	}
	
	public String getMapValueByName(String name){
		return map.get(name);
	}
	
	public String getOrigvalue() {
		return origvalue;
	}

	public void setOrigvalue(String origvalue) {
		this.origvalue = origvalue;
	}

	public String getDataSourse() {
		return dataSourse;
	}

	public void setDataSourse(String dataSourse) {
		this.dataSourse = dataSourse;
	}

	public String getIndexStart() {
		return indexStart;
	}

	public void setIndexStart(String indexStart) {
		this.indexStart = indexStart;
	}

	public String getIndexEnd() {
		return indexEnd;
	}

	public void setIndexEnd(String indexEnd) {
		this.indexEnd = indexEnd;
	}

	public String getControlType() {
		return controlType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setControlType(String controlType) {
		this.controlType = controlType;
	}

	public String getValueAlignment() {
		return valueAlignment;
	}

	public void setValueAlignment(String valueAlignment) {
		this.valueAlignment = valueAlignment;
	}

	public List<CustomForm> getInput() {
		return inputs;
	}

	public void setInput(List<CustomForm> inputs) {
		this.inputs = inputs;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	
}
