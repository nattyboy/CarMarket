package com.example.aftermarket.ui;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.ReceiptAddressListAdapter;
import com.example.aftermarket.adpter.SquareAdapterGridItem;
import com.example.aftermarket.bean.Address;
import com.example.aftermarket.bean.AddressInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.utils.Date_U;
import com.example.aftermarket.views.NoScrollGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pingplusplus.android.PaymentActivity;
import com.pingplusplus.android.PingppLog;
import com.pingplusplus.libone.PayResultCallBack;
import com.pingplusplus.libone.PingppOnePayment;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.timepick.timeswitch.widget.DatePicker;
import com.timepick.timeswitch.widget.TimePicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InputOrderActivity extends Activity implements PayResultCallBack {

	private DemoApplication app;
	private ImageView orderCalen;// 日历button
	private Calendar calendar;
	private DatePicker dp_test;
	private TimePicker tp_test;
	private TextView tv_ok, tv_cancel; // 确定、取消button
	private PopupWindow pw;
	private TextView orderTimeTv;
	private String selectDate, selectTime;
	private ImageView chooseCar;
	// 选择时间与当前时间，用于判断用户选择的是否是以前的时间
	private int currentHour, currentMinute, currentDay, selectHour, selectMinute, selectDay;
	// 整体布局
	private LinearLayout Ll_all;
	private Button insurePay;
	private TextView addressTv;
	private LinearLayout layout;
	private NoScrollGridView gridView;
	private Address address;
	private ArrayList<Boolean> defaultAddrList = new ArrayList<>();
	private List<AddressInfo> addressList = new ArrayList<>();
	private String companyName;
	PopupWindow popuWindowTel = null;
	View contentView1 = null;
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
	private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";
	private static final String REQUEST_HEADER = "orderSubmit";
	protected static final int GOTOPAY = 0x1;
	protected static final int CHOOSE_CAR_REQUESTCODE = 0x10;
	private static final int RESULT_OK_CAr = 0x20;
	private EditText amountEditText;
	private String currentAmount = "";
	private TextView myCarVersion;
	private RelativeLayout chooseAddrLayout;
	private String washName;
	private String washPrice;
	private String washCmp;
	private TextView oderItemTv;
	private TextView userName;
	private TextView userTel;
	private TextView cmpName;
	private TextView tvChange;
	private ImageView xialaImg;
	private LinearLayout inputOrderBar;
	private LinearLayout inputLayout;
	private PopupWindow popupWindow;
	private SquareAdapterGridItem adapterName;
	private RadioButton zhifubao, weixin, choose_bi;
	private String car_id;
	private int flag = 1;
	private String merchant_id;
	private String address_id;
	private TextView textView_time;
	private RelativeLayout chooseCarLayout;
	private String balance;
	private TextView balanceTv;
	private String cmpNameorder;
	View allView;
	private String business_id = "";
	private String cost = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_order);
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		allView = mLayoutInflater.inflate(R.layout.input_order, null);
		balanceTv = (TextView) findViewById(R.id.textView_bao_order);
		chooseCarLayout = (RelativeLayout) findViewById(R.id.my_car_order);
		zhifubao = (RadioButton) findViewById(R.id.choose_zhifubao);
		weixin = (RadioButton) findViewById(R.id.choose_weixin);
		choose_bi = (RadioButton) findViewById(R.id.choose_bi);
		inputLayout = (LinearLayout) findViewById(R.id.input_order_layout);
		inputOrderBar = (LinearLayout) findViewById(R.id.input_order_progress);
		amountEditText = (EditText) findViewById(R.id.input_money);
		userName = (TextView) findViewById(R.id.textView_name);
		userTel = (TextView) findViewById(R.id.textView_tel);
		addressTv = (TextView) findViewById(R.id.choose_addr_tv_id);
		chooseAddrLayout = (RelativeLayout) findViewById(R.id.choose_addr_id);
		myCarVersion = (TextView) findViewById(R.id.car_info_id);
		SharedPreferences sp = getSharedPreferences("LoginAfterCarActivity", MODE_PRIVATE);
		String category_name = sp.getString("car_Name", "");
		car_id = sp.getString("car_Id", "");
		myCarVersion.setText(category_name);
		app = (DemoApplication) getApplication();
		chooseCar = (ImageView) findViewById(R.id.order_choosecar);
		insurePay = (Button) findViewById(R.id.insure_pay);
		Ll_all = (LinearLayout) findViewById(R.id.Ll_all);
		orderTimeTv = (TextView) findViewById(R.id.order_time);
		oderItemTv = (TextView) findViewById(R.id.order_item_tv);
		calendar = Calendar.getInstance();
		orderCalen = (ImageView) findViewById(R.id.order_rili);
		cmpName = (TextView) findViewById(R.id.cmp_name_order);
		tvChange = (TextView) findViewById(R.id.textView_change);
		xialaImg = (ImageView) findViewById(R.id.xiala_img);
		Intent intent = getIntent();
		washName = intent.getStringExtra("wash_Name");
		washPrice = intent.getStringExtra("wash_Price");
		washCmp = intent.getStringExtra("cmp_name");
		business_id = intent.getStringExtra("business_id");
		cost = intent.getStringExtra("cost");
		cmpNameorder = intent.getStringExtra("cmp_name_order");
		merchant_id = intent.getStringExtra("merchant_id");
		address_id = intent.getStringExtra("address_id");
		cmpName.setText(cmpNameorder);
		balance = getIntent().getStringExtra("balance");
		balanceTv.setText(balance);
		textView_time = (TextView) findViewById(R.id.textView_time);
		inputOrderBar.setVisibility(View.GONE);
		inputLayout.setVisibility(View.VISIBLE);

		if (null != oderItemTv) {
			oderItemTv.setText(washName);
		}
		if (null != washPrice) {
			amountEditText.setText(washPrice.substring(1));
		}

		if (null != washCmp) {
			cmpName.setText(washCmp);
			tvChange.setVisibility(View.GONE);
			xialaImg.setVisibility(View.GONE);

		}
		chooseAddrLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(InputOrderActivity.this, ReceiptAddressActivity.class);
				intent.putExtra("choose_address", "choose_address");
				startActivity(intent);
			}
		});
		orderCalen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				View view = View.inflate(InputOrderActivity.this, R.layout.dialog_select_time, null);
				// selectDate = calendar.get(Calendar.YEAR) + "年" +
				// calendar.get(Calendar.MONTH) + "月" +
				// calendar.get(Calendar.DAY_OF_MONTH) + "日" +
				// DatePicker.getDayOfWeekCN(calendar.get(Calendar.DAY_OF_WEEK));
				selectDate = calendar.get(Calendar.YEAR) + "年" + (((calendar.get(Calendar.MONTH) + 1) < 10) ? ("0" + (calendar.get(Calendar.MONTH) + 1)) : (calendar.get(Calendar.MONTH) + 1)) + "月"
						+ ((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? ("0" + calendar.get(Calendar.DAY_OF_MONTH)) : calendar.get(Calendar.DAY_OF_MONTH)) + "日";
				// 选择时间与当前时间的初始化，用于判断用户选择的是否是以前的时间，如果是，弹出toss提示不能选择过去的时间
				selectDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
				Log.e("time", "2" + calendar.get(Calendar.MONTH));
				selectMinute = currentMinute = calendar.get(Calendar.MINUTE);
				selectHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY);
				selectTime = ((currentHour < 10) ? ("0" + currentHour) : currentHour) + "时" + ((currentMinute < 10) ? ("0" + currentMinute) : currentMinute) + "分";
				dp_test = (DatePicker) view.findViewById(R.id.dp_test);
				tp_test = (TimePicker) view.findViewById(R.id.tp_test);
				tv_ok = (TextView) view.findViewById(R.id.tv_ok);
				tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
				// 设置滑动改变监听器
				dp_test.setOnChangeListener(dp_onchanghelistener);
				tp_test.setOnChangeListener(tp_onchanghelistener);
				pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
				// //设置这2个使得点击pop以外区域可以去除pop
				// pw.setOutsideTouchable(true);
				// pw.setBackgroundDrawable(new BitmapDrawable());
				ColorDrawable cd = new ColorDrawable(0x000000);
				pw.setBackgroundDrawable(cd);
				pw.setAnimationStyle(R.style.ActionSheetDialogAnimation);
				// 产生背景变暗效果
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 0.4f;
				getWindow().setAttributes(lp);
				// 出现在布局底端
				pw.showAtLocation(Ll_all, Gravity.BOTTOM, 0, 0);

				// 点击确定
				tv_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (selectDay == currentDay) { // 在当前日期情况下可能出现选中过去时间的情况
							if (selectHour < currentHour) {
								Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重新选择", 0).show();
							} else if ((selectHour == currentHour) && (selectMinute < currentMinute)) {
								Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重新选择", 0).show();
							} else {
								// orderTimeTv.setText("服务时间:" + selectDate +
								// selectTime);
								textView_time.setText(selectDate + selectTime);
								pw.dismiss();
							}
						} else {
							// orderTimeTv.setText("服务时间:" + selectDate +
							// selectTime);
							textView_time.setText(selectDate + selectTime);
							pw.dismiss();
						}
					}
				});
				// 点击取消
				tv_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						pw.dismiss();
					}
				});
				pw.setOnDismissListener(new OnDismissListener() {

					// 在dismiss中恢复透明度
					public void onDismiss() {
						WindowManager.LayoutParams lp = getWindow().getAttributes();
						lp.alpha = 1f;
						getWindow().setAttributes(lp);
					}
				});

			}
		});

		chooseCarLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(InputOrderActivity.this, MyCarPortActivity.class);
				intent.putExtra("choose_car", "choose_car");
				startActivity(intent);
			}
		});

		zhifubao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				weixin.setBackgroundResource(R.drawable.xuanzhefukuanfangshi_no);
				zhifubao.setBackgroundResource(R.drawable.xuanzhefukuanfangshi);
				choose_bi.setBackgroundResource(R.drawable.xuanzhefukuanfangshi_no);
				payWay = CHANNEL_ALIPAY;
				flag = 1;
				Log.e("hello", "flag-----" + flag);
			}
		});
		weixin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				zhifubao.setBackgroundResource(R.drawable.xuanzhefukuanfangshi_no);
				weixin.setBackgroundResource(R.drawable.xuanzhefukuanfangshi);
				choose_bi.setBackgroundResource(R.drawable.xuanzhefukuanfangshi_no);
				payWay = CHANNEL_WECHAT;
				flag = 2;
				Log.e("hello", "flag-----" + flag);
			}
		});
		choose_bi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				choose_bi.setBackgroundResource(R.drawable.xuanzhefukuanfangshi);
				zhifubao.setBackgroundResource(R.drawable.xuanzhefukuanfangshi_no);
				weixin.setBackgroundResource(R.drawable.xuanzhefukuanfangshi_no);
				flag = 3;
				Log.e("hello", "flag-----" + flag);
			}
		});
		/**
		 * 支付代码
		 */
		insurePay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Log.e("hello", "--flag--" + flag);

				if (flag == 3) {

					Log.e("hello", "--f--" + flag);
					submitOrder();

				} else {

					if (null == app.getData()) {
						submitOrder();

					} else {
						URL = (String) app.getData();
						Message msg = new Message();
						msg.what = GOTOPAY;
						handler.sendEmptyMessage(msg.what);
						app.setData(null);
					}

				}

			}
		});
		amountEditText = (EditText) findViewById(R.id.input_money);
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

		loadAddressData();
		getIntent().getStringExtra("cmp_name");
		if (null != cost) {

			amountEditText.setText(cost);

		}

	}

	// listeners
	DatePicker.OnChangeListener dp_onchanghelistener = new DatePicker.OnChangeListener() {
		@Override
		public void onChange(int year, int month, int day, int day_of_week) {
			selectDay = day;
			// selectDate = year + "年" + month + "月" + day + "日" +
			// DatePicker.getDayOfWeekCN(day_of_week);
			if (month <= calendar.get(Calendar.MONTH))
				month = calendar.get(Calendar.MONTH);
			selectDate = year + "年" + ((month < 10) ? ("0" + month) : month) + "月" + ((day < 10) ? ("0" + day) : day) + "日";
			Log.e("time", "1" + selectDate);
		}
	};
	TimePicker.OnChangeListener tp_onchanghelistener = new TimePicker.OnChangeListener() {
		@Override
		public void onChange(int hour, int minute) {
			selectTime = ((hour < 10) ? ("0" + hour) : hour) + "时" + ((minute < 10) ? ("0" + minute) : minute) + "分";
			selectHour = hour;
			selectMinute = minute;
		}
	};

	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GOTOPAY:
				String amountText = amountEditText.getText().toString();
				if (amountText.equals(""))
					return;

				String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
				String cleanString = amountText.toString().replaceAll(replaceable, "");

				if (null == amountEditText.getText()) {
					Toast.makeText(InputOrderActivity.this, "订单金额不能为空", Toast.LENGTH_SHORT).show();
					return;
				}

				if (TextUtils.isEmpty(amountEditText.getText().toString())) {
					Toast.makeText(InputOrderActivity.this, "订单金额不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if (amountEditText.getText().toString().matches("[0-9]+\\.?[0-9]*")) {

				} else {
					Toast.makeText(InputOrderActivity.this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
					return;
				}
				if (Float.parseFloat(amountEditText.getText().toString()) < 0.009) {
					Toast.makeText(InputOrderActivity.this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
					return;
				}

				float amount = Float.parseFloat(amountEditText.getText().toString().trim());
				new PaymentTask().execute(new PaymentRequest(payWay, amount));
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	public void pingPay(View v) {
		Toast.makeText(this, "去支付", Toast.LENGTH_SHORT).show();

	}

	public class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

		@Override
		protected void onPreExecute() {

			// insurePay.setOnClickListener(null);

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
			Intent intent = new Intent(InputOrderActivity.this, PaymentActivity.class);
			intent.putExtra(PaymentActivity.EXTRA_CHARGE, data);
			startActivityForResult(intent, REQUEST_CODE_PAYMENT);
		}

	}

	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。 最终支付成功根据异步通知为准
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
				Log.e("message", errorMsg + "" + extraMsg);
				// showMsg(result, errorMsg, extraMsg);

				if (result.equals("success")) {
					paySuccess();
				}
				if (result.equals("fail")) {

					Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
					app.setData(null);

				}
				if (result.equals("cancel")) {

					Toast.makeText(this, "取消了支付", Toast.LENGTH_SHORT).show();
					app.setData(null);

				}
				if (result.equals("invalid")) {

					Toast.makeText(this, "未安装支付宝或微信", Toast.LENGTH_SHORT).show();
					app.setData(null);

				}
				app.setData(null);

			}
		}
		// 车辆信息修改
		if (requestCode == CHOOSE_CAR_REQUESTCODE) {
			if (resultCode == RESULT_OK_CAr) {

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		Intent intent = getIntent();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		myCarVersion = (TextView) findViewById(R.id.car_info_id);
		addressTv = (TextView) findViewById(R.id.choose_addr_tv_id);
		userName = (TextView) findViewById(R.id.textView_name);
		userTel = (TextView) findViewById(R.id.textView_tel);
		if (null != intent.getStringExtra("car_version")) {
			myCarVersion.setText(intent.getStringExtra("car_version"));
		}
		if (null != intent.getStringExtra("user_address")) {
			addressTv.setText(intent.getStringExtra("user_address"));
		}
		if (null != intent.getStringExtra("user_name")) {
			userName.setText(intent.getStringExtra("user_name"));
		}
		if (null != intent.getStringExtra("user_tel")) {
			userTel.setText(intent.getStringExtra("user_tel"));
		}
		if (null != intent.getStringExtra("car_id")) {
			car_id = intent.getStringExtra("car_id");
			Log.e("car_version", "car_versionintent" + intent.getStringExtra("user_Name"));
		}
		if (null != intent.getStringExtra("address_id")) {
			address_id = intent.getStringExtra("address_id");
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
		AlertDialog.Builder builder = new Builder(InputOrderActivity.this);
		builder.setMessage(str);
		builder.setTitle("提示");
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}

	private static String postJson(String url, String json) throws IOException {
		MediaType type = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(type, json);
		Request request = new Request.Builder().url(url).post(body).build();

		OkHttpClient client = new OkHttpClient();
		Response response = client.newCall(request).execute();

		return response.body().string();
	}

	public class PaymentRequest {
		String channel;
		float amount;

		public PaymentRequest(String channel, float amount) {
			Log.e("hello", "amount执行到这里" + amount);
			this.channel = channel;
			this.amount = amount;
		}
	}

	@Override
	public void getPayResult(Intent data) {
		if (data != null) {
			/**
			 * result：支付结果信息 code：支付结果码 -2:用户自定义错误、 -1：失败、 0：取消、1：成功
			 */
			// Toast.makeText(this, data.getExtras().getString("result") + "  "
			// + data.getExtras().getInt("code"), Toast.LENGTH_LONG).show();
			if (1 == data.getExtras().getInt("code")) {

				// Toast.makeText(this, "购买成功", Toast.LENGTH_LONG).show();
				// paySuccess();
			} else if (-1 == data.getExtras().getInt("code")) {

				Toast.makeText(this, "付款失败", Toast.LENGTH_LONG).show();
			} else if (0 == data.getExtras().getInt("code")) {

				insurePay.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.e("hello", "--flag--" + flag);

						if (flag == 3) {

							Log.e("hello", "--f--" + flag);
							submitOrder();

						} else {

							if (null == app.getData()) {
								submitOrder();
								Log.e("hello", "amountsubmitOrder执行到这里" + app.getData());

							} else {
								Log.e("hello", "amountsubmitOrder执行到这里" + app.getData());
								URL = (String) app.getData();
								Message msg = new Message();
								msg.what = GOTOPAY;
								handler.sendEmptyMessage(msg.what);
								app.setData(null);
							}

						}

					}
				});
			}
			Log.e("hello", "amountsubmitOrder执行到这里" + app.getData());
		}

	}

	private void submitOrder() {

		Log.e("hello", "getJSONObject" + "-----" + flag);

		if (null == myCarVersion.getText()) {
			Toast.makeText(this, "车型不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(myCarVersion.getText().toString())) {
			Toast.makeText(this, "车型不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		if (null == addressTv.getText()) {
			Toast.makeText(this, "收货地址不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(addressTv.getText().toString())) {
			Toast.makeText(this, "收货地址不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		if (null == oderItemTv.getText()) {
			Toast.makeText(this, "订单类型不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(oderItemTv.getText().toString())) {
			Toast.makeText(this, "订单类型不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (null == amountEditText.getText()) {
			Toast.makeText(this, "订单金额不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(amountEditText.getText().toString())) {
			Toast.makeText(this, "订单金额不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (amountEditText.getText().toString().matches("[0-9]+\\.?[0-9]*")) {

		} else {
			Toast.makeText(this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("hello", "getJSONObject" + "---2--" + flag);
		if (Float.parseFloat(amountEditText.getText().toString()) < 0.009) {
			Toast.makeText(this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
			return;
		}

		if (flag == 3) {
			if (Float.parseFloat(amountEditText.getText().toString()) > Float.parseFloat(app.getBalance())) {
				Toast.makeText(this, "金额不能大于养车币", Toast.LENGTH_SHORT).show();
				return;

			}

		}

		Log.e("hello", "getJSONObject" + "---3--" + flag);

		if (null == textView_time.getText()) {
			Toast.makeText(this, "服务时间不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(textView_time.getText().toString())) {
			Toast.makeText(this, "服务时间不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		Log.e("hello", "getJSONObject" + "---3--" + flag);

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		Log.e("hello", "app.getToken()" + app.getToken());
		params.addBodyParameter("car_id", car_id);
		params.addBodyParameter("address_id", address_id);
		params.addBodyParameter("merchant_id", merchant_id);
		Log.e("hello", "merchant_id" + merchant_id);
		params.addBodyParameter("order_date", new Date_U().data(textView_time.getText().toString() + "00秒"));
		Log.e("Tag", "time" + new Date_U().data(textView_time.getText().toString() + "00秒"));
		params.addBodyParameter("pay_id", String.valueOf(flag));
		Log.e("hello", "flag" + flag);
		params.addBodyParameter("business", oderItemTv.getText().toString());
		params.addBodyParameter("order_amount", amountEditText.getText().toString().trim());
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("business_id", business_id);

		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {

				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.e("hello", "getJSONObject" + result);
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

						Log.e("hello", "getJSONObject" + "---flagflag--" + flag);

						if (flag == 3) {

							Log.e("hello", "getJSONObject" + "---paySuccess--" + flag);

							paySuccess();

							app.setBalance(String.valueOf(Float.parseFloat(app.getBalance()) - Float.parseFloat(amountEditText.getText().toString().trim())));

						} else {

							URL = jsonObj.getJSONObject("data").toString();
							app.setData(URL);
							Log.e("hello", "amountsubmitOrder执行到这里" + app.getData() + "-----" + flag);
							Message msg = new Message();
							msg.what = GOTOPAY;
							handler.sendEmptyMessage(msg.what);
						}

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(InputOrderActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(InputOrderActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(InputOrderActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
					}
					Log.e("hello", "json " + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void showPopupWindowSellerItem(View view) {
		showPopupWindow(view);

	}

	public void showPopupWindow(View parent) {

		ArrayList<String> dataItem = new ArrayList<>();
		dataItem.add("洗车");
		dataItem.add("美容装潢");
		dataItem.add("修复改装");
		dataItem.add("保养");
		dataItem.add("钣喷");
		dataItem.add("诊断维修");
		dataItem.add("轮胎");
		dataItem.add("二手车");
		// 加载布局
		layout = (LinearLayout) LayoutInflater.from(InputOrderActivity.this).inflate(R.layout.order_item, null);
		// 找到布局的控件
		gridView = (NoScrollGridView) layout.findViewById(R.id.gridView_seller_item);
		// 设置适配器
		Log.e("dajiayilian", "gridView" + gridView);
		adapterName = new SquareAdapterGridItem(InputOrderActivity.this, 0);
		Log.e("dajiayilian", "adapterName" + adapterName);
		gridView.setAdapter(adapterName);
		adapterName.addData(dataItem);
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		// 控制键盘是否可以获得焦点
		popupWindow.setFocusable(true);
		// 设置popupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		// 获取xoff
		int xpos = manager.getDefaultDisplay().getWidth() - popupWindow.getWidth();
		// xoff,yoff基于anchor的左下角进行偏移。
		popupWindow.showAsDropDown(parent, 0, 0);
		// 监听
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

				oderItemTv.setText(adapterName.returnCheckTextList());
			}
		});

	}

	public void backToSellerFragment(View view) {
		finish();
	}

	private void loadAddressData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + "addressList";
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Address>() {
					}.getType();
					address = gson.fromJson(result, type);
					if (null != address.data) {
						addressList = address.data;
						Log.e("ReceiptAddressActivity", "ReceiptAddressActivity" + addressList);
						for (int i = 0; i < addressList.size(); i++) {

							if (1 == addressList.get(i).is_default) {
								userName.setText(addressList.get(i).consignee);
								addressTv.setText(addressList.get(i).full_address);
								userTel.setText(addressList.get(i).mobile);
								address_id=addressList.get(i).address_id;

							}
						}
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(InputOrderActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(InputOrderActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void paySuccess() {

		if (popuWindowTel == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(this);
			contentView1 = mLayoutInflater.inflate(R.layout.pay_success, null);
			popuWindowTel = new PopupWindow(contentView1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		ColorDrawable cd = new ColorDrawable(0x000000);
		popuWindowTel.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.6f;
		getWindow().setAttributes(lp);
		popuWindowTel.setOutsideTouchable(false);
		popuWindowTel.setFocusable(false);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		popuWindowTel.setWidth(display.getWidth() * 80 / 100);
		popuWindowTel.showAtLocation(allView, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

		popuWindowTel.update();
		TextView cancelTv = (TextView) contentView1.findViewById(R.id.cancel_tv_pay);
		TextView ringTv = (TextView) contentView1.findViewById(R.id.ring_tv_pay);
		cancelTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				popuWindowTel.dismiss();
			}
		});
		ringTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InputOrderActivity.this, HomeActivity.class);
				startActivity(intent);
				popuWindowTel.dismiss();
				finish();
			}
		});
		popuWindowTel.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				finish();

			}
		});

	}

}
