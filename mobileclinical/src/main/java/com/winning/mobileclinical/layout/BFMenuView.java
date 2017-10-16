package com.winning.mobileclinical.layout;

import java.util.ArrayList;
import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.cis.BedSearchinfo;
import com.winning.mobileclinical.model.cis.Bedinfo;
import com.winning.mobileclinical.utils.ViewUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

@SuppressLint("NewApi")
public class BFMenuView extends LinearLayout {
	
//	private List<CheckBox> mTabs = new ArrayList<CheckBox>();
	private List<LinearLayout> mTabs = new ArrayList<LinearLayout>();
	private List<TextView> mtvs = new ArrayList<TextView>();
	List<BedSearchinfo> menus = new ArrayList<BedSearchinfo>();
	private int currentItem;

	private OnChangeListener changelistener;
	
	
	public BFMenuView(Context context) {
		super(context);
		init();
	}
	
	
	public BFMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init(){
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setBackgroundColor(Color.parseColor("#f2f2f2"));
		
		
	}
	
	public void setOnItemChangeListener(OnChangeListener l)
	{
		changelistener=l;
	}
	
	public void selectTabButton(){
		for(int i = 0;i<mTabs.size();i++){
			int tabint =  (Integer) mTabs.get(i).getTag();
			if(tabint==currentItem){
				mtvs.get(i).setTextColor(Color.parseColor("#FFFFFF"));
//				mTabs.get(i).setBackgroundColor(Color.parseColor("#FF860B"));
				mTabs.get(i).setBackground(getResources().getDrawable(R.drawable.ward_sel));
				changelistener.onPageChangeListener(menus.get(currentItem));
			}
			else 
			{
//				mtvs.get(i).setCompoundDrawables(null, null, null, null);
				mtvs.get(i).setTextColor(Color.parseColor("#A4A4A4"));
//				mTabs.get(i).setBackgroundColor(Color.parseColor("#00279B"));
				mTabs.get(i).setBackground(getResources().getDrawable(R.drawable.ward));
			}
		}
	}

//	public void checkTabButton(CheckBox tab){
//		for(int i = 0;i<mTabs.size();i++){
//			if(mTabs.get(i)!=tab){
//				mTabs.get(i).setChecked(false);
//				mTabs.get(i).setTextColor(Color.parseColor("#A4A4A4"));
////				mTabs.get(i).setBackground(getResources().getDrawable(android.R.color.transparent));
//				mTabs.get(i).setBackgroundResource(R.drawable.ward);
//				mTabs.get(i).setClickable(true);
//			}
//			else
//			{
//				changelistener.onPageChangeListener(menus.get(i));
//			}
//		}
////		tab.setBackground(getResources().getDrawable(R.drawable.test));
//		tab.setBackgroundResource(R.drawable.ward_sel);
//		tab.setTextColor(Color.parseColor("#ffffff"));
//		tab.setClickable(false);
//		
//	}
	
//	public CheckBox getTabButton(String title){
//		CheckBox tab = new CheckBox(getContext());
//		
//		
//		tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
//                getResources().getDimensionPixelSize(R.dimen.fpd_menu));
//		tab.setSingleLine();
//		tab.setGravity(Gravity.CENTER);
////		tab.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
////		tab.setBackgroundResource(R.drawable.tab_btn);
////		tab.setPadding(ViewUtil.diptopx(getContext(),0),ViewUtil.diptopx(getContext(),0),ViewUtil.diptopx(getContext(),10),ViewUtil.diptopx(getContext(),10));
//		tab.setTextColor(Color.parseColor("#A4A4A4"));
////		tab.setBackgroundColor(Color.parseColor("#00EE00"));
//		tab.setBackgroundResource(R.drawable.ward);
//		tab.setText(title+"房");
//		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,ViewUtil.diptopx(getContext(),60),1);
//		layoutParams.setMargins(0, 0, 0, 0);
//		tab.setLayoutParams(layoutParams);
//		tab.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				checkTabButton((CheckBox) arg0);
//			}
//		});
//		return tab;
//	}
	
	public LinearLayout getTabButton(String title){
		LinearLayout tab = new LinearLayout(getContext());
		tab.setLayoutParams(new LayoutParams(
				ViewUtil.diptopx(getContext(),250), ViewUtil.diptopx(getContext(),70)));

		tab.setGravity(Gravity.CENTER);	
		tab.setBackgroundColor(Color.parseColor("#50000000"));
		TextView tView=new TextView(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tView.setLayoutParams(params);
		tView.setText(title+"房");
		tView.setTextColor(Color.parseColor("#A4A4A4"));
		tView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
                getResources().getDimensionPixelSize(R.dimen.pm_menu_text));
//		tView.setBackground(getResources().getDrawable(R.drawable.ward));
		tView.setGravity(Gravity.CENTER);
		tab.addView(tView);
		tab.setTag(mTabs.size());
		mTabs.add(tab);
		mtvs.add(tView);
		return tab;
	}
	
	public void setCurrentItem(int item){
		this.currentItem = item;
//		if(mTabs.size()>item&&!mTabs.get(item).isChecked())
//		{
//			mTabs.get(item).setChecked(true);
//			checkTabButton(mTabs.get(item));
//		}
		selectTabButton();
	}
	
	public int getTabCount(){
		return mTabs.size();
	}
	

	public int getCurrentItem() {
		return currentItem;
	}
	
//	public String getCurretnTab(){
//		return mTabs.get(currentItem).getText().toString();
//	}
	
	public interface OnChangeListener{
		public void onPageChangeListener(BedSearchinfo menu);
	}

	public void removeAllMenus() {
		// TODO Auto-generated method stub
		menus.clear();
		mTabs.clear();
		this.removeAllViews();
		init();
	}


	public void addMenus(List<BedSearchinfo> menus) {
		// TODO Auto-generated method stub
		for (int i = 0; i < menus.size(); i++) {
//			CheckBox tab = getTabButton(menus.get(i).getRoom());
//			tab.setTag(mTabs.size());
//			this.addView(tab);
//			mTabs.add(tab);
//			this.menus.add(menus.get(i));
			
			LinearLayout tab = getTabButton(menus.get(i).getRoom());
	    	tab.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int sel =  (Integer) arg0.getTag();
					
					if(sel != currentItem)
					{
						currentItem = sel;
						selectTabButton();
					}
				}
			});
			
			this.addView(tab);
			this.menus.add(menus.get(i));
		}
		
	}
}
