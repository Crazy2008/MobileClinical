package com.winning.mobileclinical.form;

import com.winning.mobileclinical.model.MBControl;

import android.widget.TextView;

public interface CustomForm {

	public String getTextValue();
	public void setTextValue(String text);
	public void clear();
	public TextView getLabel();
	public void setGravity(int gravity);
	public MBControl getMb();
	public void setMb(MBControl mb);
	
	
}
