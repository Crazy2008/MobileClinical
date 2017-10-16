package com.winning.mobileclinical.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.LoginAction;
import com.winning.mobileclinical.action.SysConfigAction;
import com.winning.mobileclinical.adapter.LoginBqksAdapter;
import com.winning.mobileclinical.db.dao.DepartmentWardDao;
import com.winning.mobileclinical.db.dao.DoctorDao;
import com.winning.mobileclinical.db.dao.SysConfigDao;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.layout.CornerListView;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.MisConfigInfo;
import com.winning.mobileclinical.model.cis.TableClass;
import com.winning.mobileclinical.update.UpdateManager;
import com.winning.mobileclinical.utils.LogUtils;
import com.winning.mobileclinical.utils.SharedPreferenceTools;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.SystemUtil;
import com.winning.mobileclinical.web.WebUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity implements OnGestureListener {


    private EditText et_username; // 登录用户名
    private EditText et_password; // 登录密码
    private Button bt_submit; // 登录按钮
    private EditText setIP;
    private String[] history = {"", "", "", "", ""};
    private EditText setEMR;
    private EditText setHLBL;
    private EditText setPACS;
    private Button setIP_confirm, setIP_test, setIP_cancel;
    private Dialog builder;
    List<String> ids = null;
    ProgressDialog proDialog; // 显示登陆中
    private CheckBox login_cb_offline;
    public static TextView et_bqks;
    private PopupWindow bqListWindow = null;
    private List<DeptWardMapInfo> bqlist = null; // 病区科室list
    private LoginBqksAdapter bqksAdapter = null;
    private LinearLayout bqkslinLayout = null;
    private final static float TARGET_HEAP_UTILIZATION = 0.75f;
    private EditText setWebservice;
    private Button setWeb_confirm, setWeb_cancel;
    /**
     * Handler 接收显示登录消息
     */
    Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 0) {
                String showText = (String) msg.obj;
                Toast.makeText(Login.this, showText, Toast.LENGTH_LONG).show();
                if (GlobalCache.getCache().getBqSel() == -1) {
                    GlobalCache.getCache().setBqSel(0);
                }
                Intent intent = new Intent(Login.this, PatientBrowse.class);

                // Intent intent = new Intent(Login.this, PatientMenu.class);
                startActivity(intent);

            } else if (msg.what == 3) {



                bqlist = GlobalCache.getCache().getDeptWardMapInfos();
                if (bqlist.size() > 0) {
                    if (GlobalCache.getCache().getBqSel() == -1) {
                        et_bqks.setText(bqlist.get(0).getBqmc() + " | " + bqlist.get(0).getKsmc());
                        GlobalCache.getCache().setBqSel(0);
                    } else {
                        et_bqks.setText(bqlist.get(GlobalCache.getCache().getBqSel()).getBqmc() + " | " + bqlist.get(GlobalCache.getCache().getBqSel()).getKsmc());
                    }
                }
            } else {
                String showText = (String) msg.obj;
                Toast.makeText(Login.this, showText, Toast.LENGTH_LONG).show();
            }
        }
    };
    private CheckBox cb_remeber_password;
    private UpdateManager updateManager;
private  String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
//		VMRuntime.getRuntime().setTargetHeapUtilization(TARGET_HEAP_UTILIZATION);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginnew);
        // GlobalCache.getCache().setContext(Login.this);
        ids = new ArrayList<String>();

        GlobalCache.getCache().setUrl(SharedPreferenceTools.getString(this,"newWebservice", GlobalCache.getCache().getUrl()));;
        GlobalCache.getCache().setHost(SharedPreferenceTools.getString(this,"newHost", GlobalCache.getCache().getHost()));;
        WebUtils.WEBSERVICE=SharedPreferenceTools.getString(this,"webservice",WebUtils.WEBSERVICE);
        WebUtils.HOST = SharedPreferenceTools.getString(this,"host", WebUtils.HOST);
        WebUtils.EMRconfig = SharedPreferenceTools.getString(this,"hostemr", WebUtils.EMRconfig);
        WebUtils.NISconfig = SharedPreferenceTools.getString(this,"hosthlbl", WebUtils.NISconfig);
        WebUtils.PACSIMAGE = SharedPreferenceTools.getString(this,"hostpacs", WebUtils.PACSIMAGE);

        // 当前选择的病区
        int bqSel = SharedPreferenceTools.getInt(this, "BqSel", -1);
        if(bqSel!=-1){
            GlobalCache.getCache().setBqSel(bqSel);
        }

        // 获得控件
        et_username = (EditText) findViewById(R.id.login_et_username);
        et_password = (EditText) findViewById(R.id.login_et_password);
        et_bqks = (TextView) findViewById(R.id.login_ksbq);
        bt_submit = (Button) findViewById(R.id.login_bt_submit);
        bqkslinLayout = (LinearLayout) findViewById(R.id.login_ksbqlayout);

        cb_remeber_password = (CheckBox) findViewById(R.id.cb_remeber_password);

        et_username.setText("00");
        if(!et_username.getText().toString().trim().equals("")){
            setPassword(et_username.getText().toString().trim());
        }
        updateManager = new UpdateManager(Login.this);
        // 获取当前软件版本
        versionName = updateManager.findVersionName();
        // 检查软件更新
        updateManager.checkUpdate(0);

        bqkslinLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

//				Thread getbqksThread = new Thread(new Logingetbqks());
//				getbqksThread.start();
                if (et_username.getText().toString() == "") {
                    return;
                }

                if (GlobalCache.getCache().getDeptWardMapInfos() != null && GlobalCache.getCache().getDeptWardMapInfos().size() > 0) {
                    initBqListWindow();
                }
//
//					Toast.makeText(Login.this, "获取病区科室错误，请查看本机是否可以访问服务器！", Toast.LENGTH_SHORT).show();

            }
        });
        //当用户名等于空的时候，清空密码
        et_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("tag", "beforeTextChanged==="+s+"---start="+start+",,,,count="+count+"----after="+after);

                if(start==0){
                    et_password.setText("");
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("tag", "onTextChanged==="+s+"---start="+start+",,,,count="+count+"----count="+count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("tag", "afterTextChanged==="+s);
                setPassword(s.toString());


            }
        });
        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {


            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d("tag","--------------------"+hasFocus);
                if(hasFocus){
                    //获得焦点
                }else{
                    if(TextUtils.isEmpty(et_username.getText().toString())){
                        return;
                    }
                }

                Thread getbqksThread = new Thread(new Logingetbqks());
                getbqksThread.start();
            }
        });


        // 登录处理
        bt_submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 判断用户名是否为空
                if (et_username.getText() == null
                        || "".equals(et_username.getText().toString())) {
                    Message message = new Message();
                    message.obj = "请输入用户名";
                    message.what = 1;
                    loginHandler.sendMessage(message);
                    return;
                }
                if ("8888".equals(et_username.getText().toString())
                        && "9999".equals(et_password.getText().toString())) {
//					setIPandPort();

                    setAddress();
                    return;
                }


                JSONObject loginObj = LoginAction.login(et_username.getText().toString().trim(), et_password.getText().toString().trim());
                LogUtils.showLog("loginObj==="+loginObj);

                if (!SystemUtil.isConnect(Login.this)) {
                    proDialog = ProgressDialog.show(Login.this, "",
                            "网络已断开，离线登录中...", true, true);
                    // 启动登录线程
                    // Thread loginThread = new Thread(new LoginHandler());
//					GlobalCache.getCache().setIsofflion(true);

                    Thread loginThread = new Thread(new LoginLXWebservice());

                    loginThread.start();

                } else {
                    proDialog = ProgressDialog.show(Login.this, "", "登录中...",
                            true, true);
                    // 启动登录线程
                    // Thread loginThread = new Thread(new LoginHandler());
                    Thread loginThread = new Thread(new LoginWebservice());
                    loginThread.start();
                }
                // 显示登陆中
            }
        });

    }
        //如果此用户名在记住密码本地有保存的时候，就去回显密码

    private void setPassword(String s) {
        if(!s.equals("")){
            String password = SharedPreferenceTools.getString(Login.this, s.toString(), "");
            et_password.setText(password);
            cb_remeber_password.setChecked(true);
        }
    }

    private void initBqListWindow() {
        List<String> data = new ArrayList<String>();

        bqlist = GlobalCache.getCache().getDeptWardMapInfos();

//		ListView bqlist_view = new ListView(this);

        CornerListView bqlist_view = new CornerListView(this);
        bqlist_view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//        bqksmap_view.setBackgroundResource(R.drawable.bqksback);
        bqlist_view.setDividerHeight(0);
        bqlist_view.setBackgroundResource(R.drawable.list_corner);
        bqlist_view.setFadingEdgeLength(5);

        bqlist_view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        bqlist_view.setDividerHeight(0);
        int index = GlobalCache.getCache().getBqSel();
        bqksAdapter = new LoginBqksAdapter(Login.this, bqlist,index);
        bqlist_view.setAdapter(bqksAdapter);

        if (bqlist.size() > 0) {

            if (GlobalCache.getCache().getBqSel() == -1) {
                et_bqks.setText(bqlist.get(0).getBqmc() + " | "
                        + bqlist.get(0).getKsmc());
                GlobalCache.getCache().setBqSel(0);
            } else {
                et_bqks.setText(bqlist.get(GlobalCache.getCache().getBqSel())
                        .getBqmc()
                        + " | "
                        + bqlist.get(GlobalCache.getCache().getBqSel())
                        .getKsmc());
            }
        }

        bqlist_view.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                bqListWindow.dismiss();
                DeptWardMapInfo departmentandward = bqlist.get(arg2);
                et_bqks.setText(bqlist.get(arg2).getBqmc() + " | "
                        + bqlist.get(arg2).getKsmc());
                SharedPreferenceTools.saveInt(Login.this,"BqSel",arg2);
                GlobalCache.getCache().setBqSel(arg2);
            }
        });

        int[] location = new int[2];
        bt_submit.getLocationOnScreen(location);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）


        bqListWindow = new PopupWindow(bqlist_view, ViewUtil.diptopx(
                Login.this, 600), ViewUtil.diptopx(Login.this, 500), true);
        bqListWindow.setBackgroundDrawable(new BitmapDrawable());// 点mPopupWindow外面可以让mPopupWindow消失
        bqListWindow.setOutsideTouchable(true);
        if (android.os.Build.VERSION.SDK_INT >= 22) {

        } else {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.alpha = 0.7f;
            getWindow().setAttributes(lp);
        }

//

        bqListWindow.showAtLocation(et_bqks, Gravity.CENTER, 0, 0);

        bqListWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });



    }

    private String getTextCount(int conut, String name) {
        // TODO Auto-generated method stub
        for (int i = 0; i < conut; i++) {
            name = name + " ";
        }
        return name;
    }

    /**
     * 在线登录线程 为了加速,登录时只下载和保存医生信息
     *
     * @author liu
     */

    private class LoginWebservice implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            // 访问数据库进行登录处理

            GlobalCache.getCache().init();

            List<TableClass> tableClass = new ArrayList<TableClass>();
            tableClass = SysConfigAction.getTableClasses(Login.this, "");
            LogUtils.showLog("Login===================");
            System.out.println(new Gson().toJson(tableClass));
            GlobalCache.getCache().setTableClasses(tableClass);

            if (tableClass == null) {
                Message message = new Message();
                message.obj = "登入错误，请查看本机是否可以访问服务器！";
                message.what = 1;
                loginHandler.sendMessage(message);
                proDialog.dismiss();
                return;
            }

            List<MisConfigInfo> misConfigInfos = new ArrayList<MisConfigInfo>();
            misConfigInfos = SysConfigAction.getMisConfigInfo(Login.this, "");
            System.out.println(new Gson().toJson(misConfigInfos));
            GlobalCache.getCache().setMisConfigInfos(misConfigInfos);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", et_username.getText().toString());
            JSONObject temp = new JSONObject(map);
            DoctorInfo doInfo = LoginAction.getDoctorInfo(Login.this,
                    temp.toString());
            LogUtils.showLog("temp===="+temp.toString());
            Log.d("tag", "doInfo====" + doInfo);

//            if(et_password.getText().toString().trim().equals("")){
//                Message message = new Message();
//                message.obj = "密码不能为空";
//                message.what = 1;
//                loginHandler.sendMessage(message);
//                proDialog.dismiss();
//                return;
//            }

            //验证用户名和密码
            if(!et_password.getText().toString().trim().equals(doInfo.getPassword())){
                Message message = new Message();
                message.obj = "用户名或者密码错误";
                message.what = 1;
                loginHandler.sendMessage(message);
                proDialog.dismiss();
                return;
            }
            //如果密码正确
            if(et_password.getText().toString().trim().equals(doInfo.getPassword())){
                //判断记住密码的复选框是否选中
                if(cb_remeber_password.isChecked()){
                    //保存密码到sp
                    SharedPreferenceTools.saveString(Login.this,et_username.getText().toString().trim(),et_password.getText().toString().trim());
                }else{
                    SharedPreferenceTools.removeString(Login.this,et_username.getText().toString().trim());
                }
            }

            Map<String, Object> mapks = new HashMap<String, Object>();
            mapks.put("ysdm", et_username.getText().toString());
            JSONObject tempks = new JSONObject(mapks);
            List<DeptWardMapInfo> deptWardMapInfos = LoginAction.getDeptWard(
                    Login.this, tempks.toString());

            Log.d("tag", "deptWardMapInfos=====" + deptWardMapInfos.size());
            if (GlobalCache.getCache().getBqSel() == -1) {
                GlobalCache.getCache().setBqSel(0);
            }

            DeptWardMapInfo dept = deptWardMapInfos.get(GlobalCache.getCache().getBqSel());
            Map<String, Object> mapconut = new HashMap<String, Object>();
            mapconut.put("id", et_username.getText().toString());
            mapconut.put("ksdm", dept.getKsdm());
            mapconut.put("bqdm", dept.getBqdm());
            JSONObject tempcount = new JSONObject(mapconut);

            int wjzCount = LoginAction.getRemindCount(Login.this, tempcount.toString());



            GlobalCache.getCache().setWjzcount(wjzCount);

            if (doInfo == null) {
                Message message = new Message();
                message.obj = "登入错误，请核对用户名和密码！";
                message.what = 1;
                loginHandler.sendMessage(message);
            } else {
                // 登录成功处理
                if (true) {
                    // 获得登陆人信息
                    GlobalCache.getCache().setDoctor(doInfo);
                    GlobalCache.getCache()
                            .setDeptWardMapInfos(deptWardMapInfos);

                    Message message = new Message();
                    message.obj = "登录成功";
                    message.what = 0;
                    loginHandler.sendMessage(message);


                }

            }


            // 进度dialog关闭
            proDialog.dismiss();
        }
    }

    /**
     * 离线登录线程 为了加速,登录时只下载和保存医生信息
     *
     * @author liu
     */
    private class LoginLXWebservice implements Runnable {

        @Override
        public void run() {

            GlobalCache.getCache().init();
            List<TableClass> tableClass = new ArrayList<TableClass>();
            tableClass = SysConfigDao.getTableList(Login.this);
            GlobalCache.getCache().setTableClasses(tableClass);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", et_username.getText().toString());
            JSONObject temp = new JSONObject(map);
            // DoctorInfo doInfo
            // =LoginAction.getDoctorInfo(Login.this,temp.toString());
            DoctorInfo doInfo = DoctorDao.getDoctor(Login.this, et_username
                    .getText().toString(), et_password.getText().toString());
            Map<String, Object> mapks = new HashMap<String, Object>();
            mapks.put("ysdm", et_username.getText().toString());
            JSONObject tempks = new JSONObject(mapks);
            // List<DeptWardMapInfo> deptWardMapInfos =
            // LoginAction.getDeptWard(Login.this, tempks.toString());
            List<DeptWardMapInfo> deptWardMapInfos = DepartmentWardDao
                    .getbqksList(Login.this, et_username.getText().toString());

            if (doInfo == null) {
                Message message = new Message();
                message.obj = "登入错误，未下载离线数据！";
                message.what = 1;
                loginHandler.sendMessage(message);
            } else {
                // 登录成功处理
                if (true) {
                    // 获得登陆人信息
                    GlobalCache.getCache().setDoctor(doInfo);
                    GlobalCache.getCache()
                            .setDeptWardMapInfos(deptWardMapInfos);

                    Message message = new Message();
                    message.obj = "登录成功";
                    message.what = 0;
                    loginHandler.sendMessage(message);

                }
            }
            // 进度dialog关闭
            proDialog.dismiss();
        }
    }

    private class LoginHandler implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            // 访问数据库进行登录处理

            // PatientInfo PatientInfo =
            // PatientAction.getPatientinfo(Login.this,"","","");

            JSONObject obj = null;

            obj = LoginAction.login(et_username.getText().toString(),
                    et_password.getText().toString());

            if (obj == null) {
                Message message = new Message();
                message.obj = "登入错误，请检查网络！";
                message.what = 1;
                loginHandler.sendMessage(message);
            } else {
                try {
                    // 登录成功处理
                    if ("true".equals(obj.getString("success"))) {
                        // 获得登陆人信息
                        JSONObject json = obj.getJSONObject("doctor");
                        Gson gson = new Gson();
                        DoctorInfo doctorInfo = gson.fromJson(json.toString(),
                                DoctorInfo.class);
                        GlobalCache.getCache().setDoctor(doctorInfo);

                        Message message = new Message();
                        message.obj = "登录成功";
                        message.what = 0;
                        loginHandler.sendMessage(message);

                        // Intent intent = new Intent(Login.this,
                        // PatientMenu.class);
                        // startActivity(intent);

                    } else {
                        Message message = new Message();
                        message.obj = obj.getString("msg");
                        message.what = 1;
                        loginHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            // 进度dialog关闭
            proDialog.dismiss();
        }
    }

    // 管理员帐号配置IP及端口
    private void setIPandPort() {
        LayoutInflater inflater = getLayoutInflater();
        View linearlayout = inflater.inflate(R.layout.setip, null);

        setIP = (EditText) linearlayout.findViewById(R.id.et_setIP);

        setEMR = (EditText) linearlayout.findViewById(R.id.et_setemrIP);
        setHLBL = (EditText) linearlayout.findViewById(R.id.et_sethlblIP);
        setPACS = (EditText) linearlayout.findViewById(R.id.et_setpacsIP);

        setIP_confirm = (Button) linearlayout.findViewById(R.id.setIP_confirm);
        setIP_test = (Button) linearlayout.findViewById(R.id.setIP_test);
        setIP_cancel = (Button) linearlayout.findViewById(R.id.setIP_cancel);
        setIP.setText(WebUtils.HOST);
        setEMR.setText(WebUtils.EMRconfig);
        setHLBL.setText(WebUtils.NISconfig);
        setPACS.setText(WebUtils.PACSIMAGE);
        // builder = new
        // AlertDialog.Builder(this).setTitle("服务器IP及端口号").setView(linearlayout).show();
        builder = new AlertDialog.Builder(this).setView(linearlayout).show();
        builder.setCanceledOnTouchOutside(false);
        setIP_confirm.setOnClickListener(confirm);
        setIP_test.setOnClickListener(test);
        setIP_cancel.setOnClickListener(cancel);
    }


    private void setAddress() {
        LayoutInflater inflater = getLayoutInflater();
        View linearlayout = inflater.inflate(R.layout.setaddress, null);
        setWebservice = (EditText) linearlayout.findViewById(R.id.log_setwebservice);

        setWeb_confirm = (Button) linearlayout.findViewById(R.id.log_confirm);
        setWeb_cancel = (Button) linearlayout.findViewById(R.id.log_cancel);

        setWebservice.setText(GlobalCache.getCache().getUrl());
        builder = new AlertDialog.Builder(this).setView(linearlayout).show();
        builder.setCanceledOnTouchOutside(false);
        setWeb_confirm.setOnClickListener(confirmWeb);
        setWeb_cancel.setOnClickListener(cancelWeb);
    }

    // 事件 --确认
    private OnClickListener confirmWeb = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String hostweb = setWebservice.getText().toString();
            WebUtils.WEBSERVICE = hostweb;
            GlobalCache.getCache().setUrl(hostweb);
            String host=hostweb.substring(0,WebUtils.WEBSERVICE.lastIndexOf("/"));
            WebUtils.HOST=host;
            GlobalCache.getCache().setHost(host);
//            GlobalCache.getCache().setCFJLUploadUrl(host+"/upload.ashx?name=dailyrecord");
//            GlobalCache.getCache().setCFJLDownloadUrl(host+"/download.ashx?name=dailyrecord");
//            GlobalCache.getCache().setEditBookMarkUrl(host+"/upload.ashx?name=emr");
//            GlobalCache.getCache().setEditBookMarkDownloadUrl(host+"/download.ashx?name=emr");

            SharedPreferences sp = getSharedPreferences("mobile_doctouch", 0);
            Editor et = sp.edit();
            et.putString("webservice", hostweb);
            et.putString("newWebservice",hostweb);
            et.putString("newHost",host);
            et.commit();
            Toast.makeText(Login.this, "成功设置服务器IP", Toast.LENGTH_SHORT).show();
            et_username.setText("");
            et_password.setText("");
            builder.dismiss();
        }
    };


    // 事件 ---取消
    private OnClickListener cancelWeb = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            builder.dismiss();
        }
    };


    // 事件 --确认
    private OnClickListener confirm = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String hostip = setIP.getText().toString();
            String hostemr = setEMR.getText().toString();
            String hosthlbl = setHLBL.getText().toString();
            String hostpacs = setPACS.getText().toString();
            WebUtils.HOST = hostip;
            WebUtils.EMRconfig = hostemr;
            WebUtils.NISconfig = hosthlbl;
            WebUtils.PACSIMAGE = hostpacs;
            SharedPreferences sp = getSharedPreferences("mobile_doctouch", 0);
            Editor et = sp.edit();
            et.putString("host", hostip);
            et.putString("newHost",hostip);
            et.putString("hostemr", hostemr);
            et.putString("hosthlbl", hosthlbl);
            et.putString("hostpacs", hostpacs);
            et.commit();
            Toast.makeText(Login.this, "成功设置服务器IP", Toast.LENGTH_SHORT).show();
            et_username.setText("");
            et_password.setText("");
            builder.dismiss();
        }
    };
    // 事件 ---测试连接
    private OnClickListener test = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String iptext = setIP.getText().toString();
            new Thread(new TestHandler(iptext)).start();
        }
    };
    // 事件 ---取消
    private OnClickListener cancel = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            builder.dismiss();
        }
    };

    private class TestHandler implements Runnable {
        private String iptext;

        public TestHandler(String iptext) {
            // TODO Auto-generated constructor stub
            this.iptext = iptext;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            JSONObject res = LoginAction.login_test(iptext);
            try {
                if (res != null) {
                    Message message = new Message();
                    message.obj = "连接成功";
                    message.what = 1;
                    loginHandler.sendMessage(message);
                    // builder.dismiss();
                } else {
                    Message message = new Message();
                    message.obj = "连接失败";
                    message.what = 1;
                    loginHandler.sendMessage(message);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private int readHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                "mobile_doctouch", 0);
        history[0] = sharedPreferences.getString("history1", "");
        history[1] = sharedPreferences.getString("history2", "");
        history[2] = sharedPreferences.getString("history3", "");
        history[3] = sharedPreferences.getString("history4", "");
        history[4] = sharedPreferences.getString("history5", "");
        int res = 0;
        for (int i = 0; i < 5; i++) {
            if (!history[res].equals("")) {
                res++;
            } else
                break;
        }
        return res;
    }

    private boolean isInHistory(String name) {
        for (int i = 0; i < 5; i++) {
            if (name.equals(history[i]))
                return true;
        }
        return false;
    }

    private void addNewUserName(String name) {
        int i = 0;
        for (i = 0; i < 5; i++) {
            if (history[i].equals("") || i == 4) {
                break;
            }
        }
        for (int j = 1; j <= i; j++) {
            history[j - 1] = history[j];
        }
        history[i] = name;
        saveHistory();
    }

    private void saveHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                "mobile_doctouch", 0);
        Editor edit = sharedPreferences.edit();
        edit.putString("history1", history[0]);
        edit.putString("history2", history[1]);
        edit.putString("history3", history[2]);
        edit.putString("history4", history[3]);
        edit.putString("history5", history[4]);
        edit.commit();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Thread getbqksThread = new Thread(new Logingetbqks());
            getbqksThread.start();

        }

        // }

        return false;
    }

    private class Logingetbqks implements Runnable {

        @Override
        public void run() {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("ysdm", et_username.getText().toString());
            JSONObject temp = new JSONObject(map);
            List<DeptWardMapInfo> deptWardMapInfos = null;

            if (!SystemUtil.isConnect(Login.this)) {
                deptWardMapInfos = DepartmentWardDao.getbqksList(Login.this, et_username.getText().toString());
            } else {
                deptWardMapInfos = LoginAction.getDeptWard(Login.this, temp.toString());
            }
            LogUtils.showLog("Login---deptWardMapInfos.size==="+deptWardMapInfos.size());
            GlobalCache.getCache().setDeptWardMapInfos(deptWardMapInfos);

            System.out.println(deptWardMapInfos == null);


            if (!SystemUtil.isConnect(Login.this)) {
                if (deptWardMapInfos == null || deptWardMapInfos.size() == 0) {
                    Message message = new Message();
                    message.obj = "登入错误，此用户未下载离线数据！";
                    message.what = 1;
                    loginHandler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.obj = "查询科室成功";
                    message.what = 3;
                    loginHandler.sendMessage(message);
                }
            } else {
                if (deptWardMapInfos == null) {
                    Message message = new Message();
                    message.obj = "登入错误，请查看本机是否可以访问服务器！";
                    message.what = 1;
                    loginHandler.sendMessage(message);
                } else if (deptWardMapInfos.size() == 0) {
                    Message message = new Message();
                    message.obj = "此用户未设置指定病区！";
                    message.what = 1;
                    loginHandler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.obj = "查询科室成功";
                    message.what = 3;
                    loginHandler.sendMessage(message);
                }
            }


            // 进度dialog关闭
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!et_username.getText().toString().trim().equals("")){
            setPassword(et_username.getText().toString().trim());
        }
    }





}
