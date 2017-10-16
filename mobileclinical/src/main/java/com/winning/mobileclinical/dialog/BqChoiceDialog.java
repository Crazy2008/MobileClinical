package com.winning.mobileclinical.dialog;

import java.util.ArrayList;
import java.util.List;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.cis.DeptWardMapInfo;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class BqChoiceDialog extends Dialog{

	private BqChoicAdapter<String> bqChoicAdapter;
	private Context mContext;

	private List<DeptWardMapInfo> bqlist;
	private ListView bqchoice_lv;
	private Button bqchoice_ok;
	private Button bqchoice_cl;
	private BqChoiceListener bqChoiceListener = null;
	
	public BqChoiceDialog(Activity act,List<DeptWardMapInfo> bqlist) {
		super(act);
		mContext = act;
		this.bqlist = bqlist;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initView(mContext);
		DisplayMetrics metric = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int dpWidth = (int) (metric.widthPixels);
        int dpHeight = (int) (metric.heightPixels) ;
		WindowManager.LayoutParams params = this.getWindow().getAttributes();
		params.width = (int) (dpWidth/2);
		params.height = (int) (dpHeight/2) ;
		this.getWindow().setAttributes(params);
		
		
        //dialog添加视图
		


	}
	
	protected void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.bqchoicelayout, null);

		bqchoice_lv = (ListView) view.findViewById(R.id.bqchoice_lv);
		bqchoice_ok = (Button) view.findViewById(R.id.bqchoice_ok);
		bqchoice_cl = (Button) view.findViewById(R.id.bqchoice_cl);
		bqchoice_ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(bqChoiceListener != null)
				{
					bqChoiceListener.BqChoice(bqChoicAdapter.getSelectItem()); //选中确认的场合 未选择的场合也是-1
				}
				dismiss();
			}
		});
		bqchoice_cl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		bqChoicAdapter = new BqChoicAdapter<String>(bqlist);
		bqchoice_lv.setAdapter(bqChoicAdapter);
		if(bqlist.size()>0)
		{
			bqChoicAdapter.setSelectItem(0);
		}
//		yymdchoice_ypmc.setText(ypmc);
		setContentView(view);
	}
	public interface BqChoiceListener{
		public void BqChoice(int bqsel);
	}
	public void setBqChoiceListener(BqChoiceListener l)
	{
		this.bqChoiceListener = l;
	}
	
	@SuppressWarnings("hiding")
	class BqChoicAdapter<String> extends BaseAdapter implements
					View.OnClickListener {
		private List<DeptWardMapInfo> mObjects = new ArrayList<DeptWardMapInfo>();
		private int mSelectItem = -1;
		
		private LayoutInflater mInflater;
		
		public BqChoicAdapter(List<DeptWardMapInfo> objects) {
			this.mObjects = objects;
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getSelectItem() {
			// TODO Auto-generated method stub
			return mSelectItem;
		}
		public void setSelectItem(int mSelectItem) {
			// TODO Auto-generated method stub
			this.mSelectItem = mSelectItem;
			notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(mObjects != null) {
				return mObjects.size();
			} else {
				return 0;
			}
		}
	
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mObjects.get(position);
		}
	
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		class ViewHolder {
		//	public TextView mTextView;
			public CheckBox mCheckBox;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.pschoiceitem,
						null);
				viewHolder = new ViewHolder();
	
				viewHolder.mCheckBox = (CheckBox) convertView
						.findViewById(R.id.psitem_check);
				convertView.setTag(viewHolder);
		
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			DeptWardMapInfo wardDTO = (DeptWardMapInfo) getItem(position);
			viewHolder.mCheckBox.setChecked(mSelectItem == position);
			
//			int k = CharLength.String_length(wardDTO.getWardName());
//			if(k < 20) {
//        		int conut = 20 - k;
//        		java.lang.String name = wardDTO.getWardName();
//        		name = getTextCount(conut,name);
//        		
//        		viewHolder.mCheckBox.setText(name+"|　"+wardDTO.getDepartmentName());
//        	} else {
//        		viewHolder.mCheckBox.setText(wardDTO.getWardName()+"|　"+wardDTO.getDepartmentName());
//        	}
			
//			if(wardDTO.getBqmc().getBytes().length  < 20) {
//        		int conut = 20 - wardDTO.getBqmc().getBytes().length;
//        		java.lang.String name = wardDTO.getBqmc();
//        		name = getTextCount(conut,name);
//        		
//        		viewHolder.mCheckBox.setText(name+"|　"+wardDTO.getKsmc());
//        	} else {
//        		viewHolder.mCheckBox.setText(wardDTO.getBqmc()+"|　"+wardDTO.getKsmc());
//        	}
			
			viewHolder.mCheckBox.setText(wardDTO.getBqmc()+"----"+wardDTO.getKsmc());
			
			
			
//			viewHolder.mCheckBox.setText(wardDTO.getWardName()+"|"+wardDTO.getDepartmentName());
			viewHolder.mCheckBox.setTag(position);
			viewHolder.mCheckBox.setOnClickListener(this);
			return convertView;
		}

	private java.lang.String getTextCount(int conut, java.lang.String name) {
		// TODO Auto-generated method stub
		for(int i=0; i<conut;i++) {
			name = name + " ";
		}
		return name;
	}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int position = (Integer) v.getTag();
			if (position != mSelectItem) {
				mSelectItem = position;
			}
			notifyDataSetChanged();
		}
	}
}


