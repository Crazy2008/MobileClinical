package com.winning.mobileclinical.action;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.model.DrInfo;
import com.winning.mobileclinical.model.MediaList;
import com.winning.mobileclinical.model.cis.BookmarkInfo;
import com.winning.mobileclinical.model.cis.PatDailyRecordInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NoteAction {
	
	public static List<DrInfo> getDoctorDailyRecordList(Context context, String jsonargs) {
		List<DrInfo> list = new ArrayList<DrInfo>();
		String name = "dailyrecord";
		String key = "dr-all";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
//		System.out.println("result===="+result);
		
		if(result == null || result=="null") {
			return null;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Gson gson = new Gson();
			System.out.println("dailyrecord===="+json.getString("data"));
			list = gson.fromJson(json.getString("data"),
                new TypeToken<List<DrInfo>>() {
                }.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static List<PatDailyRecordInfo> getBQRecordList(Context context, String jsonargs) {
		List<PatDailyRecordInfo> list = new ArrayList<PatDailyRecordInfo>();
		String name = "dailyrecord";
		String key = "dr-pat";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
//		System.out.println("result===="+result);
		
		if(result == null || result=="null") {
			return null;
		}
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Gson gson = new Gson();
			System.out.println("dailyrecord===="+json.getString("data"));
			list = gson.fromJson(json.getString("data"),
                new TypeToken<List<PatDailyRecordInfo>>() {
                }.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	public static List<MediaList> getMediaInfo(Context context, String jsonargs) {
		List<MediaList> list = new ArrayList<MediaList>();
		String name = "dailyrecord";
		String key = "dr-media";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		if(result == null || result=="null") {
			return null;
		}
//		System.out.println("resultmedia===="+result);
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Gson gson = new Gson();
			System.out.println("resultmedia===="+json.getString("data"));
			list = gson.fromJson(json.getString("data"),
                new TypeToken<List<MediaList>>() {
                }.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fasfas===="+list.size());
		return list;
	}

	/**
	 *  获得病历涂鸦的图片
	 * @param jsonargs
	 * @return
     */
	public static List<BookmarkInfo>getEmrBookmark(String jsonargs ){
		List<BookmarkInfo> list=new ArrayList<>();
		String name="emr";
		String key="bookmark";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);

		if(result == null || result=="null") {
			return null;
		}

		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Gson gson = new Gson();
			System.out.println("resultmedia===="+json.getString("data"));
			list = gson.fromJson(json.getString("data"),
					new TypeToken<List<BookmarkInfo>>() {
					}.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fasfas===="+list.size());
		return list;
	}

	public static List<MediaList> getMediaInfo_offline(Context context, String jsonargs, int cfxh) {
		List<MediaList> list = new ArrayList<MediaList>();
		String name = "dailyrecord";
		String key = "dr-media";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);

		if(result == null || result=="null") {
			return null;
		}

//		System.out.println("resultmedia===="+result);
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Gson gson = new Gson();
			System.out.println("resultmedia===="+json.getString("data"));
			list = gson.fromJson(json.getString("data"),
					new TypeToken<List<MediaList>>() {
					}.getType());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("fasfas===="+list.size());

		for(int i=0;i < list.size();i++){
			list.get(i).setXh(cfxh);
		}
		return list;
	}

	//删除图片音频信息
	public static void deleteMedia(Context context, String jsonargs){
		String name = "dailyrecord";
		String key = "del-media";
		System.out.println("删除xh===="+jsonargs);
		String result = UtilsAction.postRemoteInfo(name, key, jsonargs);
		
		System.out.println("删除返回结果"+result);
	}
	public static boolean deleteBookMark(String jsonargs) {
		boolean flag=false;
		String name = "emr";
		String key = "del-mark";
		System.out.println("删除xh====" + jsonargs);
		String result = UtilsAction.postRemoteInfo(name, key, jsonargs);
		try {
			JSONObject jsonObject = new JSONObject(result);
			flag = jsonObject.getBoolean("success");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("删除返回结果" + result);
		return flag;
	}


}
