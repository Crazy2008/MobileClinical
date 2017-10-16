package com.winning.mobileclinical.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.utils.LogUtils;
import com.winning.mobileclinical.web.PubInterfce;

public class PublicWebView extends Activity {
    private   WebView webView=null;
    private ProgressDialog dialog;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_web_view);
       webView= (WebView) findViewById(R.id.webview);
        ImageView close= (ImageView) findViewById(R.id.close);
        ImageView imageView= (ImageView) findViewById(R.id.close);
        String url = getIntent().getStringExtra("url");
        if(url.endsWith(".jpg")){
            webView.setVisibility(View.GONE);
            Glide.with(this).load(url).into(imageView);
        }



        LogUtils.showLog("PublicWebView==============url="+url);
        settings= webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        webView.setDownloadListener(null);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);
        settings.setDisplayZoomControls(true);
        settings.setSupportZoom(true);
        //设置webView debug模式
        webView.setWebContentsDebuggingEnabled(true);
        webView.addJavascriptInterface(new JavaScriptInterface(this), "App");
//        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(url);
      /*  webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });*/

//        webView.getSettings().setUseWideViewPort(true);//关键点
       dialog =   ProgressDialog.show(this, "加载中", "请稍后");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog!=null&&dialog.isShowing()){
                    dialog.dismiss();
                }
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){


        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(webView!=null){
            webView.setVisibility(View.GONE);
            webView.destroy();
            webView=null;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(webView!=null){
            webView.onPause();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webView!=null){
            webView.onResume();
        }
    }
    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
    public  class JavaScriptInterface  extends PubInterfce {
        JavaScriptInterface(Context c) {
            super(c);
        }
        public WebView getWebViewForSub() {
            // TODO Auto-generated method stub
            return webView;
        }
    }
}
