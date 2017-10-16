package com.winning.mobileclinical.model.cis;

public class TableClass {
	public String provider;   //对象
	public String service;    //服务
	public String className;  //表名
	public String type;    //状态 用于区分同个病人不同数据
	public int single;    //状态 用于区分返回数据格式 =1 单个记录  =0 数组。
	
	
	
	public int getSingle() {
		return single;
	}
	public void setSingle(int single) {
		this.single = single;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
