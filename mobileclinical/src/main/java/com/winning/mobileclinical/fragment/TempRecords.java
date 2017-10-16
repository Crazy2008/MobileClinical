package com.winning.mobileclinical.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.PubInterfce;

/**
 * 体温单
 * @author liu
 * 
 */
public class TempRecords extends FragmentChild {
	private static final int MODE_LIST = 0;
	private static final int MODE_PDF = 1;
	private int mode;
	private PatientInfo patient = null;
	private static WebView webView = null;
	private DoctorInfo doctor = null;
	private ProgressDialog progressBar;

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.webdialog2, container, false);
		doctor = GlobalCache.getCache().getDoctor();
		webView = (WebView) view.findViewById(R.id.webView);
		ViewUtil.setWebViewAttribute(webView);
		String sDataPath = getActivity().getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		webView.getSettings().setDatabasePath(sDataPath);
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		webView.setWebChromeClient(new webChromeClient());
		webView.setWebViewClient(new MyWebViewClient());
		if (!GlobalCache.getCache().getLoadTchart()) {
			progressBar = ProgressDialog.show(getActivity(), "加载中", "请稍等");
			progressBar.setCanceledOnTouchOutside(true);
			webView.addJavascriptInterface(new JavaScriptInterface(getActivity()), "App");
			webView.loadUrl(ViewUtil.url + "tchart.html");
//			GlobalCache.getCache().setLoadTchart(true);
		} else {
		}
		return view;
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
	
	public static void loadJsData() {
		webView.loadUrl("javascript:loadData()");
	}
	
	public static void loadPatTempData(String syxh,String yexh) {
		webView.loadUrl("javascript:loadData('" + syxh + "','" + yexh
				+ "')");
	}
	
	private class MyWebViewClient extends WebViewClient {
			private boolean  isDone=false;
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
}
