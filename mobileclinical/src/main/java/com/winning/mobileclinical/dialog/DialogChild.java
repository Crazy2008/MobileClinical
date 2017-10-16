package com.winning.mobileclinical.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class DialogChild extends Dialog{
	protected Context context = null;
	public DialogChild(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}
	public DialogChild(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}
	public ProgressDialog proDialog;
	
	public String message="提交成功";

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
		NothingLoadHandler t = new NothingLoadHandler();
		t.start();
	}
	protected void loadDataWithProgressDialog() {
		proDialog = ProgressDialog.show(this.context, "", "正在获取数据...", true,
				true);
		LoadHandler t = new LoadHandler();
		t.start();
	}
	
	protected void submitWithProgressDialog() {
		proDialog = ProgressDialog.show(this.context, "", "正在传输数据...", true,
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
	private class NothingLoadHandler extends Thread {
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
				Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
				break;
			}
			default: {

			}
			}
		}
	};

	protected OnBtnClickListener onBtnClickListener = null;
	public interface OnBtnClickListener{
		public void OnBtnClick(int cnt);
	}
	public void SetOnBtnClickListener(OnBtnClickListener l){
		this.onBtnClickListener  = l;
	}
}
