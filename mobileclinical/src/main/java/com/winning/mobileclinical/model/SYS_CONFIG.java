package com.winning.mobileclinical.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统配置�?
 * 
 */
public class SYS_CONFIG {
	private String id;
	private String text;
	private String note;
	private String memo;
	private String config;
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

}
