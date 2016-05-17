package com.example.aftermarket.ui;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.Address;
import com.example.aftermarket.bean.AddressPicker;
import com.example.aftermarket.bean.CarInfo;
import com.example.aftermarket.bean.UserInfo;
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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class LoginAfterCarActivity extends Activity {

	private RelativeLayout sexLayout;
	private RelativeLayout arealayout;
	private RelativeLayout chooseCar;
	private TextView setSex;
	private TextView setArea;
	private PopupWindow popupWindow; 
	private TextView manTv;
	private TextView womanTv;
	private TextView cancelTv;
	private TextView skipClick;
	private UserInfo userInfo;
	private DemoApplication app;
	private EditText userName;
	private static final String REQUEST_HEADER_MODIFYUSER = "modifyUser";
	private static final String REQUEST_HEADER_DEFAUTADD = "setDefaultAddress";
	private static final String REQUEST_HEADER = "addressAdd";
	private TextView carTv;
	private View wholeView;
	PopupWindow popuWindowTel = null;
	View contentView1 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) getApplication();
		setContentView(R.layout.login_aftercar_activity);
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);

		wholeView = mLayoutInflater.inflate(R.layout.login_aftercar_activity, null);
		userInfo = app.getUserInfo();
		Log.e("dajiayilian", userInfo.toString());
		carTv = (TextView) findViewById(R.id.textView11_car);
		chooseCar = (RelativeLayout) findViewById(R.id.choose_car_skip);
		userName = (EditText) findViewById(R.id.editText1_user_name);
		sexLayout = (RelativeLayout) findViewById(R.id.sex_layout);
		arealayout = (RelativeLayout) findViewById(R.id.area_layout);
		setSex = (TextView) findViewById(R.id.set_sex);
		setArea = (TextView) findViewById(R.id.set_area);
		skipClick = (TextView) findViewById(R.id.textView2_skip);
		/*LayoutInflater inflater = LayoutInflater.from(this);
		final View view = inflater.inflate(R.layout.sex_item, null);
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.update();
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		ColorDrawable dw = new ColorDrawable(-00000);
		popupWindow.setBackgroundDrawable(dw);*/

		skipClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(LoginAfterCarActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
			}
		});
		sexLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popuWindowTel == null) {
					LayoutInflater mmLayoutInflater = LayoutInflater.from(LoginAfterCarActivity.this);
					contentView1 = mmLayoutInflater.inflate(R.layout.sex_item, null);
					popuWindowTel = new PopupWindow(contentView1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				}
				ColorDrawable cd = new ColorDrawable(0x000000);
				popuWindowTel.setBackgroundDrawable(cd);
				// 产生背景变暗效果
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 0.4f;
				getWindow().setAttributes(lp);
				//popuWindowTel.setOutsideTouchable(true);
				//popuWindowTel.setFocusable(true);
				WindowManager windowManager = getWindowManager();
				Display display = windowManager.getDefaultDisplay();
				//popuWindowTel.setWidth(display.getWidth() * 80 / 100);
				popuWindowTel.showAtLocation(sexLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 10);
				popuWindowTel.setAnimationStyle(R.style.ActionSheetDialogAnimation);
				popuWindowTel.update();
				manTv = (TextView) contentView1.findViewById(R.id.man_tv);
				womanTv = (TextView) contentView1.findViewById(R.id.woman_tv);
				cancelTv = (TextView) contentView1.findViewById(R.id.cancle);
				manTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (setSex.getText().toString().equals("男")) {
							popuWindowTel.dismiss();
						}
						setSex.setText("男");
						modifyUserInfo(userInfo.user_name, "1");
						userInfo.sex = "1";
						app.setUserInfo(userInfo);
						popuWindowTel.dismiss();

					}
				});

				womanTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (setSex.getText().toString().equals("女")) {
							popuWindowTel.dismiss();
						}
						setSex.setText("女");
						modifyUserInfo(userInfo.user_name, "0");
						userInfo.sex = "0";
						app.setUserInfo(userInfo);
						popuWindowTel.dismiss();
					}
				});
				cancelTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						popuWindowTel.dismiss();
					}
				});
				popuWindowTel.setOnDismissListener(new OnDismissListener() {

					// 在dismiss中恢复透明度
					public void onDismiss() {
						WindowManager.LayoutParams lp = getWindow().getAttributes();
						lp.alpha = 1f;
						getWindow().setAttributes(lp);
					}
				});
			}
		});
		
		/**
		 * 设置所在地
		 */
		arealayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AddressPicker addressPicker = new AddressPicker();

				addressPicker.selectAddressDialog(LoginAfterCarActivity.this, setArea);

				Log.e("dajia", "addressPicker" + setArea.getText());
			}
		});
		/**
		 * 选择车系
		 */
		chooseCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginAfterCarActivity.this, MyCarPortActivity.class);
				intent.putExtra("choose_car_login", "choose_car_login");
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		carTv = (TextView) findViewById(R.id.textView11_car);
		if (null != intent.getStringExtra("car_version_login")) {
			carTv.setText(intent.getStringExtra("car_version_login"));
		}
		Log.e("car_version", "car_versionintent" + intent.getStringExtra("car_version"));
	}

	public void toCarHome(View v) {

		Intent intent = new Intent(LoginAfterCarActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	public void baceToRegister(View v){
		Intent intent=new Intent(LoginAfterCarActivity.this,ShopRegisterActivity.class);
		startActivity(intent);
		finish();
	}

	private void setDefaultAddress(String address) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("consignee", userName.getText().toString());
		params.addBodyParameter("mobile", userInfo.mobile);
		params.addBodyParameter("address", setArea.getText().toString());
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
					java.lang.reflect.Type type = new TypeToken<Address>() {
					}.getType();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(LoginAfterCarActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(LoginAfterCarActivity.this, ShopLoginActivity.class);
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

	private void modifyUserInfo(String userName, String sex) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_MODIFYUSER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("user_name", userName);
		params.addBodyParameter("sex", sex);
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
					java.lang.reflect.Type type = new TypeToken<CarInfo>() {
					}.getType();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(LoginAfterCarActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(LoginAfterCarActivity.this, ShopLoginActivity.class);
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
