package com.example.aftermarket.adpter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chatuidemo.activity.ChatActivity;
import com.example.aftermarket.DemoApplication;
import com.example.aftermarket.R;
import com.example.aftermarket.adpter.MyCollectionListAdapter.ViewHolder;
import com.example.aftermarket.bean.Address;
import com.example.aftermarket.bean.AddressInfo;
import com.example.aftermarket.config.ConstantClass;
import com.example.aftermarket.ui.EditAddrActivity;
import com.example.aftermarket.ui.InputOrderActivity;
import com.example.aftermarket.ui.ReceiptAddressActivity;
import com.example.aftermarket.ui.ShopLoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiptAddressListAdapter extends BaseAdapter {

	private static final String REQUEST_HEADER_DEL = "addressDel";
	private static final String REQUEST_HEADER_DEFAULT = "setDefaultAddress";
	private Context context;
	private LayoutInflater inflater;
	private Map<String, Object> addressMap;
	private List<AddressInfo> addressList;
	private DemoApplication app;
	private Activity activity;
	private static int defaultAddr = 0;
	ArrayList<Boolean> defaultAddrList;

	public ReceiptAddressListAdapter(Context context, List<AddressInfo> addressList, ArrayList<Boolean> defaultAddrList) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.addressList = addressList;
		app = (DemoApplication) context.getApplicationContext();
		this.activity = activity;
		this.defaultAddrList = defaultAddrList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return addressList.size() > 0 ? addressList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return addressList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vh;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = inflater.inflate(R.layout.new_addr_list_item, null);
			vh.receiptName = (TextView) convertView.findViewById(R.id.receipt_name);
			vh.receiptTel = (TextView) convertView.findViewById(R.id.receipt_tel);
			vh.receiptAddrDefault = (TextView) convertView.findViewById(R.id.receipt_addr_default);
			vh.defaultAddrIv = (RadioButton) convertView.findViewById(R.id.default_addr_iv);
			vh.AddrTv = (TextView) convertView.findViewById(R.id.addr_tv_textview);
			vh.addrDelBt = (RelativeLayout) convertView.findViewById(R.id.addr_edit_bt);
			vh.addrEditBt = (RelativeLayout) convertView.findViewById(R.id.addr_del_bt);
			vh.addrItemBt = (LinearLayout) convertView.findViewById(R.id.addr_layout_id);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.receiptName.setText(addressList.get(position).consignee);
		vh.receiptTel.setText(addressList.get(position).mobile);
		if (1 == addressList.get(position).is_default) {
			vh.receiptAddrDefault.setVisibility(View.VISIBLE);
			vh.defaultAddrIv.setChecked(true);
			// setDefaultAddress(addressList.get(position).address_id);
		} else {
			vh.receiptAddrDefault.setVisibility(View.GONE);
			vh.defaultAddrIv.setChecked(false);
		}
		vh.AddrTv.setText(addressList.get(position).full_address);

		vh.addrDelBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				delData(addressList.get(position).address_id, position);
				if (addressList.get(position).is_default == 1) {
					if (0 != defaultAddrList.size()) {

					}
				}
			}
		});

		vh.addrEditBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, EditAddrActivity.class);
				intent.putExtra("address_id", addressList.get(position).address_id);
				intent.putExtra("addressInfo", addressList.get(position));
				context.startActivity(intent);

			}
		});
		if (0 != defaultAddrList.size()) {
			if (defaultAddrList.get(position) == false) {
				vh.defaultAddrIv.setChecked(false);
				vh.defaultAddrIv.setClickable(true);
				vh.receiptAddrDefault.setVisibility(View.GONE);
			} else {
				vh.defaultAddrIv.setChecked(true);
				vh.defaultAddrIv.setClickable(false);
				vh.receiptAddrDefault.setVisibility(View.VISIBLE);
			}
		}

		vh.defaultAddrIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vh.defaultAddrIv.setChecked(true);
				for (int i = 0; i < defaultAddrList.size(); i++) {
					defaultAddrList.set(i, false);
				}
				if (0 != defaultAddrList.size()) {
					defaultAddrList.set(position, true);
					vh.receiptAddrDefault.setVisibility(View.VISIBLE);
					setDefaultAddress(addressList.get(position).address_id);
					notifyDataSetChanged();
				}

			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView receiptName;
		TextView receiptTel;
		TextView receiptAddrDefault;
		TextView AddrTv;
		RadioButton defaultAddrIv;
		RelativeLayout addrDelBt;
		RelativeLayout addrEditBt;
		LinearLayout addrItemBt;

	}

	private void delData(String address_id, final int position) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_DEL;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("address_id", address_id);
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
					java.lang.reflect.Type type = new TypeToken<Address>() {
					}.getType();
					addressList.remove(position);
					if (0 != defaultAddrList.size()) {
						defaultAddrList.remove(position);
					}
					notifyDataSetChanged();

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

	private void setDefaultAddress(String address_id) {
		HttpUtils httpUtils = new HttpUtils();
		String url = ConstantClass.NET_URL + REQUEST_HEADER_DEFAULT;
		RequestParams params = new RequestParams();
		params.addBodyParameter("token", app.getToken());
		params.addBodyParameter("address_id", address_id);
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
					java.lang.reflect.Type type = new TypeToken<Address>() {
					}.getType();

				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					if ("1".equals(String.valueOf(jsonObj.getInt("code")))) {

						// Toast.makeText(context, "设置成功",
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
}
