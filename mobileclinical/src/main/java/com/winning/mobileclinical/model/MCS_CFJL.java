package com.winning.mobileclinical.model;

import java.io.Serializable;

public class MCS_CFJL implements Serializable {
	private String  xh;//序号
	private String  syxh;	 //首页序号
	private String  name;	 //患者姓名
	private String  memo;    //查房文本内容
	private String  ysdm;//查房医生代码
	private String  ysmc;	 //查房医生姓名
	private String  cjrq;    //创建日期
	private int  clzt;	 //处理状态 0 未处理  1 已处理
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	public String getSyxh() {
		return syxh;
	}
	public void setSyxh(String syxh) {
		this.syxh = syxh;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getYsdm() {
		return ysdm;
	}
	public void setYsdm(String ysdm) {
		this.ysdm = ysdm;
	}
	public String getYsmc() {
		return ysmc;
	}
	public void setYsmc(String ysmc) {
		this.ysmc = ysmc;
	}
	public String getCjrq() {
		return cjrq;
	}
	public void setCjrq(String cjrq) {
		this.cjrq = cjrq;
	}
	public int getClzt() {
		return clzt;
	}
	public void setClzt(int clzt) {
		this.clzt = clzt;
	}
}
