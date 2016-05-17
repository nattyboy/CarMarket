package com.example.aftermarket.ui;

import org.lmw.demo.slidingtab.widget.PagerSlidingTabStrip;

import com.example.aftermarket.R;
import com.example.aftermarket.adpter.ToServerListAdapter.SureToPay;
import com.example.aftermarket.fragment.AllOrderFragment;
import com.example.aftermarket.fragment.RefundFragment;
import com.example.aftermarket.fragment.ToBePayFragment;
import com.example.aftermarket.fragment.ToBeServerFragment;
import com.example.aftermarket.fragment.ToEvaluateFragment;
import com.example.aftermarket.fragment.ToBePayFragment.PaymentRequest;
import com.example.aftermarket.fragment.ToBePayFragment.PaymentTask;
import com.google.gson.Gson;
import com.pingplusplus.android.PaymentActivity;
import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PayResultCallBack;
import com.pingplusplus.libone.PingppOnePayment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class OrderItemActivity extends FragmentActivity implements PayResultCallBack, SureToPay {
	PagerSlidingTabStrip tabs;
	ViewPager pager;
	DisplayMetrics dm;
	private ImageView onlineTel;
	AllOrderFragment mAllOrderFragment;
	ToBeServerFragment mToBeServerFragment;
	ToBePayFragment mToBePayFragment;
	ToEvaluateFragment mToBeevaluateFragment;
	RefundFragment mRefundFragment;
	String[] titles = { "全部", "待支付", "待服务", "待评价", "退款/售后" };
	PopupWindow popuWindowTel = null;
	View contentView1 = null;
	protected static final int GOTOPAY = 0x1;
	PopupWindow popuWindowTell = null;
	View contentView1l = null;
	View allView;
	/**
	 * 支付所用变量
	 */
	private static String YOUR_URL = "http://218.244.151.190/demo/charge";
	public static String URL = YOUR_URL;

	private static final int REQUEST_CODE_PAYMENT = 1;

	/**
	 * 银联支付渠道
	 */
	private static final String CHANNEL_UPACP = "upacp";
	/**
	 * 微信支付渠道
	 */
	private static final String CHANNEL_WECHAT = "wx";
	/**
	 * 支付支付渠道
	 */
	private static final String CHANNEL_ALIPAY = "alipay";
	/**
	 * 百度支付渠道
	 */
	private static final String CHANNEL_BFB = "bfb";
	/**
	 * 京东支付渠道
	 */
	private String payWay = CHANNEL_WECHAT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		onlineTel = (ImageView) findViewById(R.id.order_onlie_tel);
		onlineTel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (popuWindowTel == null) {
					LayoutInflater mLayoutInflater = LayoutInflater.from(OrderItemActivity.this);
					contentView1 = mLayoutInflater.inflate(R.layout.online_tel, null);
					popuWindowTel = new PopupWindow(contentView1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				}
				ColorDrawable cd = new ColorDrawable(0x000000);
				popuWindowTel.setBackgroundDrawable(cd);
				// 产生背景变暗效果
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 0.4f;
				getWindow().setAttributes(lp);
				popuWindowTel.setOutsideTouchable(true);
				popuWindowTel.setFocusable(true);
				WindowManager windowManager = getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				popuWindowTel.setWidth(display.getWidth() * 80 / 100);
				popuWindowTel.showAtLocation((View) view.getParent(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

				popuWindowTel.update();
				TextView cancelTv = (TextView) contentView1.findViewById(R.id.cancel_tv);
				TextView ringTv = (TextView) contentView1.findViewById(R.id.ring_tv);
				cancelTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						popuWindowTel.dismiss();
					}
				});
				ringTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String mobile = "4000000000";
						Intent intent = new Intent();
						intent.setAction("android.intent.action.CALL");
						intent.setData(Uri.parse("tel:" + mobile));// mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
						startActivity(intent);
						popuWindowTel.dismiss();
					}
				});
				popuWindowTel.setOnDismissListener(new OnDismissListener() {

					// 在dismiss中恢复透明度
					public void onDismiss() {
						WindowManager.LayoutParams lp = getWindow().getAttributes();
						lp.alpha = 1f;
						getWindow().setAttributes(lp);
					}
				});
			}
		});

		/**
		 * 支付代码
		 */
		// 设置需要使用的支付方式,true:显示该支付通道，默认为false
		PingppOnePayment.SHOW_CHANNEL_WECHAT = true;
		// PingppOnePayment.SHOW_CHANNEL_UPACP = true;
		// PingppOnePayment.SHOW_CHANNEL_BFB = true;
		PingppOnePayment.SHOW_CHANNEL_ALIPAY = true;

		// 设置支付通道的排序,最小的排在最前
		// PingppOnePayment.CHANNEL_UPACP_INDEX = 1;
		PingppOnePayment.CHANNEL_ALIPAY_INDEX = 2;
		PingppOnePayment.CHANNEL_WECHAT_INDEX = 3;
		// PingppOnePayment.CHANNEL_BFB_INDEX = 4;
		PingppOnePayment.CONTENT_TYPE = "application/json";
		PingppLog.DEBUG = true;

	}

	public void backTOMycenter(View v) {
		finish();
	}

	private void initView() {
		setContentView(R.layout.order_item_activity);
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		allView = mLayoutInflater.inflate(R.layout.order_item_activity, null);
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager.setAdapter(new MyAdapter(getSupportFragmentManager(), titles));
		tabs.setViewPager(pager);
		tabs.setSelectedTextColor(0xFF007ACB);
		tabs.setIndicatorColor(0xFF0B83CF);
		tabs.setUnderlineHeight(2);
		tabs.setIndicatorHeight(8);
		Intent intent = getIntent();
		intent.getIntExtra("currentPage", 0);
		pager.setCurrentItem(intent.getIntExtra("currentPage", 0));

	}

	public class MyAdapter extends FragmentPagerAdapter {
		String[] _titles;

		public MyAdapter(FragmentManager fm, String[] titles) {
			super(fm);
			_titles = titles;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return _titles[position];
		}

		@Override
		public int getCount() {
			return _titles.length;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				if (mAllOrderFragment == null) {
					mAllOrderFragment = new AllOrderFragment();
				}
				return mAllOrderFragment;
			case 1:
				if (mToBePayFragment == null) {
					mToBePayFragment = new ToBePayFragment();
				}
				return mToBePayFragment;
			case 2:
				if (mToBeServerFragment == null) {
					mToBeServerFragment = new ToBeServerFragment();
				}
				return mToBeServerFragment;
			case 3:
				if (mToBeevaluateFragment == null) {
					mToBeevaluateFragment = new ToEvaluateFragment();
				}
				return mToBeevaluateFragment;
			case 4:
				if (mRefundFragment == null) {
					mRefundFragment = new RefundFragment();
				}
				return mRefundFragment;
			default:
				return null;
			}
		}
	}

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GOTOPAY:
				new PaymentTask().execute(new PaymentRequest(payWay, 1));
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(PaymentRequest... pr) {

			PaymentRequest paymentRequest = pr[0];
			String data = null;
			String json = new Gson().toJson(paymentRequest);
			try {
				// 向Your Ping++ Server SDK请求数据
				Log.e("hello", "URL " + URL);
				data = URL;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		/**
		 * 获得服务端的charge，调用ping++ sdk。
		 */
		@Override
		protected void onPostExecute(String data) {
			if (null == data) {
				showMsg("请求出错", "请检查URL", "URL无法获取charge");
				return;
			}
			Log.d("charge", data);
			Intent intent = new Intent(OrderItemActivity.this, PaymentActivity.class);
			intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
			startActivityForResult(intent, REQUEST_CODE_PAYMENT);
		}

	}

	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。 最终支付成功根据异步通知为准
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 支付页面返回处理
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getExtras().getString("pay_result");
				/*
				 * 处理返回值 "success" - payment succeed "fail" - payment failed
				 * "cancel" - user canceld "invalid" - payment plugin not
				 * installed
				 */
				String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
				String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
				//showMsg(result, errorMsg, extraMsg);
				if ("user_cancelled".equals(errorMsg)) {

				} else {
					paySuccess();
				}
			}
		}
	}

	public void showMsg(String title, String msg1, String msg2) {
		String str = title;
		if (null != msg1 && msg1.length() != 0) {
			str += "\n" + msg1;
		}
		if (null != msg2 && msg2.length() != 0) {
			str += "\n" + msg2;
		}
		AlertDialog.Builder builder = new Builder(OrderItemActivity.this);
		builder.setMessage(str);
		builder.setTitle("提示");
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}

	@Override
	public void getPayResult(Intent data) {
		if (data != null) {
			/**
			 * result：支付结果信息 code：支付结果码 -2:用户自定义错误、 -1：失败、 0：取消、1：成功
			 */
			if (1 == data.getExtras().getInt("code")) {

				// Toast.makeText(this, "购买成功", Toast.LENGTH_LONG).show();
				// paySuccess();
				paySuccess();
			} else if (-1 == data.getExtras().getInt("code")) {

				Toast.makeText(this, "付款失败", Toast.LENGTH_LONG).show();
			} else if (0 == data.getExtras().getInt("code")) {
				
			}
		}
	}

	class PaymentRequest {
		String channel;
		float amount;

		public PaymentRequest(String channel, float amount) {
			Log.e("hello", "amount执行到这里" + amount);
			this.channel = channel;
			this.amount = amount;
		}
	}

	@Override
	public void goToPay(String s) {
		// TODO Auto-generated method stub
		URL = s;
		Message msg = new Message();
		msg.what = GOTOPAY;
		handler.sendEmptyMessage(msg.what);
		// Toast.makeText(this, "回掉成功"+URL , Toast.LENGTH_LONG).show();
	}
	
	public void paySuccess() {

		if (popuWindowTell == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(this);
			contentView1l = mLayoutInflater.inflate(R.layout.pay_success, null);
			popuWindowTell = new PopupWindow(contentView1l, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		ColorDrawable cd = new ColorDrawable(0x000000);
		popuWindowTell.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.6f;
		getWindow().setAttributes(lp);
		popuWindowTell.setOutsideTouchable(true);
		popuWindowTell.setFocusable(true);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		popuWindowTell.setWidth(display.getWidth() * 80 / 100);
		popuWindowTell.showAtLocation(allView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

		popuWindowTell.update();
		TextView cancelTv = (TextView) contentView1l.findViewById(R.id.cancel_tv_pay);
		TextView ringTv = (TextView) contentView1l.findViewById(R.id.ring_tv_pay);
		cancelTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				popuWindowTell.dismiss();
			}
		});
		ringTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(OrderItemActivity.this,HomeActivity.class);
				startActivity(intent);
				popuWindowTell.dismiss();
				finish();
			}
		});
		popuWindowTell.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);

			}
		});

	}

}