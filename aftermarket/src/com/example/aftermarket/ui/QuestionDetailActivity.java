package com.example.aftermarket.ui;

import android.R.bool;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.AnswerAdapter;
import com.example.aftermarket.adpter.SquareAdapter;
import com.example.aftermarket.bean.Answer;
import com.example.aftermarket.bean.QuestionDetail;
import com.example.aftermarket.bean.ResponseResult;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.views.NoScrollGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EASTMOOD on 2016/3/15.
 */
public class QuestionDetailActivity extends Activity {

	private static final String REQUEST_HEADER = "questionDetail";
	ListView listView;
	TextView content, car_name, order_time, checked, reply;
	NoScrollGridView gridView;
	SquareAdapter squareAdapter;
	AnswerAdapter adapter;
	private String question_id = null;
	private ImageView imageArrow;
	private boolean arrowBool = true;
	PopupWindow popuWindowTel = null;
	View contentView1 = null;
	private boolean addImg=true;
	private String questionActivityId=null;
	private DemoApplication app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app=(DemoApplication) getApplication();
		setContentView(R.layout.activity_question_detail);
		content = (TextView) findViewById(R.id.content);
		car_name = (TextView) findViewById(R.id.car_name);
		order_time = (TextView) findViewById(R.id.order_time);
		checked = (TextView) findViewById(R.id.checked);
		reply = (TextView) findViewById(R.id.reply);
		Intent intent = getIntent();
		question_id = intent.getStringExtra("question_id");
		questionActivityId=intent.getStringExtra("MyQuestionActivity");
		listView = (ListView) findViewById(R.id.listView_question);
		adapter = new AnswerAdapter(this, question_id);
		listView.setAdapter(adapter);
		imageArrow = (ImageView) findViewById(R.id.imageView2_arraw);
		gridView = (NoScrollGridView) findViewById(R.id.gridView_question);
		squareAdapter = new SquareAdapter(this, 100);
		gridView.setAdapter(squareAdapter);
		imageArrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				arrowBool = !arrowBool;
				if (arrowBool) {
					imageArrow.setImageResource(R.drawable.cllick_xiala);
					listView.setVisibility(View.GONE);
				} else {
					imageArrow.setImageResource(R.drawable.cllick_xiala2);
					listView.setVisibility(View.VISIBLE);
				}

			}
		});
		Log.e("str", "oncreate");
		//loadData();
	}

	@Override
	protected void onResume() {

		super.onResume();
		
		loadData();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		addImg=false;
		Log.e("str", "onResume");
	}

	private void loadData() {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("question_id", question_id);
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
					ResponseResult<QuestionDetail> responseResult = new Gson().fromJson(result, new TypeToken<ResponseResult<QuestionDetail>>() {
					}.getType());
					content.setText(responseResult.data.content);
					car_name.setText(responseResult.data.car_name);
					if("false".equals(responseResult.data.order_time)){
						order_time.setText("");
					}else{
						order_time.setText("预约时间: " + responseResult.data.order_time);
					}
					if("买车".equals(responseResult.data.checked)||"卖车".equals(responseResult.data.checked)){
						order_time.setText("");
					}
					Log.e("dajiayilian", "responseResult" + responseResult.data.order_time);
					checked.setText(responseResult.data.checked);
					reply.setText(responseResult.data.reply + "个回答");
					if(addImg){
						List<String> imgs = responseResult.data.question_img;
						squareAdapter.addData(imgs);
						squareAdapter.notifyDataSetChanged();
						addImg=false;
					}
					
					List<Answer> questions = responseResult.data.answers;
					adapter.setItems(questions, false);
					adapter.notifyDataSetChanged();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(QuestionDetailActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(QuestionDetailActivity.this, ShopLoginActivity.class);
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

	public void backToQuestionFragment(View v) {
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
		lp.alpha = 0.6f;
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
	public String getquestionActivityId(){
		
		return questionActivityId;
	}
}
