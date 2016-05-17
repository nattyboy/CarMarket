package com.example.aftermarket.ui;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.CategoryListAdapter;
import com.example.aftermarket.adpter.SellerListAdapter;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.config.ConstantClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class ServiceSellerActivity extends Activity {
	private static final String REQUEST_HEADER = "merchantList";
	private TextView areaText;
	private TextView distanceText;
	private TextView optionText;
	private PopupWindow mPopWin;
	private LinearLayout layout;
	private ListView rootList;
	private ListView childList;
	private FrameLayout flChild;
	private String title[] = { "包河区", "瑶海区", "庐阳区", "蜀山区", "滨湖新区", "经开区", "高新区", "新站区" };
	private ArrayList<HashMap<String, Object>> itemList;
	private LinearLayout linLayout;
	private ListView lv_shangjia;
	private ListView sellListView;
	private DemoApplication app;
	private Seller seller;
	private ArrayList<SellerInfo> sellerInfoList = new ArrayList();
	private ArrayList<Seller> sellerItems = new ArrayList();
	private SellerListAdapter adpter;
	private String sellerRegion;
	//private String sellerLat;
	//private String sellerLng;
	private String sellerLat = "31.8371046436";
	 private String sellerLng = "117.2688041888";
	private String sellerType = "1";
	LocationManager loctionManager;
	String contextService = Context.LOCATION_SERVICE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seller_info_activity);
		app=(DemoApplication) getApplication();
		sellListView = (ListView) findViewById(R.id.seller_listview);
		initPopupWindow();
		loctionManager = (LocationManager) getSystemService(contextService);
		String provider = LocationManager.GPS_PROVIDER;
		Location location = loctionManager.getLastKnownLocation(provider);
		if (null == location) {

		} else {
			sellerLat = String.valueOf(location.getLatitude());
			sellerLng = String.valueOf(location.getLatitude());
			Log.e("dajia", "location" + location);
		}
		loadData();
		sellListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(ServiceSellerActivity.this, SellerInfoActivity.class);
				startActivity(intent);
			}
		});
	}

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("region", sellerRegion);
		params.addBodyParameter("lat", sellerLat);
		params.addBodyParameter("lng", sellerLng);
		params.addBodyParameter("type", sellerType);
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("type", "1");
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
					int a = seller.data.size();
					Log.e("dajiyilian", "" + a);

					sellerInfoList = (ArrayList<SellerInfo>) seller.data;
					Log.e("dajiyilian", "sellerInfoList" + sellerInfoList.size());
					adpter = new SellerListAdapter(ServiceSellerActivity.this, sellerInfoList);
					sellListView.setAdapter(adpter);
					adpter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(ServiceSellerActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(ServiceSellerActivity.this, ShopLoginActivity.class);
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

	private void initPopupWindow() {
		itemList = new ArrayList<HashMap<String, Object>>();
		areaText = (TextView) findViewById(R.id.allArea);
		distanceText = (TextView) findViewById(R.id.distance);
		optionText = (TextView) findViewById(R.id.opinion);
		linLayout = (LinearLayout) findViewById(R.id.second);

		areaText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopupWindow(linLayout.getWidth(), linLayout.getHeight());
			}
		});

		distanceText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopupWindow(linLayout.getWidth(), linLayout.getHeight());
			}
		});

		optionText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// showPopupWindow(linLayout.getWidth(), linLayout.getHeight());
				showEreaPopwin();
			}
		});
	}

	private void showPopupWindow(int width, int height) {

		itemList = new ArrayList<HashMap<String, Object>>();
		layout = (LinearLayout) LayoutInflater.from(ServiceSellerActivity.this).inflate(R.layout.popup_category, null);
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for (int i = 0; i < title.length; i++) {
			HashMap<String, Object> items = new HashMap<String, Object>();
			items.put("name", title[i]);
			items.put("count", i * 5);
			itemList.add(items);
		}

		CategoryListAdapter cla = new CategoryListAdapter(ServiceSellerActivity.this, itemList);
		rootList.setAdapter(cla);
		mPopWin = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.showAsDropDown(areaText, 5, 1);
		mPopWin.update();
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Toast.makeText(ServiceSellerActivity.this, "" + position, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void showEreaPopwin() {

		layout = (LinearLayout) LayoutInflater.from(ServiceSellerActivity.this).inflate(R.layout.popup_category, null);
		mPopWin = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPopWin.showAsDropDown(areaText, 5, 1);
		itemList = new ArrayList<HashMap<String, Object>>();
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for (int i = 0; i < 3; i++) {
			HashMap<String, Object> items = new HashMap<String, Object>();
			items.put("name", title[i]);
			items.put("count", i * 5);
			itemList.add(items);
		}

		CategoryListAdapter cla = new CategoryListAdapter(ServiceSellerActivity.this, itemList);
		rootList.setAdapter(cla);
		mPopWin.update();

	}

}

/*
 * private void showPopupWindow(View parent) { if (popWindow == null) {
 * LayoutInflater layoutInflater =
 * (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 * 
 * View view = layoutInflater.inflate(R.layout.popwindow_layout, null); dm = new
 * DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(dm); //
 * 创建一个PopuWidow对象 popWindow = new PopupWindow(view, dm.widthPixels,
 * LinearLayout.LayoutParams.WRAP_CONTENT); } // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
 * popWindow.setFocusable(true); // 设置允许在外点击消失
 * popWindow.setOutsideTouchable(true); //
 * //设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景 popWindow.setBackgroundDrawable(new
 * BitmapDrawable()); // PopupWindow的显示及位置设置 // popWindow.showAtLocation(parent,
 * Gravity.FILL, 0, 0); popWindow.showAsDropDown(parent, 0,0);
 * 
 * popWindow.setOnDismissListener(new OnDismissListener() {
 * 
 * @Override public void onDismiss() { // 改变显示的按钮图片为正常状态 changeButtonImage(); }
 * });
 * 
 * // 监听触屏事件 popWindow.setTouchInterceptor(new OnTouchListener() { public
 * boolean onTouch(View view, MotionEvent event) { // 改变显示的按钮图片为正常状态
 * changeButtonImage(); popWindow.dismiss(); return false; } });}
 */
