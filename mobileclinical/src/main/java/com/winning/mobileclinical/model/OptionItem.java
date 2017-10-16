package com.winning.mobileclinical.model;

import org.json.JSONException;
import org.json.JSONObject;

public class OptionItem {

	private String text ;
	private String value;
	
//	private String hasText;
//	private String table;
//	private String field;
//	private String truevalue;
//	private String falsevalue;
	
	
	public OptionItem(String text,String value){
		this.text = text;
		this.value = value;
	}
	
	public OptionItem(JSONObject json){
		try {
			this.text = json.getString("text");
			this.value = json.getString("value");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			this.hasText = json.getString("hasText");
//		} catch (JSONException e) {
//			this.hasText = null;
//		}
//		
//		try {
//			this.truevalue = json.getString("truevalue");
//		} catch (JSONException e) {
//			this.truevalue = null;
//		}
//		
//		try {
//			this.falsevalue = json.getString("falsevalue");
//		} catch (JSONException e) {
//			this.falsevalue = null;
//		}
		
	}
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
