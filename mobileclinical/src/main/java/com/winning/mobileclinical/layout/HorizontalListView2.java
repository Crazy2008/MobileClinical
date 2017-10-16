package com.winning.mobileclinical.layout;


/*
 * HorizontalListView.java v1.5
 *
 * 
 * The MIT License
 * Copyright (c) 2011 Paul Soucy (paul@dev-smart.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.winning.mobileclinical.layout.HorizontalListView.OnScrollStateChangedListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

@SuppressLint("WrongCall")
public class HorizontalListView2 extends AdapterView<ListAdapter> {

	public boolean mAlwaysOverrideTouch = true;
	protected ListAdapter mAdapter;
	private int mLeftViewIndex = -1;
	private int mRightViewIndex = 0;
	protected int mCurrentX;
	protected int mNextX;
	private int mMaxX = Integer.MAX_VALUE;
	private int mDisplayOffset = 0;
	protected Scroller mScroller;
	private GestureDetector mGesture;
	private Queue<View> mRemovedViewQueue = new LinkedList<View>();
	private OnItemSelectedListener mOnItemSelected;
	private OnItemClickListener mOnItemClicked;
	private OnItemLongClickListener mOnItemLongClicked;
	private boolean mDataChanged = false;
	
	private int mCurrentlySelectedAdapterIndex;
	 /** The adapter index of the leftmost view currently visible */
    private int mLeftViewAdapterIndex;

    /** The adapter index of the rightmost view currently visible */
    private int mRightViewAdapterIndex;
    
    private OnScrollStateChangedListener mOnScrollStateChangedListener = null;

    /**
     * Represents the current scroll state of this view. Needed so we can detect when the state changes so scroll listener can be notified.
     */
    private OnScrollStateChangedListener.ScrollState mCurrentScrollState = OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE;

    
    /** Holds a cache of recycled views to be reused as needed */
    private List<Queue<View>> mRemovedViewsCache = new ArrayList<Queue<View>>();

	

	public HorizontalListView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	private synchronized void initView() {
		mLeftViewAdapterIndex = -1;
	    mRightViewAdapterIndex = -1;
		mLeftViewIndex = -1;
		mRightViewIndex = 0;
		mDisplayOffset = 0;
		mCurrentX = 0;
		mNextX = 0;
		mMaxX = Integer.MAX_VALUE;
		mScroller = new Scroller(getContext());
		mGesture = new GestureDetector(getContext(), mOnGesture);
		setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
	}
	
	 private void setCurrentScrollState(OnScrollStateChangedListener.ScrollState newScrollState) {
	        // If the state actually changed then notify listener if there is one
	        if (mCurrentScrollState != newScrollState && mOnScrollStateChangedListener != null) {
	            mOnScrollStateChangedListener.onScrollStateChanged(newScrollState);
	        }

	        mCurrentScrollState = newScrollState;
	    }
	
	@Override
	public void setOnItemSelectedListener(OnItemSelectedListener listener) {
		mOnItemSelected = listener;
	}
	
	@Override
	public void setOnItemClickListener(OnItemClickListener listener){
		mOnItemClicked = listener;
	}
	
	@Override
	public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
		mOnItemLongClicked = onItemLongClickListener;
	}

	private DataSetObserver mDataObserver = new DataSetObserver() {

		@Override
		public void onChanged() {
			synchronized(HorizontalListView2.this){
				mDataChanged = true;
			}
			invalidate();
			requestLayout();
		}

		@Override
		public void onInvalidated() {
			reset();
			invalidate();
			requestLayout();
		}
		
	};

	@Override
	public ListAdapter getAdapter() {
		return mAdapter;

	}

	@Override
	public View getSelectedView() {
		//TODO: implement
//		return null;
		
		return getChild(mCurrentlySelectedAdapterIndex);
	}
	
	 private View getChild(int adapterIndex) {
	        if (adapterIndex >= mLeftViewAdapterIndex && adapterIndex <= mRightViewAdapterIndex) {
	            return getChildAt(adapterIndex - mLeftViewAdapterIndex);
	        }

	        return getChildAt(adapterIndex);
	    }

	@Override
	public void setAdapter(ListAdapter adapter) {
		if(mAdapter != null) {
			mAdapter.unregisterDataSetObserver(mDataObserver);
		}
		
		 if (adapter != null) {
	            // Clear so we can notify again as we run out of data

	            mAdapter = adapter;
	            mAdapter.registerDataSetObserver(mDataObserver);
	        }

	        initializeRecycledViewCache(mAdapter.getViewTypeCount());
	        reset();
		
//		mAdapter = adapter;
//		mAdapter.registerDataSetObserver(mDataObserver);
//		reset();
	}
	
	 private void initializeRecycledViewCache(int viewTypeCount) {
	        // The cache is created such that the response from mAdapter.getItemViewType is the array index to the correct cache for that item.
	        mRemovedViewsCache.clear();
	        for (int i = 0; i < viewTypeCount; i++) {
	            mRemovedViewsCache.add(new LinkedList<View>());
	        }
	    }
	
	private synchronized void reset(){
		initView();
		removeAllViewsInLayout();
        requestLayout();
	}

//	@Override
//	public void setSelection(int position) {
//		//TODO: implement
//	}
	
//	public void setSelectionFromTop(int position, int y) {
//	     if (mAdapter == null) {
//	         return;
//	     }
//	     if (!isInTouchMode()) {
//	         position = lookForSelectablePosition(position, true);
//	         if (position >= 0) {
//	             setNextSelectedPositionInt(position);
//	         }
//	     } else {
//	         mResurrectToPosition = position;
//	     }
//	     if (position >= 0) {
//	         mLayoutMode = LAYOUT_SPECIFIC;
//	         mSpecificTop = mListPadding.top + y;
//	         if (mNeedSync) {
//	             mSyncPosition = position;
//	             mSyncRowId = mAdapter.getItemId(position);
//	         }
//	         requestLayout();
//	     }
//	 }
	
	@Override
    public void setSelection(int position) {
        mCurrentlySelectedAdapterIndex = position;
		
//		getChild(mCurrentlySelectedAdapterIndex);
        
        int positionX = position* 60;
        int maxWidth = this.getChildCount()*this.getWidth();
        if(positionX <=0){
                positionX  = 0;
        }
//        if(positionX >maxWidth){
//                positionX =maxWidth;
//        }
//        scrollTo(positionX );
        
//        synchronized(HorizontalListView.this){
//			mNextX += (int)positionX;
//		}
        scrollTo(positionX);
//        invalidate();
//		requestLayout();
        
    }

//	@Override  
//	public void setSelection(int position) {  
//	    setSelectionFromTop(position, 0);  
//	}  
	
	private void addAndMeasureChild(final View child, int viewPos) {
		LayoutParams params = child.getLayoutParams();
		if(params == null) {
			params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		}

		addViewInLayout(child, viewPos, params, true);
		child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST),
				MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
	}
	
	

	@Override
	protected synchronized void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		if(mAdapter == null){
			return;
		}
		
		if(mDataChanged){
			int oldCurrentX = mCurrentX;
			initView();
			removeAllViewsInLayout();
			mNextX = oldCurrentX;
			mDataChanged = false;
		}

		if(mScroller.computeScrollOffset()){
			int scrollx = mScroller.getCurrX();
			mNextX = scrollx;
		}
		
		if(mNextX <= 0){
			mNextX = 0;
			mScroller.forceFinished(true);
		}
		if(mNextX >= mMaxX) {
			mNextX = mMaxX;
			mScroller.forceFinished(true);
		}
		
		int dx = mCurrentX - mNextX;
		
		removeNonVisibleItems(dx);
		fillList(dx);
		positionItems(dx);
		
		mCurrentX = mNextX;
		
		 if (determineMaxX()) {
	            // Redo the layout pass since we now know the maximum scroll position
	            onLayout(changed, left, top, right, bottom);
	            return;
	        }

	        // If the fling has finished
	        if (mScroller.isFinished()) {
	            // If the fling just ended
	            if (mCurrentScrollState == OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING) {
	                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
	            }
	        } else {
	            // Still in a fling so schedule the next frame
	            ViewCompat.postOnAnimation(this, mDelayedLayout);
	        }
		
//		if(!mScroller.isFinished()){
//			post(new Runnable(){
//				@Override
//				public void run() {
////					requestLayout();
//				}
//			});
//			
//		}
	}
	
	
	
	/** Use to schedule a request layout via a runnable */
    private Runnable mDelayedLayout = new Runnable() {
        @Override
        public void run() {
//            requestLayout();
        }
    };
	
	 private boolean determineMaxX() {
	        // If the last view has been laid out, then we can determine the maximum x position
	        if (isLastItemInAdapter(mRightViewAdapterIndex)) {
	            View rightView = getRightmostChild();

	            if (rightView != null) {
	                int oldMaxX = mMaxX;

	                // Determine the maximum x position
	                mMaxX = mCurrentX + (rightView.getRight() - getPaddingLeft()) - getRenderWidth();

	                // Handle the case where the views do not fill at least 1 screen
	                if (mMaxX < 0) {
	                    mMaxX = 0;
	                }

	                if (mMaxX != oldMaxX) {
	                    return true;
	                }
	            }
	        }

	        return false;
	    }
	 
	 private View getLeftmostChild() {
	        return getChildAt(0);
	    }

	    /** Gets the current child that is rightmost on the screen. */
	    private View getRightmostChild() {
	        return getChildAt(getChildCount() - 1);
	    }
	 
	 /** Simple convenience method for determining if this index is the last index in the adapter */
	    private boolean isLastItemInAdapter(int index) {
	        return index == mAdapter.getCount() - 1;
	    }

	    /** Gets the height in px this view will be rendered. (padding removed) */
	    private int getRenderHeight() {
	        return getHeight() - getPaddingTop() - getPaddingBottom();
	    }

	    /** Gets the width in px this view will be rendered. (padding removed) */
	    private int getRenderWidth() {
	        return getWidth() - getPaddingLeft() - getPaddingRight();
	    }
	
	private void fillList(final int dx) {
		int edge = 0;
		View child = getChildAt(getChildCount()-1);
		if(child != null) {
			edge = child.getRight();
		}
		fillListRight(edge, dx);
		
		edge = 0;
		child = getChildAt(0);
		if(child != null) {
			edge = child.getLeft();
		}
		fillListLeft(edge, dx);
		
		
	}
	
	private void fillListRight(int rightEdge, final int dx) {
		while(rightEdge + dx < getWidth() && mRightViewIndex < mAdapter.getCount()) {
			
			View child = mAdapter.getView(mRightViewIndex, mRemovedViewQueue.poll(), this);
			addAndMeasureChild(child, -1);
			rightEdge += child.getMeasuredWidth();
			
			if(mRightViewIndex == mAdapter.getCount()-1) {
				mMaxX = mCurrentX + rightEdge - getWidth();
			}
			
			if (mMaxX < 0) {
				mMaxX = 0;
			}
			mRightViewIndex++;
		}
		
	}
	
	private void fillListLeft(int leftEdge, final int dx) {
		while(leftEdge + dx > 0 && mLeftViewIndex >= 0) {
			View child = mAdapter.getView(mLeftViewIndex, mRemovedViewQueue.poll(), this);
			addAndMeasureChild(child, 0);
			leftEdge -= child.getMeasuredWidth();
			mLeftViewIndex--;
			mDisplayOffset -= child.getMeasuredWidth();
		}
	}
	
	private void removeNonVisibleItems(final int dx) {
		View child = getChildAt(0);
		while(child != null && child.getRight() + dx <= 0) {
			mDisplayOffset += child.getMeasuredWidth();
			mRemovedViewQueue.offer(child);
			removeViewInLayout(child);
			mLeftViewIndex++;
			child = getChildAt(0);
			
		}
		
		child = getChildAt(getChildCount()-1);
		while(child != null && child.getLeft() + dx >= getWidth()) {
			mRemovedViewQueue.offer(child);
			removeViewInLayout(child);
			mRightViewIndex--;
			child = getChildAt(getChildCount()-1);
		}
	}
	
	private void positionItems(final int dx) {
		if(getChildCount() > 0){
			mDisplayOffset += dx;
			int left = mDisplayOffset;
			for(int i=0;i<getChildCount();i++){
				View child = getChildAt(i);
				int childWidth = child.getMeasuredWidth();
				child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
				left += childWidth + child.getPaddingRight();
			}
		}
	}
	
	public synchronized void scrollTo(int x) {
		mScroller.startScroll(mNextX, 0, x - mNextX, 0);
		setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING);
		requestLayout();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean handled = super.dispatchTouchEvent(ev);
		handled |= mGesture.onTouchEvent(ev);
		return handled;
	}
	
	protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
		synchronized(HorizontalListView2.this){
			mScroller.fling(mNextX, 0, (int)-velocityX, 0, 0, mMaxX, 0, 0);
		}
		requestLayout();
		
		return true;
	}
	
	protected boolean onDown(MotionEvent e) {
		mScroller.forceFinished(true);
		return true;
	}
	
	private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

		@Override
		public boolean onDown(MotionEvent e) {
			return HorizontalListView2.this.onDown(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return HorizontalListView2.this.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			
			synchronized(HorizontalListView2.this){
				mNextX += (int)distanceX;
			}
			requestLayout();
			
			return true;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			for(int i=0;i<getChildCount();i++){
				View child = getChildAt(i);
				if (isEventWithinView(e, child)) {
					if(mOnItemClicked != null){
						mOnItemClicked.onItemClick(HorizontalListView2.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId( mLeftViewIndex + 1 + i ));
					}
					if(mOnItemSelected != null){
						mOnItemSelected.onItemSelected(HorizontalListView2.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId( mLeftViewIndex + 1 + i ));
					}
					
					if(mOnItemLongClicked != null){
//						mOnItemLongClicked.onItemLongClick(HorizontalListView.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId( mLeftViewIndex + 1 + i ));
					}
					break;
				}
				
			}
			return true;
		}
		
		@Override
		public void onLongPress(MotionEvent e) {
			int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				View child = getChildAt(i);
				if (isEventWithinView(e, child)) {
					if (mOnItemLongClicked != null) {
						mOnItemLongClicked.onItemLongClick(HorizontalListView2.this, child, mLeftViewIndex + 1 + i, mAdapter.getItemId(mLeftViewIndex + 1 + i));
					}
					break;
				}

			}
		}

		private boolean isEventWithinView(MotionEvent e, View child) {
            Rect viewRect = new Rect();
            int[] childPosition = new int[2];
            child.getLocationOnScreen(childPosition);
            int left = childPosition[0];
            int right = left + child.getWidth();
            int top = childPosition[1];
            int bottom = top + child.getHeight();
            viewRect.set(left, top, right, bottom);
            return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
        }
	};

	

}
