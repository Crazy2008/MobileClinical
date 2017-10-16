package com.winning.mobileclinical.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.text.format.Time;

public class TimeTool {
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH:mm:ss");
	public static int getHour() {
		Time t = new Time();
		t.setToNow();
		return t.hour;
	}
	
	public static Time getNow() {
		Time t = new Time();
		t.setToNow();
		return t;
	}
	
	public static Date getCurrentTime() {
		return new Date(System.currentTimeMillis());
	}
	
	public static Date formateTime(String time) throws ParseException {
		return format.parse(time);
	}
	
	public static Date formateTime(String time,String formatStr) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.parse(time);
	}
	
	public static String parseTime(Date time,String formatStr){
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(time);
	}
	
	public static String formateRiQi(int year, int month, int day) {
		String m;
		if (month < 10)
			m = "0" + month;
		else
			m = month + "";
		String d;
		if (day < 10)
			d = "0" + day;
		else
			d = day + "";
		return year + "-" + m + "-" + d;
	}
}
