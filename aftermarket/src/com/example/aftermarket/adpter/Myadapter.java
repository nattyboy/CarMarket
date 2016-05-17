package com.example.aftermarket.adpter;

import java.util.List;

import com.example.aftermarket.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class Myadapter extends BaseAdapter{
	
	private Context context;
	String[] titles;

    public Myadapter(Context context,String[] titles) {
        this.context = context;
        this.titles=titles;
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.length>0?titles.length:0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return titles[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.seller_list_item, null);
		return convertView;
	}

}
