package com.winning.mobileclinical.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public abstract class FragmentChild extends Fragment implements SwitchPatientListener{

	public ProgressDialog proDialog;

	public static ProgressDialog progressDialog;

	public int progressBarStatus = 0;

	public int fileSize = 0;

	public String flag = "";

	public Handler progressBarHandler;

	public String message="提交成功";

	public String loadmessage = "正在获取数据...";

	public abstract void switchPatient();

	protected void afterLoadData() {

	}

	protected void loadDate() {

	}

	protected void afterSubmit() {

	}

	protected void submitDate() {

	}

	protected void updateProgress(){

	}

	public void receiveBlueTooth(String tm) {

	}
	protected void loadDataWithNothing() {
		LoadHandlerNothing t = new LoadHandlerNothing();
		t.start();
	}

	protected void loadDataWithProgressBar() {
		progressDialog = new ProgressDialog(this.getActivity());
		progressDialog.setTitle("下载");
		progressDialog.setMessage(loadmessage);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setProgress(0);
		progressDialog.setMax(100);
		progressDialog.setCancelable(true);//设置进度条是否可以按退回键取消
//		progressDialog.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
		progressDialog.show();

		BarLoadHandler t = new BarLoadHandler();
		t.start();

	}

	protected void loadWardDataWithProgressBar() {
		progressDialog = new ProgressDialog(this.getActivity());
		progressDialog.setTitle("下载");
		progressDialog.setMessage(loadmessage);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setProgress(0);
		progressDialog.setMax(100);
		progressDialog.setCancelable(true);//设置进度条是否可以按退回键取消
//		progressDialog.setCanceledOnTouchOutside(false);//设置点击进度对话框外的区域对话框不消失
		progressDialog.show();

		WardBarLoadHandler t = new WardBarLoadHandler();
		t.start();

	}

	protected void loadDataWithProgressDialog() {
		proDialog = ProgressDialog.show(this.getActivity(), "", loadmessage, true,
				true);

		proDialog.setCanceledOnTouchOutside(false);

		LoadHandler t = new LoadHandler();
		t.start();
	}

	protected void loadDataWithProgressDialog(Context context) {
		proDialog = ProgressDialog.show(context, "", loadmessage, true,
				true);
		LoadHandler t = new LoadHandler();
		t.start();
	}

	protected void submitWithProgressDialog() {
		proDialog = ProgressDialog.show(this.getActivity(), "", "正在传输数据...", true,
				true);
		SubmitHandler t = new SubmitHandler();
		t.start();
	}
	private class LoadHandlerNothing extends Thread {
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

	private class WardBarLoadHandler extends Thread {
		@Override
		public void run() {
			try {

//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(20);
//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(40);
//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(60);

//				while(GlobalCache.getCache().getProgress()<100){
//                  	Thread.sleep(1000);
//                 	updateProgress.sendEmptyMessage(GlobalCache.getCache().getProgress());
//				}

				loadDate();

//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(80);
//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(100);

				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("type", 0);
				message.setData(bundle);
				UIHandler.sendMessage(message);
//     			   progressDialog.cancel();
//                 progressDialog.dismiss();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				progressDialog.cancel();
				progressDialog.dismiss();
			}
		}
	}

	private class BarLoadHandler extends Thread {
		@Override
		public void run() {
			try {

//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(20);
//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(40);
//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(60);
//            	 while(GlobalCache.getCache().getProgress()<100){
//                  	Thread.sleep(1000);
//                 	updateProgress.sendEmptyMessage(GlobalCache.getCache().getProgress());
//              	 }

				loadDate();

//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(80);
//				Thread.sleep(500);
//				updateProgress.sendEmptyMessage(100);

				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putInt("type", 0);
				message.setData(bundle);
				UIHandler.sendMessage(message);
//     			   progressDialog.cancel();  
//                 progressDialog.dismiss();  
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				progressDialog.cancel();
				progressDialog.dismiss();
			}
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

	public static final Handler updateWardProgress = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what >= 100) {
				progressDialog.cancel();
				progressDialog.dismiss();
			}


			progressDialog.setProgress(msg.what);

			super.handleMessage(msg);
		}
	};

	public static final Handler updateProgress = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what >= 100) {
				progressDialog.cancel();
				progressDialog.dismiss();
			}


			progressDialog.setProgress(msg.what);

			super.handleMessage(msg);
		}
	};

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
	public Context getContext() {
		// TODO Auto-generated method stub
		return this.getActivity();
	}

	// file download simulator... a really simple
	public int doSomeTasks() {

		while (fileSize <= 1000000) {
			fileSize += 100000;

			if (fileSize == 100000) {
				return 10;
			} else if (fileSize == 200000) {
				return 20;
			} else if (fileSize == 300000) {
				return 30;
			}
			// ...add your own
		}

		return 100;

	}
}
