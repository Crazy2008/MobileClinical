package com.winning.mobileclinical.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.TempPatient;
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.OrderInfo;
import com.winning.mobileclinical.model.cis.TableClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;

/**
 * @author liu
 * 
 */
public class CommonJsonDao {

	/**
	 * 
	 * @return CommonJson
	 */
	public static List<CommonJson> getOrderlist(Context context, String syxh,
			String jsonlb) {

		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		List<CommonJson> list = new ArrayList<CommonJson>();
		String sql = "";
		sql = "select * from  MOBILE_JSONDATA where SYXH=? and TYPE=?";

		if (db != null && db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, new String[] { syxh, jsonlb });
			cursor.moveToFirst();
			CommonJson commonJson = null;
			while (!cursor.isAfterLast()) {
				commonJson = new CommonJson();
				commonJson.setBqdm(cursor.getString(cursor
						.getColumnIndex("BQDM")));
				commonJson.setKsdm(cursor.getString(cursor
						.getColumnIndex("KSDM")));
				commonJson.setSyxh(cursor.getString(cursor
						.getColumnIndex("SYXH")));
				commonJson.setYexh(cursor.getString(cursor
						.getColumnIndex("YEXH")));
				commonJson.setType(cursor.getString(cursor
						.getColumnIndex("TYPE")));
				commonJson.setJson(cursor.getString(cursor
						.getColumnIndex("JSON")));
				list.add(commonJson);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return list;
	}

	// 插入医嘱信息
	public static void insert(Context context, List<CommonJson> list) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			for (CommonJson coJson : list) {
				ContentValues cv = new ContentValues();
				// cv.put("YZXH", yz.getXh());
				// cv.put("SYXH", yz.getSyxh());
				// cv.put("YEXH", yz.getYexh());

				if (db != null && db.isOpen()) {
					db.insert("MOBILE_JSONDATA", null, cv);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	/**
	 * 
	 * @return boolean 删除医嘱
	 */
	public static synchronized boolean deleteYzlist(Context context) {
		boolean result = false;
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		if (db != null && db.isOpen()) {
			try {
				db.execSQL("DELETE FROM MOBILE_JSONDATA", new String[] {});
				result = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				db.close();
			}
		}
		return result;

	}

	public static synchronized List<CommonJson> getJSONDATA(Context context,
			List<TableClass> table) {
		List<CommonJson> list = new ArrayList<CommonJson>();
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {
			db = DBHelper.getInstance(context).getReadableDatabase();
			if (db != null && db.isOpen()) {
				cursor = db.rawQuery("SELECT * FROM "
						+ table.get(0).getClassName() + table.get(0).getType(),
						null);
				CommonJson common = null;
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					common = new CommonJson();
					common.setSyxh(cursor.getString(cursor
							.getColumnIndex("SYXH")));
					common.setYexh(cursor.getString(cursor
							.getColumnIndex("YEXH")));
					common.setKsdm(cursor.getString(cursor
							.getColumnIndex("KSDM")));
					common.setBqdm(cursor.getString(cursor
							.getColumnIndex("BQDM")));
					common.setType(cursor.getString(cursor
							.getColumnIndex("TYPE")));
					common.setJson(cursor.getString(cursor
							.getColumnIndex("JSON")));
					list.add(common);
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
	
	
	public static synchronized List<CommonJson> getJSONDATA(Context context,
			TableClass table, String sql) {
		List<CommonJson> list = new ArrayList<CommonJson>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
System.out.println("sql" + "SELECT * FROM "+ table.getClassName() + table.getType() + " where  " + sql.toString());
		try {
			db = DBHelper.getInstance(context).getReadableDatabase();
			if (db != null && db.isOpen()) {
				cursor = db.rawQuery("SELECT * FROM "
						+ table.getClassName() + table.getType() + " where " + sql.toString() ,
						null);
				CommonJson common = null;
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					common = new CommonJson();
					common.setSyxh(cursor.getString(cursor
							.getColumnIndex("SYXH")));
					common.setYexh(cursor.getString(cursor
							.getColumnIndex("YEXH")));
					common.setKsdm(cursor.getString(cursor
							.getColumnIndex("KSDM")));
					common.setBqdm(cursor.getString(cursor
							.getColumnIndex("BQDM")));
					common.setType(cursor.getString(cursor
							.getColumnIndex("TYPE")));
					common.setJson(cursor.getString(cursor
							.getColumnIndex("JSON")));
					list.add(common);
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

	public static void insertJSONDATA(Context context, List<CommonJson> list,
			List<TableClass> table) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			for (CommonJson common : list) {
				ContentValues cv = new ContentValues();
				cv.put("SYXH", common.getSyxh());
				cv.put("YEXH", common.getYexh());
				cv.put("KSDM", common.getKsdm());
				cv.put("BQDM", common.getBqdm());
				cv.put("TYPE", common.getType());
				cv.put("JSON", common.getJson());
				if (db != null && db.isOpen()) {
					db.insert(table.get(0).getClassName()
							+ table.get(0).getType(), null, cv);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	public static void insertJSONDATA(Context context, List<CommonJson> list,
			TableClass table) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			for (CommonJson common : list) {
				ContentValues cv = new ContentValues();
				cv.put("SYXH", common.getSyxh());
				cv.put("YEXH", common.getYexh());
				cv.put("KSDM", common.getKsdm());
				cv.put("BQDM", common.getBqdm());
				cv.put("TYPE", common.getType());
				cv.put("JSON", common.getJson());
				if (db != null && db.isOpen()) {
					db.insert(table.getClassName()
							+ table.getType(), null, cv);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public static void insertJSONDATA(Context context, CommonJson common, List<TableClass> table) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			ContentValues cv = new ContentValues();
			cv.put("SYXH", common.getSyxh());
			cv.put("YEXH", common.getYexh());
			cv.put("KSDM", common.getKsdm());
			cv.put("BQDM", common.getBqdm());
			cv.put("TYPE", common.getType());
			cv.put("JSON", common.getJson());
			if (db != null && db.isOpen()) {
				db.insert(table.get(0).getClassName() + table.get(0).getType(),
						null, cv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}
	
	
	// 添加病人信息
	public static void insertJSON(Context context, CommonJson common, TableClass table) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			ContentValues cv = new ContentValues();
			cv.put("SYXH", common.getSyxh());
			cv.put("YEXH", common.getYexh());
			cv.put("KSDM", common.getKsdm());
			cv.put("BQDM", common.getBqdm());
			cv.put("TYPE", common.getType());
			cv.put("JSON", common.getJson());
			if (db != null && db.isOpen()) {
				db.insert(table.getClassName() + table.getType(),
						null, cv);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

	public static List<TempPatient> getTempPatient(Context context,
			String ksdm, String bqdm) {
		List<TempPatient> list = new ArrayList<TempPatient>();
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {
			db = DBHelper.getInstance(context).getReadableDatabase();
			if (db != null && db.isOpen()) {
				cursor = db.rawQuery("SELECT * FROM PatientData0 WHERE BQDM="+bqdm+"  AND  KSDM = "+ksdm+" "
						,
						null);
				TempPatient common = null;
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					common = new TempPatient();
					common.setSyxh(cursor.getString(cursor
							.getColumnIndex("SYXH")));
					common.setYexh(cursor.getString(cursor
							.getColumnIndex("YEXH")));
					list.add(common);
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

}
