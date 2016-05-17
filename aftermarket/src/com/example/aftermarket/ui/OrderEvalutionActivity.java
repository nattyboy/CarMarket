package com.example.aftermarket.ui;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.SquareAdapter;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.activity.FeedBackActivity;
import com.example.aftermarket.views.NoScrollGridView;
import com.google.gson.Gson;
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
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

public class OrderEvalutionActivity extends Activity{
	private static final String REQUEST_HEADER = "evaluation";
	private DemoApplication app;
	private EditText input_evalute;
	private SquareAdapter adapter;
	NoScrollGridView gridView;
	private RatingBar ratingBar_seller_evalute;
	private String starNum;
	private String order_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		app=(DemoApplication) getApplication();
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		order_id=intent.getStringExtra("order_id");
		setContentView(R.layout.order_evalute_activity_layout);
		input_evalute=(EditText) findViewById(R.id.input_evalute);
		ratingBar_seller_evalute=(RatingBar) findViewById(R.id.ratingBar_seller_evalute);
		gridView = (NoScrollGridView) findViewById(R.id.noScrollgridview_evalute);
		adapter = new SquareAdapter(OrderEvalutionActivity.this, 0);
		gridView.setAdapter(adapter);
		ratingBar_seller_evalute.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

				starNum=String.valueOf(rating);
				
			}
		});
		
	}
	
	public void backToLastPage(View view){
		
		finish();
	}
	public void submitEvaluate(View view){
		
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("order_id", order_id);
		params.addBodyParameter("score", starNum);
		if (TextUtils.isEmpty(input_evalute.getText())) {

		} else {
			params.addBodyParameter("content", input_evalute.getText().toString());
		}
		if (null != adapter.imageList()) {
			params.addBodyParameter("count", String.valueOf(adapter.imageList().size()));
			for (int i = 0; i < adapter.imageList().size(); i++) {

				params.addBodyParameter("poster" + i, new File(adapter.imageList().get(i)));
			}
		}

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
				}

				catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						Toast.makeText(OrderEvalutionActivity.this, "恭喜你，评价成功！", Toast.LENGTH_SHORT).show();
						finish();
					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(OrderEvalutionActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(OrderEvalutionActivity.this, ShopLoginActivity.class);
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
