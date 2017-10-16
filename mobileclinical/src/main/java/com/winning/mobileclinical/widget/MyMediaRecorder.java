package com.winning.mobileclinical.widget;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.winning.mobileclinical.R;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MyMediaRecorder extends LinearLayout{
	private boolean isRecording = false;
	private MediaRecorder mRecorder;
	private String path;
	private MediaRecorderListener listener;
	private Runnable runnable;
	private  int voiceLength;
	Boolean bool = false;
	Boolean recording = false;
	Boolean isStopRecord= false;
	RelativeLayout relativeLayout;
	
	private ImageView iv=null;

	public MyMediaRecorder(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}
	
	public MyMediaRecorder(Context context){
		super(context);
		this.init(context);
	}
	
	private void init(Context context){
		LinearLayout layout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.audio_dn_recording, null);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		this.addView(layout);
		
		iv = (ImageView) layout.findViewById(R.id.dn_recording_img);
		
		
		iv.setOnClickListener(touchListener);
		
	}
	
	OnClickListener touchListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			stop();
		}
		
	};
	
	//开始录音
	public void start(){
		if(path==null)
			return;
		bool = true;
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		// 设置录音格式
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(path);
		// 设置编码
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			Log.e("BWLEditNew_Record", "prepare() failed");
		}
		mRecorder.start();
		isRecording = true;
		
		if(listener!=null)
			voiceLength = 0;
			timing();
			recording = true;
			isStopRecord = false;
			listener.onStart(this);
	}
    
    private Handler handler = new Handler();   
    
	private void timing() {  
        runnable = new Runnable() {  
            @Override  
            public void run() {  
                voiceLength += 100;  
//            	texttime.setText(TimeUtils.convertMilliSecondToMinute2(voiceLength));  
                handler.postDelayed(this, 100);  
            }  
        };  
        handler.postDelayed(runnable, 100);  
    }  
	
	//停止
	public void stop(){
		if (mRecorder != null) {
			voiceLength = 0;
			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
		
		isRecording = false;

		if(listener!=null) {
			if (handler != null && runnable != null) {
				handler.removeCallbacks(runnable);
				runnable = null;
			}
			voiceLength = 0;
			listener.onStop(this);
			
		}
	}
	

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void setRecorderListener(MediaRecorderListener listener){
		this.listener = listener;
	}
	
	public static interface MediaRecorderListener{
		public void onStop(MyMediaRecorder recorder);
		public void onStart(MyMediaRecorder recorder);
	}

	public boolean isRecording() {
		return isRecording;
	}

	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	// 获取录音文件名
	public String getRecordFileName(String syxh) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				syxh+"_'V'_yyyyMMddHHmmss");
		return dateFormat.format(date) + ".3gp";
	}
	
	// 获取文件名图片名
	public String getPhotoFileName(String syxh) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				syxh+"_'P'_yyyyMMddHHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
	
}
