package com.winning.mobileclinical.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.winning.mobileclinical.R;

/**
 * @author liu
 * 
 */
@SuppressLint({ "SimpleDateFormat", "NewApi", "SetJavaScriptEnabled" })
public class VersionActivity extends PatientInfoChild {

	private static WebView webView;
	private ProgressDialog progressBar;
	
	
	private TextView textView;
	private Button backButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.version);
		
		backButton = (Button) findViewById(R.id.version_btn_back);
		
		
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				VersionActivity.this.finish();
			}
		});
		
		textView = (TextView) findViewById(R.id.version_text);
		textView.setText(getVersionName(this) + "版本");
		
	}
	
	private String getVersionName(Context context)
	{
		String versionName = "";
		try
		{
			versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionName;
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
