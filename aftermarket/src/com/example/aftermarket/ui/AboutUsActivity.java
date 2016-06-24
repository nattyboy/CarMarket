package com.example.aftermarket.ui;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.AboutUs;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.activity.FeedBackActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class AboutUsActivity extends Activity {

	private static final String REQUEST_HEADER = "about";
	private ImageView onlineTel;
	PopupWindow popuWindowTel = null;
	View contentView1 = null;
	private AboutUs aboutUs;
	private ImageView about_us_logo;
	private TextView about_us_tv;
	private TextView version_tv;
	private DemoApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_activity);
		app=(DemoApplication) getApplication();
		version_tv = (TextView) findViewById(R.id.version_tv);
		about_us_tv = (TextView) findViewById(R.id.about_us_tv);
		about_us_logo = (ImageView) findViewById(R.id.about_us_logo);
		onlineTel = (ImageView) findViewById(R.id.online_kefu);
		onlineTel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if (popuWindowTel == null) {
					LayoutInflater mLayoutInflater = LayoutInflater.from(AboutUsActivity.this);
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
		});
		//loadData();
	}

	public void baceToMycenter(View v) {
		finish();
	}

	public void loadData() {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
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
					java.lang.reflect.Type type = new TypeToken<AboutUs>() {
					}.getType();
					aboutUs = gson.fromJson(result, type);
					if (null != aboutUs && null != aboutUs.data) {

						version_tv.setText(aboutUs.data.footer);
						about_us_tv.setText(aboutUs.data.content);
						int maxMemory = (int) Runtime.getRuntime().maxMemory();
						int cacheSize = maxMemory / 8;
						String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/x-utils";
						BitmapUtils bitmapUtils = new BitmapUtils(AboutUsActivity.this, path, cacheSize);
						bitmapUtils.configDefaultBitmapMaxSize(100, 100);
						bitmapUtils.display(about_us_logo, aboutUs.data.logo);
					}
				}

				catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录

					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
