package com.winning.mobileclinical.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.MediaList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordAdapter extends BaseAdapter{
//	private int[] mIconIDs;
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Drawable> drawables;
	private List<MediaList> medialist;
	Bitmap iconBitmap;
	private int selectIndex = -1;
//	private List<AudioPlayer> list;
	private boolean isplay=true;

	public RecordAdapter(Context context, List<Drawable> drawables,List<MediaList> medialist){
		this.mContext = context;
		this.drawables = drawables;
		this.medialist = medialist;
//		this.list = list;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return drawables.size();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "NewApi", "SimpleDateFormat" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_recorditem, null);
			holder.logo=(ImageView)convertView.findViewById(R.id.list_audio_record);
			holder.label=(TextView)convertView.findViewById(R.id.list_time_label);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			
//			holder.logo.c
			
			if(isplay){
				holder.logo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.recordplay));
//				isplay = true;
			}else{
				holder.logo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.record_play));
//				isplay = false;
			}
			convertView.setSelected(true);
		}else{
			holder.logo.setImageDrawable(mContext.getResources().getDrawable(R.drawable.record_play));
			convertView.setSelected(false);
		}

		Date date = new Date();
		SimpleDateFormat fmt=new SimpleDateFormat("HHmmss");
		String datetime=fmt.format(date);
		String name = medialist.get(position).getName();
		holder.label.setText(name.substring((name.length()-10), (name.length()-4)));
		
		return convertView;
	}

	private static class ViewHolder {
		private ImageView logo;
		private TextView label ;
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
	public void setSelectIndex(int i,Boolean paly){
		selectIndex = i;
		isplay = paly;
	}
}