package com.winning.mobileclinical.adapter;

import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.Bwl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BWLListAdapter extends BaseAdapter {
	private class BWLHolder {

		TextView title;
		TextView time,content,status;
		ImageView img;
	}

	private Context context;  
	List<Bwl> list;
	private LayoutInflater mInflater;
	public BWLListAdapter(Context context){
		super();
		this.context=context;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int index) {
		return list.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BWLHolder holder;
		if(convertView==null){
			convertView=mInflater.inflate(R.layout.bwlitem,null);
			holder=new BWLHolder();
//			holder.title=(TextView) convertView.findViewById(R.id.bwl_title);
//			holder.img = (ImageView) convertView.findViewById(R.id.bwl_uploadImg);
			
			holder.status =(TextView) convertView.findViewById(R.id.bwl_zt);
			holder.time=(TextView) convertView.findViewById(R.id.bwl_time);
			holder.content = (TextView) convertView.findViewById(R.id.bwl_content);
			convertView.setTag(holder);
		}else{
			holder=(BWLHolder) convertView.getTag();
		}
		setHolder(position, holder);
		return convertView;
	}
	private void setHolder(int position,BWLHolder holder){
		Bwl bwl=list.get(position);
		String title=bwl.getTitle();
		String content=bwl.getContents();
		holder.time.setText("创建日期 ："+bwl.getCjsj());
//		holder.content.setText((content==null||"".equals(content.trim()))?"无内容":content);
		holder.content.setText("炎症（活检小标本）（宫颈）鳞状上皮及柱状上皮粘膜慢性炎；部分腺体及表面柱状上皮鳞化、增生；部分鳞状上皮增生明显呈现 CINⅠ、Ⅱ、Ⅲ级改变；部分腺体潴留、扩张；鳞状上皮基底细胞增生；棘层内可见一些挖空细胞，建议进一步行HPV检测。");
//		holder.title.setText((title==null||"".equals(title.trim()))?"无标题":title);
//		if(bwl.getSave_type()==1){
//			holder.img.setVisibility(View.INVISIBLE);
//		} else {
//			holder.img.setVisibility(View.VISIBLE);
//		}
		
		if(bwl.getSave_type()==1){
			holder.status.setText("未上传");
		} else {
			holder.status.setText("已上传");
		}
		
			
	}
	public List<Bwl> getList() {
		return list;
	}
	public void setList(List<Bwl> list) {
		this.list = list;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
}
