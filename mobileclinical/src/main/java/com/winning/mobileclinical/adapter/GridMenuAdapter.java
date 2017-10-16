package com.winning.mobileclinical.adapter;

import java.util.ArrayList;
import java.util.HashMap;




import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.NameAndImage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class GridMenuAdapter extends BaseAdapter{

	private Context context;
	private String[] textBtn;
	private String[] imageNames;
	private ArrayList<NameAndImage> list = null;
	private LayoutInflater inflater;
	class Holder{
		TextView text;
		ImageView image;
	}
	public GridMenuAdapter(Context context,String[] str,String[] imageNames) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		textBtn = str;
		list = new ArrayList<NameAndImage>();
		this.imageNames = imageNames;
		
		for (int i = 0; i < imageNames.length; i++) {
			NameAndImage n = new NameAndImage(textBtn[i], imageNames[i]);
			list.add(n);
		}
	}
	/*public SimpleAdapter getGridAdapter() {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < textBtn.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("id", i);
			map.put("image", getImageId(i));
			map.put("text", textBtn[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(context, data,R.layout.grid_item_menu, 
				new String[] {"image","text" },new int[] {R.id.item_image, R.id.item_text });
		return simperAdapter;
	}*/
	
	public int getImageId(int i) {
		int id = 0;
		try{
			switch (i) {
			case 0:
				id = context.getResources().getIdentifier(imageNames[i], "drawable", context.getPackageName());
				break;

			case 1:
				id = context.getResources().getIdentifier(imageNames[i], "drawable", context.getPackageName());
				break;
			case 2:
				id = context.getResources().getIdentifier(imageNames[i], "drawable", context.getPackageName());
				break;
			case 3:
				id = context.getResources().getIdentifier(imageNames[i], "drawable", context.getPackageName());
				break;
			case 4:
				id = context.getResources().getIdentifier(imageNames[i], "drawable", context.getPackageName());
				break;
			case 5:
				id = context.getResources().getIdentifier(imageNames[i], "drawable", context.getPackageName());
				break;
			case 6:
				id = context.getResources().getIdentifier(imageNames[i], "drawable", context.getPackageName());
				break;
			case 7:
				id = context.getResources().getIdentifier(imageNames[i], "drawable", context.getPackageName());
				break;
			case 8:
				id = context.getResources().getIdentifier(imageNames[i], "drawable", context.getPackageName());
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	@Override
	public Object getItem(int position) {
		
		return list.get(position);
	}
	@Override
	public long getItemId(int position) {
		
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder h;
		if(convertView == null){
			h = new Holder();
			convertView = inflater.inflate(R.layout.grid_item_menu, null);
			h.text = (TextView) convertView.findViewById(R.id.item_text);
			h.image = (ImageView) convertView.findViewById(R.id.item_image);
			convertView.setTag(h);
		} else {
			h = (Holder) convertView.getTag();
		}
		setHolder(h,position);
		return convertView;
	}
	
	private void setHolder(Holder h,int position) {
		NameAndImage n = list.get(position);
		
		if(n.getName()==null||n.getName().equals("")) {
			h.text.setVisibility(View.GONE);
		} else {
			h.text.setText(n.getName());
		}
		h.image.setImageResource(getImageId(position));
	}
}
