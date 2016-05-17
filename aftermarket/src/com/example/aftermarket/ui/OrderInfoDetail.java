package com.example.aftermarket.ui;

import com.example.aftermarket.R;
import com.example.aftermarket.bean.OrderItem;
import com.example.aftermarket.bean.OrderItemR;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_info);
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
		Log.e("dajiayilian", "" + getIntent().getSerializableExtra("OrderItemR"));
		if (null != orderItem) {
			mycarTv.setText(orderItem.car_name);
			myNameTv.setText(orderItem.user_name);
			myAddressTv.setText(orderItem.order_address);
			mycmpName.setText(orderItem.company_name);
			moneyTv.setText(orderItem.order_amount + "元");
			timeTv.setText(orderItem.order_date);
			if ("1" == orderItem.pay_id) {
				payWay.setText("支付宝支付");
			} else {
				payWay.setText("微信支付");
			}
			orderItemTv.setText(orderItem.dim_name);

			orderNum.setText(orderItem.order_sn);
			payTime.setText(orderItem.create_date);
		}
		if (null != orderItemR) {
			mycarTv.setText(orderItemR.car_name);
			myNameTv.setText(orderItemR.user_name);
			myAddressTv.setText(orderItemR.order_address);
			mycmpName.setText(orderItemR.company_name);
			moneyTv.setText(orderItemR.order_amount + "元");
			timeTv.setText(orderItemR.order_date);
			if ("1" == orderItemR.pay_id) {
				payWay.setText("支付宝支付");
			} else {
				payWay.setText("微信支付");
			}
			orderItemTv.setText(orderItemR.dim_name);

			orderNum.setText(orderItemR.order_sn);
			payTime.setText(orderItemR.create_date);
		}

	}

	public void backToOrderitem(View view) {
		finish();
	}

}
