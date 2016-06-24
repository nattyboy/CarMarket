package com.example.aftermarket.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chatuidemo.adapter.PopupWindowListAdapter;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.CategoryListAdapter;
import com.example.aftermarket.adpter.SellerListAdapter;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.ui.BeautifulActivity;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.example.aftermarket.ui.UsedCarSellerActivity;
import com.example.aftermarket.ui.SellerInfoActivity;
import com.example.aftermarket.utils.Px2DpUtils;
import com.example.aftermarket.views.PullToRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SellerFragment extends Fragment implements IXListViewListener {
	private TextView areaText;
	private TextView distanceText;
	private TextView optionText;
	private PopupWindow mPopWin;
	private LinearLayout layout;
	private ListView rootList;
	private ListView childList;
	private FrameLayout flChild;
	private String title[] = { "全部区域", "包河区", "瑶海区", "庐阳区", "蜀山区", "滨湖新区", "经开区", "高新区", "新站区" };
	private ArrayList<HashMap<String, Object>> itemList;
	private LinearLayout linLayout;
	private ListView lv_shangjia;
	private XListView sellListView;
	private Seller seller;
	private View v;// 获取视图
	private ArrayList<SellerInfo> sellerInfoList = new ArrayList();
	private ArrayList<Seller> sellerItems = new ArrayList();
	private SellerListAdapter adpter;
	private Activity homeActivity;
	private ImageView areaIv, distanceIv, opinionIv;
	private DemoApplication app;
	private final String REQUEST_HEADER = "merchantList";
	private String sellerRegion = "";
	 private String sellerLat = "31.8371046436";
	 private String sellerLng = "117.2688041888";
	//private String sellerLat;
	//private String sellerLng;
	private String sellerType = "1";
	LocationManager loctionManager;
	String contextService = Context.LOCATION_SERVICE;
	private ImageView onlineTel;
	private ImageView upShang;
	private LinearLayout sellerProgress;
	PopupWindow popuWindowTel = null;
	View contentView1 = null;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;

	private int mPosition = 0;
	private String[] city;
	private ArrayList<HashMap<String, Object>> arrayList = new ArrayList<HashMap<String, Object>>();
	private PopupWindowListAdapter mAdapter;
	private String mcity = null;
	private EditText et;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private LinearLayout areaLayout;
	View navView;
	View layoutBar;
	private LinearLayout menuItems;

	// 通过系统服务，取得LocationManager对象
	private LocationManager locationManager;
	private String locationProvider;
	private EditText et1;
	private double latitude ;
	private double longitude ;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.seller_info_activity, container, false);
		menuItems = (LinearLayout) v.findViewById(R.id.menu_item);
		navView = v.findViewById(R.id.navigate_layout);
		layoutBar = v.findViewById(R.id.layout_bar);
		sellListView = (XListView) v.findViewById(R.id.seller_listview);
		onlineTel = (ImageView) v.findViewById(R.id.online_tel);
		homeActivity = this.getActivity();
		sellListView.setXListViewListener(this);
		sellListView.setPullLoadEnable(false);
		mHandler = new Handler();
		sellerProgress = (LinearLayout) v.findViewById(R.id.sellerfragment_progress);
		app = (DemoApplication) homeActivity.getApplication();
		loctionManager = (LocationManager) homeActivity.getSystemService(contextService);
		String provider = LocationManager.GPS_PROVIDER;
		Location location = loctionManager.getLastKnownLocation(provider);
		if (null == location) {

		} else {
			sellerLat = String.valueOf(location.getLatitude());
			sellerLng = String.valueOf(location.getLatitude());

		}
		initPopupWindow();
		loadData();
		distanceText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

			}
		});
		onlineTel.setOnClickListener(new OnClickListener() {

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
						String mobile = app.getTelNum().trim();
						Intent intent = new Intent();
						intent.setAction("android.intent.action.CALL");
						intent.setData(Uri.parse("tel:" + mobile));// mobile为你要拨打的电话号码，模拟器中为模拟器编号也可
						startActivity(intent);
						mDialog.dismiss();
					}
				});
			}
		});
		return v;

	}

	

	public void showPopUp(View v) {
		city = title;
		for (int i = 0; i < city.length; i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("city", city[i]);
			hashMap.put("checked", false);
			if (!arrayList.contains(hashMap)) {
				if (arrayList.size() == city.length) {

				} else {
					arrayList.add(hashMap);
				}

			}
		}

		View view = homeActivity.getLayoutInflater().inflate(R.layout.popup_category, null);
		pop = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

		WindowManager windowManager = homeActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		pop.setWidth(display.getWidth());
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		ColorDrawable cd = new ColorDrawable(0x000000);
		pop.setBackgroundDrawable(cd);
		// 产生背景变暗效果
		WindowManager.LayoutParams lp = homeActivity.getWindow().getAttributes();
		// lp.alpha = 0.6f;
		homeActivity.getWindow().setAttributes(lp);
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		pop.setAnimationStyle(R.style.ActionSheetDialogAnimation);
		pop.showAsDropDown(menuItems, 0, 0);
		pop.showAtLocation(v, Gravity.CENTER_HORIZONTAL, 0, 0);
		ImageView upShangla = (ImageView) view.findViewById(R.id.up_shang);
		ListView listView = (ListView) view.findViewById(R.id.rootcategory);
		mAdapter = new PopupWindowListAdapter(homeActivity, arrayList, mPosition);
		view.findViewById(R.id.view_tra).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				pop.dismiss();
			}
		});
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View pConvertView, int pPosition, long arg3) {
				if (mPosition == pPosition) {
					return;
				} else if (-1 != mPosition) {
					HashMap<String, Object> map = arrayList.get(mPosition);
					map.put("checked", false);
				}
				mPosition = pPosition;
				HashMap<String, Object> map = arrayList.get(mPosition);
				map.put("checked", true);

				TextView tv = (TextView) pConvertView.findViewById(R.id.textView_popu);
				tv.setTextColor(Color.rgb(0, 121, 202));
				areaIv.setImageResource(R.drawable.daohang_xiala);
				sellerRegion = tv.getText().toString();
				areaText.setText(tv.getText());
				if ("全部区域".equals(sellerRegion)) {
					sellerRegion = "";
				}
				loadData();
				mcity = (String) map.get("city");
				mAdapter.notifyDataSetChanged();
				pop.dismiss();

			}
		});
		pop.setOnDismissListener(new OnDismissListener() {

			// 在dismiss中恢复透明度
			public void onDismiss() {
				WindowManager.LayoutParams lp = homeActivity.getWindow().getAttributes();
				lp.alpha = 1f;
				homeActivity.getWindow().setAttributes(lp);
				areaIv.setImageResource(R.drawable.daohang_xiala);
			}
		});
		upShangla.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null != pop) {
					pop.dismiss();
				}
			}
		});

	}

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("region", sellerRegion);

		params.addBodyParameter("lat", app.getmCurrentLantitude());
		params.addBodyParameter("lng", app.getmCurrentLongitude());
		params.addBodyParameter("type", sellerType);

		params.addBodyParameter("token", app.getToken());

		Log.e("test", "sellerLng" + sellerLat + sellerLng);
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(homeActivity.getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				sellerProgress.setVisibility(View.GONE);
				sellListView.setVisibility(View.VISIBLE);
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Seller>() {
					}.getType();
					seller = gson.fromJson(result, type);
					sellerInfoList = (ArrayList<SellerInfo>) seller.data;
					if (null != sellerInfoList) {
						adpter = new SellerListAdapter(homeActivity, sellerInfoList);
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

	private void initPopupWindow() {
		areaLayout = (LinearLayout) v.findViewById(R.id.areaLayoutId);
		itemList = new ArrayList<HashMap<String, Object>>();
		areaText = (TextView) v.findViewById(R.id.allArea);
		distanceText = (TextView) v.findViewById(R.id.distance);
		optionText = (TextView) v.findViewById(R.id.opinion);
		linLayout = (LinearLayout) v.findViewById(R.id.second);
		areaIv = (ImageView) v.findViewById(R.id.region_arrow);
		distanceIv = (ImageView) v.findViewById(R.id.region_distance);
		opinionIv = (ImageView) v.findViewById(R.id.opinion_arrow);

		areaLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				areaIv.setImageResource(R.drawable.daohang_shangla);
				showPopUp(v);
				// showPopupWindow(linLayout.getWidth(), linLayout.getHeight());
			}
		});

		optionText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
	}

	private void showPopupWindow(int width, int height) {

		itemList = new ArrayList<HashMap<String, Object>>();
		layout = (LinearLayout) LayoutInflater.from(homeActivity).inflate(R.layout.popup_category, null);
		upShang = (ImageView) layout.findViewById(R.id.up_shang);
		rootList = (ListView) layout.findViewById(R.id.rootcategory);
		for (int i = 0; i < title.length; i++) {
			HashMap<String, Object> items = new HashMap<String, Object>();
			items.put("name", title[i]);
			items.put("count", i * 5);
			itemList.add(items);
			upShang.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (null != mPopWin) {
						mPopWin.dismiss();
					}
				}
			});
		}

		CategoryListAdapter cla = new CategoryListAdapter(homeActivity, itemList);
		rootList.setAdapter(cla);
		mPopWin = new PopupWindow(layout, width, height * 3 / 4);
		mPopWin.setOutsideTouchable(true);
		mPopWin.setFocusable(true);
		mPopWin.setBackgroundDrawable(new BitmapDrawable());
		mPopWin.showAsDropDown(areaText, 5, 1);
		mPopWin.update();
		rootList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView tv = (TextView) view.findViewById(R.id.name_category);

				areaIv.setImageResource(R.drawable.daohang_xiala);
				sellerRegion = tv.getText().toString();
				areaText.setText(tv.getText());
				if ("全部区域".equals(sellerRegion)) {
					sellerRegion = "";
				}
				loadData();
				mPopWin.dismiss();
			}
		});
		mPopWin.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				areaIv.setImageResource(R.drawable.daohang_xiala);
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
				loadData();
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		sellListView.stopLoadMore();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

}
