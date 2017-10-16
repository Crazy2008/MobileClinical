package com.winning.mobileclinical.dialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.action.BFSearchAction;
import com.winning.mobileclinical.activity.PatientBrowse;
import com.winning.mobileclinical.activity.PatientMenu;
import com.winning.mobileclinical.adapter.FindBedAdapter;
import com.winning.mobileclinical.fragment.PatientList;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.layout.BFMenuView;
import com.winning.mobileclinical.layout.BFMenuView.OnChangeListener;
import com.winning.mobileclinical.model.cis.BedSearchinfo;
import com.winning.mobileclinical.model.cis.Bedinfo;
import com.winning.mobileclinical.model.cis.DoctorInfo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

public class FindPatientDialog extends DialogChild {
	private static final int LOADMENU = 0;
	private static final int LOADDATA = 1;
	
	private int loadMode = LOADMENU;
	
	private GridView bf_gridView;		
	
	private BFMenuView MenuView = null;	
	private List<BedSearchinfo> bedmenus = null;
	private List<BedSearchinfo> bedmxs = null;
	private List<BedSearchinfo> bedlist =null;
	private DoctorInfo doctorInfo = null;
	private Button lcyl_close;
	private String ysdm;
	LinearLayout ll_menu;
	private Button bfdialog_back,bfdialog_close;
	
	
	public FindPatientDialog(Context context,int theme) {
		super(context,theme);
//		context.setTheme(R.style.translucent);
		// TODO Auto-generated constructor stub
		//加载布局文件
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View  view=inflater.inflate(R.layout.bfdialog, null);  
//		MenuView = (BFMenuView) view.findViewById(R.id.bf_ll_menu);

		ll_menu = (LinearLayout) view.findViewById(R.id.bf_ll_menu);
		bfdialog_back = (Button) view.findViewById(R.id.bfdialog_back);
		bfdialog_close = (Button) view.findViewById(R.id.bfdialog_close);
		bf_gridView = (GridView) view.findViewById(R.id.bf_gridview);
		
		if(doctorInfo != null) {
			doctorInfo = GlobalCache.getCache().getDoctor();
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog添加视图
        setContentView(view);
        loadMode = LOADMENU;
        loadDataWithNothing();
        
        bfdialog_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
        
        bfdialog_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
        
        bf_gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				System.out.println("点击病人==="+bedlist.get(arg2).getName());
//				Bundle bundle = new Bundle();
//				bundle.putString("syxh", bedlist.get(arg2).getSyxh());
//				bundle.putString("yexh", bedlist.get(arg2).getYexh());
//				Intent intent = new Intent(getContext(), PatientMenu.class);
//				intent.putExtras(bundle);
//				getContext().startActivity(intent);
				dismiss();
				PatientList.switchCFPatient(bedlist.get(arg2).getSyxh(),bedlist.get(arg2).getYexh());
			}
		});
	}
	@Override
	protected void loadDate() {
		// TODO Auto-generated method stub
		
		Map<String,Object> mapks = new HashMap<String,Object>(); 
		
		if(GlobalCache.getCache().getDeptWardMapInfos() != null) {
			
			mapks.put("bqdm", GlobalCache.getCache().getDeptWardMapInfos().get(GlobalCache.getCache().getBqSel()).getBqdm()); 
		}
		
	    JSONObject tempks = new JSONObject(mapks);
		
		if(loadMode == LOADMENU)
		{
			if(GlobalCache.getCache().getBedSearchinfos().size()>0){
				List<BedSearchinfo> templist = GlobalCache.getCache().getBedSearchinfos();
				for(int i=0;i<templist.size()-1;i++){
					for(int j=templist.size()-1;j>i;j--){
						if(templist.get(j).getRoom().equals(templist.get(i).getRoom())){
							templist.remove(j);
						}
					}
				}
				bedmenus = templist;
				Collections.sort(bedmenus, new Comparator<BedSearchinfo>() {
		            public int compare(BedSearchinfo arg0, BedSearchinfo arg1) {
		                return arg0.getRoom().compareTo(arg1.getRoom());
		            }
		        });
			}

		}
		else if(loadMode == LOADDATA)
		{
			bedmxs = GlobalCache.getCache().getBedSearchinfos();
		}
	}
	@Override
	protected void afterLoadData() {
		// TODO Auto-generated method stub
		if(loadMode == LOADMENU && bedmenus != null)
		{
			showmenu();
		}
		else if(loadMode == LOADDATA)
		{
			showListView();
		}
	}
	private void showListView() {
		// TODO Auto-generated method stub
		if(GlobalCache.getCache().getBedSearchinfos() != null) {
			
		}
		
		FindBedAdapter adapter = new FindBedAdapter(context, GlobalCache.getCache().getBedSearchinfos());
		bf_gridView.setAdapter(adapter);
	}
	/**
	 * 显示菜单 
	 */
	private void showmenu() {
		

		if(MenuView == null)
		{
			MenuView = new BFMenuView(getContext());
		}
		else
		{
			MenuView.removeAllMenus();
		}
		ll_menu.removeAllViews();
		MenuView.addMenus(bedmenus);
		ll_menu.addView(MenuView);
		
		MenuView.setOnItemChangeListener(new OnChangeListener() {
			
			@Override
			public void onPageChangeListener(BedSearchinfo meBedinfo) {
//				System.out.println(new Gson().toJson(GlobalCache.getCache().getBedSearchinfos()));
				bedlist = new ArrayList<BedSearchinfo>();
				for(int i=0;i<GlobalCache.getCache().getBedSearchinfos().size();i++){
					if(meBedinfo.getRoom().equals(GlobalCache.getCache().getBedSearchinfos().get(i).getRoom())){
						bedlist.add(GlobalCache.getCache().getBedSearchinfos().get(i));
					}
				}
//				System.out.println("listsize====="+bedlist.size());
				FindBedAdapter adapter = new FindBedAdapter(context, bedlist);
				bf_gridView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
//				loadMode = LOADDATA;
//				loadDataWithProgressDialog();
			}
		});
		
		MenuView.setCurrentItem(0);
	//	aboutpatient.setChecked(true);
	
		
		// TODO Auto-generated method stub
//		if(MenuView != null)
//		{
//			MenuView.removeAllMenus();
//			
//			MenuView.addMenus(bedmenus);
//			
//
////			if(bedmenus.size() > 0)
////			{
////				MenuView.setCurrentItem(0);
////			}
//		}
	}
	
}


