package com.example.aftermarket.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.QuestionListAdapter;
import com.example.aftermarket.adpter.SearchAutoAdapter;
import com.example.aftermarket.bean.Question;
import com.example.aftermarket.bean.QuestionList;
import com.example.aftermarket.bean.ResponseResult;
import com.example.aftermarket.bean.SearchAutoData;
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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.AutoText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SearchQuestionContent extends Activity implements OnClickListener, IXListViewListener {

	private EditText mAutoEdit;
	private ImageView cancelBt;
	public static final String SEARCH_HISTORY = "search_history";
	private static final String REQUEST_HEADER = "questionList";
	private ListView mAutoListView;
	private SearchAutoAdapter mSearchAutoAdapter;
	private TextView mSearchButtoon;
	private XListView listView;
	private DemoApplication app;
	private QuestionListAdapter adapter; 
	private List<Question> questions;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) getApplication();
		setContentView(R.layout.search_edit_activity);
		mAutoEdit = (EditText) findViewById(R.id.auto_edit);
		cancelBt = (ImageView) findViewById(R.id.cancel_img);
		listView = (XListView) findViewById(R.id.listView_search);
		listView.setXListViewListener(this);
		listView.setPullLoadEnable(false);
		mHandler = new Handler();
		mAutoEdit.setOnKeyListener(onKey);
		cancelBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				mAutoEdit.setText("");
			}
		});

		init();

		TextWatcher watcher = new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

				Log.e("dajiayilian", "watcher" + s.toString());
				if (TextUtils.isEmpty(s)) {

				} else {

				}
			}
		};
		mAutoEdit.addTextChangedListener(watcher);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (null != questions) {
					Intent intent = new Intent(SearchQuestionContent.this, QuestionDetailActivity.class);
					intent.putExtra("question_id", questions.get(position).question_id);
					startActivity(intent);
				}

			}
		});
	}

	OnKeyListener onKey = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			// TODO Auto-generated method stub
			refreshData(mAutoEdit.getText().toString().trim());

			if (keyCode == KeyEvent.KEYCODE_ENTER) {

				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

				if (imm.isActive()) {

					imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

				}

				return true;

			}

			return false;

		}

	};

	private void init() {
		mSearchAutoAdapter = new SearchAutoAdapter(this, -1, this);
		/*
		 * mAutoListView = (ListView) findViewById(R.id.auto_listview);
		 * mAutoListView.setAdapter(mSearchAutoAdapter);
		 * mAutoListView.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View view, int
		 * position, long arg3) { SearchAutoData data = (SearchAutoData)
		 * mSearchAutoAdapter.getItem(position);
		 * mAutoEdit.setText(data.getContent()); mSearchButtoon.performClick();
		 * } });
		 */

		mSearchButtoon = (TextView) findViewById(R.id.search_cancel);
		mSearchButtoon.setOnClickListener(this);
		mAutoEdit = (EditText) findViewById(R.id.auto_edit);
		mAutoEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				mSearchAutoAdapter.performFiltering(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.search_cancel) {// 搜索按钮
			// saveSearchHistory();
			// mSearchAutoAdapter.initSearchHistory();
			finish();
		} else {// "+"号按钮
			SearchAutoData data = (SearchAutoData) v.getTag();
			mAutoEdit.setText(data.getContent());
		}
	}

	/*
	 * 保存搜索记录
	 */
	private void saveSearchHistory() {
		String text = mAutoEdit.getText().toString().trim();
		if (text.length() < 1) {
			return;
		}
		SharedPreferences sp = getSharedPreferences(SEARCH_HISTORY, 0);
		String longhistory = sp.getString(SEARCH_HISTORY, "");
		String[] tmpHistory = longhistory.split(",");
		ArrayList<String> history = new ArrayList<String>(Arrays.asList(tmpHistory));
		if (history.size() > 0) {
			int i;
			for (i = 0; i < history.size(); i++) {
				if (text.equals(history.get(i))) {
					history.remove(i);
					break;
				}
			}
			history.add(0, text);
		}
		if (history.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < history.size(); i++) {
				sb.append(history.get(i) + ",");
			}
			sp.edit().putString(SEARCH_HISTORY, sb.toString()).commit();
		} else {
			sp.edit().putString(SEARCH_HISTORY, text + ",").commit();
		}
	}

	private void refreshData(String data) {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", data);
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
					ResponseResult<QuestionList> responseResult = new Gson().fromJson(result, new TypeToken<ResponseResult<QuestionList>>() {
					}.getType());
					questions = responseResult.data.list;
					adapter = new QuestionListAdapter(SearchQuestionContent.this);
					listView.setAdapter(adapter);
					adapter.setItems(questions, false);
					adapter.notifyDataSetChanged();
					Log.e("dajia", "===" + questions);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(SearchQuestionContent.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(SearchQuestionContent.this, ShopLoginActivity.class);
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

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshData("");
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		listView.stopLoadMore();
	}

}
