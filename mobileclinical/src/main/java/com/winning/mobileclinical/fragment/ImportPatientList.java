package com.winning.mobileclinical.fragment;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSON;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.PatientAction;
import com.winning.mobileclinical.action.UtilsAction;
import com.winning.mobileclinical.activity.PatientMenu;
import com.winning.mobileclinical.db.dao.PatientDao;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.web.SystemUtil;
import com.winning.mobileclinical.widget.LJWebView;

import android.R.integer;
import android.R.string;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.RenderPriority;
import android.widget.Toast;

/**
 * 医嘱信息
 * 
 * @author liu
 * 
 */
@SuppressLint("HandlerLeak")
public class ImportPatientList extends FragmentChild {
	private static  int LOADMODE=0;
	private static  int SELECTPATIENT= 1;
	private PatientInfo patient = null;
	private static WebView webView;
	private DoctorInfo doctor = null;
//	private String syxh,yexh;
//	private List<DeptWardMapInfo> deptWardMapInfo = null;
	
	DeptWardMapInfo deptWardMapInfo = null;
	private ProgressDialog progressBar;
	private final PatientInfo patientInfo = new PatientInfo();

	@SuppressLint({ "NewApi", "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		SystemUtil.getConnect(this.getActivity());
		
		
		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		
		View view = inflater.inflate(R.layout.webdialog2, container, false);
		
		if(GlobalCache.getCache().getDoctor() != null)
		{
			doctor = GlobalCache.getCache().getDoctor();
			int sel = GlobalCache.getCache().getBqSel();
			deptWardMapInfo = GlobalCache.getCache().getDeptWardMapInfos().get(sel);
		}
		webView = (WebView) view.findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		String sDataPath = getActivity().getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		webView.getSettings().setDatabasePath(sDataPath);
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setSavePassword(true);
		webView.getSettings().setAllowContentAccess(true);
		webView.getSettings().setAllowFileAccess(true);
		
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setDisplayZoomControls(false);
		webView.setInitialScale(25);
		webView.getSettings().setUseWideViewPort(true);//关键点
//		if (SystemUtil.isConnect(getActivity())) { // 连接网络成功
//
//		}
		// 切换有线读缓存
		// webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 直接读网络
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		Log.i("databasepath", webView.getSettings().getDatabasePath());

		webView.setWebChromeClient(new webChromeClient());
		webView.setWebViewClient(new MyWebViewClient());

//		if (!GlobalCache.getCache().getLoadImpPatient()) {
			progressBar = ProgressDialog.show(getActivity(), "加载中", "请稍等");
			webView.addJavascriptInterface(new JavaScriptInterface1(getActivity()), "App");
			webView.loadUrl("http://www.sina.com.cn/");
//			GlobalCache.getCache().setLoadImpPatient(true);
//		} else {
//
//		}
		return view;
	}
	
	public static void loadJsData() {
//		if(GlobalCache.getCache().getDoctor() != null)
//		{
//			doctor = GlobalCache.getCache().getDoctor();
//			int sel = GlobalCache.getCache().getBqSel();
//			deptWardMapInfo = GlobalCache.getCache().getDeptWardMapInfos().get(sel);
//		}
		webView.loadUrl("javascript:loadData()");
		
	}
	
	
	public  class JavaScriptInterface1 {
		Context mContext;

	    JavaScriptInterface1(Context c) {
	        mContext = c;
	    }
	    
	    @JavascriptInterface
	    public String  getName() {
	    	return doctor.getHospital_name();
	    }
	    @JavascriptInterface
	    public String  getDoctor() {
	    	return "{\"id\":\"" + doctor.getId() + "\"}";
	    }
	    @JavascriptInterface
	    public String  getDept() {
	    	
			int sel = GlobalCache.getCache().getBqSel();
	    	deptWardMapInfo = GlobalCache.getCache().getDeptWardMapInfos().get(sel);
	    	return "{\"ksdm\":\"" + deptWardMapInfo.getKsdm() + "\",\"ksmc\":\"" + deptWardMapInfo.getKsmc() + "\",\"bqdm\":\"" + deptWardMapInfo.getBqdm() + "\",\"bqmc\":\"" + deptWardMapInfo.getBqmc() + "\"}";
	    }
	    @JavascriptInterface
	    public void  setDept(String ksdm, String bqdm) {
	    	
	    	System.out.println(ksdm + bqdm);
	    }
	    @JavascriptInterface
	    public String getData(String key) {
			return (GlobalCache.getCache().getMap().containsKey(key) ? GlobalCache.getCache().getMap().get(key) : "");
		}
	    @JavascriptInterface
	    public void setData(String key, String data) {
	    	GlobalCache.getCache().getMap().put(key, data);
		}
	    @JavascriptInterface
	    public String findData(String provider, String service, String jsonArgs) {
	    	System.out.println(provider  + service + jsonArgs);
	    	if(SystemUtil.isConnect(getActivity())) {
	    		String result = UtilsAction.getRemoteInfo(provider, service, jsonArgs);
	    		JSONObject json = null;
	    		try {
	    			json = new JSONObject(result);
	    			System.out.println(json.getString("success"));
	    			System.out.println(json.getString("data"));
	    			Gson gson = new Gson();
	    			return json.getString("data");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    	} else {
	    		List<PatientInfo> paInfos = PatientDao.getPatientList(getActivity(), deptWardMapInfo.getKsdm(), deptWardMapInfo.getBqdm());
	    		
	    		
	    		Gson gson = new Gson();
	    		return gson.toJson(paInfos);
	    	}
	    	return "";
	    }
	    
	    @JavascriptInterface
	    public void switchPatient(int syxh,int yexh,String name, String sex, String age, String blh, String ryrq, String zdmc) {
	    	//获取患者信息
			BigDecimal s=new BigDecimal(syxh);
	    	BigDecimal y=new BigDecimal(yexh); 
	    	patientInfo.setSyxh(s);
	    	patientInfo.setYexh(y);
	    	patientInfo.setName(name);
	    	patientInfo.setSex(sex);
	    	patientInfo.setAge(age);
	    	patientInfo.setBlh(blh);
	    	patientInfo.setRyrq(ryrq);
	    	patientInfo.setZdmc(zdmc);


	    	GlobalCache.getCache().setPatient_selected(patientInfo);
	    	
	    	Intent intent = new Intent(getActivity(), PatientMenu.class);
			startActivityForResult(intent, 0);
	    	
//	    	
//		    LOADMODE = SELECTPATIENT;
//		    loadDataWithNothing();	
		}





    }

	private class Callback extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return (false);
		}

	}

	private class webChromeClient extends WebChromeClient {
		@Override
		public void onExceededDatabaseQuota(String url,
				String databaseIdentifier, long currentQuota,
				long estimatedSize, long totalUsedQuota,
				WebStorage.QuotaUpdater quotaUpdater) {
			quotaUpdater.updateQuota(estimatedSize * 2);
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int flag = msg.arg1;

		}
	};

	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			view.setEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.webkit.WebViewClient#onPageFinished(android.webkit.WebView,
		 * java.lang.String)
		 */
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			if (progressBar.isShowing()) {
				progressBar.dismiss();
			}
			
			loadJsData();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return true;

		}

		@SuppressLint("NewApi")
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			return super.shouldInterceptRequest(view, url);

		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			// TODO Auto-generated method stub
			// MessageUtils.showMsgDialog(oThis, errorCode + "");
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

	}

	@Override
	public void switchPatient() {
		// TODO Auto-generated method stub
		refleash();
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
		
		
		if(LOADMODE == SELECTPATIENT) {
			Intent intent = new Intent(getActivity(), PatientMenu.class);
			startActivityForResult(intent, 0);
//			Map<String,Object> maphz = new HashMap<String,Object>(); 
//			maphz.put("syxh", syxh); 
//			maphz.put("yexh", yexh);
//		    JSONObject maptemp = new JSONObject(maphz);
//			
//			patient = PatientAction.getPatientinfo(getActivity(),"","",maptemp.toString());
		}
	}

	@Override
	protected void afterLoadData() {
		// TODO Auto-generated method stub
//		if(LOADMODE == SELECTPATIENT){
//			GlobalCache.getCache().setPatient_selected(patient);
//			Intent intent = new Intent(getActivity(), PatientMenu.class);
//			//startActivity(intent);
//			startActivityForResult(intent, 0);
//		}

	}

	private void refleash() {
		// TODO Auto-generated method stub
		clearDate();
		if (GlobalCache.getCache().getPatient_selected() != null) {
			patient = GlobalCache.getCache().getPatient_selected();
		}
//		mode = MODE_LIST;
		loadDataWithProgressDialog();
	}

	private void clearDate() {
		// TODO Auto-generated method stub
		// emrListView.removeAllViews();
	}

	private void showPdfList() {

	}
}
