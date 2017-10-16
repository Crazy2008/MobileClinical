package com.winning.mobileclinical.action;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.WebUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 登录访问服务器
 * @author liu
 *
 */
public class LoginAction {
	public static JSONObject login(String username, String password) {
		JSONObject res = HTTPGetTool.getTool().login(
				getLoginURL(username, password));
		return res;
	}
	public static JSONObject login_test(String host) {
		JSONObject res = HTTPGetTool.getTool().login(
				host+WebUtils.LOGINACTION + "&username=&password=");
		return res;
	}
	private static String getLoginURL(String username, String password) {
		return WebUtils.HOST + WebUtils.LOGINACTION + "&username=" + username
				+ "&password=" + password;
	}
	
	public static DoctorInfo getDoctorInfo(Context context,String jsonargs)
	{
		
		DoctorInfo doctorInfo = new DoctorInfo();
		String name = "doctor";
		String key = "info";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		if(result == null || result=="null") {
			return null;
		}
		
//		SystemUtil.RusultRequrst(context, result);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			
			if(json.getString("success") == "true") {
				Gson gson = new Gson();
				doctorInfo = gson.fromJson(json.getString("data"),DoctorInfo.class);
			} else {
				return null;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new Gson().toJson(doctorInfo));
		return doctorInfo;
	}
	
	
	public static int getRemindCount(Context context,String jsonargs)
	{
		
		String name = "doctor";
		String key = "remind-count";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		int count = 0;
		if(result == null || result=="null") {
			return 0;
		}
		
//		SystemUtil.RusultRequrst(context, result);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			count = Integer.valueOf(json.getString("data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	
	public static int getRemindCount_test(Context context,String jsonargs)
	{
		
		String name = "doctor";
		String key = "remind-count";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		int count = 0;
		if(result == null || result=="null") {
			return 0;
		}
		
//		SystemUtil.RusultRequrst(context, result);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			count = Integer.valueOf(json.getString("data"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return count;
	}
	
	public static List<DeptWardMapInfo> getDeptWard(Context context,String jsonargs)
	{
		List<DeptWardMapInfo> deptWardMapInfos = new ArrayList<DeptWardMapInfo>();
		
		String name = "dept";
		String key = "dr-map";
		
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
//		SystemUtil.RusultRequrst(context, result);
		
		if(result == null || result=="null") {
			return null;
		}
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			
			if(json.getString("success") == "true") {
				Gson gson = new Gson();
				deptWardMapInfos = gson.fromJson(json.getString("data"),
		                new TypeToken<List<DeptWardMapInfo>>() {
		                }.getType());
			} else {
				return null;
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(new Gson().toJson(deptWardMapInfos));
		return deptWardMapInfos;
	}
}
