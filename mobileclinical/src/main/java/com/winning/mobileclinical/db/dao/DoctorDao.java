package com.winning.mobileclinical.db.dao;

import java.util.List;

import org.json.JSONObject;

import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.cis.DoctorInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author liu
 *
 */
public class DoctorDao {

	/**
	 * 获取操作员信息
	 */
	public static synchronized DoctorInfo getDoctor(Context context,String id,String password) {
		DoctorInfo doctorInfo = null;
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		Cursor cursor = null;
		try {
		
			if (db != null && db.isOpen()) {
		
				cursor = db.rawQuery("SELECT * FROM MOBILE_DOCTORINFO  WHERE RTRIM(ID)=? ",
						new String[] { id.trim() });
				if (cursor.moveToFirst()) {
					doctorInfo = new DoctorInfo();
					doctorInfo.setId(cursor.getString(cursor.getColumnIndex("ID")));
					doctorInfo.setName(cursor.getString(cursor.getColumnIndex("NAME")));
					doctorInfo.setPy(cursor.getString(cursor.getColumnIndex("PY")));
					doctorInfo.setWb(cursor.getString(cursor.getColumnIndex("WB")));
					doctorInfo.setSex(cursor.getString(cursor.getColumnIndex("SEX")));
					doctorInfo.setKsdm(cursor.getString(cursor.getColumnIndex("KSDM")));
					doctorInfo.setBqdm(cursor.getString(cursor.getColumnIndex("BQDM")));
					doctorInfo.setZglb(cursor.getString(cursor.getColumnIndex("ZGLB")));
//					doctorInfo.setYxdl((cursor.getInt(cursor.getColumnIndex("YXLD")))==1? true:false);
					doctorInfo.setZcdm(cursor.getString(cursor.getColumnIndex("ZCDM")));
					doctorInfo.setZcmc(cursor.getString(cursor.getColumnIndex("ZCMC")));
					doctorInfo.setPhone(cursor.getString(cursor.getColumnIndex("PHONE")));
					doctorInfo.setJlzt(Integer.valueOf(cursor.getString(cursor.getColumnIndex("JLZT"))));
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return doctorInfo;

	}

	/**
	 * 插入操作员信息
	 */
	public static void insert(Context context,DoctorInfo doctorInfo){
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		String delete = "DELETE FROM MOBILE_DOCTORINFO WHERE ID ='"+doctorInfo.getId()+"'";
		db.execSQL(delete);
		ContentValues cv = new ContentValues();
		cv.put("ID", doctorInfo.getId());
		cv.put("NAME", doctorInfo.getName());
		cv.put("PY", doctorInfo.getPy());
		cv.put("WB", doctorInfo.getWb());
		cv.put("SEX", doctorInfo.getSex());
		cv.put("KSDM", doctorInfo.getKsdm());
		cv.put("BQDM", doctorInfo.getBqdm());
		cv.put("ZGLB", doctorInfo.getZglb());
		cv.put("YXDL", (doctorInfo.getYxdl()==true?0:1));
		cv.put("ZCDM", doctorInfo.getZcdm());
		cv.put("ZCMC", doctorInfo.getZcmc());
		cv.put("PHONE", doctorInfo.getPhone());
		cv.put("JLZT", doctorInfo.getJlzt());
		try {
			if (db != null && db.isOpen()) {
				db.insert("MOBILE_DOCTORINFO", null, cv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			db.close();
		}
	}
	
	/**
	 * 
	 * @return boolean 删除医生信息
	 */
	public static synchronized boolean deleteDoctorInfo(Context context) {
		boolean result = false;
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		if (db != null && db.isOpen()) {
			try {
				db.execSQL("DELETE FROM MOBILE_DOCTORINFO ",
						new String[] {});
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


