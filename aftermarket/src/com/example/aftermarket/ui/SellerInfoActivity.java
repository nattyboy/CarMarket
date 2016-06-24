package com.example.aftermarket.ui;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chatuidemo.activity.ChatActivity;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.PromoteDetailAdapter;
import com.example.aftermarket.adpter.SellerListAdapter;
import com.example.aftermarket.adpter.SquareAdapterGridItem;
import com.example.aftermarket.bean.Business;
import com.example.aftermarket.bean.EaseMobData;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.views.NoScrollGridView;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SellerInfoActivity extends Activity {
	private static final String REQUEST_HEADER = "merchantDetail";
	private static final String REQUEST_HEADER_COLLECTADD = "collectAdd";
	private static final String REQUEST_HEADER_UNCOLLECTADD = "collectDel";
	private static final String REQUEST_HEADER_GETUSERIMG = "getUserImg";
	private Business business;
	private ImageView seller_Bg_Img;
	private TextView Seller_name;
	private TextView ex_Time;
	private TextView seller_Ins_Desc;
	private TextView tel_Address;
	private TextView seller_Distance_Tv;
	private TextView cus_Opinion;
	private RatingBar ratingBar_seller;
	private TextView cus_Star_Num;
	private Button onlineaskbt;
	private ProgressBar progressBar_seller;
	private LinearLayout seller_Detail_Lt;
	private String easemobUser = null;
	private String merchant_id="";
	private int is_collect;
	private ImageView collectImg;
	private DemoApplication app;
	private LinearLayout sellInfoProgress;
	private RelativeLayout sellInfoContent;
	private TextView balanceTv;
	private RelativeLayout evaluteLayoutClick;
	private Seller seller;
	private ArrayList<SellerInfo> sellerInfoList = new ArrayList();
	private ArrayList<String> collectSellers;
	String urlCollect;
	private String inPrice, outPrice, standPrice, carfulPrice;
	private TextView inTv, outTv, standTv, carfulTv;
	private TextView inBt, outBt, standBt, carfulBt;
	private RelativeLayout inLayout, outLayout, standLayout, carfulLayout;
	private String cmp_name;
	private EaseMobData easeMobData;
	private String store_img;
	private String balance;
	NoScrollGridView gridViewName;
	SquareAdapterGridItem adapterName;
	private NoScrollListView listView_promote_detail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// loadData();
		setContentView(R.layout.seller_infomation);
		listView_promote_detail=(NoScrollListView) findViewById(R.id.listView_promote_detail);
		inTv = (TextView) findViewById(R.id.in_price_info);
		carfulTv = (TextView) findViewById(R.id.carful_price_info);
		outTv = (TextView) findViewById(R.id.out_price_info);
		standTv = (TextView) findViewById(R.id.stand_price_info);
		carfulBt = (TextView) findViewById(R.id.carful_price_bt_info);
		inBt = (TextView) findViewById(R.id.in_price_bt_info);
		outBt = (TextView) findViewById(R.id.out_price_bt_info);
		standBt = (TextView) findViewById(R.id.stand_price_bt_info);
		carfulLayout = (RelativeLayout) findViewById(R.id.carful_layout_info);
		inLayout = (RelativeLayout) findViewById(R.id.in_layout_info);
		outLayout = (RelativeLayout) findViewById(R.id.out_layout_info);
		standLayout = (RelativeLayout) findViewById(R.id.stand_layout_info);
		gridViewName = (NoScrollGridView) findViewById(R.id.gridView_seller_info_item);
		adapterName = new SquareAdapterGridItem(this, 0);
		gridViewName.setAdapter(adapterName);
		app = (DemoApplication) getApplication();
		Intent intent = getIntent();
		cmp_name = intent.getStringExtra("cmp_name");
		merchant_id = intent.getStringExtra("merchant_id");
		carfulPrice = intent.getStringExtra("carful_Price");
		inPrice = intent.getStringExtra("in_Price");
		outPrice = intent.getStringExtra("out_Price");
		standPrice = intent.getStringExtra("standar_Price");

		balance = intent.getStringExtra("balance");
		// is_collect = intent.getIntExtra("is_collect", 0);
		easemobUser = intent.getStringExtra("easemob_user");
		seller_Detail_Lt = (LinearLayout) findViewById(R.id.seller_detail);
		onlineaskbt = (Button) findViewById(R.id.onlineaskbt1);
		progressBar_seller = (ProgressBar) findViewById(R.id.progressBar_seller);
		collectImg = (ImageView) findViewById(R.id.forwrad_img);
		sellInfoProgress = (LinearLayout) findViewById(R.id.sellerinfo_progress);
		sellInfoContent = (RelativeLayout) findViewById(R.id.title_seller);
		balanceTv = (TextView) findViewById(R.id.textView_bao_info);
		evaluteLayoutClick = (RelativeLayout) findViewById(R.id.evalute_layout_click);
		onlineaskbt.setEnabled(false);
		if (null != carfulPrice) {
			carfulTv.setText(carfulPrice);
			carfulBt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (null != app.getToken()) {
						Intent intent = new Intent(SellerInfoActivity.this, InputOrderActivity.class);
						intent.putExtra("wash_Price", carfulPrice);
						intent.putExtra("wash_Name", "车内清洁");
						intent.putExtra("cmp_name", cmp_name);
						intent.putExtra("merchant_id", merchant_id);
						intent.putExtra("balance", balance);
						startActivity(intent);
					} else {
						Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					}
				}
			});
		} else {

			carfulLayout.setVisibility(View.GONE);
		}
		if (null != inPrice) {
			inTv.setText(inPrice);
			inBt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (null != app.getToken()) {
						Intent intent = new Intent(SellerInfoActivity.this, InputOrderActivity.class);
						intent.putExtra("wash_Price", inPrice);
						intent.putExtra("wash_Name", "车内清洁");
						intent.putExtra("cmp_name", cmp_name);
						intent.putExtra("merchant_id", merchant_id);
						intent.putExtra("balance", balance);
						startActivity(intent);
					} else {
						Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					}
				}
			});
		} else {

			inLayout.setVisibility(View.GONE);
		}
		if (null != outPrice) {
			outTv.setText(outPrice);
			outBt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (null != app.getToken()) {
						Intent intent = new Intent(SellerInfoActivity.this, InputOrderActivity.class);
						intent.putExtra("wash_Price", outPrice);
						intent.putExtra("wash_Name", "车外清洁");
						intent.putExtra("cmp_name", cmp_name);
						intent.putExtra("merchant_id", merchant_id);
						intent.putExtra("balance", balance);
						startActivity(intent);
					} else {
						Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					}

				}
			});
		} else {

			outLayout.setVisibility(View.GONE);
		}
		if (null != standPrice) {

			standTv.setText(standPrice);
			standBt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (null != app.getToken()) {

						Intent intent = new Intent(SellerInfoActivity.this, InputOrderActivity.class);
						intent.putExtra("wash_Price", standPrice);
						intent.putExtra("wash_Name", "标准清洁");
						intent.putExtra("cmp_name", cmp_name);
						intent.putExtra("merchant_id", merchant_id);
						intent.putExtra("balance", balance);
						startActivity(intent);
					} else {
						Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					}

				}
			});

		} else {

			standLayout.setVisibility(View.GONE);
		}

		evaluteLayoutClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(SellerInfoActivity.this, EvaluteOrderActivity.class);
				intent.putExtra("merchant_id", merchant_id);
				intent.putExtra("easemobUser", easemobUser);
				intent.putExtra("business", business);
				startActivity(intent);
			}
		});
		if (null != app.getToken()) {
			loadCollectionsData();
		} else {
			loadData();
		}

		carfulLayout.setVisibility(View.GONE);
		standLayout.setVisibility(View.GONE);
		outLayout.setVisibility(View.GONE);
		inLayout.setVisibility(View.GONE);

		if (null != easemobUser) {
			loadDataImg(easemobUser);
		}
		carfulLayout.setVisibility(View.GONE);
		inLayout.setVisibility(View.GONE);
		outLayout.setVisibility(View.GONE);
		standLayout.setVisibility(View.GONE);

	}

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("lat", app.getmCurrentLantitude());
		params.addBodyParameter("lng", app.getmCurrentLongitude());
		params.addBodyParameter("merchant_id", merchant_id);
		params.addBodyParameter("token", app.getToken());
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.e("store_img", ""+4444444);
				SystemClock.sleep(1000);
				onlineaskbt.setVisibility(View.VISIBLE);
				sellInfoProgress.setVisibility(View.GONE);
				seller_Detail_Lt.setVisibility(View.VISIBLE);
				seller_Bg_Img = (ImageView) findViewById(R.id.seller_bg_img);
				Seller_name = (TextView) findViewById(R.id.seller_name);
				ex_Time = (TextView) findViewById(R.id.ex_time);
				seller_Ins_Desc = (TextView) findViewById(R.id.seller_ins_desc);
				tel_Address = (TextView) findViewById(R.id.tel_address);
				seller_Distance_Tv = (TextView) findViewById(R.id.seller_distance_tv);
				cus_Opinion = (TextView) findViewById(R.id.cus_opinion);
				ratingBar_seller = (RatingBar) findViewById(R.id.ratingBar_seller_info);
				cus_Star_Num = (TextView) findViewById(R.id.cus_pingjia);
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Business>() {
					}.getType();
					business = gson.fromJson(result, type);
					
					if (null != business.data) {
						
						
						
						int maxMemory = (int) Runtime.getRuntime().maxMemory();
						int cacheSize = maxMemory / 8;
						String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/x-utils";
						BitmapUtils bitmapUtils = new BitmapUtils(SellerInfoActivity.this, path, cacheSize);
						WindowManager wm = SellerInfoActivity.this.getWindowManager();
						int width = wm.getDefaultDisplay().getWidth();
						bitmapUtils.configDefaultBitmapMaxSize(width, width);
						bitmapUtils.display(seller_Bg_Img, business.data.store_img);
						Seller_name.setText("商家名称:" + business.data.company_name);
						ex_Time.setText(business.data.on_time);
						seller_Ins_Desc.setText(business.data.company_desc);
						tel_Address.setText("电话: " + business.data.tel + "\n" + "\n" + "地址:" + business.data.address);
						BigDecimal distanceValu = new BigDecimal(business.data.distance);
						BigDecimal finalValue = distanceValu.divide(new BigDecimal(1000), 1, BigDecimal.ROUND_HALF_UP);
						seller_Distance_Tv.setText("距离：< " + finalValue + " km");
						BigDecimal b = new BigDecimal(15000);
						if (finalValue.compareTo(b) >= 0) {

							seller_Distance_Tv.setText("距离：" + "未知");
						}
						cus_Opinion.setText("客服评价" + "(" + business.data.evaluation_num + "人评价)");
						ratingBar_seller.setRating(business.data.score);
						cus_Star_Num.setText(business.data.score + "分");
						balanceTv.setText(business.data.margin);
						is_collect = business.data.is_collect;
						if (returnCollect().contains(merchant_id)) {
							collectImg.setImageResource(R.drawable.shouchang_click);
						} else {
							collectImg.setImageResource(R.drawable.shouchang);
						}

						if (null != business.data.business) {

							for (int i = 0; i < business.data.business.size(); i++) {

								if (null != business.data.business.get(i).dim_desc) {
									adapterName.addData(business.data.business.get(i).dim_desc);
								}

							}

						}
						listView_promote_detail.setAdapter(new PromoteDetailAdapter(SellerInfoActivity.this, business.data.promote,business.data));

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

						onlineaskbt.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (null != app.getToken()) {
									Intent intent = new Intent();
									intent.putExtra("userId", easemobUser);
									intent.putExtra("userNick", easemobUser);
									intent.putExtra("name_chat", business.data.company_name);
									intent.putExtra("store_img", store_img);
									Log.e("dajiayilian", "执行到这里了onlineAsk");
									Log.e("oye", business.data.store_img);
									intent.setClass(SellerInfoActivity.this, ChatActivity.class);
									intent.putExtra("cmp_name", business.data.company_name);
									intent.putExtra("merchant_id", business.data.merchant_id);
									intent.putExtra("balance", business.data.margin);
									startActivity(intent);
									SharedPreferences mSharedPreferences = getSharedPreferences(easemobUser, Context.MODE_PRIVATE);
									Editor editor = mSharedPreferences.edit();
									editor.putString("cmp_name", business.data.company_name);
									editor.putString("merchant_id", business.data.merchant_id);
									editor.putString("balance", business.data.margin);
									editor.putString("store_img", store_img);
									Log.e("dajiayilian", "store_img" + business.data.store_img);
									editor.commit();
								} else {
									Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
									startActivity(intent);
								}

							}
						});

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
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

	public void backToSellerFrg(View v) {
		Intent intent = new Intent(SellerInfoActivity.this, HomeActivity.class);
		intent.putExtra(ConstantClass.SELLERINFO_TOKEN_NAME, ConstantClass.SELLERINFO_TOKEN_VALUE);
		startActivity(intent);
	}

	public void collectClick(View view) {

		if (null == app.getToken()) {

			Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
			startActivity(intent);
		}

		if (!collectSellers.contains(merchant_id)) {
			collectImg.setImageResource(R.drawable.shouchang_click);
			urlCollect = ConstantClass.NET_URL + REQUEST_HEADER_COLLECTADD;
			collectSellers.add(merchant_id);
			Log.e("dajiayilian", "" + collectSellers);
		} else {
			collectImg.setImageResource(R.drawable.shouchang);
			urlCollect = ConstantClass.NET_URL + REQUEST_HEADER_UNCOLLECTADD;
			collectSellers.remove(merchant_id);
			Log.e("dajiayilian", "" + collectSellers);
		}
		HttpUtils httpUtils = new HttpUtils();

		final RequestParams params = new RequestParams();
		params.addBodyParameter("object_id", merchant_id);
		params.addBodyParameter("token", app.getToken());
		httpUtils.send(HttpMethod.POST, urlCollect, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				Log.e("jiekou", urlCollect);
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Business>() {
					}.getType();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						if (urlCollect.equals(ConstantClass.NET_URL + REQUEST_HEADER_COLLECTADD)) {
							Toast.makeText(SellerInfoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(SellerInfoActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
						}

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {
						Log.e("hello", "+++++" + urlCollect);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	private void loadCollectionsData() {
		HttpUtils httpUtils = new HttpUtils();
		// String url =
		// "http://jk1.cpioc.com/index.php?m=Admin&c=userApi&a=merchantList";
		String url = ConstantClass.NET_URL + "collectList";
		RequestParams params = new RequestParams();
		// params.addBodyParameter("region", sellerRegion);
		// params.addBodyParameter("lat", sellerLat);
		// params.addBodyParameter("lng", sellerLng);
		// params.addBodyParameter("type", sellerType);
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
					java.lang.reflect.Type type = new TypeToken<Seller>() {
					}.getType();
					seller = gson.fromJson(result, type);
					if (null != seller.data) {
						sellerInfoList = (ArrayList<SellerInfo>) seller.data;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

						loadData();
						collectSellers = returnCollect();

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
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

	public ArrayList<String> returnCollect() {
		ArrayList<String> collctList = new ArrayList<>();
		if (null != sellerInfoList) {
			for (int i = 0; i < sellerInfoList.size(); i++) {

				collctList.add(sellerInfoList.get(i).merchant_id);

			}
		}

		return collctList;
	}

	private void loadDataImg(String user_name) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_GETUSERIMG;
		RequestParams params = new RequestParams();

		params.addBodyParameter("user", user_name);
		Log.e("hello", "++user_name+++" + user_name);
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(SellerInfoActivity.this, "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<EaseMobData>() {
					}.getType();
					easeMobData = gson.fromJson(result, type);
					store_img = easeMobData.data.get(0).img;
					onlineaskbt.setEnabled(true);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(SellerInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SellerInfoActivity.this, ShopLoginActivity.class);
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
}
