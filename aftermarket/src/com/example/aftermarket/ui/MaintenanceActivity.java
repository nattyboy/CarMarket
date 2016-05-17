package com.example.aftermarket.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.bitlet.weupnp.Main;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.SquareAdapter;
import com.example.aftermarket.adpter.SquareAdapterGridItem;
import com.example.aftermarket.bean.ServiceName;
import com.example.aftermarket.bean.ServiceNameItem;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.util.BitmapUtils;
import com.example.aftermarket.utils.Date_U;
import com.example.aftermarket.utils.ParseFilePath;
import com.example.aftermarket.views.ActionSheetDialog;
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
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class MaintenanceActivity extends Activity implements OnLayoutChangeListener {

	/* 请求码 */
	/** 调用系统相册请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;
	/** 调用系统相机请求码 */
	private static final int CAMERA_REQUEST_CODE = 1;
	/** 调用系统剪切的请求码 */
	private static final int RESULT_REQUEST_CODE = 2;

	/** 添加图片的标记(这个字段的内容，将会添加 到imagPath) */
	public static String ADD_TAG = "add";

	TextView time;
	NoScrollGridView gridView;
	SquareAdapter adapter;
	SquareAdapterGridItem adapterName;
	NoScrollGridView gridViewName;
	private List<ServiceNameItem> data;
	private static final String REQUEST_HEADER = "serviceDetail";
	private String dim_id;
	private static final String TAG = "FloatWindowTest";
	private static final String REQUEST_HEADER_ASK = "askQuestion";
	WindowManager mWindowManager;
	WindowManager.LayoutParams wmParams;
	RelativeLayout mFloatLayout;
	ImageView mFloatView;
	private TextView myCarTv;
	private String category_name;
	private String category_id;
	private EditText editMaintenance;
	private RelativeLayout chooseCarLayout;
	private DemoApplication app;
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
	private RelativeLayout timeLayout;
	private	TextView timeTv;
	private int currentHour, currentMinute, currentDay, selectHour, selectMinute, selectDay;
	// Activity最外层的Layout视图
		private View activityRootView;
		// 屏幕高度
		private int screenHeight = 0;
		// 软件盘弹起后所占高度阀值
		private int keyHeight = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		app = (DemoApplication) getApplication();
		setContentView(R.layout.maintenance_activity);
		activityRootView = findViewById(R.id.maiten_all_id);
		// 获取屏幕高度
		screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		// 阀值设置为屏幕高度的1/3
		keyHeight = screenHeight / 3;
		timeLayout=(RelativeLayout) findViewById(R.id.time_layout_main);
		timeTv=(TextView) findViewById(R.id.time_tv_maintence);
		Ll_all=(RelativeLayout) findViewById(R.id.maiten_all_id);
		calendar = Calendar.getInstance();
		myCarTv = (TextView) findViewById(R.id.mycar_tv_maintenance);
		editMaintenance = (EditText) findViewById(R.id.edit_maintenance);
		dim_id = getIntent().getStringExtra("dim_id");
		time = (TextView) findViewById(R.id.time_maintenance);
		gridView = (NoScrollGridView) findViewById(R.id.gridView_maintenance);
		adapter = new SquareAdapter(MaintenanceActivity.this, 0);
		gridView.setAdapter(adapter);
		gridViewName = (NoScrollGridView) findViewById(R.id.gridView_maintenance_name);
		adapterName = new SquareAdapterGridItem(this, 0); 
		gridViewName.setAdapter(adapterName);
		chooseCarLayout=(RelativeLayout) findViewById(R.id.mycar_layout_maintenance);
		timeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				View view = View.inflate(MaintenanceActivity.this, R.layout.dialog_select_time, null);
				selectDate = calendar.get(Calendar.YEAR) + "年" +  (((calendar.get(Calendar.MONTH)+1) < 10) ? ("0" + (calendar.get(Calendar.MONTH)+1)) : (calendar.get(Calendar.MONTH)+1))+ "月" + ((calendar.get(Calendar.DAY_OF_MONTH) < 10) ? ("0" + calendar.get(Calendar.DAY_OF_MONTH)) : calendar.get(Calendar.DAY_OF_MONTH)) + "日";
				// 选择时间与当前时间的初始化，用于判断用户选择的是否是以前的时间，如果是，弹出toss提示不能选择过去的时间
				selectDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
				Log.e("time", "2"+calendar.get(Calendar.MONTH));
				selectMinute = currentMinute = calendar.get(Calendar.MINUTE);
				selectHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY);
				selectTime =  ((currentHour < 10) ? ("0" + currentHour) : currentHour)+ "时" + ((currentMinute < 10) ? ("0" + currentMinute) : currentMinute) + "分";
				dp_test = (DatePicker) view.findViewById(R.id.dp_test);
				tp_test = (TimePicker) view.findViewById(R.id.tp_test);
				tv_ok = (TextView) view.findViewById(R.id.tv_ok);
				tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
				// 设置滑动改变监听器
				dp_test.setOnChangeListener(dp_onchanghelistener);
				tp_test.setOnChangeListener(tp_onchanghelistener);
				pw = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
				// //设置这2个使得点击pop以外区域可以去除pop
				// pw.setOutsideTouchable(true);
				// pw.setBackgroundDrawable(new BitmapDrawable());

				// 出现在布局底端
				ColorDrawable cd = new ColorDrawable(0x000000);
				pw.setBackgroundDrawable(cd);
				pw.setAnimationStyle(R.style.ActionSheetDialogAnimation);
				// 产生背景变暗效果
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 0.4f;
				getWindow().setAttributes(lp);
				pw.showAtLocation(Ll_all, Gravity.BOTTOM, 0, 0);
				mFloatLayout.setVisibility(View.INVISIBLE);

				// 点击确定
				tv_ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (selectDay == currentDay) { // 在当前日期情况下可能出现选中过去时间的情况
							if (selectHour < currentHour) {
								Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重新选择", 0).show();
							} else if ((selectHour == currentHour) && (selectMinute < currentMinute)) {
								Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重新选择", 0).show();
							} else {
								timeTv.setText(selectDate + selectTime);
								mFloatLayout.setVisibility(View.VISIBLE);
								pw.dismiss();
							}
						} else {
							timeTv.setText(selectDate + selectTime);
							mFloatLayout.setVisibility(View.VISIBLE);
							pw.dismiss();
						}
					}
				});
				// 点击取消
				tv_cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						mFloatLayout.setVisibility(View.VISIBLE);
						pw.dismiss();
					}
				});

				pw.setOnDismissListener(new OnDismissListener() {

					// 在dismiss中恢复透明度
					public void onDismiss() {
						WindowManager.LayoutParams lp = getWindow().getAttributes();
						lp.alpha = 1f;
						getWindow().setAttributes(lp);
						mFloatLayout.setVisibility(View.VISIBLE);
					}
				});

			}
		});
		// createFloatView();
		loadData();

	}

	// listeners
		DatePicker.OnChangeListener dp_onchanghelistener = new DatePicker.OnChangeListener() {
			@Override
			public void onChange(int year, int month, int day, int day_of_week) {
				selectDay = day;
				//selectDate = year + "年" + month + "月" + day + "日" + DatePicker.getDayOfWeekCN(day_of_week);
				if(month<=calendar.get(Calendar.MONTH))
					month=calendar.get(Calendar.MONTH);
				selectDate = year + "年" + ((month < 10) ? ("0" + month) : month) + "月" +  ((day < 10) ? ("0" + day) : day) + "日";
				Log.e("time", "1"+selectDate);
			}
		};
		TimePicker.OnChangeListener tp_onchanghelistener = new TimePicker.OnChangeListener() {
			@Override
			public void onChange(int hour, int minute) {
				selectTime =   ((hour < 10) ? ("0" + hour) : hour)+ "时" + ((minute < 10) ? ("0" + minute) : minute) + "分";
				selectHour = hour;
				selectMinute = minute;
			}
		};

	public void backToHomeFragment(View view) {
		finish();
	}
	

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		myCarTv = (TextView) findViewById(R.id.mycar_tv_maintenance);
		if (null != intent.getStringExtra("car_version_maintenance")) {
			myCarTv.setText(intent.getStringExtra("car_category_name_maintenance"));
			category_id = intent.getStringExtra("car_category_id_maintenance");
			category_name = intent.getStringExtra("car_category_name_maintenance");
			Log.e("car_category_name_wash", "car_category_name_maintenance" + category_name);

		}

	}
	
	public void onclickSend(View view) {

		if (TextUtils.isEmpty(category_name)) {
			Toast.makeText(MaintenanceActivity.this, "请选择车型", Toast.LENGTH_SHORT).show();
			return;
		}
		
		/*if (TextUtils.isEmpty(adapterName.returnCheckTextList())) {
			Toast.makeText(MaintenanceActivity.this, "请选择服务项目", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(timeTv.getText())){
			Toast.makeText(MaintenanceActivity.this, "请选择预约时间", Toast.LENGTH_SHORT).show();
			return;
		}*/
		if (TextUtils.isEmpty( editMaintenance.getText())) {
			Toast.makeText(MaintenanceActivity.this, "请输入具体需求", Toast.LENGTH_SHORT).show();
			return;
		}

		uploadData(); 
		//sendBt.setClickable(false);
	}
	private void uploadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_ASK;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("category_id", category_id);
		params.addBodyParameter("category_name", category_name);
		params.addBodyParameter("dim_id", dim_id);
		params.addBodyParameter("checked", adapterName.returnCheckTextList());
		params.addBodyParameter("content", editMaintenance.getText().toString());
		if (TextUtils.isEmpty(timeTv.getText())) {

		} else {
			params.addBodyParameter("order_time", new Date_U().data(timeTv.getText().toString() + "00秒"));
		}
		
		params.addBodyParameter("count", String.valueOf(adapter.imageList().size()));
		for (int i = 0; i < adapter.imageList().size(); i++) {

			params.addBodyParameter("poster" + i, new File(adapter.imageList().get(i)));
		}
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
				//sendBt.setClickable(true);
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
						finish();
					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(MaintenanceActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(MaintenanceActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {
						//sendBt.setClickable(true);
					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
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
					//path_path = BitmapUtils.getImgPathByUri(this, uri);
					path_path = uri.getPath();
				}
			} else {
				Log.e("uri", "null");
			}
			adapter.addData(path_path);
		}
	}

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("dim_id", dim_id);
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

					java.lang.reflect.Type type = new TypeToken<ServiceName>() {
					}.getType();
					ServiceName serviceName = gson.fromJson(result, type);
					data = serviceName.data;
					if (null != data) {

						for (int i = 0; i < data.size(); i++) {

							adapterName.addData(data.get(i).member_name);
						}

					}

					Log.e("hello", "+++++" + data);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(MaintenanceActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(MaintenanceActivity.this, ShopLoginActivity.class);
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

	public void showPopUp() {
		mFloatLayout.setVisibility(View.INVISIBLE);
		// 更新头像
		pop = new PopupWindow(MaintenanceActivity.this);

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
		View parentView = getLayoutInflater().inflate(R.layout.maintenance_activity, null);
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
		wmParams.x = display.getWidth() * 70/ 100;
		wmParams.y = display.getHeight() * 70/ 100;
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
		// 绑定点击监听
		mFloatView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MaintenanceActivity.this, SellerActivity.class);
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
		if(null!=mFloatLayout){
			mFloatLayout.setVisibility(View.GONE);
			mFloatLayout=null;
		}
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
		popuWindowTel.setAnimationStyle(R.style.ActionSheetDialogAnimation);
		popuWindowTel.update();
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
				String mobile = "4000000000";
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
	public void changeMyCar(View view) {

		//Toast.makeText(BeautifulActivity.this, "美容", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(MaintenanceActivity.this, MyCarPortActivity.class);
		intent.putExtra("choose_car_maintenance", "choose_car_maintenance");
		startActivity(intent);
	}
	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
		// TODO Auto-generated method stub

		if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {

			// Toast.makeText(BeautifulActivity.this,
			// "监听到软键盘弹起...",Toast.LENGTH_SHORT).show();
			mFloatLayout.setVisibility(View.INVISIBLE);
			findViewById(R.id.up_load_maintenance).setVisibility(View.VISIBLE);
			findViewById(R.id.send_mainence).setVisibility(View.GONE);
			findViewById(R.id.title_maintence).setVisibility(View.VISIBLE);

		} else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {

			// Toast.makeText(BeautifulActivity.this,
			// "监听到软件盘关闭...",Toast.LENGTH_SHORT).show();
			mFloatLayout.setVisibility(View.VISIBLE);
			findViewById(R.id.up_load_maintenance).setVisibility(View.VISIBLE);
			findViewById(R.id.send_mainence).setVisibility(View.VISIBLE);
			findViewById(R.id.title_maintence).setVisibility(View.VISIBLE);
		}
	}
}
