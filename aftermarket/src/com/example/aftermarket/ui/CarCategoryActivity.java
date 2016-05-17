package com.example.aftermarket.ui;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.CarSeriesAdapter;
import com.example.aftermarket.bean.Car;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.listview.CharacterParser;
import com.example.aftermarket.listview.ClearEditText;
import com.example.aftermarket.listview.PinyinComparator;
import com.example.aftermarket.listview.SideBar;
import com.example.aftermarket.listview.SideBar.OnTouchingLetterChangedListener;
import com.example.aftermarket.listview.SortAdapter;
import com.example.aftermarket.listview.SortModel;
import com.example.aftermarket.utils.Px2DpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class CarCategoryActivity extends Activity {
	private static final String REQUEST_HEADER = "carCategories";
	protected static final int DATA_UPDATE = 0x1;
	private DemoApplication app;
	private String token;
	private Car car;
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;
	private EditText mClearEditText;
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList = new ArrayList<SortModel>();
	private PinyinComparator pinyinComparator;
	private PopupWindow popupWindowCar;
	private View popView;
	private RelativeLayout chooseBranch;
	private TextView popWindowAnTv;
	private ListView carSeriesLv;
	private CarSeriesAdapter carSeriesAdapter;
	private int carItemTypeCount;
	private List<String> carItemType;
	private List<String> carNameItem = new ArrayList<>();
	private List<String> carImgItem = new ArrayList<>();
	private List<String> carTitleItem = new ArrayList<>();
	private List<String> carIdItem = new ArrayList<>();
	private List<Object> carItem = new ArrayList<>();
	private Bundle bundle;
	private RelativeLayout titleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cars_model_list);
		titleBar = (RelativeLayout) findViewById(R.id.ll_car_list);
		app = (DemoApplication) getApplication();
		popView = LayoutInflater.from(this).inflate(R.layout.car_series, null);
		carSeriesLv = (ListView) popView.findViewById(R.id.car_name_listView);
		chooseBranch = (RelativeLayout) findViewById(R.id.ll_car_list);
		popWindowAnTv = (TextView) findViewById(R.id.popupwindow_id);
		bundle = this.getIntent().getExtras();
		initViews();
		loadData();

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DATA_UPDATE:
				initViews();
				break;
			}
		}

	};

	public void baceTo(View view) {
		finish();
	}

	private void initViews() {
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Toast.makeText(getApplication(), ((SortModel)
				// adapter.getItem(position)).getName(),
				// Toast.LENGTH_SHORT).show();

				showPopWindow(view, ((SortModel) adapter.getItem(position)).getId());

			}
		});

		mClearEditText = (EditText) findViewById(R.id.car_search);

		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private List<SortModel> filledData(String[] date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.length; i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date[i]);
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			Resources res = this.getResources();
			Drawable myImage = res.getDrawable(R.drawable.actionbar_camera_icon);
			// sortModel.setDraw(R.drawable.actionbar_camera_icon);
			sortModel.setDraw(myImage);

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	private void loadData() {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("category_id", "0");
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
					java.lang.reflect.Type type = new TypeToken<Car>() {
					}.getType();
					car = gson.fromJson(result, type);
					Log.e("CardModelListActivity", "jsonObj" + result);
					if (null != car.data) {

						for (int i = 0; i < car.data.size(); i++) {
							for (int j = 0; j < car.data.get(i).children.size(); j++) {

								SortModel sortModel = new SortModel();
								sortModel.setName(car.data.get(i).children.get(j).category_name);
								String pinyin = characterParser.getSelling(car.data.get(i).children.get(j).category_name);
								String sortString = pinyin.substring(0, 1).toUpperCase();
								if (sortString.matches("[A-Z]")) {
									sortModel.setSortLetters(sortString.toUpperCase());
								} else {
									sortModel.setSortLetters("#");
								}
								sortModel.setImg(car.data.get(i).children.get(j).category_img);
								sortModel.setId(car.data.get(i).children.get(j).category_id);
								SourceDateList.add(sortModel);

							}

						}
					}
					Collections.sort(SourceDateList, pinyinComparator);
					adapter = new SortAdapter(CarCategoryActivity.this, SourceDateList);
					sortListView.setAdapter(adapter);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(CarCategoryActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CarCategoryActivity.this, ShopLoginActivity.class);
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

	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

	private void showPopWindow(View view, String id) {
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		popupWindowCar = new PopupWindow(popView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		popupWindowCar.setOutsideTouchable(true);
		popupWindowCar.setAnimationStyle(android.R.style.Animation_Dialog);
		popupWindowCar.update();
		popupWindowCar.setTouchable(true);
		popupWindowCar.setFocusable(true);
		popupWindowCar.setTouchable(true);
		popupWindowCar.setFocusable(true);
		popupWindowCar.setOutsideTouchable(true);
		ImageView delImg = (ImageView) popView.findViewById(R.id.imageView1_del);
		ColorDrawable cd = new ColorDrawable(0xd00000);
		popupWindowCar.setBackgroundDrawable(cd);
		popupWindowCar.setAnimationStyle(R.style.ActionSheetDialogAnimation);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.95f;
		getWindow().setAttributes(lp);

		popupWindowCar.setWidth(manager.getDefaultDisplay().getWidth() * 86 / 100);
		if (!popupWindowCar.isShowing()) {

			popupWindowCar.showAsDropDown(titleBar, Px2DpUtils.dip2px(CarCategoryActivity.this, 150), 0);
			carSeriesLv.setAdapter(null);
			loadCarSeries(id);
		}
		delImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupWindowCar.dismiss();
				carSeriesLv.setAdapter(null);
				carNameItem.clear();
				carTitleItem.clear();

			}
		});
		popupWindowCar.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
				carSeriesLv.setAdapter(null);
				carNameItem.clear();
				carTitleItem.clear();
			}
		});

	}

	private void loadCarSeries(String id) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("category_id", id);
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
					java.lang.reflect.Type type = new TypeToken<Car>() {
					}.getType();
					car = gson.fromJson(result, type);

					Log.e("CarCategoryActivity", "car" + car);
					if (0 == car.data.size()) {
						carSeriesLv.setAdapter(null);
						return;
					} else {
						for (int i = 0; i < car.data.size(); i++) {

							for (int j = 0; j < car.data.get(i).children.size(); j++) {

								StringBuffer sb = new StringBuffer(car.data.get(i).children.get(j).category_name);
								StringBuffer sbtitle = new StringBuffer(car.data.get(i).header);
								StringBuffer sbImg = new StringBuffer(car.data.get(i).children.get(j).category_img);
								StringBuffer sbId = new StringBuffer(car.data.get(i).children.get(j).category_id);
								carIdItem = new ArrayList<>();
								carNameItem = new ArrayList<>();
								carTitleItem = new ArrayList<>();
								carImgItem = new ArrayList<>();
								carIdItem.add(sbId.toString());
								carTitleItem.add(sbtitle.toString());
								carNameItem.add(sb.toString());
								carImgItem.add(sbImg.toString());
								sb = null;
								sbtitle = null;
								sbImg = null;
								sbId = null;
							}

							carSeriesAdapter = new CarSeriesAdapter(CarCategoryActivity.this, carNameItem, carTitleItem, carImgItem, carIdItem, bundle);
							carSeriesLv.setAdapter(carSeriesAdapter);

						}

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(CarCategoryActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CarCategoryActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
