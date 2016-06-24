package com.example.aftermarket.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.UserInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.activity.FeedBackActivity;
import com.example.aftermarket.ui.AboutUsActivity;
import com.example.aftermarket.ui.CarUserInfoActivity;
import com.example.aftermarket.ui.MyCarPortActivity;
import com.example.aftermarket.ui.MyCollectionActivity;
import com.example.aftermarket.ui.MyQuestionActivity;
import com.example.aftermarket.ui.OrderItemActivity;
import com.example.aftermarket.ui.ReceiptAddressActivity;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.example.aftermarket.views.CircleImageView;
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyCenterFragment extends Fragment {

	private View view;
	private RelativeLayout addrLayout;
	private RelativeLayout opinLayout;
	private RelativeLayout aboutLayout;
	private RelativeLayout onlineLayout;
	private RelativeLayout myCarLayout;
	private ImageView UserInfoEdit;
	private ImageView userLogo;
	private RelativeLayout toServerLayout;
	private RelativeLayout toEvaluteLayout;
	private RelativeLayout toReturnLayout;
	private RelativeLayout myCollectionLayout;
	private Activity homeActivity;
	private ImageView imageViewEdit;
	private TextView userName;
	private TextView userTel;
	private DemoApplication app;
	private LinearLayout askQuestionLayout;
	private LinearLayout relativeEdit;
	private static final String REQUEST_HEADER_GETUSERIMG = "getUserImg";
	private UserInfo userInfo;
	private TextView tel_tv;
	private LinearLayout yangll;
	private TextView yangchebi;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_mycenter, container, false);
		homeActivity = getActivity();
		yangll=(LinearLayout) view.findViewById(R.id.yangll);
		app = (DemoApplication) homeActivity.getApplication();
		myCarLayout = (RelativeLayout) view.findViewById(R.id.my_car_layout);
		UserInfoEdit = (ImageView) view.findViewById(R.id.user_logo);
		toServerLayout = (RelativeLayout) view.findViewById(R.id.to_server_layout);
		toEvaluteLayout = (RelativeLayout) view.findViewById(R.id.to_eva_layout);
		toReturnLayout = (RelativeLayout) view.findViewById(R.id.return_layout);
		addrLayout = (RelativeLayout) view.findViewById(R.id.addr_layout);
		opinLayout = (RelativeLayout) view.findViewById(R.id.opinion_layout);
		aboutLayout = (RelativeLayout) view.findViewById(R.id.about_layout);
		onlineLayout = (RelativeLayout) view.findViewById(R.id.online_layout);
		imageViewEdit = (ImageView) view.findViewById(R.id.imageView_edit);
		relativeEdit = (LinearLayout) view.findViewById(R.id.relativeLayout2_edit);
		myCollectionLayout = (RelativeLayout) view.findViewById(R.id.my_collection_ll);
		userName = (TextView) view.findViewById(R.id.user_name_mycenter);
		userLogo = (CircleImageView) view.findViewById(R.id.user_logo);
		userTel = (TextView) view.findViewById(R.id.user_tel_mycenter);
		askQuestionLayout = (LinearLayout) view.findViewById(R.id.wenda_layout);
		tel_tv=(TextView) view.findViewById(R.id.tel_tv);
		yangchebi=(TextView) view.findViewById(R.id.yangchebi);
		yangchebi.setText(app.getBalance());
		if(null!=app.getToken()){
			
		}else{
			yangll.setVisibility(View.GONE);
		}
		if(null!=app.getTelNum())
		tel_tv.setText(app.getTelNum().trim());
		askQuestionLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != app.getToken()) {
					Intent intent = new Intent(homeActivity, MyQuestionActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});
		myCollectionLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {
					Intent intent = new Intent(homeActivity, MyCollectionActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});
		myCarLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {
					Intent intent = new Intent(homeActivity, MyCarPortActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});
		UserInfoEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {
					Intent intent = new Intent(homeActivity, CarUserInfoActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});
		toServerLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {
					Intent intent = new Intent(homeActivity, OrderItemActivity.class);
					intent.putExtra("currentPage", 2);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});

		toEvaluteLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {
					Intent intent = new Intent(homeActivity, OrderItemActivity.class);
					intent.putExtra("currentPage", 3);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}
			}
		});
		toReturnLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {

					Intent intent = new Intent(homeActivity, OrderItemActivity.class);
					// 传过去一个参数，显示第几页
					intent.putExtra("currentPage", 4);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});
		addrLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {

					Intent intent = new Intent(homeActivity, ReceiptAddressActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}
			}
		});
		opinLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {

					Intent intent = new Intent(homeActivity, FeedBackActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});
		aboutLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(homeActivity, AboutUsActivity.class);
				startActivity(intent);
			}
		});

		onlineLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				final Dialog mDialog = new Dialog(homeActivity, R.style.dialog);
				View v = LayoutInflater.from(homeActivity).inflate(R.layout.online_tel, null);
				TextView textView_tel_num=(TextView) v.findViewById(R.id.textView_tel_num);
				textView_tel_num.setText("拨打电话： "+app.getTelNum().trim());
				mDialog.setContentView(v);
				mDialog.setCanceledOnTouchOutside(true);
				Window dialogWindow = mDialog.getWindow();
				dialogWindow.setGravity(Gravity.CENTER);
				WindowManager m = homeActivity.getWindowManager();
				Display d = m.getDefaultDisplay();
				WindowManager.LayoutParams p = dialogWindow.getAttributes();
				p.height = (int) (d.getHeight() * 0.15);
				p.width = (int) (d.getWidth() * 0.85);
				dialogWindow.setAttributes(p);
				mDialog.show();
				TextView cancelTv = (TextView) v.findViewById(R.id.cancel_tv);
				TextView ringTv = (TextView) v.findViewById(R.id.ring_tv);
				cancelTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mDialog.dismiss();
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
						mDialog.dismiss();
					}
				});
			}
		});
		relativeEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {

					Intent intent = new Intent(homeActivity, CarUserInfoActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});
		imageViewEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != app.getToken()) {

					Intent intent = new Intent(homeActivity, CarUserInfoActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		homeActivity = getActivity();
		userName = (TextView) view.findViewById(R.id.user_name_mycenter);
		userTel = (TextView) view.findViewById(R.id.user_tel_mycenter);
		app = (DemoApplication) homeActivity.getApplicationContext();
		if (null != app.getToken()) {
			userName.setText(app.getUserInfo().user_name);
			userTel.setText(app.getUserInfo().mobile);
			Log.e("dajiayilian", "app.getUserInfo().user_img" + app.getUserInfo().user_img);
			if (null != app.getUserInfo().user_img) {
				loadImage(app.getUserInfo().user_img);
			}
			updateUserInfo();	
			Glide.with(homeActivity).load(app.getUserInfo().user_img).into(userLogo);
		}
		Log.e("Tag", "yangchebi"+app.getBalance());
		
		if(null!=app.getToken()){
			
			yangll.setVisibility(View.VISIBLE);
			yangchebi.setText(app.getBalance());
			
		}else{
			yangll.setVisibility(View.GONE);
		}
		getIconNum();
	}
	
	

	private void loadImage(String uri) {
		ImageLoader.getInstance().loadImage(uri, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				userLogo.setImageBitmap(loadedImage);
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

	private void updateUserInfo() {
		userInfo = app.getUserInfo();
		if (null != app.getUserInfo().user_img) {
			loadImage(app.getUserInfo().user_img);
		}
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_GETUSERIMG;
		RequestParams params = new RequestParams();
		params.addBodyParameter("user", userInfo.ease_user);
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(homeActivity, "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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

						userInfo.user_img = jsonObj.getJSONArray("data").getJSONObject(0).getString("img");
						app.setUserInfo(userInfo);

						loadImage(jsonObj.getJSONArray("data").getJSONObject(0).getString("img"));
						// userInfo.user_img=jsonObj.getJSONArray("data").getJSONObject(0).getString("img");


					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(homeActivity, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}
	
	private void getIconNum() {
		
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + "getUserInfo";
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(homeActivity, "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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
				Log.e("Tag", "---"+result);
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

						app.setBalance(jsonObj.getString("data"));
						
						if(null!=app.getToken()){
							
							yangll.setVisibility(View.VISIBLE);
							yangchebi.setText(app.getBalance());
							
						}else{
							yangll.setVisibility(View.GONE);
						}


					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(homeActivity, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

}
