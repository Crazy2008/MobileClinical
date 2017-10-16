package com.winning.mobileclinical.adapter;

import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.DrInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter{
	private int[] mIconIDs;
	private List<DrInfo> list;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public HorizontalListViewAdapter(Context context, List<DrInfo> cfjls){
		this.mContext = context;
		this.list = cfjls;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
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
			convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
			holder.yue=(TextView)convertView.findViewById(R.id.list_yue);
			holder.nian=(TextView)convertView.findViewById(R.id.list_nian);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){

			holder.yue.setTypeface(Typeface.DEFAULT_BOLD);
			holder.yue.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
					mContext.getResources().getDimensionPixelSize(R.dimen.dn_notetitle));
			holder.yue.setTextColor(Color.parseColor("#1D46FF"));

			holder.nian.setTypeface(Typeface.DEFAULT_BOLD);
			holder.nian.setTextSize(TypedValue.COMPLEX_UNIT_PX, 
					mContext.getResources().getDimensionPixelSize(R.dimen.bqksmap_drop));
			holder.nian.setTextColor(Color.parseColor("#1D46FF"));
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		
		DrInfo cfjl = list.get(position);
		
		holder.nian.setText(cfjl.getCfsj().substring(0,4));
		holder.yue.setText(cfjl.getCfsj().substring(4,6)+"-"+cfjl.getCfsj().substring(6,8));

		return convertView;
	}

	private static class ViewHolder {
		private TextView nian ;
		private TextView yue ;
		private ImageView mImage;
	}
	private Bitmap getPropThumnail(int id){
		
		
		Drawable d = mContext.getResources().getDrawable(id);
		Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), id);
//		Bitmap b = BitmapUtil.drawableToBitmap(d);
//		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.fpd_menu);
		int h = mContext.getResources().getDimensionPixelSize(R.dimen.fpd_menu);
		
		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
		
		return thumBitmap;
	}
	public void setSelectIndex(int i){
		selectIndex = i;
	}
}