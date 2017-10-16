package com.winning.mobileclinical.adapter;

import java.util.List;



import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;

/**
 * 显示病区科室对照列表
 * @author hanmo
 *
 */
public class LoginBqksAdapter extends BaseAdapter {
	private class ListHolder {
		TextView ks_text;
		TextView bq_text ;
	}
	
	private Context context;
	private List<DeptWardMapInfo> list;
	private LayoutInflater mInflater;
	private int cur_index;
    
	public LoginBqksAdapter(Context c, List<DeptWardMapInfo> list, int cur_index) {
		this.list = list;
		this.context = c;
		this.cur_index = cur_index;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount() {
		return list.size();
	}

	public Object getItem(int index) {
		return list.get(index);
	}

	public long getItemId(int index) {
		return index;
	}

	public View getView(int index, View convertView, ViewGroup parent) {
		ListHolder holder;
		if (convertView == null) {
//			convertView = mInflater.inflate(R.layout.loginbqksmapdrop, null);
			convertView = mInflater.inflate(R.layout.bqksmapdrop, null);
			holder = new ListHolder();
			
//			holder.ks_text = (TextView)convertView.findViewById(R.id.login_ks);
//			holder.bq_text = (TextView)convertView.findViewById(R.id.login_bq);
			
			holder.ks_text = (TextView)convertView.findViewById(R.id.bqksmap_ks);
			holder.bq_text = (TextView)convertView.findViewById(R.id.bqksmap_bq);
			convertView.setTag(holder);

		} else {
			holder = (ListHolder) convertView.getTag();
		}
		final DeptWardMapInfo mapinfo = list.get(index);
		if(mapinfo != null)
		{
			holder.ks_text.setText(mapinfo.getKsmc());
			holder.bq_text.setText(mapinfo.getBqmc());
		}
		
		if(index == cur_index){
			if(cur_index == 0){
	        	if(cur_index == list.size()-1){
	        		convertView.setBackgroundResource(R.drawable.list_corner_round);
	        	}else{
	        		convertView.setBackgroundResource(R.drawable.list_corner_top);
	        	}
	        } else if(cur_index == list.size()-1){
	        	convertView.setBackgroundResource(R.drawable.list_corner_bottom);
	        } else{
	        	convertView.setBackgroundResource(R.drawable.list_corner_center);
	        }
		}else{
			convertView.setBackgroundDrawable(new BitmapDrawable());
		}
		return convertView;
	}
	public void setList(List<DeptWardMapInfo> list) {
		this.list = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public List<DeptWardMapInfo> getList() {
		return list;
	}

	public void setItem(DeptWardMapInfo msg) {
		// TODO Auto-generated method stub
		
	}
}
