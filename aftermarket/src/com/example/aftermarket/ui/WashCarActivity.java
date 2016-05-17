package com.example.aftermarket.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.SellerListAdapter;
import com.example.aftermarket.adpter.SquareAdapter;
import com.example.aftermarket.adpter.SquareAdapterGridItem;
import com.example.aftermarket.bean.Home;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.bean.ServiceName;
import com.example.aftermarket.bean.ServiceNameItem;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.util.Bimp;
import com.example.aftermarket.photo.util.BitmapUtils;
import com.example.aftermarket.views.ActionSheetDialog;
import com.example.aftermarket.views.NoScrollGridView;
import com.example.time.view.datepicker.DatePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class WashCarActivity extends Activity implements IXListViewListener {
	/** 添加图片的标记(这个字段的内容，将会添加 到imagPath) */
	public static String ADD_TAG = "add";
	TextView washKind;
	TextView washText;
	private TextView washContext;
	NoScrollGridView gridView;
	SquareAdapter adapter;
	SquareAdapterGridItem adapterName;
	NoScrollGridView gridViewName;
	private String dim_id;

	WindowManager mWindowManager;
	WindowManager.LayoutParams wmParams;
	RelativeLayout mFloatLayout;
	ImageView mFloatView;
	private TextView myCarTv;
	private DemoApplication app;
	private String category_id;
	private String category_name;
	private RadioButton selfButton, gotowashButton, cometoButton;
	private RadioGroup radioGroup;
	private ImageView washServiceImg;
	private String serviceItem = "自助洗车";
	private Button chooseButton;
	private int serviceType = 0;
	PopupWindow popuWindowTel = null;
	View contentView1 = null;
	private static final String REQUEST_HEADER_SELF = "selfWashList";
	private static final String REQUEST_HEADER_WASH = "merchantList";
	private XListView sellListView;
	private Seller seller;
	private ArrayList<SellerInfo> sellerInfoList = new ArrayList();
	private ArrayList<Seller> sellerItems = new ArrayList();
	private SellerListAdapter adpter;
	private Handler mHandler;
	LocationManager loctionManager;
	String contextService = Context.LOCATION_SERVICE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		app = (DemoApplication) getApplication();
		setContentView(R.layout.activity_wash_car);
		sellListView = (XListView) findViewById(R.id.seller_listview_wash);
		sellListView.setXListViewListener(this);
		sellListView.setPullLoadEnable(false);
		mHandler = new Handler();
		LayoutInflater.from(this);

		dim_id = getIntent().getStringExtra("dim_id");
		washKind = (TextView) findViewById(R.id.wash_kind);
		washText = (TextView) findViewById(R.id.wash_text);
		washContext = (TextView) findViewById(R.id.wash_context);
		radioGroup = (RadioGroup) findViewById(R.id.service_radiogroup);
		selfButton = (RadioButton) findViewById(R.id.zizhuxiche);
		gotowashButton = (RadioButton) findViewById(R.id.daodianxiche);
		cometoButton = (RadioButton) findViewById(R.id.shangmenxiche);
		washServiceImg = (ImageView) findViewById(R.id.wash_servce_img);
		chooseButton = (Button) findViewById(R.id.choose_button);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {

				if (checkedId == selfButton.getId()) {
					washKind.setText("自助洗车");
					washText.setText("最实惠的洗车方式！");
					serviceItem = "自助洗车";
					chooseButton.setText("查看自助洗车点");
					serviceType = 0;
					washServiceImg.setImageResource(R.drawable.selfwashcar);
					sellListView.setEnabled(false);
					sellListView.setClickable(false);
					sellListView.setItemsCanFocus(false);
					loadDataSelf();
				} else if (checkedId == gotowashButton.getId()) {
					washKind.setText("到店洗车");
					washText.setText("到店服务更贴心！");
					serviceItem = "到店洗车";
					chooseButton.setText("选择服务商家");
					serviceType = 1;
					sellListView.setEnabled(true);
					sellListView.setClickable(true);
					sellListView.setItemsCanFocus(true);
					loadDataWash();
					washServiceImg.setImageResource(R.drawable.cheshi11);
					washContext.setText("    同时除了洗车外，还会提供一下其他诸如保养、救援、用品购买等养车类服务。");
				} else {
					washKind.setText("上门洗车");
					washText.setText("最便捷的洗车方式！");
					serviceItem = "上门洗车";
					chooseButton.setText("选择社区商家");
					serviceType = 2;
					washServiceImg.setImageResource(R.drawable.cheshi11);
					washContext.setText("    上门洗车做为社区汽车护理的基础服务项目，同时也是深入开发社区资源的敲门砖。因上门服务的特殊性，要移动方便、节水环保、运营管理、地面无污染等方面");
				}
			}
		});
		loadDataSelf();
		sellListView.setEnabled(false);
		sellListView.setClickable(false);
		sellListView.setItemsCanFocus(false);		
		

	}

	public void backToHomeFragment(View view) {

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

	public void showPopUp() {
		// 更新头像
		new ActionSheetDialog(this).builder().setCancelable(false).setCanceledOnTouchOutside(true)
				.addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						photo_upload();
					}
				}).addSheetItem("从相册中选取", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						image_upload();
					}
				}).show();
	}

	public void image_upload() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_PICK);
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

		if (TextUtils.isEmpty(serviceItem)) {
			Toast.makeText(WashCarActivity.this, "请选择服务项目", Toast.LENGTH_SHORT).show();
			return;
		}
		if (serviceType == 0) {
			Intent intent = new Intent(WashCarActivity.this, SelfWashCarSeller.class);
			startActivity(intent);
		} else {

			Intent intent = new Intent(WashCarActivity.this, SellerActivity.class);
			intent.putExtra("doorTodoorWash", "doorTodoorWash");
			startActivity(intent);
		}

	}

	public void onclickSendBt(View view) {
		if (TextUtils.isEmpty(serviceItem)) {
			Toast.makeText(WashCarActivity.this, "请选择服务项目", Toast.LENGTH_SHORT).show();
			return;
		}
		if (serviceType == 0) {
			Intent intent = new Intent(WashCarActivity.this, SelfWashCarSeller.class);
			startActivity(intent);
		} else {

			Intent intent = new Intent(WashCarActivity.this, SellerActivity.class);
			intent.putExtra("doorTodoorWash", "doorTodoorWash");
			startActivity(intent);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		myCarTv = (TextView) findViewById(R.id.mycar_tv_wash);
		if (null != intent.getStringExtra("car_version_wash")) {
			myCarTv.setText(intent.getStringExtra("car_version_wash"));
			category_id = intent.getStringExtra("car_category_id_wash");
			category_name = intent.getStringExtra("car_category_name_wash");
			Log.e("car_category_name_wash", "car_category_name_wash" + category_name);

		}

	}

	
	private void loadDataSelf() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_SELF;
		RequestParams params = new RequestParams();
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				sellListView.setVisibility(View.VISIBLE);
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Seller>() {
					}.getType();
					seller = gson.fromJson(result, type);
					if (null != seller.data) {
						sellerInfoList = (ArrayList<SellerInfo>) seller.data;
						adpter = new SellerListAdapter(WashCarActivity.this, sellerInfoList,1);
						sellListView.setAdapter(adpter);
						adpter.notifyDataSetChanged();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
					} else {

					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void loadDataWash() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_WASH;
		RequestParams params = new RequestParams();
		params.addBodyParameter("region", "");
		params.addBodyParameter("lat", app.getmCurrentLantitude());
		params.addBodyParameter("lng", app.getmCurrentLongitude());
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("type", "1");
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				sellListView.setVisibility(View.VISIBLE);
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Seller>() {
					}.getType();
					seller = gson.fromJson(result, type);
					if (null != seller.data) {
						sellerInfoList = (ArrayList<SellerInfo>) seller.data;
						adpter = new SellerListAdapter(WashCarActivity.this, sellerInfoList);
						sellListView.setAdapter(adpter);
						adpter.notifyDataSetChanged();

					} else {
						sellListView.setAdapter(null); 
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {

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
		sellListView.stopRefresh();
		sellListView.stopLoadMore();
		sellListView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
				if(serviceType==0){
					loadDataSelf();
				}else if(serviceType==1){
					loadDataWash();
				}
				
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		sellListView.stopLoadMore();
	}

}
