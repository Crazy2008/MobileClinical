package com.winning.mobileclinical.layout;

import java.util.ArrayList;
import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.layout.MenuViewPager.OnChangeListener;
import com.winning.mobileclinical.model.MenuDTO;
import com.winning.mobileclinical.utils.ViewUtil;
import com.winning.mobileclinical.web.SystemUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
/**
 * 患者项下功能菜单显示
 * @author hanmo
 *
 */
public class TabMenuViewPager extends LinearLayout {
	private List<LinearLayout> mTabs = new ArrayList<LinearLayout>();
	private int currentItem;
	private OnTabChangeListener tab_changelistener;
	private List<TextView> mtvs = new ArrayList<TextView>();
	private WindowManager wm = null;
	private int width=0;
	List<MenuDTO> menus = new ArrayList<MenuDTO>();
	
	public TabMenuViewPager(Context context) {
		super(context);
		init();
	}
	
	
	public TabMenuViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init(){
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setBackgroundColor(Color.parseColor("#ffffff"));
		
		wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
	}	
	
	public void setOnTabChangeListener(OnTabChangeListener l)
	{
		tab_changelistener=l;
	}
	
	public void selectTabButton2(){
		for(int i = 0;i<mTabs.size();i++){
			int tabint =  (Integer) mTabs.get(i).getTag();
			if(tabint==currentItem){
//				mtvs.get(i).setTextColor(Color.parseColor("#EFEFEF"));
//				mTabs.get(i).setBackgroundColor(Color.parseColor("#FF860B"));
				
				Drawable drawable = getContext().getResources().getDrawable(R.drawable.pat_toptab_shape); 
				drawable.setBounds(0, 0, width, ViewUtil.diptopx(getContext(),5)); 
				mtvs.get(i).setCompoundDrawables(null, null, null, drawable);
				tab_changelistener.onTabPageChangeListener(menus.get(currentItem));
			}
			else 
			{
				mtvs.get(i).setCompoundDrawables(null, null, null, null);
//				mtvs.get(i).setTextColor(Color.parseColor("#EFEFEF"));
//				mTabs.get(i).setBackgroundColor(Color.parseColor("#00279B"));
			}
		}
	}
	
	public void selectTabButtonBwl(){
		for(int i = 0;i<mTabs.size();i++){
			int tabint =  (Integer) mTabs.get(i).getTag();
			mtvs.get(i).setCompoundDrawables(null, null, null, null);
//				mtvs.get(i).setTextColor(Color.parseColor("#EFEFEF"));
//				mTabs.get(i).setBackgroundColor(Color.parseColor("#00279B"));
		}
	}

//	public void checkTabButton(CheckBox tab){
//		for(int i = 0;i<mTabs.size();i++){
//			if(mTabs.get(i) == tab){
////				tab_changelistener.onTabPageChangeListener(menus.get(currentItem));
//			}
//			else
//			{
//				mTabs.get(i).setChecked(false);
//				mTabs.get(i).setTextColor(Color.parseColor("#999999"));
//				mTabs.get(i).setClickable(true);
//			}
//		}
//		tab.setTextColor(Color.parseColor("#ffffff"));
//		tab.setClickable(false);
//		
//	}
	
	public LinearLayout getTabButton(String title, int width){
		LinearLayout tab = new LinearLayout(getContext());
//		tab.setLayoutParams(new LayoutParams(
//		        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		tab.setLayoutParams(new LayoutParams(
				width, LayoutParams.MATCH_PARENT));
		tab.setGravity(Gravity.CENTER);
		
//		tab.setTextSize(24);
//		tab.setSingleLine();
//		tab.setGravity(Gravity.CENTER);
//		tab.setButtonDrawable(getResources().getDrawable(android.R.color.transparent));
//		tab.setBackgroundResource(R.drawable.tab_btn);
//		tab.setPadding(10,10,10,10);
//		tab.setTextColor(Color.parseColor("#999999"));
//
//		tab.setText(title);
////		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
//		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
//		layoutParams.setMargins(0, 0, 10, 10);
//		
//		tab.setLayoutParams(layoutParams);
//		tab.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				checkTabButton((CheckBox) arg0);
//			}
//		});
		
		TextView tView=new TextView(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//		params.setMargins(ViewUtil.diptopx(getContext(),10), 0, ViewUtil.diptopx(getContext(),10), ViewUtil.diptopx(getContext(),10));
		tView.setLayoutParams(params);
		tView.setText(title);
//		tView.setTextColor(Color.parseColor("#999999"));
		tView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
                getResources().getDimensionPixelSize(R.dimen.pm_menu_text));
		tView.setGravity(Gravity.CENTER);
		Drawable drawable = getContext().getResources().getDrawable(R.drawable.pat_toptab_shape); 
		drawable.setBounds(0, 0, width, ViewUtil.diptopx(getContext(),5)); 
		tView.setCompoundDrawables(null, null, null, drawable);
		tab.addView(tView);
		tab.setTag(mTabs.size());
		mTabs.add(tab);
		mtvs.add(tView);
		return tab;
	}
	
	public void setCurrentItem(int item){
		this.currentItem = item;
		selectTabButton2();
	}
	
	public int getTabCount(){
		return mTabs.size();
	}
	

	public int getCurrentItem() {
		return currentItem;
	}
	
	public interface OnTabChangeListener{
		public void onTabPageChangeListener(MenuDTO menuDTO);
	}

	public void removeAllMenus() {
		// TODO Auto-generated method stub
		menus.clear();
		mTabs.clear();
		mtvs.clear();
		this.removeAllViews();
		init();
	}
	public void addMenus(List<MenuDTO> menus,int btnWidth) {
		// TODO Auto-generated method stub
		width = (wm.getDefaultDisplay().getWidth()-btnWidth)/menus.size();
		for(int i=0;i<menus.size();i++){
	    	LinearLayout tab = getTabButton(menus.get(i).getText(), width);
	    	tab.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int sel =  (Integer) arg0.getTag();
					
					if(sel != currentItem)
					{
						currentItem = sel;
						selectTabButton2();
					}
				}
			});
			
			this.addView(tab);
			this.menus.add(menus.get(i));
	    }

	}
}
