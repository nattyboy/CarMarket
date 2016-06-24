package com.example.aftermarket.adpter;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.bean.Order;
import com.example.aftermarket.bean.OrderItem;
import com.example.aftermarket.bean.OrderItemR;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.photo.activity.AskToRefundActivity;
import com.example.aftermarket.ui.AskRefundActivity;
import com.example.aftermarket.ui.OrderInfoDetail;
import com.example.aftermarket.ui.OrderItemActivity;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.example.aftermarket.ui.ToEvaluteOrderActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pingplusplus.libone.PayResultCallBack;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToRefundListAdapter extends BaseAdapter {
	private static final String REQUEST_HEADER_REMOVE = "removeOrder";
	private Map<String, Object> carToServer;
	private Context context;
	private List<OrderItemR> orderList;
	private int orderId;
	private DemoApplication app;
	private OrderItemActivity activity;
	/**
	 * 支付所用变量
	 */
	private static String YOUR_URL = "http://218.244.151.190/demo/charge";
	public static String URL = YOUR_URL;

	private static final int REQUEST_CODE_PAYMENT = 1;

	/**
	 * 银联支付渠道
	 */
	private static final String CHANNEL_UPACP = "upacp";
	/**
	 * 微信支付渠道
	 */
	private static final String CHANNEL_WECHAT = "wx";
	/**
	 * 支付支付渠道
	 */
	private static final String CHANNEL_ALIPAY = "alipay";
	/**
	 * 百度支付渠道
	 */
	private static final String CHANNEL_BFB = "bfb";
	/**
	 * 京东支付渠道
	 */
	private String payWay = CHANNEL_WECHAT;
	private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";
	private static final String REQUEST_HEADER = "orderSubmit";
	protected static final int GOTOPAY = 0x1;
	private static final int RESULT_OK_CAr = 0x20;
	private static final String REQUEST_HEADER_GOPAY = "orderPay";
	private EditText amountEditText;
	private String currentAmount = "";

	public ToRefundListAdapter(Context context, List<OrderItemR> orderList) {
		this.context = context;
		this.orderList = orderList;
		this.orderId = orderId;
		this.activity = (OrderItemActivity) context;
		app = (DemoApplication) activity.getApplication();
	}

	@Override
	public int getCount() {
		return orderList.size() > 0 ? orderList.size() : 0;
	}

	@Override
	public Object getItem(int position) {

		return orderList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.car_to_server_item, null);
			vh.carToServerImageView = (ImageView) convertView.findViewById(R.id.car_to_server_imageView);
			vh.carSellerName = (TextView) convertView.findViewById(R.id.car_seller_name);
			vh.carSellerMoney = (TextView) convertView.findViewById(R.id.car_seller_money);
			vh.carAskReturn = (Button) convertView.findViewById(R.id.car_ask_return);
			vh.carInsurePay = (Button) convertView.findViewById(R.id.car_insure_pay);
			vh.backTv = (TextView) convertView.findViewById(R.id.textView_back);
			vh.buttonLayout = (RelativeLayout) convertView.findViewById(R.id.button_relative);
			vh.orderItem = (RelativeLayout) convertView.findViewById(R.id.order_item_layout);
			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.backTv.setVisibility(View.GONE);
		vh.buttonLayout.setVisibility(View.VISIBLE);
		if (("0".equals((orderList.get(position).pay_status))) && ("0".equals((orderList.get(position).order_status)))) {
			vh.carAskReturn.setText("删除订单");
			vh.carInsurePay.setText("去支付");

			vh.carAskReturn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					loadDataDelOrder(orderList.get(position).order_id);
					orderList.remove(position);
					notifyDataSetChanged();

				}
			});
			vh.carInsurePay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					loadDataGoPay(orderList.get(position).order_id);
				}
			});
		}
		if (("1".equals(orderList.get(position).pay_status)) && ("0".equals(orderList.get(position).order_status))) {
			vh.carAskReturn.setText("申请退款");
			vh.carInsurePay.setText("确认支付");
			vh.carAskReturn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(context, AskToRefundActivity.class);
					intent.putExtra("order_id", orderList.get(position).order_id);
					intent.putExtra("back_money", orderList.get(position).order_amount);
					context.startActivity(intent);

				}
			});
			vh.carInsurePay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					loadDataGoPay(orderList.get(position).order_id);
				}
			});
		}

		if (("1".equals(orderList.get(position).pay_status) && ("5".equals(orderList.get(position).order_status)))) {
			vh.backTv.setVisibility(View.VISIBLE);
			vh.buttonLayout.setVisibility(View.GONE);
			vh.backTv.setText("商家已同意退款");
		}
		if (("1".equals(orderList.get(position).pay_status) && ("4".equals(orderList.get(position).order_status)))) {
			vh.backTv.setVisibility(View.VISIBLE);
			vh.buttonLayout.setVisibility(View.GONE);
			vh.backTv.setText("退款中");
		}
		if (("1".equals(orderList.get(position).pay_status) && ("6".equals(orderList.get(position).order_status)))) {
			vh.backTv.setVisibility(View.VISIBLE);
			vh.buttonLayout.setVisibility(View.GONE);
			vh.backTv.setText("商家不同意退款");
		}
		
		vh.orderItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, OrderInfoDetail.class);
				intent.putExtra("OrderItemR", orderList.get(position));
				context.startActivity(intent);

			}
		});
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/x-utils";
		BitmapUtils bitmapUtils = new BitmapUtils(context, path, cacheSize);
		bitmapUtils.configDefaultBitmapMaxSize(100, 100);
		bitmapUtils.display(vh.carToServerImageView, orderList.get(position).company_logo);
		vh.carSellerName.setText(orderList.get(position).company_name);
		vh.carSellerMoney.setText(orderList.get(position).order_amount+" 元");
		return convertView;
	}

	class ViewHolder {

		ImageView carToServerImageView;
		TextView carSellerName;
		TextView carSellerMoney;
		Button carAskReturn;
		Button carInsurePay;
		ImageView carEdit;
		TextView backTv;
		RelativeLayout buttonLayout;
		RelativeLayout orderItem;

	}

	private void loadDataDelOrder(String order_id) {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_REMOVE;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("order_id", order_id);
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
					java.lang.reflect.Type type = new TypeToken<Order>() {
					}.getType();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Toast.makeText(context, "删除成功",
						// Toast.LENGTH_SHORT).show();
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

	private void loadDataGoPay(String order_id) {

		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_GOPAY;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("order_id", order_id);
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
					java.lang.reflect.Type type = new TypeToken<Order>() {
					}.getType();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {
						// Toast.makeText(context, "删除成功",
						// Toast.LENGTH_SHORT).show();
						URL = jsonObj.getJSONObject("data").toString();
						((SureToPay) activity).goToPay(URL);
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

	public interface SureToPay {
		void goToPay(String s);
	}

}
