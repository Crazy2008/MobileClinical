package com.winning.mobileclinical.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.winning.mobileclinical.R;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.cis.BookmarkInfo;
import com.winning.mobileclinical.widget.RoundImageView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.winning.mobileclinical.R.id.iv;
import static com.winning.mobileclinical.R.id.tv_time;

/**
 * Created by Administrator on 2016/12/7.
 */

public class EditBookMarkAdapter extends BaseAdapter {
    private final Context context;
    private Bitmap bitmap;
    private List<BookmarkInfo> emrBookmarkList;
    private int checkedPostion = -1;

    public EditBookMarkAdapter(List<BookmarkInfo> emrBookmarkList, Context context) {
        this.context = context;
        this.emrBookmarkList = emrBookmarkList;
    }

    @Override
    public int getCount() {
        return emrBookmarkList.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), R.layout.edit_book_mark_item, null);
            viewHolder.imageView = (RoundImageView) convertView.findViewById(iv);
            viewHolder.textView = (TextView) convertView.findViewById(tv_time);

            viewHolder.ll_background = (LinearLayout) convertView.findViewById(R.id.ll_background);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        int count = emrBookmarkList.size() - 1;
        BookmarkInfo bookmarkInfo = emrBookmarkList.get(position);
        BigDecimal id = bookmarkInfo.getId();
        if (position == count) {
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(GlobalCache.getCache().getOriginalBitmap());
            }
            viewHolder.imageView.setImageBitmap(bitmap);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
            Date date = new Date();
            String time = format.format(date);
            viewHolder.textView.setText(time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " " + time.substring(8, 10) + ":" + time.substring(10, 12));
        } else {
            final BigDecimal syxh = bookmarkInfo.getSyxh();
            final String url = GlobalCache.getCache().getEditBookMarkDownloadUrl() + "&xh=" + id + "&syxh=" + syxh;
            String time = bookmarkInfo.getCreate_time();
            viewHolder.textView.setText(time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " " + time.substring(8, 10) + ":" + time.substring(10, 12));
            Glide.with(parent.getContext()).load(url).centerCrop().into(viewHolder.imageView);
        }
        //如果是选中状态
        if (checkedPostion == position) {
//            viewHolder.textView.setTextColor(Color.RED);
            viewHolder.ll_background.setBackgroundColor(Color.parseColor("#787878"));
        } else {
//            viewHolder.textView.setTextColor(Color.WHITE);
            viewHolder.ll_background.setBackground(null);
        }
        if (checkedPostion == -1) {
            checkedPostion = count;
        }
        return convertView;
    }

    public void setCheckedPostion(int postion) {

        this.checkedPostion = postion;
        notifyDataSetChanged();
    }

    public void setDatas(List<BookmarkInfo> emrBookmarkList) {
        this.emrBookmarkList.clear();
        this.emrBookmarkList = emrBookmarkList;

    }
//            if (position == count) {
////                ViewParent  parent1=iv.getParent();
////                if(parent1!=null){
////                    ViewGroup  viewGroup=(ViewGroup)parent1;
////                    viewGroup.removeView(iv);
////                }

    //            } else {
    public static class ViewHolder {
        public LinearLayout ll_background;
        public RoundImageView imageView;
        public TextView textView;
    }
}
