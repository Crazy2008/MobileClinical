package com.winning.mobileclinical.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.globalCache.GlobalCache;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 主题功能菜单显示
 * @author liu
 *
 */
public class MenuViewPager extends LinearLayout implements Serializable {
	
	private List<LinearLayout> mTabs = new ArrayList<LinearLayout>();
	private List<ImageView> mivs = new ArrayList<ImageView>();
	private List<TextView> mtvs = new ArrayList<TextView>();
	
	private LinearLayout ll_badge = null;
	
	private ImageView badgeView = null;
	
	BadgeView badge = null;
	
//	private List<LinearLayout> mTabs1 = new ArrayList<LinearLayout>();

	List<MenuDTO> menus = new ArrayList<MenuDTO>();
	
	private int currentItem;
	
	private OnChangeListener changelistener;
	
	
	public MenuViewPager(Context context) {
		super(context);
		init();
	}
	
	
	public MenuViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init(){
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.setBackgroundColor(Color.parseColor("#00279B"));
		this.setGravity(Gravity.CENTER);
	}
	
	public void setOnItemChangeListener(OnChangeListener l)
	{
		changelistener=l;
	}

	public void selectTabButton2(){
		for(int i = 0;i<mTabs.size();i++){
			int tabint =  (Integer) mTabs.get(i).getTag();
			String icon = (String) mivs.get(i).getTag();
			if(tabint==currentItem){
				mtvs.get(i).setTextColor(Color.parseColor("#EFEFEF"));
//				mivs.get(i).setImageResource(SystemUtil.getDrawbleIdByName(getContext(), icon+""));
				mivs.get(i).setImageResource(SystemUtil.getDrawbleIdByName(getContext(), icon+"_sel"));
				mTabs.get(i).setBackgroundColor(Color.parseColor("#FF860B"));
//				mTabs.get(i).setLayoutParams(new LayoutParams(
//						ViewUtil.diptopx(getContext(),90), ViewUtil.diptopx(getContext(),90)));
				
				changelistener.onPageChangeListener(menus.get(currentItem));
			}
			else 
			{
				mtvs.get(i).setTextColor(Color.parseColor("#EFEFEF"));
				mTabs.get(i).setBackgroundColor(Color.parseColor("#00279B"));
				mivs.get(i).setImageResource(SystemUtil.getDrawbleIdByName(getContext(), icon+""));
			}
		}
	}
	
	
	public void selectTabButtonImage(){
		for(int i = 0;i<mTabs.size();i++){
			int tabint =  (Integer) mTabs.get(i).getTag();
			String icon = (String) mivs.get(i).getTag();
			if(tabint==currentItem){
				mtvs.get(i).setTextColor(Color.parseColor("#EFEFEF"));
//				mivs.get(i).setImageResource(SystemUtil.getDrawbleIdByName(getContext(), icon+""));
				if(ll_badge != null) {
					badge.hide();
				}
				mivs.get(i).setImageResource(SystemUtil.getDrawbleIdByName(getContext(), icon+"_sel"));
				mTabs.get(i).setBackgroundColor(Color.parseColor("#FF860B"));
//				mTabs.get(i).setLayoutParams(new LayoutParams(
//						ViewUtil.diptopx(getContext(),90), ViewUtil.diptopx(getContext(),90)));
				
				changelistener.onPageChangeListener(menus.get(currentItem));
			}
			else 
			{
				mtvs.get(i).setTextColor(Color.parseColor("#EFEFEF"));
				mTabs.get(i).setBackgroundColor(Color.parseColor("#00279B"));
				mivs.get(i).setImageResource(SystemUtil.getDrawbleIdByName(getContext(), icon+""));
			}
		}
	}
	
	
	public void selectTabButtonBwl(){
		for(int i = 0;i<mTabs.size();i++){
			String icon = (String) mivs.get(i).getTag();
				mtvs.get(i).setTextColor(Color.parseColor("#EFEFEF"));
				mTabs.get(i).setBackgroundColor(Color.parseColor("#00279B"));
				mivs.get(i).setImageResource(SystemUtil.getDrawbleIdByName(getContext(), icon+""));
		}
	}
	
	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			int id = Integer.parseInt(source);

			// 根据id从资源文件中获取图片对象
			Drawable d = getResources().getDrawable(id);
			d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			return d;
		}
	};   
	
	public void setConunt(){
		if(badge != null) {
			badge.setText(GlobalCache.getCache().getWjzcount() + "");
			if(GlobalCache.getCache().getWjzcount() == 0) {
				badge.hide();
			} else {
				badge.show();
			}
		}
	}
	
	
	
	public LinearLayout getTabButton(String title,String icon,int orderby){
		if(orderby == 2){
			LinearLayout ll = new LinearLayout(getContext());
			ll.setLayoutParams(new LayoutParams(
					//TODO
					ViewUtil.diptopx(getContext(),127), ViewGroup.LayoutParams.WRAP_CONTENT));
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setGravity(Gravity.CENTER_HORIZONTAL);
			ll.setPadding(ViewUtil.diptopx(getContext(),15), 0, ViewUtil.diptopx(getContext(),15), 0);

			ImageView iView=new ImageView(getContext());
			LayoutParams params = new LayoutParams(ViewUtil.diptopx(getContext(),25),ViewUtil.diptopx(getContext(),25));
			params.setMargins(ViewUtil.diptopx(getContext(),6), ViewUtil.diptopx(getContext(),13.5f), ViewUtil.diptopx(getContext(),6), ViewUtil.diptopx(getContext(),5));
			iView.setLayoutParams(params);
			iView.setImageResource(SystemUtil.getDrawbleIdByName(getContext(), icon));
			iView.setTag(icon);
			
			ll_badge = new LinearLayout(getContext());
			ll_badge.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			ll_badge.setOrientation(LinearLayout.HORIZONTAL);
			ll_badge.setGravity(Gravity.CENTER_HORIZONTAL);
			ll_badge.setPadding(0, 0, 0, 0);
			ll_badge.addView(iView);
			badgeView=new ImageView(getContext());
			LayoutParams paramsba= new LayoutParams(ViewUtil.diptopx(getContext(),12),ViewUtil.diptopx(getContext(),12));
			paramsba.setMargins(0, 0, 0, 0);
			badgeView.setLayoutParams(paramsba);
			ll_badge.addView(badgeView);
			ll.addView(ll_badge);		
			badge = new BadgeView(getContext(), ll_badge);
			Log.d("tag","MenuViewPage====================================GlobalCache.getCache().getWjzcount()="+GlobalCache.getCache().getWjzcount());
			badge.setText(GlobalCache.getCache().getWjzcount() + "");
			
			if(GlobalCache.getCache().getWjzcount() == 0) {
				badge.hide();
			} else {
				badge.show();
			}
			
//			ll.removeView(ll_badge);
//			ll_badge.setVisibility(View.GONE);
			
			TextView tView=new TextView(getContext());
			LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params1.setMargins(ViewUtil.diptopx(getContext(),6), 0, ViewUtil.diptopx(getContext(),15), ViewUtil.diptopx(getContext(),9));
			tView.setLayoutParams(params1);
			tView.setText(title);
			tView.setTextColor(Color.parseColor("#ffffff"));
			tView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
	                getResources().getDimensionPixelSize(R.dimen.mvp_menu_bottom));
			tView.setGravity(Gravity.CENTER);
			ll.addView(tView);
			ll.setTag(mTabs.size());
			
			mTabs.add(ll);
			mivs.add(iView);
			mtvs.add(tView);
			return ll;
		}else{
			LinearLayout ll = new LinearLayout(getContext());
			ll.setLayoutParams(new LayoutParams(
					ViewUtil.diptopx(getContext(),120), LayoutParams.WRAP_CONTENT));
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setGravity(Gravity.CENTER);
			ll.setPadding(ViewUtil.diptopx(getContext(),15), 0, ViewUtil.diptopx(getContext(),15), 0);
//			ll.setBackgroundColor(getResources().getColor(R.color.white));
			ImageView iView=new ImageView(getContext());
//			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			LayoutParams params = new LayoutParams(ViewUtil.diptopx(getContext(),25),ViewUtil.diptopx(getContext(),25));
			params.setMargins(ViewUtil.diptopx(getContext(),6), ViewUtil.diptopx(getContext(),15), ViewUtil.diptopx(getContext(),6), ViewUtil.diptopx(getContext(),5));
			iView.setLayoutParams(params);
			iView.setImageResource(SystemUtil.getDrawbleIdByName(getContext(), icon));
			
//			iView.setImageResource(R.drawable.yizhu);
			iView.setTag(icon);
			ll.addView(iView);
			TextView tView=new TextView(getContext());
			LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params1.setMargins(ViewUtil.diptopx(getContext(),6), 0, ViewUtil.diptopx(getContext(),6), ViewUtil.diptopx(getContext(),6));
			tView.setLayoutParams(params1);
			tView.setText(title);
			tView.setTextColor(Color.parseColor("#ffffff"));
			tView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
	                getResources().getDimensionPixelSize(R.dimen.gv_menu_left));
			tView.setGravity(Gravity.CENTER);
			ll.addView(tView);
			ll.setTag(mTabs.size());
			
//			ll.setBackgroundResource(R.drawable.menu_btn);

			mTabs.add(ll);
			mivs.add(iView);
			mtvs.add(tView);
			return ll;
		}
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
	
	
	public interface OnChangeListener{
		public void onPageChangeListener(MenuDTO menuDTO);
	}

	public void removeAllMenus() {
		// TODO Auto-generated method stub
		menus.clear();
		mTabs.clear();
		mivs.clear();
		mtvs.clear();
		this.removeAllViews();
		init();
	}


	public void addMenus(List<MenuDTO> menus) {
		// TODO Auto-generated method stub
		for (int i = 0; i < menus.size(); i++) {
			LinearLayout tab = getTabButton(menus.get(i).getText(),menus.get(i).getImageurl(),menus.get(i).getOrderby());
			
//			LinearLayout line = new LinearLayout(getContext());
//			ImageView iView=new ImageView(getContext());
//			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewUtil.diptopx(getContext(),Float.parseFloat("0.5")));
//			iView.setLayoutParams(params);
//			iView.setBackgroundColor(Color.parseColor("#666666"));
//			line.addView(iView);
			
			final MenuDTO menu = menus.get(i);

			tab.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int sel =  (Integer) arg0.getTag();
					
					if((sel != currentItem)||(sel == currentItem && menu.getSuptype()==1))
					{
						currentItem = sel;
						
						if(menu.getOrderby()==2) {
							selectTabButtonImage();
						} else{
							selectTabButton2();
						}
//						mViewPager.setCurrentItem(sel);
					} 
					
//					else if(sel == currentItem && menu.getOrderby()==2) {
//						currentItem = sel;
//						
//					}
				}
			});
			
			this.addView(tab);
//			this.addView(line);
			this.menus.add(menus.get(i));
		}
		
	}
}
