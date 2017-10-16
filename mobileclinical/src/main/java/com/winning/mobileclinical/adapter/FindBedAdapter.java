package com.winning.mobileclinical.adapter;

import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.cis.BedSearchinfo;
import com.winning.mobileclinical.model.cis.Bedinfo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 显示电子病例列表
 * @author zhh
 *
 */
public class FindBedAdapter extends BaseAdapter {
	private class ListHolder {
		LinearLayout fb_ll;
		ImageView fb_head;
		TextView fb_name;
		TextView fb_age;
		TextView fb_cwh;
		TextView fb_blh;
		TextView fb_zd;
		TextView fb_sex;
	}
	private Context context;

	private List<BedSearchinfo> list;
	private LayoutInflater mInflater;

	public FindBedAdapter(Context c,List<BedSearchinfo> list) {
		this.list = list;
		this.context = c;
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
			holder = new ListHolder();
			convertView = mInflater.inflate(R.layout.findbedgrid, null);
			holder.fb_head = (ImageView) convertView.findViewById(R.id.findpatient_head);
			holder.fb_ll = (LinearLayout) convertView.findViewById(R.id.findbedgrid);
			holder.fb_name = (TextView) convertView.findViewById(R.id.findpatient_name);
			holder.fb_age = (TextView) convertView.findViewById(R.id.findpatient_age);
			holder.fb_cwh = (TextView) convertView.findViewById(R.id.findpatient_cwh);
			holder.fb_blh = (TextView) convertView.findViewById(R.id.findpatient_blh);
			holder.fb_zd = (TextView) convertView.findViewById(R.id.findpatient_zd);
//			holder.fb_sex = (TextView) convertView.findViewById(R.id.findpatient_image);
			convertView.setTag(holder);

		} else {
			holder = (ListHolder) convertView.getTag();
		}
		final BedSearchinfo bedinfo = list.get(index);
		if(bedinfo != null)
		{
			if(bedinfo.getSex().trim().equals("女")){
				holder.fb_ll.setBackgroundResource(R.drawable.bedcard_red);
//				Drawable drawable = convertView.getResources().getDrawable(R.drawable.head_red);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
//				holder.fb_name.setCompoundDrawables(drawable,null,null,null);
				holder.fb_head.setBackgroundResource(R.drawable.head_red);
				holder.fb_cwh.setBackgroundResource(R.drawable.circle_red);
			}else{
				holder.fb_ll.setBackgroundResource(R.drawable.bedcard_blue);
//				Drawable drawable = convertView.getResources().getDrawable(R.drawable.head_green);
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
//				holder.fb_name.setCompoundDrawables(drawable,null,null,null);
				holder.fb_head.setBackgroundResource(R.drawable.head_blue);
				holder.fb_cwh.setBackgroundResource(R.drawable.circle_blue);
			}
			holder.fb_name.setText(bedinfo.getName());
			holder.fb_age.setText(bedinfo.getAge().replace("岁", ""));
			holder.fb_cwh.setText(bedinfo.getCwdm()+"床");
			holder.fb_blh.setText(bedinfo.getBlh());
			if(bedinfo.getZdmc().length()>10){
				holder.fb_zd.setText(bedinfo.getZdmc().substring(0, 9)+"...");
			}else{
				holder.fb_zd.setText(bedinfo.getZdmc());
			}

			
		}
	
		return convertView;
	}

}
