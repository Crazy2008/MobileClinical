package com.winning.mobileclinical.fragment;

import java.util.ArrayList;
import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.PatientAction;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.cis.DeptInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.web.SystemUtil;
import com.winning.mobileclinical.widget.LJWebView;

import net.sf.json.util.NewBeanInstanceStrategy;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
/**
 * 医技报告
 * @author liu
 *
 */
@SuppressLint("NewApi")
public class Test2 extends FragmentChild{
	private static final int MODE_LIST = 0;
	private static final int MODE_PDF = 1;
	
	private int mode;
	private PatientInfo patient = null;
	
	private WebView webView = null;
	private WebView webView1 = null;
	private WebView webView2 = null;
	private WebView webView3 = null;
	private WebView webView4 = null;
	private String cookie;
	private DoctorInfo doctor = null;
	Object object2;
	
	private ProgressDialog progressBar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SystemUtil.getConnect(this.getActivity());
		
//		Intent intent = new Intent(HaozhiTZ.this.getActivity(), winning.doctouchclient.activitys.WebDialogActivity.class);
//		String url = "http://222.185.125.182:801/tcloud/padimageviewer.html?studyUID=1.3.6.1.4.1.19439.0.108707908.20150616092000.1267.14762932&source=miis";
//		intent.putExtra("url", url);
//		startActivity(intent);
//		if(GlobalCache.getCache().getPatient_selected() != null)
//		{
//			patient = GlobalCache.getCache().getPatient_selected();
//		}
		
		View view = inflater.inflate(R.layout.webdialog1, container, false);
		doctor = GlobalCache.getCache().getDoctor();
		webView = (WebView)view.findViewById(R.id.webView);
		webView1 = (WebView)view.findViewById(R.id.webViewris);
		webView2 = (WebView)view.findViewById(R.id.webViewlis);
		webView3 = (WebView)view.findViewById(R.id.webViewemr);
		webView4 = (WebView)view.findViewById(R.id.webViewtemp);
		
		webView.setVisibility(View.GONE);
	    webView1.setVisibility(view.VISIBLE);
	    webView2.setVisibility(view.GONE);
	    webView3.setVisibility(view.GONE);
	    webView4.setVisibility(view.GONE);
		
		
	    webView1.getSettings().setJavaScriptEnabled(true);
		webView1.getSettings().setDatabaseEnabled(true);
		webView1.getSettings().setDomStorageEnabled(true);
	    //String sDataPath = "/data/data/" + webView.getContext().getPackageName() + "/databases/";
	    String sDataPath = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
	    System.out.println("sDataPath"+sDataPath);
	    
	    webView1.getSettings().setDatabasePath(sDataPath);
	    webView1.getSettings().setDatabaseEnabled(true);
	    webView1.getSettings().setSavePassword(true);
	    webView1.getSettings().setAllowContentAccess(true);
	    webView1.getSettings().setAllowFileAccess(true);

	    webView1.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	    
	    webView1.setWebChromeClient(new webChromeClient());
	    webView1.setWebViewClient(new MyWebViewClient());
	    
	    
	    final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//		 
       progressBar = ProgressDialog.show(getActivity(), "加载中", "请稍等");
		
		webView1.loadUrl("file:///android_asset/www/ris.html");
			
		
//		mode = MODE_LIST;
//		loadDataWithProgressDialog();
		return view;
	}
	
	private class Callback extends WebViewClient{  

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

 }
	
	private class webChromeClient extends WebChromeClient
	 {
	     @Override
	        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) { 
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
	private String resultString;
	
	private class MyWebViewClient extends WebViewClient
	{

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			view.setEnabled(true);
			super.onPageStarted(view, url, favicon);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.webkit.WebViewClient#onPageFinished(android.webkit.WebView,
		 * java.lang.String)
		 */
		@Override
		public void onPageFinished(WebView view, String url)
		{
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
			
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			
			view.loadUrl(url);     
            return true; 
			
		}

		@SuppressLint("NewApi")
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url)
		{
			return super.shouldInterceptRequest(view, url);

		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
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
		
	}
	@Override
	protected void afterLoadData() {
		// TODO Auto-generated method stub
		
			
	}
	
	

	private void refleash() {
		// TODO Auto-generated method stub
		clearDate();
		if(GlobalCache.getCache().getPatient_selected() != null)
		{
			patient = GlobalCache.getCache().getPatient_selected();
		}
		mode = MODE_LIST;
		loadDataWithProgressDialog();
	}

	private void clearDate() {
		// TODO Auto-generated method stub
	//	emrListView.removeAllViews();
	}
	private void showPdfList() {
		
	}
}
