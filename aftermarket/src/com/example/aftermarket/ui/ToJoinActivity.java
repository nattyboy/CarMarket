package com.example.aftermarket.ui;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.utils.MobileNumPattern;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.EditText;
import android.widget.Toast;

public class ToJoinActivity extends Activity {
	private static final String REQUEST_HEADER = "applyIn";
	private HttpUtils httpUtils;
	private HttpParams params;
	private EditText userEditText;
	private EditText userTelEditText;
	private EditText cmpNameEditText;
	private EditText userAddressEditText;
	private EditText userDescEditText;
	private CheckBox toJoinCheckBox;
	PopupWindow popuWindowTel = null;
	View contentView1 = null;
	private DemoApplication app;
	private TextView textVi8_protocal;
	private  WebView webview;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.to_join_activity);
		app=(DemoApplication) getApplication();
		userEditText = (EditText) findViewById(R.id.join_username);
		userTelEditText = (EditText) findViewById(R.id.join_tel);
		cmpNameEditText = (EditText) findViewById(R.id.join_cmp_name);
		userAddressEditText = (EditText) findViewById(R.id.join_cmp_address);
		userDescEditText = (EditText) findViewById(R.id.join_cmp_desc);
		toJoinCheckBox = (CheckBox) findViewById(R.id.to_join_checkBox);
		textVi8_protocal=(TextView) findViewById(R.id.textVi8_protocal);
		textVi8_protocal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			
				Intent intent=new Intent(ToJoinActivity.this,ProtocalActivity.class);
				startActivity(intent);
				
			}
		});

	}
	

	public void joinOnclick(View v) {
		toJoinCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					// Toast.makeText(ToJoinActivity.this,
					// R.string.please_click_agree, Toast.LENGTH_LONG).show();

					// return;
				} else {
				}

			}
		});

		if (TextUtils.isEmpty(userEditText.getText())) {
			Toast.makeText(ToJoinActivity.this, "姓名不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(userTelEditText.getText())) {
			Toast.makeText(ToJoinActivity.this, "电话号码不能为空", Toast.LENGTH_LONG).show();
			return;
		} else {
			/*if (!MobileNumPattern.isMobileNO(userTelEditText.getText().toString())) {

				Toast.makeText(ToJoinActivity.this, "请输入正确手机号", Toast.LENGTH_LONG).show();
				return;
			}*/

		}
		if (TextUtils.isEmpty(cmpNameEditText.getText())) {
			Toast.makeText(ToJoinActivity.this, "公司名称不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(userAddressEditText.getText())) {
			Toast.makeText(ToJoinActivity.this, "公司地址不能为空", Toast.LENGTH_LONG).show();
			return;
		}
		if (toJoinCheckBox.isChecked()) {
		} else {
			Toast.makeText(ToJoinActivity.this, R.string.please_click_agree, Toast.LENGTH_LONG).show();
			return;
		}

		httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("contact_user", userEditText.getText().toString());
		params.addBodyParameter("contact_mobile", userTelEditText.getText().toString());
		params.addBodyParameter("company_name", cmpNameEditText.getText().toString());
		params.addBodyParameter("address", userAddressEditText.getText().toString());
		params.addBodyParameter("company_desc", userDescEditText.getText().toString());
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
				//Toast.makeText(ToJoinActivity.this, s, Toast.LENGTH_SHORT).show();
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
						Toast.makeText(ToJoinActivity.this, "提交成功，请耐心等候", Toast.LENGTH_SHORT).show();
						finish();
						
					} else {
						Toast.makeText(ToJoinActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void backToHomeFragment(View view) {
		finish();
	}

	public void onlineTel(View view) {

		if (popuWindowTel == null) {
			LayoutInflater mLayoutInflater = LayoutInflater.from(this);
			contentView1 = mLayoutInflater.inflate(R.layout.online_tel, null);
			popuWindowTel = new PopupWindow(contentView1, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		ColorDrawable cd = new ColorDrawable(0x000000);
		popuWindowTel.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.4f;
		getWindow().setAttributes(lp);
		popuWindowTel.setOutsideTouchable(true);
		popuWindowTel.setFocusable(true);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		popuWindowTel.setWidth(display.getWidth() * 80 / 100);
		popuWindowTel.showAtLocation((View) view.getParent(), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);

		popuWindowTel.update();
		TextView textView_tel_num=(TextView) contentView1.findViewById(R.id.textView_tel_num);
		textView_tel_num.setText("拨打电话： "+app.getTelNum().trim());
		TextView cancelTv = (TextView) contentView1.findViewById(R.id.cancel_tv);
		TextView ringTv = (TextView) contentView1.findViewById(R.id.ring_tv);
		cancelTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popuWindowTel.dismiss();
			}
		});
		ringTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mobile =app.getTelNum().trim();
				Intent intent = new Intent();
				intent.setAction("android.intent.action.CALL");
				intent.setData(Uri.parse("tel:" + mobile));// mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
				startActivity(intent);
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
}
