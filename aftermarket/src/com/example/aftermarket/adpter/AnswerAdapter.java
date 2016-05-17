package com.example.aftermarket.adpter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.easemob.chatuidemo.activity.ChatActivity;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.Answer;
import com.example.aftermarket.bean.AskAgain;
import com.example.aftermarket.ui.BeautifulActivity;
import com.example.aftermarket.ui.HomeActivity;
import com.example.aftermarket.ui.QuestionDetailActivity;
import com.example.aftermarket.ui.SellerInfoActivity;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.example.aftermarket.ui.SubmitOnlineSpeak;
import com.example.aftermarket.views.AskAgainView;
import com.example.aftermarket.views.CircleImageView;
import com.example.aftermarket.views.NoScrollListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by EASTMOOD on 2016/3/15.
 */
public class AnswerAdapter extends BaseListAdapter<Answer> {

	private Context context;
	private String question_id;
	private DemoApplication app;
	private QuestionDetailActivity activity;
	public AnswerAdapter(Context context, String question_id) {
		super(context);
		this.context = context;
		this.question_id = question_id;
		this.activity=(QuestionDetailActivity) context;
		app=(DemoApplication) activity.getApplication();
	}

	@Override
	public View getListView(final int position, View convertView, ViewGroup viewGroup) {

		ViewHolder holder = findViewHolder(convertView, R.layout.item_question_detail, ViewHolder.class, true);

		final Answer answer = getItem(position);

		Glide.with(context).load(answer.logo).into(holder.logo);

		holder.name.setText(answer.name);
		holder.score.setText(answer.score + "分");
		holder.time.setText("2小时前");
		holder.content.setText(answer.content);
		holder.seller_ratingBar_question.setRating((float) answer.score);

		View v = LayoutInflater.from(context).inflate(R.layout.item_question_detail, null);
		/*
		 * for (AskAgain askAgain : answer.list) {
		 * holder.linearLayout.addView(new AskAgainView(context, askAgain)); }
		 */
		if (null != answer.list && 0 != answer.list.size()) {
			/*
			 * for (AskAgain askAgain : answer.list) {
			 * holder.linearLayout.addView(new AskAgainView(context, askAgain));
			 * } if (null != answer.list && 0 != answer.list.size()) {
			 * holder.linearLayout.setBackgroundResource(R.drawable.last_ll); }
			 */
			holder.answer_list.setAdapter(new AnswerListAdapter(context, answer.list));
			//holder.answer_list.setBackgroundResource(R.drawable.last_ll);
		}
		holder.answer_list.setAdapter(new AnswerListAdapter(context, answer.list));
		holder.onlineSpeakLayout = (RelativeLayout) rowView.findViewById(R.id.zixun_layout);
		holder.onlineAskLayout=(RelativeLayout) rowView.findViewById(R.id.liuyan_layout);
		if((activity).getquestionActivityId() != null){
			holder.onlineAskLayout.setVisibility(View.VISIBLE);
		}else{
			holder.onlineAskLayout.setVisibility(View.GONE);
		}
		holder.onlineAskLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, SubmitOnlineSpeak.class);
				String s = null;
				if (answer.list.size() > 0) {
					s = answer.list.get(answer.list.size()-1).id;
					
				}
				if(answer.list.size()==0){
					s=answer.answer_id;
				}
				intent.putExtra("question_id", question_id);
				intent.putExtra("pre_id", s);
				context.startActivity(intent);
				
			}
		});
		//holder.onlineAskLayout = (RelativeLayout) rowView.findViewById(R.id.zaixianzixun_layout);
		holder.onlineSpeakLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(null!=app.getToken()){
					Intent intent = new Intent();

					intent.putExtra("userId", answer.answer_ease_user);
					intent.putExtra("userNick", answer.answer_ease_user);
					intent.putExtra("store_img", answer.logo);
					SharedPreferences  sp = context.getSharedPreferences(answer.answer_ease_user, Context.MODE_PRIVATE);
					String cmp_name=sp.getString("cmp_name", "");
					String merchant_id=sp.getString("merchant_id", "");
					String balance=sp.getString("balance", "");
					String store_img=sp.getString("store_img", "");
					intent.putExtra("cmp_name", answer.name);
					intent.putExtra("merchant_id",answer.merchant_id);
					intent.putExtra("balance", answer.margin);
					intent.putExtra("store_img", answer.logo);
					intent.setClass(context, ChatActivity.class);
					context.startActivity(intent);
				}else{
					Toast.makeText(activity, "请登录", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(activity,ShopLoginActivity.class);
					activity.startActivity(intent);
					
				}

				
			}
		});

		return rowView;
	}

	public static class ViewHolder {
		public CircleImageView logo;
		public TextView name, score, time, content;
		public LinearLayout linearLayout;
		public RatingBar seller_ratingBar_question;
		public RelativeLayout onlineSpeakLayout;
		public RelativeLayout onlineAskLayout;
		public NoScrollListView answer_list;
	}

	class AnswerListAdapter extends BaseAdapter {

		public Context mcontext;
		public List<AskAgain> mlist;

		AnswerListAdapter(Context mcontext, List<AskAgain> mlist) {
			this.mcontext = mcontext;
			this.mlist = mlist;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return mlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater.from(mcontext);
			convertView = inflater.inflate(R.layout.answer_item, null);
			TextView answerType = (TextView) convertView.findViewById(R.id.answer_type);
			TextView answerTv = (TextView) convertView.findViewById(R.id.answer_tv);
			if (mlist.get(position).type == 0) {
				answerType.setText("追问：");

				answerType.setTextColor(mcontext.getResources().getColor(R.color.zhuiwen_blue));

				answerTv.setText(mlist.get(position).msg);
				Log.e("msg", mlist.get(position).msg);
				
			}
			if (mlist.get(position).type == 1) {
				answerType.setText("追答：");
				answerType.setTextColor(mcontext.getResources().getColor(R.color.zhuida_red));
				answerTv.setText(mlist.get(position).msg);
			}
			return convertView;

		}

	}

}
