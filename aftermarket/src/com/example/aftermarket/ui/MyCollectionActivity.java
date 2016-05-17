package com.example.aftermarket.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.MyCollectionListAdapter;
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
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MyCollectionActivity extends Activity implements IXListViewListener {
	private XListView myCollcetionListView;
	private DemoApplication app;
	private final String REQUEST_HEADER = "collectList";
	private Seller seller;
	private ArrayList<SellerInfo> sellerInfoList = new ArrayList();
	private ArrayList<Seller> sellerItems = new ArrayList();
	private SellerListAdapter adpter;
	private LinearLayout collcetionProgress;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	private String sellerRegion;
	private String sellerLat;
	private String sellerLng;
	private String sellerType = "1";
	LocationManager loctionManager;
	String contextService = Context.LOCATION_SERVICE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_collection_activity);
		myCollcetionListView = (XListView) findViewById(R.id.my_collection_listview);
		myCollcetionListView.setXListViewListener(this);
		myCollcetionListView.setPullLoadEnable(false);
		mHandler = new Handler();
		collcetionProgress = (LinearLayout) findViewById(R.id.my_colletion_progress);
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
	}

	public void backToMyCenter(View view) {

		finish();
	}

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("region", sellerRegion);
		params.addBodyParameter("lat", app.getmCurrentLantitude());
		params.addBodyParameter("lng", app.getmCurrentLongitude());
		params.addBodyParameter("type", sellerType);
		params.addBodyParameter("token", app.getToken());
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				myCollcetionListView.setVisibility(View.VISIBLE);
				collcetionProgress.setVisibility(View.GONE);
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
						adpter = new SellerListAdapter(MyCollectionActivity.this, sellerInfoList);
						myCollcetionListView.setAdapter(adpter);
						adpter.notifyDataSetChanged();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(MyCollectionActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(MyCollectionActivity.this, ShopLoginActivity.class);
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

	private void onLoad() {
		myCollcetionListView.stopRefresh();
		myCollcetionListView.stopLoadMore();
		myCollcetionListView.setRefreshTime("刚刚");
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
		myCollcetionListView.stopLoadMore();
	}

}
