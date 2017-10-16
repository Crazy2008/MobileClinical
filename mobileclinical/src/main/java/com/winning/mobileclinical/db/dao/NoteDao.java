package com.winning.mobileclinical.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.winning.mobileclinical.action.NoteAction;
import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.DrInfo;
import com.winning.mobileclinical.model.MCS_IMAGE;
import com.winning.mobileclinical.model.MCS_CFJL;
import com.winning.mobileclinical.model.MCS_CFJL_MX;
import com.winning.mobileclinical.model.MediaList;
import com.winning.mobileclinical.model.MediaModel;
import com.winning.mobileclinical.model.cis.PatDailyRecordInfo;
import com.winning.mobileclinical.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NoteDao {
    static NoteDao noteDao = null;


    public static NoteDao getInstance() {
        if (noteDao == null) {
            noteDao = new NoteDao();
        }

        return noteDao;
    }

    //在线查房便签离线保存至本地
    public List<MCS_CFJL> getNote(Context context, String bqid, String czryid, String syxh, String yexh) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        List<MCS_CFJL> list = new ArrayList<MCS_CFJL>();

        if (db != null && db.isOpen()) {
            String sql = "SELECT * FROM MCS_CFJL where  SYXH=? AND　YEXH=?";
            try {
                Cursor cursor = db.rawQuery(sql, new String[]{syxh, yexh});
                while (cursor.moveToNext()) {
                    MCS_CFJL cfjl = new MCS_CFJL();
                    cfjl.setXh(cursor.getString(cursor.getColumnIndex("XH")));
                    cfjl.setSyxh(cursor.getString(cursor.getColumnIndex("SYXH")));
                    cfjl.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                    cfjl.setMemo(cursor.getString(cursor.getColumnIndex("MEMO")));
                    cfjl.setYsdm(cursor.getString(cursor.getColumnIndex("YSDM")));
                    cfjl.setYsmc(cursor.getString(cursor.getColumnIndex("YSMC")));
                    cfjl.setCjrq(cursor.getString(cursor.getColumnIndex("CJRQ")));
                    cfjl.setClzt(cursor.getInt(cursor.getColumnIndex("CLZT")));
                    list.add(cfjl);
                }
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return list;
    }


    public List<MCS_CFJL_MX> getNoteMX(Context context, String bqid, String czryid, String syxh, String yexh) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        List<MCS_CFJL_MX> list = new ArrayList<MCS_CFJL_MX>();

        if (db != null && db.isOpen()) {
            String sql = "SELECT * FROM MCS_CFJL_MX where  SYXH=? AND YEXH=?";

            try {
                Cursor cursor = db.rawQuery(sql, new String[]{bqid, czryid, syxh, yexh});
                while (cursor.moveToNext()) {
                    MCS_CFJL_MX cfjlmx = new MCS_CFJL_MX();
                    cfjlmx.setCfxh(cursor.getString(cursor.getColumnIndex("CFXH")));
                    cfjlmx.setSyxh(cursor.getString(cursor.getColumnIndex("SYXH")));
                    cfjlmx.setFlag(cursor.getInt(cursor.getColumnIndex("FLAG")));
                    cfjlmx.setCjrq(cursor.getString(cursor.getColumnIndex("CJRQ")));
                    list.add(cfjlmx);
                }
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return list;
    }


    public List<MCS_IMAGE> getMediaList(Context context, String bqid, String czryid, String syxh, String yexh) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        List<MCS_IMAGE> list = new ArrayList<MCS_IMAGE>();

        if (db != null && db.isOpen()) {
            String sql = "SELECT * FROM MCS_IMAGE where  SYXH=? AND YEXH=?";

            try {
                Cursor cursor = db.rawQuery(sql, new String[]{bqid, czryid, syxh, yexh});
                while (cursor.moveToNext()) {
                    MCS_IMAGE cfjlmx = new MCS_IMAGE();

                    list.add(cfjlmx);
                }
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return list;
    }


    public int updateNote(Context context, MCS_CFJL mcsCfjl) {

        SQLiteDatabase db = DBHelper.getWritableDatabase(context);

        int result = 0;
        if (db != null && db.isOpen()) {

            ContentValues cv = new ContentValues();


            try {
                result = db.update("MCS_CFJL", cv, "XH=?", new String[]{mcsCfjl.getXh()});
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }

        return result;
    }


    public int updateNoteMX(Context context, MCS_CFJL_MX cfjl_MX) {

        SQLiteDatabase db = DBHelper.getWritableDatabase(context);

        int result = 0;
        if (db != null && db.isOpen()) {

            ContentValues cv = new ContentValues();
//			cv.put("TITLE", bwl.getTitle());
//			cv.put("CONTENTS", bwl.getContents());
//			cv.put("TYPE", bwl.getType());
//			cv.put("TXSJ", bwl.getTxsj());
//			cv.put("GLBR", bwl.getGlbr());

            try {
                result = db.update("MCS_CFJL_MX", cv, "XH=?", new String[]{cfjl_MX.getCfxh()});
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }

        return result;
    }


    //下载离线病人时插入查房便签
    public List<MediaList> insertCfjl(Context context, List<DrInfo> cfjls) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        List<MediaList> list = new ArrayList<MediaList>();
        String sql = "SELECT * FROM MCS_CFJL where SYXH=? and CJRQ=? and YSDM=?";
        int xh=0;
        try {
            for (DrInfo drInfo : cfjls) {

                ContentValues cv = new ContentValues();
//                cv.put("XH", drInfo.getXh());
                cv.put("SYXH", drInfo.getSyxh());
                cv.put("YEXH", drInfo.getYexh());
                cv.put("MEMO", drInfo.getMemo());
                cv.put("YSDM", drInfo.getYsdm());
                cv.put("CJRQ", drInfo.getCfsj());
                cv.put("CLZT", 0);
                if (db != null && db.isOpen()) {
                    db.insert("MCS_CFJL", null, cv);
                }
                Cursor cursor = db.rawQuery(sql, new String[]{drInfo.getSyxh(), drInfo.getCfsj(),drInfo.getYsdm()});
                while (cursor.moveToNext()) {
                    xh = cursor.getInt(cursor.getColumnIndex("XH"));
                }
                list.addAll(NoteAction.getMediaInfo_offline(context, "{\"cfxh\":" + drInfo.getXh() + "}", xh));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return list;
    }

    //下载离线病人时插入查房便签明细
    public void insertCfjlmx(Context context, List<MediaList> cfjlmxs) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        try {
            for (MediaList media : cfjlmxs) {
                ContentValues cv = new ContentValues();
                cv.put("CFXH", media.getXh());
                cv.put("FLAG", media.getFlag());
                cv.put("NAME", media.getName());
                cv.put("CJRQ", media.getCfsj());
                if (db != null && db.isOpen()) {
                    db.insert("MCS_CFJL_MX", null, cv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    public List<MediaModel> getMedia(SQLiteDatabase db, String id, String syxh, String yexh) {

        List<MediaModel> list = new ArrayList<MediaModel>();

        if (id != null && !id.equals("")) {
            int bwlId = Integer.parseInt(id);

            String sql = "SELECT * FROM DOCTOUCH_MEDIA WHERE JLZT=1 AND BWLID = " + bwlId + "  AND SYXH='" + syxh + "'AND YEXH='" + yexh + "'";

            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                MediaModel media = new MediaModel();
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

        return list;
    }


    public int updateBwl(Context context, String id, String title, String contents, String time) {
        int ID = 0;
        if (!id.equals("")) {
            ID = Integer.parseInt(id);

        } else {
            return 0;
        }
        //String deletesql = "UPDATE DOCTOUCH_BWL SET TITLE = ? ,CONTENTS = ? , GXSJ = ? WHERE XH=?";

        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        int result = 0;

        ContentValues cv = new ContentValues();
        cv.put("TITLE", title);
        cv.put("CONTENTS", contents);
        cv.put("GXSJ", time);

        try {
            result = db.update("DOCTOUCH_BWL", cv, "XH=?", new String[]{ID + ""});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }


        return result;
    }


    public int upLoadUpdate(Context context, String path, String id) {
        //String sql = "UPDATE DOCTOUCH_MEDIA SET SRC = ? WHERE ID = ?";

        int ID = 0;
        if (!id.equals("")) {
            ID = Integer.parseInt(id);

        } else {
            return 0;
        }

        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        int result = 0;

        ContentValues cv = new ContentValues();
        cv.put("SRC", path);

        try {
            result = db.update("DOCTOUCH_MEDIA", cv, "ID=?", new String[]{ID + ""});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return result;
    }


    public int updateDescription(Context context, String text, String id) {
        //String sql = "UPDATE DOCTOUCH_MEDIA SET DESCRIPTION = ? WHERE ID = ?";

        int ID = 0;
        if (!id.equals("")) {
            ID = Integer.parseInt(id);

        } else {
            return 0;
        }


        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        int result = 0;

        ContentValues cv = new ContentValues();
        cv.put("DESCRIPTION", text);

        try {
            result = db.update("DOCTOUCH_MEDIA", cv, "ID=?", new String[]{ID + ""});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return result;
    }

    //离线查房便签保存至本地，在线时自动上传
    public static List<DrInfo> getNote_cfjlAll(Context context) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        List<DrInfo> list = new ArrayList<DrInfo>();
        if (db != null && db.isOpen()) {
            String sql = "SELECT * FROM MCS_CFJL WHERE CLZT=2";
            try {
                Cursor cursor = db.rawQuery(sql, new String[]{});
                while (cursor.moveToNext()) {
                    DrInfo cfjl = new DrInfo();
                    cfjl.setXh(cursor.getString(cursor.getColumnIndex("XH")));
                    cfjl.setSyxh(cursor.getString(cursor.getColumnIndex("SYXH"))+"");
                    cfjl.setYsdm(cursor.getString(cursor.getColumnIndex("YSDM"))+"");
                    cfjl.setMemo(cursor.getString(cursor.getColumnIndex("MEMO")));
                    cfjl.setYexh(cursor.getString(cursor.getColumnIndex("YEXH")));
                    cfjl.setCfsj(cursor.getString(cursor.getColumnIndex("CFSJ")));
                    list.add(cfjl);
                }
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return list;
    }

    public static List<DrInfo> getNote_cfjl(Context context, String bqid, String ysdm, String syxh, String yexh) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);

        //离线的时候，传过来的syxh和yexh类似  314765.0   0.0这种格式，所以过滤下，把.0去掉
        if(syxh.contains(".")){
            syxh=syxh.substring(0,syxh.indexOf("."));
        }
        if(yexh.contains(".")){
            yexh=yexh.substring(0,yexh.indexOf("."));
        }

        List<DrInfo> list = new ArrayList<DrInfo>();
        if (db != null && db.isOpen()) {
            String sql = "SELECT * FROM MCS_CFJL where  SYXH=? AND YEXH=?  AND YSDM=?";
            try {
                Cursor cursor = db.rawQuery(sql, new String[]{syxh, yexh,ysdm});
                while (cursor.moveToNext()) {
                    DrInfo cfjl = new DrInfo();
                    cfjl.setXh(cursor.getString(cursor.getColumnIndex("XH")));
                    cfjl.setSyxh(cursor.getInt(cursor.getColumnIndex("SYXH"))+"");
                    cfjl.setYsdm(cursor.getInt(cursor.getColumnIndex("YSDM"))+"");
                    cfjl.setMemo(cursor.getString(cursor.getColumnIndex("MEMO")));
                    cfjl.setYexh(cursor.getString(cursor.getColumnIndex("YEXH")));
                    cfjl.setCfsj(cursor.getString(cursor.getColumnIndex("CJRQ")));
                    list.add(cfjl);
                }
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return list;
    }

    public List<DrInfo> getNote_cfjl_offline(Context context, String bqid, String ysdm, String syxh, String yexh) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);

        //离线的时候，传过来的syxh和yexh类似  314765.0   0.0这种格式，所以过滤下，把.0去掉
        if(syxh.contains(".")){
            syxh=syxh.substring(0,syxh.indexOf("."));
        }
        if(yexh.contains(".")){
            yexh=yexh.substring(0,yexh.indexOf("."));
        }

        List<DrInfo> list = new ArrayList<DrInfo>();
        if (db != null && db.isOpen()) {
            String sql = "SELECT * FROM MCS_CFJL where  SYXH=? AND YEXH=?  AND YSDM=?";
            try {
                Cursor cursor = db.rawQuery(sql, new String[]{syxh, yexh,ysdm});
                while (cursor.moveToNext()) {
                    DrInfo cfjl = new DrInfo();
                    cfjl.setXh(cursor.getString(cursor.getColumnIndex("XH")));
                    cfjl.setSyxh(cursor.getInt(cursor.getColumnIndex("SYXH"))+"");
                    cfjl.setYexh(cursor.getString(cursor.getColumnIndex("YEXH")));
                    cfjl.setMemo(cursor.getString(cursor.getColumnIndex("MEMO")));
                    cfjl.setYsdm(cursor.getInt(cursor.getColumnIndex("YSDM"))+"");
                    cfjl.setCfsj(cursor.getString(cursor.getColumnIndex("CJRQ")));
                    list.add(cfjl);
                }
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return list;
    }


    public static List<MediaList> getMediaList_cfjl(Context context, String xh, String czryid, String syxh, String today_CFXH) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        List<MediaList> list = new ArrayList<MediaList>();
        if (db != null && db.isOpen()) {
            String sql = "SELECT * FROM MCS_CFJL_MX WHERE  CFXH=? ";
            try {
                Cursor cursor = db.rawQuery(sql, new String[]{xh});
                while (cursor.moveToNext()) {
                    MediaList cfjlmx = new MediaList();
                    cfjlmx.setId(cursor.getInt(cursor.getColumnIndex("XH")));
                    cfjlmx.setXh(cursor.getInt(cursor.getColumnIndex("CFXH")));
                    cfjlmx.setFlag(cursor.getInt(cursor.getColumnIndex("FLAG")));
                    cfjlmx.setName(cursor.getString(cursor.getColumnIndex("NAME")));
//                    cfjlmx.setUrl(cursor.getString(cursor.getColumnIndex("URL")));
                    cfjlmx.setCfsj(cursor.getString(cursor.getColumnIndex("CJRQ")));
                    list.add(cfjlmx);
                }
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();

            }
        }
        return list;
    }

    public static List<MediaList> getMediaList_cfjl_offline(Context context, String xh, String czryid, String syxh, String today_CFXH) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        List<MediaList> list = new ArrayList<MediaList>();
        if (db != null && db.isOpen()) {
            String sql = "SELECT * FROM MCS_CFJL_MX where  XH=? ";
            try {
                Cursor cursor = db.rawQuery(sql, new String[]{xh});
                while (cursor.moveToNext()) {
                    MediaList cfjlmx = new MediaList();
                    cfjlmx.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    cfjlmx.setXh(cursor.getInt(cursor.getColumnIndex("XH")));
                    cfjlmx.setName(cursor.getString(cursor.getColumnIndex("NAME")));
                    cfjlmx.setFlag(cursor.getInt(cursor.getColumnIndex("FLAG")));
                    cfjlmx.setUrl(cursor.getString(cursor.getColumnIndex("URL")));
                    cfjlmx.setCfsj(cursor.getString(cursor.getColumnIndex("CFSJ")));
                    list.add(cfjlmx);
                }
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            } finally {
                db.close();

            }
        }
        return list;
    }


    public static void add_cfjlMedia(Context context, List<MediaList> list) {

        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        try {
            for (MediaList media : list) {
                ContentValues cv = new ContentValues();
                cv.put("XH", media.getXh());
                cv.put("NAME", media.getName());
                cv.put("FLAG", media.getFlag());
                cv.put("URL", media.getUrl());
                cv.put("CFSJ", media.getCfsj());
                if (db != null && db.isOpen()) {
                    db.insert("MIS_MEDIA", null, cv);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    /*离线状态下添加查房记录*/
    public static int add_cfjl(Context context, DrInfo drInfo) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        ContentValues cv = new ContentValues();
        cv.put("SYXH", drInfo.getSyxh());
        cv.put("YEXH", drInfo.getYexh());
        cv.put("MEMO", drInfo.getMemo());
        cv.put("YSDM", drInfo.getYsdm());
        cv.put("CJRQ", drInfo.getCfsj());
        cv.put("CLZT", 2);
        int id = 0;
        String sql = "SELECT * FROM MCS_CFJL where SYXH=? and CJRQ=? and YSDM=?";
        if (db != null && db.isOpen()) {
            try {
                     Cursor cursor1 = db.rawQuery(sql, new String[]{drInfo.getSyxh(), drInfo.getCfsj(),drInfo.getYsdm()});
                if (cursor1.moveToNext()) {
                    int xh = cursor1.getInt(cursor1.getColumnIndex("XH"));
                    db.update("MCS_CFJL", cv, "XH=?", new String[]{"" + xh});
                    id = xh;
                }else{
                db.insert("MCS_CFJL", null, cv);
                Cursor cursor = db.rawQuery(sql, new String[]{drInfo.getSyxh(), drInfo.getCfsj(),drInfo.getYsdm()});
                while (cursor.moveToNext()) {
                    id = cursor.getInt(cursor.getColumnIndex("XH"));
                }

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }

        }
        LogUtils.showLog("xh====add_cfjl" + id);
        return id;
    }
    /*离线状态下添加查房明细记录*/
	public static void add_cfjlMedia(Context context,MediaList media, int cfxh) {
			/*保留副本*/
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);


		int id=0;
		if (db != null && db.isOpen()) {
			try{
				ContentValues cv = new ContentValues();
				cv.put("CFXH", cfxh);
                cv.put("FLAG", media.getFlag());
				cv.put("NAME", media.getName());
//				cv.put("URL", media.getUrl());
				cv.put("CJRQ", media.getCfsj());
//				id=(int)db.insert("MIS_MEDIA", null,cv);
			db.insert("MCS_CFJL_MX", null,cv);
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				db.close();
			}

		}
//		return cfxh;
	}


    public static int add_cfjl_menu(Context context, List<PatDailyRecordInfo> recordInfos) {

        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        int id = 0;
        if (db != null && db.isOpen()) {
            try {
                for (PatDailyRecordInfo media : recordInfos) {
                    ContentValues cv = new ContentValues();
                    cv.put("CFRQ", media.getCfrq());
                    cv.put("MEMO", media.getMemo());
                    cv.put("SYXH", media.getSyxh());
                    cv.put("YEXH", media.getYexh());
                    cv.put("HZXM", media.getHzxm());
                    cv.put("CWDM", media.getCwdm());
                    if (db != null && db.isOpen()) {
                        db.insert("MIS_PATDAILY", null, cv);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }

        }
        return id;
    }

    public static List<PatDailyRecordInfo> get_cfjl_menu(Context context, String bqdm, String ksdm, String syxh, String yexh) {
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);
        List<PatDailyRecordInfo> list = new ArrayList<PatDailyRecordInfo>();
          Cursor cursor=null;
        if (db != null && db.isOpen()) {
//			String sql = "SELECT * FROM MIS_PATDAILY where  BQDM=? AND　KSDM=?";
            String sql = "SELECT * FROM MIS_PATDAILY ";

            try {
//				Cursor cursor=db.rawQuery(sql,new String[]{bqdm,ksdm});
                cursor = db.rawQuery(sql, new String[]{});
                while (cursor.moveToNext()) {
                    PatDailyRecordInfo cfjl = new PatDailyRecordInfo();
                    cfjl.setCfrq(cursor.getString(cursor.getColumnIndex("CFRQ")));
                    cfjl.setSyxh(cursor.getString(cursor.getColumnIndex("SYXH")));
                    cfjl.setMemo(cursor.getString(cursor.getColumnIndex("MEMO")));
                    cfjl.setYexh(cursor.getString(cursor.getColumnIndex("YEXH")));
                    cfjl.setHzxm(cursor.getString(cursor.getColumnIndex("HZXM")));
                    cfjl.setCwdm(cursor.getString(cursor.getColumnIndex("CWDM")));
                    list.add(cfjl);
                }
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
                db.close();
            }
        }
        return list;
    }


            /*根据id删除媒体信息*/
    public static  boolean  delete_Cfjl_Media(Context context,String url){
        //如果midiaId等于0，说明是最新添加的数据，就删除最后一条记录



        boolean flag=false;
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);

        String sql="DELETE FROM MIS_MEDIA WHERE URL=? ";
        if (db != null) {
            try {
              db.execSQL(sql,new String[]{url});
                flag=true;
//            db.execSQL(sql,new String[]{""+delXh});
            }catch (android.database.SQLException e ){
                e.printStackTrace();
            }finally {
                db.close();
            }
        }
        return flag;
    }

    //删除手持设备上缓存文件
    public static void deleteMediaAll(List<MediaList> list){
        for (int i=0;i<list.size();i++){
            delFile(list.get(i).getUrl());
        }

    }

    public static void delFile(String filePath){
        File file = new File(filePath);
        if(file.isFile()){
            file.delete();
        }
        file.exists();
    }

    public static void updateLocalCfjl(Context context,List<DrInfo> drinfo_list){
        SQLiteDatabase db = DBHelper.getWritableDatabase(context);

        if (db != null) {
            ContentValues cv = new ContentValues();
            for(DrInfo drInfo:drinfo_list){
                try {
                    db.update("MCS_CFJL",cv,"XH=?",new String[]{""+drInfo.getXh()});
                }catch (android.database.SQLException e ){
                    e.printStackTrace();
                }finally {
                    db.close();
                }
            }

        }
    }
}
