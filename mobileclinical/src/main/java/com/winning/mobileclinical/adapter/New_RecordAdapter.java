package com.winning.mobileclinical.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.MediaList;
import com.winning.mobileclinical.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public class New_RecordAdapter extends BaseAdapter {

    private List<MediaList> medialist_audio=new ArrayList<>();

    public New_RecordAdapter(List<MediaList> medialist_audio) {

        this.medialist_audio = medialist_audio;
    }

    @Override
    public int getCount() {
        return medialist_audio.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView= View.inflate(parent.getContext(), R.layout.horizontal_list_recorditem, null);
        ImageView iv_audio_record = (ImageView) convertView.findViewById(R.id.list_audio_record);
        TextView tv_time_lable = (TextView) convertView.findViewById(R.id.list_time_label);
        iv_audio_record.setImageResource(R.drawable.picture_icon);
        MediaList mediaList = medialist_audio.get(position);
        String name = mediaList.getName();
        int i = name.lastIndexOf(".");
        name=name.substring(i-14,i-6)+"-"+(position);
        LogUtils.showLog(name);
        tv_time_lable.setText(name);
        return convertView;
    }

    public void setDatas(List<MediaList> medialist_audio){
        this.medialist_audio=medialist_audio;
        notifyDataSetChanged();
    }
}
