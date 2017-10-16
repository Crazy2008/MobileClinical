package com.winning.mobileclinical.model.cis;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class WardInfo {// <summary>
	// / 病区代码
	// / </summary>
	public String id;
	// / <summary>
	// / 病区名称
	// / </summary>
	public String name;
	// / <summary>
	// / 拼音
	// / </summary>
	public String py;
	// / <summary>
	// / 五笔
	// / </summary>
	public String wb;
	// / <summary>
	// / 医院代码
	// / </summary>
	public String hospital_id;
	// / <summary>
	// / 类别(0:一般，1：急观，2：产房，3：ICU&CCU)
	// / </summary>
	public int category;
	// / <summary>
	// / 记录状态
	// / </summary>
	public int jlzt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getWb() {
		return wb;
	}

	public void setWb(String wb) {
		this.wb = wb;
	}

	public String getHospital_id() {
		return hospital_id;
	}

	public void setHospital_id(String hospital_id) {
		this.hospital_id = hospital_id;
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public int getJlzt() {
		return jlzt;
	}

	public void setJlzt(int jlzt) {
		this.jlzt = jlzt;
	}

}
