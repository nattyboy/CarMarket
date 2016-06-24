package com.example.aftermarket.adpter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.MainActivity;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.HomePageInfo;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.ui.InputOrderActivity;
import com.example.aftermarket.ui.SelfWashCarSeller;
import com.example.aftermarket.ui.SellerInfoActivity;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.example.aftermarket.ui.WashCarActivity;
import com.example.aftermarket.views.NoScrollListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SellerListAdapter extends BaseAdapter {
	private ArrayList<SellerInfo> sellerItems;
	// private ArrayList<SellerInfo> sellerInfoList
	private Context context;
	private LayoutInflater inflater;
	Seller homePageInfo;
	private DemoApplication app;
	private Activity activity;
	private int tyreId = 0;

	public SellerListAdapter(Context context, ArrayList<SellerInfo> sellerItems) {
		this.context = context;
		this.sellerItems = sellerItems;
		inflater = LayoutInflater.from(context);
		this.activity = (Activity) context;
		app = (DemoApplication) activity.getApplication();
	}

	public SellerListAdapter(Context context, ArrayList<SellerInfo> sellerItems, int tyreId) {
		this.context = context;
		this.sellerItems = sellerItems;
		inflater = LayoutInflater.from(context);
		this.activity = (Activity) context;
		app = (DemoApplication) activity.getApplication();
		// 当tyreId==1时，代表自助洗车
		this.tyreId = tyreId;
	}

	@Override
	public int getCount() {

		return sellerItems.size() > 0 ? sellerItems.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return sellerItems.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.seller_list_item, null);
			vh = new ViewHolder();
			vh.seller_Cmp_Name = (TextView) convertView.findViewById(R.id.seller_cmp_name);
			vh.seller_Cmp_balance = (LinearLayout) convertView.findViewById(R.id.seller_cmp_balance);
			vh.seller_RatingBar = (RatingBar) convertView.findViewById(R.id.seller_ratingBar);
			vh.seller_Rating_Grade = (TextView) convertView.findViewById(R.id.seller_rate_grade);
			vh.custom_Option_Num = (TextView) convertView.findViewById(R.id.custom_option_num);
			vh.seller_Icon = (ImageView) convertView.findViewById(R.id.seller_icon);
			vh.seler_Distance = (TextView) convertView.findViewById(R.id.seller_distance);
			vh.seller_Address = (TextView) convertView.findViewById(R.id.seller_address);
			vh.seller_Sucess = (TextView) convertView.findViewById(R.id.seller_sucess);
			vh.in_Price = (TextView) convertView.findViewById(R.id.in_price);
			vh.carful_Price = (TextView) convertView.findViewById(R.id.carful_price);
			vh.out_Price = (TextView) convertView.findViewById(R.id.out_price);
			vh.standar_Price = (TextView) convertView.findViewById(R.id.stand_price);
			vh.inPriceBt = (TextView) convertView.findViewById(R.id.in_price_bt);
			vh.carfulPriceBt = (TextView) convertView.findViewById(R.id.carful_price_bt);
			vh.outPriceBt = (TextView) convertView.findViewById(R.id.out_price_bt);
			vh.standPriceBt = (TextView) convertView.findViewById(R.id.stand_price_bt);
			vh.standLayout = (RelativeLayout) convertView.findViewById(R.id.stand_layout);
			vh.outLayout = (RelativeLayout) convertView.findViewById(R.id.out_layout);
			vh.inLayout = (RelativeLayout) convertView.findViewById(R.id.in_layout);
			vh.carfulLayout = (RelativeLayout) convertView.findViewById(R.id.carful_layout);
			// vh.distance_Icon = (ImageView)
			// convertView.findViewById(R.id.distance_icon);
			vh.balanceMoney = (TextView) convertView.findViewById(R.id.textView_bao);
			vh.sellerClick1 = (LinearLayout) convertView.findViewById(R.id.seller_info_click1);
			vh.sellerClick2 = (LinearLayout) convertView.findViewById(R.id.seller_info_click2);
			vh.distance_icon = (ImageView) convertView.findViewById(R.id.distance_icon);
			vh.mNoListView = (NoScrollListView) convertView.findViewById(R.id.listView_promote_list);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.mNoListView.setAdapter(new PromoteAdapter(context, sellerItems.get(position).promote,sellerItems,position));

		Log.e("dajia", "--inflater---" + sellerItems.get(position).balance);
		vh.seller_Cmp_Name.setText(sellerItems.get(position).company_name);
		// vh.seller_Cmp_balance.setText(sellerItems.get(position).balance);
		vh.balanceMoney.setText(sellerItems.get(position).margin);
		vh.seller_RatingBar.setRating(sellerItems.get(position).score);
		vh.seller_Rating_Grade.setText(sellerItems.get(position).score + "分");
		vh.custom_Option_Num.setText(sellerItems.get(position).evaluation_num + "人评价");
		if (context instanceof SelfWashCarSeller) {
			vh.standLayout.setVisibility(View.GONE);
			vh.inLayout.setVisibility(View.GONE);
			vh.outLayout.setVisibility(View.GONE);
			vh.carfulLayout.setVisibility(View.GONE);
			vh.seler_Distance.setVisibility(View.GONE);
			vh.distance_icon.setVisibility(View.GONE);
		}
		if (null != sellerItems.get(position).distance) {
			BigDecimal distanceValu = new BigDecimal(sellerItems.get(position).distance);
			BigDecimal finalValue = distanceValu.divide(new BigDecimal(1000), 1, BigDecimal.ROUND_HALF_UP);
			vh.seler_Distance.setText("距离：< " + finalValue + " km");
			BigDecimal b = new BigDecimal(15000);
			if (finalValue.compareTo(b) >= 0) {

				vh.seler_Distance.setText("距离：" + "未知");
			}

		}

		if (null == sellerItems.get(position).distance) {
			vh.seler_Distance.setVisibility(View.GONE);
			vh.distance_icon.setVisibility(View.GONE);
		}
		vh.seller_Address.setText("地址： " + sellerItems.get(position).address);
		if (null != sellerItems.get(position).business_approve) {
			vh.seller_Sucess.setText("已成交数：" + sellerItems.get(position).business_approve);
		} else {
			vh.seller_Sucess.setText("已成交数：" + 0);
		}

		if (context instanceof SelfWashCarSeller) {

		} else {

			if (null != sellerItems.get(position).door && 0 != sellerItems.get(position).door.size()) {

				for (int i = 0; i < sellerItems.get(position).door.size(); i++) {

					if (sellerItems.get(position).door.get(i).id.equals("104")) {

						vh.carfulLayout.setVisibility(View.VISIBLE);
						vh.carful_Price.setText("￥" + sellerItems.get(position).door.get(i).cost);
					}
					if (sellerItems.get(position).door.get(i).id.equals("103")) {

						vh.standLayout.setVisibility(View.VISIBLE);
						vh.standar_Price.setText("￥" + sellerItems.get(position).door.get(i).cost);
					}

				}

			} else {

			}
			if (null != sellerItems.get(position).shop && 0 != sellerItems.get(position).shop.size()) {

				for (int i = 0; i < sellerItems.get(position).shop.size(); i++) {

					if (sellerItems.get(position).shop.get(i).id.equals("102")) {

						vh.outLayout.setVisibility(View.VISIBLE);
						vh.out_Price.setText("￥" + sellerItems.get(position).shop.get(i).cost);
					}
					if (sellerItems.get(position).shop.get(i).id.equals("101")) {

						vh.inLayout.setVisibility(View.VISIBLE);
						vh.in_Price.setText("￥" + sellerItems.get(position).shop.get(i).cost);
					}

				}

			} else {

			}
		}
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/x-utils";
		BitmapUtils bitmapUtils = new BitmapUtils(context, path, cacheSize);
		bitmapUtils.configDefaultBitmapMaxSize(100, 100);

		bitmapUtils.display(vh.seller_Icon, sellerItems.get(position).company_logo);
		vh.inPriceBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null == app.getToken()) {

					Toast.makeText(context, "请登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(context, ShopLoginActivity.class);
					context.startActivity(intent);

				} else {

					Intent intent = new Intent(context, InputOrderActivity.class);
					intent.putExtra("wash_Price", vh.in_Price.getText());
					intent.putExtra("wash_Name", "车内清洁");
					intent.putExtra("cmp_name", sellerItems.get(position).company_name);
					intent.putExtra("merchant_id", sellerItems.get(position).merchant_id);
					intent.putExtra("balance", sellerItems.get(position).margin);
					context.startActivity(intent);
				}
			}
		});
		vh.outPriceBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null == app.getToken()) {

					Toast.makeText(context, "请登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(context, ShopLoginActivity.class);
					context.startActivity(intent);

				} else {
					Intent intent = new Intent(context, InputOrderActivity.class);
					intent.putExtra("wash_Price", vh.out_Price.getText());
					intent.putExtra("wash_Name", "车外清洁");
					intent.putExtra("cmp_name", sellerItems.get(position).company_name);
					intent.putExtra("merchant_id", sellerItems.get(position).merchant_id);
					intent.putExtra("balance", sellerItems.get(position).margin);
					context.startActivity(intent);
				}
			}
		});
		vh.standPriceBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null == app.getToken()) {

					Toast.makeText(context, "请登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(context, ShopLoginActivity.class);
					context.startActivity(intent);

				} else {

					Intent intent = new Intent(context, InputOrderActivity.class);

					intent.putExtra("wash_Name", "标准洗车");
					intent.putExtra("cmp_name", sellerItems.get(position).company_name);
					intent.putExtra("merchant_id", sellerItems.get(position).merchant_id);
					intent.putExtra("balance", sellerItems.get(position).margin);
					context.startActivity(intent);
				}
			}
		});

		vh.carfulPriceBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null == app.getToken()) {

					Toast.makeText(context, "请登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(context, ShopLoginActivity.class);
					context.startActivity(intent);

				} else {

					Intent intent = new Intent(context, InputOrderActivity.class);

					intent.putExtra("wash_Name", "精细洗车");
					intent.putExtra("cmp_name", sellerItems.get(position).company_name);
					intent.putExtra("merchant_id", sellerItems.get(position).merchant_id);
					intent.putExtra("balance", sellerItems.get(position).margin);
					context.startActivity(intent);
				}
			}
		});
		if (tyreId == 1) {

		} else {

			vh.seller_Cmp_Name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(context, SellerInfoActivity.class);
					intent.putExtra("cmp_name", sellerItems.get(position).company_name);
					intent.putExtra("easemob_user", sellerItems.get(position).easemob_user);
					intent.putExtra("merchant_id", sellerItems.get(position).merchant_id);
					intent.putExtra("is_collect", sellerItems.get(position).is_collect);
					// intent.putExtra("in_Price", vh.in_Price.getText());
					// intent.putExtra("out_Price", vh.out_Price.getText());
					intent.putExtra("balance", sellerItems.get(position).margin);
					if (null == sellerItems.get(position).shop || 0 == sellerItems.get(position).shop.size()) {

					} else {
						intent.putExtra("out_Price", vh.out_Price.getText());
						intent.putExtra("standar_Price", vh.standar_Price.getText());
						intent.putExtra("in_Price", vh.in_Price.getText());

					}
					if (null == sellerItems.get(position).door || 0 == sellerItems.get(position).door.size()) {

					} else {

						intent.putExtra("carful_Price", vh.carful_Price.getText());
						intent.putExtra("standar_Price", vh.standar_Price.getText());
						Log.e("tag", "1111" + vh.standar_Price.getText().toString());
					}

					context.startActivity(intent);
				}
			});

			vh.sellerClick1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(context, SellerInfoActivity.class);
					intent.putExtra("cmp_name", sellerItems.get(position).company_name);
					intent.putExtra("easemob_user", sellerItems.get(position).easemob_user);
					intent.putExtra("merchant_id", sellerItems.get(position).merchant_id);
					intent.putExtra("is_collect", sellerItems.get(position).is_collect);
					intent.putExtra("balance", sellerItems.get(position).margin);

					if (null == sellerItems.get(position).shop || 0 == sellerItems.get(position).shop.size()) {

					} else {
						intent.putExtra("out_Price", vh.out_Price.getText());
						intent.putExtra("standar_Price", vh.standar_Price.getText());
						Log.e("tag", "1111" + vh.standar_Price.getText().toString());
					}
					if (null == sellerItems.get(position).door || 0 == sellerItems.get(position).door.size()) {

					} else {

						intent.putExtra("in_Price", vh.in_Price.getText());
						intent.putExtra("carful_Price", vh.carful_Price.getText());
					}
					context.startActivity(intent);
				}
			});

			vh.sellerClick2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(context, SellerInfoActivity.class);
					intent.putExtra("cmp_name", sellerItems.get(position).company_name);
					intent.putExtra("easemob_user", sellerItems.get(position).easemob_user);
					intent.putExtra("merchant_id", sellerItems.get(position).merchant_id);
					intent.putExtra("is_collect", sellerItems.get(position).is_collect);
					intent.putExtra("balance", sellerItems.get(position).margin);
					if (null == sellerItems.get(position).shop || 0 == sellerItems.get(position).shop.size()) {

					} else {
						intent.putExtra("out_Price", vh.out_Price.getText());
						intent.putExtra("standar_Price", "1111" + vh.standar_Price.getText());
						Log.e("tag", "1111" + vh.standar_Price.getText().toString());
					}
					if (null == sellerItems.get(position).door || 0 == sellerItems.get(position).door.size()) {

					} else {

						intent.putExtra("in_Price", vh.in_Price.getText());
						intent.putExtra("carful_Price", vh.carful_Price.getText());
					}
					context.startActivity(intent);
				}
			});
		}
		if (null == sellerItems.get(position).margin || "0.00".equals(sellerItems.get(position).margin)) {
			vh.seller_Cmp_balance.setVisibility(View.GONE);
		}

		vh.standLayout.setVisibility(View.GONE);
		vh.inLayout.setVisibility(View.GONE);
		vh.outLayout.setVisibility(View.GONE);
		vh.carfulLayout.setVisibility(View.GONE);

		return convertView;
	}

	static class ViewHolder {
		TextView seller_Cmp_Name;
		LinearLayout seller_Cmp_balance;
		RatingBar seller_RatingBar;
		TextView seller_Rating_Grade;
		TextView custom_Option_Num;
		ImageView seller_Icon;
		TextView seller_Address;
		TextView seller_Sucess;
		// ImageView distance_Icon;
		TextView seler_Distance;
		TextView in_Price;
		TextView out_Price;
		TextView standar_Price;
		TextView carful_Price;
		TextView inPriceBt;
		TextView outPriceBt;
		TextView standPriceBt;
		TextView carfulPriceBt;
		RelativeLayout standLayout, outLayout, inLayout, carfulLayout;
		TextView balanceMoney;
		LinearLayout sellerClick1;
		LinearLayout sellerClick2;
		ImageView distance_icon;
		NoScrollListView mNoListView;
	}

}
