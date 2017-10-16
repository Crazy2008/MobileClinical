package com.winning.mobileclinical.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.activity.EditBookMarkActivity;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.utils.LogUtils;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.PubInterfce;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *病历
 */
@SuppressLint("NewApi")
public class CaseHistory extends FragmentChild {
    private static final int MODE_LIST = 0;
    private static ProgressDialog progressDialog;
    private static ProgressDialog ProgressDialog;
    private PatientInfo patient = null;
    private static WebView webView = null;
    private DoctorInfo doctor = null;
    private ProgressDialog progressBar;
    private String imgPath;
    private File photoFile;
    private int syxh;
    private int yexh;
    private int emrid;


    private int contentHeight;
    private int screenWidth;
    private int screenHeight;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    jiequBitmap();
                    break;
            }
        }
    };
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // 屏幕宽（像素，如：480px）
        screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        // 屏幕高（像素，如：
        screenHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();

        View view = inflater.inflate(R.layout.webviewpub, container, false);
        doctor = GlobalCache.getCache().getDoctor();
        webView = (WebView) view.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        String defaultTextEncodingName = settings.getDefaultTextEncodingName();
        LogUtils.showLog("encoding==="+defaultTextEncodingName);
        ViewUtil.setWebViewAttribute(webView);
        webView.setDrawingCacheEnabled(true);


        String sDataPath = getActivity().getApplicationContext()
                .getDir("database", Context.MODE_PRIVATE).getPath();
        webView.getSettings().setDatabasePath(sDataPath);
        LogUtils.showLog("CaseHistory-----------GlobalCache.getCache().getLoadEMR()="+GlobalCache.getCache().getLoadEMR());
        if (!GlobalCache.getCache().getLoadEMR()) {
            progressBar = ProgressDialog.show(getActivity(), "加载中", "请稍等");
            progressBar.setCanceledOnTouchOutside(true);

        }
        webView.addJavascriptInterface(new JavaScriptInterface(getActivity()), "App");
//        webView.getSettings().setDefaultTextEncodingName("ISO-8859-1");
//
//        webView.loadData(ViewUtil.url + "emr.html", "text/html", "UTF-8") ;
        webView.loadUrl(ViewUtil.url + "emr.html");
//        webView.loadDataWithBaseURL(ViewUtil.url + "emr.html", "", "text/html","UTF-8", null);

//        GlobalCache.getCache().setLoadEMR(true);


        webView.setWebChromeClient(new webChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        return view;
    }





    public class JavaScriptInterface extends PubInterfce {


        JavaScriptInterface(Context c) {
            super(c);
        }

        @Override
        public WebView getWebViewForSub() {
            // TODO Auto-generated method stub
            return webView;
        }
        @JavascriptInterface
        public void doEditBookmark(int syxh,int yexh,int emrid){
            CaseHistory.this.syxh = syxh;
            CaseHistory.this. yexh = yexh;
            CaseHistory.this.emrid = emrid;


//             Bitmap bitmap = getJieTu(CaseHistory.this.getActivity());
//            GlobalCache.getCache().setBitmap(bitmap);
//            jiequBitmap();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   Bitmap bitmap = getJieTuForWebView(webView);
                         GlobalCache.getCache().setBitmap(bitmap);
                    handler.sendEmptyMessage(1);
                }
            });

        }

    }

    private Bitmap   getJieTuForWebView(WebView webView){

        Picture picture = webView.capturePicture();
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        picture.writeToStream(bos);
//        byte[] ba = bos.toByteArray();
////        BitmapFactory.Options  options=new BitmapFactory.Options();
//        byte[] decode = Base64.decode(ba, Base64.DEFAULT);
//        Bitmap bmp = BitmapFactory.decodeByteArray(decode, 0, decode.length);
//        BitmapFactory.Options  options=new BitmapFactory.Options();
        Bitmap bmp = Bitmap.createBitmap(picture.getWidth(),picture.getHeight(), Bitmap.Config.ARGB_8888);
//        String s = bitmapToBase64(bmp);
//      Bitmap  bmp3 = base64ToBitmap(s);
//        Bitmap bmp = Bitmap.createBitmap(screenWidth,screenHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas= new Canvas(bmp);
        picture.draw(canvas);
        return bmp;

    }

    /**
     * 获取截图
     * @param activity
     * @return
     */
    public Bitmap getJieTu(Activity activity) {
        // 获取windows中最顶层的view
        View decorView = activity.getWindow().getDecorView();
        view=decorView.getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
////        View view = decorView.getRootView();
//        // 获取状态栏高度
//        Rect rect = new Rect();
//        view.getWindowVisibleDisplayFrame(rect);
//        int statusBarHeights = rect.top;
//        Display display = activity.getWindowManager().getDefaultDisplay();
//        // 获取屏幕宽和高
//        int widths = display.getWidth();
//        int heights = display.getHeight();
        // 允许当前窗口保存缓存信息
//        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
//                statusBarHeights, widths, heights - statusBarHeights);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                0, screenWidth, screenHeight);
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        view=null;
        return bmp;

    }


    public Bitmap screenShot() throws InterruptedException
    {
        Process sh;
        Bitmap bitmap=null;
        try
        {
            sh = Runtime.getRuntime().exec("su", null, null);
            OutputStream os = sh.getOutputStream();
            InputStream inputStream = sh.getInputStream();
             bitmap = BitmapFactory.decodeStream(inputStream);
//            byte[]buffer=new byte[1024];
//            os.write(buffer);
//            bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            return  bitmap;

    }


    private class webChromeClient extends WebChromeClient {
        @Override
        public void onExceededDatabaseQuota(String url,
                                            String databaseIdentifier, long currentQuota,
                                            long estimatedSize, long totalUsedQuota,
                                            WebStorage.QuotaUpdater quotaUpdater) {
            quotaUpdater.updateQuota(estimatedSize * 2);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
                if(newProgress==100){


                }
        }
    }


    public void        jiequBitmap(){
        Bitmap bitmap=GlobalCache.getCache().getBitmap();
        int margin_top = (int)(getResources().getDimension(R.dimen.pm_pattab_rlheight)+0.5f);
        int height_top = (int)(getResources().getDimension(R.dimen.pm_topheight)+0.5f);
        int b=ViewUtil.diptopx(getActivity(),20);
        int sum=margin_top+height_top+b;
        int v = (int)((bitmap.getWidth() * 0.175)+0.5);
        LogUtils.showLog("bitmapwidth="+bitmap.getWidth()+"bpheight="+bitmap.getHeight());
        //R.dimen.pb_topheight是下面床位选择器的高度   contentHeight是webview的高度
        bitmap=Bitmap.createBitmap(bitmap,v,0,bitmap.getWidth()-v,bitmap.getHeight());
        int topTitleHeight=ViewUtil.diptopx(getActivity(),60);
//            int topTitleHeight=ViewUtil.diptopx(getActivity(),getActivity().getResources().getDimension(R.dimen.pm_topheight));
        int lashenWidth=screenWidth-ViewUtil.diptopx(getActivity(),125);
        //拉伸图片
        bitmap=Bitmap.createScaledBitmap(bitmap,lashenWidth,screenHeight-topTitleHeight,true);
        GlobalCache.getCache().setBitmap(bitmap);
        GlobalCache.getCache().setOriginalBitmap(Bitmap.createBitmap(bitmap));
        Intent intent = new Intent(CaseHistory.this.getActivity(), EditBookMarkActivity.class);
        intent.putExtra("syxh",syxh);
        intent.putExtra("yexh",yexh);
        intent.putExtra("emrid",emrid);
        startActivity(intent);
    }

    private class MyWebViewClient extends WebViewClient {
         boolean isDone=false;
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.setEnabled(true);
            super.onPageStarted(view, url, favicon);
        }
      
        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
            if(!isDone){
                loadJsData();
                isDone=true;
            }

            //获取webview的高度
            contentHeight = webView.getHeight();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if(progressDialog!=null&&progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            return true;

        }
        @SuppressLint("NewApi")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          String url) {
            return super.shouldInterceptRequest(view, url);
        }
        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            // MessageUtils.showMsgDialog(oThis, errorCode + "");
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    public static void loadJsData() {
        webView.loadUrl("javascript:loadData()");
    }

    public static void loadPatEMRData(String syxh, String yexh) {
        webView.loadUrl("javascript:loadData('" + syxh + "','" + yexh
                + "')");
    }



    @Override
    public void switchPatient() {
        // TODO Auto-generated method stub
        refleash();
    }

    @Override
    protected void loadDate() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void afterLoadData() {
        // TODO Auto-generated method stub

    }



    private void clearDate() {
        // TODO Auto-generated method stub
        // emrListView.removeAllViews();
    }
    private void refleash() {
        // TODO Auto-generated method stub
        clearDate();
        if (GlobalCache.getCache().getPatient_selected() != null) {
            patient = GlobalCache.getCache().getPatient_selected();
        }
        loadDataWithProgressDialog();
    }


    public static void CloseDialog(ProgressDialog dialog){
          CaseHistory.progressDialog=dialog;
    }











    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();

    }
//    public void clearBitmap(){
//        if(bitmap != null&& !bitmap.isRecycled()){
//            // 回收并且置为null
//            bitmap.recycle();
//            bitmap = null;
//        }
//        System.gc();
//    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    /**
     *
     * @Title: bitmapToBase64
     * @Description: TODO(Bitmap 转换为字符串)
     * @param @param bitmap
     * @param @return    设定文件
     * @return String    返回类型
     * @throws
     */

    @SuppressLint("NewApi")
    public  String bitmapToBase64(Bitmap bitmap) {

        // 要返回的字符串
        String reslut = null;

        ByteArrayOutputStream baos = null;

        try {

            if (bitmap != null) {

                baos = new ByteArrayOutputStream();
                /**
                 * 压缩只对保存有效果bitmap还是原来的大小
                 */
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);

                baos.flush();
                baos.close();
                // 转换为字节数组
                byte[] byteArray = baos.toByteArray();

                // 转换为字符串
                reslut = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return reslut;

    }

    /**
     *
     * @Title: base64ToBitmap
     * @Description: TODO(base64l转换为Bitmap)
     * @param @param base64String
     * @param @return    设定文件
     * @return Bitmap    返回类型
     * @throws
     */
    public  Bitmap base64ToBitmap(String base64String){

        byte[] decode = Base64.decode(base64String, Base64.DEFAULT);
        BitmapFactory.Options options =new BitmapFactory.Options();
        options.inMutable=true;
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length,options);

        return bitmap;
    }







}
