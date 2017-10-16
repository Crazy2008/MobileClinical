package com.winning.mobileclinical.model;

import java.io.Serializable;

public class MenuDTO implements Serializable {
	private int int_id;
	String id;
	String text;	//显示文字
	String note;	//备注
	int suptype;	//显示在关于患者还是医生记录界面
	String intent;  //响应的Fragment
	int orderby;	//显示顺序
	int state;	//使用状态
	int lcljbz;	//临床路径标注
	String imageurl;  //
	
	public String getImageurl() {
		return imageurl;
	}
	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}
	public void setLcljbz(int lcljbz) {
		this.lcljbz = lcljbz;
	}
	public int getLcljbz() {
		return lcljbz;
	}
	public void setIslcljbz(int lcljbz) {
		this.lcljbz = lcljbz;
	}
	public int getInt_id() {
		return int_id;
	}
	public void setInt_id(int int_id) {
		this.int_id = int_id;
	}
	public MenuDTO() {
		// TODO Auto-generated constructor stub
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
	public int getSuptype() {
		return suptype;
	}
	public void setSuptype(int suptype) {
		this.suptype = suptype;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public int getOrderby() {
		return orderby;
	}
	public void setOrderby(int orderby) {
		this.orderby = orderby;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}

	
}
