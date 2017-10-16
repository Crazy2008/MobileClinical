package com.winning.mobileclinical.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *         数据库第一次创建时onCreate方法会被调用，我们可以执行创建表的语句，
 *         当系统发现版本变化之后，会调用onUpgrade方法，我们可以执行修改表结构等语句。
 */

public class DBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "mobileclinical.db";
	private static final int DATABASE_VERSION =201600603;
	private static final String TAG = DBHelper.class.getSimpleName();

	private static DBHelper mInstance;
	
	public synchronized static DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	}
	public synchronized static SQLiteDatabase getWritableDatabase(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		try {
			SQLiteDatabase db=mInstance.getWritableDatabase();
			return db;
		} catch (Exception e) {
			return null;
		}
	}
	
	public  static void close(SQLiteDatabase db)
	{
	}
	
	public synchronized static void destoryInstance() {
		if (mInstance != null) {
			mInstance.close();
		}
	}


	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public DBHelper(Context context, int version) {
		super(context, DATABASE_NAME, null, version);
	}

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	// 在数据库第一次被创建的时候 会执行onCreate();
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("我被调用了 oncreate");
		
		//病人信息
		String mobile_patientinfo = "CREATE TABLE MOBILE_PATIENTINFO("+
									"EMRXH    	NUMERIC(9,0)  NOT NULL," +
									"SYXH    	NUMERIC(9,0)  NOT NULL," +
									"YEXH    	NUMERIC(9,0)      NULL," +
									"BLH    	VARCHAR(32)       NULL," +
									"NAME    	VARCHAR(12)       NULL," +
									"SEX    	CHAR(2)           NULL," +
									"AGE    	CHAR(4)           NULL," +
									"BIRTH    	VARCHAR(12)       NULL," +
									"PY    	    VARCHAR(12)       NULL," +
									"WB    	    VARCHAR(12)       NULL," +
									"SFZH    	VARCHAR(32)       NULL," +
									"BRZT    	INT               NULL," +
									"KSDM    	VARCHAR(12)       NULL," +
									"KSMC    	VARCHAR(12)       NULL," +
									"BQDM    	VARCHAR(12)       NULL," +
									"BQMC    	VARCHAR(12)       NULL," +
									"CWDM    	CHAR(4)           NULL," +
									"HLDM    	VARCHAR(12)       NULL," +
									"HLMC    	VARCHAR(12)       NULL," +
									"RYRQ    	VARCHAR(32)       NULL," +
									"RQRQ    	VARCHAR(32)       NULL," +
									"CQRQ    	VARCHAR(32)       NULL," +
									"CYRQ    	VARCHAR(32)       NULL," +
									"WZJB    	CHAR(4)           NULL," +
									"ZDDM    	VARCHAR(12)       NULL," +
									"ZDMC    	VARCHAR(32)       NULL," +
									"CYFS    	VARCHAR(32)       NULL," +
									"JGBZ    	INT               NULL," +
									"YBDM    	VARCHAR(12)       NULL," +
									"YBSM    	VARCHAR(32)       NULL," +
									"PZLX    	INT               NULL," +
									"BRLX    	CHAR(2)           NULL," +
									"PZH    	VARCHAR(32)       NULL," +
									"CARDNO    	VARCHAR(32)       NULL," +
									"CARDTYPE   VARCHAR(12)       NULL," +
									"CYZDDM    	VARCHAR(12)       NULL," +
									"CYZDMC    	VARCHAR(32)       NULL," +
									"LCLJBZ    	INT               NULL," +
									"BLGDBZ    	INT               NULL," +
									"ZZYSDM    	VARCHAR(12)       NULL," +
									"ZZYSMC    	VARCHAR(12)       NULL)";
		
		//病人基础信息
		String mobile_patbasicinfo = "CREATE TABLE MOBILE_PATBASICINFO("+
									 "BLH    	VARCHAR(12)   NOT NULL," +
									 "HYZKDM    VARCHAR(12)       NULL," +
									 "HYZKMC    VARCHAR(32)       NULL," +
									 "MZDM    	VARCHAR(12)       NULL," +
									 "MZMC    	VARCHAR(32)       NULL," +
									 "GJDM    	VARCHAR(12)       NULL," +
									 "GJMC    	VARCHAR(12)       NULL," +
									 "WHCD    	VARCHAR(12)       NULL," +
									 "JYNX    	INT               NULL," +
									 "CSDDM    	VARCHAR(12)       NULL," +
									 "CSDMC    	VARCHAR(32)       NULL," +
									 "CSQXDM    VARCHAR(12)       NULL," +
									 "CSQXMC    VARCHAR(32)       NULL," +
									 "JGSSDM    VARCHAR(12)       NULL," +
									 "JGSSMC    VARCHAR(32)       NULL," +
									 "JGQXDM    VARCHAR(12)       NULL," +
									 "JGQXMC    VARCHAR(32)       NULL," +
									 "JOB_CODE  VARCHAR(12)       NULL," +
									 "JOB_NAME  VARCHAR(32)       NULL," +
									 "GZDW    	VARCHAR(32)       NULL," +
									 "GZDWDZ    VARCHAR(32)       NULL," +
									 "GZDWDH    VARCHAR(12)       NULL," +
									 "GZDWYB    VARCHAR(12)       NULL," +
									 "HKDZ    	VARCHAR(32)       NULL," +
									 "HKDH    	VARCHAR(12)       NULL," +
									 "XZDZDM    VARCHAR(12)       NULL," +
									 "XZDZMC    VARCHAR(32)       NULL," +
									 "XZQXDM    VARCHAR(12)       NULL," +
									 "XZQXMC    VARCHAR(32)       NULL," +
									 "XZDZJD    VARCHAR(32)       NULL," +
									 "XZLXDH    VARCHAR(12)       NULL," +
									 "XZDZYB    VARCHAR(12)       NULL)";
		
		//医生信息
		String mobile_doctorinfo = "CREATE TABLE MOBILE_DOCTORINFO("+
				 				     "ID    	VARCHAR(12)   NOT NULL," +
				 				     "NAME    	VARCHAR(12)       NULL," +
				 				     "PY    	VARCHAR(12)       NULL," +
				 				     "WB    	VARCHAR(12)       NULL," +
				 				     "SEX    	CHAR(2)           NULL," +
				 				     "KSDM    	VARCHAR(12)       NULL," +
				 				     "BQDM    	VARCHAR(12)       NULL," +
				 				     "ZGLB    	CHAR(2)           NULL," +
				 				     "YXDL    	INT               NULL," +
				 				     "ZCDM    	VARCHAR(12)       NULL," +
				 				     "ZCMC    	VARCHAR(12)       NULL," +
				 				     "PHONE    	VARCHAR(12)       NULL," +
				 				     "JLZT    	CHAR(2)           NULL)";
		
		//科室信息
		String mobile_deptinfo =  "CREATE TABLE MOBILE_DEPTINFO("+
									 "ID    	    VARCHAR(12)   NOT NULL," +
								     "NAME    	    VARCHAR(12)       NULL," +
								     "PY    	    VARCHAR(12)       NULL," +
								     "WB    	    VARCHAR(12)       NULL," +
								     "HOSPITAL_ID   VARCHAR(12)       NULL," +
								     "EJ_ID    	    VARCHAR(12)       NULL," +
								     "EJ_NAME    	VARCHAR(12)       NULL," +
								     "EJ_PY    	    VARCHAR(12)       NULL," +
								     "EJ_WB    	    VARCHAR(12)       NULL," +
								     "JLZT    	    INT               NULL)";
		
		//病区信息
		String mobile_wardinfo =  "CREATE TABLE MOBILE_WARDINFO("+
									 "ID    	    VARCHAR(12)   NOT NULL," +
								     "NAME    	    VARCHAR(12)       NULL," +
								     "PY    	    VARCHAR(12)       NULL," +
								     "WB    	    VARCHAR(12)       NULL," +
								     "HOSPITAL_ID   VARCHAR(12)       NULL," +
								     "CATEGORY    	INT               NULL," +
								     "JLZT    	    INT               NULL)";
		
		//科室病区对应信息
		String mobile_deptwardmapinfo =  "CREATE TABLE MOBILE_DEPTWARDMAPINFO("+
									 "YSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
								     "BQMC    	VARCHAR(12)       NULL," +
								     "KSDM    	VARCHAR(12)       NULL," +
								     "KSMC    	VARCHAR(12)       NULL)";
		
		
		//床位对应信息
		String mobile_bedinfo =  "CREATE TABLE MOBILE_BEDINFO("+
											 "CWDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
										     "BQMC    	VARCHAR(12)       NULL," +
										     "ROOM    	VARCHAR(12)       NULL," +
										     "KSDM    	VARCHAR(12)       NULL," +
											 "KSMC    	VARCHAR(12)       NULL," +
										     "CWFDM    	VARCHAR(12)       NULL," +
										     "GFCWFDM    	VARCHAR(12)       NULL," +
										     "ZYYSDM    	VARCHAR(12)       NULL," +
											 "ZZYSDM    	VARCHAR(12)       NULL," +
										     "ZRYSDM    	VARCHAR(12)       NULL," +
										     "CWLX    	INT               NULL," +
										     "BZLX    	INT               NULL," +
										     "ZCBZ    	INT               NULL," +
										     "TXBZ    	INT               NULL," +
										     "QYID    	INT               NULL," +
										     "QYMC    	VARCHAR(12)       NULL)";
		
		
		String mobile_orderinfo = "CREATE TABLE MOBILE_ORDERINFO("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "KSMC    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "BQMC    	VARCHAR(12)       NULL," +
									 "YSDM    	VARCHAR(12)       NULL," +
									 "YSMC    	VARCHAR(12)       NULL," +
									 "LRRQ    	VARCHAR(32)       NULL," +
									 "KSSJ    	VARCHAR(32)       NULL," +
									 "XH    	NUMERIC(9,0)  NOT NULL," +
									 "FZXH    	NUMERIC(9,0)      NULL," +
									 "YZBZ    	INT               NULL," +
									 "SJXH    	NUMERIC(9,0)      NULL," +
									 "IDM    	NUMERIC(9,0)      NULL," +
									 "YPDM    	VARCHAR(12)       NULL," +
									 "LCXMDM    VARCHAR(12)       NULL," +
									 "YPMC    	VARCHAR(12)       NULL," +
									 "YPGG    	VARCHAR(12)       NULL," +
									 "DXMDM    	VARCHAR(12)       NULL," +
									 "YZLB    	INT               NULL," +
									 "PCDM    	CHAR(2)           NULL," +
									 "PCMC    	VARCHAR(12)       NULL," +
									 "PCZXSJ    VARCHAR(32)       NULL," +
									 "YPYF    	VARCHAR(12)       NULL," +
									 "YPYFMC    VARCHAR(12)       NULL," +
									 "YPJL    	VARCHAR(12)       NULL," +
									 "JLDW    	VARCHAR(12)       NULL," +
									 "DWLB    	INT               NULL," +
									 "YPSL    	NUMERIC(9,0)      NULL," +
									 "ZXDW    	VARCHAR(12)       NULL," +
									 "YZNR    	VARCHAR(128)       NULL," +
									 "ZXKSDM    VARCHAR(12)       NULL," +
									 "ZXKSMC    VARCHAR(12)       NULL," +
									 "SHCZYH    VARCHAR(12)       NULL," +
									 "SHCZYMC   VARCHAR(12)       NULL," +
									 "SHSJ    	VARCHAR(32)       NULL," +
									 "ZXCZYH    VARCHAR(12)       NULL," +
									 "ZXCZYMC   VARCHAR(12)       NULL," +
									 "ZXCZYQM   VARCHAR(12)       NULL," +
									 "ZXSJ    	VARCHAR(32)       NULL," +
									 "TZSJ    	VARCHAR(32)       NULL," +
									 "TZCZYH    VARCHAR(12)       NULL," +
									 "TZCZYMC   VARCHAR(12)       NULL," +
									 "DCCZYH    VARCHAR(12)       NULL," +
									 "DCCZYMC   VARCHAR(12)       NULL," +
									 "DCSJ    	VARCHAR(32)       NULL," +
									 "YSQM    	VARCHAR(12)       NULL)";
		
		//普通保存JSON数据表
		String mobile_jsondata = "CREATE TABLE MOBILE_JSONDATA("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +
									 "JSON    	BLOB)";
		
		
		//服务配置表
		String mobile_config = "CREATE TABLE MOBILE_CONFIG("+
									 "PROVIDER    	VARCHAR(12)      NULL," +
									 "SERVICE    	VARCHAR(12)      NULL," +
									 "CLASSNAME    	VARCHAR(12)       NULL," +
									 "SINGLE    	INT               NULL," +
									 "TYPE    	VARCHAR(12)       NULL)";
		
		
				//普通保存JSON数据表 病人信息
				String mobile_patientData0 = "CREATE TABLE PatientData0("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表 医嘱信息
				String mobile_patientData1 = "CREATE TABLE PatientData1("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表 emr信息
				String mobile_patientData2 = "CREATE TABLE PatientData2("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData3 = "CREATE TABLE PatientData3("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表 lis信息
				String mobile_patientData4 = "CREATE TABLE PatientData4("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData5 = "CREATE TABLE PatientData5("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData6 = "CREATE TABLE PatientData6("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData7 = "CREATE TABLE PatientData7("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData8 = "CREATE TABLE PatientData8("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData9 = "CREATE TABLE PatientData9("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData10 = "CREATE TABLE PatientData10("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData11 = "CREATE TABLE PatientData11("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData12 = "CREATE TABLE PatientData12("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
				//普通保存JSON数据表
				String mobile_patientData13 = "CREATE TABLE PatientData13("+
											 "SYXH    	NUMERIC(9,0)      NULL," +
											 "YEXH    	NUMERIC(9,0)      NULL," +
											 "KSDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
											 "TYPE    	VARCHAR(12)       NULL," +  
											 "JSON    	BLOB)";
				
		//备忘录配置表、记录表
		      String mobile_mcs_config = "CREATE TABLE MCS_CONFIG("+
									     "XH    	    INTEGER           NOT NULL PRIMARY KEY AUTOINCREMENT," +
										 "ID    	    VARCHAR(64)       NULL," +
										 "MODULEID      INT           NOT NULL," +
										 "MODULENAME    VARCHAR(64)       NULL," +
										 "FUNCTYPEID    INT               NULL," +  
										 "FUNCTYPENAME  VARCHAR(64)       NULL," +  
										 "FUNCID        INT           NOT NULL," +  
										 "FUNCNAME      VARCHAR(64)       NULL," +
										 "CONFIGVALUE   VARCHAR(256)      NULL," +
										 "EXTERNALVALUE VARCHAR(256)      NULL," +
										 "MEMO    	    VARCHAR(256)      NULL)";

		;


		      
		      String mobile_mcs_cfjl = "CREATE TABLE MCS_CFJL("+
										 "XH    	    INTEGER           NOT NULL PRIMARY KEY," +
										 "SYXH    	    NUMERIC(9,0)      NULL," +
										 "YEXH    	    NUMERIC(9,0)      NULL," +
										 "MEMO    	    VARCHAR(64)       NULL," +
										 "YSDM    	    VARCHAR(6)    NOT NULL," +
										 "CJRQ    	    VARCHAR(32)       NULL," +
										 "CLZT    	    INT               NULL)";
		      
		      String mobile_mcs_cfjlmx = "CREATE TABLE MCS_CFJL_MX("+
					                     "XH    	    INTEGER           NOT NULL PRIMARY KEY AUTOINCREMENT," +
										 "CFXH    	    NUMERIC(9,0)      NOT NULL," +
										 "FLAG    	    INT               NULL," +
					  					 "NAME    	    VARCHAR(64)       NULL," +
										 "CJRQ    	    VARCHAR(32)       NULL)";
		      
		      String mobile_mcs_image = "CREATE TABLE MCS_IMAGE("+
						 "SYXH    	    NUMERIC(9,0)      NULL," +
						 "YEXH    	    NUMERIC(9,0)      NULL," +
						 "KSDM    	    VARCHAR(12)       NULL," +  
						 "BQDM    	    VARCHAR(12)       NULL," +  
						 "KEY    	    VARCHAR(32)       NULL," +  
						 "RES_ID    	VARCHAR(128)       NULL," +   
						 "SRC    	    VARCHAR(128)       NULL)";
		      
		try {
			
			db.execSQL(mobile_patientinfo);
			db.execSQL(mobile_patbasicinfo);
			db.execSQL(mobile_doctorinfo);
			db.execSQL(mobile_deptinfo);
			db.execSQL(mobile_wardinfo);
			db.execSQL(mobile_deptwardmapinfo);
			
			db.execSQL(mobile_bedinfo);
			
			db.execSQL(mobile_orderinfo);
			db.execSQL(mobile_jsondata);
			db.execSQL(mobile_config);
			
			db.execSQL(mobile_patientData0);
			db.execSQL(mobile_patientData1);
			db.execSQL(mobile_patientData2);
			db.execSQL(mobile_patientData3);
			db.execSQL(mobile_patientData4);
			db.execSQL(mobile_patientData5);
			db.execSQL(mobile_patientData6);
			db.execSQL(mobile_patientData7);
			db.execSQL(mobile_patientData8);
			db.execSQL(mobile_patientData9);
			db.execSQL(mobile_patientData10);
			db.execSQL(mobile_patientData11);
			db.execSQL(mobile_patientData12);
			db.execSQL(mobile_patientData13);
			
			db.execSQL(mobile_mcs_config);
			db.execSQL(mobile_mcs_cfjl);
			db.execSQL(mobile_mcs_cfjlmx);
			db.execSQL(mobile_mcs_image);

//			db.execSQL("INSERT INTO MCS_CONFIG VALUES('SYS01',1,'系统配置',1,'数据访问',1,'电子病历使用版本 ','1','','1 webservice模式 2 前台直接访问')");
			db.execSQL("INSERT INTO MCS_CONFIG('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO') VALUES('SYS01',1,'系统配置',1,'数据访问',1,'电子病历使用版本 ','1','','1 webservice模式 2 前台直接访问')");
			db.execSQL("INSERT INTO MCS_CONFIG ('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO') VALUES('EMR01',2,'电子病历',1,'电子病历使用版本',1,'电子病历使用版本 ','1','','1 卫宁EMR')");
			db.execSQL("INSERT INTO MCS_CONFIG('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO') VALUES('LIS01',3,'检验',1,'检验版本',1,'检验版本','1','','1 卫宁'");
			db.execSQL("INSERT INTO MCS_CONFIG('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO') VALUES('RIS01',4,'检查',1,'检查版本',1,'检查版本','1','','1 卫宁')");
			db.execSQL("INSERT INTO MCS_CONFIG('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO') VALUES('NIS01',5,'护理',1,'护理版本',1,'护理版本','1','','1 卫宁')");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("on update ");
		
		//病人、医生、科室、病区、医嘱
		String drop_mobile_patientinfo = "DROP TABLE if exists MOBILE_PATIENTINFO;";
		String drop_mobile_patbasicinfo = "DROP TABLE if exists MOBILE_PATBASICINFO;";
		String drop_mobile_doctorinfo = "DROP TABLE if exists MOBILE_DOCTORINFO;";
		String drop_mobile_deptinfo = "DROP TABLE if exists MOBILE_DEPTINFO;";
		String drop_mobile_wardinfo = "DROP TABLE if exists MOBILE_WARDINFO;";
		String drop_mobile_deptwardmapinfo = "DROP TABLE if exists MOBILE_DEPTWARDMAPINFO;";
		String drop_mobile_orderinfo = "DROP TABLE if exists MOBILE_ORDERINFO;";
		String drop_mobile_jsondata = "DROP TABLE if exists MOBILE_JSONDATA;";
		String drop_mobile_config = "DROP TABLE if exists MOBILE_CONFIG;";
		
		String drop_mobile_patientData0 = "DROP TABLE if exists PatientData0;";
		String drop_mobile_patientData1 = "DROP TABLE if exists PatientData1;";
		String drop_mobile_patientData2 = "DROP TABLE if exists PatientData2;";
		String drop_mobile_patientData3 = "DROP TABLE if exists PatientData3;";
		String drop_mobile_patientData4 = "DROP TABLE if exists PatientData4;";
		String drop_mobile_patientData5 = "DROP TABLE if exists PatientData5;";
		String drop_mobile_patientData6 = "DROP TABLE if exists PatientData6;";
		String drop_mobile_patientData7 = "DROP TABLE if exists PatientData7;";
		String drop_mobile_patientData8 = "DROP TABLE if exists PatientData8;";
		String drop_mobile_patientData9 = "DROP TABLE if exists PatientData9;";
		String drop_mobile_patientData10 = "DROP TABLE if exists PatientData10;";
		String drop_mobile_patientData11 = "DROP TABLE if exists PatientData11;";
		String drop_mobile_patientData12 = "DROP TABLE if exists PatientData12;";
		String drop_mobile_patientData13 = "DROP TABLE if exists PatientData13;";
		
		String drop_mobile_mcs_config = "DROP TABLE if exists MCS_CONFIG;";
		String drop_mobile_mcs_cfjl = "DROP TABLE if exists MCS_CFJL;";
		String drop_mobile_mcs_cfjlmx = "DROP TABLE if exists MCS_CFJL_MX;";
		
		String drop_mobile_bedinfo = "DROP TABLE if exists MOBILE_BEDINFO;";
		
		String drop_mobile_image = "DROP TABLE if exists MCS_IMAGE;";
		
		//病人信息
		String mobile_patientinfo = "CREATE TABLE MOBILE_PATIENTINFO("+
									"EMRXH    	NUMERIC(9,0)  NOT NULL," +
									"SYXH    	NUMERIC(9,0)  NOT NULL," +
									"YEXH    	NUMERIC(9,0)      NULL," +
									"BLH    	VARCHAR(32)       NULL," +
									"NAME    	VARCHAR(12)       NULL," +
									"SEX    	CHAR(2)           NULL," +
									"AGE    	CHAR(4)           NULL," +
									"BIRTH    	VARCHAR(12)       NULL," +
									"PY    	    VARCHAR(12)       NULL," +
									"WB    	    VARCHAR(12)       NULL," +
									"SFZH    	VARCHAR(32)       NULL," +
									"BRZT    	INT               NULL," +
									"KSDM    	VARCHAR(12)       NULL," +
									"KSMC    	VARCHAR(12)       NULL," +
									"BQDM    	VARCHAR(12)       NULL," +
									"BQMC    	VARCHAR(12)       NULL," +
									"CWDM    	CHAR(4)           NULL," +
									"HLDM    	VARCHAR(12)       NULL," +
									"HLMC    	VARCHAR(12)       NULL," +
									"RYRQ    	VARCHAR(32)       NULL," +
									"RQRQ    	VARCHAR(32)       NULL," +
									"CQRQ    	VARCHAR(32)       NULL," +
									"CYRQ    	VARCHAR(32)       NULL," +
									"WZJB    	CHAR(4)           NULL," +
									"ZDDM    	VARCHAR(12)       NULL," +
									"ZDMC    	VARCHAR(32)       NULL," +
									"CYFS    	VARCHAR(32)       NULL," +
									"JGBZ    	INT               NULL," +
									"YBDM    	VARCHAR(12)       NULL," +
									"YBSM    	VARCHAR(32)       NULL," +
									"PZLX    	INT               NULL," +
									"BRLX    	CHAR(2)           NULL," +
									"PZH    	VARCHAR(32)       NULL," +
									"CARDNO    	VARCHAR(32)       NULL," +
									"CARDTYPE   VARCHAR(12)       NULL," +
									"CYZDDM    	VARCHAR(12)       NULL," +
									"CYZDMC    	VARCHAR(32)       NULL," +
									"LCLJBZ    	INT               NULL," +
									"BLGDBZ    	INT               NULL," +
									"ZZYSDM    	VARCHAR(12)       NULL," +
									"ZZYSMC    	VARCHAR(12)       NULL)";
		
		//病人基础信息
		String mobile_patbasicinfo = "CREATE TABLE MOBILE_PATBASICINFO("+
									 "BLH    	VARCHAR(12)   NOT NULL," +
									 "HYZKDM    VARCHAR(12)       NULL," +
									 "HYZKMC    VARCHAR(32)       NULL," +
									 "MZDM    	VARCHAR(12)       NULL," +
									 "MZMC    	VARCHAR(32)       NULL," +
									 "GJDM    	VARCHAR(12)       NULL," +
									 "GJMC    	VARCHAR(12)       NULL," +
									 "WHCD    	VARCHAR(12)       NULL," +
									 "JYNX    	INT               NULL," +
									 "CSDDM    	VARCHAR(12)       NULL," +
									 "CSDMC    	VARCHAR(32)       NULL," +
									 "CSQXDM    VARCHAR(12)       NULL," +
									 "CSQXMC    VARCHAR(32)       NULL," +
									 "JGSSDM    VARCHAR(12)       NULL," +
									 "JGSSMC    VARCHAR(32)       NULL," +
									 "JGQXDM    VARCHAR(12)       NULL," +
									 "JGQXMC    VARCHAR(32)       NULL," +
									 "JOB_CODE  VARCHAR(12)       NULL," +
									 "JOB_NAME  VARCHAR(32)       NULL," +
									 "GZDW    	VARCHAR(32)       NULL," +
									 "GZDWDZ    VARCHAR(32)       NULL," +
									 "GZDWDH    VARCHAR(12)       NULL," +
									 "GZDWYB    VARCHAR(12)       NULL," +
									 "HKDZ    	VARCHAR(32)       NULL," +
									 "HKDH    	VARCHAR(12)       NULL," +
									 "XZDZDM    VARCHAR(12)       NULL," +
									 "XZDZMC    VARCHAR(32)       NULL," +
									 "XZQXDM    VARCHAR(12)       NULL," +
									 "XZQXMC    VARCHAR(32)       NULL," +
									 "XZDZJD    VARCHAR(32)       NULL," +
									 "XZLXDH    VARCHAR(12)       NULL," +
									 "XZDZYB    VARCHAR(12)       NULL)";
		
		//医生信息
		String mobile_doctorinfo = "CREATE TABLE MOBILE_DOCTORINFO("+
				 				     "ID    	VARCHAR(12)   NOT NULL," +
				 				     "NAME    	VARCHAR(12)       NULL," +
				 				     "PY    	VARCHAR(12)       NULL," +
				 				     "WB    	VARCHAR(12)       NULL," +
				 				     "SEX    	CHAR(2)           NULL," +
				 				     "KSDM    	VARCHAR(12)       NULL," +
				 				     "BQDM    	VARCHAR(12)       NULL," +
				 				     "ZGLB    	CHAR(2)           NULL," +
				 				     "YXDL    	CHAR(2)           NULL," +
				 				     "ZCDM    	VARCHAR(12)       NULL," +
				 				     "ZCMC    	VARCHAR(12)       NULL," +
				 				     "PHONE    	VARCHAR(12)       NULL," +
				 				     "JLZT    	CHAR(2)           NULL)";
		
		//科室信息
		String mobile_deptinfo =  "CREATE TABLE MOBILE_DEPTINFO("+
									 "ID    	    VARCHAR(12)   NOT NULL," +
								     "NAME    	    VARCHAR(12)       NULL," +
								     "PY    	    VARCHAR(12)       NULL," +
								     "WB    	    VARCHAR(12)       NULL," +
								     "HOSPITAL_ID   VARCHAR(12)       NULL," +
								     "EJ_ID    	    VARCHAR(12)       NULL," +
								     "EJ_NAME    	VARCHAR(12)       NULL," +
								     "EJ_PY    	    VARCHAR(12)       NULL," +
								     "EJ_WB    	    VARCHAR(12)       NULL," +
								     "JLZT    	    INT               NULL)";
		
		//病区信息
		String mobile_wardinfo =  "CREATE TABLE MOBILE_WARDINFO("+
									 "ID    	    VARCHAR(12)   NOT NULL," +
								     "NAME    	    VARCHAR(12)       NULL," +
								     "PY    	    VARCHAR(12)       NULL," +
								     "WB    	    VARCHAR(12)       NULL," +
								     "HOSPITAL_ID   VARCHAR(12)       NULL," +
								     "CATEGORY    	INT               NULL," +
								     "JLZT    	    INT               NULL)";
		
		//科室病区对应信息
		String mobile_deptwardmapinfo =  "CREATE TABLE MOBILE_DEPTWARDMAPINFO("+
									 "YSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
								     "BQMC    	VARCHAR(12)       NULL," +
								     "KSDM    	VARCHAR(12)       NULL," +
								     "KSMC    	VARCHAR(12)       NULL)";
		
		//床位对应信息
		String mobile_bedinfo =  "CREATE TABLE MOBILE_BEDINFO("+
											 "CWDM    	VARCHAR(12)       NULL," +
											 "BQDM    	VARCHAR(12)       NULL," +
										     "BQMC    	VARCHAR(12)       NULL," +
										     "ROOM    	VARCHAR(12)       NULL," +
										     "KSDM    	VARCHAR(12)       NULL," +
											 "KSMC    	VARCHAR(12)       NULL," +
										     "CWFDM    	VARCHAR(12)       NULL," +
										     "GFCWFDM    	VARCHAR(12)       NULL," +
										     "ZYYSDM    	VARCHAR(12)       NULL," +
											 "ZZYSDM    	VARCHAR(12)       NULL," +
										     "ZRYSDM    	VARCHAR(12)       NULL," +
										     "CWLX    	INT               NULL," +
										     "BZLX    	INT               NULL," +
										     "ZCBZ    	INT               NULL," +
										     "TXBZ    	INT               NULL," +
										     "QYID    	INT               NULL," +
										     "QYMC    	VARCHAR(12)       NULL)";
		
		
		String mobile_orderinfo = "CREATE TABLE MOBILE_ORDERINFO("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "KSMC    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "BQMC    	VARCHAR(12)       NULL," +
									 "YSDM    	VARCHAR(12)       NULL," +
									 "YSMC    	VARCHAR(12)       NULL," +
									 "LRRQ    	VARCHAR(32)       NULL," +
									 "KSSJ    	VARCHAR(32)       NULL," +
									 "XH    	NUMERIC(9,0)  NOT NULL," +
									 "FZXH    	NUMERIC(9,0)      NULL," +
									 "YZBZ    	INT               NULL," +
									 "SJXH    	NUMERIC(9,0)      NULL," +
									 "IDM    	NUMERIC(9,0)      NULL," +
									 "YPDM    	VARCHAR(12)       NULL," +
									 "LCXMDM    VARCHAR(12)       NULL," +
									 "YPMC    	VARCHAR(12)       NULL," +
									 "YPGG    	VARCHAR(12)       NULL," +
									 "DXMDM    	VARCHAR(12)       NULL," +
									 "YZLB    	INT               NULL," +
									 "PCDM    	CHAR(2)           NULL," +
									 "PCMC    	VARCHAR(12)       NULL," +
									 "PCZXSJ    VARCHAR(32)       NULL," +
									 "YPYF    	VARCHAR(12)       NULL," +
									 "YPYFMC    VARCHAR(12)       NULL," +
									 "YPJL    	VARCHAR(12)       NULL," +
									 "JLDW    	VARCHAR(12)       NULL," +
									 "DWLB    	INT               NULL," +
									 "YPSL    	NUMERIC(9,0)      NULL," +
									 "ZXDW    	VARCHAR(12)       NULL," +
									 "YZNR    	VARCHAR(128)       NULL," +
									 "ZXKSDM    VARCHAR(12)       NULL," +
									 "ZXKSMC    VARCHAR(12)       NULL," +
									 "SHCZYH    VARCHAR(12)       NULL," +
									 "SHCZYMC   VARCHAR(12)       NULL," +
									 "SHSJ    	VARCHAR(32)       NULL," +
									 "ZXCZYH    VARCHAR(12)       NULL," +
									 "ZXCZYMC   VARCHAR(12)       NULL," +
									 "ZXCZYQM   VARCHAR(12)       NULL," +
									 "ZXSJ    	VARCHAR(32)       NULL," +
									 "TZSJ    	VARCHAR(32)       NULL," +
									 "TZCZYH    VARCHAR(12)       NULL," +
									 "TZCZYMC   VARCHAR(12)       NULL," +
									 "DCCZYH    VARCHAR(12)       NULL," +
									 "DCCZYMC   VARCHAR(12)       NULL," +
									 "DCSJ    	VARCHAR(32)       NULL," +
									 "YSQM    	VARCHAR(12)       NULL)";
		
		
		//普通保存JSON数据表
		String mobile_jsondata = "CREATE TABLE MOBILE_JSONDATA("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  //保存json的类别 医嘱YZ 体温TW 病历EMR,检查RIS，检验等
									 "JSON    	BLOB)";
		
		
		
		//服务配置表
		String mobile_config = "CREATE TABLE MOBILE_CONFIG("+
											 "PROVIDER    	VARCHAR(12)      NULL," +
											 "SERVICE    	VARCHAR(12)      NULL," +
											 "CLASSNAME    	VARCHAR(12)       NULL," +
											 "SINGLE    	INT               NULL," +
											 "TYPE    	VARCHAR(12)       NULL)";
		
		
		//普通保存JSON数据表
		String mobile_patientData0 = "CREATE TABLE PatientData0("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData1 = "CREATE TABLE PatientData1("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData2 = "CREATE TABLE PatientData2("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData3 = "CREATE TABLE PatientData3("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData4 = "CREATE TABLE PatientData4("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData5 = "CREATE TABLE PatientData5("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData6 = "CREATE TABLE PatientData6("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData7 = "CREATE TABLE PatientData7("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData8 = "CREATE TABLE PatientData8("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData9 = "CREATE TABLE PatientData9("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData10 = "CREATE TABLE PatientData10("+
									 "SYXH    	NUMERIC(9,0)      NULL," +
									 "YEXH    	NUMERIC(9,0)      NULL," +
									 "KSDM    	VARCHAR(12)       NULL," +
									 "BQDM    	VARCHAR(12)       NULL," +
									 "TYPE    	VARCHAR(12)       NULL," +  
									 "JSON    	BLOB)";
		
		String mobile_patientData11 = "CREATE TABLE PatientData11("+
				 "SYXH    	NUMERIC(9,0)      NULL," +
				 "YEXH    	NUMERIC(9,0)      NULL," +
				 "KSDM    	VARCHAR(12)       NULL," +
				 "BQDM    	VARCHAR(12)       NULL," +
				 "TYPE    	VARCHAR(12)       NULL," +  
				 "JSON    	BLOB)";

		//普通保存JSON数据表
		String mobile_patientData12 = "CREATE TABLE PatientData12("+
						 "SYXH    	NUMERIC(9,0)      NULL," +
						 "YEXH    	NUMERIC(9,0)      NULL," +
						 "KSDM    	VARCHAR(12)       NULL," +
						 "BQDM    	VARCHAR(12)       NULL," +
						 "TYPE    	VARCHAR(12)       NULL," +  
						 "JSON    	BLOB)";
		
		//普通保存JSON数据表
		String mobile_patientData13 = "CREATE TABLE PatientData13("+
						 "SYXH    	NUMERIC(9,0)      NULL," +
						 "YEXH    	NUMERIC(9,0)      NULL," +
						 "KSDM    	VARCHAR(12)       NULL," +
						 "BQDM    	VARCHAR(12)       NULL," +
						 "TYPE    	VARCHAR(12)       NULL," +  
						 "JSON    	BLOB)";
		
		//备忘录配置表、记录表
	      String mobile_mcs_config = "CREATE TABLE MCS_CONFIG("+
								     "XH    	    INTEGER PRIMARY KEY AUTOINCREMENT," +
									 "ID    	    VARCHAR(64)       NULL," +
									 "MODULEID      INT           NOT NULL," +
									 "MODULENAME    VARCHAR(64)       NULL," +
									 "FUNCTYPEID    INT               NULL," +  
									 "FUNCTYPENAME  VARCHAR(64)       NULL," +  
									 "FUNCID        INT           NOT NULL," +  
									 "FUNCNAME      VARCHAR(64)       NULL," +
									 "CONFIGVALUE   VARCHAR(256)      NULL," +
									 "EXTERNALVALUE VARCHAR(256)      NULL," +
									 "MEMO    	    VARCHAR(256)      NULL)";

		String mobile_mcs_cfjl = "CREATE TABLE MCS_CFJL("+
									"XH    	    INTEGER           NOT NULL PRIMARY KEY," +
									"SYXH    	    NUMERIC(9,0)      NULL," +
									"YEXH    	    NUMERIC(9,0)      NULL," +
									"MEMO    	    VARCHAR(64)       NULL," +
									"YSDM    	    VARCHAR(6)    NOT NULL," +
									"CJRQ    	    VARCHAR(32)       NULL," +
									"CLZT    	    INT               NULL)";

		String mobile_mcs_cfjlmx = "CREATE TABLE MCS_CFJL_MX("+
									"XH    	    INTEGER           NOT NULL PRIMARY KEY AUTOINCREMENT," +
									"CFXH    	    NUMERIC(9,0)      NOT NULL," +
									"FLAG    	    INT               NULL," +
									"NAME    	    VARCHAR(64)       NULL," +
									"CJRQ    	    VARCHAR(32)       NULL)";
	      
	      String mobile_mcs_image = "CREATE TABLE MCS_IMAGE("+
									 "SYXH    	    NUMERIC(9,0)      NULL," +
									 "YEXH    	    NUMERIC(9,0)      NULL," +
									 "KSDM    	    VARCHAR(12)       NULL," +  
									 "BQDM    	    VARCHAR(12)       NULL," +  
									 "KEY    	    VARCHAR(32)       NULL," +  
									 "RES_ID    	VARCHAR(128)       NULL," +  
									 "SRC    	    VARCHAR(128)       NULL)";
		
		try {
			db.execSQL(drop_mobile_patientinfo);
			db.execSQL(drop_mobile_patbasicinfo);
			db.execSQL(drop_mobile_doctorinfo);
			db.execSQL(drop_mobile_deptinfo);
			db.execSQL(drop_mobile_wardinfo);
			db.execSQL(drop_mobile_deptwardmapinfo);
			db.execSQL(drop_mobile_bedinfo);
			db.execSQL(drop_mobile_orderinfo);
			db.execSQL(drop_mobile_jsondata);
			db.execSQL(drop_mobile_config);
			db.execSQL(drop_mobile_patientData0);
			db.execSQL(drop_mobile_patientData1);
			db.execSQL(drop_mobile_patientData2);
			db.execSQL(drop_mobile_patientData3);
			db.execSQL(drop_mobile_patientData4);
			db.execSQL(drop_mobile_patientData5);
			db.execSQL(drop_mobile_patientData6);
			db.execSQL(drop_mobile_patientData7);
			db.execSQL(drop_mobile_patientData8);
			db.execSQL(drop_mobile_patientData9);
			db.execSQL(drop_mobile_patientData10);
			db.execSQL(drop_mobile_patientData11);
			db.execSQL(drop_mobile_patientData12);
			db.execSQL(drop_mobile_patientData13);
			db.execSQL(drop_mobile_mcs_config);
			db.execSQL(drop_mobile_mcs_cfjl);
			db.execSQL(drop_mobile_mcs_cfjlmx);
			db.execSQL(drop_mobile_image);
			
			db.execSQL(mobile_patientinfo);
			db.execSQL(mobile_patbasicinfo);
			db.execSQL(mobile_doctorinfo);
			db.execSQL(mobile_deptinfo);
			db.execSQL(mobile_wardinfo);
			db.execSQL(mobile_deptwardmapinfo);
			db.execSQL(mobile_bedinfo);
			db.execSQL(mobile_orderinfo);
			db.execSQL(mobile_jsondata);
			db.execSQL(mobile_config);
			db.execSQL(mobile_patientData0);
			db.execSQL(mobile_patientData1);
			db.execSQL(mobile_patientData2);
			db.execSQL(mobile_patientData3);
			db.execSQL(mobile_patientData4);
			db.execSQL(mobile_patientData5);
			db.execSQL(mobile_patientData6);
			db.execSQL(mobile_patientData7);
			db.execSQL(mobile_patientData8);
			db.execSQL(mobile_patientData9);
			db.execSQL(mobile_patientData10);
			db.execSQL(mobile_patientData11);
			db.execSQL(mobile_patientData12);
			db.execSQL(mobile_patientData13);
			
			db.execSQL(mobile_mcs_config);
			db.execSQL(mobile_mcs_cfjl);
			db.execSQL(mobile_mcs_cfjlmx);
			db.execSQL(mobile_mcs_image);

			db.execSQL("INSERT INTO MCS_CONFIG ('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO')VALUES('SYS01',1,'系统配置',1,'数据访问',1,'电子病历使用版本 ','1','','1 webservice模式 2 前台直接访问')");
			db.execSQL("INSERT INTO MCS_CONFIG ('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO')VALUES('EMR01',2,'电子病历',1,'电子病历使用版本',1,'电子病历使用版本 ','1','','1 卫宁EMR')");
			db.execSQL("INSERT INTO MCS_CONFIG ('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO')VALUES('LIS01',3,'检验',1,'检验版本',1,'检验版本','1','','1 卫宁'");
			db.execSQL("INSERT INTO MCS_CONFIG('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO') VALUES('RIS01',4,'检查',1,'检查版本',1,'检查版本','1','','1 卫宁')");
			db.execSQL("INSERT INTO MCS_CONFIG('ID','MODULEID','MODULENAME','FUNCTYPEID','FUNCTYPENAME','FUNCID','FUNCNAME','CONFIGVALUE','EXTERNALVALUE','MEMO') VALUES('NIS01',5,'护理',1,'护理版本',1,'护理版本','1','','1 卫宁')");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
