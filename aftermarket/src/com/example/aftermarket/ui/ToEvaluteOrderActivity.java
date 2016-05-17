package com.example.aftermarket.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.SquareAdapter;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.util.BitmapUtils;
import com.example.aftermarket.views.NoScrollGridView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ToEvaluteOrderActivity extends Activity {

	private static final String REQUEST_HEADER = "evaluation";
	private SquareAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap;
	private ImageView addPicture;
	private DemoApplication app;
	private EditText inputEt;
	NoScrollGridView gridView;
	private RatingBar rt;
	private String order_id;
	private String company_logo;
	private ImageView imageView1_order;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) getApplication();
		parentView = getLayoutInflater().inflate(R.layout.evalute_order, null);
		setContentView(parentView);
		Intent intent = getIntent();
		order_id = intent.getStringExtra("order_id");
		company_logo = intent.getStringExtra("company_logo");
		rt = (RatingBar) findViewById(R.id.evalute_ratingBar_order);
		inputEt = (EditText) findViewById(R.id.editText_order);
		gridView = (NoScrollGridView) findViewById(R.id.noScrollgridview_order);
		adapter = new SquareAdapter(ToEvaluteOrderActivity.this, 0);
		gridView.setAdapter(adapter);
		imageView1_order = (ImageView) findViewById(R.id.imageView1_order);
		rt.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				// TODO Auto-generated method stub
				Log.e("rating", "+++++" + rating);
			}
		});
		loadImage(company_logo);
	}

	private void loadImage(String uri) {
		ImageLoader.getInstance().loadImage(uri, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				imageView1_order.setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String str = null;
				switch (failReason.getType()) {
				case IO_ERROR: // 文件I/O错误
					str = "Input/Output error";
					break;
				case DECODING_ERROR: // 解码错误
					str = "Image can't be decoded";
					break;
				case NETWORK_DENIED: // 网络延迟
					str = "Downloads are denied";
					break;
				case OUT_OF_MEMORY: // 内存不足
					str = "Out Of Memory error";
					break;
				case UNKNOWN: // 原因不明
					str = "Unknown error";
					break;
				}
				// Toast.makeText(homeActivity.getApplication(), str,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
			}
		});
	}

	public void submitEvalute(View view) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("content", inputEt.getText().toString());
		params.addBodyParameter("order_id", order_id);
		params.addBodyParameter("score", String.valueOf(rt.getRating()));
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
						Toast.makeText(ToEvaluteOrderActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
						finish();
					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(ToEvaluteOrderActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(ToEvaluteOrderActivity.this, ShopLoginActivity.class);
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

	public void backToOrderItem(View view) {
		finish();
	}

	public void showPopUp() {
		// 更新头像
		pop = new PopupWindow(ToEvaluteOrderActivity.this);

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
					path_path = BitmapUtils.getImgPathByUri(this, uri);
					// path_path = uri.getPath();
				}
			} else {
				Log.e("uri", "null");
			}
			adapter.addData(path_path);
		}
	}
}
