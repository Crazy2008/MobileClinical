package com.winning.mobileclinical.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.OrderInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author liu
 *
 */
public class OrderMessageDao {

	/**
	 * 
	 * @return OrderInfo  查询医嘱
	 */
	public static List<OrderInfo> getOrderlist(Context context, String syxh, String cisxh, String lb,boolean isgetnew) {

		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		List<OrderInfo> list = new ArrayList<OrderInfo>();
		String sql = "";
		sql = "select * from  MOBILE_JSONDATA where SYXH=? and JSONLB='YZ'";
		
		if (db != null && db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, new String[] { syxh });
			cursor.moveToFirst();
			OrderInfo order = null;
			while (!cursor.isAfterLast()) {
				order = new OrderInfo();
				
				list.add(order);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return list;

	}
	
	/**
	 * 
	 * @return OrderInfo  插入医嘱
	 */
	public static synchronized Boolean insert(Context context, List<OrderInfo> orderInfos) {
		boolean result = true;
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		
		if (db != null && db.isOpen()) {
			db.beginTransaction();
			try {
				
				if (orderInfos != null && orderInfos.size() > 0) {
					for (OrderInfo orderInfo : orderInfos) {
						insertorderInfo(db,orderInfo);
					}
				}
				db.setTransactionSuccessful();
				result = true;
			} catch (SQLException e) {
				result = false;
			} finally {
				db.endTransaction();
				db.close();
			}
		}
		return result;
	}
	
	public static void insertorderInfo(SQLiteDatabase db,OrderInfo orderInfo){
		ContentValues cv = new ContentValues();
		cv.put("SYXH", ""+orderInfo.getSyxh());
		cv.put("YEXH", ""+orderInfo.getYexh());
		cv.put("KSDM", orderInfo.getKsdm());
		cv.put("BQDM", orderInfo.getBqdm());
		cv.put("JSON", orderInfo.getYznr());
		try {
			if (db != null && db.isOpen()) {
				db.insert("MOBILE_JSONDATA", null, cv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


