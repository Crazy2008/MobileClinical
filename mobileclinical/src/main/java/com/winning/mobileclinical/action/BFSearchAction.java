package com.winning.mobileclinical.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.R.integer;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.db.dao.PatientDao;
import com.winning.mobileclinical.model.MenuDTO;
import com.winning.mobileclinical.model.cis.Bedinfo;
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.HttpResult;
import com.winning.mobileclinical.web.WebUtils;

public class BFSearchAction {
	//test
	//test
	public static List<Bedinfo> getBF(Context context, String jsonargs) {
		List<Bedinfo> list = new ArrayList<Bedinfo>();
		String name = "dept";
		String key = "ward-bed";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		if(result == null || result=="null") {
			return null;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Gson gson = new Gson();
			list = gson.fromJson(json.getString("data"),
                new TypeToken<List<Bedinfo>>() {
                }.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<Bedinfo> getBFMX(Context context, String jsonargs) {
		List<Bedinfo> list = new ArrayList<Bedinfo>();
		String name = "dept";
		String key = "dr-map";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		if(result == null || result=="null") {
			return null;
		}
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Gson gson = new Gson();
			list = gson.fromJson(json.getString("data"),
                new TypeToken<List<Bedinfo>>() {
                }.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	

}
