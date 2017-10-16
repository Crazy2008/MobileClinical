package com.winning.mobileclinical.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.cis.BedSearchinfo;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class CwhListViewAdapter extends BaseAdapter{
	private List<BedSearchinfo> list=new ArrayList<BedSearchinfo>();
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public CwhListViewAdapter(Context context, List<BedSearchinfo> patientlist){

		this.mContext = context;
		this.list = patientlist;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
//		Log.d("tag","------CwhListViewAdapter-----list.size()="+list.size());
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_cwh, null);
			holder.horizontal_list_cwh = (LinearLayout) convertView.findViewById(R.id.horizontal_list_cwh);
			holder.list_cwh_checked = (ImageView) convertView.findViewById(R.id.list_cwh_checked);
			holder.cwh=(TextView)convertView.findViewById(R.id.list_cwh);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		
		holder.list_cwh_checked.setVisibility(View.GONE);
		
		
		if(position == selectIndex){
			convertView.setBackgroundColor(Color.parseColor("#FE860B"));
			holder.list_cwh_checked.setVisibility(View.VISIBLE);
//			holder.cwh.setTypeface(Typeface.DEFAULT_BOLD);
//			holder.cwh.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
//					mContext.getResources().getDimensionPixelSize(R.dimen.dn_notetitle));
//			holder.cwh.setTextColor(Color.parseColor("#ffffff"));
//			convertView.setSelected(true);

		}else{
//			holder.horizontal_list_cwh.setBackgroundColor(Color.parseColor("#193687"));
			holder.list_cwh_checked.setVisibility(View.GONE);
//			convertView.setSelected(false);
			
			convertView.setBackgroundColor(Color.parseColor("#00000000"));
//			convertView.setBackground(mContext.getResources().getDrawable(R.drawable.top_headbg));
		}

		BedSearchinfo patient = list.get(position);
		
		holder.cwh.setText(patient.getCwdm());

		return convertView;
	}

	private static class ViewHolder {
		private LinearLayout horizontal_list_cwh;
		private ImageView list_cwh_checked;
		private TextView cwh ;
	}
	
	public void setSelectIndex(int i){
		selectIndex = i;
	}


}