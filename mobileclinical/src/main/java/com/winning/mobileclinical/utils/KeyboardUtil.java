package com.winning.mobileclinical.utils;


import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.activity.NewBWLEditActivity;
import com.winning.mobileclinical.dialog.FindPatientDialog;
import com.winning.mobileclinical.dialog.SelectDialog;
import com.winning.mobileclinical.fragment.PatientList;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.Bwl;
import com.winning.mobileclinical.model.cis.DoctorInfo;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;

public class KeyboardUtil extends Activity {
	public static final int MODE_DIALOG = 0;
	public static final int MODE_YZADD = 1;
	public static final int MODE_MAINPAGE = 2;
	public static final int MODE_YZADD2 = 3;
	public static final int MODE_MAINPAGE_JDYY = 4;

	private Context ctx;
//	private Activity act;
	private KeyboardView keyboardView;
	private Keyboard k1;// 字母键盘
//	private Keyboard k2;// 数字键盘
	public boolean isnun = false;// 是否数据键盘
	public boolean isupper = false;// 是否大写

	private EditText ed;
		public  KeyboardUtil(){}
	public KeyboardUtil(Context ctx, KeyboardView keyview, EditText edit,int type) {
			this.ed = edit;
			this.ctx = ctx;
			this.keyboardView = keyview;
			//根据不 同的type来选择不同的布局文件
			setMode(type);
			

	
	}
	public void setMode(int type) {
		// TODO Auto-generated method stub
		if(type == MODE_YZADD)
		{
			k1 = new Keyboard(ctx, R.layout.qwerty0);
		}
		
		else if(type == MODE_MAINPAGE)
		{
			k1 = new Keyboard(ctx, R.layout.qwerty_main);
		}
		
		keyboardView.setKeyboard(k1);
		keyboardView.setEnabled(true);
//		keyboardView.setPreviewEnabled(true);
		keyboardView.setPreviewEnabled(false); //弹出框
		keyboardView.setOnKeyboardActionListener(listener);
	}
	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
			hideKeyboard();
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				
				PatientList.search("");
				
				hideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			}else if (primaryCode == Keyboard.KEYCODE_DONE) {//清空   //按房查找
//				if (editable != null && editable.length() > 0) {
//					if (start > 0) {
//						editable.delete(0, start);
//					}
//				}
				
//				Intent intent = new Intent(ctx,NewBWLEditActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				Bwl bwl = new Bwl();
//				intent.putExtra("bwl", bwl);
//				startActivity(intent);
				
//				DialogHelper.showAlert(ctx, "测试");
				DoctorInfo docInfo = null;
				if(GlobalCache.getCache().getDoctor() != null) {
					docInfo = GlobalCache.getCache().getDoctor();
				}
				
				FindPatientDialog selectDialog = new FindPatientDialog(ctx,R.style.dialog); 
//				dialog.show();
				
//				SelectDialog selectDialog = new SelectDialog(ctx,R.style.dialog);//创建Dialog并设置样式主题
				
				WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
				Window dialogWindow = selectDialog.getWindow();
		        LayoutParams lp = dialogWindow.getAttributes();
		        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//		        lp.x = 100; // 新位置X坐标
		        lp.y = ViewUtil.diptopx(ctx,80); // 新位置Y坐标
//		        lp.width = 800; // 宽度
		        lp.height = wm.getDefaultDisplay().getHeight()-ViewUtil.diptopx(ctx,80); // 高度
//		        lp.alpha = 0.7f; // 透明度
		        dialogWindow.setAttributes(lp);
//				selectDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
		        hideKeyboard();
				selectDialog.show();
				
				
			} 
			else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
				changeKey();
				keyboardView.setKeyboard(k1);

			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
				if (isnun) {
					isnun = false;
					keyboardView.setKeyboard(k1);
				} else {
					isnun = true;
//					keyboardView.setKeyboard(k2);
				}
			} else if (primaryCode == 57419) { // go left
				if (start > 0) {
					ed.setSelection(start - 1);
				}
			} else if (primaryCode == 57421) { // go right
				if (start < ed.length()) {
					ed.setSelection(start + 1);
				}
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};
	
	/**
	 * 键盘大小写切换
	 */
	private void changeKey() {
		List<Key> keylist = k1.getKeys();
		if (isupper) {//大写切换小写
			isupper = false;
			for(Key key:keylist){
				if (key.label!=null && isword(key.label.toString())) {
					key.label = key.label.toString().toLowerCase();
					key.codes[0] = key.codes[0]+32;
				}
			}
		} else {//小写切换大写
			isupper = true;
			for(Key key:keylist){
				if (key.label!=null && isword(key.label.toString())) {
					key.label = key.label.toString().toUpperCase();
					key.codes[0] = key.codes[0]-32;
				}
			}
		}
	}

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }
    
    private boolean isword(String str){
    	String wordstr = "abcdefghijklmnopqrstuvwxyz";
    	if (wordstr.indexOf(str.toLowerCase())>-1) {
			return true;
		}
    	return false;
    }

}