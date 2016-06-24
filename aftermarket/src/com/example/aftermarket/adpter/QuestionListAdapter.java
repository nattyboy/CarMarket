package com.example.aftermarket.adpter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.Question;
import com.example.aftermarket.ui.QuestionDetailActivity;
import com.example.aftermarket.views.CircleImageView;
import com.example.aftermarket.views.SlideShowView;
import com.example.aftermarket.views.SlideShowViewQ;

/**
 * Created by EASTMOOD on 2016/3/15.
 */
public class QuestionListAdapter extends BaseListAdapter<Question> {

	private Context context;
	private ArrayList<ImageView> imageDots = new ArrayList<>();;// 定义圆点dot数组
	private ImageView dot0, dot1, dot2, dot3;
	List<Integer> imageUris;

	public QuestionListAdapter(Context context) {
		super(context);
		this.context = context;
		imageUris = new ArrayList<Integer>();
		imageUris.add(R.drawable.tiku_banner1);
		imageUris.add(R.drawable.tiku_banner2);
		imageUris.add(R.drawable.tiku_banner3);
	}

	@Override
	public View getListView(final int position, View convertView, ViewGroup viewGroup) {

		ViewHolder holder = findViewHolder(convertView, R.layout.item_question, ViewHolder.class, true);

		final Question question = getItem(position);

		Glide.with(context).load(question.user_img).into(holder.user_img);
		Glide.with(context).load(question.answer_img).into(holder.answer_img);

		holder.user_name.setText(question.user_name);
		holder.car_name.setText(question.car_name);
		holder.reply.setText(question.reply + "个回复");
		holder.time.setText(question.time);
		holder.content.setText(question.content);
		holder.answer_from.setText(question.answer_from);
		holder.answer_content.setText(question.answer_content);

		holder.ll_answer.setVisibility(question.reply == 0 ? View.GONE : View.VISIBLE);
		if (position == 0) {
			holder.slideshowView_question_item.setImageUris(imageUris);
			dot0 = (ImageView) holder.slideshowView_question_item.findViewById(R.id.dot0);
			dot1 = (ImageView) holder.slideshowView_question_item.findViewById(R.id.dot1);
			dot2 = (ImageView) holder.slideshowView_question_item.findViewById(R.id.dot2);
			dot3 = (ImageView) holder.slideshowView_question_item.findViewById(R.id.dot3);
			imageDots.add(dot0);
			imageDots.add(dot1);
			imageDots.add(dot2);
			imageDots.add(dot3);
			holder.slideshowView_question_item.setVisibility(View.VISIBLE);
			
		}
		
		rowView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, QuestionDetailActivity.class);
				intent.putExtra("question_id", question.question_id);
				context.startActivity(intent);
			}
		});

		if (position == 0) {
		
			rowView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					Intent intent = new Intent(context, QuestionDetailActivity.class);
					intent.putExtra("question_id", getItem(position).question_id);
					context.startActivity(intent);
					
					
				}
			});
			
		}
		return rowView;
	}

	public static class ViewHolder {
		public CircleImageView user_img;
		public CircleImageView answer_img;
		public TextView user_name, car_name, reply, time, content, answer_from, answer_content;
		LinearLayout ll_answer;
		SlideShowViewQ slideshowView_question_item;
	}

}
