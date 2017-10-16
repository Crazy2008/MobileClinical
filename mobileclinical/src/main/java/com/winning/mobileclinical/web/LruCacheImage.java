package com.winning.mobileclinical.web;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

@SuppressLint("NewApi")
public class LruCacheImage {
	
	private static Map<String, SoftReference<Bitmap>> imageMap  = new HashMap<String, SoftReference<Bitmap>>();
	
	private LruCache<String, Bitmap> mMemoryCache;
	
	int MAXMEMONRY = (int) (Runtime.getRuntime() .maxMemory() / 1024);
	
	
	private void LruCacheUtils() {
	        if (mMemoryCache == null)
	            mMemoryCache = new LruCache<String, Bitmap>(
	            		MAXMEMONRY / 8) {
	                @Override
	                protected int sizeOf(String key, Bitmap bitmap) {
	                    // 重写此方法来衡量每张图片的大小，默认返回图片数量。
	                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
	                }
	                @Override
	                protected void entryRemoved(boolean evicted, String key,
	                        Bitmap oldValue, Bitmap newValue) {
	                    Log.v("tag", "hard cache is full , push to soft cache");
	                   
	                }
	            };
	    }
	
	//清空缓存 添加图片到缓存
	@SuppressLint("NewApi")
	public void clearCache() {
        if (mMemoryCache != null) {
            if (mMemoryCache.size() > 0) {
                Log.d("CacheUtils",
                        "mMemoryCache.size() " + mMemoryCache.size());
                mMemoryCache.evictAll();
                Log.d("CacheUtils", "mMemoryCache.size()" + mMemoryCache.size());
            }
            mMemoryCache = null;
        }
    }

	//从缓存中取得图片、从缓存中移除
    @SuppressLint("NewApi")
	public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (mMemoryCache.get(key) == null) {
            if (key != null && bitmap != null)
                mMemoryCache.put(key, bitmap);
        } else
            Log.w("", "the res is aready exits");
    }

    @SuppressLint("NewApi")
	public synchronized Bitmap getBitmapFromMemCache(String key) {
        Bitmap bm = mMemoryCache.get(key);
        if (key != null) {
            return bm;
        }
        return null;
    }

    /**
     * 移除缓存
     * 
     * @param key
     */
    public synchronized void removeImageCache(String key) {
        if (key != null) {
            if (mMemoryCache != null) {
                Bitmap bm = mMemoryCache.remove(key);
                if (bm != null)
                    bm.recycle();
            }
        }
    }
    
    
    //先通过URL查看缓存中是否有图片，如果有，则直接去缓存中取得。 如果没有，就开线程重新去网上下载。
    public static Bitmap loadBitmap(final String imageUrl,final ImageCallBack imageCallBack) {
        SoftReference<Bitmap> reference = imageMap.get(imageUrl);
        if(reference != null) {
            if(reference.get() != null) {
                return reference.get();
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(final Message msg) {
                //加入到缓存中
                Bitmap bitmap = (Bitmap)msg.obj;
                imageMap.put(imageUrl, new SoftReference<Bitmap>(bitmap));
                if(imageCallBack != null) {
                    imageCallBack.getBitmap(bitmap);
                }
            }
        };
        new Thread(){
            public void run() {
                Message message = handler.obtainMessage();
                message.obj = downloadBitmap(imageUrl);
                handler.sendMessage(message);
            }
        }.start();
        return null ;
    }

    // 从网上下载图片
    private static Bitmap downloadBitmap (String imageUrl) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(imageUrl).openStream());
            return bitmap ;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
    }
    public interface ImageCallBack{
        void getBitmap(Bitmap bitmap);
    }
	
}
