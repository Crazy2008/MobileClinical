package com.winning.mobileclinical.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.adapter.GridMenuAdapter;
import com.winning.mobileclinical.fragment.Memorandum;
import com.winning.mobileclinical.model.TuYa;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.widget.MultiModel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class TuYaActivity extends Activity {

	public TuYa viewWaller;
	private Button backBtn, sureBtn;
	private LinearLayout layout;
	ArrayList<TuYa.DrawPath> list = new ArrayList<TuYa.DrawPath>();
	private String tuyaFileName = "";

	private GridView grid;
	private GridView colorgrid,brushgrid;
	private String[] text = new String[] { "笔触", "颜色", "撤销", "清空" };
	private String[] text1 = new String[] { "", "", "", "", "", "", "", "", "" };
	private String[] images = new String[] { "tab_brush", "tab_color",
			"tab_cancel", "tab_delete" };
	private String[] brushes = new String[] { "brush1", "brush2",
			"brush3", "brush4", "brush5", "brush6", "brush7", "brush8" };
	private String[] images_color = new String[] { "color1", "color2",
			"color3", "color4", "color5", "color6", "color7", "color8",
			"color9" };

	private PopupWindow pop;
	private boolean sdexiting;					//bwl002
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuya_layout);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			sdexiting = true;	//bwl002
		//	init();				//bwl002
		} else {
			sdexiting = false;	//bwl002
		//	Toast.makeText(TuYaActivity.this, "SD卡不可用",Toast.LENGTH_LONG).show();
		//	Intent intent = new Intent(TuYaActivity.this, BWLNew.class);
		//	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		//			| Intent.FLAG_ACTIVITY_NEW_TASK);
			// MainActivity.act.switchActivity(intent, ViewUtil.MENU_BWLNEW, 2);
		//	finish();
		}
		init();					//bwl002

	}

	@SuppressWarnings("deprecation")
	private void init() {
		grid = (GridView) findViewById(R.id.tuyaGrid);
		GridMenuAdapter gridAdapter = new GridMenuAdapter(TuYaActivity.this, text,
				images);

		grid.setAdapter(gridAdapter);
		grid.setOnItemClickListener(new TuYaItemClickListener());

		int screenW = getWindowManager().getDefaultDisplay().getWidth();
		int screenH = getWindowManager().getDefaultDisplay().getHeight();
		viewWaller = new TuYa(this, screenW, screenH - 100);

		layout = (LinearLayout) findViewById(R.id.tuya);
		backBtn = (Button) findViewById(R.id.tuya_back);
		sureBtn = (Button) findViewById(R.id.tuya_sure);

		backBtn.setOnClickListener(BtnListener);
		sureBtn.setOnClickListener(BtnListener);
		layout.addView(viewWaller);
		viewWaller.setDrawingCacheEnabled(true);
	}

	class TuYaItemClickListener implements OnItemClickListener {
		@SuppressLint("NewApi")
		@Override
		public void onItemClick(AdapterView<?> p, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			View v = (View) grid.getChildAt(arg2);
			if (arg2 == 0) {
				ImageView image = null;
				LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
				if (pop == null) {
					View layout = LayoutInflater.from(TuYaActivity.this).inflate(
							R.layout.tuya_brush_layout, null);
					image = (ImageView) layout.findViewById(R.id.tuya_brush);
					brushgrid = (GridView) layout.findViewById(R.id.tuya_brushGrid);
					GridMenuAdapter gridAdapter = new GridMenuAdapter(TuYaActivity.this, text1,
							brushes);

					brushgrid.setAdapter(gridAdapter);
					brushgrid.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							switch (position) {
							case 0:
								viewWaller.setStrokeWidth(4);
								break;
							case 1:
								viewWaller.setStrokeWidth(5);
								break;
							case 2:
								viewWaller.setStrokeWidth(6);
								break;
							case 3:
								viewWaller.setStrokeWidth(7);
								break;
							case 4:
								viewWaller.setStrokeWidth(8);
								break;
							case 5:
								viewWaller.setStrokeWidth(9);
								break;
							case 6:
								viewWaller.setStrokeWidth(10);
								break;
							case 7:
								viewWaller.setStrokeWidth(11);
								break;
							case 8:
								viewWaller.setStrokeWidth(12);
								break;
							default:
								break;
							}
							if(pop.isShowing()) {
								pop.dismiss();
							}
						}
					});
					pop = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					pop.setBackgroundDrawable(new BitmapDrawable());
					pop.setFocusable(true);
					pop.setOutsideTouchable(true);
					pop.update();
				}

				if (pop.isShowing()) {
					pop.dismiss();
				} else {
					int xPx = (ViewUtil.getWidth(TuYaActivity.this)/4)/2;
					lParams.leftMargin = xPx;
					image.setLayoutParams(lParams);
					pop.showAsDropDown(v,0,20);
				}
				pop.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						pop = null;
					}
				});			
			} else if (arg2 == 1) {
				ImageView image = null;
				LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
				if (pop == null) {
					View layout = LayoutInflater.from(TuYaActivity.this).inflate(
							R.layout.tuya_color_layout, null);
					image = (ImageView) layout.findViewById(R.id.tuya_image);
					colorgrid = (GridView) layout.findViewById(R.id.tuya_colorGrid);
					GridMenuAdapter gridAdapter = new GridMenuAdapter(TuYaActivity.this, text1,
							images_color);

					colorgrid.setAdapter(gridAdapter);
					colorgrid.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							switch (position) {
							case 0:
								viewWaller.setColor("#FF0000");
								break;
							case 1:
								viewWaller.setColor("#A52A2A");
								break;
							case 2:
								viewWaller.setColor("#FFA500");
								break;
							case 3:
								viewWaller.setColor("#FFFF00");
								break;
							case 4:
								viewWaller.setColor("#00FF00");
								break;
							case 5:
								viewWaller.setColor("#00FF7F");
								break;
							case 6:
								viewWaller.setColor("#00FFFF");
								break;
							case 7:
								viewWaller.setColor("#FF1493");
								break;
							case 8:
								viewWaller.setColor("#800080");
								break;
							default:
								break;
							}
							if(pop.isShowing()) {
								pop.dismiss();
							}
						}
					});
					pop = new PopupWindow(layout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					pop.setBackgroundDrawable(new BitmapDrawable());
					pop.setFocusable(true);
					pop.setOutsideTouchable(true);
					pop.update();
				}

				if (pop.isShowing()) {
					pop.dismiss();
				} else {
					int xPx = (ViewUtil.getWidth(TuYaActivity.this))/2 - v.getWidth()/2;
					lParams.leftMargin = xPx;
					image.setLayoutParams(lParams);
					pop.showAsDropDown(v,0,15);
				}
				pop.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						pop = null;
					}
				});
			}else if(arg2==2){
				viewWaller.undo();
			}else if(arg2==3){
				viewWaller.clear();
			}
		}
	}

	OnClickListener BtnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == backBtn) {
//				viewWaller.clear();
				Intent intent = new Intent(TuYaActivity.this, Memorandum.class);
				MultiModel.getModel().clearData();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
				finish();
			} else if (v == sureBtn) {
				if (viewWaller.getMpath() != null && viewWaller.getMpath().size() > 0) {
					Bitmap b = viewWaller.getDrawingCache();
					String path = saveFileToSd(b);
					Intent intent = new Intent(TuYaActivity.this,
							BWLEdit.class);
					// MainActivity.act.setType(ViewUtil.MENU_BWLEdit);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
							| Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("tuyaPath", path);
					intent.putExtra("tuyaFileName", tuyaFileName);
					intent.putExtra("lb", "涂鸦");
					setResult(BWLEdit.TUYA_RESULT, intent);
					// startActivity(intent);
					finish();
				} else {
					Toast.makeText(TuYaActivity.this, "您没有涂鸦内容",
							Toast.LENGTH_LONG).show();
				}

			}
		}

	};
	
	private String saveFileToSd(Bitmap b) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		tuyaFileName = getPhotoFileName();
		String path = getMkDir() + "/" + tuyaFileName;
		File file = new File(path);
		FileOutputStream fos = null;
		if (!file.exists()) {
			try {
				fos = new FileOutputStream(file);
				fos.write(baos.toByteArray(), 0, baos.toByteArray().length);
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
						fos = null;
					}
					if (baos != null) {
						baos.close();
						baos = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return path;
	}

	// 获取文件名
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	private String getMkDir() {
		//String saveDir = Environment.getExternalStorageDirectory() + "/nurse";
		/* bwl002 Start */
		String saveDir = "";
		if(sdexiting) {
			 saveDir = Environment.getExternalStorageDirectory() + "/doctouch";
		}
		else
		{
			saveDir = "/mnt/sdcard-ext/doctouch";
		}
		/* bwl002 End */
		File dir = new File(saveDir);
		if (!dir.exists()) {
			dir.mkdir(); // 创建文件夹
		}
		return saveDir;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		Intent intent = new Intent();
//		intent.setClass(TuYaActivity.this, FirstActivity.class);
//		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d("FirseActivity--:", keyCode+"");
//		if(WebUtils.YJHJ&&keyCode==213){
//			boolean isSuccess=OneTouchCallingAction.sendMessage(TuYaActivity.this);
//			if(isSuccess){
//				Toast.makeText(TuYaActivity.this, "一键呼叫成功", Toast.LENGTH_SHORT).show();
//			}
//			return true;
//		}
		return super.onKeyUp(keyCode, event);
	}
}
