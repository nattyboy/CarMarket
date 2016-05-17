package com.example.aftermarket.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.Car;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.util.MD5Utils;
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
import android.widget.EditText;
import android.widget.Toast;

public class SetPasswordActivity extends Activity {

	private static final String REQUEST_HEADER = "modifyPwd";

	private DemoApplication app;

	private EditText passWord, newPassWord, newPassWord1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password_activity);
		passWord = (EditText) findViewById(R.id.password_et);
		newPassWord = (EditText) findViewById(R.id.set_password);
		newPassWord1 = (EditText) findViewById(R.id.reset_psss_et);

	}

	public void modifyPassWord(View v) {

		if (TextUtils.isEmpty(passWord.getText().toString().trim())) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(newPassWord.getText().toString().trim())) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(newPassWord1.getText().toString().trim())) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (newPassWord.getText().toString().equals(newPassWord1.getText().toString())) {
			uploadData();
		} else {
			Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
		}

	}

	private void uploadData() {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("old_pwd", MD5Utils.getMD5(passWord.getText().toString()));
		params.addBodyParameter("new_pwd", MD5Utils.getMD5(newPassWord1.getText().toString()));
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
					java.lang.reflect.Type type = new TypeToken<Car>() {
					}.getType();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						Toast.makeText(SetPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
						SetPasswordActivity.this.finish();
					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(SetPasswordActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SetPasswordActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {
						Log.e("hello", "+++++" + jsonObj.getString("msg"));
						Toast.makeText(SetPasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void backToMycent(View view) {
		finish();
	}

}
