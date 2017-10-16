package com.winning.mobileclinical.web;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * HTTP访问响应类
 * @author xiaoliang
 * */
public class HttpResult implements Serializable {

	private int code;  //1：正常响应    2：超时   3：网络连接异常   4：服务器错误    5：响应解析异常  6：会话超时 7：其他错误
	private String msg;
	private JSONObject json;
	
	public HttpResult(int code,String msg,JSONObject json){
		this.code = code;
		this.msg = msg;
		this.json = json;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public JSONObject getJson() {
		return json;
	}
	public void setJson(JSONObject json) {
		this.json = json;
	}
	
	
	
}
