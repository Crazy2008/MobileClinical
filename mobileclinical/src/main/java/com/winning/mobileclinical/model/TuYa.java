package com.winning.mobileclinical.model;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TuYa extends View {

	private Paint paint;
	private Canvas cacheCanvas;
	private Bitmap cachebBitmap, newBitmap1;
	private float cur_x, cur_y;
	private Path path;
	private DrawPath dp;
	private ArrayList<DrawPath> mpath = null;
	private boolean isClear = false;
	private boolean isundo = false;
	private boolean isend = false;
	private String color;
	private int screenWidth, screenHeight;// ��Ļ���

	public class DrawPath {
		public Path mPath;// ·��
		public Paint paint;// ����
		public int col;
		public float size;
	}

	public TuYa(Context context, int w, int h) {
		super(context);
		screenWidth = w;
		screenHeight = h;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);

		cachebBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Config.ARGB_8888);
		newBitmap1 = Bitmap.createBitmap(cachebBitmap);
		mpath = new ArrayList<DrawPath>();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#ffffff"));

		if (isClear) {
			cacheCanvas = new Canvas(newBitmap1);
			canvas.drawBitmap(newBitmap1, 0, 0, null);
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(5);
			isClear = false;
		} else if (isundo) {
			cacheCanvas = new Canvas(newBitmap1);
			if (mpath != null && mpath.size() > 0) {
				mpath.remove(mpath.size() - 1);
				Iterator iter = mpath.iterator();
				while (iter.hasNext()) {
					DrawPath drawPath = (DrawPath) iter.next();
					int x = drawPath.col;// �������󻭱���ɫ����
					float size = drawPath.size;
					drawPath.paint.setStrokeWidth(size);
					drawPath.paint.setColor(x);
					cacheCanvas.drawPath(drawPath.mPath, drawPath.paint);
				}
				canvas.drawBitmap(newBitmap1, 0, 0, null);
			}
			isundo = false;
			isend = true;
			if(isend){
				paint.setColor(Color.BLACK);
				paint.setStrokeWidth(5);
				isend = false;
			}
		}else {
			//�滭�ϴ�����
			cacheCanvas = new Canvas(newBitmap1);
			canvas.drawBitmap(newBitmap1, 0, 0, null);
			if (path != null) {
				canvas.drawPath(path, paint);
			}

		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			path = new Path();
			dp = new DrawPath();
			dp.mPath = path;
			dp.paint = paint;
			dp.col = paint.getColor();
			dp.size = paint.getStrokeWidth();
			cur_x = x;
			cur_y = y;
			path.moveTo(cur_x, cur_y);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			// �������߷�ʽ����
			path.quadTo(cur_x, cur_y, x, y);
			cur_x = x;
			cur_y = y;
			break;
		}

		case MotionEvent.ACTION_UP: {
			// ��굯�𱣴����״̬
			cacheCanvas.drawPath(path, paint);
			mpath.add(dp);
			// path.reset(); //��path = null�����
			path = null;
			break;
		}

		}
		// ˢ�½���
		invalidate();
		return true;
	}

	public ArrayList<DrawPath> getMpath() {
		return mpath;
	}

	public void setMpath(ArrayList<DrawPath> mpath) {
		this.mpath = mpath;
	}

	public void clear() {
		isClear = true;
		mpath = null;
		newBitmap1.recycle();
		newBitmap1 = null;
		newBitmap1 = Bitmap.createBitmap(cachebBitmap);
		mpath = new ArrayList<DrawPath>();
		invalidate();
	}

	public void undo() {
		isundo = true;
		newBitmap1.recycle();
		newBitmap1 = null;
		newBitmap1 = Bitmap.createBitmap(cachebBitmap);
		// ˢ�½���
		invalidate();
	}

	public void setColor(String color) {
		this.color = color;
		paint.setColor(Color.parseColor(this.color));
	}

	public void setStrokeWidth(int size) {
		paint.setStrokeWidth(size);
	}

}
