package com.winning.mobileclinical.action;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.MenuDTO;
import com.winning.mobileclinical.model.cis.DeptInfo;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.model.cis.WardInfo;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.SystemUtil;
import com.winning.mobileclinical.web.WebUtils;

public class DeptWardAction {
	/**
	 * @param 获取所有科室信息  ddd
	 */
	public static DeptInfo getAllDepts(Context context, String name, String key,String jsonargs)
	{
		
		DeptInfo deptInfo = new DeptInfo();
		
		jsonargs = "";
		name = "dept";
		key = "all-dept";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			Gson gson = new Gson();
			deptInfo = gson.fromJson(json.getString("data"),deptInfo.getClass());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new Gson().toJson(deptInfo));
		
		return deptInfo;
		
	}
	
	/**
	 * @param 获取所有病区信息 
	 */
	public static WardInfo getAllWards(Context context, String name, String key,String jsonargs)
	{
		
		WardInfo wardInfo = new WardInfo();
		
		jsonargs = "";
		name = "dept";
		key = "all-ward";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			Gson gson = new Gson();
			wardInfo = gson.fromJson(json.getString("data"),wardInfo.getClass());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new Gson().toJson(wardInfo));
		
		return wardInfo;
		
	}
	
	/**
	 * @param 获取科室病区对应信息 
	 */
	public static DeptWardMapInfo getAllMaps(Context context, String name, String key,String jsonargs)
	{
		
		DeptWardMapInfo deptwardMapInfo = new DeptWardMapInfo();
		
		jsonargs = "";
		name = "dept";
		key = "all-map";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
//		SystemUtil.RusultRequrst(context, result);
		
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			Gson gson = new Gson();
			deptwardMapInfo = gson.fromJson(json.getString("data"),deptwardMapInfo.getClass());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new Gson().toJson(deptwardMapInfo));
		
		return deptwardMapInfo;
		
	}
	
	/**
	 * @param 获取医生科室病区对应信息 
	 */
	public static DeptWardMapInfo getDoctorMaps(Context context, String name, String key,String jsonargs)
	{
		
		DeptWardMapInfo deptwardMapInfo = new DeptWardMapInfo();
		
		jsonargs = "{\"ysdm\":\"00\"}";
		name = "dept";
		key = "dr-map";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			System.out.println(json.getString("className"));
			Gson gson = new Gson();
			deptwardMapInfo = gson.fromJson(json.getString("data"),deptwardMapInfo.getClass());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new Gson().toJson(deptwardMapInfo));
		
		return deptwardMapInfo;
	}
	
	public static List<DeptWardMapInfo> getDeptWardMapInfos(Context context, String jsonargs)
	{
		List<DeptWardMapInfo> list = new ArrayList<DeptWardMapInfo>();
		String name = "dept";
		String key = "dr-map";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Gson gson = new Gson();
			list = gson.fromJson(json.getString("data"),
                new TypeToken<List<DeptWardMapInfo>>() {
                }.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
}
