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
import com.example.aftermarket.bean.CarInfo;
import com.example.aftermarket.bean.CarInfoInput;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.ui.AddCarInfoActivity;
import com.example.aftermarket.ui.EditCarInfoActivity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddCarAdapter extends BaseAdapter {
	private static final String REQUEST_HEADER = "carDel";
	private Context context;
	private Map<String, Object> CarPortMap;
	List<CarInfoInput> myCarList;
	private DemoApplication app;
	private Activity activity;

	// public String returnCar

	public AddCarAdapter(Context context, List<CarInfoInput> myCarList) {
		this.context = context;
		this.myCarList = myCarList;
		app = (DemoApplication) context.getApplicationContext();
	}

	@Override
	public int getCount() {

		return (myCarList.size()) > 0 ? myCarList.size() : 0;

	}

	@Override
	public Object getItem(int arg0) {
		return myCarList.get(arg0);
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
			convertview = inflater.inflate(R.layout.my_carport_item, null);
			vh.carLogo = (ImageView) convertview.findViewById(R.id.car_logo_img);
			vh.carEdit = (ImageView) convertview.findViewById(R.id.edit_carlist_iv);
			vh.carPercent = (TextView) convertview.findViewById(R.id.car_percent);
			vh.carVersion = (TextView) convertview.findViewById(R.id.car_version);
			vh.carProgressbar = (ProgressBar) convertview.findViewById(R.id.car_progressBar);
			vh.addCarLayout = (LinearLayout) convertview.findViewById(R.id.add_car_llayout);
			vh.delCarIv = (ImageView) convertview.findViewById(R.id.del_car_iv);
			vh.delCarLayout = (LinearLayout) convertview.findViewById(R.id.del_car_layout);
			convertview.setTag(vh);
		} else {
			vh = (ViewHolder) convertview.getTag();
		}
		vh.carVersion.setText(myCarList.get(arg0).category_name);
		vh.carPercent.setText(String.valueOf(Float.parseFloat(myCarList.get(arg0).completeness) * 100) + "%");
		vh.carProgressbar.setProgress((int) (Float.parseFloat(myCarList.get(arg0).completeness) * 100));
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/x-utils";
		BitmapUtils bitmapUtils = new BitmapUtils(context, path, cacheSize);
		bitmapUtils.configDefaultBitmapMaxSize(100, 100);
		bitmapUtils.display(vh.carLogo, myCarList.get(arg0).img);
		vh.carEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, EditCarInfoActivity.class);
				intent.putExtra("car_id", myCarList.get(arg0).car_id);
				intent.putExtra("category_id", myCarList.get(arg0).category_id);
				intent.putExtra("carriage_number", myCarList.get(arg0).carriage_number);
				intent.putExtra("engine_number", myCarList.get(arg0).engine_number);
				intent.putExtra("car_owner", myCarList.get(arg0).car_owner);
				intent.putExtra("car_number", myCarList.get(arg0).car_number);
				intent.putExtra("buy_date", myCarList.get(arg0).buy_date);
				intent.putExtra("buy_money", myCarList.get(arg0).buy_money);
				intent.putExtra("total_mileage", myCarList.get(arg0).total_mileage);
				intent.putExtra("category_name", myCarList.get(arg0).category_name);
				/**
				 * arg0+1 防止返回0造成误判断
				 */
				intent.putExtra("item_position", String.valueOf(arg0 + 1));
				context.startActivity(intent);
				/*
				 * activity=(Activity) context; activity.finish();
				 */

			}
		});
		vh.delCarLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				notifyDataSetChanged();
				del(myCarList.get(arg0).car_id);
				myCarList.remove(arg0);
			}

		});

		return convertview;
	}

	static class ViewHolder {

		ImageView carLogo;
		TextView carVersion;
		TextView carPercent;
		ProgressBar carProgressbar;
		ImageView carEdit;
		LinearLayout addCarLayout;
		ImageView delCarIv;
		LinearLayout delCarLayout;

	}

	private void del(String carId) {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("car_id", carId);
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(context.getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(context, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(context, ShopLoginActivity.class);
						context.startActivity(intent);
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

}
