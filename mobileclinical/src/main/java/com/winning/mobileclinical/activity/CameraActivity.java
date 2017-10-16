package com.winning.mobileclinical.activity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.cis.PatientInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends Activity implements View.OnClickListener {

    private Button button;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Camera camera;
    private boolean sdexiting;
    private PatientInfo patient = null;			//病人信息

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        bindView();
        judgeSdCard();
        /* 获取当前病人 */
        if(GlobalCache.getCache().getPatient_selected() != null)
        {
            patient = GlobalCache.getCache().getPatient_selected();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }
    //判断sdk状态
    private void judgeSdCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED))
        {
            sdexiting = true;
        }
        else
        {
            sdexiting = false;
            Toast.makeText(this, "SD卡不可用,拍照使用小图模式", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindView() {
        button = (Button) findViewById(R.id.but_camera);
        button.setOnClickListener(this);
        surfaceView = (SurfaceView) findViewById(R.id.surfaveView);
        holder = surfaceView.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                startCamera();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopCamera();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Log.d("tag", "onClick ----onClick");
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) { //拍照时返回的影像
                Log.d("tag", "zoubuzou ----onPictureTaken");
                Log.d("tag", "zoubuzou ----onPictureTaken"+data.length);
                String path = "";
                if(!(path = saveData(data)).equals("")){
                    Intent imageIntent=new Intent(CameraActivity.this,CameraPreviewActivity.class);
                    imageIntent.putExtra("path",path);
                    startActivity(imageIntent);
                    finish();
                }
//                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);



            }
        });
    }

    //保存临时文件，返回保存的路径
    private String saveData(byte[] data) {
        try {
            File file = File.createTempFile("img", "");//在默认临时文件目录中创建一个空文件，使用给定前缀和后缀生成其名称。
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void startCamera() {
        camera = Camera.open();
        try {
            camera.setPreviewDisplay(holder);//设置预览窗口
            camera.startPreview(); //开始预览
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopCamera() {
        camera.stopPreview();//停止预览；
        camera.release();//回收资源
        camera = null;
    }

    public void back(View view) {
        finish();
    }



    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }
}
