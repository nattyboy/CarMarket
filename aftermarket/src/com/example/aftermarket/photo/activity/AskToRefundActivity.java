package com.example.aftermarket.photo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.AddCarAdapter;
import com.example.aftermarket.adpter.SquareAdapter;
import com.example.aftermarket.bean.CarInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.util.Bimp;
import com.example.aftermarket.photo.util.BitmapUtils;
import com.example.aftermarket.photo.util.FileUtils;
import com.example.aftermarket.photo.util.ImageItem;
import com.example.aftermarket.photo.util.PublicWay;
import com.example.aftermarket.photo.util.Res;
import com.example.aftermarket.ui.AskRefundActivity;
import com.example.aftermarket.ui.InputOrderActivity;
import com.example.aftermarket.ui.MyCarPortActivity;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.example.aftermarket.ui.UsedCarActivity;
import com.example.aftermarket.views.NoScrollGridView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class AskToRefundActivity extends Activity {

	private static final String REQUEST_HEADER = "backOrder";
	private NoScrollGridView noScrollgridview;
	private SquareAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap;
	private ImageView addPicture;
	private DemoApplication app;
	private EditText inputEt;
	private String orderId = null;
	private String orderAmount = null;
	private LinearLayout layout;
	private ListView listView;
	private PopupWindow popupWindow;
	private String title[] = { "买错了", "不想买了", "其他" };
	private RelativeLayout refundReason;
	private TextView reasonTv;
	private EditText modify_name_et;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		app = (DemoApplication) getApplication();

		bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(R.layout.ask_to_refund_activity_layout, null);
		setContentView(parentView);
		modify_name_et=(EditText) findViewById(R.id.modify_name_et);
		inputEt = (EditText) findViewById(R.id.input_opinion_ask);
		refundReason = (RelativeLayout) findViewById(R.id.ask_reason);
		Intent intent = getIntent();
		orderId = intent.getStringExtra("order_id");
		orderAmount = intent.getStringExtra("back_money");
		reasonTv = (TextView) findViewById(R.id.textView2_reason);
		noScrollgridview = (NoScrollGridView) findViewById(R.id.noScrollgridview_ask);
		modify_name_et.setHint("最多￥"+orderAmount);
		adapter = new SquareAdapter(AskToRefundActivity.this, 0);
		noScrollgridview.setAdapter(adapter);
		refundReason.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showPopupWindow(v, title);
			}
		});
	}

	public void submitRefund(View v) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		if(null!=modify_name_et.getText()){
			
			if (modify_name_et.getText().toString().matches("[0-9]+\\.?[0-9]*")) {

			} else {
				Toast.makeText(AskToRefundActivity.this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(Float.parseFloat(orderAmount)<Float.parseFloat(modify_name_et.getText().toString())){
				Toast.makeText(AskToRefundActivity.this, "退款金额不能大于支付金额", Toast.LENGTH_SHORT).show();
				return;
			}else{
				orderAmount=modify_name_et.getText().toString();
			}
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("order_id", orderId);
		params.addBodyParameter("back_money", orderAmount);
		String content = "";
		if (TextUtils.isEmpty(inputEt.getText())) {

		} else {
			content = inputEt.getText().toString();
		}
		params.addBodyParameter("back_desc", content);
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
				}

				catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						finish();
					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(AskToRefundActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(AskToRefundActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void showPopUp() {
		// 更新头像
		pop = new PopupWindow(AskToRefundActivity.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		pop.setWidth(display.getWidth());
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		View parentView = getLayoutInflater().inflate(R.layout.repair_activity, null);
		pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

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
					//path_path = BitmapUtils.getImgPathByUri(this, uri);
					path_path = uri.getPath();
				}
			} else {
			}
			adapter.addData(path_path);
		}
	}

	protected void onRestart() {
		super.onRestart();
	}

	public void backToMyCenter(View v) {
		finish();
	}

	public void showPopupWindow(View parent, String title[]) {
		// 加载布局
		layout = (LinearLayout) LayoutInflater.from(AskToRefundActivity.this).inflate(R.layout.pop_dialog, null);
		// 找到布局的控件
		listView = (ListView) layout.findViewById(R.id.lv_dialog);
		// 设置适配器
		listView.setAdapter(new ArrayAdapter<String>(AskToRefundActivity.this, R.layout.pop_text, R.id.tv_text_tv, title));
		// 实例化popupWindow
		popupWindow = new PopupWindow(layout, 300, 500);
		// 控制键盘是否可以获得焦点
		popupWindow.setFocusable(true);
		// 设置popupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		// 获取xoff
		int xpos = manager.getDefaultDisplay().getWidth() - popupWindow.getWidth();
		// xoff,yoff基于anchor的左下角进行偏移。
		popupWindow.showAsDropDown(parent, xpos, 0);
		// 监听
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

				TextView tv = (TextView) v.findViewById(R.id.tv_text_tv);
				String s = tv.getText().toString();
				// 关闭popupWindow
				reasonTv.setText(s);
				popupWindow.dismiss();
				popupWindow = null;
			}
		});
	}

}
