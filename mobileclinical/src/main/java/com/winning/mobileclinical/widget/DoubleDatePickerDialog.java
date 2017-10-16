/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.winning.mobileclinical.widget;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.winning.mobileclinical.R;
import com.winning.mobileclinical.model.cis.PickerInfo;

/**
 * A simple dialog containing an {@link DatePicker}.
 * 
 * <p>
 * See the <a href="{@docRoot}guide/topics/ui/controls/pickers.html">Pickers</a>
 * guide..
 * </p>
 */
@SuppressLint("NewApi")
public class DoubleDatePickerDialog extends AlertDialog implements OnClickListener, OnDateChangedListener, OnTimeChangedListener {
	private String mIstimeselector;
	private String mIsrangeselector;
	private String mTitle;
	private String mMin;
	private String mMax;
	private String mBegin;
	private String mEnd;

	private static final String START_YEAR = "start_year";
	private static final String END_YEAR = "end_year";
	private static final String START_MONTH = "start_month";
	private static final String END_MONTH = "end_month";
	private static final String START_DAY = "start_day";
	private static final String END_DAY = "end_day";

	private final DatePicker mDatePicker_start;
	private final DatePicker mDatePicker_end;


	private ImageView mFengeImage;
	private ImageView mFengeImageTo;

	private final OnDateSetListener mCallBack;
	private Context mContext;



	/**
	 * The callback used to indicate the user is done filling in the date.
	 */
	public interface OnDateSetListener {
		/**
		 * 跟你讲下时间控件功能定义 ： 增加这个组建，需要能够通过javascriptinterface调用到。就跟之前的findData一样
		 * ，你提供一个datetimepicker方法，我调用这个方法，就显示这个时间选择窗口 。 我传入7个参数： istimeselector,
		 * israngeselector, title, min, max, begin, end
		 * istimeselector : string "1"表示 精确到时分 "0"表示只精确到年月日
		 * israngeselector: string "1" 表示选择时间段 "0" 表示选择时间点
		 * title : string 标题
		 * min: string 允许的最小日期 格式 "2016010100:01", 传入空字符串则表示没有下限。
		 * max: string 允许的最大日期 格式 "2016010100:01", 传入空字符串则表示没有上限。
		 * begin: string 开始日期初始值 格式 "2016010100:01"
		 * end : string 结束日期初始值 格式 "2016010100:01"
		 *
		 *
		 * 选择完日期,点击确定，再回调一个js方法 datetimepicker_callback ，传入
		 * 选择的两个日期，如果只有一个日期，则只传入一个参数。 点击取消，则不调用。
		 */
		void onDateSet(String s1, String s2);
	}


	@SuppressLint("SimpleDateFormat")
	public DoubleDatePickerDialog(Context context, int theme, OnDateSetListener callBack, PickerInfo pickerInfo, boolean isDayVisible) {
		super(context, theme);
		this.mContext = context;

		mIstimeselector = pickerInfo.istimeselector;
		mIsrangeselector = pickerInfo.israngeselector;
		mTitle = pickerInfo.title;
		mMin = pickerInfo.min;
		mMax = pickerInfo.max;
		mBegin = pickerInfo.begin;
		mEnd = pickerInfo.end;

		mCallBack = callBack;

		Context themeContext = getContext();
		setButton(BUTTON_POSITIVE, "确定", this);
		setButton(BUTTON_NEGATIVE, "取消", this);
		// setButton(BUTTON_POSITIVE,
		// themeContext.getText(android.R.string.date_time_done), this);
		setIcon(0);

		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		setView(view);
		mDatePicker_start = (DatePicker) view.findViewById(R.id.datePickerStart);
		mDatePicker_end = (DatePicker) view.findViewById(R.id.datePickerEnd);

		 mFengeImage= (ImageView) view.findViewById(R.id.fengeImage);
		mFengeImageTo = (ImageView) view.findViewById(R.id.fengeImageTo);



		mTimePickerStart = (TimePicker) view.findViewById(R.id.timePickerStart);
		mTimePickerEnd = (TimePicker) view.findViewById(R.id.timePickerEnd);

		mTv_Title = (TextView) view.findViewById(R.id.tv_title);
		if ("0".equals(mIsrangeselector)) {
			mTv_Title.setText(mTitle);
		}

		setDateMinAndMax();




		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int monthOfYear = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);

		// Log.d("tag",
		// "System.currentMillions=========="+System.currentTimeMillis()+"");
		Log.d("tag", "System.year==========" + year);
		Log.d("tag", "System.monthOfYear==========" + monthOfYear);
		Log.d("tag", "System.dayOfMonth==========" + dayOfMonth);
		Log.d("tag", "System.hour==========" + hour);
		Log.d("tag", "System.minute==========" + minute);

		// mBegin=(mBegin.length())==0?System.currentTimeMillis():mBegin;
		if (mBegin.length() <= 0 && "".equals(mBegin)) {
			
			currentHourStart=hour;
			currentMinuteStart=minute;
			
			
			mDatePicker_start.init(year, monthOfYear, dayOfMonth, this);
			mTimePickerStart.setCurrentHour(hour);
			mTimePickerStart.setCurrentMinute(minute);
			
		} else {
			mDatePicker_start.init(Integer.parseInt(mBegin.substring(0, 4)), Integer.parseInt(mBegin.substring(4, 6)) - 1, Integer.parseInt(mBegin.substring(6, 8)), this);
			
			currentHourStart=Integer.parseInt(mBegin.substring(8, 10));
			currentMinuteStart=Integer.parseInt(mBegin.substring(11, 13));
			mTimePickerStart.setCurrentHour(currentHourStart);
			mTimePickerStart.setCurrentMinute(currentMinuteStart);
			
		}
		if (mEnd.length() <= 0 && "".equals(mEnd)) {
			
			currentHourEnd=hour;
			currentMinuteEnd=minute;
			
			
			mDatePicker_end.init(year, monthOfYear, dayOfMonth, this);
			mTimePickerEnd.setCurrentHour(hour);
			mTimePickerEnd.setCurrentMinute(minute);
		} else {

			mDatePicker_end.init(Integer.parseInt(mEnd.substring(0, 4)), Integer.parseInt(mEnd.substring(4, 6)) - 1, Integer.parseInt(mEnd.substring(6, 8)), this);
			currentHourEnd=Integer.parseInt(mBegin.substring(8, 10));
			currentMinuteEnd=Integer.parseInt(mBegin.substring(11, 13));
			mTimePickerEnd.setCurrentHour(currentHourEnd);
			mTimePickerEnd.setCurrentMinute(currentMinuteEnd);
		}
		
		
		
		
		
		
		
		
		
		

		// ���ò���
		setTimePickerAttr(mTimePickerStart);
		setTimePickerAttr(mTimePickerEnd);

		if ("0".equals(mIstimeselector) && "1".equals(mIsrangeselector)) {
			mFengeImage.setVisibility(View.GONE);
			mFengeImageTo.setVisibility(View.VISIBLE);
			mTimePickerStart.setVisibility(View.GONE);
			mTimePickerEnd.setVisibility(View.GONE);
		}
		if ("0".equals(mIstimeselector) && "0".equals(mIsrangeselector)) {
			mDatePicker_end.setVisibility(View.GONE);
			mFengeImage.setVisibility(View.GONE);
			mTimePickerStart.setVisibility(View.GONE);
			mTimePickerEnd.setVisibility(View.GONE);
		}
		if ("1".equals(mIstimeselector) && "0".equals(mIsrangeselector)) {
			mFengeImage.setVisibility(View.GONE);
			mDatePicker_end.setVisibility(View.GONE);
			mTimePickerEnd.setVisibility(View.GONE);
		}
		if ("1".equals(mIstimeselector) && "1".equals(mIsrangeselector)) {
			mFengeImage.setVisibility(View.GONE);
			mFengeImageTo.setVisibility(View.VISIBLE);
			
		}


		// ��Ӽ����¼�
		mTimePickerStart.setOnTimeChangedListener(this);
		mTimePickerEnd.setOnTimeChangedListener(this);

		if (!isDayVisible) {
			hidDay(mDatePicker_start);
			hidDay(mDatePicker_end);
		}

		currentYearStart = mDatePicker_start.getYear();
		currentYearEnd = mDatePicker_end.getYear();

		currentMonthStart = mDatePicker_start.getMonth();
		this.currentMonthEnd = mDatePicker_end.getMonth();

		currentDayStart = mDatePicker_start.getDayOfMonth();
		currentDayEnd = mDatePicker_end.getDayOfMonth();

		// TODO

	}

	private void setDateMinAndMax() {

		if ((!"".equals(mMin)) & mMin.length() > 13) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			// String pattern = format.format(mMin);
			Date parse = null;
			try {
				parse = format.parse(mMin.substring(0, 8));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mDatePicker_start.setMinDate(parse.getTime());
			mDatePicker_end.setMinDate(parse.getTime());

//			int minHour = Integer.parseInt(mMin.substring(8, 10));
//			mTimePickerStart.setCurrentHour(minHour);
//			mTimePickerEnd.setCurrentHour(minHour);
//
//			int minMinute = Integer.parseInt(mMin.substring(11, 13));
//			mTimePickerStart.setCurrentMinute(minMinute);
//			mTimePickerEnd.setCurrentMinute(minMinute);

		}
		if ((!"".equals(mMax) & mMax.length() > 13)) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			// String pattern = format.format(mMin);
			Date parse = null;
			try {
				parse = format.parse(mMax.substring(0, 8));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mDatePicker_start.setMaxDate(parse.getTime());
			mDatePicker_end.setMaxDate(parse.getTime());

//			int minHour = Integer.parseInt(mMax.substring(8, 10));
//			mTimePickerStart.setCurrentHour(minHour);
//			mTimePickerEnd.setCurrentHour(minHour);
//
//			int minMinute = Integer.parseInt(mMax.substring(11, 13));
//			mTimePickerStart.setCurrentMinute(minMinute);
//			mTimePickerEnd.setCurrentMinute(minMinute);
		}

	}

	/**
	 * ���ÿؼ��Ĳ�������
	 * 
	 * @param timePickerStart
	 */
	private void setTimePickerAttr(TimePicker timePickerStart) {
		// TODO Auto-generated method stub
		timePickerStart.setIs24HourView(true);

	}

	/**
	 * ����DatePicker�е�������ʾ
	 * 
	 * @param mDatePicker
	 */
	private void hidDay(DatePicker mDatePicker) {
		Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
		for (Field datePickerField : datePickerfFields) {
			if ("mDaySpinner".equals(datePickerField.getName())) {
				datePickerField.setAccessible(true);
				Object dayPicker = new Object();
				try {
					dayPicker = datePickerField.get(mDatePicker);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
				// datePicker.getCalendarView().setVisibility(View.GONE);
				((View) dayPicker).setVisibility(View.GONE);
			}
		}
	}



	public void onClick(DialogInterface dialog, int which) {
		// Log.d(this.getClass().getSimpleName(), String.format("which:%d",
		// which));
		// ����ǡ�ȡ ��ť���򷵻أ�����ǡ�ȷ ������ť��������ִ��
		if (which == BUTTON_POSITIVE)
			tryNotifyDateSet();
	}

	/**
	 * ��ÿ�ʼ���ڵ�DatePicker
	 * 
	 * @return The calendar view.
	 */
	public DatePicker getDatePickerStart() {
		return mDatePicker_start;
	}

	/**
	 * ��ý������ڵ�DatePicker
	 * 
	 * @return The calendar view.
	 */
	public DatePicker getDatePickerEnd() {
		return mDatePicker_end;
	}

	/**
	 * Sets the start date.
	 * 
	 * @param year
	 *            The date year.
	 * @param monthOfYear
	 *            The date month.
	 * @param dayOfMonth
	 *            The date day of month.
	 */
	public void updateStartDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker_start.updateDate(year, monthOfYear, dayOfMonth);
	}

	/**
	 * Sets the end date.
	 * 
	 * @param year
	 *            The date year.
	 * @param monthOfYear
	 *            The date month.
	 * @param dayOfMonth
	 *            The date day of month.
	 */
	public void updateEndDate(int year, int monthOfYear, int dayOfMonth) {
		mDatePicker_end.updateDate(year, monthOfYear, dayOfMonth);
	}

	private void tryNotifyDateSet() {
		String hourStartBack = handlerNumberFormat(currentHourStart);
		String minuteStartBack = handlerNumberFormat(currentMinuteStart);
		
		
		
		String str1 = "";
		String str2 = "";
		if (mCallBack != null) {
			mDatePicker_start.clearFocus();
			mDatePicker_end.clearFocus();
			String yearStart = handlerNumberFormat(mDatePicker_start.getYear());
			String monthStart = handlerNumberFormat(mDatePicker_start.getMonth() + 1);
			String dayStart = handlerNumberFormat(mDatePicker_start.getDayOfMonth());
			String yearEnd = handlerNumberFormat(mDatePicker_end.getYear());
			String monthEnd = handlerNumberFormat(mDatePicker_end.getMonth() + 1);
			String dayEnd = handlerNumberFormat(mDatePicker_end.getDayOfMonth());
			Log.d("tag", "yearStart==================" + yearStart);
			Log.d("tag", "monthStart==================" + monthStart);
			Log.d("tag", "dayStart==================" + dayStart);
			Log.d("tag", "yearEnd==================" + yearEnd);
			Log.d("tag", "monthEnd==================" + monthEnd);
			Log.d("tag", "dayEnd==================" + dayEnd);

			if ("1".equals(mIstimeselector) && "1".equals(mIsrangeselector)) {
				Log.i("tag", "hourStart============"+hourStart);
				Log.i("tag", "minuteStart============"+minuteStart);
				Log.i("tag", "currentHourStart============"+currentHourStart);
				
				
				str1 = yearStart + monthStart + dayStart + hourStartBack + ":" + minuteStartBack;
				str2 = yearEnd + monthEnd + dayEnd + handlerNumberFormat(currentHourEnd) + ":" + handlerNumberFormat(currentMinuteEnd);
			}
			if ("0".equals(mIstimeselector) && "0".equals(mIsrangeselector)) {
				str1 = yearStart + monthStart + dayStart;

			}
			if ("1".equals(mIstimeselector) && "0".equals(mIsrangeselector)) {
				str1 = yearStart + monthStart + dayStart + hourStartBack + ":" + minuteStartBack;
			}
			if ("0".equals(mIstimeselector) && "1".equals(mIsrangeselector)) {
				str1 = yearStart + monthStart + dayStart;
				str2 = yearEnd + monthEnd + dayEnd;
			}
			//
		}
		mCallBack.onDateSet(str1, str2);
	}

	private String handlerNumberFormat(int monthStart) {
		String num = "";
		if (monthStart > 9) {
			num = monthStart + "";
		} else {
			num = "0" + monthStart;
		}
		return num;
	}

	@Override
	protected void onStop() {
		// tryNotifyDateSet();
		super.onStop();
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(START_YEAR, mDatePicker_start.getYear());
		state.putInt(START_MONTH, mDatePicker_start.getMonth());
		state.putInt(START_DAY, mDatePicker_start.getDayOfMonth());
		state.putInt(END_YEAR, mDatePicker_end.getYear());
		state.putInt(END_MONTH, mDatePicker_end.getMonth());
		state.putInt(END_DAY, mDatePicker_end.getDayOfMonth());
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int start_year = savedInstanceState.getInt(START_YEAR);
		int start_month = savedInstanceState.getInt(START_MONTH);
		int start_day = savedInstanceState.getInt(START_DAY);
		mDatePicker_start.init(start_year, start_month, start_day, this);

		int end_year = savedInstanceState.getInt(END_YEAR);
		int end_month = savedInstanceState.getInt(END_MONTH);

		int end_day = savedInstanceState.getInt(END_DAY);
		mDatePicker_end.init(end_year, end_month, end_day, this);

	}

	/**
	 * datePicker���������յĻص�����
	 */

	private int currentYearStart, currentYearEnd;// ��
	private int currentMonthStart, currentMonthEnd;// ��
	private int currentDayStart, currentDayEnd;// ��

	
	private int currentHourStart, currentHourEnd;// ʱ
	private int currentMinuteStart, currentMinuteEnd;// ��
	
	
	private TimePicker mTimePickerStart;
	private TimePicker mTimePickerEnd;
	private TextView mTv_Title;

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {

		mStartTime = Integer.parseInt(currentYearStart + "" + handlerNumberFormat(currentMonthStart) + handlerNumberFormat(currentDayStart));
		mEndTime = Integer.parseInt(currentYearEnd + "" + handlerNumberFormat(currentMonthEnd) + handlerNumberFormat(currentDayEnd));
		Log.d("tag", "startTime========" + mStartTime + "");
		switch (view.getId()) {
		case R.id.datePickerStart:
			currentYearStart = view.getYear();
			currentMonthStart = view.getMonth();
			currentDayStart = view.getDayOfMonth();
			/**
			 * 如果开始日期大于结束日期，就重置为结束日期
			 */
			if (mStartTime > mEndTime) {
				updateStartDate(currentYearEnd, currentMonthEnd, currentDayEnd);
			}

			Log.d("tag", "Start year====" + year + "dateStartmonth====" + month + "dateStart day====" + day);
			break;
		case R.id.datePickerEnd:
			currentYearEnd = view.getYear();
			currentMonthEnd = view.getMonth();
			currentDayEnd = view.getDayOfMonth();

			if (mEndTime < mStartTime) {
				updateEndDate(currentYearStart, currentMonthStart, currentDayStart);
			}
			Log.d("tag", "dateEndyear---------" + year + "dateEndmonth---------" + month + "dateEndday---------" + day);

			break;

		default:
			break;

		}

	}
	/**
	 * 返回的时分数据
	 */
	private String hourStart=handlerNumberFormat(currentHourStart);
	private String minuteStart=handlerNumberFormat(currentMinuteStart);
	private String hourEnd=handlerNumberFormat(currentHourEnd);
	private String minuteEnd=handlerNumberFormat(currentMinuteEnd);
	private int mStartTime;
	private int mEndTime;
	
	
	

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		// ��ʼʱ��Ļص�
		case R.id.timePickerStart:
			currentHourStart=view.getCurrentHour();
			
			currentMinuteStart=view.getCurrentMinute();
			
			 Log.d("tag", "currentHourStart====" + currentHourStart);
			 Log.d("tag", "currentMinuteStart====" + currentMinuteStart);
			if(mStartTime==mEndTime&&currentHourStart>currentHourEnd){
				mTimePickerStart.setCurrentHour(currentHourEnd);
			}
			if(mStartTime==mEndTime&&currentHourStart==currentHourEnd&&currentMinuteStart>currentMinuteEnd)
				mTimePickerStart.setCurrentMinute(currentMinuteEnd);
			break;
		// ����ʱ��Ļص�
		case R.id.timePickerEnd:
			
			currentHourEnd=view.getCurrentHour();
			currentMinuteEnd=view.getCurrentMinute();
//			Log.d("tag", "currentHourEnd========" + currentHourEnd);
//			Log.d("tag", "currentMinuteEnd========" + currentMinuteEnd);
		
			
			if(mStartTime==mEndTime&&currentHourEnd<currentHourStart){
				mTimePickerEnd.setCurrentHour(currentHourStart);
			}
			if(mStartTime==mEndTime&&currentHourStart==currentHourEnd&&currentMinuteEnd<currentMinuteStart)
				mTimePickerEnd.setCurrentMinute(currentMinuteStart);
			// Log.d("tag", "currentHourEnd====" + currentHourEnd);
			// Log.d("tag", "currentMinuteEnd====" + currentMinuteEnd);

			break;
		default:
			break;
		}

	}

}
