package com.example.aftermarket.adpter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.aftermarket.R;
import com.example.aftermarket.views.MyCheckBox;

public class SquareAdapterOrder extends BaseAdapter {

	private List<String> list = new ArrayList();
	private Context mContext;
	private int status = 100;
	private List<String> checkBoxTextList = new ArrayList<>();

	public String returnCheckTextList() {

		StringBuffer sb = new StringBuffer();
		if(null==checkBoxTextList)
			return "";
		if(0==checkBoxTextList.size())
			return "";
		for (int i = 0; i < checkBoxTextList.size(); i++) {
			/*if (i + 1 == checkBoxTextList.size()) {
				sb.append(checkBoxTextList.get(i));
			}*/
			sb.append(checkBoxTextList.get(i)).append(",");
		}
		//checkBoxTextList.clear();
		return sb.toString().substring(0,sb.toString().length()-1);

	}

	public SquareAdapterOrder(Context context, int status) {
		// 测试数据
		this.mContext = context;
		this.status = status;
	}

	public void addData(String data) {
		list.add(data);
		notifyDataSetChanged();
	}

	public void addData(List<String> data) {
		for (String d : data) {
			list.add(d);
		}
		notifyDataSetChanged();
	}

	public List<String> getList() {
		return this.list;
	}

	@Override
	public int getCount() {
		return list.size();

	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vr;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.chebox_item, null);
			vr = new ViewHolder();
			vr.mybox = (MyCheckBox) convertView.findViewById(R.id.mychebox);
			vr.mybox.setText(list.get(position));
			convertView.setTag(vr);

		} else {
			vr = (ViewHolder) convertView.getTag();
		}

		vr.mybox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (vr.mybox.isChecked()) {
					checkBoxTextList.add(vr.mybox.getText());
				} else {
					if (checkBoxTextList.contains(vr.mybox.getText())) {
						checkBoxTextList.remove(vr.mybox.getText());
					}
				}
			}
		});

		return convertView;
	}

	class ViewHolder {
		MyCheckBox mybox;
	}

}