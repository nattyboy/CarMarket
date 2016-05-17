package com.example.aftermarket.ui;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditCarInfoActivity extends Activity {

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
	private static final String REQUEST_HEADER = "carEdit";
	private String tempCarId = null;
	private String item_position_id;// 哪一个车型item传输过来的
	private String car_id;
	private String category_id;
	Bundle bundle=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (DemoApplication) this.getApplication();
		setContentView(R.layout.my_car_info_edit);
		carTotalPath = (EditText) findViewById(R.id.total_et_edit_edit);
		carBuyTime = (EditText) findViewById(R.id.buy_time_et_edit);
		carPrice = (EditText) findViewById(R.id.price_et_edit);
		carNum = (EditText) findViewById(R.id.car_num_et_edit);
		carUserName = (EditText) findViewById(R.id.name_et_edit);
		carEngNum = (EditText) findViewById(R.id.eng_et_edit);
		carFrameNum = (EditText) findViewById(R.id.car_fra_et_edit);
		reChooseIv = (ImageView) findViewById(R.id.rechoose_iv_edit);
		reChooseTv = (TextView) findViewById(R.id.rechoose_tv_edit);
		carNameTv = (TextView) findViewById(R.id.car_name_id_edit);

		reChooseIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(EditCarInfoActivity.this, CarCategoryActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("carFrameNum", carFrameNum.getText().toString());
				bundle.putString("carEngNum", carEngNum.getText().toString());
				bundle.putString("carNum", carNum.getText().toString());
				bundle.putString("carPrice", carPrice.getText().toString());
				bundle.putString("carBuyTime", carBuyTime.getText().toString());
				bundle.putString("carTotalPath", carTotalPath.getText().toString());
				bundle.putString("carUserName", carUserName.getText().toString());
				bundle.putString("car_id", car_id);
				bundle.putString("car_owner", carUserName.getText().toString());
				bundle.putString("category_id", category_id);
				intent.putExtras(bundle);
				startActivity(intent);
				//finish();
			}
		});
		reChooseTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(EditCarInfoActivity.this, CarCategoryActivity.class);
				Bundle bundle=new Bundle();
				bundle.putString("carFrameNum", carFrameNum.getText().toString());
				bundle.putString("carEngNum", carEngNum.getText().toString());
				bundle.putString("carNum", carNum.getText().toString());
				bundle.putString("carPrice", carPrice.getText().toString());
				bundle.putString("carBuyTime", carBuyTime.getText().toString());
				bundle.putString("carTotalPath", carTotalPath.getText().toString());
				bundle.putString("carUserName", carUserName.getText().toString());
				bundle.putString("car_id", car_id);
				bundle.putString("car_owner", carUserName.getText().toString());
				bundle.putString("category_id", category_id);
				intent.putExtras(bundle);
				startActivity(intent);
				//finish();

			}
		});

		Intent intent = getIntent();
		if (intent.getStringExtra("item_position") != null) {
			carNameTv.setText(intent.getStringExtra("category_name"));
			carTotalPath.setText(intent.getStringExtra("total_mileage"));
			carBuyTime.setText(intent.getStringExtra("buy_date"));
			carNum.setText(intent.getStringExtra("car_number"));
			carUserName.setText(intent.getStringExtra("car_owner"));
			carEngNum.setText(intent.getStringExtra("engine_number"));
			carFrameNum.setText(intent.getStringExtra("carriage_number"));
			carPrice.setText(intent.getStringExtra("buy_money"));
			car_id = intent.getStringExtra("car_id");
			item_position_id = intent.getStringExtra("item_position");
			category_id = intent.getStringExtra("category_id");
		}else{
			bundle = intent.getExtras();
			carNameTv.setText(bundle.getString("carName"));
			carTotalPath.setText(bundle.getString("carTotalPath"));
			carFrameNum.setText(bundle.getString("carFrameNum"));
			carEngNum.setText(bundle.getString("carEngNum"));
			carNum.setText(bundle.getString("carNum"));
			carPrice.setText(bundle.getString("carPrice"));
			carBuyTime.setText(bundle.getString("carBuyTime"));
			carUserName.setText(bundle.getString("carUserName"));
			car_id=bundle.getString("car_id");
			category_id=bundle.getString("category_id");
			 Log.e("Bundle", "EditCarInfoActivity"+bundle);
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		/*carName = intent.getStringExtra("carName");
		Log.e("CarInfoActivity", "carNameonre" + intent.getStringExtra("carName"));
		*//**
		 * 返回汽车头像
		 *//*
		String carImg = intent.getStringExtra("carImg");
		String carId = intent.getStringExtra("carId");

		carNameTv.setText(carName);
		tempCarId = intent.getStringExtra("carId");*/
		if (intent.getStringExtra("item_position") != null) {
			carNameTv.setText(intent.getStringExtra("category_name"));
			carTotalPath.setText(intent.getStringExtra("total_mileage"));
			carBuyTime.setText(intent.getStringExtra("buy_date"));
			carNum.setText(intent.getStringExtra("car_number"));
			carUserName.setText(intent.getStringExtra("car_owner"));
			carEngNum.setText(intent.getStringExtra("engine_number"));
			carFrameNum.setText(intent.getStringExtra("carriage_number"));
			carPrice.setText(intent.getStringExtra("buy_money"));
			car_id = intent.getStringExtra("car_id");
			item_position_id = intent.getStringExtra("item_position");
			category_id = intent.getStringExtra("category_id");
		}else{
			bundle = intent.getExtras();
			carNameTv.setText(bundle.getString("carName"));
			carTotalPath.setText(bundle.getString("carTotalPath"));
			carFrameNum.setText(bundle.getString("carFrameNum"));
			carEngNum.setText(bundle.getString("carEngNum"));
			carNum.setText(bundle.getString("carNum"));
			carPrice.setText(bundle.getString("carPrice"));
			carBuyTime.setText(bundle.getString("carBuyTime"));
			carUserName.setText(bundle.getString("carUserName"));
			car_id=bundle.getString("car_id");
			category_id=bundle.getString("category_id");
			 Log.e("Bundle", "EditCarInfoActivity"+bundle);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUESTCODE_CARINFO && resultCode == RESULT_OK) {
			String number = data.getStringExtra("carName");
			Log.e("number", "number" + number);
			//Toast.makeText(this, ""+number, Toast.LENGTH_SHORT).show();
		}
	}

	public void backToMycarPortEdit(View v) {
		finish();
	}

	public void insureToSubmit(View v) {

		Intent intent = new Intent(EditCarInfoActivity.this, MyCarPortActivity.class);
		intent.putExtra("carNameTv", carNameTv.getText().toString());
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
		intent.putExtra("item_position", item_position_id);
		
		/**
		 * 返回汽车头像见上面的intent 也要传送过去
		 */
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("car_id", car_id);
		params.addBodyParameter("total_mileage", carTotalPath.getText().toString());
		params.addBodyParameter("buy_money", carPrice.getText().toString());
		params.addBodyParameter("car_number", carNum.getText().toString());
		params.addBodyParameter("car_owner", carUserName.getText().toString());
		params.addBodyParameter("buy_date", carBuyTime.getText().toString());
		params.addBodyParameter("category_id", category_id);
		params.addBodyParameter("engine_number", carEngNum.getText().toString());
		params.addBodyParameter("carriage_number", carFrameNum.getText().toString());
		httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException e, String s) { Toast.makeText(getApplicationContext(), "数据获取失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(EditCarInfoActivity.this, "请登录", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(EditCarInfoActivity.this, ShopLoginActivity.class);
						startActivity(intent);
					} else {

					}
					Log.e("hello", "+++++" + jsonObj.getString("msg"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		startActivity(intent);

		finish();

	}

}
