package com.winning.mobileclinical.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.model.cis.FileInfo;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.SystemUtil;
import com.winning.mobileclinical.web.WebUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ImageAction {
	
	public static List<FileInfo> getReportImageList(Context context, String jsonargs) {
		List<FileInfo> list = new ArrayList<FileInfo>();
		String name = "ris";
		String key = "patient-img";
		String result = UtilsAction.getRemoteInfo(name, key, jsonargs);
		
		
		SystemUtil.RusultRequrst(context, result);
		
		JSONObject json = null;
		try {
			json = new JSONObject(result);
			Gson gson = new Gson();
			
			if(json.getString("success") == "true") {
				list = gson.fromJson(json.getString("data"),
						new TypeToken<List<FileInfo>>() {
				}.getType());
			} else {
				return null;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	public static Bitmap getReportImage(Context context,String imgurl) {
		
		byte[] imagedata = HTTPGetTool.getTool().ImageDownLoad(context,
				WebUtils.HOST );
		Bitmap bitmap = null;
		try{
			if (imagedata != null) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				bitmap = BitmapFactory.decodeByteArray(imagedata, 0,
						imagedata.length,opts);
				opts.inJustDecodeBounds = false;
				
				bitmap = BitmapFactory.decodeByteArray(imagedata, 0,
						imagedata.length,opts);
			}
		} catch (JsonIOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
}
