package com.winning.mobileclinical.action;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.winning.mobileclinical.db.dao.BfDao;
import com.winning.mobileclinical.db.dao.CommonJsonDao;
import com.winning.mobileclinical.db.dao.DeleteDataDao;
import com.winning.mobileclinical.db.dao.DepartmentWardDao;
import com.winning.mobileclinical.db.dao.DoctorDao;
import com.winning.mobileclinical.db.dao.ImageDao;
import com.winning.mobileclinical.db.dao.NoteDao;
import com.winning.mobileclinical.db.dao.SysConfigDao;
import com.winning.mobileclinical.fragment.CFJLFragment;
import com.winning.mobileclinical.fragment.FragmentChild;
import com.winning.mobileclinical.fragment.PatientList;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.DrInfo;
import com.winning.mobileclinical.model.MediaList;
import com.winning.mobileclinical.model.cis.Bedinfo;
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.FileInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.model.cis.TableClass;
import com.winning.mobileclinical.utils.LogUtils;
import com.winning.mobileclinical.utils.ServerUtil;
import com.winning.mobileclinical.web.SystemUtil;
import com.winning.mobileclinical.widget.MyProgress;
import com.winning.mobileclinical.widget.MyProgressDialog;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfflineAction {
	
	private String imgUrl="";
	private String imgName="";
	private Bitmap bitmap;
	
	static OfflineAction action = null;
	private Context context = null;
	public static NoteDao noteDao=NoteDao.getInstance();
	List<MediaList> medialist = null;
	private Thread noteServerThread;

	private static Boolean status = false;
	
	public OfflineAction(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public static String startload(Context context,String ysdm,List<PatientInfo> pInfos,String syxh, String yexh,Boolean isAll)
	{
		status = isAll;
		if(action == null)
		{
			action = new OfflineAction(context);
		}
		
		return action.download(ysdm,pInfos,syxh,yexh,isAll);
		
//		return action.downSingle(syxh,yexh);
	}

	public String download(String ysdm,List<PatientInfo> pInfos,String syxh, String yexh, Boolean isAll)
	{
		long startTime=System.currentTimeMillis(); //获取开始时间
		DoctorInfo doctor = new DoctorInfo();
		DeptWardMapInfo dInfo = new DeptWardMapInfo();
		if(GlobalCache.getCache().getDoctor() != null)
		{
			doctor = GlobalCache.getCache().getDoctor();
			int sel = GlobalCache.getCache().getBqSel();
			dInfo = GlobalCache.getCache().getDeptWardMapInfos().get(sel);
		}
		
		if(isAll) {
			DeleteDataDao.deleteAll(context, ysdm, dInfo.getBqdm(), dInfo.getKsdm());
		} else {
			DeleteDataDao.deleteSingle(context, ysdm, syxh, yexh);
		}
		
		String message = "下载成功";
		
		//基本信息统一必须下载   职工库  病区科室对应库 配置表
		//职工信息
		List<TableClass> tableClass = new ArrayList<TableClass>();
		tableClass = GlobalCache.getCache().getTableClasses();
		
		System.out.println("配置信息插入====");
		if(tableClass.size() > 0) {
			SysConfigDao.insert(context, tableClass);
		}
		System.out.println("配置信息插入成功====");
		
		System.out.println("职工信息插入====");
		DoctorDao.insert(context, doctor);
		System.out.println("职工信息插入成功====");
//		message += "职工信息下载成功\r\n";
		
		//职工对应病区
		Map<String,Object> map = new HashMap<String,Object>(); 
	    map.put("ysdm", ysdm); 
	    JSONObject temp = new JSONObject(map);
		List<DeptWardMapInfo> depList = DeptWardAction.getDeptWardMapInfos(context, temp.toString());

		if(depList.size()>0)
		{
//			message += "职工对应病区科室下载成功\r\n";
		}
		else 
		{
			message += "职工对应病区科室下载失败\r\n";
		}
		System.out.println("科室病区对应信息插入====");
		DepartmentWardDao.insert(context, depList,doctor.getId());
		System.out.println("科室病区对应信息插入成功====");

		//患者床位对应
		Map<String,Object> mapbq = new HashMap<String,Object>(); 
		mapbq.put("bqdm", dInfo.getBqdm()); 
	    JSONObject tempbq = new JSONObject(mapbq);
		List<Bedinfo> bedlist = BFSearchAction.getBF(context, tempbq.toString());
		if(bedlist != null)
		{
//			message += "患者对应病房下载成功\r\n";
		}
		else 
		{
			message += "患者对应病房下载失败\r\n";
		}
		
		if(bedlist != null)
		{
			System.out.println("患者对应病房信息插入====");
			BfDao.insertJSONDATA(context, bedlist);
			System.out.println("患者对应病房信息插入成功====");
		}
		if(isAll) {   //下载整个病区
			Map<String,Object> maphz = new HashMap<String,Object>();
			maphz.put("ksdm", dInfo.getKsdm()); 
			maphz.put("bqdm", dInfo.getBqdm());
		    JSONObject maptemp = new JSONObject(maphz);
			System.out.println("ksdm+bqdm=" + maptemp.toString());
			List<CommonJson> patientInfos = PatientAction.getWardInPatients(context, maptemp.toString());
			System.out.println("病人详情——"+patientInfos.get(0).getJson());
			int percent = 100/patientInfos.size();
			if(patientInfos.size() > 0) {
//				message += "病人下载成功\r\n";
			}else{
				message += "病人下载失败\r\n";
			}
			TableClass  pat_table = SysConfigDao.getValue(context,"patient","all-inpaitient");
			System.out.println("整个病区病区信息插入===="+pat_table.getClassName()+pat_table.getType());
			CommonJsonDao.insertJSONDATA(context, patientInfos, pat_table);
			System.out.println("整个病区病区信息插入成功====");
//			
			if(patientInfos.size() > 0) {
				for(int i=0; i<patientInfos.size();i++) {
					String syxh_single = patientInfos.get(i).getSyxh();
					String yexh_single = patientInfos.get(i).getYexh();
					if (i==(patientInfos.size()-1)&&(100-GlobalCache.getCache().getProgress()>percent)){
						GlobalCache.getCache().setProgress(GlobalCache.getCache().getProgress()+percent);
						PatientList.updateWardProgress.sendEmptyMessage(GlobalCache.getCache().getProgress());
					}
					downSingle(syxh_single,yexh_single,ysdm);
					if (i==(patientInfos.size()-1)){
						PatientList.updateWardProgress.sendEmptyMessage(100);
					}else{
						GlobalCache.getCache().setProgress(GlobalCache.getCache().getProgress()+percent);
						PatientList.updateWardProgress.sendEmptyMessage(GlobalCache.getCache().getProgress());
					}
				}
			}
			
//			System.out.println("病人医嘱下载开始====");
//			List<CommonJson> orderInfos = OrdersMessageAction.getWardPatientOrders(context, maptemp.toString());
//			System.out.println("病人医嘱下载成功====");
//			if(orderInfos.size() > 0) {
//				message += "病人医嘱下载成功\r\n";
//			}else{
//				message += "病人医嘱下载失败\r\n";
//			}
//			TableClass  order_table = SysConfigDao.getValue(context,"order","ward-order");
//			System.out.println("整个病区医嘱信息插入===="+order_table.getClassName()+order_table.getType());
//			CommonJsonDao.insertJSONDATA(context, orderInfos, order_table);
//			System.out.println("整个病区病区信息插入成功====");
//			
//			//病历暂缓
//			
//			
//			System.out.println("病人检验报告开始下载====");
//		    List<CommonJson> lisJsonList = CommonJsonAction.getCommoninfoList(context, "lis", "ward-rpt", maptemp.toString());
//		    System.out.println("病人检验报告下载结束====");
//		    if(lisJsonList.size() > 0) {
//				message += "病人检验报告下载成功\r\n";
//			}else{
//				message += "病人检验报告下载失败\r\n";
//			}
//		    TableClass  lis_table = SysConfigDao.getValue(context,"lis","ward-rpt");
//			System.out.println("插入表===="+lis_table.getClassName()+lis_table.getType());
//			
//			System.out.println("病人检验报告开始插入====");
//			CommonJsonDao.insertJSONDATA(context, lisJsonList, lis_table);
//		    System.out.println("病人检验报告插入成功====");
//		    
//		    System.out.println("病人检验报告明细开始下载====");
//		    List<CommonJson> lisJsonListmx = CommonJsonAction.getCommoninfoList(context, "lis", "ward-detail", maptemp.toString());
//		    System.out.println("病人检验报告明细下载结束====");
//		    if(lisJsonList.size() > 0) {
//				message += "病人检验报告明细下载成功\r\n";
//			}else{
//				message += "病人检验报告明细下载失败\r\n";
//			}
//		    TableClass  lismx_table = SysConfigDao.getValue(context,"lis","ward-detail");
//			System.out.println("插入表明细===="+lismx_table.getClassName()+lismx_table.getType());
//			
//			System.out.println("病人检验报告明细开始插入====");
//			CommonJsonDao.insertJSONDATA(context, lisJsonListmx, lismx_table);
//		    System.out.println("病人检验报告明细插入成功====");
//		    
//		    System.out.println("病人检查报告开始下载====");
//		    List<CommonJson> risJsonList = CommonJsonAction.getCommoninfoList(context, "ris", "ward-rpt", maptemp.toString());
//		    System.out.println("病人检查报告下载结束====");
//		    if(risJsonList.size() > 0) {
//				message += "病人检查报告下载成功\r\n";
//			}else{
//				message += "病人检查报告下载失败\r\n";
//			}
//		    TableClass  ris_table = SysConfigDao.getValue(context,"ris","ward-rpt");
//			System.out.println("插入表===="+ris_table.getClassName()+ris_table.getType());
//			
//			System.out.println("病人检查报告开始插入====");
//			CommonJsonDao.insertJSONDATA(context, risJsonList, ris_table);
//		    System.out.println("病人检查报告插入成功====");
//		    
//		    System.out.println("病人检查报告明细开始下载====");
//		    List<CommonJson> risJsonListmx = CommonJsonAction.getCommoninfoList(context, "ris", "ward-detail", maptemp.toString());
//		    System.out.println("病人检查报告明细下载结束====");
//		    if(risJsonList.size() > 0) {
//				message += "病人检查报告明细下载成功\r\n";
//			}else{
//				message += "病人检查报告明细下载失败\r\n";
//			}
//		    TableClass  rismx_table = SysConfigDao.getValue(context,"ris","ward-detail");
//			System.out.println("插入表明细===="+rismx_table.getClassName()+rismx_table.getType());
//			
//			System.out.println("病人检查报告明细开始插入====");
//			CommonJsonDao.insertJSONDATA(context, risJsonListmx, rismx_table);
//		    System.out.println("病人检查报告明细插入成功====");
//		    
//		    long endTime=System.currentTimeMillis(); //获取结束时间
//			System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
//			message += "程序运行时间： "+(endTime-startTime)+"ms";
			
		} else {
			PatientList.updateProgress.sendEmptyMessage(10);

			Map<String,Object> maphz = new HashMap<String,Object>(); 
			maphz.put("syxh", syxh); 
			maphz.put("yexh", yexh);
		    JSONObject maptemp = new JSONObject(maphz);
		    
		    System.out.println(maptemp.toString());
		    System.out.println("病人信息下载====");
		    CommonJson patJson = CommonJsonAction.getCommoninfo(context, "patient", "info", maptemp.toString());
		    
		    if(patJson != null) {
//				message += "病人下载成功\r\n";
			}else{
				message += "病人下载失败\r\n";
			}
		    
		    System.out.println("病人信息下载成功====");
		    TableClass  pat_table = SysConfigDao.getValue(context,"patient","info");
			System.out.println("插入表===="+pat_table.getClassName()+pat_table.getType());
			
			if(patJson != null) {
				System.out.println("病人信息开始插入====");
				CommonJsonDao.insertJSON(context, patJson, pat_table);
				System.out.println("病人信息插入成功====");
			}

			PatientList.updateProgress.sendEmptyMessage(20);
			
			downSingle(syxh,yexh,ysdm);

			PatientList.updateProgress.sendEmptyMessage(100);
			
//			
//			System.out.println("病人医嘱信息开始下载====");
//		    List<CommonJson> orderJsonList = CommonJsonAction.getCommoninfoList(context, "order", "patient-order", maptemp.toString());
//		    System.out.println("病人医嘱信息下载成功====");
//		    
//		    if(orderJsonList.size() > 0) {
//				message += "病人医嘱下载成功\r\n";
//			}else{
//				message += "病人医嘱下载失败\r\n";
//			}
//		    
//		    TableClass  order_table = SysConfigDao.getValue(context,"order","patient-order");
//			System.out.println("插入表===="+order_table.getClassName()+order_table.getType());
//		    
//			System.out.println("病人医嘱信息开始插入====");
//			CommonJsonDao.insertJSONDATA(context, orderJsonList, order_table);
//			System.out.println("病人医嘱信息插入成功====");
//			
//			System.out.println("病人病历信息开始下载====");
//		    CommonJson emrJson = CommonJsonAction.getCommoninfo(context, "emr", "patient-emr", maptemp.toString());
//		    System.out.println("病人病历信息下载结束====");
//		    
//		    if(emrJson != null) {
//				message += "病人病历下载成功\r\n";
//			}else{
//				message += "病人病历下载失败\r\n";
//			}
//		    
//		    TableClass  emr_table = SysConfigDao.getValue(context,"emr","patient-emr");
//			System.out.println("插入表===="+emr_table.getClassName()+emr_table.getType());
//			
//			System.out.println("病人病历信息开始插入====");
//			CommonJsonDao.insertJSON(context, emrJson, emr_table);
//		    System.out.println("病人病历信息插入成功====");
//		    
//		    
//		    
//		    System.out.println("病人检验报告开始下载====");
//		    List<CommonJson> lisJsonList = CommonJsonAction.getCommoninfoList(context, "lis", "rpt-list", maptemp.toString());
//		    System.out.println("病人检验报告下载结束====");
//		    if(lisJsonList.size() > 0) {
//				message += "病人检验报告下载成功\r\n";
//			}else{
//				message += "病人检验报告下载失败\r\n";
//			}
//		    TableClass  lis_table = SysConfigDao.getValue(context,"lis","rpt-list");
//			System.out.println("插入表===="+lis_table.getClassName()+lis_table.getType());
//			
//			System.out.println("病人检验报告开始插入====");
//			CommonJsonDao.insertJSONDATA(context, lisJsonList, lis_table);
//		    System.out.println("病人检验报告插入成功====");
//		    
//		    System.out.println("病人检验报告明细开始下载====");
//		    List<CommonJson> lisJsonListmx = CommonJsonAction.getCommoninfoList(context, "lis", "rpt-detail", maptemp.toString());
//		    System.out.println("病人检验报告明细下载结束====");
//		    if(lisJsonList.size() > 0) {
//				message += "病人检验报告明细下载成功\r\n";
//			}else{
//				message += "病人检验报告明细下载失败\r\n";
//			}
//		    TableClass  lismx_table = SysConfigDao.getValue(context,"lis","rpt-detail");
//			System.out.println("插入表明细===="+lismx_table.getClassName()+lismx_table.getType());
//			
//			System.out.println("病人检验报告明细开始插入====");
//			CommonJsonDao.insertJSONDATA(context, lisJsonListmx, lismx_table);
//		    System.out.println("病人检验报告明细插入成功====");
//		    
//		    
//		    
//		    System.out.println("病人检查报告开始下载====");
//		    List<CommonJson> risJsonList = CommonJsonAction.getCommoninfoList(context, "ris", "rpt-list", maptemp.toString());
//		    System.out.println("病人检查报告下载结束====");
//		    if(risJsonList.size() > 0) {
//				message += "病人检查报告下载成功\r\n";
//			}else{
//				message += "病人检查报告下载失败\r\n";
//			}
//		    TableClass  ris_table = SysConfigDao.getValue(context,"ris","rpt-list");
//			System.out.println("插入表===="+ris_table.getClassName()+ris_table.getType());
//			
//			System.out.println("病人检查报告开始插入====");
//			CommonJsonDao.insertJSONDATA(context, risJsonList, ris_table);
//		    System.out.println("病人检查报告插入成功====");
//		    
//		    System.out.println("病人检查报告明细开始下载====");
//		    List<CommonJson> risJsonListmx = CommonJsonAction.getCommoninfoList(context, "ris", "rpt-detail", maptemp.toString());
//		    System.out.println("病人检查报告明细下载结束====");
//		    if(risJsonList.size() > 0) {
//				message += "病人检查报告明细下载成功\r\n";
//			}else{
//				message += "病人检查报告明细下载失败\r\n";
//			}
//		    TableClass  rismx_table = SysConfigDao.getValue(context,"ris","rpt-detail");
//			System.out.println("插入表明细===="+rismx_table.getClassName()+rismx_table.getType());
//			
//			System.out.println("病人检查报告明细开始插入====");
//			CommonJsonDao.insertJSONDATA(context, risJsonListmx, rismx_table);
//		    System.out.println("病人检查报告明细插入成功====");
		    
//		    CommonJson lisJson = CommonJsonAction.getCommoninfo(context, "patient", " info", maptemp.toString());
//		    
//		    CommonJson risJson = CommonJsonAction.getCommoninfo(context, "patient", " info", maptemp.toString());
		    
//		    long endTime=System.currentTimeMillis(); //获取结束时间
//			System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
			
//			message += "程序运行时间： "+(endTime-startTime)+"ms";
		}
		return message;
	}
	
	
	public String  downSingle(String syxh, String yexh, String ysdm) {
		
		long startTime=System.currentTimeMillis(); //获取开始时间
		
		String message = "";
		Map<String,Object> maphz = new HashMap<String,Object>(); 
		maphz.put("syxh", syxh); 
		maphz.put("yexh", yexh);
	    JSONObject maptemp = new JSONObject(maphz);
	    System.out.println(maptemp.toString());
//	    System.out.println("病人信息下载====");
//	    CommonJson patJson = CommonJsonAction.getCommoninfo(context, "patient", "info", maptemp.toString());
//	    
//	    if(patJson != null) {
//			message += "病人下载成功\r\n";
//		}else{
//			message += "病人下载失败\r\n";
//		}
//	    
//	    System.out.println("病人信息下载成功====");
//	    TableClass  pat_table = SysConfigDao.getValue(context,"patient","info");
//		System.out.println("插入表===="+pat_table.getClassName()+pat_table.getType());
//		
//		System.out.println("病人信息开始插入====");
//		CommonJsonDao.insertJSON(context, patJson, pat_table);
//		System.out.println("病人信息插入成功====");
		
		System.out.println("病人医嘱信息开始下载====");
	    List<CommonJson> orderJsonList = CommonJsonAction.getCommoninfoList(context, "order", "patient-order", maptemp.toString());
	    System.out.println("病人医嘱信息下载成功====");
	    
//	    if(orderJsonList.size() > 0) {
//			message += "病人医嘱下载成功\r\n";
//		}else{
//			message += "病人医嘱下载失败\r\n";
//		}


	    
	    TableClass  order_table = SysConfigDao.getValue(context,"order","patient-order");
		System.out.println("插入表===="+order_table.getClassName()+order_table.getType());
	    
		System.out.println("病人医嘱信息开始插入====");
		CommonJsonDao.insertJSONDATA(context, orderJsonList, order_table);
		System.out.println("病人医嘱信息插入成功====");

		if (!status){
			PatientList.updateProgress.sendEmptyMessage(30);
		}
		
		System.out.println("病人路径执行状况下载====");
	    CommonJson ljzxJson = CommonJsonAction.getCommoninfo(context, "clinicalpath", "cp-lcinfo", maptemp.toString());
	    
	    
	    System.out.println("病人路径执行状况下载成功====");
	    TableClass  ljzx_table = SysConfigDao.getValue(context,"clinicalpath","cp-lcinfo");
		System.out.println("插入表===="+ljzx_table.getClassName()+ljzx_table.getType());
		
		if(ljzxJson != null) {
			System.out.println("病人路径执行状况开始插入====");
			CommonJsonDao.insertJSON(context, ljzxJson, ljzx_table);
			System.out.println("病人路径执行状况插入成功====");
		}

		if (!status){
			PatientList.updateProgress.sendEmptyMessage(40);
		}
		
		System.out.println("病人路径诊疗状况下载====");
	    List<CommonJson> ljzlJson = CommonJsonAction.getCommoninfoList(context, "clinicalpath", "cp-lcjhzxinfo", maptemp.toString());
	    if(ljzlJson != null) {
			message += "病人路径诊疗状况下载成功\r\n";
		}else{
			message += "病人路径诊疗状况下载失败\r\n";
		}
	    
	    System.out.println("病人路径诊疗状况下载成功====");
	    TableClass  ljzl_table = SysConfigDao.getValue(context,"clinicalpath","cp-lcjhzxinfo");
		System.out.println("插入表===="+ljzl_table.getClassName()+ljzl_table.getType());
		
		System.out.println("病人路径诊疗状况开始插入====");
		if(ljzlJson != null) {
//			CommonJsonDao.insertJSON(context, ljzlJson, ljzl_table);
			CommonJsonDao.insertJSONDATA(context, ljzlJson, ljzl_table);
		}
		System.out.println("病人路径诊疗状况插入成功====");

		if (!status){
			PatientList.updateProgress.sendEmptyMessage(50);
		}
		
		
		System.out.println("病人病历目录信息开始下载====");
	    CommonJson emrJson = CommonJsonAction.getCommoninfo(context, "emr", "fullmodel", maptemp.toString());
	    System.out.println("病人病历目录信息下载结束====");
	    
	    if(emrJson != null) {
			message += "病人病历目录下载成功\r\n";
		}else{
			message += "病人病历目录下载失败\r\n";
		}
	    
	    TableClass  emr_table = SysConfigDao.getValue(context,"emr","fullmodel");
		System.out.println("插入表===="+emr_table.getClassName()+emr_table.getType());
		
		System.out.println("病人病历目录信息开始插入====");
		if(emrJson != null) { 
			CommonJsonDao.insertJSON(context, emrJson, emr_table);
		}
	    System.out.println("病人病历目录信息插入成功====");
	    
	    
	    System.out.println("病人病历明细信息开始下载====");
//	    CommonJson emrmxJson = CommonJsonAction.getCommoninfo(context, "emr", "patient-model", maptemp.toString());
	    List<CommonJson> emrmxJson = CommonJsonAction.getCommoninfoList(context, "emr", "patient-model", maptemp.toString());
	    
	    System.out.println("病人病历目录信息下载结束====");
	    
	    if(emrmxJson != null) {
			message += "病人病历目录明细下载成功\r\n";
		}else{
			message += "病人病历目录明细下载失败\r\n";
		}
	    
	    TableClass  emrmx_table = SysConfigDao.getValue(context,"emr","patient-model");
		System.out.println("插入表===="+emrmx_table.getClassName()+emrmx_table.getType());
		
		
		if(emrmxJson != null) {
			System.out.println("病人病历目录明细信息开始插入====");
//			CommonJsonDao.insertJSON(context, emrmxJson, emrmx_table);
			
			CommonJsonDao.insertJSONDATA(context, emrmxJson, emrmx_table);
			System.out.println("病人病历目录明细信息插入成功====");
		}

		if (!status){
			PatientList.updateProgress.sendEmptyMessage(60);
		}
	    
	    System.out.println("病人检验报告开始下载====");
	    List<CommonJson> lisJsonList = CommonJsonAction.getCommoninfoList(context, "lis", "rpt-list", maptemp.toString());
	    System.out.println("病人检验报告下载结束====");
	    if(lisJsonList != null) {
			message += "病人检验报告下载成功\r\n";
		}else{
			message += "病人检验报告下载失败\r\n";
		}
	    TableClass  lis_table = SysConfigDao.getValue(context,"lis","rpt-list");
		System.out.println("插入表===="+lis_table.getClassName()+lis_table.getType());
		
		if(lisJsonList != null) {
			System.out.println("病人检验报告开始插入====");
			CommonJsonDao.insertJSONDATA(context, lisJsonList, lis_table);
		    System.out.println("病人检验报告插入成功====");
		}
		
	    
	    System.out.println("病人检验报告明细开始下载====");
	    List<CommonJson> lisJsonListmx = CommonJsonAction.getCommoninfoList(context, "lis", "patient-detail", maptemp.toString());
	    System.out.println("病人检验报告明细下载结束====");
	    if(lisJsonListmx != null) {
			message += "病人检验报告明细下载成功\r\n";
		}else{
			message += "病人检验报告明细下载失败\r\n";
		}
	    TableClass  lismx_table = SysConfigDao.getValue(context,"lis","patient-detail");
		System.out.println("插入表明细===="+lismx_table.getClassName()+lismx_table.getType());
		
		if(lisJsonListmx != null) {
			System.out.println("病人检验报告明细开始插入====");
			CommonJsonDao.insertJSONDATA(context, lisJsonListmx, lismx_table);
		    System.out.println("病人检验报告明细插入成功====");
		}

	    System.out.println("病人检查报告开始下载====");
	    List<CommonJson> risJsonList = CommonJsonAction.getCommoninfoList(context, "ris", "rpt-list", maptemp.toString());
	    System.out.println("病人检查报告下载结束====");
	    if(risJsonList != null) {
			message += "病人检查报告下载成功\r\n";
		}else{
			message += "病人检查报告下载失败\r\n";
		}
	    TableClass  ris_table = SysConfigDao.getValue(context,"ris","rpt-list");
		System.out.println("插入表===="+ris_table.getClassName()+ris_table.getType());
		
		if(risJsonList != null) {
			System.out.println("病人检查报告开始插入====");
			CommonJsonDao.insertJSONDATA(context, risJsonList, ris_table);
		    System.out.println("病人检查报告插入成功====");
		}

		if (!status){
			PatientList.updateProgress.sendEmptyMessage(70);
		}
	    
	    System.out.println("病人检查报告明细开始下载====");
	    List<CommonJson> risJsonListmx = CommonJsonAction.getCommoninfoList(context, "ris", "patient-detail", maptemp.toString());
	    System.out.println("病人检查报告明细下载结束====");
	    if(risJsonList != null) {
			message += "病人检查报告明细下载成功\r\n";
		}else{
			message += "病人检查报告明细下载失败\r\n";
		}
	    TableClass  rismx_table = SysConfigDao.getValue(context,"ris","patient-detail");
		System.out.println("插入表明细===="+rismx_table.getClassName()+rismx_table.getType());
		if(risJsonList != null) {
			System.out.println("病人检查报告明细开始插入====");
			CommonJsonDao.insertJSONDATA(context, risJsonListmx, rismx_table);
		    System.out.println("病人检查报告明细插入成功====");
		}
		
//	    
	    System.out.println("病人检查报告图像开始下载====");
	    List<FileInfo> risJsonImage = ImageAction.getReportImageList(context, maptemp.toString());
	    System.out.println("病人检查报告图像下载结束====" +  risJsonImage);
//	    if(risJsonImage != null) {
//			message += "病人检查报告图像下载成功\r\n";
//		}else{
//			message += "病人检查报告图像下载失败\r\n";
//		}
	    if(risJsonImage != null) {
	    	if(risJsonImage.size() > 0 ) {
		    	String imagepath = getMkDir();
	    		for(int i=0; i<risJsonImage.size();i++) {
		    		if(risJsonImage.get(i).getSrc().length() > 0) {
		    			
		    			String src = risJsonImage.get(i).getSrc();
		    			String temp[]  = src.split("/");
		    			imgUrl = risJsonImage.get(i).getSrc();
		    			imgName =temp[temp.length-1];
			    		GetImage getimage = new GetImage();
			    		new Thread(getimage).start();
		    		}
			    }
		    	
		    	for(int i=0; i<risJsonImage.size();i++) {
		    		if(risJsonImage.get(i).getSrc().length() > 0) {
		    			String src = risJsonImage.get(i).getSrc();
		    			String temp[]  = src.split("/");
		    			risJsonImage.get(i).setSrc(imagepath + "/"+temp[temp.length-1]);
		    		}
			    }
		    }
	    	System.out.println("病人检查报告图像开始插入====");
			ImageDao.insertFileInfos(context, risJsonImage);
		    System.out.println("病人检查报告图像插入成功====");


			if (!status){
				PatientList.updateProgress.sendEmptyMessage(80);
			}
		    
		    
		    
		    System.out.println("病人体征开始下载====");
		    List<CommonJson> lisJsonListtz = CommonJsonAction.getCommoninfoList(context, "patient", "vital-signs", maptemp.toString());
		    System.out.println("病人体征下载结束====");
		    if(lisJsonListmx != null) {
				message += "病人体征下载成功\r\n";
			}else{
				message += "病人体征下载失败\r\n";
			}
		    TableClass  listz_table = SysConfigDao.getValue(context,"patient","vital-signs");
			System.out.println("插入表明细===="+listz_table.getClassName()+listz_table.getType());
			
			if(lisJsonListmx != null) {
				System.out.println("病人体征开始插入====");
				CommonJsonDao.insertJSONDATA(context, lisJsonListtz, listz_table);
			    System.out.println("病人体征插入成功====");
			}
	    }

		if (!status){
			PatientList.updateProgress.sendEmptyMessage(90);
		}
		//查房便签下载

		String params = "{\"syxh\":" + syxh + ",\"yexh\":" + yexh + ",\"ysdm\":\"" + ysdm + "\"}";
//				System.out.println("params========="+params);
		List<DrInfo> cfjls = NoteAction.getDoctorDailyRecordList(context, params);
		List<MediaList> cfjlmxs = new ArrayList<MediaList>();

		System.out.println("查房记录插入====");
		cfjlmxs = noteDao.insertCfjl(context,cfjls);
		System.out.println("查房记录插入成功====");
		System.out.println("查房记录明细插入====");
		noteDao.insertCfjlmx(context,cfjlmxs);
		System.out.println("查房记录明细插入成功====");

        //下载多媒体文件
		medialist = new ArrayList<MediaList>();
		for(int i=0;i<cfjls.size();i++){
			medialist.addAll(NoteAction.getMediaInfo(context, "{\"cfxh\":" + cfjls.get(i).getXh() + "}"));
		}

		if (SystemUtil.isConnect(context)) {
			//服务器下载文件
			noteServerThread = new Thread(new NoteServerDownLoad());
			noteServerThread.start();
		}


		//圖像
//		imgUrl = "http://192.168.10.6:8787/20100913054924876.jpg";
//		imgName ="text";
//		GetImage getimage = new GetImage();
//		new Thread(getimage).start();
		
		
//	    
//	    
////	    CommonJson lisJson = CommonJsonAction.getCommoninfo(context, "patient", " info", maptemp.toString());
////	    
////	    CommonJson risJson = CommonJsonAction.getCommoninfo(context, "patient", " info", maptemp.toString());
//	    
//	    long endTime=System.currentTimeMillis(); //获取结束时间
//		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
//		message += "程序运行时间： "+(endTime-startTime)+"ms";
		

		
		return message;
	}

	public class NoteServerDownLoad implements Runnable{
		public void run() {
			for (int i=0;i < medialist.size();i++){
				ServerUtil.downloadfromServer(medialist.get(i));
			}
		}
	}
	
	public class GetImage implements Runnable {

		@Override
		public void run() {
			URL url;
			try {
					url = new URL(imgUrl);
					InputStream is = url.openStream();
					bitmap = BitmapFactory.decodeStream(is);
					saveImage(bitmap, imgName);
		//			handler.sendEmptyMessage(GETPIC_OK);
					is.close();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	
	private void saveImage(Bitmap bitmap, String imgName) throws IOException {
//		
//		File dirFile = new File(getMkDir(),"image");
//		
////		if (!dirFile.exists()) {
////			dirFile.mkdir();
////		}
////		
//      
//		dirFile.mkdirs();
		
//		String path = getMkDir() + "/" + imgName+".jpg";
		String path = getMkDir() + "/" +imgName;
		System.out.println(getMkDir() + imgName);
		
		File myCaptureFile = new File(path);
		BufferedOutputStream bos = new BufferedOutputStream(
		new FileOutputStream(myCaptureFile));
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}
	
	// 获取文件夹
	public String getMkDir() {
		String saveDir = "";
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
		if(sdCardExist) {
			 saveDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobileclinical";
			 System.out.println(saveDir);
		}
		else
		{
			
			
			saveDir = "";
//			saveDir = Environment.getExternalStorageDirectory()
//		            .toString();
		}
		
		
		File dir = new File(saveDir);
		if (!dir.exists()) {
			dir.mkdir(); // 创建文件夹
		}
		
		return saveDir;
	}
	
}
