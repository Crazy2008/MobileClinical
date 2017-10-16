package com.winning.mobileclinical.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.winning.mobileclinical.R;

import java.io.File;

public class CameraPreviewActivity extends Activity implements View.OnClickListener {

    private ImageView imageView;
    private Button chongpai;
    private Button save;
    private String path;
    private boolean flag;
    private File file;
    private Boolean isDelete=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);
        imageView = (ImageView)findViewById(R.id.imageView);
        chongpai = (Button) findViewById(R.id.chongpai);
        save = (Button) findViewById(R.id.save);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        if(path != null && path != ""){
            File file = new File(path);
            imageView.setImageURI(Uri.fromFile(file));
        }
        chongpai.setOnClickListener(this);
        save.setOnClickListener(this);
//        byte[] bitmaps = intent.getByteArrayExtra("bitmap");
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmaps, 0, bitmaps.length);
//        imageView.setImageBitmap(bitmap);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chongpai:
                startActivity(new Intent(CameraPreviewActivity.this,CameraActivity.class));
                break;
            case R.id.save:
                isDelete=true;
                finish();
                break;
        }

    }
    public boolean deleteFile(String sPath) {
        flag = false;
        file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isDelete){
            deleteFile(path);
        }
    }
}
