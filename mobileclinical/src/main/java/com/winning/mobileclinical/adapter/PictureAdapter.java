package com.winning.mobileclinical.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.winning.mobileclinical.R;

import java.util.List;

public class PictureAdapter extends BaseAdapter{
	//	private int[] mIconIDs;
//	private ImageView mimage;
	private List<Bitmap> bitmaps;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;

	public PictureAdapter(Context context, List<Bitmap> bitmaps){
		this.mContext = context;
		this.bitmaps = bitmaps;
		mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return bitmaps.size();
	}
	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.horizontal_list_picitem, null);
			holder.image=(ImageView)convertView.findViewById(R.id.img_list_item);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}

		holder.image.setImageBitmap(bitmaps.get(position));
		return convertView;
	}

	private static class ViewHolder {
		private ImageView image ;
	}
	private Bitmap getPropThumnail(int id){
//		Drawable d = mContext.getResources().getDrawable(id);
		Bitmap b = BitmapFactory.decodeResource(mContext.getResources(), id);
//		Bitmap b = BitmapUtil.drawableToBitmap(d);
//		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.note_pic_width);
		int h = mContext.getResources().getDimensionPixelSize(R.dimen.note_pic_height);

		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);

		return thumBitmap;
	}

	public void setSelectIndex(int i){
		selectIndex = i;
	}
}