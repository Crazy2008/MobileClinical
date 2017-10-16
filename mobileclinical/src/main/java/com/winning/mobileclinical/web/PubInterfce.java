package com.winning.mobileclinical.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winning.mobileclinical.action.UtilsAction;
import com.winning.mobileclinical.db.dao.BfDao;
import com.winning.mobileclinical.db.dao.CommonJsonDao;
import com.winning.mobileclinical.db.dao.NoteDao;
import com.winning.mobileclinical.db.dao.SysConfigDao;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.NameValue;
import com.winning.mobileclinical.model.cis.BedSearchinfo;
import com.winning.mobileclinical.model.cis.Bedinfo;
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatDailyRecordInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.model.cis.PickerInfo;
import com.winning.mobileclinical.model.cis.TableClass;
import com.winning.mobileclinical.utils.LogUtils;
import com.winning.mobileclinical.widget.DoubleDatePickerDialog;
import com.winning.mobileclinical.widget.DoubleDatePickerDialog.OnDateSetListener;
import net.sf.json.JSONSerializer;
public abstract class PubInterfce {
    Context mContext;
    DoctorInfo doctor = GlobalCache.getCache().getDoctor();
    DeptWardMapInfo deptWardMapInfo = GlobalCache.getCache().getDeptWardMapInfos().get(GlobalCache.getCache().getBqSel());


    public PubInterfce(Context c) {
        mContext = c;
    }


    //    public  HashMap<String, String> map = new HashMap<String, String>();
    @JavascriptInterface
    public String getName() {
        return doctor.getHospital_name();
    }

    @JavascriptInterface
    public String getDoctor() {
        return "{\"id\":\"" + doctor.getId() + "\",\"name\":\"" + doctor.getName()+"\"}";
    }

    @JavascriptInterface
    public String getDept() {

        int sel = GlobalCache.getCache().getBqSel();
        deptWardMapInfo = GlobalCache.getCache().getDeptWardMapInfos().get(sel);
        return "{\"ksdm\":\"" + deptWardMapInfo.getKsdm() + "\",\"ksmc\":\"" + deptWardMapInfo.getKsmc() + "\",\"bqdm\":\"" + deptWardMapInfo.getBqdm() + "\",\"bqmc\":\"" + deptWardMapInfo.getBqmc() + "\"}";
    }

    @JavascriptInterface
    public void setDept(String ksdm, String bqdm) {

        System.out.println(ksdm + bqdm);
    }

    @JavascriptInterface
    public String getData(String key) {
        return (GlobalCache.getCache().getMap().containsKey(key) ? GlobalCache.getCache().getMap().get(key) : "");
    }

    @JavascriptInterface
    public void setData(String key, String data) {
        GlobalCache.getCache().getMap().put(key, data);
    }

    @JavascriptInterface
    public void datetimepicker(String istimeselector, String israngeselector,
                               String title, String min, String max, String begin, String end) {
        final long start = System.currentTimeMillis();
        LogUtils.showLog("PubInterface=================start"+start);
        System.out.println("datetimepicker---------------------------------");
        PickerInfo pickerInfo = new PickerInfo(istimeselector,
                israngeselector,
                title,
                min,
                max,
                begin,
                end);
        DoubleDatePickerDialog pickerDialog = new DoubleDatePickerDialog(mContext, 0, new OnDateSetListener() {

            @Override
            public void onDateSet(final String s1, final String s2) {
                // TODO Auto-generated method stub
//					"javascript:loadData('" + syxh + "','" + yexh
//					+ "')"
//					System.out.println("Thread.currentThread().getName()==="+Thread.currentThread().getName());
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        long end = System.currentTimeMillis();
                        long use=end-start;
                        LogUtils.showLog("Public==================use="+use);
                        getWebViewForSub().loadUrl("javascript:datetimepicker_callback('" + s1 + "','" + s2 + "')");
                    }
                });

            }
        }, pickerInfo, true);
        pickerDialog.show();

        // 确定
        Button btnPositive = pickerDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
        btnPositive.setBackgroundColor(Color.parseColor("#0037c2"));
        btnPositive.setTextColor(Color.WHITE);
        btnPositive.setTextSize(18.0f);
//			webView.loadUrl("javascript:datetimepicker_callback('"+str1+"','"+str2+"')");

        Button btnNegative = pickerDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE);
        btnNegative.setBackgroundColor(Color.parseColor("#FF0000"));
        btnNegative.setTextColor(Color.WHITE);
        btnNegative.setTextSize(18.0f);

    }


    public abstract WebView getWebViewForSub();


    @JavascriptInterface
    public void postData(String provider, String service, String jsonArgs) {
        String result = UtilsAction.postRemoteInfo(provider, service, jsonArgs);

        JSONObject json = null;
        try {
            json = new JSONObject(result);

            if (json.getString("success").equals("false")) {
                alert(json.getString("message"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    @JavascriptInterface
    public void alert(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public String findData(String provider, String service, String jsonArgs) throws JSONException {
        System.out.println(provider + service + jsonArgs);
        if (SystemUtil.isConnect(mContext)) {
            String result = UtilsAction.getRemoteInfo(provider, service, jsonArgs);
            JSONObject json = null;
            try {
                json = new JSONObject(result);

                if (json.getString("success").equals("false")) {
                    alert(json.getString("message"));
                }

                return json.getString("data");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {


            if ((service == "ward-bed" || service.equals("ward-bed")) && (provider == "dept" || provider.equals("dept"))) {
                List<Bedinfo> list = null;
                Gson gson = new Gson();
                list = BfDao.getJSONDATA(mContext, deptWardMapInfo.getBqdm());

                int single = SystemUtil.getJsonLB(provider, service);

                if (single == 0) {
                    System.out.println(gson.toJson(list));
                    return gson.toJson(list);
                } else {
                    return gson.toJson(list.get(0));
                }
            } else if ((service == "all-inpaitient" || service.equals("all-inpaitient")) && (provider == "patient" || provider.equals("patient"))) {
                TableClass emr_table = SysConfigDao.getValue(mContext, provider, service);
                Gson gson = new Gson();
                List<CommonJson> list = null;
                net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject
                        .fromObject(jsonArgs);
                StringBuffer sb = new StringBuffer();
                List<NameValue> sqlList = new ArrayList<NameValue>();
                for (Iterator<?> iter = jsonObject.keys(); iter.hasNext(); ) {
                    NameValue nameValue = new NameValue();
                    String key = (String) iter.next();
                    nameValue.setName(key);
                    nameValue.setValue(jsonObject.get(key).toString());
                    sqlList.add(nameValue);
                }
                if (sqlList.size() > 0) {
                    for (int i = 0; i < sqlList.size(); i++) {
                        if (i == (sqlList.size() - 1)) {
                            sb.append(sqlList.get(i).getName().toUpperCase());
                            sb.append("='" + sqlList.get(i).getValue() + "'");
                        } else {
                            sb.append(sqlList.get(i).getName().toUpperCase());
                            sb.append("='" + sqlList.get(i).getValue() + "'  and  ");
                        }
                    }
                }
//    			String sql = " SYXH=" +json.getString("syxh")+ " and " + " YEXH=" +  json.getString("yexh");


                System.out.println(provider + service + emr_table.getClassName() + emr_table.getType());

                list = CommonJsonDao.getJSONDATA(mContext, emr_table, sb.toString());
                int single = SystemUtil.getJsonLB(provider, service);

                if (single == 0) {
                    System.out.println(gson.toJson(list));
                    return gson.toJson(list);
                } else {
                    return gson.toJson(list.get(0));
                }

            } else if ((service == "patient-img" || service.equals("patient-img")) && (provider == "ris" || provider.equals("ris"))) {


            } else if ((service == "dr-pat" || service.equals("dr-pat")) && (provider == "dailyrecord" || provider.equals("dailyrecord"))) {


                List<PatDailyRecordInfo> list = null;
                Gson gson = new Gson();
                list = NoteDao.get_cfjl_menu(mContext, deptWardMapInfo.getBqdm(), deptWardMapInfo.getKsdm(), "", "");

                return gson.toJson(list);

            } else {
                TableClass emr_table = SysConfigDao.getValue(mContext, provider, service);
                Gson gson = new Gson();
                List<CommonJson> list = null;


                JSONObject json = null;
                try {
                    json = new JSONObject(jsonArgs);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

//        		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject
//    					.fromObject(jsonArgs);
//    			StringBuffer sb = new StringBuffer();
//    			List<NameValue> sqlList = new ArrayList<NameValue>();
//    			for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) {
//    				NameValue nameValue = new NameValue();
//    				String key = (String) iter.next();
//    				nameValue.setName(key);
//    				nameValue.setValue(jsonObject.get(key).toString());
//    				sqlList.add(nameValue);
//    			}
//    			if(sqlList.size()> 0) {
//    				for(int i=0;i<sqlList.size();i++) {
//    					if(i == (sqlList.size()-1)) {
//    						sb.append(sqlList.get(i).getName().toUpperCase());
//    						sb.append("='"+sqlList.get(i).getValue() + "'");
//    					} else {
//    						sb.append(sqlList.get(i).getName().toUpperCase());
//    						sb.append("='"+sqlList.get(i).getValue() + "'  and  ");
//    					}
//    				}
//    			}
                String sql = " SYXH=" + json.getString("syxh") + " and " + " YEXH=" + json.getString("yexh");


                System.out.println(provider + service + emr_table.getClassName() + emr_table.getType());

                list = CommonJsonDao.getJSONDATA(mContext, emr_table, sql);

                int single = SystemUtil.getJsonLB(provider, service);

                if (single == 0) {
                    System.out.println(gson.toJson(list));
                    return gson.toJson(list);
                } else {
                    return gson.toJson(list.get(0));
                }

            }


        }
        return "";
    }

    @JavascriptInterface
    public void switchPatient(int syxh, int yexh, String name, String sex, String age, String blh, String ryrq, String zdmc, int lcljbz) {
        //获取患者信息
        PatientInfo patientInfo = new PatientInfo();
        BigDecimal s = new BigDecimal(syxh);
        BigDecimal y = new BigDecimal(yexh);
        patientInfo.setSyxh(s);
        patientInfo.setYexh(y);
        patientInfo.setName(name);
        patientInfo.setSex(sex);
        patientInfo.setAge(age);
        patientInfo.setBlh(blh);
        patientInfo.setRyrq(ryrq);
        patientInfo.setZdmc(zdmc);
        patientInfo.setLcljbz(lcljbz);

        GlobalCache.getCache().setPatient_selected(patientInfo);
//    	Intent intent = new Intent(getActivity(), PatientMenu.class);
//		startActivityForResult(intent, 0);
    }
    //获取床位信息

    @JavascriptInterface
    public void getBedSearch(String syxh, String yexh, String name, String sex, String age, String cwdm, String blh, String zdmc, String room
            , int lcljbz, String ryrq
    ) {
        BedSearchinfo info = new BedSearchinfo();
        info.setSyxh(syxh);
        info.setYexh(yexh);
        info.setName(name);
        info.setSex(sex);
        info.setAge(age);
        info.setCwdm(cwdm);
        info.setBlh(blh);
        info.setZdmc(zdmc);
        info.setRoom(room);
        info.setLcljbz(lcljbz);
        info.setRyrq(ryrq);
//        Log.d("tag","PubInterfce   syxh=================="+syxh);
//        Log.d("tag","PubInterfce  name=================="+name);

        GlobalCache.getCache().getBedSearchinfos().add(info);

//    	Intent intent = new Intent(getActivity(), PatientMenu.class);
//		startActivityForResult(intent, 0);
    }

    @JavascriptInterface
    public int isOnline() {
        Boolean online = SystemUtil.isConnect(mContext);
        if (online) {
            return 1;
        }
        return 0;
    }

}
