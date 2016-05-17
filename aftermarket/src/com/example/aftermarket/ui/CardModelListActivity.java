package com.example.aftermarket.ui;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.Car;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.listview.CharacterParser;
import com.example.aftermarket.listview.ClearEditText;
import com.example.aftermarket.listview.PinyinComparator;
import com.example.aftermarket.listview.SideBar;
import com.example.aftermarket.listview.SortAdapter;
import com.example.aftermarket.listview.SortModel;
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
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CardModelListActivity extends Activity {
	private static final String REQUEST_HEADER = "carCategories";
	private DemoApplication app;
	private String token;
	private Car car;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	private PinyinComparator pinyinComparator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) this.getApplication();
		setContentView(R.layout.cars_model_list);
		token=app.getToken();
		Log.e("CardModelListActivity", ""+token);
		loadData();
	}

	private void loadData() {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", token);
		params.addBodyParameter("category_id", "0");
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
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Car>() {}.getType();
					car = gson.fromJson(result, type);
					
					for (int i = 0; i<car.data.size(); i++) {
						for (int j = 0; j < car.data.get(i).children.size(); j++) {
							Log.e("CardModelListActivity", "jsonObj"+car.data.get(i).children.get(j).category_id);
						}
						
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(CardModelListActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CardModelListActivity.this, ShopLoginActivity.class);
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
