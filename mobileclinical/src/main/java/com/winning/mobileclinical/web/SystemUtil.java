package com.winning.mobileclinical.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;











import com.winning.mobileclinical.globalCache.GlobalCache;
import com.winning.mobileclinical.model.NameValue;
import com.winning.mobileclinical.model.cis.MisConfigInfo;
import com.winning.mobileclinical.model.cis.TableClass;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Vibrator;
import android.text.SpannableString;
import android.util.Log;
import android.widget.Toast;

public class SystemUtil {

	public static String getMAC(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	public static String getLocalIpAddress() {
		Enumeration<NetworkInterface> netInterfaces = null;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				// System.out.println("DisplayName:" + ni.getDisplayName());
				// System.out.println("Name:" + ni.getName());
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress ip = ips.nextElement();
					if (!ip.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ip
									.getHostAddress())) { // gutao
						return ip.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	public static int getDrawbleIdByName(Context context, String name) {
		int id = 0;
		try {
			id = context.getResources().getIdentifier(name, "drawable",
					"com.winning.mobileclinical");

		} catch (Exception e) {
			id = context.getResources().getIdentifier("about_doctor_selected",
					"drawable", "com.winning.mobileclinical");
		}

		return id;
	}

	public static void playSound(Context context, int resource) {
		AudioManager mAudioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
		if (volume == 0) { // ����Ϊ0ʱ��������
			Vibrator vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(1500); // ��2��
		} else {
			// ��ͨͨ��ҽԺ��Ӧ����̫С
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
					volume * 2, AudioManager.FLAG_PLAY_SOUND);
			MediaPlayer mp = null;
			mp = MediaPlayer.create(context, resource);
			if (mp != null)
				mp.start();
		}

	}

	public static Date getDate(Context context) {
		Date date;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"winning_cmie", 0);
		long timediff = Long.parseLong(sharedPreferences.getString("timediff",
				"0"));
		try {
			date = new Date(System.currentTimeMillis() + timediff);
		} catch (Exception e) {
			date = new Date();
		}
		return date;
	}

	public static void savePreference(Context context, String name, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"winning_cmie", 0);
		Editor edit = sharedPreferences.edit();
		edit.putString(name, value);
		edit.commit();
	}

	public static String getPreference(Context context, String name,
			String defaultvalue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"winning_cmie", 0);
		return sharedPreferences.getString(name, defaultvalue);
	}

	public static boolean isConnect(Context context) {
		// 获取手机�?��连接管理对象（包括对wi-fi,net等连接的管理�?
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对�?
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("error", e.toString());
		}
		return false;
	}

	public static void getConnect(Context context) {
		if (isConnect(context) == false) {
			new AlertDialog.Builder(context)
					.setTitle("网络错误")
					.setMessage("网络连接失败，请确认网络连接")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									// System.exit(1);
									return;
								}
							}).show();
		}
		//
		// AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// final Dialog dialog = builder.show();
		// builder.setTitle("网络错误")
		// .setMessage("网络连接失败，请确认网络连接")
		// .setPositiveButton("确定",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface arg0,
		// int arg1) {
		// // TODO Auto-generated method stub
		// System.exit(0);
		// }
		// });
		//
		// dialog.show();

	}
	
	
	
	
	
	public static void RusultRequrst(Context context,String result) {
		
		if(result == null || result=="null") {
			Toast.makeText(context, "请检查服务或者网络是否连接正确",Toast.LENGTH_LONG).show();
			return;
			}
		
	}
	
	
	
	

	public static  boolean getPing() {

		String result = null;

		try {

			String ip = "180.97.33.107";// 

			Process p = Runtime.getRuntime().exec("/system/bin/ping -c 1 -w 10 " + ip);// ping3次

			// 读取ping的内容，可不加。

//			InputStream input = p.getInputStream();
//
//			BufferedReader in = new BufferedReader(new InputStreamReader(input));
//
//			StringBuffer stringBuffer = new StringBuffer();
//
//			String content = "";
//
//			while ((content = in.readLine()) != null) {
//
//				stringBuffer.append(content);
//
//			}
//
//			Log.i("TTT", "result content : " + stringBuffer.toString());

			int status = p.waitFor();

			if (status == 0) {

				result = "successful~";

				return true;

			} else {

				result = "failed~ cannot reach the IP address";

			}

		} catch (IOException e) {

			result = "failed~ IOException";

		} catch (InterruptedException e) {

			result = "failed~ InterruptedException";

		} finally {

			Log.i("TTT", "result = " + result);

		}

		return false;

	}
	
	public static String getNameValue(String json) {
		net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(json);
		StringBuffer sb = new StringBuffer();
		List<NameValue> list = new ArrayList<NameValue>();
		for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();) {
			NameValue nameValue = new NameValue();
			String key = (String) iter.next();
			nameValue.setName(key);
			nameValue.setValue(jsonObject.get(key).toString());
			list.add(nameValue);
		}
		if(list.size()> 0) {
			for(int i=0;i<list.size();i++) {
				if(i == (list.size()-1)) {
					sb.append(list.get(i).getName());
					sb.append("='"+list.get(i).getValue() + "'");
				} else {
					sb.append(list.get(i).getName());
					sb.append("='"+list.get(i).getValue() + "'  and  ");
				}
				
			}
		}
		return sb.toString();
	}
	
	
	public static String getValue(String key)
	{
		String value = "";
		List<MisConfigInfo> misConfigInfos = GlobalCache.getCache().getMisConfigInfos();
		if(misConfigInfos != null) {
			for(int i=0; i<misConfigInfos.size();i++) {
				if(key == misConfigInfos.get(i).getId() || key.equals(misConfigInfos.get(i).getId())) {
					value = misConfigInfos.get(i).getConfigvalue();
					break;
				}
			}
		}
		return value;
	}
	
	
	public static Integer getJsonLB(String provider, String service)
	{
		int value = 0;
		List<TableClass> table = GlobalCache.getCache().getTableClasses();
		if(table != null) {
			for(int i=0; i<table.size();i++) {
				if((provider == table.get(i).getProvider() || provider.equals(table.get(i).getProvider())) && 
						(service == table.get(i).getService() || service.equals(table.get(i).getService()))) {
					value = table.get(i).single;
					break;
				}
			}
		}
		return value;
	}
}
