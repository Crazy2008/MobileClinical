package com.winning.mobileclinical.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.utils.LogUtils;

/**
 * Created by Administrator on 2016/11/9.
 */

public class ShowPhotoActivity extends FragmentActivity {


    private Bitmap bitmap;
    private String pic_url;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);
        ImageView photoView= (ImageView) findViewById(R.id.photoView);
        pic_url = getIntent().getStringExtra("pic_url");
        //如果是网络图片
        if(pic_url.startsWith("http")){
            Glide.with(this).load(pic_url).into(photoView);
        } else{//本地图片加载
        LogUtils.showLog(pic_url);
//     Glide.with(this).load(pic_url).into()
         bitmap = BitmapFactory.decodeFile(pic_url);
        photoView.setImageBitmap(bitmap);
        }
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
