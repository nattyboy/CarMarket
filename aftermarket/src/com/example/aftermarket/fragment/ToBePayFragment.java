package com.example.aftermarket.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.ToServerListAdapter;
import com.example.aftermarket.adpter.ToServerListAdapter.SureToPay;
import com.example.aftermarket.bean.Order;
import com.example.aftermarket.bean.OrderItem;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.ui.ShopLoginActivity;
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

public class ToBePayFragment extends Fragment {
	private static final String REQUEST_HEADER = "orderList";
	private static final String TOSERVERID = "1";
	private ListView CarToServerList;
	private Map<String, Object> carToServer = new HashMap<>();
	private Activity orderItemActivity;
	private List<Map<String, Object>> carToServerList = new ArrayList<>();
	private DemoApplication app;
	private Order order;
	private List<OrderItem> orderList;
	protected static final int GOTOPAY = 0x1;
	private LinearLayout noneItems;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		orderItemActivity = getActivity();
		app = (DemoApplication) orderItemActivity.getApplication();
		View view = inflater.inflate(R.layout.car_to_server_fragment_layout, container, false);
		CarToServerList = (ListView) view.findViewById(R.id.car_to_server_listview);
		noneItems=(LinearLayout) view.findViewById(R.id.none_layout);
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
		loadData();
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loadData();
	}

	private void loadData() {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("status", TOSERVERID);
		//params.addBodyParameter("page", "1");
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(orderItemActivity.getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Order>() {
					}.getType();
					order = gson.fromJson(result, type);
					orderList = order.data;
					if (null != orderList) {
						if(orderList.size()>0){
							noneItems.setVisibility(View.GONE);
						}else{
							noneItems.setVisibility(View.VISIBLE);
						}
						
						ToServerListAdapter toServerListAdapter = new ToServerListAdapter(orderItemActivity, orderList);
						CarToServerList.setAdapter(toServerListAdapter);
						
					}else{
						noneItems.setVisibility(View.VISIBLE);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(orderItemActivity, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(orderItemActivity, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	

	public class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {

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
			Intent intent = new Intent(orderItemActivity, PaymentActivity.class);
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
				showMsg(result, errorMsg, extraMsg);
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
		AlertDialog.Builder builder = new Builder(orderItemActivity);
		builder.setMessage(str);
		builder.setTitle("提示");
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}

	

	public class PaymentRequest {
		String channel;
		float amount;

		public PaymentRequest(String channel, float amount) {
			this.channel = channel;
			this.amount = amount;
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

}
