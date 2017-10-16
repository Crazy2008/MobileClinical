package com.winning.mobileclinical.widget;


import com.winning.mobileclinical.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

public class MyProgressDialog extends Dialog
{

	private Context context = null;
	private int screenheight;
	private int screenwidth;
	private static MyProgressDialog myProgressDialog = null;
	private static MyProgress myProgress = null;
	private static TextView custommyprogressdialog_title = null;

	public MyProgressDialog(Context context)
	{
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public MyProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public MyProgressDialog(Context context, int theme, int screenheight, int screenwidth)
	{
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.screenheight = screenheight;
		this.screenwidth = screenwidth;
	}

	public static MyProgressDialog createMyDialog(Context context, int screenheight, int screenwidth)
	{
		myProgressDialog = new MyProgressDialog(context, R.style.my_dialog, screenheight, screenwidth);
		myProgressDialog.setContentView(R.layout.custommyprogressdialog);
		WindowManager.LayoutParams params = myProgressDialog.getWindow().getAttributes();
		params.width = screenwidth * 3 / 5;
		params.height = screenheight * 1 / 7;
		myProgressDialog.getWindow().setAttributes(params);
		myProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		myProgress = (MyProgress) myProgressDialog.findViewById(R.id.progressBar);
		return myProgressDialog;
	}

	/**
	 * 
	 * [Summary] setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public MyProgressDialog setTitile(String strTitle)
	{
		custommyprogressdialog_title = (TextView) myProgressDialog.findViewById(R.id.custommyprogressdialog_title);
		custommyprogressdialog_title.setText(strTitle);
		return myProgressDialog;
	}

	public MyProgress getMyProgress()
	{
		return myProgress;
	}
	

}
