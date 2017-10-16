package com.winning.mobileclinical.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.OfflineAction;
import com.winning.mobileclinical.activity.PatientMenu;
import com.winning.mobileclinical.db.dao.CommonJsonDao;
import com.winning.mobileclinical.dialog.DialogHelper;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.TempPatient;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.PubInterfce;
import com.winning.mobileclinical.web.SystemUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 医嘱信息
 *
 * @author liu
 * 
 */
@SuppressLint("HandlerLeak")
public class PatientList extends FragmentChild {



	private static int LOADMODE = 0;
	private static int DOWNDATA = 1;
	private static int DOWNDATASINGLE = 2;
	private static int SELECTPATIENT = 3;
	private PatientInfo patient = null;
	private static WebView webView;
	private DoctorInfo doctor = null;
	public static DeptWardMapInfo deptWardMapInfo = null;
	private ProgressDialog progressBar;
	private String syxh_temp,yexh_temp;
	private String errorMsg = "";

	private final PatientInfo patientInfo = new PatientInfo();

	@SuppressLint({ "NewApi", "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// SystemUtil.getConnect(this.getActivity());


		DisplayMetrics metric = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）

		View view = inflater.inflate(R.layout.webdialog2, container, false);

		if (GlobalCache.getCache().getDoctor() != null) {
			doctor = GlobalCache.getCache().getDoctor();
			int sel = GlobalCache.getCache().getBqSel();
			deptWardMapInfo = GlobalCache.getCache().getDeptWardMapInfos()
					.get(sel);
		}

		webView = (WebView) view.findViewById(R.id.webView);
		ViewUtil.setWebViewAttribute(webView);
		String sDataPath = getActivity().getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		webView.getSettings().setDatabasePath(sDataPath);


		// 切换有线读缓存
		// webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 直接读网络
		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

		webView.setWebChromeClient(new webChromeClient());
		webView.setWebViewClient(new MyWebViewClient());

		if (!GlobalCache.getCache().getLoadPatient()) {
			progressBar = ProgressDialog.show(getActivity(), "加载中", "请稍等");
			webView.addJavascriptInterface(new JavaScriptInterface(
					getActivity()), "App");
			webView.loadUrl(ViewUtil.url + "plist.html");
			GlobalCache.getCache().setLoadPatient(true);
		} else {

		}
		return view;
	}

	public static void loadJsData() {
		webView.loadUrl("javascript:loadData()");
	}
	
	public static void scanCode(String syxh) {
		webView.loadUrl("javascript:scanCode('" + syxh + "')");
	}
	

	public static void switchCFPatient(String syxh, String yexh) {

		webView.loadUrl("javascript:locatePatient('" + syxh + "','" + yexh
				+ "')");
	}

	public static void search(String s) {
		webView.loadUrl("javascript:search('" + s + "')");
	}

	public class JavaScriptInterface extends PubInterfce {



		private HashMap hashMap=new HashMap();


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
		public void switchPatient(int syxh, int yexh, String name, String sex,
				String age, String blh, String ryrq, String zdmc, int lcljbz) {

			Log.d("tag","----------------------------switchPatient=="+syxh);

			/*String syxh, String yexh, String name, String sex,
					String age, String cwdm, String blh,
					String zdmc, String room, int lcljbz, String ryrq*/
			
//			LOADMODE = DOWNDATA;
//			loadmessage = "正在下载离线病人数据...";
//			loadDataWithProgressDialog();
			
//			syxh_temp = "311625";
//			yexh_temp = "0";
//			LOADMODE = DOWNDATASINGLE;
//			loadDataWithProgressDialog();
			// 获取患者信息
			Object o = hashMap.get(syxh+"");
			BigDecimal s = new BigDecimal(syxh);
			BigDecimal y = new BigDecimal(yexh);
			patientInfo.setSyxh(s);
			patientInfo.setYexh(y);
			patientInfo.setName(name);
			patientInfo.setSex(sex);
			patientInfo.setAge(age);
			patientInfo.setBlh(blh);
			patientInfo.setRyrq(ryrq);
			patientInfo.setZdmc(zdmc);
			patientInfo.setLcljbz(lcljbz);
			patientInfo.setCwdm((String)o);

			GlobalCache.getCache().setPatient_selected(patientInfo);
			Intent intent = new Intent(getActivity(), PatientMenu.class);
			intent.putExtra("order","0");
			startActivityForResult(intent, 0);
		}
		
		@JavascriptInterface
		public void forwardCFJL(int syxh, int yexh, String name, String sex,
				String age, String blh, String ryrq, String zdmc, int lcljbz) {
			
//			LOADMODE = DOWNDATA;
//			loadmessage = "正在下载离线病人数据...";
//			loadDataWithProgressDialog();
			
//			syxh_temp = "311625";
//			yexh_temp = "0";
//			LOADMODE = DOWNDATASINGLE;
//			loadDataWithProgressDialog();
			
			// 获取患者信息
			BigDecimal s = new BigDecimal(syxh);
			BigDecimal y = new BigDecimal(yexh);
			patientInfo.setSyxh(s);
			patientInfo.setYexh(y);
			patientInfo.setName(name);
			patientInfo.setSex(sex);
			patientInfo.setAge(age);
			patientInfo.setBlh(blh);
			patientInfo.setRyrq(ryrq);
			patientInfo.setZdmc(zdmc);
			patientInfo.setLcljbz(lcljbz);


			GlobalCache.getCache().setPatient_selected(patientInfo);
			Intent intent = new Intent(getActivity(), PatientMenu.class);
			intent.putExtra("order","8");
			
			
			startActivityForResult(intent, 0);
		}
		@JavascriptInterface
		public Boolean downSingle(String syxh, String yexh) {
			
//			LOADMODE = DOWNDATA;
//			loadmessage = "正在下载离线病人数据...";
//			loadDataWithProgressDialog();
			
			syxh_temp = syxh;
			yexh_temp = yexh;
			LOADMODE = DOWNDATASINGLE;
			
//			loadDataWithProgressDialog();
			loadDataWithProgressBar();
			
			return true;
		}
		
		@JavascriptInterface
		public Boolean downWard(String bqdm, String ksdm) {
			
//			LOADMODE = DOWNDATA;
//			loadmessage = "正在下载离线病人数据...";
//			loadDataWithProgressDialog();
			LOADMODE = DOWNDATA;
//			loadDataWithProgressDialog();
			loadWardDataWithProgressBar();
			
			return true;
		}
		
		@JavascriptInterface
		public String getDownPaient(String bqdm, String ksdm) {
			
			List<TempPatient> temp =  new ArrayList<TempPatient>();
			
			temp = CommonJsonDao.getTempPatient(getActivity(),deptWardMapInfo.getKsdm(),deptWardMapInfo.getBqdm());
			
			return new Gson().toJson(temp);
			
		}
		
		
		@JavascriptInterface
		public void downLoad() {
			LOADMODE = DOWNDATA;
			loadDataWithProgressDialog();
		}
		
		@JavascriptInterface
		public void downSingleLoad(String syxh, String yexh) {
			syxh_temp = syxh;
			yexh_temp = yexh;
			LOADMODE = DOWNDATASINGLE;
			loadDataWithProgressDialog();
		}
		@JavascriptInterface
		@Override
		public void getBedSearch(String syxh, String yexh, String name, String sex, String age, String cwdm, String blh, String zdmc, String room, int lcljbz, String ryrq) {
			super.getBedSearch(syxh, yexh, name, sex, age, cwdm, blh, zdmc, room, lcljbz, ryrq);
			Log.d("tag","----------------------------cwdm=="+cwdm);
			Log.d("tag","----------------------------syxh=="+syxh);
			hashMap.put(syxh,cwdm);

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

			// switchfj();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if(url.indexOf("tel:")<0){
				view.loadUrl(url);
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

	@Override
	public void switchPatient() {
		// TODO Auto-generated method stub
		refleash();
	}

	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		 if(LOADMODE == DOWNDATA) {  //下载全部数据
			 
			 if(SystemUtil.isConnect(getActivity())) {
				 
				 errorMsg = OfflineAction.startload(getActivity(), doctor.getId(), null,"","", true);
			 } else {
				 errorMsg = "网络未连接无法下载";
//				 Toast.makeText(getActivity(), "网络未连接无法下载",Toast.LENGTH_LONG).show();
//				 return;
			 }
			 
			 
		 } else if(LOADMODE == DOWNDATASINGLE) { //下载单个数据
			 
			 if(SystemUtil.isConnect(getActivity())) {
				 
				 errorMsg =  OfflineAction.startload(getActivity(), doctor.getId(), null,syxh_temp,yexh_temp, false);
			 } else {
				 errorMsg = "网络未连接无法下载";
			 }
			 
			 
		 }
	}

	@Override
	protected void afterLoadData() {
		if(SystemUtil.isConnect(getActivity())) {
			if(LOADMODE == DOWNDATA || LOADMODE == DOWNDATASINGLE) {
				
				loadJsData();
				
				showDialog();
			}
		} else {
			DialogHelper.showAlert(getActivity(), errorMsg);
		}
		
	}
	
	private void showDialog() {  
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
        builder.setTitle("离线数据下载消息");  
        builder.setMessage(errorMsg);  
       /* builder.setNeutralButton("重试",
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    	loadmessage = "正在下载离线病人数据...";
            			loadDataWithProgressDialog();
                    }  
                });  */
		builder.setCancelable(false);
        builder.setNegativeButton("关闭",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    }  
                });  
       builder.show();  
    } 

	private void refleash() {
		// TODO Auto-generated method stub
		clearDate();
		if (GlobalCache.getCache().getPatient_selected() != null) {
			patient = GlobalCache.getCache().getPatient_selected();
		}
		// mode = MODE_LIST;
		loadDataWithProgressDialog();
	}

	private void clearDate() {
		// TODO Auto-generated method stub
		// emrListView.removeAllViews();
	}

	public static void importPatient() {
		// TODO Auto-generated method stub
		webView.loadUrl("javascript:focusedPatients()");
	}


}
