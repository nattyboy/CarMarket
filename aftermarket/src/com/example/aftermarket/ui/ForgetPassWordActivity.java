package com.example.aftermarket.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.R;
import com.example.aftermarket.bean.Car;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.util.MD5Utils;
import com.example.aftermarket.utils.MobileNumPattern;
import com.example.aftermarket.utils.TimeCountUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPassWordActivity extends Activity {
	protected static final String REQUEST_HEADER = "getCode";
	private static final String REQUEST_HEADER_UPDATEPWD = "updatePwd";
	private HttpUtils httpUtils;
	private EditText forgetUserEditText;
	private EditText forgetVetifyEditText;
	private TextView btnGetCodeBt;
	private EditText passWordNew;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password_activity);
		forgetUserEditText = (EditText) findViewById(R.id.forget_username);
		forgetVetifyEditText = (EditText) findViewById(R.id.forget_verification);
		passWordNew = (EditText) findViewById(R.id.password_new);
		btnGetCodeBt = (TextView) findViewById(R.id.btnGetCodeBt_forget);
		btnGetCodeBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(forgetUserEditText.getText())) {
					Toast.makeText(ForgetPassWordActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
					return;
				} else {
					/*
					 * if
					 * (!MobileNumPattern.isMobileNO(forgetUserEditText.getText
					 * ().toString())) {
					 * 
					 * Toast.makeText(ForgetPassWordActivity.this, "请输入正确手机号",
					 * Toast.LENGTH_LONG).show(); return; }
					 */

				}
				TimeCountUtil timeCountUtil = new TimeCountUtil(ForgetPassWordActivity.this, 60000, 1000, btnGetCodeBt);
				timeCountUtil.start();
				httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("mobile", forgetUserEditText.getText().toString());
				String url = ConstantClass.NET_URL + REQUEST_HEADER;
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
						} catch (JSONException e) {
							e.printStackTrace();
						}
						try {
							if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
								

							} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
							} else {

							}
							Log.e("hello", "+++++" + jsonObj.getString("msg"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

			}
		});

	}

	public void loginForget(View v) {

		if (TextUtils.isEmpty(forgetUserEditText.getText())) {
			Toast.makeText(ForgetPassWordActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
			return;
		} else {
			/*
			 * if
			 * (!MobileNumPattern.isMobileNO(forgetUserEditText.getText().toString
			 * ())) {
			 * 
			 * Toast.makeText(ForgetPassWordActivity.this, "请输入正确手机号",
			 * Toast.LENGTH_LONG).show(); return; }
			 */

		}
		if (TextUtils.isEmpty(forgetVetifyEditText.getText())) {
			Toast.makeText(ForgetPassWordActivity.this, "验证码不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		/*
		 * if (TextUtils.isEmpty(passWordNew.getText())) {
		 * Toast.makeText(ForgetPassWordActivity.this, "密码不能为空",
		 * Toast.LENGTH_LONG).show(); return; } else { if
		 * (!MobileNumPattern.isPassWordNO(passWordNew.getText().toString())) {
		 * 
		 * Toast.makeText(ForgetPassWordActivity.this, "请输入正确的密码",
		 * Toast.LENGTH_LONG).show(); return; } }
		 */
		
		Intent intent = new Intent(ForgetPassWordActivity.this, FindPassWordActivity.class);
		intent.putExtra("user_mobile", forgetUserEditText.getText().toString());
		intent.putExtra("get_code", forgetVetifyEditText.getText().toString());
		startActivity(intent);
		finish();
		/*httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobile", forgetUserEditText.getText().toString());
		params.addBodyParameter("code", forgetVetifyEditText.getText().toString());
		params.addBodyParameter("pwd", "12345");
		String url = ConstantClass.NET_URL + REQUEST_HEADER_UPDATEPWD;*/
		/*httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
				// Toast.makeText(ForgetPassWordActivity.this, s,
				// Toast.LENGTH_SHORT).show();
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
						Intent intent = new Intent(ForgetPassWordActivity.this, FindPassWordActivity.class);
						intent.putExtra("user_mobile", forgetUserEditText.getText().toString());
						intent.putExtra("get_code", forgetVetifyEditText.getText().toString());
						startActivity(intent);
						finish();
					} else if ("Code is Error".equals(String.valueOf(jsonObj.getString("msg")))) {
						Toast.makeText(ForgetPassWordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(ForgetPassWordActivity.this, "修改失败,请重试", Toast.LENGTH_SHORT).show();
					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});*/
	}

	public void backTo(View v) {
		Intent intent = new Intent(ForgetPassWordActivity.this, ShopLoginActivity.class);
		startActivity(intent);
	}
}
