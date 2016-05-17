package com.example.aftermarket.adpter;

import java.util.ArrayList;

import com.example.aftermarket.R;
import com.example.aftermarket.adpter.SellerListAdapter.ViewHolder;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.ui.InputOrderActivity;
import com.example.aftermarket.ui.SellerInfoActivity;
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
import android.widget.RatingBar;
import android.widget.TextView;

public class MyCollectionListAdapter extends BaseAdapter {

	private Context context;
	Seller homePageInfo;
	private ArrayList<SellerInfo> sellerItems;
	private LayoutInflater inflater;

	public MyCollectionListAdapter(Context context, ArrayList<SellerInfo> sellerItems) {
		this.context = context;
		this.sellerItems = sellerItems;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sellerItems.size()>0?sellerItems.size():0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sellerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.my_collection_list_item, null);
			vh = new ViewHolder();
			vh.seller_Cmp_Name = (TextView) convertView.findViewById(R.id.my_seller__cmp_name);
			vh.seller_RatingBar = (RatingBar) convertView.findViewById(R.id.my_seller__ratingBar);
			vh.seller_Rating_Grade = (TextView) convertView.findViewById(R.id.my_seller__rate_grade);
			vh.custom_Option_Num = (TextView) convertView.findViewById(R.id.my_custom_option_num);
			vh.seller_Icon = (ImageView) convertView.findViewById(R.id.my_seller__icon);
			vh.seler_Distance = (TextView) convertView.findViewById(R.id.my_seller__distance);
			vh.seller_Address = (TextView) convertView.findViewById(R.id.my_seller__address);
			vh.seller_Sucess = (TextView) convertView.findViewById(R.id.my_seller__sucess);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.seller_Cmp_Name.setText(sellerItems.get(position).company_name);
		// vh.seller_Cmp_balance.setText(sellerItems.get(position).balance);
		vh.seller_RatingBar.setRating(sellerItems.get(position).score);
		vh.seller_Rating_Grade.setText(sellerItems.get(position).score + "分");
		vh.custom_Option_Num.setText(sellerItems.get(position).evaluation_num + "人评价");
		vh.seler_Distance.setText("距离：<" + sellerItems.get(position).distance + "km");
		vh.seller_Address.setText(sellerItems.get(position).address);
		vh.seller_Sucess.setText(sellerItems.get(position).business_approve);
		
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/x-utils";
		BitmapUtils bitmapUtils = new BitmapUtils(context, path, cacheSize);
		bitmapUtils.configDefaultBitmapMaxSize(100, 100);
		bitmapUtils.display(vh.seller_Icon, sellerItems.get(position).store_img);
		vh.seller_Cmp_Name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(context, SellerInfoActivity.class);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView seller_Cmp_Name;
		ImageView seller_Cmp_balance;
		RatingBar seller_RatingBar;
		TextView seller_Rating_Grade;
		TextView custom_Option_Num;
		ImageView seller_Icon;
		TextView seller_Address;
		TextView seller_Sucess;
		// ImageView distance_Icon;
		TextView seler_Distance;
		TextView in_Price;
		TextView out_Price;
		TextView standar_Price;
		TextView inPriceBt;
		TextView outPriceBt;
		TextView standPriceBt;
	}
}
