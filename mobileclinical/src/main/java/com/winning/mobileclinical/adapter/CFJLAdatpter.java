package com.winning.mobileclinical.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.DrInfo;

import java.util.ArrayList;
import java.util.List;

import static com.winning.mobileclinical.R.id.tv_cfjl_month;
import static com.winning.mobileclinical.R.id.tv_cfjl_year;

/**
 * Created by Administrator on 2016/11/2.
 */

public class CFJLAdatpter extends BaseAdapter {

    private List<DrInfo> cfjlmxs = new ArrayList<>();
    private  int mSelect=0;

    public CFJLAdatpter(List<DrInfo> cfjls) {

        if (cfjls != null) {
           this.cfjlmxs = cfjls;
        }
    }

    @Override
    public int getCount() {
        return cfjlmxs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            viewHolder=new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.fragment_cfjl_item, null);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_cfjl_year = (TextView) convertView.findViewById(tv_cfjl_year);
        viewHolder.tv_cfjl_month = (TextView) convertView.findViewById(tv_cfjl_month);
        viewHolder.tv_cfjl_month.setText(cfjlmxs.get(position).getCfsj().substring(4,6)+"-"+cfjlmxs.get(position).getCfsj().substring(6,8));
        viewHolder.tv_cfjl_year.setText(cfjlmxs.get(position).getCfsj().substring(0,4));

        if(mSelect==position){
            convertView.setBackgroundColor(Color.BLUE);
            viewHolder.tv_cfjl_month.setTextColor(Color.WHITE);
            viewHolder.tv_cfjl_year.setTextColor(Color.WHITE);
        }else{
            convertView.setBackgroundColor(Color.WHITE);
            viewHolder.tv_cfjl_month.setTextColor(Color.BLACK);
            viewHolder.tv_cfjl_year.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    public void setDatas(List<DrInfo> datas) {
        cfjlmxs.addAll(datas);
        notifyDataSetChanged();
    }

    private class ViewHolder{
        private  TextView tv_cfjl_year;
        private  TextView tv_cfjl_month;

    }


    public void changeSelected(int positon){ //刷新方法
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
    }
}
