package com.example.aftermarket.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.AddCarAdapter;
import com.example.aftermarket.adpter.SellerListAdapter;
import com.example.aftermarket.bean.CarInfo;
import com.example.aftermarket.bean.CarInfoInput;
import com.example.aftermarket.bean.Seller;
import com.example.aftermarket.bean.SellerInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.listview.NewListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyCarPortActivity extends Activity {

	private NewListView addCarLv;
	private Map<String, Object> CarPortMap = new HashMap<>();
	private List<Map<String, Object>> carPortList = new ArrayList<>();
	private AddCarAdapter addCarAdapter;
	private final static int REQUESTCODE_MYPORT = 0x2;
	private final static String REQUEST_HEADER = "carList";
	protected static final int RESULT_OK_CAR = 0x20;
	private DemoApplication app;
	private CarInfo carinfo;
	private List<CarInfoInput> myCarList = new ArrayList<>();
	private String tempChooseCarString = null;
	private String tempChooseCarStringWash = null;
	private String tempChooseCarStringbeauty = null;
	private String tempChooseCarStringrepair = null;
	private String tempChooseCarStringmaintenance = null;
	private String tempChooseCarStringsheet = null;
	private String tempChooseCarStringdiagnosis = null;
	private String tempChooseCarStringtyre = null;
	private String tempChooseCarStringused = null;
	private String tempChooseCarStringlogin = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycar_port_activity);
		app = (DemoApplication) this.getApplication();
		addCarLv = (NewListView) findViewById(R.id.add_car_lv);
		Intent intent = getIntent();
		tempChooseCarString = intent.getStringExtra("choose_car");
		tempChooseCarStringWash = intent.getStringExtra("choose_car_wash");
		tempChooseCarStringbeauty = intent.getStringExtra("choose_car_beauty");
		tempChooseCarStringrepair = intent.getStringExtra("choose_car_repair");
		tempChooseCarStringmaintenance = intent.getStringExtra("choose_car_maintenance");
		tempChooseCarStringsheet = intent.getStringExtra("choose_car_sheet");
		tempChooseCarStringdiagnosis = intent.getStringExtra("choose_car_diagnosis");
		tempChooseCarStringtyre = intent.getStringExtra("choose_car_tyre");
		tempChooseCarStringused = intent.getStringExtra("choose_car_used");
		tempChooseCarStringlogin=intent.getStringExtra("choose_car_login");
		addCarLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (null != tempChooseCarString) {
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					Intent intent = new Intent(MyCarPortActivity.this, InputOrderActivity.class);
					intent.putExtra("car_version", tv.getText().toString());
					intent.putExtra("car_id", myCarList.get(position).car_id);
					Log.e("car_version", "car_version" + tv.getText().toString());
					startActivity(intent);
					finish();
				}
				if (null != tempChooseCarStringWash) {
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					Intent intent = new Intent(MyCarPortActivity.this, WashCarActivity.class);
					intent.putExtra("car_version_wash", tv.getText().toString());
					Log.e("car_version", "car_version_wash" + tv.getText().toString());
					intent.putExtra("car_category_id_wash", myCarList.get(position).car_id);
					intent.putExtra("car_category_name_wash", myCarList.get(position).category_name);
					startActivity(intent);
					finish();
				}
				if (null != tempChooseCarStringbeauty) {
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					Intent intent = new Intent(MyCarPortActivity.this, BeautifulActivity.class);
					intent.putExtra("car_version_beauty", tv.getText().toString());
					Log.e("car_version", "car_version_beauty" + tv.getText().toString());
					intent.putExtra("car_category_id_beauty", myCarList.get(position).car_id);
					intent.putExtra("car_category_name_beauty", myCarList.get(position).category_name);
					startActivity(intent);
					finish();
				}
				if (null != tempChooseCarStringrepair) {
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					Intent intent = new Intent(MyCarPortActivity.this, RepairActivity.class);
					intent.putExtra("car_version_repair", tv.getText().toString());
					Log.e("car_version", "car_version_repair" + tv.getText().toString());
					intent.putExtra("car_category_id_repair", myCarList.get(position).car_id);
					intent.putExtra("car_category_name_repair", myCarList.get(position).category_name);
					startActivity(intent);
					finish();
				}
				if (null != tempChooseCarStringmaintenance) {
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					Intent intent = new Intent(MyCarPortActivity.this, MaintenanceActivity.class);
					intent.putExtra("car_version_maintenance", tv.getText().toString());
					Log.e("car_version", "car_version_maintenance" + tv.getText().toString());
					intent.putExtra("car_category_id_maintenance", myCarList.get(position).car_id);
					intent.putExtra("car_category_name_maintenance", myCarList.get(position).category_name);
					startActivity(intent);
					finish();
				}
				if (null != tempChooseCarStringsheet) {
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					Intent intent = new Intent(MyCarPortActivity.this, SheetSprayActivity.class);
					intent.putExtra("car_version_sheet", tv.getText().toString());
					Log.e("car_version", "car_version_sheet" + tv.getText().toString());
					intent.putExtra("car_category_id_sheet", myCarList.get(position).car_id);
					intent.putExtra("car_category_name_sheet", myCarList.get(position).category_name);
					startActivity(intent);
					finish();
				}
				if (null != tempChooseCarStringdiagnosis) {
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					Intent intent = new Intent(MyCarPortActivity.this, DiagnosisActivity.class);
					intent.putExtra("car_version_diagnosis", tv.getText().toString());
					Log.e("car_version", "car_version_diagnosis" + tv.getText().toString());
					intent.putExtra("car_category_id_diagnosis", myCarList.get(position).car_id);
					intent.putExtra("car_category_name_diagnosis", myCarList.get(position).category_name);
					startActivity(intent);
					finish();
				}
				if (null != tempChooseCarStringtyre) {
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					Intent intent = new Intent(MyCarPortActivity.this, TyreActivity.class);
					intent.putExtra("car_version_tyre", tv.getText().toString());
					Log.e("car_version", "car_version_tyre" + tv.getText().toString());
					intent.putExtra("car_category_id_tyre", myCarList.get(position).car_id);
					intent.putExtra("car_category_name_tyre", myCarList.get(position).category_name);
					startActivity(intent);
					finish();
				}
				if (null != tempChooseCarStringused) {
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					Intent intent = new Intent(MyCarPortActivity.this, UsedCarActivity.class);
					intent.putExtra("car_version_used", tv.getText().toString());
					Log.e("car_version", "car_version_used" + tv.getText().toString());
					intent.putExtra("car_category_id_used", myCarList.get(position).car_id);
					intent.putExtra("car_category_name_used", myCarList.get(position).category_name);
					startActivity(intent);
					finish();
				}
				if (null != tempChooseCarStringlogin) {
					
					Intent intent = new Intent(MyCarPortActivity.this, LoginAfterCarActivity.class);
					TextView tv = (TextView) view.findViewById(R.id.car_version);
					intent.putExtra("car_version_login", tv.getText().toString());
					Log.e("car_version", "car_version_login" + tv.getText().toString());
					intent.putExtra("car_category_id_login", myCarList.get(position).car_id);
					intent.putExtra("car_category_name_login", myCarList.get(position).category_name);
					startActivity(intent);
					finish();
				}
				SharedPreferences sp = getSharedPreferences("LoginAfterCarActivity", MODE_PRIVATE);
				Editor editor = sp.edit();
				TextView tv = (TextView) view.findViewById(R.id.car_version);
				editor.putString("car_Name", tv.getText().toString());
				//editor.putString("car_Img", myCarList.get(position).car);
				editor.putString("car_Id", myCarList.get(position).car_id);
				
				editor.commit();

			}
		});
		/*
		 * String carNameTv = intent.getStringExtra("carNameTv"); String
		 * carInfoPercent = intent.getStringExtra("carInfoPercent");
		 * Log.e("MyCarPortActivity", "MyCar+PortActivitycarName" + carNameTv);
		 */
		String item_position = intent.getStringExtra("item_position");
		Log.e("MyCarPortActivity", "item_position" + item_position);
	}

	private void initDAta() {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		Log.e("dajiyilian", "" + app.getToken());
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
					carinfo = gson.fromJson(result, type);

					if (null != carinfo.data) {
						myCarList = carinfo.data;
						addCarAdapter = new AddCarAdapter(MyCarPortActivity.this, myCarList);
						addCarLv.setAdapter(addCarAdapter);

					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if(null!=jsonObj){
						if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

						} else if ("2".equals(String.valueOf(jsonObj.getInt("code")))) {
							// Token过期请重新登录
							Toast.makeText(MyCarPortActivity.this, "请登录", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(MyCarPortActivity.this, ShopLoginActivity.class);
							startActivity(intent);
						} else {

						}
						Log.e("hello", "+++++" + jsonObj.getString("msg"));
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		initDAta();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

	}

	public void addCar(View view) {

		Intent intent = new Intent(MyCarPortActivity.this, AddCarInfoActivity.class);
		startActivityForResult(intent, REQUESTCODE_MYPORT);

	}

	public void backToMyCenter(View v) {
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
