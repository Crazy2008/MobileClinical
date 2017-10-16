package com.winning.mobileclinical.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.UtilsAction;
import com.winning.mobileclinical.activity.PublicWebView;
import com.winning.mobileclinical.db.dao.BfDao;
import com.winning.mobileclinical.db.dao.CommonJsonDao;
import com.winning.mobileclinical.db.dao.ImageDao;
import com.winning.mobileclinical.db.dao.SysConfigDao;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.NameValue;
import com.winning.mobileclinical.model.cis.Bedinfo;
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.FileInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.model.cis.TableClass;
import com.winning.mobileclinical.utils.LogUtils;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.PubInterfce;
import com.winning.mobileclinical.web.SystemUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 医技报告
 * 
 * @author liu
 * 
 */
@SuppressLint("NewApi")
public class MedicalReportsRIS extends FragmentChild {
	private static final int MODE_LIST = 0;
	private static final int MODE_PDF = 1;
	private int mode;
	private PatientInfo patient = null;
	private static WebView webView = null;
	private WebView webView1 = null;
	private WebView webView2 = null;
	private WebView webView3 = null;
	private WebView webView4 = null;
	private String cookie;
	private DoctorInfo doctor = null;
	private List<DeptWardMapInfo> bqlist = null; //病区科室list
	
	DeptWardMapInfo deptWardMapInfo = null;
	Object object2;

	private ProgressDialog progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.webdialog2, container, false);
		if(GlobalCache.getCache().getPatient_selected() != null) {
			doctor = GlobalCache.getCache().getDoctor();
			patient = GlobalCache.getCache().getPatient_selected();
			bqlist = GlobalCache.getCache().getDeptWardMapInfos();
			deptWardMapInfo = bqlist.get(GlobalCache.getCache().getBqSel());
		}
		
		doctor = GlobalCache.getCache().getDoctor();
		webView = (WebView) view.findViewById(R.id.webView);
		String sDataPath = getActivity().getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		webView.getSettings().setDatabasePath(sDataPath);

		//设置webView公共属性
		ViewUtil.setWebViewAttribute(webView);

		webView.setWebChromeClient(new webChromeClient());
		webView.setWebViewClient(new MyWebViewClient());
		if (!GlobalCache.getCache().getLoadRIS()) {
			progressBar = ProgressDialog.show(getActivity(), "加载中", "请稍等");
			webView.addJavascriptInterface(new JavaScriptInterface(getActivity()), "App");
			progressBar.setCanceledOnTouchOutside(true);
			webView.loadUrl(ViewUtil.url + "ris.html");
//			GlobalCache.getCache().setLoadRIS(true);
		} else {

		}
		return view;
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


	private class MyWebViewClient extends WebViewClient {
		private  boolean isDone = false;
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

			if (!this.isDone) loadJsData();
			this.isDone = true;

		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			LogUtils.showLog("MeRis======url="+url);
			if(url.contains("tel:")){
				webView.loadUrl(url);
			}
			//检查tab过滤条件，
			if(url.startsWith("http") && url.contains("PacsEnterpriseManager/LoadImage.aspx")){
				Intent intent = new Intent(getActivity(), PublicWebView.class);
				String[] args = url.substring(url.indexOf("?") + 1).split("&");
				String repno = "";
				for (int i = 0; i < args.length; i++){
					if (!args[i].startsWith("ApplyNo")) continue;
					repno = args[i].substring(8);
					break;
				}

				intent.putExtra("url",ViewUtil.url + "pacs.html#repno=" + repno);
				startActivity(intent);
			}
			else if(url.startsWith("http://mcs/")){
				Intent intent = new Intent(getActivity(), PublicWebView.class);
				intent.putExtra("url", ViewUtil.url.concat(url.substring(11)));
				startActivity(intent);
			}
			else if(url.startsWith("http")&&!url.contains("nohandlerurl")){
				Intent intent = new Intent(getActivity(), PublicWebView.class);
				intent.putExtra("url",url);
				startActivity(intent);
			}

			else{
				webView.loadUrl(url);
			}

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

	public  class JavaScriptInterface  extends PubInterfce {
		JavaScriptInterface(Context c) {
			super(c);
		}
		@Override
		public WebView getWebViewForSub() {
			// TODO Auto-generated method stub
			return webView;
		}
		@Override
		@JavascriptInterface
	    public String findData(String provider, String service, String jsonArgs) throws JSONException {
			LogUtils.showLog("findData");
	    	System.out.println(provider  + service + jsonArgs);
	    	if(SystemUtil.isConnect(getActivity())) {
	    		String result = UtilsAction.getRemoteInfo(provider, service, jsonArgs);
	    		JSONObject json = null;
	    		try {
	    			json = new JSONObject(result);
	    			
	    			if(json.getString("success").equals("false")) {
	    				alert(json.getString("message"));
	    			}
	    			
	    			return json.getString("data");
	    		} catch (JSONException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    	} else {
	    		
	    		if((service =="ward-bed"|| service.equals("ward-bed")) && (provider =="dept"|| provider.equals("dept"))) {
	    			List<Bedinfo> list = null;
	    			Gson gson = new Gson();
	    			list = BfDao.getJSONDATA(getActivity(), deptWardMapInfo.getBqdm());
	    			
	    			int single = SystemUtil.getJsonLB(provider, service);
	    			
	    			if(single == 0) {
	    				System.out.println(gson.toJson(list));
	    				return gson.toJson(list);
	    			} else {
	    				return gson.toJson(list.get(0));
	    			}
	    		} else if((service =="all-inpaitient"|| service.equals("all-inpaitient")) && (provider =="patient"|| provider.equals("patient"))) {
	    			TableClass  emr_table = SysConfigDao.getValue(getActivity(),provider,service);
	        		Gson gson = new Gson();
	        		List<CommonJson> list = null;
	        		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject
	    					.fromObject(jsonArgs);
	    			StringBuffer sb = new StringBuffer();
	    			List<NameValue> sqlList = new ArrayList<NameValue>();
	    			for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) {
	    				NameValue nameValue = new NameValue();
	    				String key = (String) iter.next();
	    				nameValue.setName(key);
	    				nameValue.setValue(jsonObject.get(key).toString());
	    				sqlList.add(nameValue);
	    			}
	    			if(sqlList.size()> 0) {
	    				for(int i=0;i<sqlList.size();i++) {
	    					if(i == (sqlList.size()-1)) {
	    						sb.append(sqlList.get(i).getName().toUpperCase());
	    						sb.append("='"+sqlList.get(i).getValue() + "'");
	    					} else {
	    						sb.append(sqlList.get(i).getName().toUpperCase());
	    						sb.append("='"+sqlList.get(i).getValue() + "'  and  ");
	    					}
	    				}
	    			}
//	    			String sql = " SYXH=" +json.getString("syxh")+ " and " + " YEXH=" +  json.getString("yexh");
	    			
	        		
	    			System.out.println(provider + service + emr_table.getClassName() + emr_table.getType());
	        		
	        		list = CommonJsonDao.getJSONDATA(getActivity(), emr_table,sb.toString());
	        		int single = SystemUtil.getJsonLB(provider, service);
	    			
	    			if(single == 0) {
	    				System.out.println(gson.toJson(list));
	    				return gson.toJson(list);
	    			} else {
	    				return gson.toJson(list.get(0));
	    			}
	        		
	    		} else if((service =="patient-img"|| service.equals("patient-img")) && (provider =="ris"|| provider.equals("ris"))) {
	    			

	    			List<FileInfo> list = null;
	    			list = ImageDao.getFileInfos(getActivity(), patient.getSyxh()+"",patient.getYexh()+"");
	    			Gson gson = new Gson();
	    		System.out.println("文件路径"+gson.toJson(list));	
	    			if(list != null && list.size() > 0) {
	    				
	    				return gson.toJson(list);
	    			} 
	    		
	    			
	    		} else {
	    			TableClass  emr_table = SysConfigDao.getValue(getActivity(),provider,service);
	        		Gson gson = new Gson();
	        		List<CommonJson> list = null;
	        		
	        		
	        		JSONObject json = null;
	    			try {
	    				json = new JSONObject(jsonArgs);
	    			} catch (JSONException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			System.out.println(json.getString("syxh"));
	    			System.out.println(json.getString("yexh"));
	        		
//	        		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject
//	    					.fromObject(jsonArgs);
//	    			StringBuffer sb = new StringBuffer();
//	    			List<NameValue> sqlList = new ArrayList<NameValue>();
//	    			for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) {
//	    				NameValue nameValue = new NameValue();
//	    				String key = (String) iter.next();
//	    				nameValue.setName(key);
//	    				nameValue.setValue(jsonObject.get(key).toString());
//	    				sqlList.add(nameValue);
//	    			}
//	    			if(sqlList.size()> 0) {
//	    				for(int i=0;i<sqlList.size();i++) {
//	    					if(i == (sqlList.size()-1)) {
//	    						sb.append(sqlList.get(i).getName().toUpperCase());
//	    						sb.append("='"+sqlList.get(i).getValue() + "'");
//	    					} else {
//	    						sb.append(sqlList.get(i).getName().toUpperCase());
//	    						sb.append("='"+sqlList.get(i).getValue() + "'  and  ");
//	    					}
//	    				}
//	    			}
	    			String sql = " SYXH=" +json.getString("syxh")+ " and " + " YEXH=" +  json.getString("yexh");
	    			
	        		
	    			System.out.println(provider + service + emr_table.getClassName() + emr_table.getType());
	        		
	        		list = CommonJsonDao.getJSONDATA(getActivity(), emr_table,sql);
	        		
	        		int single = SystemUtil.getJsonLB(provider, service);
	    			
	    			if(single == 0) {
	    				System.out.println(gson.toJson(list));
	    				return gson.toJson(list);
	    			} else {
	    				return gson.toJson(list.get(0));
	    			}
	        		
	    		}
	    		
	    		
	    	}
	    	return "";
	    }
		
	}

	public static void loadJsData() {
		LogUtils.showLog("loadJsData");
		webView.loadUrl("javascript:loadData()");
	}
	
	public static void loadPatRISData(String syxh,String yexh) {
		LogUtils.showLog("loadPatRISData");
		webView.loadUrl("javascript:loadData('" + syxh + "','" + yexh
				+ "')");
	}

	@Override
	public void switchPatient() {
		// TODO Auto-generated method stub
		refleash();
	}

	@Override
	protected void loadDate() {
		
	}

	@Override
	protected void afterLoadData() {
		// TODO Auto-generated method stub

	}

	private void refleash() {
		// TODO Auto-generated method stub
		clearDate();
		if (GlobalCache.getCache().getPatient_selected() != null) {
			patient = GlobalCache.getCache().getPatient_selected();
		}
		mode = MODE_LIST;
		loadDataWithProgressDialog();
	}

	private void clearDate() {
		// TODO Auto-generated method stub
		// emrListView.removeAllViews();
	}

	@Override
	public void onPause() {
		super.onPause();
		if(webView!=null){
			webView.onPause();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(webView!=null){
			webView.onResume();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(webView!=null){
			webView.destroy();
			webView=null;
		}
	}

}
