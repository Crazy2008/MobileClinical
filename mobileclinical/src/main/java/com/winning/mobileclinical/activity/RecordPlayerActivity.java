package com.winning.mobileclinical.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.fragment.CFJLFragment;
import com.winning.mobileclinical.utils.LogUtils;

import java.io.IOException;

public class RecordPlayerActivity extends Activity implements View.OnClickListener, MediaPlayer.OnCompletionListener {

    private RelativeLayout rl;
    private Chronometer timer;
    private FrameLayout fl_circle;
    private ImageView jicheng;
    private Button btn_record_save;
    private Button btn_record_pause;
    private CFJLFragment cfjlFragment;
    private MediaPlayer player;
        private long rangeTimer;
    private Animation rota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palyer_record);
//        cfjlFragment = (CFJLFragment) getSupportFragmentManager().findFragmentByTag("cfjlFragment");
        String record_url = getIntent().getStringExtra("record_url");
        if(TextUtils.isEmpty(record_url)){
            return;
        }
       player = new MediaPlayer();
        /*给录音播放添加监听结束的事件*/
        player.setOnCompletionListener(this);
        try {
            player.setDataSource(record_url);
            player.prepare();
            player.start();
        } catch (IOException e) {
            LogUtils.showLog("播放音频失败");
            e.printStackTrace();
        }


        initView();
        initClick();
        playAnimation();

    }

    private void initClick() {
        btn_record_save.setOnClickListener(this);
        btn_record_pause.setOnClickListener(this);
    }

    private void initView() {
        timer = (Chronometer) findViewById(R.id.timer);
        fl_circle = (FrameLayout) findViewById(R.id.fl_circle);
        jicheng = (ImageView) findViewById(R.id.jicheng);
        btn_record_save = (Button) findViewById(R.id.record_save);
        btn_record_pause = (Button) findViewById(R.id.record_pause);


    }

    private void playAnimation() {
        rota = new RotateAnimation(360, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rota.setRepeatCount(-1);
        rota.setDuration(2000);
        rota.setInterpolator(new LinearInterpolator());
        rota.setFillAfter(true);
        jicheng.startAnimation(rota);



        timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
        timer.setFormat("0" + String.valueOf(hour) + ":%s");
        timer.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //保存录音的点击事件
            case R.id.record_save:
                setResult(30);
                finish();
                break;

            case R.id.record_pause:
                if(player.isPlaying()){
                    rangeTimer = SystemClock.elapsedRealtime()-timer.getBase();
                    timer.stop();
                    jicheng.clearAnimation();
                    btn_record_pause.setBackgroundResource(R.drawable.play);

                player.pause();
                }else{
                    timer.setBase(SystemClock.elapsedRealtime()-rangeTimer);
                    timer.start();
                    jicheng.startAnimation(rota);
                   player.start();
                    btn_record_pause.setBackgroundResource(R.drawable.pause);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(player.isPlaying()){
            player.stop();
            player.release();
        }
        timer.stop();

    }
    //当录音播放结束之后调用的方法
    @Override
    public void onCompletion(MediaPlayer mp) {
        Toast.makeText(this,"录音播放结束",Toast.LENGTH_SHORT).show();
            finish();
    }
}
