package com.example.aftermarket.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.ReceiptAddressListAdapter;
import com.example.aftermarket.adpter.SellerListAdapter;
import com.example.aftermarket.bean.Address;
import com.example.aftermarket.bean.AddressInfo;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiptAddressActivity extends Activity implements IXListViewListener {

	private static final String REQUEST_HEADER = "addressList";
	private XListView addNewAddrListView;
	private Map<String, Object> addressMap = new HashMap<>();
	private List<AddressInfo> addressList = new ArrayList<>();
	private ReceiptAddressListAdapter receiptAddressListAdapter;
	private TextView addNewAddr;
	private DemoApplication app;
	private Handler mHandler;
	private Address address;
	private ArrayList<Boolean> defaultAddrList=new ArrayList<>();
	private String tempChooseAddressString=null;
	private LinearLayout nonelayout;
	private String sellerRegion;
	private String sellerLat;
	private String sellerLng;
	private String sellerType = "1";
	LocationManager loctionManager;
	String contextService = Context.LOCATION_SERVICE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		app = (DemoApplication) getApplication();
		setContentView(R.layout.receipt_address_activity);
		nonelayout=(LinearLayout) findViewById(R.id.none_addr_layout);
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
		Intent intent=getIntent();
		tempChooseAddressString=intent.getStringExtra("choose_address");
		addNewAddr = (TextView) findViewById(R.id.add_new_addr);
		addNewAddrListView = (XListView) findViewById(R.id.add_new_addr_listView);
		addNewAddrListView.setXListViewListener(this);
		addNewAddrListView.setPullLoadEnable(false);
		mHandler = new Handler();
		/*
		 * addressList.add("hello"); addressList.add("hello");
		 */
		//addNewAddrListView.setAdapter(new ReceiptAddressListAdapter(ReceiptAddressActivity.this, addressList,defaultAddrList));
		
		Log.e("dajiayilian", "defaultAddrList"+defaultAddrList);
		addNewAddr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(ReceiptAddressActivity.this, AddReceiptAddrActivity.class);
				startActivity(intent);

			}
		});
		
		addNewAddrListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(null !=tempChooseAddressString){
					TextView tv = (TextView) view.findViewById(R.id.addr_tv_textview);
					TextView tvName = (TextView) view.findViewById(R.id.receipt_name);
					TextView tvTel = (TextView) view.findViewById(R.id.receipt_tel);
					Intent intent = new Intent(ReceiptAddressActivity.this,InputOrderActivity.class);
					if(null!=tv.getText()){
						intent.putExtra("user_address", tv.getText().toString());
					}
					if(null!=tvName.getText()){
						intent.putExtra("user_name", tvName.getText().toString());
						Log.e("user_address", "user_address"+tvName.getText().toString());
					}
					if(null!=tvTel.getText()){
						intent.putExtra("user_tel", tvTel.getText().toString());
					}
					if(position-1>=0)
					intent.putExtra("address_id", addressList.get(position-1).address_id);				
					startActivity(intent);
					finish();
				}
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loadData();
		addNewAddr = (TextView) findViewById(R.id.add_new_addr);
		addNewAddrListView = (XListView) findViewById(R.id.add_new_addr_listView);
		addNewAddrListView.setAdapter(new ReceiptAddressListAdapter(ReceiptAddressActivity.this, addressList,defaultAddrList));
		Log.e("dajiayilian", "defaultonResumeAddrList"+defaultAddrList);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		defaultAddrList.clear();
		Log.e("dajiayilian", "defaultAdonStopdrList"+defaultAddrList);
	}

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url=ConstantClass.NET_URL+REQUEST_HEADER;
		RequestParams params = new RequestParams();
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
					java.lang.reflect.Type type = new TypeToken<Address>() {}.getType();
					address = gson.fromJson(result, type);
					if(null!=address.data){
						
						addressList=address.data;
						if(addressList.size()>0){
							nonelayout.setVisibility(View.GONE);
						}else{
							nonelayout.setVisibility(View.VISIBLE);
						}
						Log.e("ReceiptAddressActivity", "ReceiptAddressActivity"+addressList);
						for (int i = 0; i <addressList.size(); i++) {
							
							if(0==addressList.get(i).is_default){
								defaultAddrList.add(false);
							}else{
								defaultAddrList.add(true);
							}
							
						}
						ReceiptAddressListAdapter receiptAddressListAdapter=new ReceiptAddressListAdapter(ReceiptAddressActivity.this, addressList,defaultAddrList);
						addNewAddrListView.setAdapter(receiptAddressListAdapter);
					}else{
						nonelayout.setVisibility(View.VISIBLE);
					}
					

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(ReceiptAddressActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(ReceiptAddressActivity.this, ShopLoginActivity.class);
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
	public void backToMyCenter (View v){
		finish();
	}
	private void onLoad() {
		addNewAddrListView.stopRefresh();
		addNewAddrListView.stopLoadMore();
		addNewAddrListView.setRefreshTime("刚刚");
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
		addNewAddrListView.stopLoadMore();
	}
	
	public void ImgVisible(){
		
		nonelayout.setVisibility(View.VISIBLE);
	}
	public void ImgGoen(){
		nonelayout.setVisibility(View.GONE);
	}
}
