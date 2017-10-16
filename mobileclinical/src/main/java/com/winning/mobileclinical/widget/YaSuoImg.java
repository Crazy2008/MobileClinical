package com.winning.mobileclinical.widget;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class YaSuoImg {

	
	public YaSuoImg(){

	}
	
	public Bitmap getimage(String filePath,String finalpath,String fileName) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		//��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath,newOpts);
		
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int hh = 800;//�������ø߶�Ϊ800f
		int ww = 480;//�������ÿ��Ϊ480f
		//���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ����ݽ��м��㼴��
		int be = 1;//be=1��ʾ������
		if (w > h && w > ww) {//����ȴ�Ļ���ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {//���߶ȸߵĻ���ݸ߶ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;//�������ű���
		//���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		bitmap = BitmapFactory.decodeFile(filePath, newOpts);
		//System.out.println(srcPath);
		File mFile=new File(filePath);
		mFile.delete();
		return compressImage(bitmap,finalpath,fileName);//ѹ���ñ����С���ٽ�������ѹ��
	}
	/**
	 * ����ѹ��
	 * @param image
	 * @return
	 */
	private Bitmap compressImage(Bitmap image,String finalpath,String fileName) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//����ѹ������������100��ʾ��ѹ������ѹ�������ݴ�ŵ�baos��
		int options = 100;
		while ( baos.toByteArray().length / 1024>100) {	//ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��		
			baos.reset();//����baos�����baos
			options -= 10;//ÿ�ζ�����10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//����ѹ��options%����ѹ�������ݴ�ŵ�baos��
			
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//��ѹ��������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//��ByteArrayInputStream������ͼƬ
		try {       
			makeRootDirectory(finalpath);
			
			FileOutputStream out = new FileOutputStream(finalpath+fileName);
			Log.i("path",finalpath+fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
		} 
		catch (Exception e) {       
			e.printStackTrace();
		}
		return bitmap;
	}	
	
	  public static void makeRootDirectory(String filePath) {
		   File file = null;
		   try {
			   file = new File(filePath);  
			   if (!file.exists()) {
				   file.mkdir(); 
				   } 
			   } catch (Exception e) { 
				   
			   }
	  }
	
	
	
}
