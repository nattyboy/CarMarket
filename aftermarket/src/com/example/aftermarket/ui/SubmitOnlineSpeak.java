package com.example.aftermarket.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.Answer;
import com.example.aftermarket.bean.QuestionDetail;
import com.example.aftermarket.bean.ResponseResult;
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
import android.widget.EditText;
import android.widget.Toast;

public class SubmitOnlineSpeak extends Activity {
	private static final String REQUEST_HEADER = "doAnswer";
	private EditText onlineSpeakText;
	private DemoApplication app;
	private String question_id;
	private String pre_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) getApplication();
		setContentView(R.layout.online_liuyan_activity);
		onlineSpeakText = (EditText) findViewById(R.id.online_speak_et);
		Intent intent = getIntent();
		question_id = intent.getStringExtra("question_id");
		pre_id = intent.getStringExtra("pre_id");
	}

	public void submitOnlineSpeak(View view) {

		String content = onlineSpeakText.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(this, "留言不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {
			uploadData(content);
		}

	}

	public void backTOQuestionDetail(View view) {
		finish();
	}

	private void uploadData(String content) {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("question_id", question_id);
		params.addBodyParameter("pre_id", pre_id);
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("content", content);
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

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						SubmitOnlineSpeak.this.finish();
					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(SubmitOnlineSpeak.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SubmitOnlineSpeak.this, ShopLoginActivity.class);
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
