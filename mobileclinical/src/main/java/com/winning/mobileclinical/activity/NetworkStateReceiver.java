package com.winning.mobileclinical.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.winning.mobileclinical.action.NoteAction;
import com.winning.mobileclinical.db.dao.NoteDao;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.DrInfo;
import com.winning.mobileclinical.model.MediaList;
import com.winning.mobileclinical.utils.NetWorkUtil;
import com.winning.mobileclinical.utils.ServerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NetworkStateReceiver extends BroadcastReceiver {

	private static final String TAG = "NetworkStateReceiver";

	private static Boolean networkAvailable = false;// 默认网络状态
	private static com.winning.mobileclinical.utils.NetWorkUtil.netType type;
	private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	public final static String TA_ANDROID_NET_CHANGE_ACTION = "ta.android.net.conn.CONNECTIVITY_CHANGE";
	private static NetworkStateReceiver receiver;

	private Context mcontext;
	List<MediaList> media_list;
	private Thread thread;
	private Thread noteServerThread;
	private List<DrInfo> drinfo_list;
	private DrInfo drinfo;
	private String today;
	private List<DrInfo> cfjls = new ArrayList<>();

	private NetworkStateReceiver()
	{
		super();
	}

	public static NetworkStateReceiver getNetWorkReceiver()
	{
		// TODO Auto-generated constructor stub
		if (receiver == null)
		{
			synchronized (NetworkStateReceiver.class)
			{
				if (receiver == null)
				{
					receiver = new NetworkStateReceiver();
				}
			}
		}
		return receiver;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stu
		this.mcontext = context;
		receiver = NetworkStateReceiver.this;
		if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)
				|| intent.getAction().equalsIgnoreCase(TA_ANDROID_NET_CHANGE_ACTION))
		{
			Log.i(TAG, "有收到网络连接的相关讯息");
			if (!NetWorkUtil.isNetworkAvailable(context) && networkAvailable == true)
			{
				Log.i(TAG, "断开了网络");
				networkAvailable = false;
				Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
			} else if (NetWorkUtil.isNetworkAvailable(context) && networkAvailable == false)
			{
				Log.i(TAG, "网络连接成功");
				networkAvailable = true;
				type = NetWorkUtil.getAPNType(context);
				Toast.makeText(context, "网络连接成功", Toast.LENGTH_SHORT).show();
				drinfo_list = NoteDao.getNote_cfjlAll(context);
				if(drinfo_list.size()>0){
					thread = new Thread(new SubmitThread());
					thread.start();
				}
			}
		}
	}

	class SubmitThread implements Runnable {
		@Override
		public void run() {

			noteServerThread = new Thread(new NoteServerSubmit());
			noteServerThread.start();
		}
	}

	/**
	 * 更新服务端数据
	 *
	 * @author HM
	 */
	class NoteServerSubmit implements Runnable {
		@Override
		public void run() {
			String json = "";
			String today_xh = "";
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			today = format.format(date);



			for(int i = 0;i < drinfo_list.size();i++){
				drinfo = drinfo_list.get(i);
				media_list = NoteDao.getMediaList_cfjl(mcontext,drinfo.getXh(),"","","");
				drinfo.setMediaList(media_list);

				String syxh = "" + drinfo.getSyxh();
				String yexh = "" + drinfo.getYexh();
				String ysdm = "" + drinfo.getYsdm();
				String params = "{\"syxh\":" + syxh + ",\"yexh\":" + yexh + ",\"ysdm\":\"" + ysdm + "\"}";
				cfjls = NoteAction.getDoctorDailyRecordList(mcontext, params);

				for(int j=0;j < cfjls.size();j++){
					System.out.println(today+"::"+cfjls.get(j).getCfsj());
					if (cfjls.get(j).getCfsj().startsWith(today)) {
						today_xh = cfjls.get(j).getXh();
					}
				}
				drinfo.setXh(today_xh==""?"0":today_xh);
				json = new Gson().toJson(drinfo);
//				System.out.println("json=======" + SystemUtil.getValue("SYS02") + "?drInfo=" + json);
				ServerUtil.uploadParamstoServer(GlobalCache.getCache().getCFJLUploadUrl() + "?drInfo=" + json, "", "");
				for (int k = 0; k < media_list.size(); k++) {
					ServerUtil.uploadtoServer(GlobalCache.getCache().getCFJLUploadUrl(), media_list.get(k).getName(), media_list.get(k).getUrl());
				}
			}

			Message msg = new Message();
			msg.arg1 = 2;
			msg.arg2 = 1;
			handler.sendMessage(msg);
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int flag = msg.arg1;
			if (flag == 1) {
				Toast.makeText(mcontext, "离线上传便签失败", Toast.LENGTH_LONG)
						.show();
			}
			if (flag == 2) { // 保存成功或更新成功
				//更新本地查房便签数据状态
				NoteDao.updateLocalCfjl(mcontext,drinfo_list);
				Toast.makeText(mcontext, "离线上传便签成功", Toast.LENGTH_LONG)
						.show();
			}

		}
	};

	/**
	 * 注册网络监听
	 *
	 * @param context
	 */
	public static void registerNetworkStateReceiver(Context context)
	{
		Intent intent = new Intent();
		intent.setAction(TA_ANDROID_NET_CHANGE_ACTION);
		context.sendBroadcast(intent);
	}

	/**
	 * 显示当前网络状态
	 *
	 * @param context
	 */
	public static void checkNetWorkState(Context context)
	{
		Intent intent = new Intent();
		intent.setAction(TA_ANDROID_NET_CHANGE_ACTION);
		context.sendBroadcast(intent);
	}

	/**
	 * 注销网络监听
	 *
	 * @param context
	 */
	public static void unRegisterNetworkStateReceiver(Context context)
	{
		if (receiver != null)
		{
			try
			{
				context.getApplicationContext().unregisterReceiver(receiver);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static Boolean isNetWorkAvailable()
	{
		return networkAvailable;
	}

	public static com.winning.mobileclinical.utils.NetWorkUtil.netType getNetWorkType()
	{
		return type;
	}

}
