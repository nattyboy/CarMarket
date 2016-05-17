package com.example.aftermarket.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.Address;
import com.example.aftermarket.bean.AddressPicker;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddReceiptAddrActivity extends Activity{
	private static final String REQUEST_HEADER = "addressAdd";
	private RelativeLayout selectAreaLayout;
	private TextView selectAreaTv;
	private DemoApplication app;
	private EditText userName;
	private EditText mobileNum;
	private EditText addrDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app=(DemoApplication) getApplication();
		setContentView(R.layout.add_receipt_addr_activity);
		selectAreaLayout=(RelativeLayout) findViewById(R.id.select_province_layout_add);
		selectAreaTv=(TextView) findViewById(R.id.select_area_tv_add);
		userName=(EditText) findViewById(R.id.user_name_et);
		mobileNum=(EditText) findViewById(R.id.mobile_num_et);
		addrDetail=(EditText) findViewById(R.id.address_detail);
		selectAreaLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				AddressPicker addressPicker = new AddressPicker();
				addressPicker.selectAddressDialog(AddReceiptAddrActivity.this,selectAreaTv);
			}
		});
		
		
	}
	
	private void loadData() {
		
		
		if(null==userName.getText()){
			Toast.makeText(AddReceiptAddrActivity.this, "请输入收货人姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(userName.getText())) {
			Toast.makeText(AddReceiptAddrActivity.this, "请输入收货人姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if(null==mobileNum.getText()){
			Toast.makeText(AddReceiptAddrActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(mobileNum.getText())) {
			Toast.makeText(AddReceiptAddrActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
			return;
		}
		if(null==selectAreaTv.getText()){
			Toast.makeText(AddReceiptAddrActivity.this, "请输入省、市、区", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(selectAreaTv.getText())) {
			Toast.makeText(AddReceiptAddrActivity.this, "请输入省、市、区", Toast.LENGTH_SHORT).show();
			return;
		}
		if(null==addrDetail.getText()){
			Toast.makeText(AddReceiptAddrActivity.this, "请输入详细地址", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(addrDetail.getText())) {
			Toast.makeText(AddReceiptAddrActivity.this, "请输入详细地址", Toast.LENGTH_SHORT).show();
			return;
		}
		
		HttpUtils httpUtils = new HttpUtils();
		String url=ConstantClass.NET_URL+REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("consignee", userName.getText().toString());
		params.addBodyParameter("mobile", mobileNum.getText().toString());
		params.addBodyParameter("address", selectAreaTv.getText()+addrDetail.getText().toString());
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
					Intent intent=new Intent(AddReceiptAddrActivity.this,ReceiptAddressActivity.class);
					startActivity(intent);
					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(AddReceiptAddrActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(AddReceiptAddrActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void insureToAddAddr(View v){
		loadData();
		
	}
	public void backToAddrActivity(View v){
		finish();
	}
}
