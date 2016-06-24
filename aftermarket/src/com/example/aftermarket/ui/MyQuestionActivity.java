package com.example.aftermarket.ui;

import java.util.ArrayList;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.MyListener;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.MyQuestionAdapter;
import com.example.aftermarket.bean.MyQuestion;
import com.example.aftermarket.bean.MyQuestionData;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.views.PullToRefreshLayout;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class MyQuestionActivity extends Activity implements IXListViewListener {
	private static final String REQUEST_HEADER = "askList";
	PopupWindow popuWindowTel = null;
	View contentView1 = null;
	private DemoApplication app;
	private LinearLayout myQuestionProgress;
	private XListView questionListView;
	private MyQuestion myQuestion;
	private ArrayList<MyQuestionData> myQuestionDataList;
	private MyQuestionAdapter adapter;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		app = (DemoApplication) getApplication();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_question_layout);
		questionListView = (XListView) findViewById(R.id.my_question_listview);
		questionListView.setXListViewListener(this);
		questionListView.setPullLoadEnable(false);
		mHandler = new Handler();
		myQuestionProgress = (LinearLayout) findViewById(R.id.question_progress);
		loadData();
		questionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if(position-1>=0){
					Intent intent=new Intent(MyQuestionActivity.this,QuestionDetailActivity.class);
					intent.putExtra("question_id", myQuestionDataList.get(position-1).question_id);
					intent.putExtra("MyQuestionActivity", "MyQuestionActivity");
					startActivity(intent);
				}
				
			}
		});
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

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		/*
		 * params.addBodyParameter("region", sellerRegion);
		 * params.addBodyParameter("lat", sellerLat);
		 * params.addBodyParameter("lng", sellerLng);
		 * params.addBodyParameter("type", sellerType);
		 */
		params.addBodyParameter("token", app.getToken());
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(MyQuestionActivity.this, "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				myQuestionProgress.setVisibility(View.GONE);
				questionListView.setVisibility(View.VISIBLE);
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<MyQuestion>() {
					}.getType();
					myQuestion = gson.fromJson(result, type);
					myQuestionDataList = (ArrayList<MyQuestionData>) myQuestion.data;
					if (null != myQuestionDataList) {
						adapter = new MyQuestionAdapter(MyQuestionActivity.this, myQuestionDataList);
						questionListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					} else {
						questionListView.setAdapter(null);
					}
					Log.e("hello", "+++result++" + myQuestionDataList);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(MyQuestionActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(MyQuestionActivity.this, ShopLoginActivity.class);
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
	
	public void backToHomepage(View view){
		
		finish();
	}
	private void onLoad() {
		questionListView.stopRefresh();
		questionListView.stopLoadMore();
		questionListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				loadData();
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		questionListView.stopLoadMore();
	}

}
