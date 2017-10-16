package com.winning.mobileclinical.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ViewUtil {

	
	public static String url ="file:///android_asset/www/";
//	public static String url ="http://192.168.1.115:9797/";
//	public static String url ="http://192.168.0.1:9797/";

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int diptopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int pxtodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	/**
	 * 获取width px
	 */
	public static int getWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}
	
	/**
	 * 获取width px
	 */
	public static int getHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	
	/*
	 * 获取densityDpi
	 */
	public static int getDm(Context context) {
		return context.getResources().getDisplayMetrics().densityDpi;
	}
	
	
	public static Typeface gettypeface(Context context)
	{
		AssetManager mgr = context.getAssets();//得到AssetManager
		Typeface tf=Typeface.createFromAsset(mgr, "fonts/Roboto-Thin.ttf");//根据路径得到Typeface
		return tf;
	}
	 /**
	  * 将px值转换为sp值，保证文字大小不变
	  * 
	  * @param pxValue
	  * @param
	  * @return
	  */
	 public static int px2sp(Context context,float pxValue) {
		 final float fontScale = context.getResources().getDisplayMetrics().density;
		 return (int) (pxValue / fontScale + 0.5f);
	 }

	 /**
	  * 将sp值转换为px值，保证文字大小不变
	  * 
	  * @param spValue
	  * @param
	  * @return
	  */
	 public static int sp2px(Context context,float spValue) {
		 final float fontScale = context.getResources().getDisplayMetrics().density;
		 return (int) (spValue * fontScale + 0.5f);
	 }
	public static void setWebViewAttribute(WebView webView){
		WebView.setWebContentsDebuggingEnabled(true);
		WebSettings webSettings = webView.getSettings();

		webSettings.setJavaScriptEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setSavePassword(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAllowContentAccess(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setSupportZoom(false);
		webSettings.setDisplayZoomControls(false);
		webView.setInitialScale(25);
		webSettings.setUseWideViewPort(true);//关键点
	}
	/**
	 * 根据图片的url路径获得Bitmap对象
	 * @param url
	 * @return
	 */
	public static Bitmap getBitmapForNet(String url) {
		URL fileUrl = null;
		Bitmap bitmap = null;

		try {
			fileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			HttpURLConnection conn = (HttpURLConnection) fileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();


			BitmapFactory.Options options = new BitmapFactory.Options();

			options.inMutable=true;
			bitmap = BitmapFactory.decodeStream(is,null,options);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;

	}
}
