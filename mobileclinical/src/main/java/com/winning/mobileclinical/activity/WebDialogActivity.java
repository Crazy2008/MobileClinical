package com.winning.mobileclinical.activity;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.widget.LJWebView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class WebDialogActivity extends Activity {

	private LJWebView webView;
	private String cookie;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webdialog);
		
		
		webView = (LJWebView)findViewById(R.id.webView);
		

		
		webView.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setJavaScriptEnabled(true);
		
		webView.setPluginsEnabled(true);
		webView.setSupportZoom(false);
		webView.setRenderPriority(RenderPriority.HIGH);
		webView.setBlockNetworkImage(false);
//		webView.setDomStorageEnabled(true);
//		String appCachePath = getDir("netCache", Context.MODE_PRIVATE).getAbsolutePath();
//		webView.setAppCacheEnabled(true);
//		webView.setAppCachePath(appCachePath);
		webView.setAppCacheMaxSize(1024 * 1024 * 5);
		webView.setDatabaseEnabled(true);
		webView.setDomStorageEnabled(true);
//		String databasePath = getDir("databases", Context.MODE_PRIVATE).getPath();
//		webView.setDatabasePath(databasePath);
		webView.clearView();
		webView.clearCache(true);
		// ���ر���html���룬�˴���λ��assetsĿ¼�£�ͨ��file:///android_asset/jsdroid.html���ʡ�
		webView.setWebViewClient(new MyWebViewClient());
//		webView.setWebChromeClient(new MyWebChromeClient());
//		webView.loadUrl("http://128.0.254.186/pem/Default.aspx?openApplyNo=1108998");
//		webView.loadUrl("http://222.185.125.182:801/tcloud/padimageviewer.html?studyUID=1.3.6.1.4.1.19439.0.108707908.20150616092000.1267.14762932&source=miis");
//		webView.setWebViewClient(new WebViewClient(){
//	           @Override
//	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//	            // TODO Auto-generated method stub
//	               //����ֵ��true��ʱ�����ȥWebView�򿪣�Ϊfalse����ϵͳ���������������
//	             view.loadUrl(url);
//	            return true;
//	        }
//	       });
		
//		webView.setWebChromeClient(new WebChromeClient());


//		Map<String, String> map = new HashMap<String, String>();
//		map.put("studyUID", "1.2.840.113704.1.111.6560.1356576188.1");
//		map.put("source", "miis");
//		
//		webView.loadUrl("http://192.168.33.216/tcloud/padimageviewer.html", map);
		
		webView.loadUrl(getIntent().getStringExtra("url"));
		
//		new LoadWebViewTask().execute();
        

		
	}
	
	private class MyWebViewClient extends WebViewClient
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.webkit.WebViewClient#onPageStarted(android.webkit.WebView,
		 * java.lang.String, android.graphics.Bitmap)
		 */
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			// TODO Auto-generated method stub
			// openProDialog("");
			view.setEnabled(true);
			super.onPageStarted(view, url, favicon);
			// System.out.println("======> onPageStarted ��ʼ����");
			// mTimer = new Timer();
			// mTimerTask = new TimerTask()
			// {
			// @Override
			// public void run()
			// {
			// // ��TIMEOUTʱ���,��ܿ��ܳ�ʱ.
			// // ��ʱ��webView���С��100,���ж��䳬ʱ
			// // �������Handle���ͳ�ʱ����Ϣ
			// System.out.println("======> mWebView.getProgress()=" +
			// mLJWebView.getProgress());
			// if (mLJWebView.getProgress() < 100)
			// {
			// Message msg = new Message();
			// msg.what = TIMEOUT_ERROR;
			// mHandler.sendMessage(msg);
			// if (mTimer != null)
			// {
			// mTimer.cancel();
			// mTimer.purge();
			// }
			// }
			// if (mLJWebView.getProgress() == 100)
			// {
			// System.out.println("======> δ��ʱ");
			// if (mTimer != null)
			// {
			// mTimer.cancel();
			// mTimer.purge();
			// }
			// }
			// }
			// };
			// mTimer.schedule(mTimerTask, TIMEOUT, 1);
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
			// �ǳ�����(��Ajax)�����޷�ֱ���������ͷ����ƴ�ӵ�urlĩβ,����ƴ��һ��imei��Ϊʾ��

//			String ajaxUrl = url;
//			// ���ʶ:req=ajax
//			if (url.contains("req=ajax"))
//			{
//
//			}
			return super.shouldInterceptRequest(view, url);

		}

		/*
		 * (�� Javadoc)
		 * 
		 * <p>Title: onReceivedError</p>
		 * 
		 * <p>Description: </p>
		 * 
		 * @param view
		 * 
		 * @param errorCode
		 * 
		 * @param description
		 * 
		 * @param failingUrl
		 * 
		 * @see android.webkit.WebViewClient#onReceivedError(android.webkit.WebView,
		 * int, java.lang.String, java.lang.String)
		 */
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
			// TODO Auto-generated method stub
			// MessageUtils.showMsgDialog(oThis, errorCode + "");
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

	}
	
	private class MyWebChromeClient extends WebChromeClient
	{

		@Override
		public void onReceivedTitle(WebView view, String title)
		{
			super.onReceivedTitle(view, title);

		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	
}
