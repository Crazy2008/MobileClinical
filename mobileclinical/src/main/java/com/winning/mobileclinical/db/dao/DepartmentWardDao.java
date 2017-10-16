package com.winning.mobileclinical.db.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author liu
 *
 */
public class DepartmentWardDao {

	/**
	 * 
	 * @param userid
	 * @return PATIENT 根据用户名返回对应病区科室
	 */
	public static List<DeptWardMapInfo> getbqksList(Context context, String ysdm) {
		List<DeptWardMapInfo> list = new ArrayList<DeptWardMapInfo>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = DBHelper.getInstance(context).getReadableDatabase();
			if (db != null && db.isOpen()) {
				cursor = db.rawQuery("SELECT * FROM MOBILE_DEPTWARDMAPINFO where YSDM=?", new String[] { ysdm.trim() });
				DeptWardMapInfo depward = null;
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					depward = new DeptWardMapInfo();
					depward.setBqdm(cursor.getString(cursor.getColumnIndex("BQDM")));
					depward.setBqmc(cursor.getString(cursor.getColumnIndex("BQMC")));
					depward.setKsdm(cursor.getString(cursor.getColumnIndex("KSDM")));
					depward.setKsmc(cursor.getString(cursor.getColumnIndex("KSMC")));
					list.add(depward);
					cursor.moveToNext();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
			DBHelper.close(db);
		}
		return list;
	}

	
	//添加职工对应科室病区
	public static synchronized void insert(Context context,List<DeptWardMapInfo> list,String ysdm){
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		
		try {
			for(DeptWardMapInfo deptWardMapInfo:list){
				ContentValues cv = new ContentValues();
				cv.put("YSDM", ysdm);
				cv.put("BQDM", deptWardMapInfo.getBqdm());
				cv.put("BQMC", deptWardMapInfo.getBqmc());
				cv.put("KSDM", deptWardMapInfo.getKsdm());
				cv.put("KSMC", deptWardMapInfo.getKsmc());
				if (db != null && db.isOpen()) {
					db.insert("MOBILE_DEPTWARDMAPINFO", null, cv);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
	}
	
	/**
	 * 
	 * @param bqdm
	 * @return boolean 删除某用户对应科室病区信息
	 */
	public static synchronized boolean deleteDeptWardMapInfo(Context context, DeptWardMapInfo deptWardMapInfo) {
		boolean result = false;
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		if (db != null && db.isOpen()) {
			try {
				db.execSQL("DELETE FROM MOBILE_DEPTWARDMAPINFO WHERE BQDM =? and KSDM=?",
						new String[] { deptWardMapInfo.getBqdm(), deptWardMapInfo.getKsdm()});
				result = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				db.close();
			}
		}
		return result;

	}
}


