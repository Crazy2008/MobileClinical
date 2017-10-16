package com.winning.mobileclinical.widget;

import com.winning.mobileclinical.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * 备注: 此LJWebView继承自Relativielayout,�?��会导致丢失一个WebView的属性，如果大家
 * 在项目中�?��用到，可是此类中加入，然后调用即可，可参�?public void setClickable(boolean value){
 * mWebView.setClickable(value); } 这个方法的定义和调用
 * 
 * @author Administrator
 */
public class LJWebView extends RelativeLayout
{

	public static int Circle = 0x01;
	public static int Horizontal = 0x02;

	private Context context;

	private WebView mWebView = null; //
	private ProgressBar progressBar = null; // 水平进度�?
	private RelativeLayout progressBar_circle = null; // 包含圆形进度条的布局
	private int barHeight = 8; // 水平进度条的�?
	private boolean isAdd = false; // 判断是否已经加入进度�?
	private int progressStyle = Horizontal; // 进度条样�?Circle表示为圆形，Horizontal表示为水�?

	public LJWebView(Context context)
	{
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public LJWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public LJWebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	private void init()
	{
		mWebView = new WebView(context);
		this.addView(mWebView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mWebView.setWebChromeClient(new AppCacheWebChromeClient());
	}

	private class AppCacheWebChromeClient extends WebChromeClient
	{
		@Override
		public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater)
		{
			quotaUpdater.updateQuota(spaceNeeded * 2);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			if (newProgress == 100)
			{
				if (progressStyle == Horizontal)
				{
					progressBar.setVisibility(View.GONE);
				}
				else
				{
					progressBar_circle.setVisibility(View.GONE);
				}
			}
			else
			{
				if (!isAdd)
				{
					if (progressStyle == Horizontal)
					{
						progressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress_horizontal, null);
						progressBar.setMax(100);
						progressBar.setProgress(50);
						LJWebView.this.addView(progressBar, LayoutParams.FILL_PARENT, barHeight);
					}
//					else
//					{
//						progressBar_circle = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.progress_circle, null);
//						LJWebView.this.addView(progressBar_circle, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
//					}
					isAdd = true;
				}

//				if (progressStyle == Horizontal)
//				{
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setProgress(newProgress);
//				}
//				else
//				{
//					progressBar_circle.setVisibility(View.VISIBLE);
//				}
			}
		}
	}

	public void setBarHeight(int height)
	{
		barHeight = height;
	}

	public void setProgressStyle(int style)
	{
		progressStyle = style;
	}

	public void setClickable(boolean value)
	{
		mWebView.setClickable(value);
	}

	public void setUseWideViewPort(boolean value)
	{
		mWebView.getSettings().setUseWideViewPort(value);
	}

	public void setSupportZoom(boolean value)
	{
		mWebView.getSettings().setSupportZoom(value);
	}

	public void setBuiltInZoomControls(boolean value)
	{
		mWebView.getSettings().setBuiltInZoomControls(value);
	}

	public void setJavaScriptEnabled(boolean value)
	{
		mWebView.getSettings().setJavaScriptEnabled(value);
	}

	public void setCacheMode(int value)
	{
		mWebView.getSettings().setCacheMode(value);
	}

	public void setWebViewClient(WebViewClient value)
	{
		mWebView.setWebViewClient(value);
	}

	public void setWebChromeClient(WebChromeClient value)
	{
		mWebView.setWebChromeClient(value);
	}

	public void loadUrl(String url)
	{
		mWebView.loadUrl(url);
	}

	public void setPluginsEnabled(boolean value)
	{

		if(value){
		mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
		}else{
		mWebView.getSettings().setPluginState(WebSettings.PluginState.OFF);

		}
	}

	public boolean canGoBack()
	{
		return mWebView.canGoBack();
	}

	public void setRenderPriority(RenderPriority value)
	{
		mWebView.getSettings().setRenderPriority(value);
	}

	public void setBlockNetworkImage(boolean value)
	{
		mWebView.getSettings().setBlockNetworkImage(value);
	}

	public void setDomStorageEnabled(boolean value)
	{
		mWebView.getSettings().setDomStorageEnabled(value);
	}

	public void setAppCacheEnabled(boolean value)
	{
		mWebView.getSettings().setAppCacheEnabled(value);
	}

	public void setAppCachePath(String value)
	{
		mWebView.getSettings().setAppCachePath(value);
	}

	public void setAppCacheMaxSize(long value)
	{
		mWebView.getSettings().setAppCacheMaxSize(value);
	}

	public void setDatabaseEnabled(boolean value)
	{
		mWebView.getSettings().setDatabaseEnabled(value);
	}

	public void setDatabasePath(String value)
	{
		mWebView.getSettings().setDatabasePath(value);
	}

	public void goBack()
	{
		mWebView.goBack();
	}

	public WebView getmWebView()
	{
		return mWebView;
	}

	public int getProgress()
	{
		return mWebView.getProgress();
	}

	public void clearCache(boolean flag)
	{
		mWebView.clearCache(flag);
	}

	public void clearView()
	{
		mWebView.clearView();
	}

	public void setJavaScriptCanOpenWindowsAutomatically(boolean value)
	{
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(value);
	}

	public void addJavascriptInterface(Object object, String name)
	{
		mWebView.addJavascriptInterface(object, name);
	}

	public void reload()
	{
		mWebView.reload();
	}

	public String getOriginalUrl()
	{
		return mWebView.getOriginalUrl();
	}

	public String getUrl()
	{
		return mWebView.getUrl();
	}

	// /**
	// * 手指抖动误差
	// */
	// private static final int SHAKE_MOVE_VALUE = 8;
	// /**
	// * Scrollview内部的view
	// */
	// private View innerView;
	// /**
	// * 记录innerView�?��的位�?
	// */
	// private float startY;
	// /**
	// * 拉动回弹的区�?
	// */
	// private Rect outRect = new Rect();
	//
	// private boolean animationFinish = true;
	//
	// /**
	// * 继承自View 在xml的所有布�?��载完之后执行
	// */
	// @Override
	// protected void onFinishInflate()
	// {
	// if (getChildCount() > 0)
	// {
	// innerView = getChildAt(0);
	// }
	// }
	//
	// /**
	// * 继承自ViewGroup 返回true, 截取触摸事件 返回false,
	// * 将事件传递给onTouchEvent()和子控件的dispatchTouchEvent()
	// */
	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev)
	// {
	//
	// // 判断 点击子控�?or 按住子控件滑�?
	// // 如果点击子控件，则返�?false, 子控件响应点击事�?
	// // 如果按住子控件滑动，则返�?true, 滑动布局
	// switch (ev.getAction())
	// {
	// case MotionEvent.ACTION_DOWN:
	// {
	// startY = ev.getY();
	// break;
	// }
	// case MotionEvent.ACTION_MOVE:
	// {
	// float currentY = ev.getY();
	// float scrollY = currentY - startY;
	//
	// // 是否返回 true
	// return Math.abs(scrollY) > SHAKE_MOVE_VALUE;
	// }
	// }
	// // 默认返回 false
	// return super.onInterceptTouchEvent(ev);
	// }
	//
	// @Override
	// public boolean onTouchEvent(MotionEvent ev)
	// {
	// if (innerView == null)
	// {
	// return super.onTouchEvent(ev);
	// }
	// else
	// {
	// myTouchEvent(ev);
	// }
	// return super.onTouchEvent(ev);
	// }
	//
	// public void myTouchEvent(MotionEvent ev)
	// {
	// if (animationFinish)
	// {
	//
	// switch (ev.getAction())
	// {
	// case MotionEvent.ACTION_DOWN:
	// startY = ev.getY();
	// super.onTouchEvent(ev);
	// break;
	//
	// case MotionEvent.ACTION_UP:
	// startY = 0;
	// if (isNeedAnimation())
	// {
	// animation();
	// }
	// super.onTouchEvent(ev);
	// break;
	//
	// case MotionEvent.ACTION_MOVE:
	// final float preY = startY == 0 ? ev.getY() : startY;
	// float nowY = ev.getY();
	// int deltaY = (int) (preY - nowY);
	// startY = nowY;
	//
	// // 当滚动到�?��或�?�?��时就不会再滚动，这时移动布局
	// if (isNeedMove())
	// {
	// if (outRect.isEmpty())
	// {
	// // 保存正常的布�?���?
	// outRect.set(innerView.getLeft(), innerView.getTop(), innerView.getRight(),
	// innerView.getBottom());
	// }
	// // 移动布局
	// // 这里 deltaY/2 为了操作体验更好
	// innerView.layout(innerView.getLeft(), innerView.getTop() - deltaY / 2,
	// innerView.getRight(), innerView.getBottom()
	// - deltaY / 2);
	// }
	// else
	// {
	// super.onTouchEvent(ev);
	// }
	// break;
	// default:
	// break;
	// }
	// }
	// }
	//
	// /**
	// * �?��移动动画
	// */
	// public void animation()
	// {
	// TranslateAnimation ta = new TranslateAnimation(0, 0, 0, outRect.top -
	// innerView.getTop());
	// ta.setDuration(400);
	// // 减�?变化 为了用户体验更好
	// ta.setInterpolator(new DecelerateInterpolator());
	// ta.setAnimationListener(new Animation.AnimationListener()
	// {
	// @Override
	// public void onAnimationStart(Animation animation)
	// {
	// animationFinish = false;
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation)
	// {
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation)
	// {
	// innerView.clearAnimation();
	// // 设置innerView回到正常的布�?���?
	// innerView.layout(outRect.left, outRect.top, outRect.right, outRect.bottom);
	// outRect.setEmpty();
	// animationFinish = true;
	// }
	// });
	// innerView.startAnimation(ta);
	// }
	//
	// /**
	// * 是否�?���?��动画
	// */
	// public boolean isNeedAnimation()
	// {
	// return !outRect.isEmpty();
	// }
	//
	// /**
	// * 是否�?��移动布局
	// */
	// public boolean isNeedMove()
	// {
	// int offset = innerView.getMeasuredHeight() - getHeight();
	// offset = (offset < 0) ? 0 : offset;
	// int scrollY = getScrollY();
	// return (offset == 0 || scrollY == offset);
	// }
}
