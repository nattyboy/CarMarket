package com.example.aftermarket.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.load.model.UrlLoader;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.activity.ChatContactActivity;
import com.easemob.chatuidemo.activity.MainActivity;
import com.easemob.util.EMLog;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.SellerListAdapter;
import com.example.aftermarket.adpter.ViewPageAdapter;
import com.example.aftermarket.bean.Home;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.ui.BeautifulActivity;
import com.example.aftermarket.ui.DiagnosisActivity;
import com.example.aftermarket.ui.HomeActivity;
import com.example.aftermarket.ui.MaintenanceActivity;
import com.example.aftermarket.ui.RepairActivity;
import com.example.aftermarket.ui.SellerActivity;
import com.example.aftermarket.ui.SheetSprayActivity;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.example.aftermarket.ui.TyreActivity;
import com.example.aftermarket.ui.UsedCarActivity;
import com.example.aftermarket.ui.WashCarActivity;
import com.example.aftermarket.views.SlideShowView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomePageFragment extends Fragment implements ViewPager.OnPageChangeListener {
	private static final String REQUEST_HEADER = "home";
	protected static final int SETVISI = 0x01;

	private RadioGroup rg1;
	private RadioGroup rg2;
	private View v;
	private ViewPager viewPager;
	private ArrayList<View> views = new ArrayList<>();
	private ArrayList<ImageView> imageDots = new ArrayList<>();;// 定义圆点dot数组
	private ImageView dot0, dot1, dot2, dot3, dot4;
	private ImageView leftPng, rightPng;
	private int currentPage;
	private Activity homeactivity;
	private ImageView washCar;
	private TextView messageTitleTv;
	private LinearLayout washCarLayout, beautyLayout, rapairLayout, maitainsaLayout, sheetLayout, diagnoseLayout, tyreLayout, usedCarLayout;
	private DemoApplication app;
	private TextView tv_notice;
	private Home home;
	private ImageView tipsIv;
	private ArrayList<EMConversation> conversationList = new ArrayList<EMConversation>();
	private Handler handler = new Handler() {

		// 处理消息
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SETVISI: //
				tipsIv.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		homeactivity = getActivity();
		app = (DemoApplication) homeactivity.getApplication();

		v = inflater.inflate(R.layout.fragment_homepage, container, false);

		/*
		 * mSlideShowView = (SlideShowView) v.findViewById(R.id.slideshowView);
		 * mSlideShowView.setImageUris(imageUris);
		 */
		dot0 = (ImageView) v.findViewById(R.id.dot0);
		dot1 = (ImageView) v.findViewById(R.id.dot1);
		dot2 = (ImageView) v.findViewById(R.id.dot2);
		dot3 = (ImageView) v.findViewById(R.id.dot3);
		dot4 = (ImageView) v.findViewById(R.id.dot4);
		imageDots.add(dot0);
		imageDots.add(dot1);
		imageDots.add(dot2);
		imageDots.add(dot3);
		imageDots.add(dot4);
		imageDots.get(0).setImageResource(R.drawable.lunfan_landian);// 初始化dot
		viewPager = (ViewPager) v.findViewById(R.id.viewpage);
		views.add(inflater.inflate(R.layout.remai_one, null));
		views.add(inflater.inflate(R.layout.remai_two, null));
		views.add(inflater.inflate(R.layout.remai_three, null));
		views.add(inflater.inflate(R.layout.remai_four, null));
		views.add(inflater.inflate(R.layout.remai_five, null));
		viewPager.setCurrentItem(2);
		viewPager.setOnPageChangeListener(this);
		viewPager.setAdapter(new ViewPageAdapter(views));
		messageTitleTv = (TextView) v.findViewById(R.id.message_title_tv);
		leftPng = (ImageView) v.findViewById(R.id.left_png);
		rightPng = (ImageView) v.findViewById(R.id.right_png);
		washCar = (ImageView) v.findViewById(R.id.xiche);
		washCarLayout = (LinearLayout) v.findViewById(R.id.xiche_layout);
		beautyLayout = (LinearLayout) v.findViewById(R.id.meirong_layout);
		rapairLayout = (LinearLayout) v.findViewById(R.id.xiufu_layout);
		maitainsaLayout = (LinearLayout) v.findViewById(R.id.baoyang_layout);
		sheetLayout = (LinearLayout) v.findViewById(R.id.banpen_layout);
		diagnoseLayout = (LinearLayout) v.findViewById(R.id.zhenduan_layout);
		tyreLayout = (LinearLayout) v.findViewById(R.id.luntai_layout);
		usedCarLayout = (LinearLayout) v.findViewById(R.id.ershouche_layout);
		tipsIv = (ImageView) v.findViewById(R.id.fragment_answer_msg_iv_tis);
		messageTitleTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (null != app.getToken()) {
					Intent intent = new Intent(homeactivity, MainActivity.class);
					homeactivity.startActivity(intent);
					tipsIv.setVisibility(View.INVISIBLE);
				} else {
					Toast.makeText(homeactivity, "请登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(homeactivity, ShopLoginActivity.class);
					startActivity(intent);

				}

			}
		});
		leftPng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentPage < 0)
					return;
				viewPager.setCurrentItem(currentPage - 1);
			}
		});
		rightPng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (currentPage > 4)
					return;
				viewPager.setCurrentItem(currentPage + 1);
			}
		});

		washCarLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(homeactivity, SellerActivity.class);
				intent.putExtra("dim_id", "101");
				startActivity(intent);
			}
		});
		beautyLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(homeactivity, BeautifulActivity.class);
				intent.putExtra("dim_id", "102");
				startActivity(intent);
			}
		});
		maitainsaLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(homeactivity, MaintenanceActivity.class);
				intent.putExtra("dim_id", "104");
				startActivity(intent);
			}
		});
		rapairLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(homeactivity, RepairActivity.class);
				intent.putExtra("dim_id", "103");
				startActivity(intent);
			}
		});
		sheetLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(homeactivity, SheetSprayActivity.class);
				intent.putExtra("dim_id", "105");
				startActivity(intent);
			}
		});
		diagnoseLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(homeactivity, DiagnosisActivity.class);
				intent.putExtra("dim_id", "106");
				startActivity(intent);
			}
		});
		tyreLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(homeactivity, TyreActivity.class);
				intent.putExtra("dim_id", "107");
				startActivity(intent);
			}
		});
		usedCarLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(homeactivity, UsedCarActivity.class);
				intent.putExtra("dim_id", "108");
				startActivity(intent);
			}
		});

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		EMChatManager.getInstance().registerEventListener(new EMEventListener() {

			@Override
			public void onEvent(EMNotifierEvent event) {
				EMMessage message = (EMMessage) event.getData();
				Message msg = Message.obtain();
				// msg.obj 任意类型的对象数据
				msg.what = SETVISI;
				handler.sendMessage(msg);
			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (null != app.getToken()) {
			if (conversationList.size() != 0) {
				conversationList.clear();
			}

			conversationList.addAll(loadConversationsWithRecentChat());
			if (conversationList.size() != 0) {
				if (conversationList.get(0).getUnreadMsgCount() != 0) {
					tipsIv.setVisibility(View.VISIBLE);
				} else {
					tipsIv.setVisibility(View.INVISIBLE);
				}
			}
		}

		tv_notice = (TextView) v.findViewById(R.id.tv_notice);
		loadData();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/*
		 * if (null != mSlideShowView) { mSlideShowView.clearUrls(); }
		 */
	}

	private void setCurrentPoint(int position) {

		for (int i = 0; i <= imageDots.size() - 1; i++) {

			imageDots.get(i).setImageResource(R.drawable.lunfan_huidian);

		}
		imageDots.get(position).setImageResource(R.drawable.lunfan_landian);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {

		setCurrentPoint(arg0);
		currentPage = arg0;
	}

	private void loadData() {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(homeactivity.getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<Home>() {
					}.getType();
					home = gson.fromJson(result, type);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						if (null != home) {
							if (null != home.data) {
								StringBuffer tvNotice = new StringBuffer();
								if (null != home.data.notice) {

									for (int i = 0; i < home.data.notice.size(); i++) {
										tvNotice.append(home.data.notice.get(i).notice_title + "     ");

									}
									if (null != tvNotice) {
										tv_notice.setText(tvNotice);
									}
								}

								if (null != home.data.tel) {
									app.setTelNum(home.data.tel);
								}

								if (null != home.data) {
									if (null != home.data.top) {
										ArrayList<String> urlist = new ArrayList<>();
										ArrayList<String> idlist = new ArrayList<>();

										for (int i = 0; i < home.data.top.size(); i++) {

											// imageUris.add(home.data.top.get(i));
											urlist.add(home.data.top.get(i).ad_url);
											idlist.add(home.data.top.get(i).merchant_id);
											Log.e("store_img", home.data.top.get(i).ad_url);	
										}
										ArrayList imageUris = new ArrayList<Integer>();
										if (null != imageUris) {
											if (imageUris.size() > 3 || imageUris.size() == 3) {

											} else {
												imageUris.add(R.drawable.one);
												imageUris.add(R.drawable.two);
												imageUris.add(R.drawable.three);
											}
										}

										SlideShowView mSlideShowView = (SlideShowView) v.findViewById(R.id.slideshowView);
										
										mSlideShowView.setUrls(urlist,idlist);
										mSlideShowView.setImageUris(imageUris);
										urlist.clear();
										imageUris.clear();

									}
								}

							}
						}

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(homeactivity, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(homeactivity, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}
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
				// Toast.makeText(homeactivity.getApplication(), str,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
			}
		});
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return +
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
		// 过滤掉messages size为0的conversation
		/**
		 * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
		 * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
		 */
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized (conversations) {
			for (EMConversation conversation : conversations.values()) {
				if (conversation.getAllMessages().size() != 0) {
					// if(conversation.getType() !=
					// EMConversationType.ChatRoom){
					sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
					// }
				}
			}
		}
		try {
			// Internal is TimSort algorithm, has bug
			sortConversationByLastChatTime(sortList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<EMConversation> list = new ArrayList<EMConversation>();
		for (Pair<Long, EMConversation> sortItem : sortList) {
			list.add(sortItem.second);
		}
		return list;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
		Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
			@Override
			public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

				if (con1.first == con2.first) {
					return 0;
				} else if (con2.first > con1.first) {
					return 1;
				} else {
					return -1;
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
