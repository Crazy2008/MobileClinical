package com.winning.mobileclinical.model.cis;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class DeptInfo {// / <summary>
	// / 科室代码
	// / </summary>
	public String id;
	// / <summary>
	// / 科室名称
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
	// / 二级科室代码
	// / </summary>
	public String ej_id;
	// / <summary>
	// / 二级科室名称
	// / </summary>
	public String ej_name;
	// / <summary>
	// / 二级科室拼音
	// / </summary>
	public String ej_py;
	// / <summary>
	// / 二级科室五笔
	// / </summary>
	public String ej_wb;
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

	public String getEj_id() {
		return ej_id;
	}

	public void setEj_id(String ej_id) {
		this.ej_id = ej_id;
	}

	public String getEj_name() {
		return ej_name;
	}

	public void setEj_name(String ej_name) {
		this.ej_name = ej_name;
	}

	public String getEj_py() {
		return ej_py;
	}

	public void setEj_py(String ej_py) {
		this.ej_py = ej_py;
	}

	public String getEj_wb() {
		return ej_wb;
	}

	public void setEj_wb(String ej_wb) {
		this.ej_wb = ej_wb;
	}

	public int getJlzt() {
		return jlzt;
	}

	public void setJlzt(int jlzt) {
		this.jlzt = jlzt;
	}
}
