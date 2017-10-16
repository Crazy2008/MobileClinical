package com.winning.mobileclinical.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.BwlLocalAction;
import com.winning.mobileclinical.action.NewBwlAction;
import com.winning.mobileclinical.dialog.DialogHelper;
import com.winning.mobileclinical.dialog.DialogHelper.dialogListener;
import com.winning.mobileclinical.fragment.Memorandum;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.Bwl;
import com.winning.mobileclinical.model.MediaModel;
import com.winning.mobileclinical.model.cis.DoctorInfo;
import com.winning.mobileclinical.model.cis.PatientInfo;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.WebUtils;
import com.winning.mobileclinical.widget.AudioPlayer;
import com.winning.mobileclinical.widget.AudioRecorder;
import com.winning.mobileclinical.widget.AudioRecorder.AudioRecorderListener;
import com.winning.mobileclinical.widget.MathHelp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class NewBWLEditActivity extends Activity {
	public final static int CAMERA_RESULT = 8888;	//拍照返回
	public final static int TUYA_RESULT = 8889;		//涂鸦返回
	public final static int ALBUM_RESULT = 8890;	//相册返回
	
	private Button bwl_edit_cancle = null; 			//取消
	private Button bwl_edit_complete = null;		//提交
	private AudioRecorder bwl_audiorecorder = null; //录音控件
	private TextView bwl_edit_tx = null; 			//备忘录提醒
	private TextView bwl_edit_gl = null; 			//关联
	private TextView bwl_hzinfo = null; 			//患者姓名
	
	private EditText bwl_edit_nr = null; 			//内容
	private CheckBox bwl_edit_bqshare = null; 		//病区分享
	private LinearLayout record_container = null;	//录音列表
	private LinearLayout image_container = null;	//照片,涂鸦列表
	private Button bwl_addaudio = null; 			//添加录音
	private Button bwl_addphoto = null;				//添加图片
	private Button bwl_addsketch = null;			//添加涂鸦
	private Button bwl_addremind = null;			//添加提醒
	private Button bwl_addlink = null;				//添加关联
	private Button bwl_text = null;				//添加关联
	private TextView bwl_textdisplay = null;   //备忘录显示内容
	private ImageButton bwl_btn_mbadd = null;       //调用备忘模板
	
	private boolean sdexiting = true;				//是否有sd卡
	private boolean recording = false;				//是否正在录音
	private boolean texting = false;				//是否正在录音
	private Bwl	bwl = null;							//备忘录
	private	List<MediaModel> mediaModels = null;	//备忘录关联媒体
	private PopupWindow pop = null;					//弹出的页面
	private String mPhotoPath = "";					//每次照片选择路径
	private String mPhotoFileName = "";				//每次照片的名称
	
	private LayoutInflater inflater = null;
	private Date currentDate = new Date();			//时间
	private Dialog sjdialog = null;					//时间弹出的dialog
	private DatePicker datePicker = null;			//日期选择控件
	private TimePicker timePicker = null;			//时间选择控件
	private Dialog gldialog = null;					//关联的dialog
//	private PatientCheckAdapter listAdapter = null;	//病人选择的adapter
	private ProgressDialog proDialog = null;
	private PatientInfo patient = null;			//病人信息
//	private BwlmbDialog bwlmbyDialog = null;
	private int dpWidth = 0;
	private int dpHeight = 0;
//	private String type;   //新增类型
	/**
	 * activity创建
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 设置layout */
		
		setTheme(R.style.translucent);
		
		setContentView(R.layout.bwlnewadd);
		/* 判定sd卡是否存在 */
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			sdexiting = true;					
		} 
		else 
		{
			sdexiting = false;					
			Toast.makeText(NewBWLEditActivity.this, "SD卡不可用,拍照使用小图模式", Toast.LENGTH_SHORT).show();
		}
		inflater = (LayoutInflater) NewBWLEditActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		/* 传递进来的备忘录 */
		bwl = (Bwl) getIntent().getExtras().get("bwl");
//		type = (String) getIntent().getExtras().get("type");
		/* 初始化关联的media */
		mediaModels = new ArrayList<MediaModel>();
		
		if(GlobalCache.getCache().getPatient_selected() != null)
		{
			patient = GlobalCache.getCache().getPatient_selected();
		}
		
        DisplayMetrics metric = new DisplayMetrics();
        NewBWLEditActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        
        System.out.println((int) (metric.widthPixels));
        System.out.println((int) (metric.heightPixels));
        
        dpWidth = (int) (metric.widthPixels * 0.7 );
        dpHeight = (int) (metric.heightPixels * 0.7) ;
		
        NewBWLEditActivity.this.setFinishOnTouchOutside(false);  
        
		findViewsById();
		initListeners();
		initdata();
	}



	/**
	 * 获得控件
	 */
	private void findViewsById() {
		// TODO Auto-generated method stub
		bwl_edit_cancle 	= (Button) findViewById(R.id.bwl_edit_cancle); 
		bwl_edit_complete 	= (Button) findViewById(R.id.bwl_edit_complete); 
		bwl_audiorecorder 	= (AudioRecorder) findViewById(R.id.bwl_audiorecorder); 
		bwl_edit_tx 		= (TextView) findViewById(R.id.bwl_edit_tx); 
		bwl_edit_gl 		= (TextView) findViewById(R.id.bwl_edit_gl); 
		bwl_edit_nr 		= (EditText) findViewById(R.id.bwl_edit_nr); 
		bwl_btn_mbadd       = (ImageButton) findViewById(R.id.bwl_btn_mbadd);
		bwl_edit_bqshare 	= (CheckBox) findViewById(R.id.bwl_edit_bqshare); 
		record_container 	= (LinearLayout) findViewById(R.id.record_container); 
		image_container 	= (LinearLayout) findViewById(R.id.image_container); 
		bwl_addaudio 		= (Button) findViewById(R.id.bwl_addaudio); 
		bwl_addphoto 		= (Button) findViewById(R.id.bwl_addphoto); 
		bwl_addsketch 		= (Button) findViewById(R.id.bwl_addsketch); 
		bwl_addremind 		= (Button) findViewById(R.id.bwl_addremind); 
		bwl_text            = (Button) findViewById(R.id.bwl_text); 
		bwl_textdisplay            = (TextView) findViewById(R.id.bwl_edit_display); 
		bwl_hzinfo = (TextView) findViewById(R.id.bwl_hzinfo);       
		
		
		bwl_hzinfo.setText(patient.getCwdm()+"床" + "　" + patient.getName());
		
		
		
//		bwl_addlink 		= (Button) findViewById(R.id.bwl_addlink); 
		/* 初始化一些控件 */
		bwl_edit_gl.setVisibility(View.GONE);
		bwl_edit_gl.setText("");
		bwl_edit_tx.setVisibility(View.GONE);
		bwl_edit_tx.setText("");
		
//		if(GlobalCache.getCache().getJdyy().equals("1")){
//			bwl_btn_mbadd.setVisibility(View.VISIBLE);
//		}else{
//			bwl_btn_mbadd.setVisibility(View.GONE);
//		}
		
		if(!"0".equals(bwl.getXh())) {
//			bwl_edit_nr.setVisibility(View.VISIBLE);
			bwl_textdisplay.setVisibility(View.VISIBLE);
			bwl_textdisplay.setText(""+bwl.getContents());
		}
	}
	/**
	 * 追加监听
	 */
	private void initListeners() {
		// TODO Auto-generated method stub
		/* 模板添加按钮*/
		/* 开始录音按钮 */
		bwl_addaudio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( !recording ) 
				{
					//未在录音
					String mRecordDir = getMkDir()+ "/" + getRecordFileName();
					bwl_audiorecorder.setPath(mRecordDir);
					bwl_audiorecorder.start();
				}
				else 
				{	//正在录音,停止录音
					bwl_audiorecorder.stop();
				}
			}
		});
		/* 录音控件处理 */
		bwl_audiorecorder.setRecorderListener( new AudioRecorderListener() {
			/**
			 * 录音停止操作
			 */
			@Override
			public void onStop(AudioRecorder recorder) {
				//创建一个新的录音播放控件
				AudioPlayer player = new AudioPlayer(NewBWLEditActivity.this);
				player.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				player.setPath(recorder.getPath());
				//创建对应的媒体类,设置到tag中
				MediaModel mediaModel = new MediaModel();
				mediaModel.setId("0");
				mediaModel.setLb("2");
				mediaModel.setLbsm("录音");
				mediaModel.setFileName(player.getFilenameString());
				mediaModel.setDescription("");
				mediaModel.setState(1);
				mediaModel.setSrc(getMkDir()+ "/");
				player.setTag(mediaModel);
				player.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View view) {
						// TODO Auto-generated method stub
						delete(view,"2");	//长按删除
						return false;
					}
				});
				record_container.addView(player);			//添加到录音的控件集合中
				bwl_audiorecorder.setPath(null);			//清空录音用的控件
				bwl_audiorecorder.setVisibility(View.GONE);	//隐藏
				recording = false;							//停止录音状态
			}
			
			@Override
			public void onStart(AudioRecorder recorder) {
				bwl_audiorecorder.setVisibility(View.VISIBLE);//开始录音
				recording = true;
			}
		});
		/* 图片按钮 */
		bwl_addphoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( !recording )
				{
//					addPhoto();
					takePhoto();
					
				}
				else 
				{
					Toast.makeText(NewBWLEditActivity.this, "正在录音,请先停止录音!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
//		
		bwl_text.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!"0".equals(bwl.getXh())) {
					if( !texting )
					{
						bwl_textdisplay.setVisibility(View.GONE);
						bwl_edit_nr.setVisibility(View.VISIBLE);
						texting = true;
					}
//					else 
//					{
//						bwl_edit_nr.setVisibility(View.GONE);
//					}
					
					
					
				} else {
					if( !texting )
					{
						bwl_edit_nr.setVisibility(View.VISIBLE);
						texting = true;
					}
					else 
					{
						bwl_edit_nr.setVisibility(View.GONE);
						texting = false;
					}
				}
				
				
			}
		});
		
		/* 涂鸦按钮 */
		bwl_addsketch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if( !recording )
				{
					Intent intent = new Intent(NewBWLEditActivity.this, TuYaActivity.class);
					startActivityForResult(intent, TUYA_RESULT);
				}
				else 
				{
					Toast.makeText(NewBWLEditActivity.this, "正在录音,请先停止录音!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		/* 提醒按钮  */
		bwl_addremind.setOnClickListener(new OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if( recording )
				{
					Toast.makeText(NewBWLEditActivity.this, "正在录音,请先停止录音!", Toast.LENGTH_SHORT).show();
					return;
				}
				if (sjdialog == null) {
					currentDate = new Date();
					View layout = inflater.inflate(R.layout.datetimepicker,null);
					
					datePicker = (DatePicker) layout.findViewById(R.id.dtpicker_date);
					datePicker.init(currentDate.getYear()+1900, currentDate.getMonth(), currentDate.getDate(), null);
					
					timePicker = (TimePicker) layout.findViewById(R.id.dtpicker_time);
					timePicker.setIs24HourView(true);
					timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
					timePicker.setCurrentMinute(currentDate.getMinutes());
					
					
					Builder builder = new Builder(NewBWLEditActivity.this);
					builder.setView(layout);
					builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							String showText = datePicker.getYear()+"/"+(datePicker.getMonth()+1)+
									"/"+datePicker.getDayOfMonth()+" "+timePicker.getCurrentHour()+
									":"+timePicker.getCurrentMinute();
							bwl.setTxsj(showText);
							bwl_edit_tx.setText("提醒："+showText);
							bwl_edit_tx.setVisibility(View.VISIBLE);
						}


					});
					builder.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					sjdialog = builder.create();
				}
				if (sjdialog.isShowing()) 
				{
					sjdialog.dismiss();
				} 
				else
				{
					sjdialog.show();
				}
			}
		});
		/* 关联病人 */
//		bwl_addlink.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if( recording )
//				{
//					Toast.makeText(BWLEditActivity.this, "正在录音,请先停止录音!", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if (gldialog == null) {
////					ListView listView = new ListView(BWLEditActivity.this);
////					listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
////					if(listAdapter == null)
////					{
////						listAdapter = new PatientCheckAdapter(BWLEditActivity.this,GlobalCache.getCache().getPatients());
////					}
////					listView.setAdapter(listAdapter);
////					Builder builder = new AlertDialog.Builder(BWLEditActivity.this);
////					builder.setView(listView);
////					builder.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
////						
////						@Override
////						public void onClick(DialogInterface arg0, int arg1) {
////							// TODO Auto-generated method stub
////							String showString = listAdapter.getShowString();
////							if("".equals(showString))
////							{
////								bwl_edit_gl.setVisibility(View.GONE);
////							}
////							else 
////							{
////								bwl_edit_gl.setVisibility(View.VISIBLE);
////							}
////							bwl.setGlbr(listAdapter.getSaveString());
////							bwl_edit_gl.setText(showString);
////						}
////					});
////					builder.setNegativeButton("取 消", new DialogInterface.OnClickListener() {
////						@Override
////						public void onClick(DialogInterface dialog, int which) {
////							dialog.dismiss();
////						}
////					});
////					gldialog = builder.create();
//				}
//				if (gldialog.isShowing()) 
//				{
//					gldialog.dismiss();
//				} 
//				else
//				{
////					if(bwl.getGlbr() != null)
////					{
////						listAdapter.setSaveString(bwl.getGlbr());
////					}
////					else 
////					{
////						listAdapter.setSaveString("");
////					}
//					
//					gldialog.show();
//				}
//			}
//		});
		/* 退出 */
		bwl_edit_cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if( recording )
				{
					Toast.makeText(NewBWLEditActivity.this, "正在录音,请先停止录音!", Toast.LENGTH_SHORT).show();
					return;
				}
//				deleteFileByType(2);
				NewBWLEditActivity.this.finish();
			}
		});
		/* 提交按钮 */
		bwl_edit_complete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if( recording )
				{
					Toast.makeText(NewBWLEditActivity.this, "正在录音,请先停止录音!", Toast.LENGTH_SHORT).show();
					return;
				}
				//更新备忘录的数据
				String contents = bwl_edit_nr.getText().toString();
				if(contents.length() > 10)
				{
					bwl.setTitle(contents.substring(0, 10));
				}
				else {
					bwl.setTitle(contents);
				}
				bwl.setContents(contents);
				if(bwl_edit_bqshare.isChecked())
				{
					bwl.setType("1");
				}
				else 
				{
					bwl.setType("0");
				}
				//媒体类处理
				mediaModels.clear();
				AudioPlayer audioPlayer = null;
				for(int i = 0;i<record_container.getChildCount();i++){
					if(record_container.getChildAt(i) instanceof AudioPlayer){	
						audioPlayer = (AudioPlayer)record_container.getChildAt(i);
						mediaModels.add((MediaModel) audioPlayer.getTag());
					}
				}
				ImageView imageView = null;
				for(int i = 0;i<image_container.getChildCount();i++){
					if(image_container.getChildAt(i) instanceof ImageView){
						imageView = (ImageView)image_container.getChildAt(i);
						mediaModels.add((MediaModel) imageView.getTag());
					}
				}
				//提交开启
				new Thread(new SubmitThread()).start();
				proDialog = ProgressDialog.show(NewBWLEditActivity.this, "",
						"正在传输数据...", true, true);
			}
		});
	}
	/**
	 * 添加闹钟提醒
	 */
	private void addAlarm() {
		//式样
//		String showText = datePicker.getYear()+"/"+(datePicker.getMonth()+1)+
//				"/"+datePicker.getDayOfMonth()+" "+timePicker.getCurrentHour()+
//				":"+timePicker.getCurrentMinute();
		if(bwl.getTxsj() != null && !"".equals(bwl.getTxsj()))
		{
			//分割时间
			String showText = bwl.getTxsj();
			int point = showText.indexOf("/");
			String temp = showText.substring(0,point);
			showText = showText.substring(point+1);
			int year = MathHelp.parseInt(temp);
			
			point = showText.indexOf("/");
			temp = showText.substring(0,point);
			showText = showText.substring(point+1);
			int month = MathHelp.parseInt(temp);
			
			point = showText.indexOf(" ");
			temp = showText.substring(0,point);
			showText = showText.substring(point+1);
			int dayOfMonth = MathHelp.parseInt(temp);		
			
			point = showText.indexOf(":");
			temp = showText.substring(0,point);
			showText = showText.substring(point+1);
			int currentHour = MathHelp.parseInt(temp);		
			
			int currentMinute = MathHelp.parseInt(showText);		
			// TODO Auto-generated method stub
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month-1);
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			calendar.set(Calendar.HOUR_OF_DAY, currentHour);
			calendar.set(Calendar.MINUTE, currentMinute);
			calendar.set(Calendar.SECOND, 0);
			Intent intent = new Intent(NewBWLEditActivity.this, AlarmReceiver.class); //创建Intent对象
			intent.putExtra("tickerText", "备忘录提醒");
			intent.putExtra("title", bwl.getTitle());
			intent.putExtra("message", bwl.getContents());
			intent.putExtra("bwl", bwl);
			PendingIntent pi = PendingIntent.getBroadcast(NewBWLEditActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT); //创建PendingIntent
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis() , pi); //设置闹钟，当前时间就唤醒
			Toast.makeText(NewBWLEditActivity.this, "闹钟设置成功", Toast.LENGTH_LONG).show();//提示用户
		}
		
	}
	/**
	 * 弹出拍照还是照片选择的菜单
	 */
	@SuppressWarnings("deprecation")
	protected void addPhoto() {
		View layout = LayoutInflater.from(NewBWLEditActivity.this).inflate(R.layout.camera_check_layout, null);
		ListView cameralist = (ListView) layout.findViewById(R.id.camera_ListView);
	
		// 生成数据数组
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	
		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("listitem_image", R.drawable.fill4);
		map0.put("listitem_text", "拍照");
		listItem.add(map0);
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("listitem_image", R.drawable.fill4);
		map1.put("listitem_text", "相册");
		listItem.add(map1);
		// 生成适配器的Item和数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(NewBWLEditActivity.this, listItem,R.layout.camera_list_layout, 
				new String[] {"listitem_image", "listitem_text" },
				new int[] { R.id.listitem_image,R.id.listitem_text });
	
		cameralist.setAdapter(listItemAdapter);
		cameralist.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(android.widget.AdapterView<?> parent,View view, int position, long id) {
						switch (position) {
						case 0:
							takePhoto();
							break;
						case 1:
							getAlbum();
							break;
						}
						if (pop != null) {
							pop.dismiss();
						}
					};
				});
	
		pop = new PopupWindow(layout, ViewUtil.diptopx(NewBWLEditActivity.this,150),LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.update();
		
		
		int[] location = new int[2];  
		bwl_addphoto.getLocationOnScreen(location); 
	
		if (pop.isShowing()) {
			pop.dismiss();
		} else {
//			System.out.println( location[0]);
//			
//			System.out.println( location[1]-pop.getHeight());
//			pop.showAtLocation(bwl_addphoto, Gravity.NO_GRAVITY, location[0], location[1]-pop.getHeight());  
			
			pop.showAsDropDown(bwl_addphoto, location[0]-200 , 15);
		}
	}
	/*
	 * 拍照
	 */
	private void takePhoto() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		if(sdexiting)		
		{					
			String mPhotoDir = getMkDir();
			mPhotoFileName = getPhotoFileName();
			mPhotoPath = mPhotoDir + "/" + mPhotoFileName;
			File mPhotoFile = new File(mPhotoPath);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
		}					
		startActivityForResult(intent, CAMERA_RESULT);
	}
	/*
	 * 获取相册图片
	 */
	private void getAlbum() {
		
		Intent intent = new Intent("android.intent.action.PICK");
		intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
		String mPhotoDir = getMkDir();
		mPhotoFileName = getPhotoFileName();
		mPhotoPath = mPhotoDir + "/" + mPhotoFileName;
		startActivityForResult(intent, ALBUM_RESULT);
	}
	/**
	 * 初始化数据
	 */
	private void initdata() {
		// TODO Auto-generated method stub
		if(!"0".equals(bwl.getXh()))
		{
			proDialog = ProgressDialog.show(NewBWLEditActivity.this, "", "正在获取数据...",
			true, false);
//			handler.post(new LoadThread());		//加载媒体
			
			new Thread(new LoadThread()).start();
			
			bwl_edit_nr.setText(""+bwl.getContents());
			if(bwl.getTxsj()!= null && !"".equals(bwl.getTxsj()))
			{
				bwl_edit_tx.setText("提醒："+bwl.getTxsj());
				bwl_edit_tx.setVisibility(View.VISIBLE);
			}
			if(bwl.getGlbr()!= null && !"".equals(bwl.getGlbr()))
			{
//				if(listAdapter == null)
//				{
//					listAdapter = new PatientCheckAdapter(BWLEditActivity.this,GlobalCache.getCache().getPatients());
//				}
//				listAdapter.setSaveString(bwl.getGlbr());
//				bwl_edit_gl.setText(listAdapter.getShowString());
//				bwl_edit_gl.setVisibility(View.VISIBLE);
			}
			if("1".equals(bwl.getType()))
			{
				bwl_edit_bqshare.setChecked(true);
			}
			else 
			{
				bwl_edit_bqshare.setChecked(false);
			}
			bwl_edit_bqshare.setClickable(false);
		}
		else 
		{
			bwl_edit_tx.setVisibility(View.GONE);
			bwl_edit_gl.setVisibility(View.GONE);
			bwl_edit_bqshare.setClickable(true);
		}
		
	}
	
	/*
	 * 处理拍照、涂鸦返回
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {//bwl002
			Bitmap bitmap = null;
			if(sdexiting)		
			{
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				bitmap = BitmapFactory.decodeFile(mPhotoPath , opts);
				int scale = (int) (opts.outWidth / (float) 320);
				if (scale <= 0)
				{
					scale = 1;
				}
				opts.inSampleSize = scale;
				opts.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(mPhotoPath, opts);	
			}
			else
			{
				String mPhotoDir = getMkDir();
				mPhotoFileName  = getPhotoFileName();
				mPhotoPath = mPhotoDir + "/" + mPhotoFileName;
				
				Bundle bundle = data.getExtras();  
				bitmap = (Bitmap) bundle.get("data");  
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(mPhotoPath);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					fos.flush();
					fos.close(); 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bitmap != null) 
			{
				addImage(bitmap);
			} else {
//				Intent intent = new Intent();
//				intent.setClass(BWLEditActivity.this, FirstActivity.class);
//				startActivity(intent);
				finish();
			}
			
		} else if (requestCode == TUYA_RESULT) {// 涂鸦返回
			if (data != null) {
				if (data.getExtras().getString("tuyaPath") != null) {
					mPhotoPath = data.getExtras().getString("tuyaPath");
					mPhotoFileName = data.getExtras().getString("tuyaFileName");
					
					Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);
					if (bitmap != null) {
						addImage(bitmap);
					}
				}
			}

		}
		else if (requestCode == ALBUM_RESULT && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			ContentResolver cr = this.getContentResolver(); 
			Bitmap bitmap = null;
			String mPhotoDir = getMkDir();
			mPhotoFileName = getPhotoFileName();
			mPhotoPath = mPhotoDir + "/" + mPhotoFileName;
			
			
			FileOutputStream fos = null;
			try {
				//图片处理类
				BitmapFactory.Options opts = new BitmapFactory.Options();
				//获得图片的长宽
				opts.inJustDecodeBounds = true;
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),null,opts);
				//算出压缩比例
				int scale = (int) (opts.outWidth / (float) 320);
				if (scale <= 0)
				{
					scale = 1;
				}
				//设置压缩比例
				opts.inSampleSize = scale;
				opts.inJustDecodeBounds = false;
				//读取图片
				bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri),null,opts);
				fos = new FileOutputStream(mPhotoPath);
				//把压缩后图片保存在nurse目录
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close(); 
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (bitmap != null) 
			{
				addImage(bitmap);
			}
		}
	}
	//添加图片  拍照、相册、涂鸦
	private void addImage(Bitmap bitmap){
		
		ImageView image = new ImageView(this);
		image.setAdjustViewBounds(true);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		image.setPadding(2, 2, 2, 2);
		image.setImageBitmap(bitmap);
		
		image.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				// TODO Auto-generated method stub
				delete(view,"1");
				return false;
			}
		});
		MediaModel mediaModel = new MediaModel();
		mediaModel.setId("0");
		mediaModel.setLb("1");
		mediaModel.setLbsm("图片");
		mediaModel.setFileName(mPhotoFileName);
		mediaModel.setDescription("");
		mediaModel.setState(1);
		mediaModel.setSrc(getMkDir()+ "/");
		image.setTag(mediaModel);
		
		image_container.addView(image);
	}
	
	
	/**
	 * 资源文件的删除，只针对数据库存在的媒体资源。
	 */
	public void delete(final View view,final String mediatype){
		DialogHelper.showAlert(this, "提示", "确定要删除吗 ？",R.drawable.fill4, 
				new dialogListener() {
					@Override
					public void onOk(Dialog dialog) {
						//判断数据库是否存在
						MediaModel media = (MediaModel) view.getTag();
						if(!"0".equals(media.getId()) && bwl.getSave_type() == 1)
						{
							BwlLocalAction.deleteMedia(NewBWLEditActivity.this, media.getId());
							File file=new File(getMkDir()+"/"+media.getFileName());
							file.delete();
						}
						else if(!"0".equals(media.getId()) && bwl.getSave_type() == 2)
						{
							NewBwlAction.deleteMedia(NewBWLEditActivity.this, media.getId());
							File file=new File(getMkDir()+"/"+media.getFileName());
							file.delete();
						}
						if("1".equals(mediatype)){			//图片删除
							image_container.removeView(view);
						}else if("2".equals(mediatype)){	//录音删除
							record_container.removeView(view);
						}
						dialog.dismiss();
					}

					@Override
					public void oncancel(Dialog dialog) {
						// TODO Auto-generated method stub
						
					}
				}
			);
	} 
	
	/*
	 * 提交
	 */
	class SubmitThread implements Runnable {

		@Override
		public void run() {
			if (bwl.getXh()!=null&&!"0".equals(bwl.getXh())) {// 更新内容
				//本地更新
				if(bwl.getSave_type()==1){
//					handler.post(new BwlLocalSubmit());
					
					new Thread(new BwlLocalSubmit()).start();
					
				}else{
					//服务器更新
//					handler.post(new BwlServerSubmit());
					new Thread(new BwlServerSubmit()).start();
				}
			} else {	//新建的
				if(bwl_edit_bqshare.isChecked())						
				{											
					//服务器新上传	
					new Thread(new BwlServerSubmit()).start();
//					handler.post(new BwlServerSubmit());	
				}										
				else										
				{	
					new Thread(new BwlLocalSubmit()).start();
//					handler.post(new BwlLocalSubmit());		
				}											
			}
		}
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int flag = msg.arg1;
			
			if (flag == 0) {
				Toast.makeText(NewBWLEditActivity.this, "上传失败", Toast.LENGTH_LONG)
						.show();
			}
			if (flag == 2) { // 保存成功或更新成功
//				deleteFileByType(1);
				mediaModels.clear();
				Intent intent = new Intent(NewBWLEditActivity.this, Memorandum.class);
				setResult(Memorandum.EDIT_RESULT, intent);
				NewBWLEditActivity.this.finish();
			}
			if (flag == 3) { // download 图片及音频
				
				//服务器音频，需要下载
				DownloadThread download = new DownloadThread();
				new Thread(download).start();
			}
			if(flag == 4)
			{
				MediaModel media = (MediaModel) msg.obj;
				if("1".equals(media.getLb()))
				{
					initLocalPicView(media);
				}
				if("2".equals(media.getLb()))
				{
					initLocalRecordView(media);
				}
			}
			
		}
	};
	/**
	 * 删除本地文件:(1)提交  (2)返回
	 * 1、服务器提交或返回：
	 *    每次会下载，全部删除。
	 * 2、本地返回：
	 *    返回：只删除新增的。          
	 * 3、本地提交的删除操作不在此处处理。
	 * 
	 * 参数type:
	 * 1、提交   2、返回
	 */
//	private void deleteFileByType(int type) {
//		if(mediaModels!=null&&mediaModels.size()>0){
//			for(MediaModel media:mediaModels){
//				String path=getMkDir()+"/"+media.getFileName();
//				if(bwl.getSave_type() == 1 && type == 2 && media.getState() == 1 ){		//本地备忘录返回操作
//					new File(path).delete();
//				}else if(bwl.getSave_type()==2){								//服务端备忘录提交或返回
//					new File(path).delete();
//				}
//			}
//		}
//		
//	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(mediaModels!=null&&mediaModels.size()>0){
			for(MediaModel media:mediaModels){
				String path=getMkDir()+"/"+media.getFileName();
				if(bwl.getSave_type()==2){								//服务端备忘录提交或返回
					new File(path).delete();
				}
			}
		}
		super.onDestroy();
	}
	//备忘录本地保存或更新
	class BwlLocalSubmit implements Runnable {

		@Override
		public void run() {
			try {
				// 判断是添加还是更新
				if (!"0".equals(bwl.getXh())) {
					// 更新备忘录
					BwlLocalAction.updateBwl(NewBWLEditActivity.this,bwl,patient.getSyxh()+"",patient.getYexh()+"");
					//2、新增 3、删除
					if(mediaModels.size()>0){
						for(MediaModel media:mediaModels){
							if(media.getState()==1)
							{		//新增
								if("1".equals(media.getLb()))
								{	//图片
									BwlLocalAction.upLoad(NewBWLEditActivity.this,"1", "拍照", 
											  getMkDir()+"/",
											  bwl.getXh(), 
											  media.getDescription(),
											  media.getFileName(),
											  patient.getSyxh()+"",patient.getYexh()+"");
								}
								else if("2".equals(media.getLb()))
								{
									BwlLocalAction.upLoad(NewBWLEditActivity.this,"2", "录音", 
											  getMkDir()+"/",
											  bwl.getXh(), 
											  media.getDescription(),
											  media.getFileName(),
											  patient.getSyxh()+""
											  ,patient.getYexh()+""
											  );
								}
							}
						}
					}
					Message msg = new Message();
					msg.arg1 = 2;
					msg.arg2 = 1;
					handler.sendMessage(msg);
				} else {
					int bwlxh = BwlLocalAction.addBwl(NewBWLEditActivity.this,bwl,patient.getSyxh()+"",patient.getYexh()+"");
					if(bwlxh>0){
						if(mediaModels.size()>0){
							for(MediaModel media:mediaModels){
								if("1".equals(media.getLb()))
								{
									BwlLocalAction.upLoad(NewBWLEditActivity.this,"1", "拍照", 
											  getMkDir()+"/",
											  bwlxh + "", 
											  media.getDescription(),
											  media.getFileName(),
											  patient.getSyxh()+"",patient.getYexh()+"");
								}
								else if("2".equals(media.getLb()))
								{
									BwlLocalAction.upLoad(NewBWLEditActivity.this,"2", "录音", 
											  getMkDir()+"/",
											  bwlxh + "", 
											  media.getDescription(),
											  media.getFileName(),
											  patient.getSyxh()+"",patient.getYexh()+"");
								}
							}
						}
						bwl.setXh(""+bwlxh);
						addAlarm();
						Message msg = new Message();
						msg.arg1 = 2;
						msg.arg2 = 1;
						handler.sendMessage(msg);
					}else{
						Toast.makeText(NewBWLEditActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
						Message msg = new Message();
						msg.arg1 = 2;
						msg.arg2 = 2;
						handler.sendMessage(msg);
					}
					proDialog.dismiss();
				}

				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 更新服务端数据
	 * @author Administrator
	 *
	 */
	class BwlServerSubmit implements Runnable{
		@Override
		public void run() {
			Map<String, File> fileMap = new HashMap<String, File>();
			if(bwl.getXh() == null || "0".equals(bwl.getXh()))
			{
				int bwlxh = NewBwlAction.addBwl(NewBWLEditActivity.this,bwl,patient.getSyxh()+"",patient.getYexh()+"");
				if(mediaModels.size()>0){
					for(int i=0;i<mediaModels.size();i++){
						MediaModel media=mediaModels.get(i);
						if(media.getState()==1){	//新增
							String path=media.getSrc()+media.getFileName();
							//图像
							if("1".equals(media.getLb())){
								fileMap.put("photo" + i, new File(path));
							}
							//录音
							if("2".equals(media.getLb())){
								fileMap.put("record", new File(path));
							}
							HTTPGetTool upload = HTTPGetTool.getTool();
							JSONObject json = upload.post(WebUtils.HOST	+ WebUtils.UPLOAD, fileMap);
							
							try {
								//上传成功，同步数据库
								if(json.get("success").equals("true")){
									//
									if("1".equals(media.getLb())){
										NewBwlAction.upLoad(NewBWLEditActivity.this,"1", "拍照", 
												  "C:/doctouch/photo/",
												  bwlxh+"", 
												  media.getDescription(),
												  media.getFileName(),patient.getSyxh()+"",patient.getYexh()+"");
									}else if("2".equals(media.getLb())){
										NewBwlAction.upLoad(NewBWLEditActivity.this,"2", "录音", 
												"C:/doctouch/record/",
												  bwlxh+"", 
												  media.getDescription(),
												  media.getFileName(),patient.getSyxh()+"",patient.getYexh()+"");
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}
				Message msg = new Message();
				msg.arg1 = 2;
				msg.arg2 = 1;
				handler.sendMessage(msg);
				proDialog.dismiss();
			}
			else
			{
				DoctorInfo nurse=GlobalCache.getCache().getDoctor();
				if(nurse != null && !bwl.getCjhsid().trim().equals(nurse.getId().trim()))
				{
					Toast.makeText(NewBWLEditActivity.this, "保存失败,非创建者本人操作", Toast.LENGTH_SHORT).show();
					proDialog.dismiss();
					return;
				}

				NewBwlAction.updateBwl(NewBWLEditActivity.this,bwl);
				if(mediaModels.size()>0){
					int cnt = 0;
					for(MediaModel media:mediaModels){
						if(media.getState()==1){	//新增
							String path=media.getSrc()+media.getFileName();
							//图像
							if("1".equals(media.getLb())){
								fileMap.put("photo" + cnt, new File(path));
							}
							//录音
							if("2".equals(media.getLb())){
								fileMap.put("record" + cnt, new File(path));
							}
							HTTPGetTool upload = HTTPGetTool.getTool();
							JSONObject json = upload.post(WebUtils.HOST	+ WebUtils.UPLOAD, fileMap);
							
							try {
								//上传成功，同步数据库
								if(json.get("success").equals("true")){
									//
									if("1".equals(media.getLb())){
										NewBwlAction.upLoad(NewBWLEditActivity.this,"1", "拍照", 
												  "C:/doctouch/photo/",
												  bwl.getXh(), 
												  media.getDescription(),
												  media.getFileName(),patient.getSyxh()+"",patient.getYexh()+"");
									}else if("2".equals(media.getLb())){
										NewBwlAction.upLoad(NewBWLEditActivity.this,"2", "录音", 
												"C:/doctouch/record/",
												  bwl.getXh(), 
												  media.getDescription(),
												  media.getFileName(),patient.getSyxh()+"",patient.getYexh()+"");
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
						cnt ++;
					}
				}
				
				Message msg = new Message();
				msg.arg1 = 2;
				msg.arg2 = 1;
				handler.sendMessage(msg);
				proDialog.dismiss();
			}
		}
		
	}
	/**
	 * 下载数据
	 * @author ZHH
	 *
	 */
	class LoadThread implements Runnable {

		@Override
		public void run() {
			
			//本地查找对应media
			if(bwl.getSave_type() == 1){
				mediaModels = BwlLocalAction.getMediaId(NewBWLEditActivity.this,bwl.getXh(),patient.getSyxh()+"",patient.getYexh()+"");
				
				//初始化音频数据
				if(mediaModels != null && mediaModels.size() > 0){
					for(final MediaModel media:mediaModels){
						final String type=media.getLb();
						//图片、录音
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								if("1".equals(type)){
									initLocalPicView(media);
								}
								if("2".equals(type)){
									initLocalRecordView(media);
								}
							}


						});
						
					}
				}
				proDialog.dismiss();
				
			}else{
				//获取服务器音频文件
//				mediaModels = BwlLocalAction.getMediaId2(BWLEditActivity.this,bwl.getXh());
				
				mediaModels = NewBwlAction.getMediaId(NewBWLEditActivity.this,bwl.getXh());
				
				Message msg = new Message();
				if (mediaModels != null && mediaModels.size() > 0) {
					msg.arg1 = 3;
					proDialog.dismiss();
				} else {
					proDialog.dismiss();
					return;
				}
				handler.sendMessage(msg);
			}
			
		}

	}
	//
	//本地图片、涂鸦初始化
	public void initLocalPicView(MediaModel media){
		String path = getMkDir() + "/" + media.getFileName();
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(path , opts);
		int scale = (int) (opts.outWidth / (float) 320);
		if (scale <= 0)
		{
			scale = 1;
		}
		opts.inSampleSize = scale;
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(path, opts);	
		
		ImageView image = new ImageView(this);
		image.setAdjustViewBounds(true);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		image.setPadding(2, 2, 2, 2);
		image.setImageBitmap(bitmap);
		image.setTag(media);
		image.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				// TODO Auto-generated method stub
				delete(view,"1");
				return false;
			}
		});
		image_container.addView(image);
	}
	private void initLocalRecordView(MediaModel media) {
		// TODO Auto-generated method stub
		String path = getMkDir() + "/" + media.getFileName();
		AudioPlayer player = new AudioPlayer(NewBWLEditActivity.this);
		player.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		player.setPath(path);
		player.setTag(media);
		player.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View view) {
				// TODO Auto-generated method stub
				delete(view,"2");
				return false;
			}
		});
		record_container.addView(player);
	}


	class DownloadThread implements Runnable {

		@Override
		public void run() {
			for (MediaModel media:mediaModels) {
				String path = media.getSrc()+media.getFileName();
				InputStream in = NewBwlAction.downLoad(path);
				String lb = media.getLb().toString().trim();
				if (in == null) {
					continue;
				} else {
					if (lb.equals("1")) 
					{
						savefile(media, in);
//						setDownloadJPGValue(media, in);
					}
					if (lb.equals("2")) 
					{
						savefile(media, in);
//						setDownloadRecordValue(media, in);
					}
				}
			}
//			proDialog.dismiss();
		}
		private void savefile(MediaModel media, InputStream in)
		{
			String fileNamePath = getMkDir() + "/" + media.getFileName();
			byte[] b = new byte[4 * 1024];
			int len = 0;
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(fileNamePath);
				while ((len = in.read(b)) != -1) {
					fos.write(b, 0, len);
				}
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (fos != null) {
						fos.close();
					}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			Message msg = new Message();
			msg.arg1 = 4;
			msg.obj = media;
			handler.sendMessage(msg);

		}
	}
//
//	/*
//	 * 压缩图片并且将SDCard原有图片替换
//	 */
//	private Bitmap yaSuo(Bitmap bitmap, String mPhotoPath) {
//		Matrix matrix = new Matrix();
//		matrix.postScale(0.3f, 0.25f);
//		Bitmap temp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//				bitmap.getHeight(), matrix, true);
//		File f = new File(mPhotoPath);
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		temp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//		byte[] data = baos.toByteArray();
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream(f);
//			fos.write(data, 0, data.length);
//			fos.flush();
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (fos != null) {
//					fos.close();
//					fos = null;
//				}
//				if (baos != null) {
//					baos.close();
//					baos = null;
//				}
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return temp;
//	}
//
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			deleteFileByType(2);
		}
		return super.onKeyDown(keyCode, event);
	}
	////////////////////////////////////工具
	@Override
	protected void onPause() {
		super.onPause();
		
		//释放音频播放器
		for(int i = 0;i<record_container.getChildCount();i++){
			if(record_container.getChildAt(i) instanceof AudioPlayer){
				((AudioPlayer)record_container.getChildAt(i)).releasePlayer();
			}
		}			
	}
	// 获取文件夹
	private String getMkDir() {
		String saveDir = "";
		if(sdexiting) {
			 saveDir = Environment.getExternalStorageDirectory() + "/doctouch";
		}
		else
		{
			saveDir = "/mnt/sdcard-ext/doctouch";
		}
		File dir = new File(saveDir);
		if (!dir.exists()) {
			dir.mkdir(); // 创建文件夹
		}
		return saveDir;
	}
	// 获取文件名图片名
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	// 获取录音文件名
	private String getRecordFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'RECORD'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".3gp";
	}
}
