package com.winning.mobileclinical.widget;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.winning.mobileclinical.R;

public class AudioPlayer extends LinearLayout{

	
	private ImageButton button;
	
	private boolean isPlaying = false;
	
	private String path;

	private MediaPlayer mPlayer;

	public boolean isfinish;
	
	String totalText = "";
	
	private IsFinishedListener isFinishedListener;
	
	public AudioPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}
	
	public AudioPlayer(Context context){
		super(context);
		this.init(context);
	}
	
	private void init(Context context){
		LinearLayout layout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.audio_playernew, null);
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		this.addView(layout);
		
		button = (ImageButton) layout.findViewById(R.id.audioplayer_play);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isPlaying)
					stop();
				else
					start();
			}
		});
		
	}
	
	//开始播放
	public void start(){
		
		if(path==null)
			return;
		
		if(mPlayer==null)
			mPlayer = new MediaPlayer();
		
		try {
			mPlayer.reset();
			mPlayer.setDataSource(path);  
			mPlayer.prepare();
			
			mPlayer.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
//			Toast.makeText(this.getContext(), "播放异常……", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Log.e("BWLEdit_Record", "prepare() failed");
		}
		new ProgressThread().start();
		
		
		button.setBackgroundResource(R.drawable.record_play);
		isPlaying = true;
		
	}
	
	
	//开始播放
	public void startrecord(final String url){
		
		if(url==null)
			return;
		
		if(mPlayer==null)
			mPlayer = new MediaPlayer();
		
		new Thread(new Runnable() {  
			  
		    @Override  
		    public void run() {  
		    	playUrl(url);  
		    }  
		}).start();
		new ProgressThread().start();
		
		
		button.setBackgroundResource(R.drawable.record_play);
		isPlaying = true;
		
	}
	
	public void playUrl(String url) {  
        try {  
        	mPlayer.reset();  
        	mPlayer.setDataSource(url); // 设置数据源  
        	mPlayer.prepare(); // prepare自动播放  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IllegalStateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  

	
	//停止
	public void stop(){
		mPlayer.stop();
		isfinish=true;
//		button.setBackgroundResource(R.drawable.record_play);
		isPlaying = false;
	}

	
	class ProgressThread extends Thread{
		
		@Override
		public void run() {
			isfinish=false;
			int current = 0;
			while(!isfinish){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				current = mPlayer.getCurrentPosition();
				
				if(!mPlayer.isPlaying()){
					isfinish = true;
					
					//返回结束监听
					if(isFinishedListener!=null){
						isFinishedListener.isFinished(isfinish);
					}
					
					
					handler.sendEmptyMessage(-1);
				}else{
					handler.sendEmptyMessage(current);
				}
				
			}
		
		}
	}
	
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int current = msg.what;
			if(current==-1){
				stop();
//				exectime.setText("0:00/"+totalText);
			}else{
//				progressbar.setProgress(current);
				int seconds=current/1000;
				String currentText = seconds/60+":"+(seconds%60<10?("0"+seconds%60):seconds%60);
//				exectime.setText(currentText+"/"+totalText);
			}
			
		}
	};
	
	//播放结束监听
	public interface IsFinishedListener{
		public void isFinished(boolean isfinished);
	}
	
	public void setIsFinishedListener(IsFinishedListener i){
		this.isFinishedListener = i;
	}
	
	public void releasePlayer(){
		if(mPlayer!=null){
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
//		this.filename.setText(path.substring(path.lastIndexOf("/")+1));
		
		if(mPlayer==null)
			mPlayer = new MediaPlayer();
		try {
			mPlayer.reset();
			mPlayer.setDataSource(path);  
			mPlayer.prepare();
			int totaltime = mPlayer.getDuration();
			int seconds=totaltime/1000;
			totalText = seconds/60+":"+(seconds%60<10?("0"+seconds%60):seconds%60);
//			exectime.setText("0:00/"+totalText);
//			progressbar.setMax(totaltime);
		} catch (IOException e) {
			Log.e("BWLEdit_Record", "prepare() failed");
		}
	}

	public String getFilenameString() {
		return "";
	}
}
