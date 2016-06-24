package com.example.aftermarket.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.SquareAdapter;
import com.example.aftermarket.bean.ServiceName;
import com.example.aftermarket.bean.ServiceNameItem;
import com.example.aftermarket.bean.UsedCar;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.util.BitmapUtils;
import com.example.aftermarket.utils.ParseFilePath;
import com.example.aftermarket.views.NoScrollGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.timepick.timeswitch.widget.DatePicker;
import com.timepick.timeswitch.widget.TimePicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class UsedCarActivity extends Activity implements OnLayoutChangeListener {

	private LinearLayout layout;
	private ListView listView;
	private PopupWindow popupWindow;
	private String title[] = { "1", "2", "3", "4", "5" };
	private String price[] = { "不限", "十万以内", "三十万", "五十万", "一百万", "五百万" };
	private String years[] = { "不限", "一年以内", "三年", "五年", "五年以上" };
	private String diantances[] = { "不限", "一万公里以内", "三万公里", "十万公里", "十万公里以上" };
	private RadioButton buyButton, sellButton;
	private RadioGroup radioGroup;
	private EditText inputEdit;
	private DemoApplication app;
	private String dim_id;
	private View mView;
	private TextView distanceTv, yearTv, priceTv, carVersionTv;
	private String category_name;
	private String category_id;
	private static int DISTANCE_ID = 1;
	private static int YEAR_ID = 2;
	private static int PRICE_ID = 3;
	private static final String TAG = "FloatWindowTest";
	WindowManager mWindowManager;
	WindowManager.LayoutParams wmParams;
	RelativeLayout mFloatLayout;
	ImageView mFloatView;
	private static final String REQUEST_HEADER = "serviceDetail";
	private static final String REQUEST_HEADER_ASK = "askQuestion";
	private List<ServiceNameItem> data;
	NoScrollGridView gridView;
	SquareAdapter adapter;
	PopupWindow popuWindowTel = null;
	View contentView1 = null;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private ImageView orderCalen;// 日历button
	private Calendar calendar;
	private DatePicker dp_test;
	private TimePicker tp_test;
	private TextView tv_ok, tv_cancel; // 确定、取消button
	private PopupWindow pw;
	private String selectDate, selectTime;
	// 选择时间与当前时间，用于判断用户选择的是否是以前的时间
	private RelativeLayout Ll_all;
	private int currentHour, currentMinute, currentDay, selectHour, selectMinute, selectDay;
	private RelativeLayout editLayout;
	// Activity最外层的Layout视图
	private View activityRootView;
	// 屏幕高度
	private int screenHeight = 0;
	// 软件盘弹起后所占高度阀值
	private int keyHeight = 0;
	private String checked="买车";
	private  EditText priceEt,distanceEt,yearEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) getApplication();
		setContentView(R.layout.used_car_activity);
		priceEt=(EditText) findViewById(R.id.car_price_et_edit);
		distanceEt=(EditText) findViewById(R.id.car_distance_et_edit);
		yearEt=(EditText) findViewById(R.id.car_year_et_edit);
		activityRootView = findViewById(R.id.all_used_id);
		// 获取屏幕高度
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;
		distanceTv = (TextView) findViewById(R.id.distance_textview);
		editLayout = (RelativeLayout) findViewById(R.id.edit_layout1);
		yearTv = (TextView) findViewById(R.id.year_textView);
		priceTv = (TextView) findViewById(R.id.price_textView);
		carVersionTv = (TextView) findViewById(R.id.car_version_textview);
		SharedPreferences sp = getSharedPreferences("LoginAfterCarActivity", MODE_PRIVATE);
		category_name=sp.getString("car_Name", "");
		category_id=sp.getString("car_Id", "");
		carVersionTv.setText(category_name);
		mView = LayoutInflater.from(this).inflate(R.layout.used_car_activity, null);
		dim_id = getIntent().getStringExtra("dim_id");
		gridView = (NoScrollGridView) findViewById(R.id.gridView_used_car);
		adapter = new SquareAdapter(UsedCarActivity.this, 0);
		gridView.setAdapter(adapter);
		inputEdit = (EditText) findViewById(R.id.edit_used_car);
		buyButton = (RadioButton) findViewById(R.id.radioBuy);
		buyButton = (RadioButton) findViewById(R.id.radioSell);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				if (checkedId != buyButton.getId()) {

					inputEdit.setVisibility(View.INVISIBLE);
					gridView.setVisibility(View.INVISIBLE);
					editLayout.setVisibility(View.INVISIBLE);
					checked="买车";

				} else {
					inputEdit.setVisibility(View.VISIBLE);
					gridView.setVisibility(View.VISIBLE);
					editLayout.setVisibility(View.VISIBLE);
					checked="卖车";
				}
			}
		});
		createFloatView();
	}

	public void backTo(View view) {
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

	public void showPopupWindow(View parent, String title[], final int id) {
		// 加载布局
		layout = (LinearLayout) LayoutInflater.from(UsedCarActivity.this).inflate(R.layout.pop_dialog, null);
		// 找到布局的控件
		listView = (ListView) layout.findViewById(R.id.lv_dialog);
		// 设置适配器
		listView.setAdapter(new ArrayAdapter<String>(UsedCarActivity.this, R.layout.pop_text, R.id.tv_text_tv, title));
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, 300, ViewGroup.LayoutParams.WRAP_CONTENT);
		// 控制键盘是否可以获得焦点
		popupWindow.setFocusable(true);
		// 设置popupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		// 获取xoff
		int xpos = manager.getDefaultDisplay().getWidth() - popupWindow.getWidth();
		// xoff,yoff基于anchor的左下角进行偏移。
		popupWindow.showAsDropDown(parent, xpos - 20, 0);
		// 监听
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

				TextView tv = (TextView) v.findViewById(R.id.tv_text_tv);
				String s = tv.getText().toString();
				Log.e("dajiayilian", "11111" + v);
				// 关闭popupWindow

				if (id == DISTANCE_ID) {

					distanceTv.setText(s);

				} else if (id == PRICE_ID) {

					priceTv.setText(s);

				} else if (id == YEAR_ID) {

					yearTv.setText(s);
				}

				popupWindow.dismiss();
				popupWindow = null;
			}
		});
	}

	public void distanceClick(View view) {
		showPopupWindow(view, diantances, DISTANCE_ID);
		// showPopupWindowTel(view);
	}

	public void priceClick(View view) {
		showPopupWindow(view, price, PRICE_ID);
	}

	public void yearClick(View view) {
		showPopupWindow(view, years, YEAR_ID);
	}

	private void uploadData(String content) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_ASK;
		
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("dim_id", dim_id);
		params.addBodyParameter("category_id", category_id);
		params.addBodyParameter("category_name", category_name);
		params.addBodyParameter("content", content);
		params.addBodyParameter("checked", checked);
		params.addBodyParameter("content[total_mileage]",distanceEt.getText().toString());
		params.addBodyParameter("content[price]", priceEt.getText().toString());
		params.addBodyParameter("content[car_age]", yearEt.getText().toString());
		Log.e("message", "content" + yearTv.getText().toString()+distanceTv.getText().toString());
		params.addBodyParameter("count", String.valueOf(adapter.imageList().size()));
		for (int i = 0; i < adapter.imageList().size(); i++) {

			params.addBodyParameter("poster" + i, new File(adapter.imageList().get(i)));
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

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						UsedCarActivity.this.finish();
					}
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(UsedCarActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(UsedCarActivity.this, ShopLoginActivity.class);
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

	private void createFloatView() {
		// 获取LayoutParams对象
		wmParams = new WindowManager.LayoutParams();

		// 获取的是LocalWindowManager对象
		mWindowManager = this.getWindowManager();
		Log.i(TAG, "mWindowManager1--->" + this.getWindowManager());
		// mWindowManager = getWindow().getWindowManager();
		Log.i(TAG, "mWindowManager2--->" + getWindow().getWindowManager());

		// 获取的是CompatModeWrapper对象
		// mWindowManager = (WindowManager)
		// getApplication().getSystemService(Context.WINDOW_SERVICE);
		Log.i(TAG, "mWindowManager3--->" + mWindowManager);
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		wmParams.format = PixelFormat.RGBA_8888;
		;
		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		wmParams.x = display.getWidth() * 70 / 100;
		wmParams.y = display.getHeight() * 70 / 100;
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = this.getLayoutInflater();// LayoutInflater.from(getApplication());

		mFloatLayout = (RelativeLayout) inflater.inflate(R.layout.float_layout, null);
		mWindowManager.addView(mFloatLayout, wmParams);
		// setContentView(R.layout.main);
		mFloatView = (ImageView) mFloatLayout.findViewById(R.id.float_id);

		Log.i(TAG, "mFloatView" + mFloatView);
		Log.i(TAG, "mFloatView--parent-->" + mFloatView.getParent());
		Log.i(TAG, "mFloatView--parent--parent-->" + mFloatView.getParent().getParent());
		// 绑定触摸移动监听
		mFloatView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				wmParams.x = (int) event.getRawX() - mFloatLayout.getWidth() / 2;
				// 25为状态栏高度
				wmParams.y = (int) event.getRawY() - mFloatLayout.getHeight() / 2 - 40;
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false;
			}
		});

		// 绑定点击监听
		mFloatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UsedCarActivity.this, SellerActivity.class);
				mWindowManager.removeView(mFloatLayout);
				intent.putExtra("service_item", "service_item");
				mFloatLayout = null;
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 添加layout大小发生改变监听器
		activityRootView.addOnLayoutChangeListener(this);
		if (null != mFloatLayout) {
			mFloatLayout.setVisibility(View.VISIBLE);
		} else {
			createFloatView();
		}

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (null != mFloatLayout) {
			mFloatLayout.setVisibility(View.GONE);
			mFloatLayout = null;
		}
	}

	public void showPopUp() {
		mFloatLayout.setVisibility(View.INVISIBLE);
		// 更新头像
		pop = new PopupWindow(UsedCarActivity.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		pop.setWidth(display.getWidth());
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		ColorDrawable cd = new ColorDrawable(0x000000);
		pop.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.6f;
		getWindow().setAttributes(lp);
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		pop.setAnimationStyle(R.style.ActionSheetDialogAnimation);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo_upload();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				image_upload();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		View parentView = getLayoutInflater().inflate(R.layout.used_car_activity, null);
		pop.showAtLocation(parentView, Gravity.CENTER_HORIZONTAL, 0, -30);
		pop.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);

				if (null != mFloatLayout) {
					mFloatLayout.setVisibility(View.VISIBLE);
				} else {
					createFloatView();
				}
			}
		});

	}

	public void image_upload() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
			startActivityForResult(intent, 2);
		} else {
			Toast.makeText(this, "存储卡不可用", Toast.LENGTH_LONG).show();
		}
	}

	public void photo_upload() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			startActivityForResult(intent, 1);
		} else {
			Toast.makeText(this, "存储卡不可用", Toast.LENGTH_LONG).show();
		}
	}

	@SuppressWarnings("deprecation")
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == 1) {
			Bundle bundle = data.getExtras();
			Bitmap bm = (Bitmap) bundle.get("data");

			FileOutputStream b = null;
			String image_path_sd = (Environment.getExternalStorageDirectory() + "/DCIM/") + System.currentTimeMillis() + ".jpg";

			try {
				b = new FileOutputStream(image_path_sd);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, b);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			adapter.addData(image_path_sd);
		}

		if (data != null && requestCode == 2) {
			Uri uri = data.getData();
			uri = ParseFilePath.getUri(this, uri);
			String path_path = "";
			if (uri != null) {
				if (uri.getPath().endsWith(".jpg")) {
					path_path = uri.getPath();
				} else {
					// path_path = BitmapUtils.getImgPathByUri(this, uri);
					path_path = uri.getPath();
				}
			} else {
				Log.e("uri", "null");
			}
			adapter.addData(path_path);
		}
	}

	public void onclickSend(View view) {

		if (null == app.getToken()) {
			Toast.makeText(UsedCarActivity.this, "请登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(UsedCarActivity.this, ShopLoginActivity.class);
			startActivity(intent);
		} else {
			
			
			if (TextUtils.isEmpty(category_name)) {
				Toast.makeText(UsedCarActivity.this, "请选择车型", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(priceEt.getText())) {
				Toast.makeText(UsedCarActivity.this, "请输入具体价格", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(yearEt.getText())) {
				Toast.makeText(UsedCarActivity.this, "请输入具体车龄", Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(distanceEt.getText())) {
				Toast.makeText(UsedCarActivity.this, "请输入具体公里数", Toast.LENGTH_SHORT).show();
				return;
			}

			StringBuffer sb = new StringBuffer("");
			distanceTv = (TextView) findViewById(R.id.distance_textview);
			yearTv = (TextView) findViewById(R.id.year_textView);
			priceTv = (TextView) findViewById(R.id.price_textView);
			carVersionTv = (TextView) findViewById(R.id.car_version_textview);
			ArrayList<Map> mapList = new ArrayList<>();
			UsedCar usedCar = new UsedCar();
			Map map = new HashMap();
			if (null != carVersionTv.getText()) {
				sb.append(carVersionTv.getText());
			}
			if (null != distanceTv.getText()) {
				sb.append(distanceTv.getText());
				
				usedCar.total_mileage = distanceTv.getText().toString();
				
				map.put("total_mileage", distanceTv.getText().toString());
			}
			if (null != priceTv.getText()) {
				sb.append(priceTv.getText());
				usedCar.price = priceTv.getText().toString();
				map.put("price",  priceTv.getText().toString());
			}
			if (null != yearTv.getText()) {
				sb.append(yearTv.getText());
				map.put("car_age ", yearTv.getText().toString());
				usedCar.car_age = yearTv.getText().toString();
			}

			Gson gson = new Gson();

			/*String jsonString = gson.toJson(usedCar);
			Log.e("map", "" +mapList.toString());*/
			Log.e("map", "" +map.toString());
			uploadData(map.toString());
		}
	}

	public void carCatagoryClick(View view) {
		if (null != app.getToken()) {
			Intent intent = new Intent(UsedCarActivity.this, CarCategoryActivity.class);
			intent.putExtra("choose_car_used", "choose_car_used");
			startActivity(intent);
		} else {
			Toast.makeText(UsedCarActivity.this, "请登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(UsedCarActivity.this, ShopLoginActivity.class);
			startActivity(intent);
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		carVersionTv = (TextView) findViewById(R.id.car_version_textview);
		if (null != intent.getStringExtra("carName")) {
			carVersionTv.setText(intent.getStringExtra("carName"));
			category_id = intent.getStringExtra("carId");
			category_name = intent.getStringExtra("carName");
			Log.e("carName", "car_category_name_tyre" + category_id);

		}

	}

	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		// TODO Auto-generated method stub

		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

			// Toast.makeText(BeautifulActivity.this,
			// "监听到软键盘弹起...",Toast.LENGTH_SHORT).show();
			mFloatLayout.setVisibility(View.INVISIBLE);
			findViewById(R.id.send_used).setVisibility(View.GONE);

		} else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {

			// Toast.makeText(BeautifulActivity.this,
			// "监听到软件盘关闭...",Toast.LENGTH_SHORT).show();
			mFloatLayout.setVisibility(View.VISIBLE);
			findViewById(R.id.send_used).setVisibility(View.VISIBLE);
		}
	}

}
