package com.example.time.view.datepicker;

import java.util.Calendar;

import com.example.aftermarket.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class DatePicker {

	private Dialog dialog_time;
	private Context context;
	StringBuffer buffer;
	private String age;
	private int mCurYear;
	private int mCurMonth;
	private Activity activity;
	private int mCurHour;
	private int mCurMinute;
	
	private String[] dateType;
	private TextView submit;
	private TextView cancel;
	private DateNumericAdapter monthAdapter, dayAdapter, yearAdapter,hourAdapter,minuteAdapter;
	private int mCurDay;
	private int style;
	public DatePicker(Context context){
		this.context = context;
		style = R.style.Dialog_Fullscreen;
	} 
	public DatePicker(Context context, int style){
		this.context = context;
		this.style = style;
	}

	public void selectDataTimeDialog(final TextView tv_time,String defaultTime){
		View view = newDataDialog(R.layout.picker_date);

		submit = (TextView) view.findViewById(R.id.submit);
		cancel = (TextView) view.findViewById(R.id.cancel);

		final WheelView year = (WheelView) view.findViewById(R.id.year);
		final WheelView month = (WheelView) view.findViewById(R.id.month);
		final WheelView day = (WheelView) view.findViewById(R.id.day);
		final WheelView hour = (WheelView) view.findViewById(R.id.dialog_time_hour);
		final WheelView minute = (WheelView) view.findViewById(R.id.dialog_time_minute);

		try {
			String time = ((TextView)tv_time).getText().toString().trim();
			if(TextUtils.isEmpty(time)){
				if(TextUtils.isEmpty(defaultTime)){
					this.age = "1980-01-01 10:01";
				}else{
					this.age = defaultTime;
				}
			}else{
				this.age = time;
			}
		} catch (Exception e) {
			// TODO: handle exception
			this.age = "1980-01-01 10:01";
		}


		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((TextView) tv_time).setText(age);
				dialog_time.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog_time.dismiss();
			}
		});
		Calendar calendar = Calendar.getInstance();
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDataTime(year, month, day, hour, minute);
			}

		};


		int curYear = calendar.get(Calendar.YEAR);

		if(age != null && age.contains(" ")){
			String string[] = age.split(" ");
			if (string[0] != null && string[0].contains("-")) {
				String str[] = string[0].split("-");
				mCurYear = 100 - (curYear - Integer.parseInt(str[0]));
				mCurMonth = Integer.parseInt(str[1]) - 1;
				mCurDay = Integer.parseInt(str[2]) - 1;
			}

			if (string[1] != null && string[1].contains(":")) {
				String str[] = string[1].split(":");
				mCurHour =Integer.parseInt(str[0]);
				mCurMinute = Integer.parseInt(str[1]);
			}

		}

		dateType = context.getResources().getStringArray(R.array.date);

		monthAdapter = new DateNumericAdapter(context, 1, 12, 5);
		monthAdapter.setTextType(dateType[1]);
		month.setViewAdapter(monthAdapter);
		month.setCurrentItem(mCurMonth);
		month.addChangingListener(listener);

		yearAdapter = new DateNumericAdapter(context, curYear - 100, curYear + 100, 100 - 20);
		yearAdapter.setTextType(dateType[0]);
		year.setViewAdapter(yearAdapter);
		year.setCurrentItem(mCurYear);
		year.addChangingListener(listener);

		day.setCurrentItem(mCurDay);
		day.addChangingListener(listener);

		hourAdapter = new DateNumericAdapter(context, 0, 23, calendar.get(Calendar.HOUR_OF_DAY));
		hourAdapter.setTextType(dateType[3]);
		hour.setViewAdapter(hourAdapter);
		hour.setCurrentItem(mCurHour);
		hour.addChangingListener(listener);

		minuteAdapter = new DateNumericAdapter(context, 0, 59, calendar.get(Calendar.MINUTE));
		minuteAdapter.setTextType(dateType[4]);
		minute.setViewAdapter(minuteAdapter);
		minute.setCurrentItem(mCurMinute);
		minute.addChangingListener(listener);

		dialog_time.show();
		activity=(Activity) context;
	}
	

	@SuppressWarnings("deprecation")
	private View newDataDialog(int layoutID) {
		buffer = new StringBuffer();
		dialog_time = new Dialog(context, style);
		dialog_time.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(layoutID, null);
		int screenWidth =((Activity)context).getWindowManager().getDefaultDisplay().getWidth();

		Window window = dialog_time.getWindow();
		// 重新设置
		WindowManager.LayoutParams lp = window.getAttributes();
		window.setWindowAnimations(R.style.addresspickerstyle); // 添加动画
		window.setGravity(Gravity.BOTTOM);
		// window.setWindowAnimations(R.style.mystyle); // 添加动画
		lp.x = 0; // 新位置X坐标
		lp.y = 0; // 新位置Y坐标
		// lp.width = screenWidth;
		// window.setAttributes(lp);
		view.setMinimumWidth(screenWidth - 0);
		dialog_time.setContentView(view, lp);
		return view;
	}

	private void updateDataTime(WheelView year, WheelView month, WheelView day, WheelView hour, WheelView minutes){

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		dayAdapter = new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1);
		dayAdapter.setTextType(dateType[2]);
		day.setViewAdapter(dayAdapter);
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		int years = calendar.get(Calendar.YEAR) - 100;
		String monthss = "";
		String dayss = "";
		if(month.getCurrentItem()+1<=9){
			monthss = "0"+(month.getCurrentItem()+1);
		}else{
			monthss = ""+(month.getCurrentItem()+1);
		}
		if(day.getCurrentItem()+1<=9){
			dayss = "0"+(day.getCurrentItem()+1);
		}else{
			dayss = ""+(day.getCurrentItem()+1);
		}

		String hourss = "";
		String minutess = "";
		if(hour.getCurrentItem()<=9){
			hourss = "0"+(hour.getCurrentItem());
		}else{
			hourss = ""+(hour.getCurrentItem());
		}
		if(minutes.getCurrentItem()<=9){
			minutess = "0"+(minutes.getCurrentItem());
		}else{
			minutess = ""+(minutes.getCurrentItem());
		}

		age = years + "-" + monthss + "-" + dayss + " " + hourss + ":" + minutess;
	}
	
	

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(24);
		}

		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			view.setTypeface(Typeface.SANS_SERIF);
		}

		public CharSequence getItemText(int index) {
			currentItem = index;
			return super.getItemText(index);
		}

	}
	
}
