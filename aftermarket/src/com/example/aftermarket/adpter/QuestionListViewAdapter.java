package com.example.aftermarket.adpter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.MyCollectionListAdapter.ViewHolder;
import com.example.aftermarket.bean.Question;
import com.example.aftermarket.ui.SellerInfoActivity;
import com.example.aftermarket.views.CircleImageView;
import com.example.aftermarket.views.SlideShowView;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class QuestionListViewAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ImageView> imageDots = new ArrayList<>();;// 定义圆点dot数组
	private ImageView dot0, dot1, dot2, dot3;
	List<Integer> imageUris;
	ArrayList<Question> questionsList;
	private LayoutInflater inflater;

	public QuestionListViewAdapter(Context context, List<Question> questions) {
		this.context = context;
		imageUris = new ArrayList<Integer>();
		imageUris.add(R.drawable.tiku_banner1);
		imageUris.add(R.drawable.tiku_banner2);
		imageUris.add(R.drawable.tiku_banner3);
		this.questionsList = (ArrayList<Question>) questions;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return questionsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return questionsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_question, null);
			vh = new ViewHolder();
			vh.user_img = (CircleImageView) convertView.findViewById(R.id.user_img);
			vh.answer_img = (CircleImageView) convertView.findViewById(R.id.answer_img);
			vh.user_name = (TextView) convertView.findViewById(R.id.user_name);
			vh.car_name = (TextView) convertView.findViewById(R.id.car_name);
			vh.reply = (TextView) convertView.findViewById(R.id.reply);
			vh.time = (TextView) convertView.findViewById(R.id.time);
			vh.content = (TextView) convertView.findViewById(R.id.content);
			vh.answer_from = (TextView) convertView.findViewById(R.id.answer_from);
			vh.answer_content = (TextView) convertView.findViewById(R.id.answer_content);
			vh.ll_answer = (LinearLayout) convertView.findViewById(R.id.ll_answer);
			vh.slideshowView_question_item = (SlideShowView) convertView.findViewById(R.id.slideshowView_question_item);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		Glide.with(context).load(questionsList.get(position).user_img).into(vh.user_img);
		Glide.with(context).load(questionsList.get(position).answer_img).into(vh.answer_img);
		vh.user_name.setText(questionsList.get(position).user_name);
		vh.car_name.setText(questionsList.get(position).car_name);
		vh.reply.setText(questionsList.get(position).reply + "个回复");
		vh.time.setText(questionsList.get(position).time);
		vh.content.setText(questionsList.get(position).content);
		vh.answer_from.setText(questionsList.get(position).answer_from);
		vh.answer_content.setText(questionsList.get(position).answer_content);

		vh.ll_answer.setVisibility(questionsList.get(position).reply == 0 ? View.GONE : View.VISIBLE);
		if (position == 0) {
			vh.slideshowView_question_item.setImageUris(imageUris);
			dot0 = (ImageView) vh.slideshowView_question_item.findViewById(R.id.dot0);
			dot1 = (ImageView) vh.slideshowView_question_item.findViewById(R.id.dot1);
			dot2 = (ImageView) vh.slideshowView_question_item.findViewById(R.id.dot2);
			dot3 = (ImageView) vh.slideshowView_question_item.findViewById(R.id.dot3);
			imageDots.add(dot0);
			imageDots.add(dot1);
			imageDots.add(dot2);
			imageDots.add(dot3);
			vh.slideshowView_question_item.setVisibility(View.VISIBLE);
		}
		

		return convertView;
	}

	class ViewHolder {
		public CircleImageView user_img;
		public CircleImageView answer_img;
		public TextView user_name, car_name, reply, time, content, answer_from, answer_content;
		LinearLayout ll_answer;
		SlideShowView slideshowView_question_item;
	}

}
