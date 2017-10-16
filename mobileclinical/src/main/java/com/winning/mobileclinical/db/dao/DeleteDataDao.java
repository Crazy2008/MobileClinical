package com.winning.mobileclinical.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.winning.mobileclinical.db.DBHelper;

/**
 * @author liu
 *
 */
public class DeleteDataDao {

	/**
	 * 
	 * @return boolean 删除整个病区病人
	 */
	public static synchronized Boolean deleteAll(Context context,String ysdm,String bqdm,String ksdm) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			deleteConfig(db);
			deleteDoctor(db,ysdm);
			deleteBqKs(db,ysdm);
			deleteBedinfo(db,bqdm,ksdm);
			deletePatient(db,bqdm,ksdm);
			deleteOrder(db,bqdm,ksdm);
			deleteEmr(db,bqdm,ksdm);
			deleteEmrMX(db,bqdm,ksdm);
			deleteLis(db,bqdm,ksdm);
			deleteLisMX(db,bqdm,ksdm);
			deleteRis(db,bqdm,ksdm);
			deleteRisMX(db,bqdm,ksdm);
			deleteRisImage(db,bqdm,ksdm);
			deleteTzjl(db,bqdm,ksdm);
			deleteLjxx(db,bqdm,ksdm);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return true;
	}
	
	
	/**
	 * 
	 * @return boolean 删除单个病人
	 */
	public static synchronized Boolean deleteSingle(Context context,String ysdm,String syxh,String yexh) {
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		try {
			deleteConfig(db);
			deleteDoctor(db,ysdm);
			deleteBqKs(db,ysdm);
			
//			deleteBedinfoSingle(db,cwdm,bqdm,ksdm);
			
			deletePatientSingle(db,syxh,yexh);
			deleteOrderSingle(db,syxh,yexh);
			deleteEmrSingle(db,syxh,yexh);
			deleteEmrSingleMX(db,syxh,yexh);
			deleteLisSingle(db,syxh,yexh);
			deleteLisMXSingle(db,syxh,yexh);
			deleteRisSingle(db,syxh,yexh);
			deleteRisMXSingle(db,syxh,yexh);
			deleteRisImageSingle(db,syxh,yexh);
			deleteTzjlSingle(db,syxh,yexh);
			deleteLjxxSingle(db,syxh,yexh);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		return true;
	}
	
	/**
	 * 
	 * @return boolean 删除配置表
	 */
	public static synchronized boolean deleteConfig(SQLiteDatabase db) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM MOBILE_CONFIG",
					new String[] {});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除医生信息
	 */
	public static synchronized boolean deleteDoctor(SQLiteDatabase db, String ysdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM MOBILE_DOCTORINFO WHERE RTRIM(ID)=? ",
					new String[] {ysdm});
			result = true;
		}
		return result;
	}
	
	
	/**
	 * 
	 * @return boolean 删除病区科室 对应信息
	 */
	public static synchronized boolean deleteBqKs(SQLiteDatabase db, String ysdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM MOBILE_DEPTWARDMAPINFO WHERE RTRIM(YSDM)=? ",
					new String[] {ysdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除床位 对应信息
	 */
	public static synchronized boolean deleteBedinfo(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM MOBILE_BEDINFO WHERE  KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除病人信息
	 */
	public static synchronized boolean deletePatient(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData0 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	/**
	 * 
	 * @return boolean 删除病区病人医嘱
	 */
	public static synchronized boolean deleteOrder(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData1 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除病区病人EMR
	 */
	public static synchronized boolean deleteEmr(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData8 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	public static synchronized boolean deleteEmrMX(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData9 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除病区病人检查
	 */
	public static synchronized boolean deleteRis(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData6 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除病区病人检查
	 */
	public static synchronized boolean deleteRisMX(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData7 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除病区病人检查
	 */
	public static synchronized boolean deleteRisImage(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM MIS_IMAGE WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除病区病人检验
	 */
	public static synchronized boolean deleteLis(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData4 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除病区病人检验
	 */
	public static synchronized boolean deleteLisMX(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData5 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除病区病人体温单
	 */
	public static synchronized boolean deleteTzjl(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData11 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除病区病人路径
	 */
	public static synchronized boolean deleteLjxx(SQLiteDatabase db, String bqdm, String ksdm) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData3 WHERE KSDM=? and BQDM=? ",
					new String[] {ksdm,bqdm});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除单个信息
	 */
	public static synchronized boolean deletePatientSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData0 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	/**
	 * 
	 * @return boolean 删除单个病人医嘱
	 */
	public static synchronized boolean deleteOrderSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData1 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除单个病人EMR
	 */
	public static synchronized boolean deleteEmrSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData8 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
	public static synchronized boolean deleteEmrSingleMX(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData9 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除单个病人检查
	 */
	public static synchronized boolean deleteRisSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData6 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除单个病人检验
	 */
	public static synchronized boolean deleteLisSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData4 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
	public static synchronized boolean deleteLisMXSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData5 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除单个病人体温单
	 */
	public static synchronized boolean deleteTzjlSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData11 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
	/**
	 * 
	 * @return boolean 删除个人病人路径
	 */
	public static synchronized boolean deleteLjxxSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData3 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
	
	
	public static synchronized boolean deleteRisMXSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM PatientData7 WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
	
	public static synchronized boolean deleteRisImageSingle(SQLiteDatabase db, String syxh, String yexh) {
		boolean result = false;
		if (db != null && db.isOpen()) {
			db.execSQL("DELETE FROM MCS_IMAGE WHERE SYXH=? and YEXH=? ",
					new String[] {syxh,yexh});
			result = true;
		}
		return result;
	}
	
}


