package com.example.aftermarket.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.OrderEvaluteAdapter;
import com.example.aftermarket.bean.Business;
import com.example.aftermarket.bean.EvaluteDataItem;
import com.example.aftermarket.bean.OrderEvaluteData;
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
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class EvaluteOrderActivity extends Activity implements IXListViewListener {
	private static final String REQUEST_HEADER = "evaluationList";
	private DemoApplication app;
	private LinearLayout evaluteProgress;
	private XListView evaluteListView;
	private OrderEvaluteData orderEvaluteData;
	private ArrayList<EvaluteDataItem> evaluteDataItemList;
	private OrderEvaluteAdapter adapter;
	private Business business;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;

	public Business getBusiness() {
		return business;
	}

	public void setBusiness(Business business) {
		this.business = business;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		app = (DemoApplication) getApplication();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_evalute_activity);
		evaluteProgress = (LinearLayout) findViewById(R.id.evalute_progress);
		evaluteListView = (XListView) findViewById(R.id.order_evalute_listview);
		evaluteListView.setXListViewListener(this);
		evaluteListView.setPullLoadEnable(false);
		mHandler = new Handler();
		business = (Business) getIntent().getSerializableExtra("business");
		loadData();
	}
	public String getcmpLog(){
		if(null!=business){
			return business.data.company_logo;
		}else{
			return null;
		}
	}

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("merchant_id", business.data.merchant_id);
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(EvaluteOrderActivity.this, "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				evaluteProgress.setVisibility(View.GONE);
				evaluteListView.setVisibility(View.VISIBLE);
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<OrderEvaluteData>() {
					}.getType();
					orderEvaluteData = gson.fromJson(result, type);
					evaluteDataItemList = (ArrayList<EvaluteDataItem>) orderEvaluteData.data;
					if (null != evaluteDataItemList) {
						adapter = new OrderEvaluteAdapter(EvaluteOrderActivity.this, evaluteDataItemList);
						evaluteListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					} else {
						evaluteListView.setAdapter(null);
					}
					Log.e("hello", "+++result++" + evaluteDataItemList);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(EvaluteOrderActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(EvaluteOrderActivity.this, ShopLoginActivity.class);
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

	public void backToLastPage(View view) {
		finish();
	}

	private void onLoad() {
		evaluteListView.stopRefresh();
		evaluteListView.stopLoadMore();
		evaluteListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
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
		evaluteListView.stopLoadMore();
	}

}
