package com.winning.mobileclinical.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.winning.mobileclinical.R;

import uk.co.senab.photoview.PhotoView;

public class BoostImageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost_image);
        Bitmap bitmap = getIntent().getParcelableExtra("bitmap");
        PhotoView pv = (PhotoView) findViewById(R.id.photoView);
        pv.setImageBitmap(bitmap);
    }
}
