package com.winning.mobileclinical.activity;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.dialog.DialogHelper;
import com.winning.mobileclinical.dialog.DialogHelper.dialogListener;
import com.winning.mobileclinical.utils.LogUtils;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

public abstract class PatientInfoChild extends FragmentActivity{

	public ProgressDialog proDialog;

	public abstract void switchPatient();

	protected void afterLoadData() {

	}

	protected void loadDate() {

	}

	protected void afterSubmit() {

	}

	protected void submitDate() {

	}

	public void receiveBlueTooth(String tm) {

	}
	protected void loadDataWithNothing() {
		LoadNoHandler t = new LoadNoHandler();
		t.start();
	}

	protected void loadDataWithProgressDialog() {
		proDialog = ProgressDialog.show(this, "", "正在获取数据...", true,
				true);
		LoadHandler t = new LoadHandler();
		t.start();
	}
	
	protected void loadDataWithNoDialog() {
		LoadNoHandler t = new LoadNoHandler();
		t.start();
	}
	
	
	protected void loadDataWithProgressDialog(Context context) {
		proDialog = ProgressDialog.show(context, "", "正在获取数据...", true,
				true);
		LoadHandler t = new LoadHandler();
		t.start();
	}
	protected void submitWithProgressDialog() {
		proDialog = ProgressDialog.show(this, "", "正在传输数据...", true,
				true);
		SubmitHandler t = new SubmitHandler();
		t.start();
	}

	private class LoadHandler extends Thread {
		@Override
		public void run() {
			loadDate();
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt("type", 0);
			message.setData(bundle);
			UIHandler.sendMessage(message);
			proDialog.dismiss();
		}
	}
	private class LoadNoHandler extends Thread {
		@Override
		public void run() {
			loadDate();
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt("type", 0);
			message.setData(bundle);
			UIHandler.sendMessage(message);
		}
	}
	private class SubmitHandler extends Thread {
		@Override
		public void run() {
			submitDate();
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putInt("type", 1);
			message.setData(bundle);
			UIHandler.sendMessage(message);
			proDialog.dismiss();
		}
	}

	public Handler UIHandler = new Handler() {
		public void handleMessage(Message msg) {
			int type = msg.getData().getInt("type");
			switch (type) {
			case 0: {
				afterLoadData();
				break;
			}
			case 1: {
				afterSubmit();
				Toast.makeText(getContext(),"提交成功",Toast.LENGTH_SHORT).show();
				break;
			}
			default: {

			}
			}
		}
	};
	private Context getContext() {
		// TODO Auto-generated method stub
		return this.getApplicationContext();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
//		super.onBackPressed();
//		LogUtils.showLog("获取当前activity+++++++"+getRunningActivityName());
		if(getRunningActivityName().contains("PatientBrowse")){
			exit();
		}
	}
	
	public void exit(){
		DialogHelper.showAlert(this, "退出", "确定要退出移动查房系统？", R.drawable.patient, 
			new dialogListener() {
				@Override
				public void onOk(Dialog dialog) {
					setResult(RESULT_OK);
					finish();
					dialog.dismiss();
				}

				@Override
				public void oncancel(Dialog dialog) {
					
				}
			}
		);
	}

	private String getRunningActivityName() {
//		String contextString = getContext().toString();
//		return contextString.substring(contextString.lastIndexOf(".") + 1, contextString.indexOf("@"));
		ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		return cn.getClassName();
	}
}
