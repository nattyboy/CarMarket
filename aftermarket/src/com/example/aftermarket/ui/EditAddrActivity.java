package com.example.aftermarket.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.ReceiptAddressListAdapter;
import com.example.aftermarket.bean.Address;
import com.example.aftermarket.bean.AddressInfo;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditAddrActivity extends Activity {
	private static final String REQUEST_HEADER = "addressEdit";
	private RelativeLayout selectAreaLayout;
	private TextView selectAreaTvEdit;
	private DemoApplication app;
	private EditText userNameEdit;
	private EditText mobileNumEdit;
	private EditText addrDetailEdit;
	private String addressId;
	private AddressInfo addressInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) getApplication();
		setContentView(R.layout.edit_receipt_addr_activity_edit);
		selectAreaLayout = (RelativeLayout) findViewById(R.id.select_province_layout_edit_edit);
		selectAreaTvEdit = (TextView) findViewById(R.id.select_area_tv_add_edit);
		userNameEdit = (EditText) findViewById(R.id.user_name_et_edit);
		mobileNumEdit = (EditText) findViewById(R.id.mobile_num_et_edit);
		addrDetailEdit = (EditText) findViewById(R.id.address_detail_edit);
		addressInfo=(AddressInfo) getIntent().getSerializableExtra("addressInfo");
		Log.e("dajia", "selectAreaLayout" + addressInfo);
		if(null!=addressInfo){
			
			userNameEdit.setText(addressInfo.consignee);
			mobileNumEdit.setText(addressInfo.mobile);
			String area=addressInfo.address;
			int lastIndex = area.lastIndexOf("县");
			int lastIndex1= area.lastIndexOf("区");
			int lastIndex3= area.lastIndexOf("其他");
			int temp=Math.max(lastIndex, lastIndex1);
			int max=Math.max(temp,lastIndex3);
			Log.e("dajia", "selectAreaLayout" + max);
			if(max>=0){
				selectAreaTvEdit.setText(area.subSequence(0, max+1));
				if(max+1<area.length()){
					addrDetailEdit.setText(area.subSequence(max+1, area.length()));
				}
				
			}
			
		}
		selectAreaLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AddressPicker addressPicker = new AddressPicker();
				Log.e("dajia", "addressPicker" + addressPicker);
				addressPicker.selectAddressDialog(EditAddrActivity.this, selectAreaTvEdit);
				
			}
		});
		Intent intent=getIntent();
		addressId=intent.getStringExtra("address_id");
		Log.e("dajia", "addressId"+addressId);

	}

	public void backToAddlist(View view){
		finish();
	}
	public void insureToEditAddr(View v) {
		
		loadData();
		

	}
	
	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url=ConstantClass.NET_URL+REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("address_id", addressId);
		params.addBodyParameter("consignee", userNameEdit.getText().toString());
		params.addBodyParameter("mobile", mobileNumEdit.getText().toString());
		params.addBodyParameter("address", selectAreaTvEdit.getText()+addrDetailEdit.getText().toString());
		params.addBodyParameter("token", app.getToken());
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) { Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						
						finish();
					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(EditAddrActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(EditAddrActivity.this, ShopLoginActivity.class);
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
