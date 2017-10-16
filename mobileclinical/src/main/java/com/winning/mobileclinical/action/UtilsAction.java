package com.winning.mobileclinical.action;

import com.winning.mobileclinical.globalCache.GlobalCache;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;

public class UtilsAction {
	public static String getRemoteInfo(String name, String key, String jsonargs) {
		// 命名空间
		String nameSpace = "http://tempuri.org/";
		// 调用的方法名称
		String methodName = "get";
		// EndPoint
//		String endPoint = WebUtils.WEBSERVICE;
		String endPoint = GlobalCache.getCache().getUrl();
		String soapAction = "http://tempuri.org/get";
		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		Map<String, Object> map = new HashMap<String, Object>();
		rpc.addProperty("name", name);
		rpc.addProperty("key", key);
		rpc.addProperty("jsonargs", jsonargs);
		// "{\"syxh\":190026,\"yexh\":0}"
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;﻿
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// 调用WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {
			System.out.println("调服务异常信息" + e.getMessage());
			e.printStackTrace();
			
			return null;

		}
		// 获取返回的数据
		SoapObject object = (SoapObject) envelope.bodyIn;
		// 获取返回的结果
		String result = object.getProperty(0).toString();
		
		String s = result;
		return result;
	}

	
	public static String getRemoteRecordInfo(String name, String key, String jsonargs) {
		// 命名空间
		String nameSpace = "http://tempuri.org/";
		// 调用的方法名称
		String methodName = "get";
		// EndPoint
		String endPoint = "http://192.168.10.57:8077/data.asmx";
		// SOAP Action
		String soapAction = "http://tempuri.org/get";
		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		Map<String, Object> map = new HashMap<String, Object>();

		rpc.addProperty("name", name);
		rpc.addProperty("key", key);
		rpc.addProperty("jsonargs", jsonargs);

		// "{\"syxh\":190026,\"yexh\":0}"

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// 调用WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {

			System.out.println("调服务异常信息" + e.getMessage());
			e.printStackTrace();
			
			return null;

		}
		// 获取返回的数据
		SoapObject object = (SoapObject) envelope.bodyIn;
		// 获取返回的结果
		String result = object.getProperty(0).toString();
		
		String s = result;
//		int p = 2000;
//		long length = s.length();
//		if (length < p || length == p)
//			Log.e("json返回", s);
//		else {
//			while (s.length() > p) {
//				String logContent = s.substring(0, p);
//				s = s.replace(logContent, "");
//				Log.e("json返回", logContent);
//			}
//			Log.e("json返回", s);
//		}
		// 将WebService返回的结果显示在TextView中
		return result;
	}

	public static String postRemoteInfo(String name, String key, String jsonargs) {
		// 命名空间
		String nameSpace = "http://tempuri.org/";
		// 调用的方法名称
		String methodName = "post";
		// EndPoint
//	String endPoint = WebUtils.WEBSERVICE;
		String endPoint = GlobalCache.getCache().getUrl();

		// SOAP Action
		String soapAction = "http://tempuri.org/post";
		// 指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId

		rpc.addProperty("name", name);
		rpc.addProperty("key", key);
		rpc.addProperty("jsonargs", jsonargs);

		// "{\"syxh\":190026,\"yexh\":0}"

		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = true;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);

		HttpTransportSE transport = new HttpTransportSE(endPoint);
		try {
			// 调用WebService
			transport.call(soapAction, envelope);
		} catch (Exception e) {

			System.out.println("调服务异常信息" + e.getMessage());

			e.printStackTrace();
		}

		// 获取返回的数据
		SoapObject object = (SoapObject) envelope.bodyIn;
		// 获取返回的结果
		String result = object.getProperty(0).toString();
		System.out.println(result);
		// 将WebService返回的结果显示在TextView中
		return result;
	}
}
