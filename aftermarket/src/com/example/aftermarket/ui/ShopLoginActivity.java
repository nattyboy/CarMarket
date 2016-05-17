package com.example.aftermarket.ui;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.activity.MainActivity;
import com.easemob.chatuidemo.activity.RegisterActivity;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.HanziToPinyin;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.CarUser;
import com.example.aftermarket.bean.UserLoginInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.util.MD5Utils;
import com.example.aftermarket.utils.MD5;
import com.example.aftermarket.utils.MobileNumPattern;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class ShopLoginActivity extends Activity {

	private HttpUtils httpUtils;
	private EditText userNameEditText;
	private EditText passWordEditText;
	private TextView tv;
	private static final String USER_NAME = "user_name";
	private static final String PASSWORD = "password";
	private UserLoginInfo userLoginInfo;
	private final String REQUEST_HEADER = "login";
	private CarUser user;
	private String currentUsername;
	private String currentPassword;
	public static final int REQUEST_CODE_SETNICK = 1;
	private boolean progressShow;
	private boolean autoLogin = false;
	private EditText usernameEditText;
	private EditText passwordEditText;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		userLoginInfo = new UserLoginInfo(this);
		// 如果用户名密码都有，直接进入主页面
		setContentView(R.layout.login_activity);
		userNameEditText = (EditText) findViewById(R.id.login_username_login);
		passWordEditText = (EditText) findViewById(R.id.login_password_login);
		tv = (TextView) findViewById(R.id.register_now_now);
		tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		userNameEditText.setText(userLoginInfo.getStringInfo(USER_NAME));
		passWordEditText.setText(userLoginInfo.getStringInfo(PASSWORD));
		findViewById(R.id.register_now_now).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopLoginActivity.this, ShopRegisterActivity.class);
				startActivity(intent);
			}
		});
		findViewById(R.id.shop_foget_password).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShopLoginActivity.this, ForgetPassWordActivity.class);
				startActivity(intent);
			}
		});
	}

	public void loginOnclick(View v) throws NoSuchAlgorithmException {
		if (TextUtils.isEmpty(userNameEditText.getText())) {
			Toast.makeText(ShopLoginActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
			return;
		} else {

		}

		if (TextUtils.isEmpty(passWordEditText.getText())) {
			Toast.makeText(ShopLoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
			return;
		} else {
		}
		final AlertDialog alertDialog = new AlertDialog.Builder(ShopLoginActivity.this).create();
		alertDialog.show();
		alertDialog.setCanceledOnTouchOutside(false);
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.login_progress);
		httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		params.addBodyParameter("mobile", userNameEditText.getText().toString());
		params.addBodyParameter("pwd", MD5Utils.getMD5(passWordEditText.getText().toString()));
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
					java.lang.reflect.Type type = new TypeToken<CarUser>() {
					}.getType();
					user = gson.fromJson(result, type);

					JSONObject data = jsonObj.getJSONObject("data");
					final DemoApplication app = (DemoApplication) getApplication();
					app.setToken(data.getString("token"));
					app.setEase_user(user.data.ease_user);
					app.setUserInfo(user.data);
					Log.e("dajia", "user.data.ease_user" + "" + user.data);

					// loadEase();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						Toast.makeText(ShopLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
						alertDialog.dismiss();
						userLoginInfo.setUserInfo(USER_NAME, userNameEditText.getText().toString());
						userLoginInfo.setUserInfo(PASSWORD, passWordEditText.getText().toString());
						login();
						Intent intent = new Intent(ShopLoginActivity.this, HomeActivity.class);
						startActivity(intent);
						finish();
					} else if ("0".equals(String.valueOf(jsonObj.getInt("code")))) {
						Toast.makeText(ShopLoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
						alertDialog.dismiss();
					} else {

						Toast.makeText(ShopLoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
						alertDialog.dismiss();
					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void login() {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		currentUsername = user.data.ease_user;
		currentPassword = user.data.ease_pwd;

		Log.e("dajiayilian", "update" + currentUsername);
		Log.e("dajiayilian", "update" + currentPassword);
		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		DemoApplication.currentUserNick = currentUsername;
		EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {

				if (!progressShow) {
					return;
				}
				Log.e("dajiayilian", "onSuccesscurrentUsername" + currentPassword);
				/*
				 * // 登陆成功，保存用户名密码
				 * DemoApplication.getInstance().setUserName(currentUsername);
				 * DemoApplication.getInstance().setPassword(currentPassword);
				 */

				runOnUiThread(new Runnable() {
					public void run() {
					}
				});
				try {
					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
					// conversations in case we are auto login
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// 处理好友和群组
					processContactsAndGroups();
				} catch (Exception e) {
					e.printStackTrace();
					// 取好友或者群聊失败，不让进入主页面
					runOnUiThread(new Runnable() {
						public void run() {
							DemoApplication.getInstance().logout(null);
							Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
						}
					});
					return;
				}
				// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(DemoApplication.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}
				if (!ShopLoginActivity.this.isFinishing())
					// 进入主页面
					// startActivity(new Intent(ShopLoginActivity.this,
					// MainActivity.class));
					finish();
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				if (!progressShow) {
					return;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						// Toast.makeText(getApplicationContext(),
						// getString(R.string.Login_failed) + message,
						// Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		userNameEditText.setText(userLoginInfo.getStringInfo(USER_NAME));
		passWordEditText.setText(userLoginInfo.getStringInfo(PASSWORD));
		userNameEditText.setText(intent.getStringExtra("user_name"));
		passWordEditText.setText(null);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_SETNICK) {
				DemoApplication.currentUserNick = data.getStringExtra("edittext");
				progressShow = true;
				final ProgressDialog pd = new ProgressDialog(ShopLoginActivity.this);
				pd.setCanceledOnTouchOutside(false);
				pd.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						progressShow = false;
					}
				});
				pd.setMessage(getString(R.string.Is_landing));
				pd.show();

				final long start = System.currentTimeMillis();
				// 调用sdk登陆方法登陆聊天服务器
				EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

					@Override
					public void onSuccess() {

						if (!progressShow) {
							return;
						}

						// 登陆成功，保存用户名密码
						DemoApplication.getInstance().setUserName(currentUsername);
						DemoApplication.getInstance().setPassword(currentPassword);

						runOnUiThread(new Runnable() {
							public void run() {
								pd.setMessage(getString(R.string.list_is_for));
							}
						});
						try {
							// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
							// ** manually load all local groups and
							// conversations in case we are auto login
							EMGroupManager.getInstance().loadAllGroups();
							EMChatManager.getInstance().loadAllConversations();
							// 处理好友和群组
							processContactsAndGroups();
						} catch (Exception e) {
							e.printStackTrace();
							// 取好友或者群聊失败，不让进入主页面
							runOnUiThread(new Runnable() {
								public void run() {
									pd.dismiss();
									DemoApplication.getInstance().logout(null);
									Toast.makeText(getApplicationContext(), R.string.login_failure_failed, 1).show();
								}
							});
							return;
						}
						// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
						boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(DemoApplication.currentUserNick.trim());
						if (!updatenick) {
							Log.e("LoginActivity", "update current user nick fail");
						}
						if (!ShopLoginActivity.this.isFinishing())
							pd.dismiss();
						// 进入主页面
						startActivity(new Intent(ShopLoginActivity.this, MainActivity.class));
						finish();
					}

					@Override
					public void onProgress(int progress, String status) {
					}

					@Override
					public void onError(final int code, final String message) {
						if (!progressShow) {
							return;
						}
						runOnUiThread(new Runnable() {
							public void run() {
								pd.dismiss();
								Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message, Toast.LENGTH_SHORT).show();
							}
						});
					}
				});

			}
		}
	}

	private void processContactsAndGroups() throws EaseMobException {
		// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
		List<String> usernames = EMContactManager.getInstance().getContactUserNames();
		EMLog.d("roster", "contacts size: " + usernames.size());
		Map<String, User> userlist = new HashMap<String, User>();
		for (String username : usernames) {
			User user = new User();
			user.setUsername(username);
			setUserHearder(username, user);
			userlist.put(username, user);
		}
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);

		// 存入内存
		DemoApplication.getInstance().setContactList(userlist);
		System.out.println("----------------" + userlist.values().toString());
		// 存入db
		UserDao dao = new UserDao(ShopLoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);

		// 获取黑名单列表
		List<String> blackList = EMContactManager.getInstance().getBlackListUsernamesFromServer();
		// 保存黑名单
		EMContactManager.getInstance().saveBlackList(blackList);

		// 获取群聊列表(群聊里只有groupid和groupname等简单信息，不包含members),sdk会把群组存入到内存和db中
		EMGroupManager.getInstance().getGroupsFromServer();
	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		startActivityForResult(new Intent(this, RegisterActivity.class), 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, User user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		DemoApplication app = (DemoApplication) getApplication();
		List<Activity> activities = app.activities;
		for (Activity act : activities) {
			act.finish();// 显式结束
		}
		finish();

		return super.onKeyDown(keyCode, event);
	}*/

}
