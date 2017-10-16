package com.winning.mobileclinical.action;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.OrderInfo;
public class OrdersMessageAction {
	/**
	 * @param 获取患者医嘱 
	 */
	public static OrderInfo getPatientOrders(Context context, String name, String key,String jsonargs)
	{
		
		OrderInfo orderInfo = new OrderInfo();
		
		jsonargs = "{\"syxh\":190026}";
		name = "order";
		key = "patient-order";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			Gson gson = new Gson();
			orderInfo = gson.fromJson(json.getString("data"),orderInfo.getClass());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new Gson().toJson(orderInfo));
		
		return orderInfo;
		
	}
	
	/**
	 * @param 获取一些患者的医嘱 
	 */
	public static List<OrderInfo> getPatientsOrders(Context context, String name, String key,String jsonargs)
	{
		
		List<OrderInfo> list = new ArrayList<OrderInfo>();
		
		jsonargs = "{\"keys\":\"190026-0,190326-0,230717-0\"}";
		name = "order";
		key = "list-order";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			Gson gson = new Gson();
			
			list = gson.fromJson(json.getString("data"),
	                new TypeToken<List<OrderInfo>>() {
	                }.getType());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new Gson().toJson(list));
		
		return list;
		
	}
	
	/**
	 * @param 获取指定病区患者的医嘱 
	 */
	public static List<CommonJson> getWardPatientOrders(Context context, String jsonargs)
	{
		
		List<CommonJson> list = new ArrayList<CommonJson>();
		
//		jsonargs = "{\"ksdm\":\"3106\",\"bqdm\":\"8122\"}";
		String name = "order";
		String key = "ward-order";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			Gson gson = new Gson();
			list = gson.fromJson(json.getString("data"),new TypeToken<List<CommonJson>>() {}.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new Gson().toJson(list));
		
		return list;
		
	}
}
