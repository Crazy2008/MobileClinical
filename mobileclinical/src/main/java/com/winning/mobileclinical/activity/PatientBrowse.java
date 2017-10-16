package com.winning.mobileclinical.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.LoginAction;
import com.winning.mobileclinical.action.MenuAction;
import com.winning.mobileclinical.adapter.BqksMapAdapter;
import com.winning.mobileclinical.fragment.FragmentFactory;
import com.winning.mobileclinical.fragment.OperationList;
import com.winning.mobileclinical.fragment.PatientList;
import com.winning.mobileclinical.fragment.RemindList;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.layout.CornerListView;
import com.winning.mobileclinical.layout.MenuViewPager;
import com.winning.mobileclinical.layout.MenuViewPager.OnChangeListener;
import com.winning.mobileclinical.model.MenuDTO;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.utils.FirstLetterUtil;
import com.winning.mobileclinical.utils.KeyboardUtil;
import com.winning.mobileclinical.utils.LogUtils;
import com.winning.mobileclinical.utils.PollingService;
import com.winning.mobileclinical.utils.PollingUtils;
import com.winning.mobileclinical.utils.SharedPreferenceTools;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.SystemUtil;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

/**
 * 病人列表页面
 * 统计显示页面
 * @author liu
 *implements  OnGestureListener 
 */
public class PatientBrowse extends PatientInfoChild implements  OnGestureListener  {
	final static int LOADMENU = 0;
	final static int SHOWMENU= 4;
	final static int LOADOFFLINEPAT = 1;
	final static int LOADPATLIST = 3;
	final static int IMPORTPATIENRTID = 10;  //关注病人刷新
	private static int loadMode = LOADPATLIST;
	private TextView tv_docname, tv_bqks;	//头上显示的医生信息
	private LinearLayout pb_tv_bqkslayout = null;
	private Button pb_tv_bqksselect = null;
	private BqksMapAdapter bqksMapAdapter = null;
	private CheckBox tv_import;
	private GridView gridView;				//显示病人list
	private EditText etSearch;
	private ImageView deleteText;
	private Button searchBut;
	private List<PatientInfo> patientList = null;	//病人列表
	private List<PatientInfo> patientSelectList = null; //病人检索
	
	private List<DeptWardMapInfo> bqlist = null; //病区科室list
	
	private PopupWindow bqListWindow = null;	//病区列表选择使用窗口
	private PopupWindow popupWindow = null;	//层
	private PopupWindow bqksMapWindow = null;
	private DeptWardMapInfo departmentandward = null;

	private ImageButton pb_setting = null;

	private DoctorInfo doctor = null;
//	private List<Offline_Setting> xzbrList = new ArrayList<Offline_Setting>();
	private String errorMsg = "";
	private static final int FLING_MIN_DISTANCE = 100;  
	
	private List<MenuDTO> menuList = null;
	private static LinearLayout ll_menu = null;
	private static List<MenuDTO> patmenus = null;
	private static MenuViewPager menuViewPager = null;
	private Fragment contentFrag;
	private FrameLayout framelayout,framelayoutImport,framelayoutOperation,framelayoutTX,framelayoutWD;
	private KeyboardUtil keyboardUtil = null;
	private KeyboardView keyboardView = null;
	private RelativeLayout relativeLayout = null;
	private RelativeLayout relativeLayout_top = null;
	private RelativeLayout relativeLayout_wdtop = null;
	private LinearLayout wd_menu = null;
	
	private LinearLayout pi_ll_content = null;
	
	private LinearLayout wdxz_layout = null,wdsh_layout = null,wdsz_layout = null,wdbb_layout = null,wdyj_layout = null;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	
	static Context context;
	
	Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//		SystemUtil.getConnect(PatientBrowse.this);
		setContentView(R.layout.patientbrowsenew_1); 

		doctor = GlobalCache.getCache().getDoctor();
		patmenus = new ArrayList<MenuDTO>();
		findViewsById();
		findViewsByIdWD();
//		showSearch();
		showMenu();
		showTitle();
		initKeyboard();

		if(!SystemUtil.isConnect(PatientBrowse.this)){
			if(timer!=null){
				timer.cancel();
			}
		}else{
			timer = new Timer(true);
			timer.schedule(task,1*1000, 60*1000); //延时1000ms后执行，1000ms执行一次
		}

		context = this;
		
		bqlist = GlobalCache.getCache().getDeptWardMapInfos();

		departmentandward = bqlist.get(GlobalCache.getCache().getBqSel());

//		tv_bqks.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				initBqListWindow();
//			}
//		});
		
		tv_bqks.setText(departmentandward.getBqmc()+" | "+departmentandward.getKsmc());

		pb_tv_bqkslayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initBqksMapWindow();
			}
		});
		
		pb_tv_bqksselect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				initBqksMapWindow();
			}
		});
		registerMessageReceiver();
		loadMode = LOADMENU;
		this.loadDataWithNothing();
		//开始下载病区列表
//		startDownloading(LOADPATLIST);
	//	showGridView();
		//设定离线的按钮点击效果
//		pb_setting.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				
//			}
//		});
		
//		tv_import.setChecked(new onc)
		
//		tv_import.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				System.out.println(isChecked);
//				if(isChecked){
////					showFilterImportPanient();
//				}else{
//					showGridView(patientList);
//				}
//			}
//		});

//		registerMessageReceiver();
	}
	
	@Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //Stop polling service  
        System.out.println("Stop polling service...");  
        ll_menu.removeAllViews();
        PollingUtils.stopPollingService(this, PollingService.class, PollingService.ACTION);
		unregisterReceiver(NetworkStateReceiver.getNetWorkReceiver());
    }
	
	/**
	 * 键盘处理
	 */
	private void initKeyboard() {
		// TODO Auto-generated method stub
		System.out.println("sdk版本"+android.os.Build.VERSION.SDK_INT);
//		etSearch.setInputType(InputType.TYPE_NULL);  
		
		
		if (android.os.Build.VERSION.SDK_INT <= 10) {//4.0以下 danielinbiti  
			etSearch.setInputType(InputType.TYPE_NULL);  
         } else {  
//        	 etSearch.setInputType(InputType.TYPE_NULL); 
        	 String methodName = null;
        	 
        	 if(android.os.Build.VERSION.SDK_INT >= 16){
                 // 4.2
                 methodName = "setShowSoftInputOnFocus";
             }
             else if(android.os.Build.VERSION.SDK_INT >= 14){
                 // 4.0
                 methodName = "setSoftInputShownOnFocus";
             }
        	 
        	 
        	 PatientBrowse.this.getWindow().setSoftInputMode(  
                     WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  
             try {  
                 Class<EditText> cls = EditText.class;  
                 Method setShowSoftInputOnFocus;  
                 setShowSoftInputOnFocus = cls.getMethod(methodName,  
                         boolean.class);  
                 setShowSoftInputOnFocus.setAccessible(true);  
                 setShowSoftInputOnFocus.invoke(etSearch, false);  
             } catch (Exception e) {  
                 e.printStackTrace();  
             }   
         }  
		
//		 InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE); 
//         imm.hideSoftInputFromWindow(etSearch.getWindowToken(),0); 
		
		
		etSearch.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				keyboardUtil = new KeyboardUtil(PatientBrowse.this, keyboardView, etSearch, KeyboardUtil.MODE_MAINPAGE);
				keyboardUtil.showKeyboard();				
				return false;
				
			}
		});
		
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				PatientList.search(s.toString());
				}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
			});
	}
	
	
	protected void initSettingWindow() {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		
//		SettingDialog settingDialog = new SettingDialog(this,dm.heightPixels,dm.widthPixels);
//		settingDialog.patientListShow(patientList);
//		settingDialog.setOnOKDismissListener(new OnOKDismissListener() {
//			
//			@Override
//			public void onOKDismiss(List<Offline_Setting> brlist) {
//				// TODO Auto-generated method stub
//				
//				for(int i=0;i<brlist.size();i++) {
//					if(brlist.get(i).getIsoff() == 1) {
//						
//						xzbrList.add(brlist.get(i));
//					}
//				}
//				
//				if(xzbrList.size() > 0 )
//				{
//					startDownloading(LOADOFFLINEPAT);
//				}
//				else {
//					MessageUtils.showToast(PatientBrowse.this, "请选择需要下载的病人!");
//				}
//			}
//		});
	}
	
	/**
	 * 键盘处理
	 */
//	private void initKeyboard() {
//		// TODO Auto-generated method stub
//		etSearch.setInputType(InputType.TYPE_NULL);
//		etSearch.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
//				
//				if(GlobalCache.getCache().getJdyy().equals("1")){
//					keyboardUtil = new KeyboardUtil(PatientBrowse.this, keyboardView, etSearch, KeyboardUtil.MODE_MAINPAGE_JDYY);
//				}else{
//					keyboardUtil = new KeyboardUtil(PatientBrowse.this, keyboardView, etSearch, KeyboardUtil.MODE_MAINPAGE);
//				}
//				
//				keyboardUtil.showKeyboard();				
//				return false;
//				
//			}
//		});
//	}
	

	/**
	 * 患者快速检索
	 */
	
	private void showSearch(){
		patientSelectList = new ArrayList<PatientInfo>();
		deleteText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showGridView(patientList);
				etSearch.setText("");
			}
		});
		
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					String Regx="^[A-Za-z0-9]+";
					String idReg="^[0-9]+";
					if(Pattern.matches(Regx, s)){   //字母或数字正则匹配
						if(patientSelectList!=null){
							patientSelectList.clear();
							if(Pattern.matches(idReg, s)){   //数字检索床位代码
                            	for(int i = 0; i<patientList.size(); i++){
    								String cwdm = patientList.get(i).getCwdm();
    								Pattern cwdmMatcher = Pattern.compile(s.toString(), Pattern.CASE_INSENSITIVE);
    								if(cwdmMatcher.matcher(cwdm).find()){
    									patientSelectList.add(patientList.get(i));
    								}
    							}
                            }else{  //字母检索患者姓名拼音首字母
                            	for(int i = 0; i<patientList.size(); i++){
                            		if(patientList.get(i).getName()==null)
                            			continue;
    								String firstLetters = FirstLetterUtil.getFirstLetter(patientList.get(i).getName());
    								Pattern firstLetterMatcher = Pattern.compile(s.toString(), Pattern.CASE_INSENSITIVE);
    								if(firstLetterMatcher.matcher(firstLetters).find()){
    									patientSelectList.add(patientList.get(i));
    								}
    							}
                            }
						}
						
						if(patientSelectList!=null){
							showGridView(patientSelectList);
						}
					}else{
						Toast.makeText(PatientBrowse.this,"请输入拼音首字母或数据！",Toast.LENGTH_SHORT).show();
						etSearch.setText("");
					}
				}else{
					showGridView(patientList);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() == 0) {  
					deleteText.setVisibility(View.GONE);  
                } else {  
                	deleteText.setVisibility(View.VISIBLE);  
                }  
			}
		});
	}
	
	
	
	private void showTitle() {
		// TODO Auto-generated method stub
		//设置数据
		DoctorInfo doctor = GlobalCache.getCache().getDoctor();
		if( doctor != null )
		{
//			tv_docname.setText(doctor.getKsmc() + " | " + 
//					doctor.getName());
			
//			tv_docname.setText(doctor.getName());

			
		}
		/*
		bqlist = GlobalCache.getCache().getBqlist();
		if(bqlist !=null && bqlist.size()>0)
		{
			tv_bqks.setText(bqlist.get(0).getWardName()+" | "+bqlist.get(0).getDepartmentName());
			tv_bqks.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					initBqListWindow();
				}
			});
			departmentandward = bqlist.get(0);
			switchWard();
		}
		*/
	}
	/**
	 * 显示主页病人的列表
	 * @param plist
	 */
	private void showGridView(List<PatientInfo> plist) {
		// TODO Auto-generated method stub
//		if(plist == null)
//			plist = new ArrayList<PatientInfo>();
//		
////		HISPatientGridAdapter adapter = new HISPatientGridAdapter(PatientBrowse.this, plist);  //his去数据 儿童医院需求
//		
//		gridView.setAdapter(adapter);
//		gridView.setOnItemClickListener(new MyOnItemClickListener(plist));
//		gridView.setOnTouchListener(new OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				if(keyboardUtil != null) {
//					keyboardUtil.hideKeyboard();
//				}
//				return false;
//			}
//		});
		//统计右侧部分信息
		
	}
	
	
	/**
	 * 病人列表点击打开病人详细页面
	 * @author liu
	 *
	 */
	class MyOnItemClickListener implements OnItemClickListener{
		private List<PatientInfo> plist = null;
		public MyOnItemClickListener(List<PatientInfo> plist2) {
			// TODO Auto-generated constructor stub
			this.plist = plist2;
		}
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
//			GlobalCache.getCache().setPatient_selectedByxh(plist.get(arg2).getSyxh(), plist.get(arg2).getYexh());
//			GlobalCache.getCache().setIsoffline_select(false);
			
			if(plist.get(arg2).getSyxh().equals("0")) {
				
			} else {
				if(doctor != null)
				{
						
					Intent intent = new Intent(PatientBrowse.this, PatientMenu.class);
					startActivityForResult(intent, 0);
					
				}
			}
			
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == SCANNIN_GREQUEST_CODE) {
			if(resultCode == RESULT_OK){
				if(data == null){
	        		return;
	        	}
				Bundle bundle = data.getExtras();
				String result = bundle.getString("result");
				
//				System.out.println("二维码返回值"+result);
				
				
				PatientList.scanCode(result);
				
				int index = -1;
//				for(int i=0;i<patientList.size();i++){
//					if(result.equals(patientList.get(i).getSyxh())){
//						index = i;
//					}
//				}
//				if(index>0){
////					GlobalCache.getCache().setPatient_selectedByxh(patientList.get(index).getSyxh(), patientList.get(index).getYexh());
//					PatientList.switchCFPatient("", "");
//					Intent intent = new Intent(PatientBrowse.this, PatientMenu.class);
//					startActivityForResult(intent, 0);
//				}else{
//				}
//				Toast.makeText(PatientBrowse.this, "未找到该患者！", Toast.LENGTH_LONG).show();
			}
		} else {
			MenuDTO menuDTO = new MenuDTO();
			menuDTO.setOrderby(Integer.valueOf(data.getStringExtra("order")));
			menuDTO.setText(data.getStringExtra("text"));
			menuDTO.setImageurl(data.getStringExtra("imageurl"));
			menuDTO.setIntent(data.getStringExtra("intent"));
			
			menuViewPager.setCurrentItem(Integer.valueOf(data.getStringExtra("order")));
			
			if(keyboardUtil != null) {
				keyboardUtil.hideKeyboard();
				PatientList.search("");
				etSearch.setText("");
			}
			
			switchMenu(menuDTO);
		}
		
		
		
		
		
			
	}
	
	class insertImporInfo implements Runnable {
		@Override
		public void run() {
//			importpatientId = PatientAction.getImportPatient(getApplicationContext(),
//					departmentandward.getDepartmentId(),departmentandward.getWardId());
			
		}
	}
	
	private void showMenu() {
		
//		menu_ssapbtn.setOnClickListener(new ShowPopWindow("手术查询"));
		
	}
	private void findViewsById() {
		// TODO Auto-generated method stub
		//获得控件
//		tv_docname = (TextView)findViewById(R.id.pb_tv_docname);
//		
//		tv_import = (CheckBox)findViewById(R.id.pb_tv_import);


		TextView doctorName= (TextView) findViewById(R.id.doctorName);
		doctorName.setText("您好,"+doctor.getName());


		TextView name= (TextView) findViewById(R.id.name);
		//设置logo和文字之间的距离
		name.setCompoundDrawablePadding(4);
		pb_tv_bqkslayout = (LinearLayout) findViewById(R.id.pb_tv_bqkslayout);
		tv_bqks = (TextView)findViewById(R.id.pb_tv_bqks);
		pb_tv_bqksselect = (Button) findViewById(R.id.pb_tv_bqksselect);
		keyboardView = (KeyboardView)findViewById(R.id.pb_keyboard_view);
		//病人gridview 加点击消息
//		gridView = (GridView)findViewById(R.id.pgridview);
//		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		relativeLayout_top = (RelativeLayout)findViewById(R.id.pb_top);
		relativeLayout_wdtop = (RelativeLayout)findViewById(R.id.pb_wdtop);
		
		wd_menu = (LinearLayout) findViewById(R.id.wd_menu);
		
		pi_ll_content =  (LinearLayout) findViewById(R.id.pi_ll_content);
		etSearch = (EditText)findViewById(R.id.etSearch);
		//华为平板设置永远不获取焦点，防止软键盘弹出
		
		if(android.os.Build.VERSION.SDK_INT >= 22) {
			
			etSearch.setFocusable(false);
		}
		
		
		
		// 新建一个可以添加属性的文本对象
		SpannableString ss = new SpannableString("查找病人");
		 
		// 新建一个属性对象,设置文字的大小
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(16,true);
		 
		// 附加属性到文本
		ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		 
		// 设置hint
		etSearch.setHint(new SpannedString(ss));
		
//		Drawable drawable = this.getResources().getDrawable(R.drawable.search);
//		drawable.setBounds(75, 0, 100, drawable.getIntrinsicHeight());
////		etSearch.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//		etSearch.setCompoundDrawables(drawable, null, null, null);
		
		searchBut = (Button)findViewById(R.id.saoma);
		
		searchBut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(PatientBrowse.this, CaptureActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			}
		});
		
//		etSearch.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View arg0, MotionEvent arg1) {
//				// TODO Auto-generated method stub
//				keyboardUtil = new KeyboardUtil(PatientBrowse.this, keyboardView, etSearch, KeyboardUtil.MODE_MAINPAGE);
//				keyboardUtil.showKeyboard();				
//				return false;
//			}
//		});
		
		
//		deleteText = (ImageView) findViewById(R.id.ivDeleteText);
//		pb_setting = (ImageButton)findViewById(R.id.pb_setting);
		
		ll_menu = (LinearLayout) findViewById(R.id.pi_ll_menu);
		relativeLayout = (RelativeLayout) findViewById(R.id.order_contextshow);
		framelayout = (FrameLayout) findViewById(R.id.pi_fl_content);
		framelayoutImport = (FrameLayout) findViewById(R.id.pi_fl_contentImport);
		framelayoutOperation = (FrameLayout) findViewById(R.id.pi_fl_contentOperation);
		framelayoutTX = (FrameLayout) findViewById(R.id.pi_fl_contentTX);
		framelayoutWD = (FrameLayout) findViewById(R.id.pi_fl_contentWD);
		
//		keyboardView = (KeyboardView)findViewById(R.id.pb_keyboard_view);
	}
	
	class TouhListener implements OnTouchListener{  
        @Override  
        public boolean onTouch(View v, MotionEvent event) {  
            // TODO Auto-generated method stub  
//          Toast.makeText(getApplicationContext(), "---- OnTouchListener -----", event.getAction()).show();  
//          overridePendingTransition(android.R.anim.fade_in,  
//                    android.R.anim.fade_out); // 实现淡入淡出  
        	
        	Toast.makeText(PatientBrowse.this,"敬待开放！",Toast.LENGTH_SHORT);
            return true;  
        }  
    }  
	
	
	private void findViewsByIdWD() {
		
		wdxz_layout =  (LinearLayout) findViewById(R.id.wdxz_lin);
		wdsh_layout =  (LinearLayout) findViewById(R.id.wdsh_lin);
		wdsz_layout =  (LinearLayout) findViewById(R.id.wdsz_lin);
		wdbb_layout =  (LinearLayout) findViewById(R.id.wdupdate_lin);
		wdyj_layout =  (LinearLayout) findViewById(R.id.wdyj_lin);
		
		
		
		wdxz_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(PatientBrowse.this,"敬待开放！",Toast.LENGTH_SHORT).show();
				
//				Intent intent = new Intent(PatientBrowse.this, MyDownActivity.class);
//				startActivity(intent);
			}
		});
		
		wdsh_layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						Intent intent = new Intent(PatientBrowse.this, MauditsActivity.class);
						startActivity(intent);
//						Toast.makeText(PatientBrowse.this,"敬待开放！",Toast.LENGTH_SHORT).show();
					}
				});
		
		wdsz_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				System.out.println("clickclickclick");
				// TODO Auto-generated method stub
				Toast.makeText(PatientBrowse.this,"敬待开放！",Toast.LENGTH_SHORT).show();
			}
		});
		
		wdbb_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if() {
//					
//				}
				
				Toast.makeText(PatientBrowse.this,"已升级为最新版本！",Toast.LENGTH_SHORT).show();
			}
		});
		
		wdyj_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(PatientBrowse.this,"敬待开放！",Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	
    public static void diss() {  
		
//		if(keyboardUtil != null) {
////			keyboardUtil.hideKeyboard();
//			
//			keyboardView.setVisibility(View.GONE);
////			PatientList.search("");
//		}
    }  
	
	
	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        // 将触屏事件交给手势识别类处理  
//        return this.detector.onTouchEvent(event);  
		
		if(keyboardUtil != null) {
			keyboardUtil.hideKeyboard();
		}
		 return false;  
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
    	if(keyboardUtil != null) {
			keyboardUtil.hideKeyboard();
		}
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
	
	
	/**
	 * 初始化病区科室对照页面
	 */
	private void initBqksMapWindow()
	{
		int index = bqlist.indexOf(departmentandward);
        CornerListView bqksmap_view = new CornerListView(PatientBrowse.this,index);
        bqksmap_view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//        bqksmap_view.setBackgroundResource(R.drawable.bqksback);
        bqksmap_view.setDividerHeight(0);
        bqksmap_view.setBackgroundResource(R.drawable.list_corner);
        bqksmap_view.setFadingEdgeLength(5);
		bqksMapAdapter = new BqksMapAdapter(PatientBrowse.this,bqlist,index);
        bqksmap_view.setAdapter(bqksMapAdapter);
		//设置跳转到当前病区
			bqksmap_view.setSelection(index);
		bqksMapAdapter.notifyDataSetInvalidated();

        if(bqlist.size()>1){
        	bqksmap_view.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    					long arg3) {
    				// TODO Auto-generated method stub
    				
    				WindowManager.LayoutParams lp = getWindow().getAttributes();
    				lp.alpha = 1f;
    				getWindow().setAttributes(lp);
    				
    				bqksMapWindow.dismiss();

    				departmentandward = bqlist.get(arg2);
    				tv_bqks.setText(bqlist.get(arg2).getBqmc()+" | "+bqlist.get(arg2).getKsmc());
					SharedPreferenceTools.saveInt(PatientBrowse.this,"BqSel",arg2);
					GlobalCache.getCache().setBqSel(arg2);

					int sel = GlobalCache.getCache().getBqSel();
					PatientList.deptWardMapInfo = GlobalCache.getCache().getDeptWardMapInfos()
							.get(sel);

					Login.et_bqks.setText(bqlist.get(GlobalCache.getCache().getBqSel())
							.getBqmc()
							+ " | "
							+ bqlist.get(GlobalCache.getCache().getBqSel())
							.getKsmc());
    				
    				new Thread() {
    					@Override
    					public void run() {
	    					Map<String, Object> mapconut = new HashMap<String, Object>();
	        				mapconut.put("id", doctor.getId());
	        				mapconut.put("ksdm", departmentandward.getKsdm());
	        				mapconut.put("bqdm", departmentandward.getBqdm());
	        				JSONObject tempcount = new JSONObject(mapconut);
	    					int wjzCount = LoginAction.getRemindCount(PatientBrowse.this, tempcount.toString());
	        				LogUtils.showLog("PatientBrowse==================wjzCount="+wjzCount);
	        				GlobalCache.getCache().setWjzcount(wjzCount);
    				}}.start();
    				
    				try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				
    				loadMode = LOADMENU;
    				loadDataWithNothing();
    				
    				GlobalCache.getCache().setLoadRemind(false);
//
    				PatientList.loadJsData();
    				
    			}
            	
    		});
//        	bqksMapWindow = new PopupWindow(bqksmap_view,ViewUtil.diptopx(PatientBrowse.this,400),ViewUtil.diptopx(PatientBrowse.this,400));
        	bqksMapWindow = new PopupWindow(bqksmap_view,ViewUtil.diptopx(PatientBrowse.this,600),ViewUtil.diptopx(PatientBrowse.this,500));
//        	ColorDrawable dw = new ColorDrawable(0xb0000000);
//        	bqksMapWindow.setBackgroundDrawable(dw);
//        	
            bqksMapWindow.setBackgroundDrawable(new BitmapDrawable());// 点mPopupWindow外面可以让mPopupWindow消失
            bqksMapWindow.setFocusable(true);
            bqksMapWindow.setOutsideTouchable(true);
//            bqksMapWindow.showAsDropDown(pb_tv_bqksselect,ViewUtil.diptopx(PatientBrowse.this,420),ViewUtil.diptopx(PatientBrowse.this,30));
            
            if(android.os.Build.VERSION.SDK_INT >= 22) {
    			
    		} else {
    			WindowManager.LayoutParams lp = getWindow().getAttributes();
    			lp.alpha = 0.7f;
    			getWindow().setAttributes(lp);
    		}
            
//            int[] location = new int[2];  
//            pb_tv_bqksselect.getLocationOnScreen(location);
//            
            bqksMapWindow.showAtLocation(pb_tv_bqksselect, Gravity.CENTER, 0 , 0);
            
            bqksMapWindow.setOnDismissListener(new OnDismissListener() {

    			@Override
    			public void onDismiss() {
    				WindowManager.LayoutParams lp = getWindow().getAttributes();
    				lp.alpha = 1f;
    				getWindow().setAttributes(lp);
					//
					Log.d("tag","-----------------------window xiaoshile ");




    			}
    		});
        }
        
	}
	

	private String getTextCount(int conut, String name) {
		// TODO Auto-generated method stub
		for(int i=0; i<conut;i++) {
			name = name + " ";
		}
		return name;
	}

	
	private void showDialog() {  
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setTitle("离线数据下载消息");  
        builder.setMessage(errorMsg);  
        builder.setNeutralButton("重试",
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
//                    	startDownloading(LOADOFFLINEPAT);
//                    	xzbrList.clear();
                    }  
                });
        builder.setNegativeButton("关闭",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
//                    	xzbrList.clear();
                    }  
                });  
       builder.show();  
    }  
//	private void startDownloading(int mode)
//	{
//		loadMode = mode;
//		if(mode == LOADPATLIST){
//			loadmessage = "正在下载病人数据...";
//			loadDataWithProgressDialog();	
//		}
//		
//		else if(mode == LOADOFFLINEPAT){
//			//loadDataWithNothing();
//			loadmessage = "正在下载离线病人数据...";
//			loadDataWithProgressDialog();	
//		}
//		 
//	}
	//切换病区 加载数据
	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		if(loadMode == LOADPATLIST){

		}
		 else if(loadMode == LOADMENU){
			 
			 
			 
			 
			menuList = MenuAction.getMenuList(PatientBrowse.this,null);
			
//			OfflineAction.startload(getApplicationContext(), "00", null, true);
			
			loadMode = SHOWMENU;
		}
	}

	@Override
	protected void afterLoadData() {
		if(loadMode == LOADPATLIST){
			GlobalCache.getCache().setPatientList(patientList);
			//显示病人列表
			showGridView(patientList);
//			startDownloading(LOADCOMMON);
		}
		else if(loadMode == LOADOFFLINEPAT)
		{
			showDialog();
		} else if(loadMode == SHOWMENU){
			if (patmenus != null)
				patmenus.clear();
			for (int i = 0; i < menuList.size(); i++) {
				patmenus.add(menuList.get(i));
			}
			ShowMenu(0);
		}
		
		
		
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
//			menuViewPager.addMenus(patmenus);
		}
		ll_menu.addView(menuViewPager);
		
		menuViewPager.setOnItemChangeListener(new OnChangeListener() {

			@Override
			public void onPageChangeListener(MenuDTO menuDTO) {
				// TODO Auto-generated method stub
				switchMenu(menuDTO);
			}
		});
		menuViewPager.setCurrentItem(0);
		
	}
	
		final Handler handler = new Handler(){   
		      public void handleMessage(Message msg) {   
		          switch (msg.what) {       
		              case 1:       
		            	  if(menuViewPager != null) {
		          			menuViewPager.setConunt();
		          			}
		                  break;       
		              }       
		              super.handleMessage(msg);   
		         }     
		     };  
		     
		     
		     TimerTask task = new TimerTask(){   
		         public void run() {
		        	Map<String, Object> mapconut = new HashMap<String, Object>();
     				mapconut.put("id", doctor.getId());
     				mapconut.put("ksdm", departmentandward.getKsdm());
     				mapconut.put("bqdm", departmentandward.getBqdm());
     				JSONObject tempcount = new JSONObject(mapconut);
 					int wjzCount = LoginAction.getRemindCount(PatientBrowse.this, tempcount.toString());
					 LogUtils.showLog("PatientBrowse--------GlobalCache.getCache().setWjzcount(wjzCount)="+wjzCount);
     				GlobalCache.getCache().setWjzcount(wjzCount);	 
		        	 
//		        	 Random rand = new Random();
//		             int i = rand.nextInt(100);
//		             GlobalCache.getCache().setWjzcount(i);	 
		        	 
		         Message message = new Message();       
		         message.what = 1;       
		         handler.sendMessage(message);     
		      }   
		   }; 	     
	
	public static void setCount() {
		
//		
//		ll_menu.addView(menuViewPager);
		
		System.out.println("------------"+GlobalCache.getCache().getWjzcount());
		LogUtils.showLog("GlobalCache.getCache().getWjzcount()========="+GlobalCache.getCache().getWjzcount());
		if (menuViewPager == null) {
			menuViewPager = new MenuViewPager(context);
		} else {
			menuViewPager.removeAllMenus();
		}
		ll_menu.removeAllViews();
		menuViewPager.addMenus(patmenus);
		
		if(menuViewPager != null) {
			
			menuViewPager.setConunt();
		}
	}
	
	
	
	private static  void setWjz() {
		// TODO Auto-generated method stub
		loadMode = SHOWMENU;
	}

	private void switchMenu(MenuDTO menuDTO) {
		
		
		if(keyboardUtil != null) {
			keyboardUtil.hideKeyboard();
			PatientList.search("");
			etSearch.setText("");
		}
		
		if (menuDTO.getIntent().startsWith("html#")|| menuDTO.getOrderby() == 11) {
			this.loadDataWithNothing();
		} else {

			Fragment frag = null;
			if (menuDTO.getIntent() != null) {
				frag = FragmentFactory.getFragment(menuDTO.getIntent());
			}
			if (frag != null) {
				contentFrag = frag;
				
				if(menuDTO.getOrderby() == 1) {    //
//					framelayout.setVisibility(View.GONE);
//					framelayoutImport.setVisibility(View.VISIBLE);
//					framelayoutTX.setVisibility(View.GONE);
//					framelayoutWD.setVisibility(View.GONE);
//					relativeLayout_wdtop.setVisibility(View.GONE);
//					relativeLayout_top.setVisibility(View.VISIBLE);
//					wd_menu.setVisibility(View.GONE);
//					pi_ll_content.setVisibility(View.VISIBLE);
//					if(!GlobalCache.getCache().getLoadImpPatient()) {
//						FragmentTransaction ts = this.getSupportFragmentManager()
//								.beginTransaction();
//						ts.add(R.id.pi_fl_contentImport, contentFrag);
//						ts.addToBackStack(null);
//						ts.commit();
//					} else {
//						ImportPatientList.loadJsData();
//					}
					   
//					framelayout.setVisibility(View.VISIBLE);
//					framelayoutImport.setVisibility(View.GONE);
//					framelayoutTX.setVisibility(View.GONE);
//					framelayoutWD.setVisibility(View.GONE);
//					relativeLayout_wdtop.setVisibility(View.GONE);
//					relativeLayout_top.setVisibility(View.VISIBLE);
//					wd_menu.setVisibility(View.GONE);
//					pi_ll_content.setVisibility(View.VISIBLE);
//					
//					
//					if(!GlobalCache.getCache().getLoadPatient()) {
//						FragmentTransaction ts = this.getSupportFragmentManager()
//								.beginTransaction();
//						ts.add(R.id.pi_fl_content, contentFrag);
//						ts.addToBackStack(null);
//						ts.commit();
//					} else {
////						PatientList.find();
//						PatientList.importPatient();
//					}
				
					framelayout.setVisibility(View.GONE);
					framelayoutImport.setVisibility(View.GONE);
					framelayoutOperation.setVisibility(View.VISIBLE);
					framelayoutTX.setVisibility(View.GONE);
					framelayoutWD.setVisibility(View.GONE);
					relativeLayout_wdtop.setVisibility(View.GONE);
					relativeLayout_top.setVisibility(View.VISIBLE);
					wd_menu.setVisibility(View.GONE);
					pi_ll_content.setVisibility(View.VISIBLE);
					
					
					if(!GlobalCache.getCache().getLoadOperation()) {
						FragmentTransaction ts = this.getSupportFragmentManager()
								.beginTransaction();
						ts.add(R.id.pi_fl_contentOperation, contentFrag);
						ts.addToBackStack(null);
						ts.commit();
					} else {
						OperationList.loadJsData();
					}
					
				}  else if(menuDTO.getOrderby() == 2) {   
					framelayout.setVisibility(View.GONE);
					framelayoutImport.setVisibility(View.GONE);
					framelayoutOperation.setVisibility(View.GONE);
					framelayoutTX.setVisibility(View.VISIBLE);
					framelayoutWD.setVisibility(View.GONE);
					relativeLayout_wdtop.setVisibility(View.GONE);
					relativeLayout_top.setVisibility(View.VISIBLE);
					wd_menu.setVisibility(View.GONE);
					pi_ll_content.setVisibility(View.VISIBLE);
					
					
					
					if(!GlobalCache.getCache().getLoadRemind()) {//实时提醒
						FragmentTransaction ts = this.getSupportFragmentManager()
								.beginTransaction();
						ts.add(R.id.pi_fl_contentTX, contentFrag);
						ts.addToBackStack(null);
						ts.commit();
					} else {
						RemindList.loadJsData();
					}
				}  else if(menuDTO.getOrderby() == 3) {   //我的 
					framelayout.setVisibility(View.GONE);
					framelayoutImport.setVisibility(View.GONE);
					framelayoutOperation.setVisibility(View.GONE);
					framelayoutTX.setVisibility(View.GONE);
					framelayoutWD.setVisibility(View.GONE);
					relativeLayout_wdtop.setVisibility(View.VISIBLE);
					relativeLayout_top.setVisibility(View.GONE);
					wd_menu.setVisibility(View.VISIBLE);
					pi_ll_content.setVisibility(View.GONE);
//					if(!GlobalCache.getCache().getLoadDoctor()) {
//						FragmentTransaction ts = this.getSupportFragmentManager()
//								.beginTransaction();
//						ts.add(R.id.pi_fl_contentWD, contentFrag);
//						ts.addToBackStack(null);
//						ts.commit();
//					} else {
//						DoctorList.loadJsData();
//					}
				} else {   
					framelayout.setVisibility(View.VISIBLE);
					framelayoutImport.setVisibility(View.GONE);
					framelayoutOperation.setVisibility(View.GONE);
					framelayoutTX.setVisibility(View.GONE);
					framelayoutWD.setVisibility(View.GONE);
					relativeLayout_wdtop.setVisibility(View.GONE);
					relativeLayout_top.setVisibility(View.VISIBLE);
					wd_menu.setVisibility(View.GONE);
					pi_ll_content.setVisibility(View.VISIBLE);
					
					
					if(!GlobalCache.getCache().getLoadPatient()) {
						FragmentTransaction ts = this.getSupportFragmentManager()
								.beginTransaction();
						ts.add(R.id.pi_fl_content, contentFrag);
						ts.addToBackStack(null);
						ts.commit();
					} else {
//						PatientList.loadJsData();
					}
				}
			}
		}
	}


	@Override
	public void switchPatient() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 注册监听
	 */
	private void registerMessageReceiver()
	{
		// TODO Auto-generated method stub
		NetworkStateReceiver receiver = NetworkStateReceiver.getNetWorkReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		filter.addAction("android.gzcpc.conn.CONNECTIVITY_CHANGE");
		registerReceiver(receiver, filter);

	}




}
