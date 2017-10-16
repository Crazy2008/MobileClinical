package com.winning.mobileclinical.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MessageUtils {

	public static void showToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void showMsgToastCenter(Context context, String str) {
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showMsgToastBottom(Context context, String str) {
		Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}

	// 遍历循环Viewgroup内的所有View元素
	public interface ViewTask {
		void Excute(View v);
	}

	public static void test(ViewGroup view_group, ViewTask task) {
		int k = view_group.getChildCount();
		for (int i = 0; i < k; i++) {
			View v = view_group.getChildAt(i);
			task.Excute(v);
		}
	}
	
	public static void showMsgDialog(Context context, String str){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(str)
				.setCancelable(false)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
