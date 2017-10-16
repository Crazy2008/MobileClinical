package com.winning.mobileclinical.action;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.model.cis.CommonJson;

/**
 * 获取公共json
 * @author liu
 *
 */

public class CommonJsonAction {
	public static CommonJson getCommoninfo(Context context, String name, String key, String jsonargs) {
		CommonJson info = new CommonJson();
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		if(result == null || result=="null") {
			return null;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			Gson gson = new Gson();
			info = gson.fromJson(json.getString("data"),
					info.getClass());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(name + "--" + key + "--值"+ new Gson().toJson(info));
		return info;
	}
	
	public static List<CommonJson> getCommoninfoList(Context context, String name, String key, String jsonargs) {
		List<CommonJson> info = new ArrayList<CommonJson>();
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		if(result == null || result=="null") {
			return null;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			Gson gson = new Gson();
			info = gson.fromJson(json.getString("data"),new TypeToken<List<CommonJson>>() {}.getType());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(name + "--" + key + "--值"+ new Gson().toJson(info));
		return info;
	}
	
}
