package com.winning.mobileclinical.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.MenuAction;
import com.winning.mobileclinical.action.OfflineAction;
import com.winning.mobileclinical.adapter.CwhListViewAdapter;
import com.winning.mobileclinical.db.dao.CommonJsonDao;
import com.winning.mobileclinical.db.dao.SysConfigDao;
import com.winning.mobileclinical.fragment.CaseHistory;
import com.winning.mobileclinical.fragment.FragmentFactory;
import com.winning.mobileclinical.fragment.LCLJMessage;
import com.winning.mobileclinical.fragment.MedicalReportsLIS;
import com.winning.mobileclinical.fragment.MedicalReportsRIS;
import com.winning.mobileclinical.fragment.OrdersMessage;
import com.winning.mobileclinical.fragment.TempRecords;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.layout.HorizontalListView;
import com.winning.mobileclinical.layout.MenuViewPager;
import com.winning.mobileclinical.layout.MenuViewPager.OnChangeListener;
import com.winning.mobileclinical.layout.TabMenuViewPager;
import com.winning.mobileclinical.layout.TabMenuViewPager.OnTabChangeListener;
import com.winning.mobileclinical.model.MenuDTO;
import com.winning.mobileclinical.model.cis.BedSearchinfo;
import com.winning.mobileclinical.model.cis.CommonJson;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.model.cis.TableClass;
import com.winning.mobileclinical.utils.LogUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liu
 */
@SuppressLint("SimpleDateFormat")
public class PatientMenu extends PatientInfoChild {
    private final static int LOADMENU = 0;
    private final static int LOADDATA = 1;
    private final static int LOADOFFLINE = 2;
    private final static int GETOFFLINE = 3;
    private final static int SHOWMENU = 4;
    private final static int PRINTSIZE = 5;
    private LinearLayout push = null;
    public final static int EDIT_RESULT = 200;
    private ImageView tophead_back;
    private TextView top_name, top_sex, top_age, top_blh, top_zyh, top_ryrq, top_zd;
    private List<MenuDTO> menuList = null;
    private List<MenuDTO> tab_menuList = null;
    private LinearLayout ll_menu = null;
    private LinearLayout ll_head_tabmenu = null;
    private Button pi_head_bwlbtn = null;
    private List<MenuDTO> patmenus = null;
    private List<MenuDTO> tab_patmenus = null;
    private MenuViewPager menuViewPager = null;
    private TabMenuViewPager tab_menuViewPager = null;
    private PatientInfo patient = null;
    private List<BedSearchinfo> patientlist = null;
    private DoctorInfo doctor = null;
    private Button btn_select;
    private int mode = LOADMENU;
    private Fragment contentFrag;
    //下方床位号每个条目的宽
    private   int  measuredWidth=0;

    private String cfjlforward = "1";

    private FrameLayout framelayoutEMR, framelayoutOrder, framelayoutRIS, framelayoutLIS, framelayoutLCLJ, framelayoutTchart, framelayoutBWL;

    private List<CommonJson> offlist;

    private LinearLayout biv_left, biv_right;
    private HorizontalListView bListView;

    private CwhListViewAdapter cwhadapter = null;
    private int index = 0;



    private CaseHistory caseHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patientmenunew_1);

        // TODO Auto-generated method stub
        if (GlobalCache.getCache().getDoctor() != null) {
            doctor = GlobalCache.getCache().getDoctor();
            patient = GlobalCache.getCache().getPatient_selected();
//			if(patientlist==null){
            patientlist = new ArrayList<BedSearchinfo>();

            List<BedSearchinfo> bedSearchinfoList = GlobalCache.getCache().getBedSearchinfos();
            for (int i = 0; i < bedSearchinfoList.size(); i++)  //外循环是循环的次数
            {
                for (int j = bedSearchinfoList.size() - 1; j > i; j--)  //内循环是 外循环一次比较的次数
                {
                    if (bedSearchinfoList.get(i).getSyxh().equals(bedSearchinfoList.get(j).getSyxh())) {
                        bedSearchinfoList.remove(j);
                    }
                }
            }

            patientlist.addAll(bedSearchinfoList);

        }
        for (int i = 0; i < patientlist.size(); i++) {
            if (patientlist.get(i).getSyxh().trim().equals("" + patient.getSyxh())) {
                index = i;
                break;
            }
        }

        findviewbyids();
        patmenus = new ArrayList<MenuDTO>();
        tab_patmenus = new ArrayList<MenuDTO>();
        initTitle();

        Intent intent = getIntent();
        //获取数据
        cfjlforward = intent.getStringExtra("order");

        mode = LOADMENU;
        this.loadDataWithNothing();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    private void findviewbyids() {
        // TODO Auto-generated method stub
        tophead_back = (ImageView) findViewById(R.id.top_head_back);
        top_name = (TextView) findViewById(R.id.top_head_name);
        top_sex = (TextView) findViewById(R.id.top_head_sex);
        top_age = (TextView) findViewById(R.id.top_head_age);
        top_blh = (TextView) findViewById(R.id.top_head_blh);
        top_zyh = (TextView) findViewById(R.id.top_head_zyh);
        top_ryrq = (TextView) findViewById(R.id.top_head_ryrq);
        top_zd = (TextView) findViewById(R.id.top_head_zd);

        ll_head_tabmenu = (LinearLayout) findViewById(R.id.pi_ll_head_tabmenu);
        ll_menu = (LinearLayout) findViewById(R.id.pi_ll_menu);
        pi_head_bwlbtn = (Button) findViewById(R.id.pi_head_bwlbtn);

        biv_left = (LinearLayout) findViewById(R.id.bottom_head_left);
        biv_right = (LinearLayout) findViewById(R.id.bottom_head_right);
        bListView = (HorizontalListView) findViewById(R.id.bottom_head_cwhlistview);

        framelayoutEMR = (FrameLayout) findViewById(R.id.pi_fl_contentEMR);
        framelayoutBWL = (FrameLayout) findViewById(R.id.pi_fl_contentBwl);
        framelayoutOrder = (FrameLayout) findViewById(R.id.pi_fl_contentOrder);
        framelayoutRIS = (FrameLayout) findViewById(R.id.pi_fl_contentRIS);
        framelayoutLIS = (FrameLayout) findViewById(R.id.pi_fl_contentLIS);
        framelayoutLCLJ = (FrameLayout) findViewById(R.id.pi_fl_contentLCLJ);
        framelayoutTchart = (FrameLayout) findViewById(R.id.pi_fl_contentTchart);

        cwhadapter = new CwhListViewAdapter(getApplicationContext(), patientlist);
        bListView.setAdapter(cwhadapter);



        Log.d("tag", "------------------PatientMenu--index===" + index);
//		bListView.setSelected(true);
//		bListView.scrollTo(ViewUtil.pxtodip(getApplicationContext(),32*index));


        cwhadapter.setSelectIndex(index);


        Log.d("tag", "onWindowFocusChanged----------------index=" + index);
        ListAdapter adapter = bListView.getAdapter();
        if(adapter!=null){
            View view = adapter.getView(0, null, bListView);
            view.measure(0,0);
            measuredWidth = view.getMeasuredWidth();
            LogUtils.showLog("measuredWidth======="+measuredWidth);
        }
        bListView.scrollTo(measuredWidth*index);

        bListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                //当我点击的时候把角标赋给index
                index=position;

                BedSearchinfo pat = patientlist.get(position);
                PatientInfo patinfo = new PatientInfo();
                patinfo.setSyxh(BigDecimal.valueOf(Double.parseDouble(pat.getSyxh())));
                patinfo.setYexh(BigDecimal.valueOf(Double.parseDouble(pat.getYexh())));
                patinfo.setName(pat.getName());
                patinfo.setAge(pat.getAge());
                patinfo.setSex(pat.getSex());
                patinfo.setBlh(pat.getBlh());
                patinfo.setRyrq(pat.getRyrq());
                patinfo.setZdmc(pat.getZdmc());
                patinfo.setLcljbz(pat.getLcljbz());
                patinfo.setCwdm(pat.getCwdm());
                patient = patinfo;
                GlobalCache.getCache().setPatient_selected(patinfo);
                initTitle();
                int item_index = tab_menuViewPager.getCurrentItem();
                System.out.println("item_index == " + item_index);
                if (item_index == 0) {
                    CaseHistory.loadPatEMRData("" + patient.getSyxh(), "" + patient.getYexh());
                } else if (item_index == 1) {
                    OrdersMessage.loadPatOrderData("" + patient.getSyxh(), "" + patient.getYexh());
                } else if (item_index == 2) {
                    MedicalReportsRIS.loadPatRISData("" + patient.getSyxh(), "" + patient.getYexh());
                } else if (item_index == 3) {
                    MedicalReportsLIS.loadPatLISData("" + patient.getSyxh(), "" + patient.getYexh());
                } else if (item_index == 4) {
                    TempRecords.loadPatTempData("" + patient.getSyxh(), "" + patient.getYexh());
                } else if (item_index == 8888) {
//					System.out.println("刷新备忘录");
//					FragmentTransaction ts = PatientMenu.this.getSupportFragmentManager()
//							.beginTransaction();
//					ts.add(R.id.pi_fl_contentBwl, contentFrag);
//					ts.addToBackStack(null);
//					ts.commit();


                    pi_head_bwlbtn.performClick();
                }

                cwhadapter.setSelectIndex(position);
//				cwhadapter.notifyDataSetInvalidated();
                cwhadapter.notifyDataSetChanged();


            }

        });


        tophead_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("order", "0");
                intent.putExtra("text", "患者列表");
                intent.putExtra("intent", "com.winning.mobileclinical.fragment.PatientList");
                intent.putExtra("imageurl", "patient");
                setResult(200, intent);

//                GlobalCache.getCache().getBedSearchinfos().clear();
//                patientlist.clear();
//                index = 0;
//                GlobalCache.getCache().init2();
                finish();
            }
        });

        pi_head_bwlbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                MenuDTO menuDTO = new MenuDTO();
                menuDTO.setOrderby(8);
//                menuDTO.setIntent("com.winning.mobileclinical.fragment.DoctorNote");
                menuDTO.setIntent("com.winning.mobileclinical.fragment.CFJLFragment");
                switchTabMenu(menuDTO);

                tab_menuViewPager.setCurrentItem(8888);

//				if(tab_menuViewPager != null) {
//					tab_menuViewPager.selectTabButtonBwl();
//					
////					tab_menuViewPager.selectTabButton2();
//				}
            }
        });


    }

    private void initTitle() {
        top_name.setText(patient.getName());
        top_age.setText(patient.getAge());
        top_sex.setText(patient.getSex());
        top_blh.setText(patient.getBlh());
//		top_zyh.setText(patient.getBlh());
        top_zyh.setText(patient.getCwdm());
        top_ryrq.setText(patient.getRyrq());
        top_zd.setText(patient.getZdmc());
    }

    private void ShowMenu(int select) {
        if (menuViewPager == null) {
            menuViewPager = new MenuViewPager(this);
        } else {
            menuViewPager.removeAllMenus();
        }
        ll_menu.removeAllViews();
        if (select == 0) {
            menuViewPager.addMenus(patmenus);
        } else {
//			menuViewPager.addMenus(docmenus);
        }
        ll_menu.addView(menuViewPager);
        menuViewPager.setOnItemChangeListener(new OnChangeListener() {

            @Override
            public void onPageChangeListener(MenuDTO menuDTO) {
                // TODO Auto-generated method stub
//				switchMenu(menuDTO);
                backmenu(menuDTO);
            }
        });

//		menuViewPager.setCurrentItem(0);

        if (tab_menuViewPager == null) {
            tab_menuViewPager = new TabMenuViewPager(this);
        } else {
            tab_menuViewPager.removeAllMenus();
        }
        ll_head_tabmenu.removeAllViews();
        if (select == 0) {
            tab_menuViewPager.addMenus(tab_patmenus, pi_head_bwlbtn.getWidth());
        } else {
//			menuViewPager.addMenus(docmenus);
        }
        ll_head_tabmenu.addView(tab_menuViewPager);
        tab_menuViewPager.setOnTabChangeListener(new OnTabChangeListener() {
            @Override
            public void onTabPageChangeListener(MenuDTO menuDTO) {
                // TODO Auto-generated method stub
                switchTabMenu(menuDTO);
            }
        });

        if (cfjlforward.equals("8")) {
            MenuDTO menuDTO = new MenuDTO();

            menuDTO.setOrderby(8);
            menuDTO.setIntent("com.winning.mobileclinical.fragment.CFJLFragment");
//            menuDTO.setIntent("com.winning.mobileclinical.fragment.DoctorNote");
            switchTabMenu(menuDTO);

            tab_menuViewPager.setCurrentItem(8888);
        } else {

            tab_menuViewPager.setCurrentItem(0);
        }

    }


    private void backmenu(MenuDTO menuDTO) {

        int order = menuDTO.getOrderby();
        Intent intent = new Intent();
        intent.putExtra("order", order + "");
        intent.putExtra("text", menuDTO.getText());
        intent.putExtra("intent", menuDTO.getIntent());
        intent.putExtra("imageurl", menuDTO.getImageurl());
        setResult(200, intent);
        GlobalCache.getCache().init2();
        finish();
//		if(menuDTO.getOrderby() == 0) {  //患者列表
//
//			int order = menuDTO.getOrderby();
//			Intent intent = new Intent();
//            intent.putExtra("order", order+"");
//            intent.putExtra("text", menuDTO.getText());
//            intent.putExtra("intent", menuDTO.getIntent());
//            intent.putExtra("imageurl", menuDTO.getImageurl());
//            setResult(200, intent);
//            finish();
//		} else if(menuDTO.getOrderby() == 1) {  //关注患者
////			  //患者列表
////			Intent intent = new Intent();
////            Bundle bundle = new Bundle();
////            bundle.putSerializable("menu", menuDTO);
////            intent.putExtras(bundle);
////            setResult(200, intent);
////            finish();
//			int order = menuDTO.getOrderby();
//			Intent intent = new Intent();
//            intent.putExtra("order", order+"");
//            intent.putExtra("text", menuDTO.getText());
//            intent.putExtra("intent", menuDTO.getIntent());
//            intent.putExtra("imageurl", menuDTO.getImageurl());
//            setResult(200, intent);
//            finish();
//		} else if(met) {
//			int order = menuDTO.getOrderby();
//			Intent intent = new Intent();
//	        intent.putExtra("order", order+"");
//	        intent.putExtra("text", menuDTO.getText());
//	        intent.putExtra("intent", menuDTO.getIntent());
//	        intent.putExtra("imageurl", menuDTO.getImageurl());
//	        setResult(200, intent);
//	        finish();
//	    }
    }

    private void switchTabMenu(MenuDTO menuDTO) {
        Fragment frag = null;
        if (menuDTO.getIntent() != null) {
            frag = FragmentFactory.getFragment(menuDTO.getIntent());
        }
        if (frag != null) {
            contentFrag = frag;

            if (menuDTO.getOrderby() == 1) {   //医嘱
                framelayoutEMR.setVisibility(View.GONE);
                framelayoutOrder.setVisibility(View.VISIBLE);
                framelayoutRIS.setVisibility(View.GONE);
                framelayoutBWL.setVisibility(View.GONE);
                framelayoutLIS.setVisibility(View.GONE);
                framelayoutLCLJ.setVisibility(View.GONE);
                framelayoutTchart.setVisibility(View.GONE);
                if (!GlobalCache.getCache().getLoadOrder()) {
                    FragmentTransaction ts= ts = getSupportFragmentManager().beginTransaction();
                    ts.replace(R.id.pi_fl_contentOrder, contentFrag);
                    ts.addToBackStack(null);
                    ts.commit();
                } else {
                    OrdersMessage.loadJsData();
                }

            } else if (menuDTO.getOrderby() == 8) {  //备忘录
                //医嘱
                framelayoutEMR.setVisibility(View.GONE);
                framelayoutOrder.setVisibility(View.GONE);
                framelayoutRIS.setVisibility(View.GONE);
                framelayoutBWL.setVisibility(View.VISIBLE);
                framelayoutLIS.setVisibility(View.GONE);
                framelayoutLCLJ.setVisibility(View.GONE);
                framelayoutTchart.setVisibility(View.GONE);

                FragmentTransaction ts= ts = getSupportFragmentManager().beginTransaction();
                ts.replace(R.id.pi_fl_contentBwl, contentFrag,"cfjlFragment");
                ts.addToBackStack(null);
                ts.commit();
            } else if (menuDTO.getOrderby() == 2) {   //检查
                framelayoutEMR.setVisibility(View.GONE);
                framelayoutOrder.setVisibility(View.GONE);
                framelayoutRIS.setVisibility(View.VISIBLE);
                framelayoutBWL.setVisibility(View.GONE);
                framelayoutLIS.setVisibility(View.GONE);
                framelayoutLCLJ.setVisibility(View.GONE);
                framelayoutTchart.setVisibility(View.GONE);

                if (!GlobalCache.getCache().getLoadRIS()) {
                    FragmentTransaction ts= ts = getSupportFragmentManager().beginTransaction();
                    ts.replace(R.id.pi_fl_contentRIS, contentFrag);
                    ts.addToBackStack(null);
                    ts.commit();
                } else {
                    MedicalReportsRIS.loadJsData();
                }
            } else if (menuDTO.getOrderby() == 3) {   //检验
                framelayoutEMR.setVisibility(View.GONE);
                framelayoutOrder.setVisibility(View.GONE);
                framelayoutRIS.setVisibility(View.GONE);
                framelayoutBWL.setVisibility(View.GONE);
                framelayoutLIS.setVisibility(View.VISIBLE);
                framelayoutLCLJ.setVisibility(View.GONE);
                framelayoutTchart.setVisibility(View.GONE);

                if (!GlobalCache.getCache().getLoadLIS()) {
                    FragmentTransaction ts= ts = getSupportFragmentManager().beginTransaction();
                    ts.replace(R.id.pi_fl_contentLIS, contentFrag);
                    ts.addToBackStack(null);
                    ts.commit();
                } else {
                    MedicalReportsLIS.loadJsData();
                }
            } else if (menuDTO.getOrderby() == 4) {   // 路径
                framelayoutEMR.setVisibility(View.GONE);
                framelayoutOrder.setVisibility(View.GONE);
                framelayoutRIS.setVisibility(View.GONE);
                framelayoutLIS.setVisibility(View.GONE);
                framelayoutBWL.setVisibility(View.GONE);
                framelayoutLCLJ.setVisibility(View.VISIBLE);
                framelayoutTchart.setVisibility(View.GONE);

                if (!GlobalCache.getCache().getLoadLCLJ()) {
                    FragmentTransaction ts= ts = getSupportFragmentManager().beginTransaction();
                    ts.replace(R.id.pi_fl_contentLCLJ, contentFrag);
                    ts.addToBackStack(null);
                    ts.commit();
                } else {
                    LCLJMessage.loadJsData();
                }
            } else if (menuDTO.getOrderby() == 5) {   //体温单
                framelayoutEMR.setVisibility(View.GONE);
                framelayoutOrder.setVisibility(View.GONE);
                framelayoutRIS.setVisibility(View.GONE);
                framelayoutLIS.setVisibility(View.GONE);
                framelayoutLCLJ.setVisibility(View.GONE);
                framelayoutBWL.setVisibility(View.GONE);
                framelayoutTchart.setVisibility(View.VISIBLE);
                if (!GlobalCache.getCache().getLoadTchart()) {
                    FragmentTransaction ts= ts = getSupportFragmentManager().beginTransaction();
                    ts.replace(R.id.pi_fl_contentTchart, contentFrag);
                    ts.addToBackStack(null);
                    ts.commit();
                } else {
                    TempRecords.loadJsData();
                }
            } else {   //病历资料
                framelayoutEMR.setVisibility(View.VISIBLE);
                framelayoutOrder.setVisibility(View.GONE);
                framelayoutRIS.setVisibility(View.GONE);
                framelayoutLIS.setVisibility(View.GONE);
                framelayoutBWL.setVisibility(View.GONE);
                framelayoutLCLJ.setVisibility(View.GONE);
                framelayoutTchart.setVisibility(View.GONE);

                if (!GlobalCache.getCache().getLoadEMR()) {
                    FragmentTransaction ts= ts = getSupportFragmentManager().beginTransaction();
                    ts.replace(R.id.pi_fl_contentEMR, contentFrag);
                    ts.addToBackStack(null);
                    ts.commit();
                } else {
                    CaseHistory.loadJsData();
                }
            }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onStop() {
        super.onStop();


    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.setEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return false;

        }

        @SuppressLint("NewApi")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          String url) {
            // 非超链接(如Ajax)请求无法直接添加请求头，现拼接到url末尾,这里拼接一个imei作为示例
            // String ajaxUrl = url;
            // // 如标识:req=ajax
            // if (url.contains("req=ajax"))
            // {
            //
            // }
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

    @Override
    public void switchPatient() {

    }

    @Override
    protected void loadDate() {
        // TODO Auto-generated method stub
        // if(GlobalCache.getCache().getPatient_selected() != null) {
        // patient = GlobalCache.getCache().getPatient_selected();
        // doctor=GlobalCache.getCache().getDoctor();
        //
        // }

        if (mode == LOADMENU) {
            menuList = MenuAction.getMenuListPatient(PatientMenu.this, patient);
            tab_menuList = MenuAction.getTabMenuList(PatientMenu.this, patient);
            mode = SHOWMENU;
        } else if (mode == LOADOFFLINE) {
            OfflineAction.startload(PatientMenu.this, GlobalCache.getCache().getDoctor().getId(), null, "", "", true);
        } else if (mode == GETOFFLINE) {
            List<TableClass> order_table = SysConfigDao.getSelect(PatientMenu.this, "order", "ward-order");
            offlist = CommonJsonDao.getJSONDATA(PatientMenu.this, order_table);
            mode = PRINTSIZE;
        }


//		if (menuList == null) {
//			menuList = new ArrayList<MenuDTO>();
//		}
    }

    @Override
    protected void afterLoadData() {
        // TODO Auto-generated method stub
        if (mode == SHOWMENU) {
            if (patmenus != null)
                patmenus.clear();
            for (int i = 0; i < menuList.size(); i++) {
                patmenus.add(menuList.get(i));

            }
            if (tab_patmenus != null)
                tab_patmenus.clear();
            for (int j = 0; j < tab_menuList.size(); j++) {
                tab_patmenus.add(tab_menuList.get(j));
            }
            ShowMenu(0);
        } else if (mode == PRINTSIZE) {
            System.out.println("orderlength==" + offlist.size());
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("order", "0");
            intent.putExtra("text", "患者列表");
            intent.putExtra("intent", "com.winning.mobileclinical.fragment.PatientList");
            intent.putExtra("imageurl", "patient");
            setResult(200, intent);
            GlobalCache.getCache().getBedSearchinfos().clear();
            patientlist.clear();
            index = 0;
            GlobalCache.getCache().init2();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
