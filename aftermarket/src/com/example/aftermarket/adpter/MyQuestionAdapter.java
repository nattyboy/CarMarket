package com.example.aftermarket.adpter;

import java.util.ArrayList;

import com.example.aftermarket.R;
import com.example.aftermarket.bean.EvaluteDataItem;
import com.example.aftermarket.bean.MyQuestionData;
import com.example.aftermarket.ui.EvaluteOrderActivity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyQuestionAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<MyQuestionData> myQuestionDataList;
	private LayoutInflater inflater;

	public MyQuestionAdapter(Context context, ArrayList<MyQuestionData> myQuestionDataList) {

		this.context = context;
		this.myQuestionDataList = myQuestionDataList;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return myQuestionDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return myQuestionDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.my_question_item, null);
			vh = new ViewHolder();
			vh.question_content = (TextView) convertView.findViewById(R.id.textView_ask_content);
			vh.question_undo = (TextView) convertView.findViewById(R.id.textView_ask_undo);
			vh.question_message = (TextView) convertView.findViewById(R.id.textView_ask_message);
			vh.question_time = (TextView) convertView.findViewById(R.id.textView_ask_time);
			vh.question_ask_num = (TextView) convertView.findViewById(R.id.textView_ask_num);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.question_content.setText(myQuestionDataList.get(position).content);
		if (myQuestionDataList.get(position).status.equals("0")) {
			vh.question_undo.setText("未解决");
		} else {
			vh.question_undo.setText("已解决");
		}
		if (myQuestionDataList.get(position).news.equals("0")) {
			vh.question_message.setVisibility(View.GONE);
		} else {
			vh.question_message.setText("新消息" + "(" + myQuestionDataList.get(position).news + ")");
		}

		vh.question_ask_num.setText(myQuestionDataList.get(position).reply + "个回复");

		vh.question_time.setText(myQuestionDataList.get(position).time);
		return convertView;
	}

	static class ViewHolder {
		TextView question_content;
		TextView question_undo;
		TextView question_message;
		TextView question_ask_num;
		TextView question_time;

	}

}
