package com.winning.mobileclinical.activity;


import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.Bwl;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon = R.drawable.fill4;
		notification.defaults = Notification.DEFAULT_LIGHTS;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.when = System.currentTimeMillis();
		
		String tickerText = intent.getStringExtra("tickerText");
		String title = intent.getStringExtra("title");
		String message = intent.getStringExtra("message");
		Bwl bwl = (Bwl) intent.getExtras().get("bwl");
		notification.tickerText = tickerText;
		
//		Intent intent2 = new Intent(context,BWLEditActivity.class);
//		intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent2.putExtra("bwl", bwl);
		
//		PendingIntent contentIntent = PendingIntent.getActivity(
//				context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
//		notification.setLatestEventInfo(context, "备忘录提醒:"+title, message,contentIntent);
//		notificationManager.notify(0, notification);
	}

}
