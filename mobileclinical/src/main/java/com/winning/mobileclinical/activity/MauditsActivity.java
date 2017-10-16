package com.winning.mobileclinical.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.PubInterfce;

/**
 * @author liu
 * 
 */
@SuppressLint({ "SimpleDateFormat", "NewApi", "SetJavaScriptEnabled" })
public class MauditsActivity extends PatientInfoChild {

	private static WebView webView;
	private ProgressDialog progressBar;
	
	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maudits);

		webView = (WebView) findViewById(R.id.webView_maudits);
		ViewUtil.setWebViewAttribute(webView);
		String sDataPath = this.getApplicationContext()
				.getDir("database", Context.MODE_PRIVATE).getPath();
		webView.getSettings().setDatabasePath(sDataPath);

		// 切换有线读缓存
		// webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		// 直接读网络

		webView.setWebChromeClient(new webChromeClient());
		webView.setWebViewClient(new MyWebViewClient());

		progressBar = ProgressDialog.show(this, "加载中", "请稍等");
		backButton = (Button) findViewById(R.id.maudits_btn_back);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MauditsActivity.this.finish();
			}
		});
		webView.addJavascriptInterface(new JavaScriptInterface(
				this), "App");
//		webView.loadUrl("https://www.baidu.com/");
		webView.loadUrl(ViewUtil.url + "maudits.html");

//		if (!GlobalCache.getCache().getLoadPatient()) {
//			progressBar = ProgressDialog.show(this, "加载中", "请稍等");
//			webView.addJavascriptInterface(new JavaScriptInterface(
//					this), "App");
//			webView.loadUrl(ViewUtil.url + "maudits.html");
//			GlobalCache.getCache().setLoadPatient(true);
//		} else {
//
//		}
		
	}


	public static void loadJsData() {
		webView.loadUrl("javascript:loadData()");
	}
	

	public class JavaScriptInterface extends PubInterfce {

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

//			loadJsData();
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
		// mode = MODE_LIST;
		loadDataWithProgressDialog();
	}

	private void clearDate() {
		// TODO Auto-generated method stub
		// emrListView.removeAllViews();
	}

}
