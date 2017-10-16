package com.winning.mobileclinical.fragment;


import net.sf.json.JSONObject;

import org.w3c.dom.UserDataHandler;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.PatientAction;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.cis.DeptInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.web.SystemUtil;
import com.winning.mobileclinical.widget.LJWebView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * @author liu
 *
 */
@SuppressLint({ "HandlerLeak", "NewApi" })
public class Test extends FragmentChild {
	private static final int MODE_LIST = 0;
	private static final int MODE_PDF = 1;
	
	private int mode;
	private PatientInfo patient = null;
	
	private LJWebView webView;
	private String cookie;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		SystemUtil.getConnect(this.getActivity());
		
//		Intent intent = new Intent(HaozhiTZ.this.getActivity(), winning.doctouchclient.activitys.WebDialogActivity.class);
//		String url = "http://222.185.125.182:801/tcloud/padimageviewer.html?studyUID=1.3.6.1.4.1.19439.0.108707908.20150616092000.1267.14762932&source=miis";
//		intent.putExtra("url", url);
//		
//		startActivity(intent);
		if(GlobalCache.getCache().getPatient_selected() != null)
		{
			patient = GlobalCache.getCache().getPatient_selected();
		}
		View view = inflater.inflate(R.layout.webdialog, container, false);
		webView = (LJWebView)view.findViewById(R.id.webView);
		webView.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setJavaScriptEnabled(true);
		webView.setPluginsEnabled(true);
		webView.setSupportZoom(false);
		webView.setRenderPriority(RenderPriority.HIGH);
		webView.setBlockNetworkImage(false);
		webView.setAppCacheMaxSize(1024 * 1024 * 5);
		webView.setDatabaseEnabled(true);
		webView.setDomStorageEnabled(true);
		webView.clearView();
		webView.clearCache(true);
		// 加载本地html代码，此代码位于assets目录下，通过file:///android_asset/jsdroid.html访问。
		webView.setWebViewClient(new MyWebViewClient());
		webView.loadUrl("file:///android_asset/www/lorder.html");
		mode = MODE_LIST;
		loadDataWithProgressDialog();
		return view;
	}
	
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
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			
			return false;
			
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
		
			
		if(mode == MODE_LIST)
		{
			showPdfList();
		}
			
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
