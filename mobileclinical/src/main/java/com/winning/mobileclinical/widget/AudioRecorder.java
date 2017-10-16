package com.winning.mobileclinical.widget;

import java.io.IOException;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.web.TimeUtils;

public class AudioRecorder extends LinearLayout{

	
	private TextView text;
	private TextView texttime;
	private TextView textdisplay;
	private Button button;
	
	private boolean isRecording = false;
	private MediaRecorder mRecorder;
	
	private String path;
	private AudioRecorderListener listener;
	

	private Runnable runnable;
	private  int voiceLength;
	Boolean bool = false;
	Boolean recording = false;
	Boolean isStopRecord= false;
	
	RelativeLayout relativeLayout;
	
	
	
	public AudioRecorder(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}
	
	public AudioRecorder(Context context){
		super(context);
		this.init(context);
	}
	
	private void init(Context context){
		LinearLayout layout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.bwlnewaudio_record, null);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		this.addView(layout);
		
		relativeLayout = (RelativeLayout) layout.findViewById(R.id.audiorecorder);
		
		text = (TextView) layout.findViewById(R.id.audiorecorder_text);
		texttime = (TextView) layout.findViewById(R.id.display_time);
		textdisplay = (TextView) layout.findViewById(R.id.display_text);
//		button = (Button) layout.findViewById(R.id.audiorecorder_button);
		
//		button.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(isRecording){
//					stop();
//				}else{
//					start();
//				}
//			}
//		});
		
		
		relativeLayout.setOnClickListener(touchListener);
		
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
		text.setText("正在录音");
//		text.setTextColor(Color.parseColor("#7b8694"));
		
		texttime.setText("00:00");
		textdisplay.setText("轻触结束录音");
		
//		button.setBackgroundResource(R.drawable.syxs_stop_selector);
		
		if(listener!=null)
			voiceLength = 0;
			timing();
//			handler.postDelayed(task, 1000);
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
            	texttime.setText(TimeUtils.convertMilliSecondToMinute2(voiceLength));  
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
//		text.setText("点击右边按钮，开始录音。");
//		button.setBackgroundResource(R.drawable.play_button);
		
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
	
	public void setRecorderListener(AudioRecorderListener listener){
		this.listener = listener;
	}
	
	
	
	
	public static interface AudioRecorderListener{
		public void onStop(AudioRecorder recorder);
		public void onStart(AudioRecorder recorder);
	}



	public boolean isRecording() {
		return isRecording;
	}

	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	
	
}
