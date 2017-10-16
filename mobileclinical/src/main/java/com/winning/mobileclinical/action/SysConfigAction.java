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
import com.winning.mobileclinical.model.cis.MisConfigInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.model.cis.TableClass;
import com.winning.mobileclinical.model.cis.WardInfo;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.SystemUtil;
import com.winning.mobileclinical.web.WebUtils;

public class SysConfigAction {
	
	
	@SuppressWarnings("null")
	public static List<TableClass> getTableClasses(Context context, String jsonargs)
	{
		List<TableClass> list = new ArrayList<TableClass>();
		String name = "help";
		String key = "map";
		String result = UtilsAction.getRemoteInfo(name, key, "");
//		SystemUtil.RusultRequrst(context, result);
		
		if(result == null || result=="null") {
			return null;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			if(json.getString("success")  == "true") {
				Gson gson = new Gson();
				list = gson.fromJson(json.getString("data"),
	                new TypeToken<List<TableClass>>() {
	                }.getType());
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public static List<MisConfigInfo> getMisConfigInfo(Context context, String jsonargs) {
		List<MisConfigInfo> list = new ArrayList<MisConfigInfo>();
		String name = "config";
		String key = "mis-all";
		String result = UtilsAction.getRemoteInfo(name, key, "");
//		SystemUtil.RusultRequrst(context, result);
		
		
		if(result == null || result=="null") {
			return null;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			if(json.getString("success")  == "true") {
				Gson gson = new Gson();
				list = gson.fromJson(json.getString("data"),
	                new TypeToken<List<MisConfigInfo>>() {
	                }.getType());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
//		for(int i=0;i<list.size();i++){
//			System.out.println("系统配置返回==="+list.get(i).getConfigvalue());
//		}
		return list;
	}
}
