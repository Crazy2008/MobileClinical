package com.winning.mobileclinical.web;

public class WebUtils {
//
	public static   String HOST = "http://192.168.137.1:8787";
	public  static  String WEBSERVICE =HOST+"/data.asmx";


//
//	public    String CFJLUploadUrl = WEBSERVICE+"/upload.ashx?name=dailyrecord";
////	查房便签下载的url
//	public    String CFJLDownloadUrl = WEBSERVICE+"/download.ashx?name=dailyrecord";
//
//            //涂鸦图片的上传的url
//	public    String EditBookMarkUrl=WEBSERVICE+"/upload.ashx?name=emr";
//            //涂鸦图片的下载的url
//	public    String EditBookMarkDownloadUrl=WEBSERVICE+"/download.ashx?name=emr";
	
	public static  String EMRconfig = "http://192.168.10.146:8080/EMRPDF";
	public static  String NISconfig = "http://192.168.10.146:8080/EMRPDF";
	public static  String PACSIMAGE = "http://192.168.33.100/wado/default.aspx";
	public static final String URLUPDATE ="/down/version.xml";

	//访问服务
	public static final String LOGINACTION = "/MobileServer/control?action=login";					//登录
	public static final String BWL ="/MobileServer/control?action=bwlservlet";
	public static final String UPLOAD ="/MobileServer/control?action=multiUploadServlet";
	public static final String MENUACTION ="/MobileServer/control?action=getMenuList";  //获取导航栏项目
	public static final String PATIENTLIST ="/MobileServer/control?action=getPatientList";  //
	public static final String ORDERMESSAGE ="/MobileServer/control?action=getOrderList";  //



//	 private WebUtils(){}
//	 private static WebUtils webUtils;
//	 public static  synchronized  WebUtils init() {
//		 if (webUtils == null) {
//			 webUtils = new WebUtils();
//		 }
//		 return webUtils;
//	 }
}


