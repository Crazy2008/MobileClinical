package com.winning.mobileclinical.update;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.activity.Login;
import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.web.HTTPGetTool;
import com.winning.mobileclinical.web.WebUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * @author coolszy
 * @date 2012-4-26
 * @blog http://blog.92coding.com
 */

@SuppressLint("NewApi")
public class UpdateManager
{
	/* 是否有更新 */
	private static final int ISUPDATE = 5;
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;  //下载完成
	private static final int DOWN_ERROR = 0;    //下载失败
	private static final int DOWN_CANEL = 3;    //下载取消
	private static final int DOWN_REQUEST_ERR = 4;

	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	private String updateDir = null;
	private String updateFile = null;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private NotificationManager notificationManager;
	private Notification notification;

	private Intent updateIntent;
	private PendingIntent pendingIntent;

	private int notification_id = 0;
	DownloadApkThread downloadApkThread = null;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case ISUPDATE:
					if(msg.obj!=null){
						if((Boolean) msg.obj){
							showNoticeDialog();
						}
					}
					break;
			// 正在下载
				case DOWNLOAD:
					// 设置进度条位置
					mProgress.setProgress(progress);
					break;
				case DOWNLOAD_FINISH:
					// 安装文件
					// 取消下载对话框显示
					if (mDownloadDialog != null && mDownloadDialog.isShowing())
					{
						mDownloadDialog.dismiss();
					}
					installApk();
					break;
				case DOWN_ERROR:
					if (mDownloadDialog != null && mDownloadDialog.isShowing())
					{
						mDownloadDialog.dismiss();
					}
					notification.tickerText = "下载失败";
					Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
					/**
					 * 注释内容为eclipse版本，
					 */
//					notification.setLatestEventInfo(mContext, mHashMap.get("name"), "下载失败", pendingIntent);

					new Notification.Builder(mContext).setContentInfo(mHashMap.get("name")).setContentIntent(pendingIntent).setContentText("下载失败");
					notificationManager.notify(notification_id, notification);
					break;
				case DOWN_CANEL:
					if (mDownloadDialog != null && mDownloadDialog.isShowing())
					{
						mDownloadDialog.dismiss();
					}
					notification.tickerText = "取消下载";
					//注释内容为eclipse版本，
//					notification.setLatestEventInfo(mContext, mHashMap.get("name"), "取消下载", pendingIntent);
					new Notification.Builder(mContext).setContentInfo(mHashMap.get("name")).setContentIntent(pendingIntent).setContentText("取消下载");
					notificationManager.notify(notification_id, notification);
					if (downloadApkThread != null)
					{
						downloadApkThread.interrupt();
						downloadApkThread = null;
					}
					break;
				case DOWN_REQUEST_ERR:
					if (mDownloadDialog != null && mDownloadDialog.isShowing())
					{
						mDownloadDialog.dismiss();
					}
					if(notification!=null){
						notification.tickerText = "自动更新网络连接失败，请检查网络！";
						//注释内容为eclipse版本，
//						notification.setLatestEventInfo(mContext, mHashMap.get("name"), "自动更新网络连接失败，请检查网络！", pendingIntent);
						new Notification.Builder(mContext).setContentInfo(mHashMap.get("name")).setContentIntent(pendingIntent).setContentText("自动更新网络连接失败，请检查网络！");
						notificationManager.notify(notification_id, notification);
					}
					Toast.makeText(mContext, "自动更新网络连接失败，请检查网络！", Toast.LENGTH_LONG).show();
					if (downloadApkThread != null)
					{
						downloadApkThread.interrupt();
						downloadApkThread = null;
					}
					break;
				default:
					break;
			}
		};
	};

	@SuppressLint("NewApi")
	Handler handler = new Handler(new Callback()
	{
		@Override
		public boolean handleMessage(Message msg)
		{
			contentView.setTextViewText(R.id.notificationPercent, msg.what + "%");
			contentView.setProgressBar(R.id.notificationProgress, 100, msg.what, false);
			// show_view
			notificationManager.notify(notification_id, notification);
			return false;
		}
	});

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate(int i)
	{
		
		//提示自动更新部署网络情况
//		try
//		{
//			if (isUpdate())
//			{
//				// 显示提示对话框
//				showNoticeDialog();
//			}
//			else
//			{
//				if (i == 1)
//				{
//					Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();
//				}
//			}
//		}
//		catch (NotFoundException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			mHandler.sendEmptyMessage(DOWN_REQUEST_ERR);
//		}
//		catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			mHandler.sendEmptyMessage(DOWN_REQUEST_ERR);
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			mHandler.sendEmptyMessage(DOWN_REQUEST_ERR);
//		}
		new Thread(new UpdateChecked()).start();
	}

	/**
	 * 获取当前版本信息
	 * 
	 * @return
	 */
	public String findVersionName()
	{
		return getVersionName(mContext);
	}
	
	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	private void isUpdate() throws IOException, Exception
	{
		Message message = mHandler.obtainMessage();
		// 获取当前软件版本
		int versionCode = getVersionCode(mContext);
		// 把version.xml放到网络上，然后获取文件信息
		// InputStream inStream = ParseXmlService.class.getClassLoader()
		// .getResourceAsStream("version.xml");
		String url = GlobalCache.getCache().getHost() + WebUtils.URLUPDATE;
		InputStream inStream = getVerXMLFromServer(url);
		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
		ParseXmlService service = new ParseXmlService();
		try
		{
			mHashMap = service.parseXml(inStream);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (null != mHashMap)
		{
			int serviceCode = Integer.valueOf(mHashMap.get("version"));
			// 版本判断
			if (serviceCode > versionCode)
			{
				message.obj = true;
			}else{
				message.obj = false;
			}
		}
		inStream.close();
		message.what = ISUPDATE;
		mHandler.sendMessage(message);
	}

	/**
	 * 检查软件是否有更新版本
	 * 提示自动更新部署网络情况
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
//	private boolean isUpdate() throws IOException, Exception
//	{
//		// 获取当前软件版本
//		int versionCode = getVersionCode(mContext);
//		// 把version.xml放到网络上，然后获取文件信息
//		// InputStream inStream = ParseXmlService.class.getClassLoader()
//		// .getResourceAsStream("version.xml");
//		String url = WebUtilsHOST + WebUtils.URLUPDATE;
//		InputStream inStream = getVerXMLFromServer(url);
//		// 解析XML文件。 由于XML文件比较小，因此使用DOM方式进行解析
//		ParseXmlService service = new ParseXmlService();
//		try
//		{
//			mHashMap = service.parseXml(inStream);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		if (null != mHashMap)
//		{
//			int serviceCode = Integer.valueOf(mHashMap.get("version"));
//			// 版本判断
//			if (serviceCode > versionCode)
//			{
//				return true;
//			}
//		}
//		inStream.close();
//		return false;
//	}

	private InputStream getVerXMLFromServer(String url) throws Exception, IOException
	{
		InputStream inStream = null;
		inStream = HTTPGetTool.getTool().getVerXMLFromServer(url);
//		HttpClient client = new DefaultHttpClient();
//		HttpParams httpParams = client.getParams();
//		// 设置网络超时参数
//		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
//		HttpConnectionParams.setSoTimeout(httpParams, 5000);
//		HttpResponse response = client.execute(new HttpGet(url));
//		HttpEntity entity = response.getEntity();
//		if (entity != null)
//		{
//			inStream = entity.getContent();
//		}
		return inStream;
	}

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 获取软件版本信息
	 * 
	 * @param context
	 * @return
	 */
	private String getVersionName(Context context)
	{
		String versionName = "";
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{
		// 构造对话框
		Builder builder = new Builder(mContext);
		
//		AlertDialog.Builder builder = 
		
		builder.setTitle(R.string.soft_update_title);
		builder.setMessage(R.string.soft_update_info);
		// 更新
		builder.setPositiveButton(R.string.soft_update_updatebtn, new OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 显示下载对话框
				showDownloadDialog();
				createNotification();
				// downloadApk();
			}
		});
		// 稍后更新
		builder.setNegativeButton(R.string.soft_update_later, new OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		
		noticeDialog.show();
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		// 构造软件下载对话框
		Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{

			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		mDownloadDialog.setCanceledOnTouchOutside(false);
		mDownloadDialog.show();
		// 现在文件
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		downloadApkThread = new DownloadApkThread();
		downloadApkThread.start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class DownloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				int down_step = 5;// 提示step
				int totalSize;// 文件总大小
				int downloadCount = 0;// 已经下载好的大小
				int updateCount = 0;// 已经上传的文件大小
				InputStream inputStream;
				OutputStream outputStream;
				Message message;

				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";

				}
				else
				{
					mSavePath = mContext.getDir("download", Context.MODE_WORLD_READABLE).toString();
				}

				URL url = new URL(mHashMap.get("url"));
				// 创建连接
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				if (conn.getResponseCode() == 404)
				{
					mHandler.sendEmptyMessage(DOWN_ERROR);
				}
				// 获取文件大小
				totalSize = conn.getContentLength();
				// 创建输入流
				inputStream = conn.getInputStream();

				File file = new File(mSavePath);
				// 判断文件目录是否存在
				if (!file.exists())
				{
					file.mkdirs();
				}
				File apkFile = new File(mSavePath, mHashMap.get("name"));
				outputStream = new FileOutputStream(apkFile.toString(), false);// 文件存在则覆盖掉
				byte buffer[] = new byte[1024];
				int readsize = 0;
				while ((readsize = inputStream.read(buffer)) != -1)
				{
					outputStream.write(buffer, 0, readsize);
					downloadCount += readsize;// 时时获取下载到的大小
					/**
					 * 每次增张5%
					 */
					if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount)
					{
						updateCount += down_step;
						progress = updateCount;
						mHandler.sendEmptyMessage(DOWNLOAD);
						message = handler.obtainMessage();
						message.what = updateCount;
						// 更新进度
						handler.sendMessage(message);
					}
					if (cancelUpdate)
					{
						break;
					};// 点击取消就停止下载.

				}
				if (cancelUpdate)
				{
					mHandler.sendEmptyMessage(DOWN_CANEL);
				}
				else
				{
					mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
				}

				outputStream.close();
				inputStream.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				mHandler.sendEmptyMessage(DOWN_ERROR);
			}

		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk()
	{
		String permission = "666";

		try
		{
			String command = "chmod " + permission + " " + mSavePath + "/" + mHashMap.get("name");
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		File apkfile = new File(mSavePath, mHashMap.get("name"));
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		notification.tickerText = "下载成功，点击安装";
		//下面为eclipse版本写法
//		notification.setLatestEventInfo(mContext, mHashMap.get("name"), "下载成功，点击安装", pendingIntent);
//
		new Notification.Builder(mContext).setContentInfo(mHashMap.get("name")).setContentIntent(pendingIntent).setContentText("下载成功，点击安装");

		notificationManager.notify(notification_id, notification);

		mContext.startActivity(intent);
	}

	/***
	 * 创建通知栏
	 */
	RemoteViews contentView;

	public void createNotification()
	{
		notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.icon = R.drawable.icon;
		// 这个参数是通知提示闪出来的值.
		notification.tickerText = "开始下载";
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		//
		// updateIntent = new Intent(this, MainActivity.class);
		// pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

		// 这里面的参数是通知栏view显示的内容
		// notification.setLatestEventInfo(this, app_name, "下载：0%",
		// pendingIntent);
		//
		// notificationManager.notify(notification_id, notification);

		/***
		 * 在这里我们用自定的view来显示Notification
		 */
		contentView = new RemoteViews(mContext.getPackageName(), R.layout.notification_item);
		contentView.setTextViewText(R.id.notificationTitle, "正在下载");
		contentView.setTextViewText(R.id.notificationPercent, "0%");
		contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

		notification.contentView = contentView;

		updateIntent = new Intent(mContext, Login.class);
		updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		pendingIntent = PendingIntent.getActivity(mContext, 0, updateIntent, 0);

		notification.contentIntent = pendingIntent;

		notificationManager.notify(notification_id, notification);

	}

	public void closeNotification()
	{
		if (notificationManager != null)
		{
			notificationManager.cancel(notification_id);
		}

	}
	
	private class UpdateChecked implements Runnable{

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			Looper.prepare();
			try
			{
				isUpdate();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			Looper.loop();
		}
		
	}

}
