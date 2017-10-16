package com.winning.mobileclinical.db.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import com.winning.mobileclinical.db.DBHelper;
import com.winning.mobileclinical.model.cis.PatientInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author liu
 *
 */
public class PatientDao {

	/**
	 * @param syxh
	 * @return PatientInfo 根据首页序号查找病人信息
	 */
	public static synchronized PatientInfo getPatientInfo(Context context, String syxh) {
		PatientInfo patientInfo = null;
		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
		if (db != null && db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"SELECT * FROM MOBILE_PATIENTINFO WHERE SYXH=? ",
					new String[] { syxh });

			if (cursor.moveToFirst()) {
				patientInfo = new PatientInfo();
			}
			cursor.close();
			db.close();
		}
		return patientInfo;

	}
	
	
	public static synchronized List<PatientInfo> getPatientList(Context context, String bqdm, String ksdm) {
		List<PatientInfo> list = new ArrayList<PatientInfo>();
		Cursor cursor = null;
		SQLiteDatabase db = null;
		try {
			db = DBHelper.getInstance(context).getReadableDatabase();
			if (db != null && db.isOpen()) {
				cursor = db.rawQuery("SELECT * FROM MOBILE_PATIENTINFO ", null);
				PatientInfo patientInfo = null;
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					patientInfo = new PatientInfo();
					
					BigDecimal EMRXH = new BigDecimal(cursor.getString(cursor
							.getColumnIndex("EMRXH")));
					patientInfo.setEmrxh(EMRXH);
					BigDecimal SYXH = new BigDecimal(cursor.getString(cursor
							.getColumnIndex("SYXH")));
					patientInfo.setSyxh(SYXH);
					BigDecimal YEXH = new BigDecimal(cursor.getString(cursor
							.getColumnIndex("YEXH")));
					patientInfo.setYexh(YEXH);
					patientInfo.setBlh(cursor.getString(cursor.getColumnIndex("BLH")));
//					patientInfo.setName(cursor.getString(cursor.getColumnIndex("NAME")));
					patientInfo.setName("孙寒");
					patientInfo.setSex(cursor.getString(cursor.getColumnIndex("SEX")));
					patientInfo.setAge(cursor.getString(cursor.getColumnIndex("AGE")));
					patientInfo.setBirth(cursor.getString(cursor.getColumnIndex("BIRTH")));
					patientInfo.setPy(cursor.getString(cursor.getColumnIndex("PY")));
					patientInfo.setWb(cursor.getString(cursor.getColumnIndex("WB")));
					patientInfo.setSfzh(cursor.getString(cursor.getColumnIndex("SFZH")));
					patientInfo.setBrzt(Integer.valueOf(cursor.getString(cursor.getColumnIndex("BRZT"))));
					patientInfo.setKsdm(cursor.getString(cursor.getColumnIndex("KSDM")));
					patientInfo.setKsmc(cursor.getString(cursor.getColumnIndex("KSMC")));
					patientInfo.setBqdm(cursor.getString(cursor.getColumnIndex("BQDM")));
					patientInfo.setBqmc(cursor.getString(cursor.getColumnIndex("BQMC")));
					patientInfo.setCwdm(cursor.getString(cursor.getColumnIndex("CWDM")));
					patientInfo.setHldm(cursor.getString(cursor.getColumnIndex("HLDM")));
					patientInfo.setHlmc(cursor.getString(cursor.getColumnIndex("HLMC")));
					patientInfo.setRyrq(cursor.getString(cursor.getColumnIndex("RYRQ")));
					patientInfo.setRqrq(cursor.getString(cursor.getColumnIndex("RQRQ")));
					patientInfo.setCqrq(cursor.getString(cursor.getColumnIndex("CQRQ")));
					patientInfo.setCyrq(cursor.getString(cursor.getColumnIndex("CYRQ")));
					patientInfo.setWzjb(cursor.getString(cursor.getColumnIndex("WZJB")));
					patientInfo.setZddm(cursor.getString(cursor.getColumnIndex("ZDDM")));
					patientInfo.setZdmc(cursor.getString(cursor.getColumnIndex("ZDMC")));
					patientInfo.setCyfs(cursor.getString(cursor.getColumnIndex("CYFS")));
					patientInfo.setJgbz(Integer.valueOf(cursor.getString(cursor.getColumnIndex("JGBZ"))));
					patientInfo.setYbdm(cursor.getString(cursor.getColumnIndex("YBDM")));
					patientInfo.setYbsm(cursor.getString(cursor.getColumnIndex("YBSM")));
					patientInfo.setPzlx(Integer.valueOf(cursor.getString(cursor.getColumnIndex("PZLX"))));
					patientInfo.setBrlx(cursor.getString(cursor.getColumnIndex("BRLX")));
					patientInfo.setPzh(cursor.getString(cursor.getColumnIndex("PZH")));
					patientInfo.setCardno(cursor.getString(cursor.getColumnIndex("CARDNO")));
					patientInfo.setCardtype(cursor.getString(cursor.getColumnIndex("CARDTYPE")));
					patientInfo.setCyzddm(cursor.getString(cursor.getColumnIndex("CYZDDM")));
					patientInfo.setCyzdmc(cursor.getString(cursor.getColumnIndex("CYZDMC")));
					patientInfo.setLcljbz(Integer.valueOf(cursor.getString(cursor.getColumnIndex("LCLJBZ"))));
					patientInfo.setBlgdbz(Integer.valueOf(cursor.getString(cursor.getColumnIndex("BLGDBZ"))));
					patientInfo.setZzysdm(cursor.getString(cursor.getColumnIndex("ZZYSDM")));
					patientInfo.setZzysmc(cursor.getString(cursor.getColumnIndex("ZZYSMC")));
					list.add(patientInfo);
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
	

	//添加病人信息	
//	public static void insertPatientlist(Context context,List<PatientInfo> list) {
//		SQLiteDatabase db = DBHelper.getWritableDatabase(context);
//		try {
//			for(PatientInfo patient:list){
//				ContentValues cv = new ContentValues();
//				cv.put("EMRXH", String.valueOf(patient.getEmrxh()));
//				cv.put("SYXH",String.valueOf(patient.getSyxh()));
//				cv.put("YEXH", String.valueOf(patient.getYexh()));
//				cv.put("BLH", patient.getBlh());
//				cv.put("NAME", patient.getName());
//				cv.put("SEX", patient.getSex());
//				cv.put("AGE", patient.getAge());
//				cv.put("BIRTH", patient.getBirth());
//				cv.put("PY", patient.getPy());
//				cv.put("WB", patient.getWb());
//				cv.put("SFZH", patient.getSfzh());
//				cv.put("BRZT", patient.getBrzt());
//				cv.put("KSDM", patient.getKsdm());
//				cv.put("KSMC", patient.getKsmc());
//				cv.put("BQDM", patient.getBqdm());
//				cv.put("BQMC", patient.getBqmc());
//				cv.put("CWDM", patient.getCwdm());
//				cv.put("HLDM", patient.getHldm());
//				cv.put("HLMC", patient.getHlmc());
//				cv.put("RYRQ", patient.getRyrq());
//				cv.put("RQRQ", patient.getRqrq());
//				cv.put("CQRQ", patient.getCqrq());
//				cv.put("CYRQ", patient.getCyrq());
//				cv.put("WZJB", patient.getWzjb());
//				cv.put("ZDDM", patient.getZddm());
//				cv.put("ZDMC", patient.getZdmc());
//				cv.put("CYFS", patient.getCyfs());
//				cv.put("JGBZ", patient.getJgbz());
//				cv.put("YBDM", patient.getYbdm());
//				cv.put("YBSM", patient.getYbsm());
//				cv.put("PZLX", patient.getPzlx());
//				cv.put("BRLX", patient.getBrlx());
//				cv.put("PZH", patient.getPzh());
//				cv.put("CARDNO", patient.getCardno());
//				cv.put("CARDTYPE", patient.getCardtype());
//				cv.put("CYZDDM", patient.getCyzddm());
//				cv.put("CYZDMC", patient.getCyzdmc());
//				cv.put("LCLJBZ", patient.getLcljbz());
//				cv.put("BLGDBZ", patient.getBlgdbz());
//				cv.put("ZZYSDM", patient.getZzysdm());
//				cv.put("ZZYSMC", patient.getZzysmc());
//				if (db != null && db.isOpen()) {
//					db.insert("MOBILE_PATIENTINFO", null, cv);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			db.close();
//		}
//	}

	

}


