package com.example.aftermarket.adpter;

import java.util.ArrayList;
import java.util.List;

import com.example.aftermarket.R;
import com.example.aftermarket.bean.Car;
import com.example.aftermarket.ui.AddCarInfoActivity;
import com.example.aftermarket.ui.EditCarInfoActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CarSeriesAdapter extends BaseAdapter {
	private Car car;
	private Context context;
	private List<String> carNameItem;
	private List<String> carTitleItem;
	private List<String> carImgItem;
	private List<String> carIdItem = new ArrayList<>();
	private String tempCarName;
	private String tempCarImg;
	private String tempCarId;
	private Activity activity;
	private Bundle bundle;

	public CarSeriesAdapter(Context context, List<String> carNameItem, List<String> carTitleItem,
			List<String> carImgItem, List<String> carIdItem, Bundle bundle) {
		this.context = context;
		this.carNameItem = carNameItem;
		this.carTitleItem = carTitleItem;
		this.carImgItem = carImgItem;
		this.carIdItem = carIdItem;
		this.bundle = bundle;
	}

	@Override
	public int getCount() {
		return carNameItem.size()>0?carNameItem.size():0;
	}

	@Override
	public Object getItem(int position) {
		return carNameItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			convertView = layoutInflater.inflate(R.layout.car_series_item, null);
			vh.carBrandTv = (TextView) convertView.findViewById(R.id.car_brand_detail);
			vh.carSerisName = (TextView) convertView.findViewById(R.id.car_series_name);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			vh.carBrandTv.setText(carTitleItem.get(position));
			vh.carSerisName.setText(carNameItem.get(position));
		} else {
			vh.carBrandTv.setText(carTitleItem.get(position));
			vh.carSerisName.setText(carNameItem.get(position));
			if (carTitleItem.get(position - 1).equals(carTitleItem.get(position))) {
				vh.carBrandTv.setVisibility(View.GONE);
			}
		}
		tempCarName = vh.carSerisName.getText().toString();
		tempCarImg=carImgItem.get(position);
		tempCarId=carIdItem.get(position);
		
		vh.carSerisName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(null!=bundle){
					Intent intent = new Intent(context, EditCarInfoActivity.class);
					 bundle.putString("carName", tempCarName);
					 bundle.putString("category_id", tempCarId);
					intent.putExtras(bundle);
					/*intent.putExtra("carName", tempCarName);
					intent.putExtra("carImg",tempCarImg);
					intent.putExtra("carId", tempCarId);*/
					context.startActivity(intent);
					activity=(Activity) context;
					activity.finish();
				}else{
					
					Intent intent = new Intent(context, AddCarInfoActivity.class);
					intent.putExtra("carName", tempCarName);
					intent.putExtra("carImg",tempCarImg);
					intent.putExtra("carId", tempCarId);
					context.startActivity(intent);
					activity=(Activity) context;
					activity.finish();
				}

				
				
				
			}
		});
		
		return convertView;
	}

	private static class ViewHolder {
		TextView carBrandTv;
		TextView carSerisName;
	}

}
