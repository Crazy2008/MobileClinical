package com.winning.mobileclinical.globalCache;

import android.graphics.Bitmap;

import com.winning.mobileclinical.model.MenuDTO;
import com.winning.mobileclinical.model.SYS_CONFIG;
import com.winning.mobileclinical.model.cis.BedSearchinfo;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.MisConfigInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.model.cis.TableClass;
import com.winning.mobileclinical.web.WebUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 全局数据管理类
 * 
 * */
public class GlobalCache {


	private static GlobalCache cache = null;

	private   String CFJLUploadUrl;
	private   String CFJLDownloadUrl;
	private   String EditBookMarkUrl;
	private   String EditBookMarkDownloadUrl;

	private Bitmap bitmap;

	private  String host= WebUtils.HOST;
	private String url=WebUtils.WEBSERVICE;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	//当前登录医生
	private DoctorInfo doctor;
	private SYS_CONFIG sysconfig;
	List<SYS_CONFIG> sysConfigs = null;
	List<PatientInfo> patientList = null;	//病人列表
	
	List<BedSearchinfo> bedSearchinfos = new ArrayList<BedSearchinfo>();	//病人列表
	
	List<TableClass> tableClasses = null;	//方法映射
	
	List<MisConfigInfo> misConfigInfos = null;	//系统配置
	
	List<DeptWardMapInfo> deptWardMapInfos = null; //病区科室list
	
	private int bqSel = -1;
	private PatientInfo patient_selected = null;	//选中的病人信息
	List<MenuDTO> menuList = null;
	
	private int wjzcount = 0;
	private int width;
	private Boolean loadPatient = false;
	private Boolean loadImpPatient = false;
	private Boolean loadOperation = false;
	private Boolean loadRemind = false;
	private Boolean loadDoctor = false;
	
	private Boolean loadEMR = false;
	private Boolean loadOrder = false;
	private Boolean loadRIS = false;
	private Boolean loadLIS = false;
	private Boolean loadLCLJ = false;
	private Boolean loadTchart = false;
	
	private Boolean isofflion = false;   //false在线   true 离线
	
	private int progress = 0;


	private Bitmap originalBitmap;
	public static final int MSG_UPDATE_STATUS = 0;
	public static final int MSG_UPDATE_CONNECTION_TIME = 1;
	public static final int MSG_COMPLETE_STATUS = 2;
	public static final int UPDATE_THRESHOLD = 300;


	public   Bitmap getOriginalBitmap() {
		return originalBitmap;
	}
	public  void setOriginalBitmap(Bitmap originalBitmap) {
		this.originalBitmap = originalBitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Boolean getIsofflion() {
		return isofflion;
	}

	public void setIsofflion(Boolean isofflion) {
		this.isofflion = isofflion;
	}

	private   HashMap<String, String> map = new HashMap<String, String>();
	
	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

	public Boolean getLoadRIS() {
		return loadRIS;
	}

	public void setLoadRIS(Boolean loadRIS) {
		this.loadRIS = loadRIS;
	}

	public Boolean getLoadLIS() {
		return loadLIS;
	}

	public void setLoadLIS(Boolean loadLIS) {
		this.loadLIS = loadLIS;
	}

	public Boolean getLoadEMR() {
		return loadEMR;
	}

	public void setLoadEMR(Boolean loadEMR) {
		this.loadEMR = loadEMR;
	}
	
	public Boolean getLoadLCLJ() {
		return loadLCLJ;
	}

	public void setLoadLCLJ(Boolean loadLCLJ) {
		this.loadLCLJ = loadLCLJ;
	}

	public Boolean getLoadTchart() {
		return loadTchart;
	}

	public void setLoadTchart(Boolean loadTchart) {
		this.loadTchart = loadTchart;
	}

	public Boolean getLoadPatient() {
		return loadPatient;
	}

	public void setLoadPatient(Boolean loadPatient) {
		this.loadPatient = loadPatient;
	}

	public Boolean getLoadOrder() {
		return loadOrder;
	}

	public void setLoadOrder(Boolean loadOrder) {
		this.loadOrder = loadOrder;
	}


	public Boolean getLoadImpPatient() {
		return loadImpPatient;
	}

	public void setLoadImpPatient(Boolean loadImpPatient) {
		this.loadImpPatient = loadImpPatient;
	}

	public Boolean getLoadRemind() {
		return loadRemind;
	}

	public void setLoadRemind(Boolean loadRemind) {
		this.loadRemind = loadRemind;
	}

	public Boolean getLoadDoctor() {
		return loadDoctor;
	}

	public void setLoadDoctor(Boolean loadDoctor) {
		this.loadDoctor = loadDoctor;
	}

	public List<MenuDTO> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<MenuDTO> menuList) {
		this.menuList = menuList;
	}

	private GlobalCache() {
		
	}
	
	public static GlobalCache getCache() {
		
		if(cache == null) 
			synchronized(GlobalCache.class){
			    cache = new GlobalCache();
	        }
		return cache;
	}
	
	public void clearData(){
		doctor = null;
		sysconfig = null;
	}
	
	public void init() {
		loadPatient = false;
		loadImpPatient = false;
		loadOperation = false;
		loadRemind = false;
		loadDoctor = false;
		
		loadOrder = false;
		loadRIS = false;
		loadLIS = false;
		loadEMR = false;
		loadLCLJ = false;
		loadTchart = false;
	}
	
	public void init2() {
		loadOrder = false;
		loadRIS = false;
		loadLIS = false;
		loadEMR = false;
		loadLCLJ = false;
		loadTchart = false;
	}
	
	public void initData() {
		bedSearchinfos = null;
	}

	public DoctorInfo getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorInfo doctor) {
		this.doctor = doctor;
	}

	public List<PatientInfo> getPatientList() {
		return patientList;
	}

	public void setPatientList(List<PatientInfo> patientList) {
		this.patientList = patientList;
	}

	public List<DeptWardMapInfo> getDeptWardMapInfos() {
		return deptWardMapInfos;
	}

	public void setDeptWardMapInfos(List<DeptWardMapInfo> deptWardMapInfos) {
		this.deptWardMapInfos = deptWardMapInfos;
	}


	public PatientInfo getPatient_selected() {
		return patient_selected;
	}
	public void setPatient_selected(PatientInfo patient_selected) {
		this.patient_selected = patient_selected;
	}

	public SYS_CONFIG getSysconfig() {
		return sysconfig;
	}

	public void setSysconfig(SYS_CONFIG sysconfig) {
		this.sysconfig = sysconfig;
	}

	public static void setCache(GlobalCache cache) {
		GlobalCache.cache = cache;
	}

	public List<SYS_CONFIG> getSysConfigs() {
		return sysConfigs;
	}

	public int getBqSel() {
		return bqSel;
	}

	public void setBqSel(int bqSel) {
		this.bqSel = bqSel;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public List<TableClass> getTableClasses() {
		return tableClasses;
	}

	public void setTableClasses(List<TableClass> tableClasses) {
		this.tableClasses = tableClasses;
	}

	public List<BedSearchinfo> getBedSearchinfos() {
		return bedSearchinfos;
	}

	public void setBedSearchinfos(List<BedSearchinfo> bedSearchinfos) {
		this.bedSearchinfos = bedSearchinfos;
	}

	public List<MisConfigInfo> getMisConfigInfos() {
		return misConfigInfos;
	}

	public void setMisConfigInfos(List<MisConfigInfo> misConfigInfos) {
		this.misConfigInfos = misConfigInfos;
	}

	public int getWjzcount() {
		return wjzcount;
	}

	public void setWjzcount(int wjzcount) {
		this.wjzcount = wjzcount;
	}

	public Boolean getLoadOperation() {
		return loadOperation;
	}

	public void setLoadOperation(Boolean loadOperation) {
		this.loadOperation = loadOperation;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getCFJLUploadUrl() {
		return getHost()+"/upload.ashx?name=dailyrecord";
	}



	public String getCFJLDownloadUrl() {
		return getHost()+"/download.ashx?name=dailyrecord";
	}



	public String getEditBookMarkUrl() {
		return getHost()+"/upload.ashx?name=emr";
	}



	public String getEditBookMarkDownloadUrl() {
		return getHost()+"/download.ashx?name=emr";
	}


}


