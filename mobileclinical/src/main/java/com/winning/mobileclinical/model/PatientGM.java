package com.winning.mobileclinical.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 病人过敏信息
 * 
 * @author YUTAO R&D Center
 * @Time 2011-7-6
 */
public class PatientGM {
	private int xh;        //序号
	private String syxh;   //首页序号
	private String hzxm;   //患者姓名
	private int    jlzt;   //记录状态
	private String czyh;   //操作员号
	private String ypmc;   //药品名称
	private int    gmlx;   //过敏类型
	private String ksrq;   //开始日期
	private String jsrq;   //结束日期
	private String lcidm;  //临床idm
	private String ggidm;  //规格idm
	private String csrq;

	public PatientGM(JSONObject json){
		try {
//			this.xh=json.getInt("xh");
			this.syxh = json.getString("syxh");
			this.hzxm = json.getString("hzxm");
			this.jlzt = json.getInt("jlzt");
			this.czyh = json.getString("czyh");
			this.ypmc = json.getString("ypmc");
			this.gmlx = json.getInt("gmlx");
			this.ksrq = json.getString("ksrq");
			this.jsrq = json.getString("jsrq");
			this.lcidm = json.getString("lcidm");
			this.ggidm = json.getString("ggidm");
			this.csrq = json.getString("csrq");
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public PatientGM(){}
	
	public String toString() {
		String str = "{\"syxh\":\"" + syxh + "\","
				+ "\"hzxm\":\"" + hzxm + "\","
				+ "\"jlzt\":\"" + jlzt + "\","
				+ "\"czyh\":\"" + czyh + "\","
				+ "\"ypmc\":\"" + ypmc + "\","
				+ "\"gmlx\":\"" + gmlx + "\","
				+ "\"ksrq\":\"" + ksrq + "\","
				+ "\"jsrq\":\"" + jsrq + "\","
				+ "\"lcidm\":\"" + lcidm + "\","
				+ "\"ggidm\":\"" + ggidm + "\","
				+ "\"csrq\":\""  + csrq
				+ "\"}";
		
		return str.replaceAll("\"", "\\\"");
	}
	
	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}

	public String getSyxh() {
		return syxh;
	}

	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}

	public String getHzxm() {
		return hzxm;
	}

	public void setHzxm(String hzxm) {
		this.hzxm = hzxm;
	}

	public int getJlzt() {
		return jlzt;
	}

	public void setJlzt(int jlzt) {
		this.jlzt = jlzt;
	}

	public String getCzyh() {
		return czyh;
	}

	public void setCzyh(String czyh) {
		this.czyh = czyh;
	}

	public String getYpmc() {
		return ypmc;
	}

	public void setYpmc(String ypmc) {
		this.ypmc = ypmc;
	}

	public int getGmlx() {
		return gmlx;
	}

	public void setGmlx(int gmlx) {
		this.gmlx = gmlx;
	}

	public String getKsrq() {
		return ksrq;
	}

	public void setKsrq(String ksrq) {
		this.ksrq = ksrq;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getLcidm() {
		return lcidm;
	}

	public void setLcidm(String lcidm) {
		this.lcidm = lcidm;
	}

	public String getGgidm() {
		return ggidm;
	}

	public void setGgidm(String ggidm) {
		this.ggidm = ggidm;
	}

	public String getCsrq() {
		return csrq;
	}

	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
}
