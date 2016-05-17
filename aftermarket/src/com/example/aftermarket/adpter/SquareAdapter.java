package com.example.aftermarket.adpter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.example.aftermarket.R;
import com.example.aftermarket.photo.activity.AskToRefundActivity;
import com.example.aftermarket.photo.activity.FeedBackActivity;
import com.example.aftermarket.ui.BeautifulActivity;
import com.example.aftermarket.ui.DiagnosisActivity;
import com.example.aftermarket.ui.MaintenanceActivity;
import com.example.aftermarket.ui.RepairActivity;
import com.example.aftermarket.ui.SheetSprayActivity;
import com.example.aftermarket.ui.ToEvaluteOrderActivity;
import com.example.aftermarket.ui.TyreActivity;
import com.example.aftermarket.ui.UsedCarActivity;
import com.example.aftermarket.ui.WashCarActivity;
import com.roamer.ui.view.SpaceImageDetailActivity;

public class SquareAdapter extends BaseAdapter {

	private List<String> list = new ArrayList();
	private Context mContext;
	private int status = 100;

	public SquareAdapter(Context context, int status) {
		// 测试数据
		this.mContext = context;
		this.status = status;
	}
	
	public List<String> imageList(){
		
		return list;
		
	}

	public void addData(String data) {
		list.add(data);
		notifyDataSetChanged();
	}

	public void addData(List<String> data) {
		for (String d : data) {
			list.add(d);
		}
		notifyDataSetChanged();
	}

	public List<String> getList() {
		return this.list;
	}

	@Override
	public int getCount() {
		int count;
		if (status == 0 || status == 2) {
			count = (list.size() + 1) > 10 ? 10 : (list.size() + 1);
		} else {
			count = list.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder vr;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_square, null);
			vr = new ViewHolder();
			vr.ivPic = (ImageView) convertView.findViewById(R.id.ivPic);
			vr.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);

			convertView.setTag(vr);
		} else {
			vr = (ViewHolder) convertView.getTag();
		}

		String str = (position < list.size()) ? list.get(position) : "添加";
		
		if (str.equals("添加")) {
			vr.ivDelete.setVisibility(View.GONE);
			Glide.with(mContext).load("res:///" + R.drawable.shangchuanaiche).into(vr.ivPic);

			vr.ivPic.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 选择图片
					if (mContext instanceof WashCarActivity) {
						((WashCarActivity) mContext).showPopUp();
					}
					if (mContext instanceof BeautifulActivity) {
						((BeautifulActivity) mContext).showPopUp();
					}
					if (mContext instanceof RepairActivity) {
						((RepairActivity) mContext).showPopUp();
					}
					if (mContext instanceof MaintenanceActivity) {
						((MaintenanceActivity) mContext).showPopUp();
					}
					if (mContext instanceof SheetSprayActivity) {
						((SheetSprayActivity) mContext).showPopUp();
					}
					if (mContext instanceof DiagnosisActivity) {
						((DiagnosisActivity) mContext).showPopUp();
					}
					if (mContext instanceof TyreActivity) {
						((TyreActivity) mContext).showPopUp();
					}
					if (mContext instanceof UsedCarActivity) {
						((UsedCarActivity) mContext).showPopUp();
					}
					if (mContext instanceof FeedBackActivity) {
						((FeedBackActivity) mContext).showPopUp();
					}
					if (mContext instanceof AskToRefundActivity) {
						((AskToRefundActivity) mContext).showPopUp();
					}
					if (mContext instanceof ToEvaluteOrderActivity) {
						((ToEvaluteOrderActivity) mContext).showPopUp();
					}
				}
			});

		} else {
			if (status == 0 || status == 2) {
				// 可以上传，不可以查看
				Glide.with(mContext).load("file://" + str).into(vr.ivPic);
				vr.ivDelete.setVisibility(View.VISIBLE);
				vr.ivPic.setOnClickListener(null);
				vr.ivDelete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						// mContext.deletePhoto(list.get(position));//同时删除保存的数据
						list.remove(list.get(position));// 同时在gridview里删除
						notifyDataSetChanged();
					}
				});
			} else {
				// 可以查看，不可以上传
				Glide.with(mContext).load(str).into(vr.ivPic);
				vr.ivDelete.setVisibility(View.GONE);
				vr.ivPic.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						Intent intent = new Intent(mContext, SpaceImageDetailActivity.class);
						intent.putExtra("images", list.get(position));
						intent.putExtra("position", position);
						int[] location = new int[2];
						vr.ivPic.getLocationOnScreen(location);
						intent.putExtra("locationX", location[0]);
						intent.putExtra("locationY", location[1]);

						intent.putExtra("width", vr.ivPic.getWidth());
						intent.putExtra("height", vr.ivPic.getHeight());
						mContext.startActivity(intent);
						((Activity) mContext).overridePendingTransition(0, 0);
					}
				});
			}
		}
		
		
		return convertView;
	}

	class ViewHolder {
		ImageView ivPic, ivDelete;
	}

}