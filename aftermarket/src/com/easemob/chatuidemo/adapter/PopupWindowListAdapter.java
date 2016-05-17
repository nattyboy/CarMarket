package com.easemob.chatuidemo.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.aftermarket.R;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class PopupWindowListAdapter extends BaseAdapter {

	private Activity mActivity;
	private ArrayList<HashMap<String, Object>> mArrayList;
	int lposition;

	public PopupWindowListAdapter(Activity activity, ArrayList<HashMap<String, Object>> arrayList, int lposition) {
		this.mActivity = activity;
		this.mArrayList = arrayList;
		this.lposition = lposition;
	}

	public int getCount() {
		return mArrayList.size();
	}

	public Object getItem(int position) {
		return mArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = LayoutInflater.from(mActivity).inflate(R.layout.area_popupwindow_listview_item, null);
		TextView mTextView = (TextView) convertView.findViewById(R.id.textView_popu);
		RadioButton mRadioButton = (RadioButton) convertView.findViewById(R.id.radioButton);
		mTextView.setText((String) mArrayList.get(position).get("city"));
		mRadioButton.setChecked((Boolean) mArrayList.get(position).get("checked"));
		if(lposition==position){
			mRadioButton.setChecked(true);
			lposition=-1;
			mTextView.setTextColor(Color.rgb(0,121,202));
			
		}
		mRadioButton.setClickable(false);// 
		return convertView;
	}
}
