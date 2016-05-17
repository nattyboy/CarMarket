package com.example.aftermarket.ui;

import java.util.ArrayList;
import java.util.HashMap;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelfWashCarSeller extends Activity implements IXListViewListener{

	private static final String REQUEST_HEADER = "selfWashList";
	private TextView areaText;
	private TextView distanceText;
	private TextView optionText;
	private PopupWindow mPopWin;
	private LinearLayout layout;
	private ListView rootList;
	private String title[] = { "包河区", "瑶海区", "庐阳区", "蜀山区", "滨湖新区", "经开区", "高新区", "新站区" };
	private ArrayList<HashMap<String, Object>> itemList;
	private LinearLayout linLayout;
	private XListView sellListView;
	private Seller seller;
	private ArrayList<SellerInfo> sellerInfoList = new ArrayList();
	private ArrayList<Seller> sellerItems = new ArrayList();
	private SellerListAdapter adpter;
	private LinearLayout layoutBar;
	private ImageView topBack;
	private ImageView onlineTel;
	private TextView sellerTitle;
	private LinearLayout selfWashProgress;
	private Handler mHandler;
	private String sellerLat;
	private String sellerLng;
	LocationManager loctionManager;
	String contextService = Context.LOCATION_SERVICE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seller_info_activity);
		sellListView = (XListView) findViewById(R.id.seller_listview);
		sellListView.setXListViewListener(this);
		sellListView.setPullLoadEnable(false);
		mHandler = new Handler();
		selfWashProgress=(LinearLayout) findViewById(R.id.sellerfragment_progress);
		layoutBar = (LinearLayout) findViewById(R.id.layout_bar);
		layoutBar.setVisibility(View.GONE);
		topBack = (ImageView) findViewById(R.id.self_wash_back);
		topBack.setVisibility(View.VISIBLE);
		onlineTel = (ImageView) findViewById(R.id.online_tel);
		sellerTitle = (TextView) findViewById(R.id.tv_seller_title);
		sellerTitle.setText("自助洗车");
		onlineTel.setVisibility(View.GONE);
		// initPopupWindow();
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
		sellListView.setEnabled(false);
		sellListView.setClickable(false);
		sellListView.setItemsCanFocus(false);
		sellListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*Intent intent = new Intent(SelfWashCarSeller.this, SellerInfoActivity.class);
				startActivity(intent);*/
			}
		});
		topBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				finish();
			}
		});
	}

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		// String url =
		// "http://jk1.cpioc.com/index.php?m=Admin&c=userApi&a=merchantList";
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		/*params.addBodyParameter("region", sellerRegion);
		params.addBodyParameter("lat", sellerLat);
		params.addBodyParameter("lng", sellerLng);
		params.addBodyParameter("type", sellerType);
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("type", "2");*/
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				sellListView.setVisibility(View.VISIBLE);
				selfWashProgress.setVisibility(View.GONE);
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
						adpter = new SellerListAdapter(SelfWashCarSeller.this, sellerInfoList);
						sellListView.setAdapter(adpter);
						adpter.notifyDataSetChanged();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(SelfWashCarSeller.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SelfWashCarSeller.this, ShopLoginActivity.class);
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
		layout = (LinearLayout) LayoutInflater.from(SelfWashCarSeller.this).inflate(R.layout.popup_category, null);
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for (int i = 0; i < title.length; i++) {
			HashMap<String, Object> items = new HashMap<String, Object>();
			items.put("name", title[i]);
			items.put("count", i * 5);
			itemList.add(items);
		}

		CategoryListAdapter cla = new CategoryListAdapter(SelfWashCarSeller.this, itemList);
		rootList.setAdapter(cla);
		mPopWin = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.showAsDropDown(areaText, 5, 1);
		mPopWin.update();
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Toast.makeText(SelfWashCarSeller.this, "" + position, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void showEreaPopwin() {

		layout = (LinearLayout) LayoutInflater.from(SelfWashCarSeller.this).inflate(R.layout.popup_category, null);
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

		CategoryListAdapter cla = new CategoryListAdapter(SelfWashCarSeller.this, itemList);
		rootList.setAdapter(cla);
		mPopWin.update();

	}
	private void onLoad() {
		sellListView.stopRefresh();
		sellListView.stopLoadMore();
		sellListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				loadData();
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		sellListView.stopLoadMore();
	}

}
