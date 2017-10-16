package com.winning.mobileclinical.db.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.DrInfo;
import com.winning.mobileclinical.model.cis.Bedinfo;
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.FileInfo;
import com.winning.mobileclinical.model.cis.OrderInfo;
import com.winning.mobileclinical.model.cis.TableClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author liu
 * 
 */
public class ImageDao {


	public static synchronized List<FileInfo> getFileInfos(Context context, String syxh, String yexh) {
		
		

		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		List<FileInfo> list = new ArrayList<FileInfo>();
		
		if (db != null && db.isOpen()) {
			String sql = "SELECT * FROM MIS_IMAGE WHERE SYXH=? ";
			try {
				Cursor cursor=db.rawQuery(sql,new String[]{syxh});
				while (cursor.moveToNext()) {
					FileInfo fileinfo = new FileInfo();
					BigDecimal temp_syxh = new BigDecimal(cursor.getString(cursor
							.getColumnIndex("SYXH")));
					BigDecimal temp_yexh = new BigDecimal(cursor.getString(cursor
							.getColumnIndex("YEXH")));
					fileinfo.setSyxh(temp_syxh);
					fileinfo.setYexh(temp_yexh);
					fileinfo.setKsdm(cursor.getString(cursor.getColumnIndex("KSDM")));
					fileinfo.setBqdm(cursor.getString(cursor.getColumnIndex("BQDM")));
					fileinfo.setKey(cursor.getString(cursor.getColumnIndex("KEY")));
					fileinfo.setRes_id(cursor.getString(cursor.getColumnIndex("RES_ID")));
					fileinfo.setSrc(cursor.getString(cursor.getColumnIndex("SRC")));
					list.add(fileinfo);
				}
			} catch (android.database.SQLException e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
		}
		return list;
	}
	
	
	public static void insertFileInfos(Context context, List<FileInfo> list) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			for (FileInfo fileInfo : list) {
				ContentValues cv = new ContentValues();
				cv.put("SYXH", fileInfo.getSyxh().toString());
				cv.put("YEXH", fileInfo.getYexh().toString());
				cv.put("KSDM", fileInfo.getKsdm());
				cv.put("BQDM", fileInfo.getBqdm());
				cv.put("KEY", fileInfo.getKey());
				cv.put("RES_ID", fileInfo.getRes_id());
				cv.put("SRC", fileInfo.getSrc());
				if (db != null && db.isOpen()) {
					db.insert("MIS_IMAGE", null, cv);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

}
