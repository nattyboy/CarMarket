package com.example.aftermarket.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.ToRefundListAdapter;
import com.example.aftermarket.adpter.ToServerListAdapter;
import com.example.aftermarket.bean.Back;
import com.example.aftermarket.bean.BackData;
import com.example.aftermarket.bean.Order;
import com.example.aftermarket.bean.OrderItem;
import com.example.aftermarket.bean.OrderItemR;
import com.example.aftermarket.bean.OrderR;
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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;

public class RefundFragment extends Fragment {
	private static final String REQUEST_HEADER = "orderList";
	private static final String TOSERVERID = "4";
	private ListView CarToServerList;
	private Map<String, Object> carToServer = new HashMap<>();
	private Activity orderItemActivity;
	private List<Map<String, Object>> carToServerList = new ArrayList<>();
	private DemoApplication app;
	private OrderR order;
	private List<OrderItemR> orderList;
	private LinearLayout noneItems;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		orderItemActivity = getActivity();
		app = (DemoApplication) orderItemActivity.getApplication();
		View view = inflater.inflate(R.layout.car_to_server_fragment_layout, container, false);
		CarToServerList = (ListView) view.findViewById(R.id.car_to_server_listview);
		noneItems=(LinearLayout) view.findViewById(R.id.none_layout);
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
		// params.addBodyParameter("page", "1");
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
					java.lang.reflect.Type type = new TypeToken<OrderR>() {
					}.getType();
					order = gson.fromJson(result, type);
					orderList = order.data;
					if (null != orderList) {
						if(orderList.size()>0){
							noneItems.setVisibility(View.GONE);
						}else{
							noneItems.setVisibility(View.VISIBLE);
						}
						ToRefundListAdapter toServerListAdapter = new ToRefundListAdapter(orderItemActivity, orderList);
						CarToServerList.setAdapter(toServerListAdapter);
						Log.e("dajia", orderList.toString());
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
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
}
