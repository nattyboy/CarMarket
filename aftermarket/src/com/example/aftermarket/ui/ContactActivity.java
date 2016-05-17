package com.example.aftermarket.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.easemob.chatuidemo.activity.MainActivity;
import com.easemob.chatuidemo.adapter.ChatAllHistoryAdapter;
import com.easemob.chatuidemo.db.InviteMessgeDao;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ContactActivity extends Activity {
	private InputMethodManager inputMethodManager;
	private ListView listView;
	private ChatAllHistoryAdapter adapter;
	private EditText query;
	private ImageButton clearSearch;
	public RelativeLayout errorItem;
	public TextView errorText;
	private boolean hidden;
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_conversation_history);

	}

}
