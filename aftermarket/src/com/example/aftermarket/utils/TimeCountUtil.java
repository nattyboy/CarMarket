package com.example.aftermarket.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.TextView;

public class TimeCountUtil extends CountDownTimer {

	private Activity mActivity;
	private TextView btn;// 按钮

	public TimeCountUtil(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
		// TODO Auto-generated constructor stub
	}

	public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, TextView btn) {
		super(millisInFuture, countDownInterval);
		this.mActivity = mActivity;
		this.btn = btn;
	}

	@Override
	public void onTick(long millisUntilFinished) {

		btn.setClickable(false);// 设置不能点击
		btn.setText(millisUntilFinished / 1000 + "秒后重新获取");// 设置倒计时时间
		btn.setTextSize(12);
		btn.setGravity(Gravity.CENTER);
		// 设置按钮为灰色，这时是不能点击的

		Spannable span = new SpannableString(btn.getText().toString());// 获取按钮的文字
		span.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);// 讲倒计时时间显示为红色
		btn.setText(span);
	}

	@Override
	public void onFinish() {
		btn.setText("重新获取  ");
		btn.setTextSize(15);
		btn.setGravity(Gravity.CENTER);
		btn.setClickable(true);// 重新获得点击
	}

}
