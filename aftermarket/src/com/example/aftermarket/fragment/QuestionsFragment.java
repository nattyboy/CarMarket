package com.example.aftermarket.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chatuidemo.activity.MainActivity;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.QuestionListAdapter;
import com.example.aftermarket.adpter.QuestionListViewAdapter;
import com.example.aftermarket.adpter.SellerListAdapter;
import com.example.aftermarket.bean.Question;
import com.example.aftermarket.bean.QuestionList;
import com.example.aftermarket.bean.ResponseResult;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.ui.HomeActivity;
import com.example.aftermarket.ui.QuestionDetailActivity;
import com.example.aftermarket.ui.SearchQuestionContent;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.example.aftermarket.views.SlideShowViewQ;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionsFragment extends Fragment implements IXListViewListener {
	private static final String REQUEST_HEADER = "questionList";
	protected static final int SETTIPS = 0x02;
	private XListView listView;
	private QuestionListAdapter adapter;
	private List<Question> questions;
	private Button btn;
	private View view;
	private Activity homeActivity;
	private DemoApplication app;
	private SlideShowViewQ mSlideShowView;
	private ArrayList<ImageView> imageDots = new ArrayList<>();;// 定义圆点dot数组
	private ImageView dot0, dot1, dot2, dot3;
	private LinearLayout progressLayout;
	private LinearLayout questionLayout;
	private Handler mHandler;
	private int start = 0;
	private static int refreshCnt = 0;
	private TextView messageTv;
	ArrayList<Question> questionsList;
	private ImageView tipsIv;
	private ArrayList<EMConversation> conversationList = new ArrayList<EMConversation>();

	private QuestionListViewAdapter questionListViewAdapter;
	

	private Handler handler = new Handler() {

		// 处理消息
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SETTIPS: //
				tipsIv.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		homeActivity = getActivity();
		app = (DemoApplication) homeActivity.getApplication();
		List<Integer> imageUris = new ArrayList<Integer>();
		imageUris.add(R.drawable.tiku_banner1);
		imageUris.add(R.drawable.tiku_banner2);
		imageUris.add(R.drawable.tiku_banner3);
		view = inflater.inflate(R.layout.activity_question_list, container, false);
		tipsIv = (ImageView) view.findViewById(R.id.fragment_question_msg_iv_tis);
		mSlideShowView = (SlideShowViewQ) view.findViewById(R.id.slideshowView_question);
		mSlideShowView.setImageUris(imageUris);
		dot0 = (ImageView) view.findViewById(R.id.dot0);
		dot1 = (ImageView) view.findViewById(R.id.dot1);
		dot2 = (ImageView) view.findViewById(R.id.dot2);
		dot3 = (ImageView) view.findViewById(R.id.dot3);
		imageDots.add(dot0);
		imageDots.add(dot1);
		imageDots.add(dot2);
		imageDots.add(dot3);
		// imageDots.get(0).setImageResource(R.drawable.lunfan_landian);//初始化dot
		questions = new ArrayList<Question>();
		listView = (XListView) view.findViewById(R.id.listView);
		listView.setXListViewListener(this);
		listView.setPullLoadEnable(false);
		mHandler = new Handler();
		progressLayout = (LinearLayout) view.findViewById(R.id.question_progress);
		questionLayout = (LinearLayout) view.findViewById(R.id.question_layout);
		messageTv = (TextView) view.findViewById(R.id.textView1_message);
		btn = (Button) view.findViewById(R.id.btn);
		messageTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (null != app.getToken()) {
					Intent intent = new Intent(homeActivity, MainActivity.class);
					startActivity(intent);
					tipsIv.setVisibility(View.INVISIBLE);
				}else{
					Toast.makeText(homeActivity, "请登录", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(homeActivity, ShopLoginActivity.class);
					startActivity(intent);
				}

			}
		});
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(homeActivity, SearchQuestionContent.class);
				startActivity(intent);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});

		refreshData("");

		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		EMChatManager.getInstance().registerEventListener(new EMEventListener() {

			@Override
			public void onEvent(EMNotifierEvent event) {
				EMMessage message = (EMMessage) event.getData();
				Message msg = Message.obtain();
				// msg.obj 任意类型的对象数据
				msg.what = SETTIPS;
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

	}

	private void refreshData(String data) {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("key", "");
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(homeActivity.getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				progressLayout.setVisibility(View.GONE);
				questionLayout.setVisibility(View.VISIBLE);
				try {
					jsonObj = new JSONObject(result);
					ResponseResult<QuestionList> responseResult = new Gson().fromJson(result, new TypeToken<ResponseResult<QuestionList>>() {
					}.getType());
					if (null != responseResult.data) {
						questions = responseResult.data.list;
						adapter = new QuestionListAdapter(homeActivity);
						listView.setAdapter(adapter);
						adapter.setItems(questions, false);
						adapter.notifyDataSetChanged();
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

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshData("");
				onLoad();
			}
		}, 2000);

	}

	@Override
	public void onLoadMore() {
		listView.stopLoadMore();
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
