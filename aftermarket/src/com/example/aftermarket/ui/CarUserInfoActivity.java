package com.example.aftermarket.ui;

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.EMCallBack;
import com.easemob.chatuidemo.activity.LoginActivity;
import com.easemob.chatuidemo.activity.MainActivity;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.CarInfo;
import com.example.aftermarket.bean.UserInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.views.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class CarUserInfoActivity extends Activity {
	private PopupWindow popupWindow;
	private TextView manTv;
	private TextView womanTv;
	private TextView cancelTv;
	private RelativeLayout car_sexLayout;
	private TextView car_setSex;
	private RelativeLayout modifyPassWord;
	private ImageView iconClickIn;
	private LinearLayout ll_popup;
	private View parentView;;
	private PopupWindow pop = null;
	private GridView noScrollgridview;
	private static final int PHOTO_REQUEST_CAMERA = 1;// 照相机
	private static final int PHOTO_REQUEST_GALLERY = 2;// 相册
	private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
	private static final String REQUEST_HEADER = "uploadImage";
	private static final String REQUEST_HEADER_GETUSERIMG = "getUserImg";
	private static final String REQUEST_HEADER_MODIFYUSER = "modifyUser";
	private File tempFile;
	private CircleImageView userLogo;
	private DemoApplication app;
	private ImageView nameModify;
	private String name;
	private TextView userNameTv;
	private UserInfo userInfo;
	private TextView userTel;
	private RelativeLayout nameChangeLayout;
	private RelativeLayout iconChangeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_activity);
		initview();

	}

	private void initview() {

		app = (DemoApplication) getApplication();
		// init();
		iconChangeLayout = (RelativeLayout) findViewById(R.id.change_icon_layout);
		parentView = getLayoutInflater().inflate(R.layout.user_info_activity, null);
		nameChangeLayout = (RelativeLayout) findViewById(R.id.name_change_layout);
		userLogo = (CircleImageView) findViewById(R.id.user_logo_user);
		userInfo = app.getUserInfo();
		if (null != app.getUserInfo().user_img) {
			loadImage(app.getUserInfo().user_img);
		}
		modifyPassWord = (RelativeLayout) findViewById(R.id.car_modify_password_layout);
		car_setSex = (TextView) findViewById(R.id.car_setSex);
		car_sexLayout = (RelativeLayout) findViewById(R.id.car_sexLayout);
		iconClickIn = (ImageView) findViewById(R.id.icon_click_in);
		nameModify = (ImageView) findViewById(R.id.name_jinru);
		userNameTv = (TextView) findViewById(R.id.userinfo_name);
		userTel = (TextView) findViewById(R.id.user_tel_num);
		userNameTv.setText(userInfo.user_name);
		if (userInfo.sex.equals("0")) {
			car_setSex.setText("女");
		} else if (userInfo.sex.equals("1")) {
			car_setSex.setText("男");
		} else {
			car_setSex.setText(userInfo.sex);
		}

		userTel.setText(userInfo.mobile);
		updateUserInfo();
		LayoutInflater inflater = LayoutInflater.from(this);
		final View view = inflater.inflate(R.layout.sex_item, null);
		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		popupWindow.update();
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);

		popupWindow.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
		modifyPassWord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(CarUserInfoActivity.this, SetPasswordActivity.class);
				startActivity(intent);

			}
		});
		car_sexLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!popupWindow.isShowing()) {
					// popupWindow.showAsDropDown(car_sexLayout, 0, 0);
					popupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
					popupWindow.showAtLocation(car_sexLayout, Gravity.CENTER, 0, 0);
				}
				ColorDrawable cd = new ColorDrawable(0x000000);
				popupWindow.setBackgroundDrawable(cd);
				popupWindow.setAnimationStyle(R.style.ActionSheetDialogAnimation);
				// 产生背景变暗效果
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 0.4f;
				getWindow().setAttributes(lp);
				manTv = (TextView) view.findViewById(R.id.man_tv);
				womanTv = (TextView) view.findViewById(R.id.woman_tv);
				cancelTv = (TextView) view.findViewById(R.id.cancle);
				manTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (car_setSex.getText().toString().equals("男")) {
							popupWindow.dismiss();
						}
						car_setSex.setText("男");
						modifyUserInfo(userInfo.user_name, "1");
						userInfo.sex = "1";
						app.setUserInfo(userInfo);
						popupWindow.dismiss();

					}
				});

				womanTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (car_setSex.getText().toString().equals("女")) {
							popupWindow.dismiss();
						}
						car_setSex.setText("女");
						modifyUserInfo(userInfo.user_name, "0");
						userInfo.sex = "0";
						app.setUserInfo(userInfo);
						popupWindow.dismiss();
					}
				});
				cancelTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						popupWindow.dismiss();
					}
				});
			}
		});
		iconChangeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				init();
				ll_popup.startAnimation(AnimationUtils.loadAnimation(CarUserInfoActivity.this, R.anim.activity_translate_in));
				pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			}
		});

		nameChangeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final View view = getLayoutInflater().inflate(R.layout.change_name_popupwindows, null);
				final PopupWindow popupWindowName = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				popupWindowName.update();
				popupWindowName.setTouchable(true);
				popupWindowName.setFocusable(true);
				popupWindowName.setOutsideTouchable(true);
				ColorDrawable cd = new ColorDrawable(0x000000);
				popupWindowName.setBackgroundDrawable(cd);
				popupWindowName.setAnimationStyle(R.style.ActionSheetDialogAnimation);
				// 产生背景变暗效果
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 0.4f;
				getWindow().setAttributes(lp);
				Button cancel = (Button) view.findViewById(R.id.item_popupwindows_cancel_name);
				Button submit = (Button) view.findViewById(R.id.item_popupwindows_submit_name);
				final EditText modifyNameEt = (EditText) view.findViewById(R.id.modify_name_et);
				if (!popupWindowName.isShowing()) {
					popupWindowName.showAtLocation(view, Gravity.CENTER, 0, 0);
				}
				cancel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						popupWindowName.dismiss();
					}
				});
				submit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						name = modifyNameEt.getText().toString();
						if (!TextUtils.isEmpty(name.trim())) {
							userNameTv.setText(name);
							modifyUserInfo(name, userInfo.sex);
							userInfo.user_name = name;
							app.setUserInfo(userInfo);
						}
						popupWindowName.dismiss();
					}
				});
				popupWindowName.setOnDismissListener(new OnDismissListener() {

					// 在dismiss中恢复透明度
					public void onDismiss() {
						WindowManager.LayoutParams lp = getWindow().getAttributes();
						lp.alpha = 1f;
						getWindow().setAttributes(lp);
					}
				});
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	private void init() {
		pop = new PopupWindow(CarUserInfoActivity.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
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
				camera();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gallery();
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
		pop.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);

			}
		});
	}

	public void backToMycenter(View v) {

		/*
		 * Intent intent = new Intent(CarUserInfoActivity.this,
		 * HomeActivity.class);
		 * intent.putExtra(ConstantClass.MYCenter_TOKEN_NAME,
		 * ConstantClass.MyCenter_TOKEN_VALUE); startActivity(intent);
		 */
		finish();
	}

	public void gallery() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	public void camera() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		if (hasSdcard()) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
		}
		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				Uri uri = data.getData();

				String[] proj = { MediaStore.Images.Media.DATA };

				Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);

				int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

				actualimagecursor.moveToFirst();

				String img_path = actualimagecursor.getString(actual_image_column_index);

				File file = new File(img_path);
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), opt);
				int picWidth = opt.outWidth;
				int picHeight = opt.outHeight;
				opt.inSampleSize = 4;
				opt.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(file.getPath(), opt);
				userLogo = (CircleImageView) findViewById(R.id.user_logo_user);
				upload(file.getPath());
				userLogo.setImageBitmap(bitmap);
			}
		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			if (hasSdcard()) {
				tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
				Log.e("File camera", "...." + tempFile);
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeFile(tempFile.getPath(), opt);
				int picWidth = opt.outWidth;
				int picHeight = opt.outHeight;
				opt.inSampleSize = 4;
				opt.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(tempFile.getPath(), opt);
				userLogo = (CircleImageView) findViewById(R.id.user_logo_user);
				upload(tempFile.getPath());
				userLogo.setImageBitmap(bitmap);
			} else {
				Toast.makeText(CarUserInfoActivity.this, "无内存卡", 0).show();
			}

		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	private void upload(String imagePath) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("poster", new File(imagePath));
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

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(CarUserInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CarUserInfoActivity.this, ShopLoginActivity.class);
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

	private void updateUserInfo() {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_GETUSERIMG;
		RequestParams params = new RequestParams();
		params.addBodyParameter("user", userInfo.ease_user);
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

						// userInfo.user_img=jsonObj.getJSONArray("data").getJSONObject(0).getString("img");
						Log.e("dajiayilian", "22222" + jsonObj.getJSONArray("data").getJSONObject(0).getString("img"));

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(CarUserInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CarUserInfoActivity.this, ShopLoginActivity.class);
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

	private void modifyUserInfo(String userName, String sex) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_MODIFYUSER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("user_name", userName);
		params.addBodyParameter("sex", sex);
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
					java.lang.reflect.Type type = new TypeToken<CarInfo>() {
					}.getType();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(CarUserInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CarUserInfoActivity.this, ShopLoginActivity.class);
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

	private void loadImage(String uri) {
		ImageLoader.getInstance().loadImage(uri, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				userLogo = (CircleImageView) findViewById(R.id.user_logo_user);
				Log.e("dajiayilian", "loadedImage" + loadedImage + "userLogo" + userLogo);
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
				// Toast.makeText(getApplication(), str,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
			}
		});
	}

	public void quitLogin(View v) {
		logout();
	}

	void logout() {
		final ProgressDialog pd = new ProgressDialog(CarUserInfoActivity.this);
		String st = getResources().getString(R.string.Are_logged_out);
		pd.setMessage(st);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		DemoApplication.getInstance().logout(new EMCallBack() {

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						// 重新显示登陆页面
						DemoApplication app = (DemoApplication) getApplication();
						List<Activity> activities = app.activities;
						for (Activity act : activities) {
							act.finish();// 显式结束
						}
						try {
							Thread.sleep(1000);
							finish();
							System.exit(0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {

			}
		});
	}

}
