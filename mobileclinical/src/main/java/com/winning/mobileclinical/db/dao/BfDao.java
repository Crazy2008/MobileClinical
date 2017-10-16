package com.winning.mobileclinical.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.cis.Bedinfo;
import com.winning.mobileclinical.model.cis.CommonJson;
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
public class BfDao {


	public static synchronized List<Bedinfo> getJSONDATA(Context context, String bqdm) {
		List<Bedinfo> list = new ArrayList<Bedinfo>();
		Cursor cursor = null;
		SQLiteDatabase db = null;

		try {
			db = DBHelper.getInstance(context).getReadableDatabase();
			if (db != null && db.isOpen()) {
				cursor = db.rawQuery("SELECT * FROM MOBILE_BEDINFO WHERE BQDM =?" ,
						new String[]{bqdm});
				Bedinfo bedinfo = null;
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					bedinfo = new Bedinfo();
					bedinfo.setCwdm(cursor.getString(cursor.getColumnIndex("CWDM")));
					bedinfo.setBqdm(cursor.getString(cursor.getColumnIndex("BQDM")));
					bedinfo.setBqmc(cursor.getString(cursor.getColumnIndex("BQMC")));
					bedinfo.setRoom(cursor.getString(cursor.getColumnIndex("ROOM")));
					bedinfo.setKsdm(cursor.getString(cursor.getColumnIndex("KSDM")));
					bedinfo.setKsmc(cursor.getString(cursor.getColumnIndex("KSMC")));
					bedinfo.setCwfdm(cursor.getString(cursor.getColumnIndex("CWFDM")));
					bedinfo.setGfcwfdm(cursor.getString(cursor.getColumnIndex("GFCWFDM")));
					bedinfo.setZyysdm(cursor.getString(cursor.getColumnIndex("ZYYSDM")));
					bedinfo.setZzysdm(cursor.getString(cursor.getColumnIndex("ZZYSDM")));
					bedinfo.setZrysdm(cursor.getString(cursor.getColumnIndex("ZRYSDM")));
					bedinfo.setCwlx(cursor.getInt(cursor.getColumnIndex("CWLX")));
					bedinfo.setBzlx(cursor.getInt(cursor.getColumnIndex("BZLX")));
					bedinfo.setZcbz(cursor.getInt(cursor.getColumnIndex("ZCBZ")));
					bedinfo.setTxbz(cursor.getInt(cursor.getColumnIndex("TXBZ")));
					bedinfo.setQyid(cursor.getInt(cursor.getColumnIndex("QYID")));
					bedinfo.setQymc(cursor.getString(cursor.getColumnIndex("QYMC")));
					list.add(bedinfo);
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
	
	
	public static void insertJSONDATA(Context context, List<Bedinfo> list) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			for (Bedinfo bedinfo : list) {
				ContentValues cv = new ContentValues();
				cv.put("CWDM", bedinfo.getCwdm());
				cv.put("BQDM", bedinfo.getBqdm());
				cv.put("BQMC", bedinfo.getBqmc());
				cv.put("ROOM", bedinfo.getRoom());
				cv.put("KSDM", bedinfo.getKsdm());
				cv.put("KSMC", bedinfo.getKsmc());
				cv.put("CWFDM", bedinfo.getCwfdm());
				cv.put("GFCWFDM", bedinfo.getGfcwfdm());
				cv.put("ZYYSDM", bedinfo.getZyysdm());
				cv.put("ZZYSDM", bedinfo.getZzysdm());
				cv.put("ZRYSDM", bedinfo.getZrysdm());
				cv.put("CWLX", bedinfo.getCwlx());
				cv.put("BZLX", bedinfo.getBzlx());
				cv.put("ZCBZ", bedinfo.getZcbz());
				cv.put("TXBZ", bedinfo.getTxbz());
				cv.put("QYID", bedinfo.getQyid());
				cv.put("QYMC", bedinfo.getQymc());
				if (db != null && db.isOpen()) {
					db.insert("MOBILE_BEDINFO", null, cv);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
	}

}
