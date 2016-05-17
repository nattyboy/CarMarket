package com.example.aftermarket.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.R;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.util.MD5Utils;
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
import android.widget.EditText;
import android.widget.Toast;

public class FindPassWordActivity extends Activity {
	private EditText newPassWord;
	private EditText newPassWord1;
	private String get_code;
	private String user_mobile;
	private static final String REQUEST_HEADER_REGISTER = "updatePwd";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_password_activity);
		newPassWord = (EditText) findViewById(R.id.set_password_find);
		newPassWord1 = (EditText) findViewById(R.id.reset_psss_et_find);
		Intent intent = getIntent();
		get_code = intent.getStringExtra("get_code");
		user_mobile = intent.getStringExtra("user_mobile");
		Log.e("dajia", getIntent().toString());
	}

	public void modifyPassWord(View v) {

		if (TextUtils.isEmpty(newPassWord.getText())) {
			Toast.makeText(FindPassWordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(newPassWord1.getText())) {
			Toast.makeText(FindPassWordActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();

			return;
		}
		if (newPassWord.getText().toString().equals(newPassWord1.getText().toString())) {

		} else {
			Toast.makeText(FindPassWordActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobile", user_mobile);
		params.addBodyParameter("pwd", MD5Utils.getMD5(newPassWord1.getText().toString()));
		params.addBodyParameter("code", get_code);
		String url = ConstantClass.NET_URL + REQUEST_HEADER_REGISTER;
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
				//Toast.makeText(FindPassWordActivity.this, s, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						Toast.makeText(FindPassWordActivity.this, "找回成功，请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(FindPassWordActivity.this, ShopLoginActivity.class);
						startActivity(intent);
						finish();
					} else if ("Code is Error".equals(String.valueOf(jsonObj.getString("msg")))) {
						Toast.makeText(FindPassWordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(FindPassWordActivity.this, "设置失败,请重试", Toast.LENGTH_SHORT).show();
					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
