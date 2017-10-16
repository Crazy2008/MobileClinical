package com.winning.mobileclinical.db.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.MenuDTO;
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.TableClass;

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
public class SysConfigDao {

	public static synchronized void insert(Context context,List<TableClass> tables){		
		
		delete(context);
		
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			for(TableClass tableClass:tables){
				ContentValues cv = new ContentValues();
				cv.put("PROVIDER", tableClass.getProvider());
				cv.put("SERVICE", tableClass.getService());
				cv.put("CLASSNAME", tableClass.getClassName());
				cv.put("SINGLE", tableClass.getSingle());
				cv.put("TYPE", tableClass.getType());
				if (db != null && db.isOpen()) {
					db.insert("MOBILE_CONFIG", null, cv);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
	}
	
	public static synchronized List<TableClass> getTableList(Context context){
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		List<TableClass> list = new ArrayList<TableClass>();
		if (db != null && db.isOpen()) {
			Cursor cursor = db.rawQuery(
    				"SELECT * FROM MOBILE_CONFIG ",new String[] { });
			cursor.moveToFirst();
			TableClass table = null;
			while (!cursor.isAfterLast()) {
				table = new TableClass();
				table.setClassName(cursor.getString(cursor.getColumnIndex("CLASSNAME")));
				table.setProvider(cursor.getString(cursor.getColumnIndex("PROVIDER")));
				table.setService(cursor.getString(cursor.getColumnIndex("SERVICE")));
				table.setSingle(cursor.getInt(cursor.getColumnIndex("SINGLE")));
				table.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
				list.add(table);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return list;
	}
	
	public static List<TableClass> getSelect(Context context, String provider, String service) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		List<TableClass> list = new ArrayList<TableClass>();
		if (db != null && db.isOpen()) {
			Cursor cursor = db.rawQuery(
    				"SELECT * FROM MOBILE_CONFIG where PROVIDER=? and SERVICE=?",new String[] {provider, service});
			cursor.moveToFirst();
			TableClass table = null;
			while (!cursor.isAfterLast()) {
				table = new TableClass();
				table.setClassName(cursor.getString(cursor.getColumnIndex("CLASSNAME")));
				table.setProvider(cursor.getString(cursor.getColumnIndex("PROVIDER")));
				table.setService(cursor.getString(cursor.getColumnIndex("SERVICE")));
				table.setSingle(cursor.getInt(cursor.getColumnIndex("SINGLE")));
				table.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
				list.add(table);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return list;
	}
	
	
	public static TableClass getValue(Context context, String provider, String service) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		TableClass table = new TableClass();
		Cursor cursor = null;
		if (db != null && db.isOpen()) {
	
			cursor = db.rawQuery("SELECT * FROM MOBILE_CONFIG where PROVIDER=? and SERVICE=?",new String[] {provider, service});
			if (cursor.moveToFirst()) {
				table = new TableClass();
				table.setClassName(cursor.getString(cursor.getColumnIndex("CLASSNAME")));
				table.setProvider(cursor.getString(cursor.getColumnIndex("PROVIDER")));
				table.setService(cursor.getString(cursor.getColumnIndex("SERVICE")));
				table.setSingle(cursor.getInt(cursor.getColumnIndex("SINGLE")));
				table.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
			}
		} 
		return table;
	}
	
	
	/**
	 * 
	 * @param bqdm
	 * @return boolean 删除某用户对应科室病区信息
	 */
	public static synchronized boolean delete(Context context) {
		boolean result = false;
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		if (db != null && db.isOpen()) {
			try {
				db.execSQL("DELETE FROM MOBILE_CONFIG ",
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


