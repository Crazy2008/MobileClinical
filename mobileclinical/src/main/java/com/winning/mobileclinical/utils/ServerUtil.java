package com.winning.mobileclinical.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.MediaList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 服务器上传下载类
 *
 * @author HM
 */
public class ServerUtil {
//	private static String newName = "image.jpg";
//    private static String uploadFile = "/sdcard/image.JPG";
//    private static String actionUrl = "http://192.168.10.57:8077/UploadServer.ashx";
//    private String actionUrl = "http://192.168.10.57:8011/upload.aspx";
//    private String actionUrl = "http://192.168.10.57:8077/data.asmx?op=post";
//    private String actionUrl = "http://localhost:7753/data.asmx";
//    private String actionUrl = "F:/games";
    private String downUrl = "http://192.168.10.57:8077/DownLoadServices.ashx?file=222.jpg";
    /*
     * 上传参数至服务器
	 */
    public static void uploadParamstoServer(String actionUrl, String fileName, String FilePath) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
	        /* 设置传送的method=POST */
            con.setRequestMethod("POST");
	        /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
	        /* 设置DataOutputStream */
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file1\";filename=\"" +
                    fileName + "\"" + end);
            ds.writeBytes(end);
            int res = con.getResponseCode();
	        /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
	        /* 将Response显示于Dialog */
//	        showDialog("上传成功"+b.toString().trim());
	        /* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
//	        showDialog("上传失败"+e);
        }
    }




    /*
    * 上传图片文件至服务器
    */
    public static void uploadtoServer(String actionUrl, String fileName, String FilePath) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
	        /* 设置传送的method=POST */
            con.setRequestMethod("POST");
	        /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
	        /* 设置DataOutputStream */
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file\";filename=\"" +
                    fileName + "\"" + end + "Content-Type: image/jpeg" + end);
            ds.writeBytes(end);

            if(FilePath==null||FilePath.trim().equals("")){
            }else{
	        /* 取得文件的FileInputStream */
                FileInputStream fStream = new FileInputStream(FilePath);
	        /* 设置每次写入1024bytes */
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
	        /* 从文件读取数据至缓冲区 */
                while ((length = fStream.read(buffer)) != -1) {
	          /* 将资料写入DataOutputStream中 */
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                fStream.close();
                ds.flush();
            }
            int res = con.getResponseCode();
	        /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
//	        showDialog("上传失败"+e);
        }
    }

    /*
     * 从服务器下载资源
     */
    public static void downloadfromServer(MediaList media) {
//        String newName = "123.jpg";
        File file = new File(getMkDir(true) + "/" + media.getName());
        //如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if (file.exists()) {
            file.delete();
        }
        try {
            // 构造URL
            URL url = new URL(GlobalCache.getCache().getCFJLDownloadUrl()+"&file="+media.getXh());
            // 打开连接
//		         URLConnection con = url.openConnection();  
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //获得文件的长度
            int contentLength = con.getContentLength();
            System.out.println("长度 :" + contentLength);
            int res = con.getResponseCode();
            // 输入流
            InputStream is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(getMkDir(true) + "/" + media.getName());
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
//		         showDialog("下传成功");
            os.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
//			showDialog("下载失败"+e);
        }
    }

    // 获取文件夹
    public static String getMkDir(Boolean sdexiting) {
        String saveDir = "";
        if (sdexiting) {
            saveDir = Environment.getExternalStorageDirectory() + "/mobileclinical";
        } else {
            saveDir = "/mnt/sdcard-ext/mobileclinical";
        }
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdir(); // 创建文件夹
        }
        return saveDir;
    }



    /*
   * 上传 涂鸦图片文件至服务器
   */
    public static void uploadTuYatoServer(String actionUrl, String fileName, Bitmap bitmap) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
	        /* 设置传送的method=POST */
            con.setRequestMethod("POST");
	        /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
	        /* 设置DataOutputStream */
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"file\";filename=\"" +
                    fileName + "\"" + end + "Content-Type: image/jpeg" + end);
            ds.writeBytes(end);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            InputStream fStream = new ByteArrayInputStream(baos.toByteArray());
	        /* 取得文件的FileInputStream */

	        /* 设置每次写入1024bytes */
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
	        /* 从文件读取数据至缓冲区 */
                while ((length = fStream.read(buffer)) != -1) {
	          /* 将资料写入DataOutputStream中 */
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
                fStream.close();
                ds.flush();

            int res = con.getResponseCode();
	        /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
//	        showDialog("上传失败"+e);
        }
    }
}
