package com.winning.mobileclinical.dialog;
import java.util.ArrayList;

import com.winning.mobileclinical.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;


/**
 * 提醒窗口帮助类
 * 
 * */
public class DialogHelper {

	public static void showAlert(Context context,String title,String content){
		AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
		builder.setTitle(title)
		       .setMessage(content)
			   .setCancelable(false)
			   .setPositiveButton("确定",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
    public static void showAlert(Context context,String title){
    	AlertDialog.Builder builder = new AlertDialog.Builder(
				context);
		builder.setTitle(title)
			   .setCancelable(false)
			   .setPositiveButton("确定",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
    
    public static void showAlert(Context context,String title,String content,int drawable){
    	showAlert(context, title, content, drawable,null);
    }
    
    public static void showAlert(Context context,String title,String content,int drawable,
    		final dialogListener okListener){
    	final Dialog dialog = new Dialog(context, R.style.xxdy_dialog);
    	dialog.setContentView(R.layout.dialog_with_drawable);
    	dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	ImageView mImg = (ImageView)dialog.findViewById(R.id.dialog_img);
    	mImg.setImageResource(drawable);
    	TextView mTitle = (TextView) dialog.findViewById(R.id.dialog_title);
    	mTitle.setText(title);
    	TextView mContent = (TextView) dialog.findViewById(R.id.dialog_content);
    	mContent.setText(content);
    	Button btnok = (Button) dialog.findViewById(R.id.dialog_ok);
    	
    	
    	//如果listener 为空
    	if(okListener==null){
    		btnok.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				dialog.dismiss();
    			}
    		});
    		Button btncancel = (Button) dialog.findViewById(R.id.dialog_cancel);
        	btncancel.setVisibility(View.GONE);
        	dialog.show();
        	return ;
    	}
    	btnok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				okListener.onOk(dialog);
			}
		});
    	Button btncancel = (Button) dialog.findViewById(R.id.dialog_cancel);
    	btncancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				okListener.oncancel(dialog);
				dialog.dismiss();
			}
		});
    	dialog.show();
    }

    public static interface dialogListener{
    	public void onOk(Dialog dialog);
    	public void oncancel(Dialog dialog);
    }
    
    public static interface comfrimListener{
    	public void onComfirm(Dialog dialog, String result);
    	public void oncancel(Dialog dialog);
    }
    
    public static void showMyToast(Context context,String title,String content){
		
	}
    
    public static void showMyToast(Context context,String title){
		
	}
    

}
