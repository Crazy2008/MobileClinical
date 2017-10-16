package com.winning.mobileclinical.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.MenuDTO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author liu
 *
 */
public class MenuDao {

	/**
	 * 
	 * @return Menu 菜单
	 */
	public static List<MenuDTO> getMenu(Context context) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		List<MenuDTO> list = new ArrayList<MenuDTO>();
		if (db != null && db.isOpen()) {
			Cursor cursor = db.rawQuery(
    				"SELECT * FROM DOCTOUCH_MENU",new String[] {});
			cursor.moveToFirst();
			MenuDTO menu = null;
			while (!cursor.isAfterLast()) {
				menu = new MenuDTO();
				menu.setText(cursor.getString(cursor.getColumnIndex("TEXT")));
				menu.setNote(cursor.getString(cursor.getColumnIndex("NOTE")));
				menu.setSuptype(cursor.getInt(cursor.getColumnIndex("SUPTYPE")));
				menu.setIntent(cursor.getString(cursor.getColumnIndex("INTENT")));
				menu.setOrderby(cursor.getInt(cursor.getColumnIndex("ORDERBY")));
				menu.setState(cursor.getInt(cursor.getColumnIndex("STATE")));
				menu.setIslcljbz(cursor.getInt(cursor.getColumnIndex("LCLJBZ")));
				menu.setImageurl(cursor.getString(cursor.getColumnIndex("IMAGEURL")));
				list.add(menu);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
		}
		return list;

	}

	
	//添加菜单信息
	public static void insert(Context context,List<MenuDTO> list){		
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			for(MenuDTO menu:list){
				ContentValues cv = new ContentValues();
				cv.put("TEXT", menu.getText());
				cv.put("NOTE", menu.getNote());
				cv.put("SUPTYPE", menu.getSuptype()+"");
				cv.put("INTENT", menu.getIntent());
				cv.put("ORDERBY", menu.getOrderby()+"");
				cv.put("STATE", menu.getState()+"");
				cv.put("LCLJBZ", menu.getLcljbz()+"");
				cv.put("IMAGEURL", menu.getImageurl()+"");
				
				if (db != null && db.isOpen()) {
					db.insert("DOCTOUCH_MENU", null, cv);
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
	 * @return boolean 删除菜单
	 */
	public static synchronized boolean deleteBrsys(Context context) {
		boolean result = false;
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		if (db != null && db.isOpen()) {
			try {
				db.execSQL("DELETE FROM DOCTOUCH_MENU",
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


