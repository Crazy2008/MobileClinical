package com.winning.mobileclinical.model;

import java.math.BigDecimal;

import org.json.JSONException;
import org.json.JSONObject;


/** 费用信息 */
public class Charge {
	private int xh;//序号
	private BigDecimal syxh;//首页序号
	private String name = "0";//名称
	private String zfje = "0";//自费金额
	private String xmje = "0";//项目金额
	private String dm = "0"; 
	private String kbjj = "0";

	public Charge(JSONObject json){
		try {
//			this.xh=json.getInt("xh");
			this.syxh=new BigDecimal(json.getString("syxh"));
			this.name = json.getString("name");
			this.zfje = json.getString("zfje");
			this.xmje = json.getString("xmje");
			this.dm = json.getString("dm");
			this.kbjj = json.getString("kbjj");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Charge(){}
	
	public String toString() {
		String str = "{\"syxh\":\"" + syxh + "\","
				+ "\"name\":\"" + name + "\","
				+ "\"zfje\":\"" + zfje + "\","
				+ "\"xmje\":\"" + xmje + "\","
				+ "\"dm\":\"" + dm + "\","
				+ "\"kbjj\":\"" + kbjj
				+ "\"}";
		
		return str.replaceAll("\"", "\\\"");
	}
	

	public int getXh() {
		return xh;
	}

	public void setXh(int xh) {
		this.xh = xh;
	}

	public BigDecimal getSyxh() {
		return syxh;
	}

	public void setSyxh(BigDecimal syxh) {
		this.syxh = syxh;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZfje() {
		return zfje;
	}

	public void setZfje(String zfje) {
		this.zfje = zfje;
	}

	public String getXmje() {
		return xmje;
	}

	public void setXmje(String xmje) {
		this.xmje = xmje;
	}

	public String getDm() {
		return dm;
	}

	public void setDm(String dm) {
		this.dm = dm;
	}

	public String getKbjj() {
		return kbjj;
	}

	public void setKbjj(String kbjj) {
		this.kbjj = kbjj;
	}
}
