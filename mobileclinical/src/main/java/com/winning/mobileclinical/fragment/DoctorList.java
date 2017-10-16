package com.winning.mobileclinical.fragment;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.PubInterfce;

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
import android.webkit.WebSettings.RenderPriority;
import android.widget.Toast;

/**
 * 我的
 * 
 * @author liu
 * 
 */
@SuppressLint("HandlerLeak")
public class DoctorList extends FragmentChild {
	private static  int LOADMODE=0;
	private static  int SELECTPATIENT= 1;
	private PatientInfo patient = null;
	private static WebView webView;
	private DoctorInfo doctor = null;
//	private String syxh,yexh;
//	private List<DeptWardMapInfo> deptWardMapInfo = null;
	
	DeptWardMapInfo deptWardMapInfo = null;
	private ProgressDialog progressBar;

	@SuppressLint({ "NewApi", "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.webviewpub, container, false);
		doctor = GlobalCache.getCache().getDoctor();
		webView = (WebView) view.findViewById(R.id.webView);
		ViewUtil.setWebViewAttribute(webView);
		String sDataPath = getActivity().getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		
		webView.getSettings().setDatabasePath(sDataPath);

		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		

		
		webView.setWebChromeClient(new webChromeClient());
		webView.setWebViewClient(new MyWebViewClient());
		
		if (!GlobalCache.getCache().getLoadEMR()) {
			progressBar = ProgressDialog.show(getActivity(), "加载中", "请稍等");
			webView.addJavascriptInterface(new JavaScriptInterface(getActivity()), "App");
			progressBar.setCanceledOnTouchOutside(true);
			webView.loadUrl("http://www.qq.com/");
			GlobalCache.getCache().setLoadEMR(true);
		} else {

		}

		return view;
	}
	
	public static void loadJsData() {
		webView.loadUrl("javascript:loadData()");
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
		
	}

	private void refleash() {
		// TODO Auto-generated method stub
		clearDate();
		if (GlobalCache.getCache().getPatient_selected() != null) {
			patient = GlobalCache.getCache().getPatient_selected();
		}
		loadDataWithProgressDialog();
	}

	private void clearDate() {
	}

}
