package com.example.aftermarket.ui;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Process;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.easemob.EMCallBack;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.HomePageInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.fragment.HomePageFragment;
import com.example.aftermarket.fragment.MyCenterFragment;
import com.example.aftermarket.fragment.QuestionsFragment;
import com.example.aftermarket.fragment.SellerFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class HomeActivity extends FragmentActivity implements OnClickListener {

	private static final String REQUEST_HEADER = "home";
	// 定义Fragment页面
	private HomePageFragment homePageFragment;
	private MyCenterFragment myCenterFragment;
	private QuestionsFragment questionsFragment;
	private SellerFragment sellerFragment;
	private long mExitTime;
	// 定义布局对象
	private FrameLayout homePageFl, myCenterFl, questionsFl, sellerFl;
	// 定义图片组件对象
	private ImageView homePageIv, myCenterIv, questionsIv, sellerIv;
	// 定义按钮图片组件
	private ImageView toggleImageView, plusImageView;
	HttpUtils httpUtils;
	String resultJson;
	private DemoApplication app;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置无标题
		app = (DemoApplication) getApplication();
		setContentView(R.layout.baseactivity_layout);
		plusImageView = (ImageView) findViewById(R.id.image_more);
		initView();
		initData();
		// 初始化默认为选中点击了“首页”按钮
		clickAtBtn();
		System.out.println("onCreate");
		Log.e("dajia", "homepage--toke=" + app.getToken());
		Intent intent = getIntent();
		String sellerinfo_token = intent.getStringExtra(ConstantClass.SELLERINFO_TOKEN_NAME);
		String myCenter_token = intent.getStringExtra(ConstantClass.MYCenter_TOKEN_NAME);
		Log.e("aftercar", "" + intent.getStringExtra("sellerinfo_token"));
		if (ConstantClass.SELLERINFO_TOKEN_VALUE.equals(sellerinfo_token)) {
			clickAuthBtn();// 商家
		}
		if (ConstantClass.MyCenter_TOKEN_VALUE.equals(myCenter_token)) {
			clickMoreBtn();// 我的
		}

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		app.activities.add(this);
		Log.e("tag", "" + app.activities);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		String sellerinfo_token = intent.getStringExtra(ConstantClass.SELLERINFO_TOKEN_NAME);
		String myCenter_token = intent.getStringExtra(ConstantClass.MYCenter_TOKEN_NAME);
		Log.e("aftercar", "" + intent.getStringExtra("sellerinfo_token"));
		if (ConstantClass.SELLERINFO_TOKEN_VALUE.equals(sellerinfo_token)) {
			clickAuthBtn();// 商家
		}
		if (ConstantClass.MyCenter_TOKEN_VALUE.equals(myCenter_token)) {
			clickMoreBtn();// 我的
			Log.e("aftercar", "myCenter_token" + intent.getStringExtra("sellerinfo_token"));
		}
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		// 实例化布局对象
		homePageFl = (FrameLayout) findViewById(R.id.layout_at);
		sellerFl = (FrameLayout) findViewById(R.id.layout_auth);
		questionsFl = (FrameLayout) findViewById(R.id.layout_space);
		myCenterFl = (FrameLayout) findViewById(R.id.layout_more);

		// 实例化图片组件对象
		homePageIv = (ImageView) findViewById(R.id.image_at);
		sellerIv = (ImageView) findViewById(R.id.image_space);
		questionsIv = (ImageView) findViewById(R.id.image_space);
		myCenterIv = (ImageView) findViewById(R.id.image_more);

		// 实例化按钮图片组件
		toggleImageView = (ImageView) findViewById(R.id.toggle_btn);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 给布局对象设置监听
		homePageFl.setOnClickListener(this);
		sellerFl.setOnClickListener(this);
		questionsFl.setOnClickListener(this);
		myCenterFl.setOnClickListener(this);

		// 给按钮图片设置监听
		toggleImageView.setOnClickListener(this);
		httpUtils = new HttpUtils();
		// String url =
		// "http://jk1.cpioc.com/index.php?m=Admin&c=userApi&a=home";
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		httpUtils.send(HttpMethod.POST, url, null, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
				Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<HomePageInfo>() {
					}.getType();
					HomePageInfo homePageInfo = gson.fromJson(result, type);
					resultJson = homePageInfo.data.top.get(0).ad_url;
					Log.e("json", "" + homePageInfo.data.top.get(0).ad_url);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
					} else {
						Toast.makeText(HomeActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 点击事件
	 */

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// 点击首页按钮
		case R.id.layout_at:
			clickAtBtn();
			break;
		// 点击商家按钮
		case R.id.layout_auth:

			clickAuthBtn();
			break;
		// 点击我的空间按钮
		case R.id.layout_space:
			clickSpaceBtn();
			break;
		// 点击更多按钮
		case R.id.layout_more:
			clickMoreBtn();
			break;
		// 点击中间按钮
		case R.id.toggle_btn:
			clickToggleBtn();
			break;
		}

	}

	/**
	 * 点击了“动态”按钮
	 */
	private void clickAtBtn() {
		// 实例化Fragment页面
		homePageFragment = new HomePageFragment();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, homePageFragment);
		// 事务管理提交
		fragmentTransaction.commit();
		// 改变选中状态
		homePageFl.setSelected(true);
		homePageIv.setSelected(true);
		sellerFl.setSelected(false);
		sellerIv.setSelected(false);
		questionsFl.setSelected(false);
		questionsIv.setSelected(false);
		myCenterFl.setSelected(false);
		myCenterIv.setSelected(false);

	}

	/**
	 * 点击了“与我相关”按钮
	 */
	private void clickAuthBtn() {
		// 实例化Fragment页面
		sellerFragment = new SellerFragment();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, sellerFragment);
		// 事务管理提交
		fragmentTransaction.commit();
		/*
		 * Intent intent=new Intent(HomeActivity.this,SellerActivity.class);
		 * startActivity(intent);
		 */
		homePageFl.setSelected(false);
		homePageIv.setSelected(false);

		sellerFl.setSelected(true);
		sellerIv.setSelected(true);

		questionsFl.setSelected(false);
		questionsIv.setSelected(false);

		myCenterFl.setSelected(false);
		myCenterIv.setSelected(false);

	}

	/**
	 * 点击了“我的空间”按钮
	 */
	private void clickSpaceBtn() {
		// 实例化Fragment页面
		questionsFragment = new QuestionsFragment();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, questionsFragment);
		// 事务管理提交
		fragmentTransaction.commit();

		homePageFl.setSelected(false);
		homePageIv.setSelected(false);

		sellerFl.setSelected(false);
		sellerIv.setSelected(false);

		questionsFl.setSelected(true);
		questionsIv.setSelected(true);

		myCenterFl.setSelected(false);
		myCenterIv.setSelected(false);
	}

	/**
	 * 点击了“更多”按钮
	 */
	private void clickMoreBtn() {
		// 实例化Fragment页面
		myCenterFragment = new MyCenterFragment();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, myCenterFragment);
		// 事务管理提交
		fragmentTransaction.commit();

		homePageFl.setSelected(false);
		homePageIv.setSelected(false);

		sellerFl.setSelected(false);
		sellerIv.setSelected(false);

		questionsFl.setSelected(false);
		questionsIv.setSelected(false);

		myCenterFl.setSelected(true);
		myCenterIv.setSelected(true);
	}

	/**
	 * 点击了中间按钮
	 */
	private void clickToggleBtn() {
		// showPopupWindow(toggleImageView);
		// 改变按钮显示的图片为按下时的状态
		// Intent intent = new Intent(HomeActivity.this, ToJoinActivity.class);
		Intent intent = new Intent(HomeActivity.this, ToJoinActivity.class);
		startActivity(intent);
		// plusImageView.setSelected(true);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				logout();

			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	void logout() {
		final ProgressDialog pd = new ProgressDialog(HomeActivity.this);
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
						/*try {
							Thread.sleep(2000);
							finish();
							System.exit(0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						try {
	                        Thread.sleep(500);
	                        Process.killProcess(Process.myPid());
	                    } catch (InterruptedException e) {
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

	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient.start();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			app.setmCurrentLantitude(String.valueOf(location.getLatitude()));
			app.setmCurrentLongitude(String.valueOf(location.getLongitude()));
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");

			Log.i("BaiduLocationApiDem", sb.toString());
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub

		}
	}

}
