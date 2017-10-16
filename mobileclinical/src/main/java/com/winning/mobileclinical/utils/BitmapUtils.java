package com.winning.mobileclinical.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.View;

import com.winning.mobileclinical.R;

public class BitmapUtils {
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	// 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
	private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
			int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
		if (src != dst) { // 如果没有缩放，那么不回收
			src.recycle(); // 释放Bitmap的native像素数组
		}
		return dst;
	}

	// 从Resources中加载图片
	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options); // 读取图片长款
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight); // 计算inSampleSize
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
		return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
	}

	// 从sd卡上加载图片
	public static Bitmap decodeSampledBitmapFromFd(String pathName,
			int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
		return createScaleBitmap(src, reqWidth, reqHeight);
	}
	
	//放大缩小图片  
    public static Bitmap zoomBitmap(Bitmap bitmap,int w,int h){  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
        Matrix matrix = new Matrix();  
        float scaleWidht = ((float)w / width);  
        float scaleHeight = ((float)h / height);  
        matrix.postScale(scaleWidht, scaleHeight);  
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);  
        return newbmp;  
    }  
    //将Drawable转化为Bitmap  
     public static Bitmap drawableToBitmap(Drawable drawable){  
            int width = drawable.getIntrinsicWidth();  
            int height = drawable.getIntrinsicHeight();  
            Bitmap bitmap = Bitmap.createBitmap(width, height,  
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                            : Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);  
            drawable.setBounds(0,0,width,height);  
            drawable.draw(canvas);  
            return bitmap;  
              
        }  
       
     //获得圆角图片的方法  
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){  
          
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap  
                .getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
   
        final int color = 0xff424242;  
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);  
   
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
   
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
   
        return output;  
    }  
    //获得带倒影的图片方法  
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap){  
        final int reflectionGap = 4;  
        int width = bitmap.getWidth();  
        int height = bitmap.getHeight();  
          
        Matrix matrix = new Matrix();  
        matrix.preScale(1, -1);  
          
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap,   
                0, height/2, width, height/2, matrix, false);  
          
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);  
          
        Canvas canvas = new Canvas(bitmapWithReflection);  
        canvas.drawBitmap(bitmap, 0, 0, null);  
        Paint deafalutPaint = new Paint();  
        canvas.drawRect(0, height,width,height + reflectionGap,  
                deafalutPaint);  
          
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);  
          
        Paint paint = new Paint();  
        LinearGradient shader = new LinearGradient(0,  
                bitmap.getHeight(), 0, bitmapWithReflection.getHeight()  
                + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);  
        paint.setShader(shader);  
        // Set the Transfer mode to be porter duff and destination in  
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));  
        // Draw a rectangle using the paint with our linear gradient  
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()  
                + reflectionGap, paint);  
   
        return bitmapWithReflection;  
    }
    
	public static Bitmap getViewBitmap(View v) {    
        v.clearFocus();    
        v.setPressed(false);    
    
        boolean willNotCache = v.willNotCacheDrawing();    
        v.setWillNotCacheDrawing(false);    
    
        // Reset the drawing cache background color to fully transparent    
        // for the duration of this operation    
        int color = v.getDrawingCacheBackgroundColor();    
        v.setDrawingCacheBackgroundColor(0);    
    
        if (color != 0) {    
            v.destroyDrawingCache();    
        }    
        v.buildDrawingCache();    
        Bitmap cacheBitmap = v.getDrawingCache();    
        if (cacheBitmap == null) {    
            Log.e("Folder", "failed getViewBitmap(" + v + ")", new RuntimeException());    
            return null;    
        }    
    
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);    
    
        // Restore the view    
        v.destroyDrawingCache();    
        v.setWillNotCacheDrawing(willNotCache);    
        v.setDrawingCacheBackgroundColor(color);    
    
        return bitmap;    
    }
	
	/** 
    * 把View绘制到Bitmap上 
    * @param view 需要绘制的View 
    * @param width 该View的宽度 
    * @param height 该View的高度 
    * @return 返回Bitmap对象 
    * add by csj 13-11-6 
    */  
    @SuppressLint("NewApi")
	public static Bitmap getViewBitmap(View comBitmap, int width, int height) {  
        Bitmap bitmap = null;  
        if (comBitmap != null) {  
            comBitmap.clearFocus();  
            comBitmap.setPressed(false);  
  
            boolean willNotCache = comBitmap.willNotCacheDrawing();  
            comBitmap.setWillNotCacheDrawing(false);  
  
            // Reset the drawing cache background color to fully transparent  
            // for the duration of this operation  
            int color = comBitmap.getDrawingCacheBackgroundColor();  
            comBitmap.setDrawingCacheBackgroundColor(0);  
            float alpha = comBitmap.getAlpha();  
            comBitmap.setAlpha(1.0f);  
  
            if (color != 0) {  
                comBitmap.destroyDrawingCache();  
            }  
              
            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);  
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);  
            comBitmap.measure(widthSpec, heightSpec);  
            comBitmap.layout(0, 0, width, height);  
  
            comBitmap.buildDrawingCache();  
            Bitmap cacheBitmap = comBitmap.getDrawingCache();  
            if (cacheBitmap == null) {  
                Log.e("view.ProcessImageToBlur", "failed getViewBitmap(" + comBitmap + ")",   
                        new RuntimeException());  
                return null;  
            }  
            bitmap = Bitmap.createBitmap(cacheBitmap);  
            // Restore the view  
            comBitmap.setAlpha(alpha);  
            comBitmap.destroyDrawingCache();  
            comBitmap.setWillNotCacheDrawing(willNotCache);  
            comBitmap.setDrawingCacheBackgroundColor(color);  
        }  
        return bitmap;  
    }
    
    //根据Drawable获取缩放、圆角Bitmap
    public static Bitmap getDrawableThumnail(Context context, int id, String status){
  		//获取Drawable
  		Drawable drawable = context.getResources().getDrawable(id);  
        //将Drawable转化为Bitmap  
        Bitmap bitmap = drawableToBitmap(drawable);
  		//缩放图片
        int width=0,height=0;
        //缩放宽、高
     	width = context.getResources().getDimensionPixelOffset(R.dimen.note_pic_width);
     	if(status.equals("show")){
            height = context.getResources().getDimensionPixelSize(R.dimen.note_pic_height);
     	}else{
     		height = context.getResources().getDimensionPixelSize(R.dimen.addnote_pic_height);
     	}
  		Bitmap zoomBitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
        //获取圆角图片  
        Bitmap roundBitmap = getRoundedCornerBitmap(zoomBitmap, 10.0f);  
        //获取倒影图片  
//      Bitmap reflectBitmap = BitmapUtils.createReflectionImageWithOrigin(zoomBitmap);  
                   
        return roundBitmap;
  	}
    
    //根据Bitmap位图获取缩放、圆角Bitmap
    public static Bitmap getBitmapThumnail(Context context, Bitmap bitmap, String status){

  		
  		//缩放图片
        int width=0,height=0;
     	width = context.getResources().getDimensionPixelOffset(R.dimen.note_pic_width);
        //缩放宽、高
        if(status.equals("show")){
         	height = context.getResources().getDimensionPixelSize(R.dimen.note_pic_height);
        }else{
        	height = context.getResources().getDimensionPixelSize(R.dimen.addnote_pic_height);
        }

  		Bitmap zoomBitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
        //获取圆角图片  
        Bitmap roundBitmap = getRoundedCornerBitmap(zoomBitmap, 10.0f);  
        //获取倒影图片  
//      Bitmap reflectBitmap = BitmapUtils.createReflectionImageWithOrigin(zoomBitmap);  
                   
        return roundBitmap;
  	}
    
    //根据本地图片路径获取Bitmap
    public static Bitmap getFilePathThumnail(Context context, String path){  
        //获取路径位图
        Bitmap bitmap = BitmapFactory.decodeFile(path);
  		//缩放图片
        //缩放宽、高
        int width = context.getResources().getDimensionPixelOffset(R.dimen.note_pic_width);
        int height = context.getResources().getDimensionPixelSize(R.dimen.addnote_pic_height);
  		Bitmap zoomBitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);
        //获取圆角图片  
        Bitmap roundBitmap = getRoundedCornerBitmap(zoomBitmap, 10.0f);  
        //获取倒影图片  
//      Bitmap reflectBitmap = BitmapUtils.createReflectionImageWithOrigin(zoomBitmap);  

        return roundBitmap;
  	}
    
    /**
     * 加载本地图片
     * @param url
     * @return
     */
     public static Bitmap getLoacalBitmap(String url) {
          try {
               FileInputStream fis = new FileInputStream(url);
               return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

            } catch (FileNotFoundException e) {
               e.printStackTrace();
               return null;
          }
     }
     
     public static Bitmap getBitmapFromUrl(String imgUrl) {
         URL url;
         Bitmap bitmap = null;
         try {
            url = new URL(imgUrl);	
 			InputStream is = url.openStream();
 			bitmap = BitmapFactory.decodeStream(is);
 			is.close();
         } catch (MalformedURLException e) {
                 e.printStackTrace();
         } catch (IOException e) {
                 e.printStackTrace();
         }
         return bitmap;
   }
     
     /**
      * 加载本地图片
      * @param url
      * @return
      */
      public static Bitmap getServerBitmap(String url) {
           try {
//                FileInputStream fis = new FileInputStream(url);
//                return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        
                InputStream is = new URL(url).openStream();
                return BitmapFactory.decodeStream(is);
             } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				 return null;
			}
      }
     /**
      * 加载服务器图片
      * @param url
      * @return
      */
     public static byte[] getImage(String path) throws IOException {  
         URL url = new URL(path);  
         HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
         conn.setRequestMethod("GET");   //设置请求方法为GET  
         conn.setReadTimeout(5*1000);    //设置请求过时时间为5秒 
         int status = conn.getResponseCode();
         System.out.println("打开状态"+status);
         
         InputStream inputStream = conn.getInputStream();   //通过输入流获得图片数据  
         byte[] data = readInputStream(inputStream);     //获得图片的二进制数据  
         return data;  
     }
     
     public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
         byte[] buffer = new byte[1024];  
         int len = 0;  
         ByteArrayOutputStream bos = new ByteArrayOutputStream();  
         while((len = inputStream.read(buffer)) != -1) {  
             bos.write(buffer, 0, len);  
         }  
         bos.close();  
         return bos.toByteArray();  
           
     }
     
     public static Bitmap getReportImage(Context context,String imgurl) {
 		
// 		byte[] imagedata = HTTPGetTool.getTool().ImageDownLoad(context,
// 				);
// 		Bitmap bitmap = null;
// 		try{
// 			if (imagedata != null) {
// 				BitmapFactory.Options opts = new BitmapFactory.Options();
// 				opts.inJustDecodeBounds = true;
// 				bitmap = BitmapFactory.decodeByteArray(imagedata, 0,
// 						imagedata.length,opts);
// 				opts.inJustDecodeBounds = false;
// 				
// 				bitmap = BitmapFactory.decodeByteArray(imagedata, 0,
// 						imagedata.length,opts);
// 			}
// 		} catch (JsonIOException e) {
// 			e.printStackTrace();
// 		}
// 		return bitmap;
    	 
    	 return null;
 	}

     
     
//     public byte[] ImageDownLoad(Context context, String url,
// 			List<NameValuePair> parameters) {
// 		// TODO Auto-generated method stub
// 		if (url == null)
// 			return null;
//
// 		try {
// 			HttpResponse response = null;
// 			HttpPost post = null;
//
// 			post = new HttpPost();
// 			post.setHeader("Cookie", cookie);
// 			post.setURI(new URI(url));
// 			post.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
// 			response = httpclient.execute(post);
// 			int result = response.getStatusLine().getStatusCode();
// 			if (result == 200) {
// 				byte[] data = EntityUtils.toByteArray(response.getEntity());
// 				if (new String(data, HTTP.UTF_8).equals("no session")) {
//
// 					return null;// new HttpResult(6, "会话超时！", null);
// 				}
// 				return data;
// 			} else
// 				return null;// new HttpResult(4, "服务器响应异常！", null);
//
// 		} catch (UnsupportedEncodingException e) {
// 			e.printStackTrace();
// 			return null;// new HttpResult(7, "", null);
// 		} catch (ClientProtocolException e) {
// 			e.printStackTrace();
// 			return null;// new HttpResult(7, e.getMessage(), null);
// 		} catch (ParseException e) {
// 			e.printStackTrace();
// 			return null;// new HttpResult(4, "服务器响应异常！", null);
// 		} catch (SocketTimeoutException e) {
// 			e.printStackTrace();
// 			return null;// new HttpResult(2, "访问超时！", null);
// 		} catch (ConnectTimeoutException e) {
// 			e.printStackTrace();
// 			return null;// new HttpResult(2, "访问超时！", null);
// 		} catch (HttpHostConnectException e) {
// 			e.printStackTrace();
// 			return null;// new HttpResult(3, "网络连接异常！", null);
// 		} catch (IOException e) {
// 			e.printStackTrace();
// 			return null;// new HttpResult(3, e.getMessage(), null);
// 			// } catch (JSONException e) {
// 			// e.printStackTrace();
// 			// return null;//new HttpResult(5, "消息解析异常！", null);
// 		} catch (URISyntaxException e) {
// 			e.printStackTrace();
// 			return null;// new HttpResult(7, "访问地址不正确！", null);
// 		}
//
// 	}

// 刘爱剑 2016/5/24 11:23:47
// public static Bitmap getReportImage(Context context,String imgurl,Pacsimage pacsimage) {
// 		// TODO Auto-generated method stub
// 		List<NameValuePair> params = new ArrayList<NameValuePair>();
// 		params.add(new BasicNameValuePair("method","bgtp"));
// 		params.add(new BasicNameValuePair("yjbgurl",WebUtils.PACSIMAGE)); 
// 		params.add(new BasicNameValuePair("imageuid",pacsimage.getImageuid()));
// 		params.add(new BasicNameValuePair("seriesuid",pacsimage.getSeriesuid()));
// 		params.add(new BasicNameValuePair("studyuid",pacsimage.getStudyuid()));
// 		
// 		byte[] imagedata = HTTPGetTool.getTool().ImageDownLoad(context,
// 				WebUtilsHOST + WebUtils.HISREPORTACTION,params);
// 		Bitmap bitmap = null;
// 		try{
// 			if (imagedata != null) {
// 				BitmapFactory.Options opts = new BitmapFactory.Options();
// 				opts.inJustDecodeBounds = true;
// 				bitmap = BitmapFactory.decodeByteArray(imagedata, 0,
// 						imagedata.length,opts);
// 				opts.inJustDecodeBounds = false;
// 		//		int scale = (int) (opts.outWidth / (float) 320);
// 		//		if (scale <= 0)
// 		//		{
// 		//			scale = 1;
// 		//		}
// 		//		opts.inSampleSize = scale;
// 				
// 				bitmap = BitmapFactory.decodeByteArray(imagedata, 0,
// 						imagedata.length,opts);
// 			}
// 		} catch (JsonIOException e) {
// 			e.printStackTrace();
// 		}
// 		return bitmap;
// 	}

}
