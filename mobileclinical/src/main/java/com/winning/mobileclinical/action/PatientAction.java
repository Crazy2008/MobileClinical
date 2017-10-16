package com.winning.mobileclinical.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.HttpResult;
import com.winning.mobileclinical.web.WebUtils;

public class PatientAction {
	/**
	 * @param 病人列表 
	 */
	public static PatientInfo getPatient(Context context, String syxh, String yexh)
	{
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("syxh",syxh));
		params.add(new BasicNameValuePair("yexh",yexh));
		
		HttpResult res = HTTPGetTool.getTool().post2server(context,
				WebUtils.HOST +WebUtils.PATIENTLIST, params);
		
		PatientInfo patientInfo = new PatientInfo();
		
		try {
			if (res.getCode() == 1){
			String json= res.getJson().getString("result");
			Gson gson=new Gson();
			patientInfo = gson.fromJson(json,patientInfo.getClass());
		}} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return patientInfo;
	}
	
	
	
	
	public static CommonJson getPatientinfo(Context context, String jsonargs)
	{
		CommonJson patientInfo = new CommonJson();
		String name = "patient";
		String key = "info";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		JSONObject json = null;
		JSONObject jsondata = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			jsondata = new JSONObject(json.getString("data"));
			Gson gson = new Gson();
			patientInfo = gson.fromJson(jsondata.getString("json"),patientInfo.getClass());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("病人信息"+new Gson().toJson(patientInfo));
		return patientInfo;
	}
	
	//获取病区在院患者
	@SuppressWarnings("unchecked")
	public static List<CommonJson> getWardInPatients(Context context,String jsonargs)
	{
		
		List<CommonJson> common = new ArrayList<CommonJson>();
		
//		jsonargs = "{\"ksdm\":\"3106\",\"bqdm\":\"8122\"}";
		String name = "patient";
		String key = "all-inpaitient";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			System.out.println(json.getString("success"));
			System.out.println(json.getString("data"));
			Gson gson = new Gson();
//			common = gson.fromJson(json.getString("data"),common.getClass());
			common = gson.fromJson(json.getString("data"),new TypeToken<List<CommonJson>>() {}.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("整个病区"+new Gson().toJson(common));

		return common;
	}
	
	//获取病区出院患者
		@SuppressWarnings("unchecked")
		public static List<CommonJson> GetWardLeavePatients(Context context,String jsonargs)
		{
			
			List<CommonJson> common = new ArrayList<CommonJson>();
			
//			jsonargs = "{\"ksdm\":\"3106\",\"bqdm\":\"8122\"}";
			String name = "patient";
			String key = "all-inpaitient";
			String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
			
			JSONObject json = null;
			try {
				json = new JSONObject(result);
				System.out.println(json.getString("success"));
				System.out.println(json.getString("data"));
				Gson gson = new Gson();
//				common = gson.fromJson(json.getString("data"),common.getClass());
				common = gson.fromJson(json.getString("data"),new TypeToken<List<CommonJson>>() {}.getType());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(new Gson().toJson(common));
			System.out.println("整个病区");

			return common;
		}
}
