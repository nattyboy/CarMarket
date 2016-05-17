package com.example.aftermarket.adpter;

import java.util.List;
import java.util.Map;

import com.example.aftermarket.R;
import com.example.aftermarket.ui.AskRefundActivity;
import com.example.aftermarket.ui.OrderEvalutionActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ToEvaluteListAdapter extends BaseAdapter{
	private Map<String,Object> carToServer;
	private List<Map<String,Object>> carToServerList;
	private Context context;
	public ToEvaluteListAdapter(Context context,List<Map<String,Object>> carToServerList) {
		this.context=context;
		this.carToServerList=carToServerList;
	}

	@Override
	public int getCount() {
		return carToServerList.size()>0?carToServerList.size():0;
	}

	@Override
	public Object getItem(int position) {

		return carToServerList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(convertView==null){
			vh=new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.car_to_evalute_item, null);
			vh.carToServerImageView=(ImageView) convertView.findViewById(R.id.ev_car_to_server_imageView);
			vh.carSellerName=(TextView) convertView.findViewById(R.id.ev_car_seller_name);
			vh.carSellerMoney=(TextView) convertView.findViewById(R.id.ev_car_seller_money);
			vh.carAskReturn=(Button) convertView.findViewById(R.id.ev_car_ask_return);
			vh.carInsurePay=(Button) convertView.findViewById(R.id.ev_car_insure_pay);
			convertView.setTag(vh);
			
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		
		vh.carAskReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent(context,OrderEvalutionActivity.class);
				context.startActivity(intent);
				
			}
		});
		
		return convertView;
	}
	
	static class ViewHolder {

		ImageView carToServerImageView;
		TextView carSellerName;
		TextView carSellerMoney;
		Button carAskReturn;
		Button carInsurePay;
		ImageView carEdit;

	}

}
