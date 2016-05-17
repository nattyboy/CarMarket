package com.example.aftermarket.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.CarUser;
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
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShopRegisterActivity extends Activity {

	protected static final String REQUEST_HEADER = "getCode";
	private static final String REQUEST_HEADER_REGISTER = "register";
	private EditText userNameEditText;
	private EditText passwordEditText;
	private EditText verficationEditText;
	private TextView logginText;
	private CheckBox registerCheckBox;
	private HttpUtils httpUtils;
	private TextView getCodeTv;
	private TextView loginTv;
	private CarUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		userNameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		verficationEditText = (EditText) findViewById(R.id.verification);
		logginText = (TextView) findViewById(R.id.login_loginactivity);
		logginText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		getCodeTv = (TextView) findViewById(R.id.btnGetCode);
		registerCheckBox = (CheckBox) findViewById(R.id.to_join_checkBox_register);
		loginTv = (TextView) findViewById(R.id.login_register);
		loginTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(ShopRegisterActivity.this, ShopLoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		findViewById(R.id.login_loginactivity).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopRegisterActivity.this, ShopLoginActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.btnGetCode).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(userNameEditText.getText())) {
					Toast.makeText(ShopRegisterActivity.this, R.string.phone_num_can_not_be_empty, Toast.LENGTH_LONG).show();
					return;
				} else {
					/*
					 * if
					 * (!MobileNumPattern.isMobileNO(userNameEditText.getText(
					 * ).toString())) {
					 * 
					 * Toast.makeText(ShopRegisterActivity.this, "请输入正确手机号",
					 * Toast.LENGTH_LONG).show(); return; }
					 */

				}

				TimeCountUtil timeCountUtil = new TimeCountUtil(ShopRegisterActivity.this, 60000, 1000, getCodeTv);
				timeCountUtil.start();
				httpUtils = new HttpUtils();
				RequestParams params = new RequestParams();
				params.addBodyParameter("mobile", userNameEditText.getText().toString());
				String url = ConstantClass.NET_URL + REQUEST_HEADER;
				httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException e, String s) {
						Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
						// Toast.makeText(ShopRegisterActivity.this, s,
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
								// Toast.makeText(ShopRegisterActivity.this,
								// "注册成功", Toast.LENGTH_SHORT).show();
							} else if ("mobile exists".equals(String.valueOf(jsonObj.getString("msg")))) {
								Toast.makeText(ShopRegisterActivity.this, "该账号已存在", Toast.LENGTH_SHORT).show();
							} else if ("Code is Error".equals(String.valueOf(jsonObj.getString("msg")))) {
								Toast.makeText(ShopRegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
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

	public void vertifyAndRegister(View v) {
		if (TextUtils.isEmpty(userNameEditText.getText())) {
			Toast.makeText(ShopRegisterActivity.this, R.string.phone_num_can_not_be_empty, Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(passwordEditText.getText())) {
			Toast.makeText(ShopRegisterActivity.this, R.string.phone_num_can_not_be_empty, Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(verficationEditText.getText())) {
			Toast.makeText(ShopRegisterActivity.this, R.string.phone_num_can_not_be_empty, Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(userNameEditText.getText())) {
			Toast.makeText(ShopRegisterActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
			return;
		} else {
			/*
			 * if
			 * (!MobileNumPattern.isMobileNO(userNameEditText.getText().toString
			 * ())) {
			 * 
			 * Toast.makeText(ShopRegisterActivity.this, "请输入正确手机号",
			 * Toast.LENGTH_LONG).show(); return; }
			 */

		}
		if (TextUtils.isEmpty(verficationEditText.getText())) {
			Toast.makeText(ShopRegisterActivity.this, "验证码不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(passwordEditText.getText())) {
			Toast.makeText(ShopRegisterActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
			return;
		} else {
			if (!MobileNumPattern.isPassWordNO(passwordEditText.getText().toString())) {

				Toast.makeText(ShopRegisterActivity.this, "请输入正确的密码", Toast.LENGTH_LONG).show();
				return;
			}
		}
		if (registerCheckBox.isChecked()) {
		} else {
			Toast.makeText(ShopRegisterActivity.this, R.string.please_click_agree, Toast.LENGTH_SHORT).show();
			return;
		}
		httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobile", userNameEditText.getText().toString());
		params.addBodyParameter("pwd", MD5Utils.getMD5(passwordEditText.getText().toString()));
		params.addBodyParameter("code", verficationEditText.getText().toString());
		String url = ConstantClass.NET_URL + REQUEST_HEADER_REGISTER;
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
				Toast.makeText(ShopRegisterActivity.this, s, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<CarUser>() {
					}.getType();
					user = gson.fromJson(result, type);

					JSONObject data = jsonObj.getJSONObject("data");
					final DemoApplication app = (DemoApplication) getApplication();
					app.setToken(data.getString("token"));
					app.setEase_user(user.data.ease_user);
					app.setUserInfo(user.data);
					Log.e("dajia", "user.data.ease_user" + "" + user.data);

					// loadEase();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Toast.makeText(ShopRegisterActivity.this, "登录成功",
						// Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(ShopRegisterActivity.this, LoginAfterCarActivity.class);
						startActivity(intent);
						finish();
					} else if ("mobile exists".equals(String.valueOf(jsonObj.getString("msg")))) {
						Toast.makeText(ShopRegisterActivity.this, "该账号已存在", Toast.LENGTH_SHORT).show();
					} else if ("Code is Error".equals(String.valueOf(jsonObj.getString("msg")))) {
						Toast.makeText(ShopRegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
					} else {

					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void backToLogin(View v) {
		Intent intent = new Intent(ShopRegisterActivity.this, ShopLoginActivity.class);
		startActivity(intent);
	}
}
