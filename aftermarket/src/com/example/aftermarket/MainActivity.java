package com.example.aftermarket;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.bean.HomePageInfo;
import com.example.aftermarket.config.ConstantClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String REQUEST_HEADER = "home";
	ImageView iv;
	String resultJson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// iv=(ImageView) findViewById(R.id.issmageView1);
		HttpUtils httpUtils = new HttpUtils();
		// String url =
		// "http://jk1.cpioc.com/index.php?m=Admin&c=userApi&a=home";
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		httpUtils.send(HttpMethod.POST, url, null, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
				//Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<HomePageInfo>() {
					}.getType();
					HomePageInfo homePageInfo = gson.fromJson(result, type);
					resultJson = homePageInfo.data.top.get(0).x_img;
					Log.e("json", "1" + resultJson);
					// 获取应用程序最大可用内存
					int maxMemory = (int) Runtime.getRuntime().maxMemory();
					int cacheSize = maxMemory / 8;
					String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/x-utils";
					BitmapUtils bitmapUtils = new BitmapUtils(MainActivity.this, path, cacheSize);
					Log.e("json", "" + resultJson);
					bitmapUtils.display(iv, resultJson);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						//Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
					} else {
						//Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
