package com.example.aftermarket.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.ToRefundListAdapter;
import com.example.aftermarket.bean.OrderItem;
import com.example.aftermarket.bean.OrderItemDetailR;
import com.example.aftermarket.bean.OrderItemR;
import com.example.aftermarket.bean.OrderR;
import com.example.aftermarket.bean.OrderRDetail;
import com.example.aftermarket.config.ConstantClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OrderInfoDetail extends Activity {

	TextView mycarTv;
	TextView myNameTv;
	TextView myAddressTv;
	TextView mycmpName;
	LinearLayout balanceIcon;
	TextView orderItemTv;
	TextView moneyTv;
	TextView timeTv;
	TextView payWay;
	TextView orderNum;
	TextView payTime;
	OrderItem orderItem;
	OrderItemR orderItemR;
	View lineVIew;
	RelativeLayout layout_id_back;
	TextView order_back_money;
	private DemoApplication app;
	private String order_id;
	private String pay_id;
	private OrderRDetail orderRDetail;
	private OrderItemDetailR orderItemDetailR;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_info);
		app = (DemoApplication) getApplication();
		mycarTv = (TextView) findViewById(R.id.car_info_id_info);
		myNameTv = (TextView) findViewById(R.id.textView_name_info);
		myAddressTv = (TextView) findViewById(R.id.choose_addr_tv_id);
		mycmpName = (TextView) findViewById(R.id.cmp_name_order_info);
		balanceIcon = (LinearLayout) findViewById(R.id.imageView4_balance_info);
		orderItemTv = (TextView) findViewById(R.id.order_item_tv_info);
		moneyTv = (TextView) findViewById(R.id.money_num);
		timeTv = (TextView) findViewById(R.id.textView_time_info);
		payWay = (TextView) findViewById(R.id.textView8_way);
		orderNum = (TextView) findViewById(R.id.order_number);
		payTime = (TextView) findViewById(R.id.pay_time);
		orderItem = (OrderItem) getIntent().getSerializableExtra("OrderItem");
		orderItemR = (OrderItemR) getIntent().getSerializableExtra("OrderItemR");
		lineVIew = findViewById(R.id.line_id_back);
		layout_id_back = (RelativeLayout) findViewById(R.id.layout_id_back);
		order_back_money = (TextView) findViewById(R.id.textView_back);
		Log.e("dajiayilian", "" + getIntent().getSerializableExtra("OrderItemR"));

		if (null != orderItem) {

			order_id = orderItem.order_id;
			pay_id= orderItem.pay_id;

			if (orderItem.order_status.equals("5") && orderItem.pay_status.equals("1")) {

				loadData();

			} else if (orderItem.order_status.equals("4") && orderItem.pay_status.equals("1")) {

				loadData();

			} else if (orderItem.order_status.equals("4") && orderItem.pay_status.equals("1")) {

				loadData();

			} else {

				mycarTv.setText(orderItem.car_name);
				myNameTv.setText(orderItem.user_name);
				myAddressTv.setText(orderItem.order_address);
				mycmpName.setText(orderItem.company_name);
				moneyTv.setText(orderItem.order_amount + "元");
				timeTv.setText(orderItem.order_date);
				if ("1" == orderItem.pay_id) {
					payWay.setText("支付宝支付");
				} else if("2" == orderItem.pay_id) {
					payWay.setText("微信支付");
				}else{
					
					payWay.setText("养车币支付");
				}
				orderItemTv.setText(orderItem.dim_name);

				orderNum.setText(orderItem.order_sn);
				payTime.setText(orderItem.create_date);

			}

		}
		if (null != orderItemR) {
			/*
			 * mycarTv.setText(orderItemR.car_name);
			 * myNameTv.setText(orderItemR.user_name);
			 * myAddressTv.setText(orderItemR.order_address);
			 * mycmpName.setText(orderItemR.company_name);
			 * moneyTv.setText(orderItemR.order_amount + "元");
			 * timeTv.setText(orderItemR.order_date);
			 * lineVIew.setVisibility(View.VISIBLE);
			 * layout_id_back.setVisibility(View.VISIBLE);
			 * order_back_money.setText(orderItemR.back.back_money+ "元");
			 * 
			 * if ("1" == orderItemR.pay_id) { payWay.setText("支付宝支付"); } else
			 * if ("2" == orderItemR.pay_id) { payWay.setText("微信支付"); } else {
			 * payWay.setText("养车币支付"); }
			 * orderItemTv.setText(orderItemR.dim_name);
			 * 
			 * orderNum.setText(orderItemR.order_sn);
			 * payTime.setText(orderItemR.create_date);
			 */
			order_id = orderItemR.order_id;
			pay_id= orderItemR.pay_id;
			loadData();
		}

	}

	public void backToOrderitem(View view) {
		finish();
	}

	private void loadData() {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + "orderDetail";
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("order_id", order_id);
		Log.e("Tag", "order_id" + order_id);
		// params.addBodyParameter("page", "1");
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(OrderInfoDetail.this, "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<OrderRDetail>() {
					}.getType();

					Log.e("Tag", "" + result);

					orderRDetail = gson.fromJson(result, type);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

						if (null != orderRDetail) {
							if (null != orderRDetail.data) {

								orderItemDetailR = orderRDetail.data;
								mycarTv.setText(orderItemDetailR.car_name);
								myNameTv.setText(orderItemDetailR.user_name);
								myAddressTv.setText(orderItemDetailR.order_address);
								mycmpName.setText(orderItemDetailR.company_name);
								moneyTv.setText(orderItemDetailR.order_amount + "元");
								timeTv.setText(orderItemDetailR.order_date);
								lineVIew.setVisibility(View.VISIBLE);
								layout_id_back.setVisibility(View.VISIBLE);
								order_back_money.setText(orderItemDetailR.back.back_money + "元");

								if ("1" ==orderItemDetailR.pay_id) {
									payWay.setText("支付宝支付");
								} else if ("2" ==orderItemDetailR.pay_id) {
									payWay.setText("微信支付");
								} else {
									payWay.setText("养车币支付");
								}
								orderItemTv.setText(orderItemDetailR.dim_name);

								orderNum.setText(orderItemDetailR.order_sn);
								payTime.setText(orderItemDetailR.create_date);

							}
						}

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(OrderInfoDetail.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(OrderInfoDetail.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
