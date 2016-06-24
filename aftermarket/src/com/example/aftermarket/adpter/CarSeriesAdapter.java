package com.example.aftermarket.adpter;

import java.util.ArrayList;
import java.util.List;

import com.example.aftermarket.R;
import com.example.aftermarket.bean.Car;
import com.example.aftermarket.ui.AddCarInfoActivity;
import com.example.aftermarket.ui.EditCarInfoActivity;
import com.example.aftermarket.ui.UsedCarActivity;

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
	private String used_car;
	private View vButton;
	private String carVersionName="";

	public CarSeriesAdapter(Context context, List<String> carNameItem, List<String> carTitleItem, List<String> carImgItem, List<String> carIdItem, Bundle bundle, String used_car) {
		this.context = context;
		this.carNameItem = carNameItem;
		this.carTitleItem = carTitleItem;
		this.carImgItem = carImgItem;
		this.carIdItem = carIdItem;
		this.bundle = bundle;
		this.used_car = used_car;
	}

	@Override
	public int getCount() {

		return carNameItem.size() > 0 ? carNameItem.size() : 0;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
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
		
		vh.carBrandTv.setText(carTitleItem.get(position));
		vh.carSerisName.setText(carNameItem.get(position));
		if(position==0){
			vh.carBrandTv.setVisibility(View.VISIBLE);
		}else {
			vh.carBrandTv.setVisibility(View.GONE);
		}
		/*if (position == 0) {
			vh.carBrandTv.setText(carTitleItem.get(position));
			vh.carSerisName.setText(carNameItem.get(position));
		} else {
			vh.carBrandTv.setText(carTitleItem.get(position));
			vh.carSerisName.setText(carNameItem.get(position));
			if (carTitleItem.get(position - 1).equals(carTitleItem.get(position))) {
				vh.carBrandTv.setVisibility(View.GONE);
			}
		}*/
		//vh.carBrandTv.setVisibility(View.GONE);
		
		
		tempCarName = carNameItem.get(position);
		tempCarImg = carImgItem.get(position);
		tempCarId = carIdItem.get(position);
		
		
		
		vh.carSerisName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				tempCarName = carNameItem.get(position);
				tempCarImg = carImgItem.get(position);
				tempCarId = carIdItem.get(position);

				Log.e("used_car", "" + carNameItem.get(position)+position+tempCarName);

				if (null != used_car) {

					Intent intent = new Intent(context, UsedCarActivity.class);
					intent.putExtra("carName", carNameItem.get(position));
					intent.putExtra("carImg", tempCarImg);
					intent.putExtra("carId", tempCarId);
					context.startActivity(intent);
					activity = (Activity) context;
					activity.finish();

				}

				if (null != bundle && null == used_car) {
					Intent intent = new Intent(context, EditCarInfoActivity.class);
					bundle.putString("carName", carNameItem.get(position));
					bundle.putString("category_id", tempCarId);
					intent.putExtras(bundle);
					/*
					 * intent.putExtra("carName", tempCarName);
					 * intent.putExtra("carImg",tempCarImg);
					 * intent.putExtra("carId", tempCarId);
					 */
					context.startActivity(intent);
					activity = (Activity) context;
					activity.finish();
				} else {
					if (null == used_car) {
						Intent intent = new Intent(context, AddCarInfoActivity.class);
						intent.putExtra("carName", carNameItem.get(position));
						intent.putExtra("carImg", tempCarImg);
						intent.putExtra("carId", tempCarId);
						context.startActivity(intent);
						activity = (Activity) context;
						activity.finish();
					}

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
