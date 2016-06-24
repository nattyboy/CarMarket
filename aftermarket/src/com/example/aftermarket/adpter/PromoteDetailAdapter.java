package com.example.aftermarket.adpter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chatuidemo.activity.ChatActivity;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.BusinessInfo;
import com.example.aftermarket.bean.CarInfo;
import com.example.aftermarket.bean.CarInfoInput;
import com.example.aftermarket.bean.PromoteItem;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.ui.AddCarInfoActivity;
import com.example.aftermarket.ui.EditCarInfoActivity;
import com.example.aftermarket.ui.InputOrderActivity;
import com.example.aftermarket.ui.MyCarPortActivity;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PromoteDetailAdapter extends BaseAdapter {
	private Context context;
	private DemoApplication app;
	private Activity activity;
	private ArrayList<PromoteItem> promote;
	private ArrayList<SellerInfo> sellerItems;
	private int position;
	public BusinessInfo data;

	// public String returnCar

	public PromoteDetailAdapter(Context context,ArrayList<PromoteItem> promote2, ArrayList<SellerInfo> sellerItems,int position) {
		this.context = context;
		app = (DemoApplication) context.getApplicationContext();
		this.promote=promote2;
		this.sellerItems=sellerItems;
		this.position=position;
	}
	public PromoteDetailAdapter(Context context,ArrayList<PromoteItem> promote2,BusinessInfo data) {
		this.context = context;
		app = (DemoApplication) context.getApplicationContext();
		this.promote=promote2;
		this.sellerItems=sellerItems;
		this.position=position;
		this.data=data;
	}

	@Override
	public int getCount() {

		return promote.size();

	}

	@Override
	public Object getItem(int arg0) {
		return promote.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View convertview, ViewGroup arg2) {
		ViewHolder vh;
		if (convertview == null) {
			vh = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertview = inflater.inflate(R.layout.promote_item, null);
			vh.bt=(TextView) convertview.findViewById(R.id.promote_bt);
			vh.tvPrice=(TextView) convertview.findViewById(R.id.promote_price);
			vh.tvTv=(TextView) convertview.findViewById(R.id.promote_tv);
			
			convertview.setTag(vh);
		} else {
			vh = (ViewHolder) convertview.getTag();
		}
		
		vh.tvPrice.setText(promote.get(arg0).cost+"元");
		vh.tvTv.setText(promote.get(arg0).business_name);
		
		vh.bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(context, InputOrderActivity.class);
				intent.putExtra("wash_Name", promote.get(arg0).business_name);
				intent.putExtra("cmp_name", data.company_name);
				intent.putExtra("merchant_id", data.merchant_id);
				intent.putExtra("balance", data.margin);
				intent.putExtra("business_id", promote.get(arg0).business_id);
				intent.putExtra("cost", promote.get(arg0).cost);
				context.startActivity(intent);
				
			}
		});
		

		return convertview;
	}

	static class ViewHolder {
		
		TextView bt;
		
		TextView tvPrice;
		
		TextView tvTv;

	

	}

	

}
