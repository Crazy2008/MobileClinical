package com.winning.mobileclinical.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.Bwl;
import com.winning.mobileclinical.model.MCS_CFJL;
import com.winning.mobileclinical.model.MCS_CFJL_MX;
import com.winning.mobileclinical.model.MediaModel;

public class BwlDao{
	static BwlDao bwlDao=null;

	public static BwlDao getInstance(){
		if(bwlDao==null){
			bwlDao=new BwlDao();
		}
		
		return bwlDao;
	}
	
	//病区备忘录查询
	public List<Bwl> getBqBwl(Context context,String bqid, String czryid, String syxh, String yexh) {
	//String sql = "SELECT * FROM DOCTOUCH_BWL where BQ_ID= ? AND CJHSID <> ? AND TYPE= 1 AND JLZT=1";
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		List<Bwl> list=new ArrayList<Bwl>();
		
		if (db != null && db.isOpen()) {
			String sql = "SELECT * FROM DOCTOUCH_BWL where BQ_ID= ? AND CJHSID <> ? AND TYPE= 1 AND JLZT=1 AND SYXH=? AND YEXH=?";
			
			try {
				Cursor cursor=db.rawQuery(sql,new String[]{bqid,czryid,syxh,yexh});
				while (cursor.moveToNext()) {
					Bwl bwl = new Bwl();
					bwl.setXh(cursor.getString(cursor.getColumnIndex("XH")));
					bwl.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
					bwl.setContents(cursor.getString(cursor.getColumnIndex("CONTENTS")));
					bwl.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
					bwl.setGxsj(cursor.getString(cursor.getColumnIndex("GXSJ")));
					bwl.setCjsj(cursor.getString(cursor.getColumnIndex("CJSJ")));
					bwl.setCjhsid(cursor.getString(cursor.getColumnIndex("CJHSID")));
					bwl.setCjhsname(cursor.getString(cursor.getColumnIndex("CJHSNAME")));
					bwl.setJlzt(cursor.getString(cursor.getColumnIndex("JLZT")));
					bwl.setBq_id(cursor.getString(cursor.getColumnIndex("BQ_ID")));
					bwl.setBq_mc(cursor.getString(cursor.getColumnIndex("BQ_MC")));
					bwl.setSave_type(cursor.getInt(cursor.getColumnIndex("SAVE_TYPE")));
					list.add(bwl);
				}
				
				
			} catch (android.database.SQLException e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
		}
		
		return list;
	}

	//备忘录删除：更新状态
	public int deleteBwlById(Context context ,String id) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		int result=0;
		if (db != null && db.isOpen()) {
			
			ContentValues cv = new ContentValues();
			cv.put("JLZT", "0");
			try {
				result=db.update("DOCTOUCH_BWL",cv, "XH=?", new String[]{id});
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
		}
		
		return result;
	}

	//资源文件删除：更新状态
	public int deleteMediaById(Context context ,String id) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		int result=0;
		if (db != null && db.isOpen()) {
			
			ContentValues cv = new ContentValues();
			cv.put("JLZT", "0");
			try {
				result=db.update("DOCTOUCH_MEDIA",cv, "ID=?", new String[]{id});
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
		}
		
		return result;
	}
	
	//备忘录更新
	public int updateBwl(Context context,Bwl bwl) {
		//String updatesql = "UPDATE DOCTOUCH_BWL SET TITLE=?,CONTENTS=?,TYPE=?,GXSJ=? WHERE XH=?";
		
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		
		int result=0;
		if (db != null && db.isOpen()) {
			
			ContentValues cv = new ContentValues();
			cv.put("TITLE", bwl.getTitle());
			cv.put("CONTENTS", bwl.getContents());
			cv.put("TYPE", bwl.getType());
			cv.put("TXSJ", bwl.getTxsj());
			cv.put("GLBR", bwl.getGlbr());
			
			try {
				result=db.update("DOCTOUCH_BWL", cv, "XH=?",  new String[]{bwl.getXh()});
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
		}
		

		return result;
	}
	
	//插入本地查房记录
	public int insertCFJL(Context context, MCS_CFJL cfjl) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		int id=0;
		if (db != null && db.isOpen()) {
			try{
				
				ContentValues cv = new ContentValues();
				cv.put("SYXH", cfjl.getSyxh());
				cv.put("NAME", cfjl.getName());
				cv.put("Memo", cfjl.getMemo());
				cv.put("YSDM", cfjl.getYsdm());
				cv.put("YSMC", cfjl.getYsmc());
				cv.put("CJRQ", cfjl.getCjrq());
				cv.put("CLZT", cfjl.getClzt());
				
				id=(int)db.insert("MCS_CFJL", null,cv);
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
			
		}
		return id;
	}
	
	//插入本地查房记录明细
	public int insertCFJL_MX(Context context, MCS_CFJL_MX cfjlmx, String cfxh) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		int id=0;
		if (db != null && db.isOpen()) {
			try{
				ContentValues cv = new ContentValues();
				cv.put("CFXH", cfxh);
				cv.put("SYXH", cfjlmx.getSyxh());
				cv.put("FLAG", cfjlmx.getFlag());
				cv.put("CJRQ", cfjlmx.getCjrq());
				
				id=(int)db.insert("MCS_CFJL_MX", null,cv);
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
			
		}
		return id;
	}

	//增加备忘录
	public int addBwl(Context context,Bwl bwl) {

		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		int id=0;
		
		if (db != null && db.isOpen()) {
			try{
				
				ContentValues cv = new ContentValues();
				cv.put("TITLE",  bwl.getTitle());
				cv.put("SYXH", bwl.getSyxh());
				cv.put("YEXH", bwl.getYexh());
				cv.put("CONTENTS",  bwl.getContents());
				cv.put("TYPE", 0);
				cv.put("GXSJ",  bwl.getGxsj());
				cv.put("CJSJ",  bwl.getCjsj());
				cv.put("CJHSID", bwl.getCjhsid());
				cv.put("CJHSNAME",bwl.getCjhsname());
				cv.put("JLZT", 1);
				cv.put("BQ_ID",  bwl.getBq_id());
				cv.put("BQ_MC",  bwl.getBq_mc());
				cv.put("SAVE_TYPE", 1);
				cv.put("BQSHARE", 0);
				cv.put("TXSJ", bwl.getTxsj());
				cv.put("GLBR", bwl.getGlbr());
				
				id=(int)db.insert("DOCTOUCH_BWL", null,cv);
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
			
		}
		return id;
	}


	//获取备忘录
	public List<Bwl> getMyBwl(Context context,String czryid,String syxh, String yexh) {
		
		//String sql = "SELECT * from DOCTOUCH_BWL where CJHSID = ? and JLZT=1";
		
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		List<Bwl> list=new ArrayList<Bwl>();
		
		if (db != null && db.isOpen()) {
			String sql = "SELECT * from DOCTOUCH_BWL where  SYXH=? and YEXH=?";
			
			try {
				Cursor cursor=db.rawQuery(sql, new String[]{czryid,syxh,yexh});
				
				while(cursor.moveToNext()){
					Bwl bwl = new Bwl();
					bwl.setXh(cursor.getString(cursor.getColumnIndex("XH")));
					bwl.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
					bwl.setContents(cursor.getString(cursor.getColumnIndex("CONTENTS")));
					bwl.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
					bwl.setGxsj(cursor.getString(cursor.getColumnIndex("GXSJ")));
					bwl.setCjsj(cursor.getString(cursor.getColumnIndex("CJSJ")));
					bwl.setCjhsid(cursor.getString(cursor.getColumnIndex("CJHSID")));
					bwl.setCjhsname(cursor.getString(cursor.getColumnIndex("CJHSNAME")));
					bwl.setJlzt(cursor.getString(cursor.getColumnIndex("JLZT")));
					bwl.setBq_id(cursor.getString(cursor.getColumnIndex("BQ_ID")));
					bwl.setBq_mc(cursor.getString(cursor.getColumnIndex("BQ_MC")));
					bwl.setSave_type(cursor.getInt(cursor.getColumnIndex("SAVE_TYPE")));
					bwl.setTxsj(cursor.getString(cursor.getColumnIndex("TXSJ")));
					bwl.setGlbr(cursor.getString(cursor.getColumnIndex("GLBR")));
					bwl.setSyxh(cursor.getString(cursor.getColumnIndex("SYXH")));
					bwl.setYexh(cursor.getString(cursor.getColumnIndex("YEXH")));
					list.add(bwl);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
		}
		return list;
	}

	public int updateBwlTitle(Context context,String id, String title) {
		//String deletesql = "UPDATE DOCTOUCH_BWL SET TITLE=? WHERE ID=?";
		
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		int result=0;
		
		if (db != null && db.isOpen()) {
			ContentValues cv=new ContentValues();
			cv.put("TITLE", title);
			
			try {
				db.update("DOCTOUCH_BWL", cv, "ID=?", new String[]{id});
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
		}
		
		return result;
	}

	
	public int upload(Context context,String lb, String lbsm, String path, String bwlid,String description,String fileName,String syxh,String yexh) {

		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		
		int result = 0;
		if (db != null && db.isOpen()) {
			
			ContentValues cv=new ContentValues();
			cv.put("LB", Integer.parseInt(lb));
			cv.put("SYXH", syxh);
			cv.put("YEXH", yexh);
			cv.put("LBSM", lbsm);
			cv.put("SRC", path);
			cv.put("BWLID", bwlid);
			cv.put("DESCRIPTION", description);
			cv.put("JLZT", "1");
			cv.put("FILENAME", fileName);
			
			try {
				result=(int)db.insert("DOCTOUCH_MEDIA", null, cv);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}
		}

		return result;
	}

	
	public List<MediaModel> getMedia(SQLiteDatabase db,String id, String syxh, String yexh) {
		
		List<MediaModel> list=new ArrayList<MediaModel>();
		
		if(id != null && !id.equals("")) {
			int bwlId = Integer.parseInt(id);
			
			String sql = "SELECT * FROM DOCTOUCH_MEDIA WHERE  BWLID = " + bwlId +"  AND SYXH='"+syxh+"'AND YEXH='"+yexh +"'";
			
			Cursor cursor=db.rawQuery(sql, null);
			
			while(cursor.moveToNext()){
				MediaModel media=new MediaModel();
				media.setId(cursor.getString(cursor.getColumnIndex("ID")));
				media.setLb(cursor.getString(cursor.getColumnIndex("LB")));
				media.setSrc(cursor.getString(cursor.getColumnIndex("SRC")));
				media.setFileName(cursor.getString(cursor.getColumnIndex("FILENAME")));
				media.setDescription(cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
				media.setSyxh(cursor.getString(cursor.getColumnIndex("SYXH")));
				media.setYexh(cursor.getString(cursor.getColumnIndex("YEXH")));
				list.add(media);
			}
			
			cursor.close();
		}	
		
		return 	list;
	}

	
	public int updateBwl(Context context,String id, String title, String contents, String time) {
		int ID = 0;
		if(!id.equals("")) {
			ID = Integer.parseInt(id);
			
		} else {
			return 0;
		}
		//String deletesql = "UPDATE DOCTOUCH_BWL SET TITLE = ? ,CONTENTS = ? , GXSJ = ? WHERE XH=?";
		
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		int result = 0;
		
		ContentValues cv=new ContentValues();
		cv.put("TITLE", title);
		cv.put("CONTENTS", contents);
		cv.put("GXSJ", time);
		
		try {
			result=db.update("DOCTOUCH_BWL", cv, "XH=?", new String[]{ID+""});
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
		
		return result;
	}


	
	public int upLoadUpdate(Context context,String path, String id) {
		//String sql = "UPDATE DOCTOUCH_MEDIA SET SRC = ? WHERE ID = ?";
		
		int ID = 0;
		if(!id.equals("")) {
			ID = Integer.parseInt(id);
			
		} else {
			return 0;
		}
		
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		int result = 0;
		
		ContentValues cv=new ContentValues();
		cv.put("SRC", path);
		
		try {
			result=db.update("DOCTOUCH_MEDIA", cv, "ID=?", new String[]{ID+""});
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
		return result;
	}

	
	public int updateDescription(Context context,String text, String id) {
		//String sql = "UPDATE DOCTOUCH_MEDIA SET DESCRIPTION = ? WHERE ID = ?";
		
		int ID = 0;
		if(!id.equals("")) {
			ID = Integer.parseInt(id);
			
		} else {
			return 0;
		}
		
		
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		int result = 0;
		
		ContentValues cv=new ContentValues();
		cv.put("DESCRIPTION", text);
		
		try {
			result=db.update("DOCTOUCH_MEDIA", cv, "ID=?", new String[]{ID+""});
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			db.close();
		}
		
		return result;
	}

	//备忘录批处理删除:先删除备忘录，然后删除对应媒体库
	/*public boolean deleteBwl(SQLiteDatabase db,String xh){
		
		boolean isSuccess=false;
		
			
		int result=db.delete("DOCTOUCH_BWL", "XH=?",new String[]{xh});
		
		if(result>0){
			
			//查询是否存在对应的媒体资源
			List<MediaModel> list=getMedia(db,xh);
			
			if(list.size()>0){
				result=db.delete("DOCTOUCH_MEDIA","BWLID=?",new String[]{xh});
				if(result==list.size()){
					db.setTransactionSuccessful();
					isSuccess=true;
				}
			}else{
				db.setTransactionSuccessful();
				isSuccess=true;
				System.out.println("=======================");
				
			}
			
		}
		
		return isSuccess;
	}*/
}
