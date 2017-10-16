package com.winning.mobileclinical.layout;

import com.winning.mobileclinical.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class CornerListView extends ListView {
	private int index;

	public CornerListView(Context context) {
		super(context);
	}

	public CornerListView(Context context,int index) {
		super(context);
		this.index = index;
	}

	public CornerListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CornerListView(Context context, AttributeSet attrs) {
		super(context, attrs);
;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(index>=this.getFirstVisiblePosition()&&index<=this.getLastVisiblePosition()){
			this.getChildAt(index-this.getFirstVisiblePosition()).setBackgroundDrawable(new BitmapDrawable());
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			int itemnum = pointToPosition(x, y);

			if (itemnum == 0) {
				if (itemnum == (getAdapter().getCount() - 1)) {
					// 只有一项
					setSelector(R.drawable.list_corner_round);
				} else {
					// 第一项
					setSelector(R.drawable.list_corner_top);
				}
			} else if (itemnum == (getAdapter().getCount() - 1))
				// 最后一项
				setSelector(R.drawable.list_corner_bottom);
			else {
				// 中间一项
				setSelector(R.drawable.list_corner_center);
			}

			break;
		case MotionEvent.ACTION_UP:
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}
}
