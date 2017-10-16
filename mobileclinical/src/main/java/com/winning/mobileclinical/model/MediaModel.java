package com.winning.mobileclinical.model;

import org.json.JSONException;
import org.json.JSONObject;

public class MediaModel{

	private String id;					//ID
	private String lb;					//���
	private String lbsm;				//���˵��
	private String src;					//�ϼ��ļ���
	private String bwlid;				//��������¼ID
	private String jlzt;				//��¼״̬
	private String fileName;			//�ļ���
	private String description;			//��
	
	private String syxh;			//�ļ���
	private String yexh;
	/**
	 * 1:���� 2:ɾ��
	 */
	private int state = 0;			
	public MediaModel(JSONObject media) {
		try {
			this.id = media.getString("id");
			this.lb = media.getString("lb");
			this.src = media.getString("path");
			this.description = media.getString("description");
			this.fileName = media.getString("fileName");
			this.syxh = media.getString("syxh");
			this.yexh = media.getString("yexh");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public MediaModel() {
		// TODO Auto-generated constructor stub
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLb() {
		return lb;
	}
	public void setLb(String lb) {
		this.lb = lb;
	}
	public String getLbsm() {
		return lbsm;
	}
	public void setLbsm(String lbsm) {
		this.lbsm = lbsm;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getBwlid() {
		return bwlid;
	}
	public void setBwlid(String bwlid) {
		this.bwlid = bwlid;
	}
	public String getJlzt() {
		return jlzt;
	}
	public void setJlzt(String jlzt) {
		this.jlzt = jlzt;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSyxh() {
		return syxh;
	}
	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}
	public String getYexh() {
		return yexh;
	}
	public void setYexh(String yexh) {
		this.yexh = yexh;
	}
}
