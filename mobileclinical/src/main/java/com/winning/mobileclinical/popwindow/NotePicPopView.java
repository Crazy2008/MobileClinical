package com.winning.mobileclinical.popwindow;

import java.util.List;

import com.winning.mobileclinical.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class NotePicPopView extends PopupWindow {

	private final static int SHOWIMAGE = 1;
	
	private Context context;
	
	private Bitmap bitmap = null;
	private String imagePath;
	private LinearLayout imageshow;
	private ImageView imageview;
	
	private View imView;
	private int next = 0;
	private int id=0;  
	private float scaleWidth=1;  
    private float scaleHeight=1;  
	private static final String TAG = "DisplayImage";  
	
	private Handler MyHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			
			if(msg.what == SHOWIMAGE&&bitmap!=null)
			{
				imageshow.removeAllViews();
				
				imageview = new ImageView(context);
				imageview.setAdjustViewBounds(true);
				imageview.setLayoutParams(new LayoutParams(
						1000,1000));
				imageview.setImageBitmap(bitmap);
				imageview.setScaleType(ScaleType.FIT_CENTER);
				imageshow.addView(imageview);
			}
		}
	};
	public NotePicPopView(final Context context,View parent,LayoutInflater tinflater, final Bitmap image) {
		// TODO Auto-generated constructor stub
		this.bitmap = image;
		this.context = context;
        View _view = tinflater.inflate(R.layout.doctornote_pic, null,false);
        Button close = (Button)_view.findViewById(R.id.imgpop_close);
        imageshow = (LinearLayout)_view.findViewById(R.id.imgpop_image);
        
//        imageview = new ImageView(context);
//		imageview.setAdjustViewBounds(true);
//		imageview.setLayoutParams(new LayoutParams(
//				500,500));
//		imageview.setImageBitmap(bitmap);
//		imageview.setScaleType(ScaleType.FIT_CENTER);
		

		int bmpWidth=bitmap.getWidth();  
        int bmpHeight=bitmap.getHeight();  
          
        /* 设置图片放大的比例 */  
        double scale=1.25;  
        /* 计算这次要放大的比例 */  
        scaleWidth=(float)(scaleWidth*scale);  
        scaleHeight=(float)(scaleHeight*scale);  
        /* 产生reSize后的Bitmap对象 */  
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleWidth, scaleHeight);  
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bmpWidth,   
                bmpHeight,matrix,true);  
		
        imageview = new ImageView(context);  
        imageview.setId(id);  
        imageview.setImageBitmap(resizeBmp);
		imageshow.addView(imageview);
        
        close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				NotePicPopView.this.dismiss();
			}
		});
        
        
        
        Button zoomin = (Button)_view.findViewById(R.id.imgpop_zoomin);
//        zoomin.setVisibility(View.GONE);
        
        zoomin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				big();
			}
		});
        Button zoomout = (Button)_view.findViewById(R.id.imgpop_zoomout);
//        zoomout.setVisibility(View.GONE);
        zoomout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				small();
			}
		});
        
//        Button zooprevious = (Button)_view.findViewById(R.id.imgpop_previous);
//        zooprevious.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				
//				next = next - 1;
//				System.out.println("next"+next);
//				if(imagelist != null && next >= 0 && imagelist.size() > next)
//				{
//					Thread loadImageThread = new Thread(new loadimage(imagelist.get(next)));
//					loadImageThread.start();
//				} else {
//					next = next + 1;
//					Toast.makeText(context, "第一张图片",Toast.LENGTH_LONG).show();
//				}
//				
//			}
//		});
        
        
        
//        Button zoonext = (Button)_view.findViewById(R.id.imgpop_next);
//        zoonext.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				
//				next = next + 1;
//				
//				if(imagelist != null && imagelist.size()>next)
//				{
//					Thread loadImageThread = new Thread(new loadimage(imagelist.get(next)));
//					loadImageThread.start();
//				} else {
//					next = next - 1;
//					Toast.makeText(context, "最后一张图片",Toast.LENGTH_LONG).show();
//				}
//				
//			}
//		});
        
        setContentView(_view);
        setWidth(parent.getWidth());
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
	}

	/* 图片缩小的method */  
    private void small()    {  
        int bmpWidth=bitmap.getWidth();   
        int bmpHeight=bitmap.getHeight();  
          
        Log.i(TAG, "bmpWidth = " + bmpWidth + ", bmpHeight = " + bmpHeight);  
          
        /* 设置图片缩小的比例 */  
        double scale=0.8;  
        /* 计算出这次要缩小的比例 */   
        scaleWidth=(float) (scaleWidth*scale);   
        scaleHeight=(float) (scaleHeight*scale);   
        /* 产生reSize后的Bitmap对象 */  
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleWidth, scaleHeight);  
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bmpWidth,   
                bmpHeight,matrix,true);   
         
        
//        imageshow.removeView(imageview);
        imageshow.removeAllViews();
//        if(id==0)      {  
//            /* 如果是第一次按，就删除原来默认的ImageView */  
//        	imageshow.removeView(imageview);  
//        } else {  
//            /* 如果不是第一次按，就删除上次放大缩小所产生的ImageView */  
//        	imageshow.removeView((ImageView)this.findViewById(id));  
//        }   
          
        /* 产生新的ImageView，放入reSize的Bitmap对象，再放入Layout中 */  
        id++;  
        ImageView imageView = new ImageView(context);  
        imageView.setId(id);  
        imageView.setImageBitmap(resizeBmp);  
        imageshow.addView(imageView);  
        Log.i(TAG, "imageView.getWidth() = " + imageView.getWidth()  
                + ", imageView.getHeight() = " + imageView.getHeight());  
//        setContentView(imageview);  
        /* 因为图片放到最大时放大按钮会disable，所以在缩小时把它重设为enable */   
    }  
      
    /* 图片放大的method */  
    private void big() {  
        int bmpWidth=bitmap.getWidth();  
        int bmpHeight=bitmap.getHeight();  
          
        Log.i(TAG, "bmpWidth = " + bmpWidth + ", bmpHeight = " + bmpHeight);  
          
        /* 设置图片放大的比例 */  
        double scale=1.25;  
        /* 计算这次要放大的比例 */  
        scaleWidth=(float)(scaleWidth*scale);  
        scaleHeight=(float)(scaleHeight*scale);  
        /* 产生reSize后的Bitmap对象 */  
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleWidth, scaleHeight);  
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bmpWidth,   
                bmpHeight,matrix,true);  
//        imageshow.removeView(imageview);
        imageshow.removeAllViews();
//        if(id==0) {  
//            /* 如果是第一次按，就删除原来设置的ImageView */  
//        	imageshow.removeView(imageview);  
//        } else {  
//            /* 如果不是第一次按，就删除上次放大缩小所产生的ImageView */   
////        	imageshow.removeView((ImageView)findViewById(id));  
//        }  
          
        /* 产生新的ImageView，放入reSize的Bitmap对象，再放入Layout中 */  
        id++;  
        ImageView imageView = new ImageView(context);  
        imageView.setId(id);  
        imageView.setImageBitmap(resizeBmp);  
        imageshow.addView(imageView);  
//        setContentView(imageView);  
        /* 如果再放大会超过屏幕大小，就把Button disable */  
//        if( scaleWidth * scale * bmpWidth > bmpWidth * 3 ||  
//            scaleHeight * scale * bmpHeight > bmpWidth * 3 ||  
//            scaleWidth * scale * bmpWidth > displayWidth * 5 ||  
//            scaleHeight * scale * bmpHeight > displayHeight * 5) {  
//            } else {  
//            }  
        }   
	
	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		// TODO Auto-generated method stub
		super.showAsDropDown(anchor, xoff, yoff);
//		if(imagePath != null)
//		{
//			Thread loadImageThread = new Thread(new loadimage(imagePath));
//			loadImageThread.start();
//		}
	}

	public class loadimage implements Runnable {

		String imagePath="";
		public loadimage(String imagePath) {
			// TODO Auto-generated constructor stub
			this.imagePath = imagePath;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
//			imageview.setDrawingCacheEnabled(false);
//			bitmap = HISReportAction.getReportImage(context,WebUtils.PACSIMAGE,imagePath);
			
			
			MyHandler.sendEmptyMessage(SHOWIMAGE);
			
		}
	}
	


}
