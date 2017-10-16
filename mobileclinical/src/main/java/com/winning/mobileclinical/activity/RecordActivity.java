package com.winning.mobileclinical.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.fragment.CFJLFragment;

import static com.winning.mobileclinical.R.id.record_cancle;

public class RecordActivity extends FragmentActivity implements View.OnClickListener {

    private RelativeLayout rl;
    private Chronometer timer;
    private FrameLayout fl_circle;
    private ImageView jicheng;
    private Button btn_record_save;
    private Button btn_record_cancle;
    private CFJLFragment cfjlFragment;
    private long rangeTime;
    private ImageView iv_audio_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
//        cfjlFragment = (CFJLFragment) getSupportFragmentManager().findFragmentByTag("cfjlFragment");
        initView();
        initClick();
        playAnimation();

    }

    private void initClick() {
        btn_record_save.setOnClickListener(this);
        btn_record_cancle.setOnClickListener(this);
    }

    private void initView() {
        timer = (Chronometer) findViewById(R.id.timer);
        fl_circle = (FrameLayout) findViewById(R.id.fl_circle);
        jicheng = (ImageView) findViewById(R.id.jicheng);
        iv_audio_center = (ImageView) findViewById(R.id.iv_audio_center);
        btn_record_save = (Button) findViewById(R.id.record_save);
        btn_record_cancle = (Button) findViewById(record_cancle);


    }

    private void playAnimation() {
        Animation rota = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rota.setRepeatCount(-1);
        rota.setDuration(2000);
        rota.setInterpolator(new LinearInterpolator());
        jicheng.startAnimation(rota);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv_audio_center, "alpha", 1, 0.5f, 0.25f, 0.25f, 0.5f, 1);
        objectAnimator.setDuration(2000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();


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
                setResult(20);
                finish();
                break;
            case R.id.record_cancle:
                setResult(30);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(30);
        finish();
        return true;
    }
}
