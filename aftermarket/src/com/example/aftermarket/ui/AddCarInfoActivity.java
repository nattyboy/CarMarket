package com.example.aftermarket.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.AddCarAdapter;
import com.example.aftermarket.bean.CarInfo;
import com.example.aftermarket.config.ConstantClass;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddCarInfoActivity extends Activity {

	private TextView reChooseTv;
	private ImageView reChooseIv;
	private TextView carNameTv;
	private EditText carFrameNum;
	private EditText carEngNum;
	private EditText carUserName;
	private EditText carNum;
	private EditText carPrice;
	private EditText carBuyTime;
	private EditText carTotalPath;
	private DemoApplication app;
	private static final int REQUESTCODE_CARINFO = 0x1;
	private static final String REQUEST_HEADER = "carAdd";
	private String tempCarId = null;
	private String carName;
	private Date date;
	private int buyTimeInt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) this.getApplication();
		setContentView(R.layout.my_car_info);
		carTotalPath = (EditText) findViewById(R.id.total_et);
		carBuyTime = (EditText) findViewById(R.id.buy_time_et);
		carPrice = (EditText) findViewById(R.id.price_et);
		carNum = (EditText) findViewById(R.id.car_num_et);
		carUserName = (EditText) findViewById(R.id.name_et);
		carEngNum = (EditText) findViewById(R.id.eng_et);
		carFrameNum = (EditText) findViewById(R.id.car_fra_et);
		reChooseIv = (ImageView) findViewById(R.id.rechoose_iv);
		reChooseTv = (TextView) findViewById(R.id.rechoose_tv);
		carNameTv = (TextView) findViewById(R.id.car_name_id);
		reChooseIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddCarInfoActivity.this, CarCategoryActivity.class);
				startActivityForResult(intent, REQUESTCODE_CARINFO);
			}
		});
		reChooseTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(AddCarInfoActivity.this, CarCategoryActivity.class);
				startActivity(intent);

			}
		});

		Intent intent = getIntent();
		carName = intent.getStringExtra("carName");
		Log.e("CarInfoActivity", "carName" + intent.getStringExtra("carName"));
		/**
		 * 返回汽车头像
		 */
		String carImg = intent.getStringExtra("carImg");
		String carId = intent.getStringExtra("carId");

		carNameTv.setText(carName);
		tempCarId = intent.getStringExtra("carId");
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		carName = intent.getStringExtra("carName");
		Log.e("CarInfoActivity", "carNameonre" + intent.getStringExtra("carName"));
		/**
		 * 返回汽车头像
		 */
		String carImg = intent.getStringExtra("carImg");
		String carId = intent.getStringExtra("carId");
		Log.e("carImging", "carNameonre" + carImg);
		carNameTv.setText(carName);
		tempCarId = intent.getStringExtra("carId");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUESTCODE_CARINFO && resultCode == RESULT_OK) {
			String number = data.getStringExtra("number");
		}
	}

	public void baceToMycarPort(View v) {
		finish();
	}

	public void insureToSubmit(View v) {
		
		if (null == app.getToken()) {
			Intent intent = new Intent(AddCarInfoActivity.this, ShopLoginActivity.class);
			startActivity(intent);
		} else {
			
		

		Intent intent = new Intent(AddCarInfoActivity.this, MyCarPortActivity.class);

		intent.putExtra("carNameTv", carNameTv.getText().toString());
		/*if (TextUtils.isEmpty(carFrameNum.getText())) {

			Toast.makeText(AddCarInfoActivity.this, "请输入车架号", Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.putExtra("carFrameNum", carFrameNum.getText().toString());
		}
		if (TextUtils.isEmpty(carEngNum.getText())) {
			Toast.makeText(AddCarInfoActivity.this, "请输入发动机编号", Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.putExtra("carEngNum", carEngNum.getText().toString());
		}
		if (TextUtils.isEmpty(carUserName.getText())) {
			Toast.makeText(AddCarInfoActivity.this, "请输入车主姓名", Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.putExtra("carUserName", carUserName.getText().toString());
		}
		if (TextUtils.isEmpty(carNum.getText())) {
			Toast.makeText(AddCarInfoActivity.this, "请输入车牌号", Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.putExtra("carNum", carNum.getText().toString());
		}
		if (TextUtils.isEmpty(carPrice.getText())) {
			Toast.makeText(AddCarInfoActivity.this, "请输入购车价格", Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.putExtra("carPrice", carPrice.getText().toString());
		}
		if (TextUtils.isEmpty(carBuyTime.getText())) {
			Toast.makeText(AddCarInfoActivity.this, "请输入购车日期", Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.putExtra("carBuyTime", carBuyTime.getText().toString());
		}
		if (TextUtils.isEmpty(carTotalPath.getText())) {
			Toast.makeText(AddCarInfoActivity.this, "请输入购车日期", Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.putExtra("carTotalPath", carTotalPath.getText().toString());
		}*/
		intent.putExtra("carFrameNum", carFrameNum.getText().toString());
		intent.putExtra("carEngNum", carEngNum.getText().toString());
		intent.putExtra("carUserName", carUserName.getText().toString());
		intent.putExtra("carNum", carNum.getText().toString());
		intent.putExtra("carPrice", carPrice.getText().toString());
		intent.putExtra("carBuyTime", carBuyTime.getText().toString());
		intent.putExtra("carTotalPath", carTotalPath.getText().toString());
		intent.putExtra("carInfoPercent", "60");
		intent.putExtra("carImg", intent.getStringExtra("carImg"));
		intent.putExtra("carId", intent.getStringExtra("carId"));
		if (TextUtils.isEmpty(carNameTv.getText())) {
			Toast.makeText(AddCarInfoActivity.this, "请选车车型", Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.putExtra("carNameTv", carNameTv.getText().toString());
		}

		/**
		 * 返回汽车头像见上面的intent 也要传送过去
		 */
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("total_mileage", carTotalPath.getText().toString());
		params.addBodyParameter("buy_money", carPrice.getText().toString());
		params.addBodyParameter("car_number", carNum.getText().toString());
		params.addBodyParameter("car_owner", carUserName.getText().toString());
		if(null!=carBuyTime.getText())
		Log.e("Tag", "carBuyTime" +carBuyTime.getText().toString());
		if (null != carBuyTime.getText() && carBuyTime.getText().toString()!= "") {
			
			if(carBuyTime.getText().toString().length()!=0){
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

				String str = carBuyTime.getText().toString().trim();
				try {
					Date datea = (Date) formatter.parse(str);
					Log.e("Tag", "dateaa" + datea + "ddd" + str);

				} catch (Exception e) {

					Toast.makeText(this, "请输入正确的时间格式！", Toast.LENGTH_SHORT).show();
					return;
				}

				try {
					date = simpleDateFormat.parse(carBuyTime.getText().toString());
					int timeStemp = (int) date.getTime();
					buyTimeInt = (int) date.getTime();

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					System.out.println("llll");
					return;
				}
				int timeStemp = (int) date.getTime();
				Log.e("Tag", "datea1a" + timeStemp);
				System.out.println(timeStemp);
				
			}
			
			
		}
		if (null != date) {

			params.addBodyParameter("buy_date", (String) String.valueOf(date.getTime()).subSequence(0, String.valueOf(date.getTime()).length() - 3));
		}
		params.addBodyParameter("category_id", tempCarId);
		params.addBodyParameter("engine_number", carEngNum.getText().toString());
		params.addBodyParameter("carriage_number", carFrameNum.getText().toString());

		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) {
				Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(result);
					Gson gson = new Gson();
					java.lang.reflect.Type type = new TypeToken<CarInfo>() {
					}.getType();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

					} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Token过期请重新登录
						Toast.makeText(AddCarInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(AddCarInfoActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		startActivity(intent);
		finish();
		}
	}

}
