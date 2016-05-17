package com.example.aftermarket.adpter;

import java.util.ArrayList;
import java.util.List;

import com.example.aftermarket.R;
import com.example.aftermarket.adpter.MyQuestionAdapter.ViewHolder;
import com.example.aftermarket.bean.EvaluteDataItem;
import com.example.aftermarket.bean.MyQuestionData;
import com.example.aftermarket.ui.EvaluteOrderActivity;
import com.example.aftermarket.views.CircleImageView;
import com.example.aftermarket.views.NoScrollGridView;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class OrderEvaluteAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<EvaluteDataItem> evaluteDataItemList;
	private LayoutInflater inflater;
	private EvaluteOrderActivity activity;
	public OrderEvaluteAdapter(Context context, ArrayList<EvaluteDataItem> evaluteDataItemList) {

		this.context = context;
		this.evaluteDataItemList = evaluteDataItemList;
		inflater = LayoutInflater.from(context);
		this.activity=(EvaluteOrderActivity) context;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return evaluteDataItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return evaluteDataItemList.get(position);
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
			convertView = inflater.inflate(R.layout.evalute_item, null);
			vh = new ViewHolder();
			vh.evaluteBar = (RatingBar) convertView.findViewById(R.id.evalute_ratingBar);
			vh.evalute_rate_grade = (TextView) convertView.findViewById(R.id.evalute_rate_grade);
			vh.custom_option_time = (TextView) convertView.findViewById(R.id.custom_option_time);
			vh.textView_evalute_content = (TextView) convertView.findViewById(R.id.textView_evalute_content);
			vh.gridView_evalute_name = (NoScrollGridView) convertView.findViewById(R.id.gridView_evalute_name);
			vh.textView_ask_tel = (TextView) convertView.findViewById(R.id.textView_ask_tel);
			vh.textView_ask_name = (TextView) convertView.findViewById(R.id.textView_ask_name);
			vh.imageView_evalute = (CircleImageView) convertView.findViewById(R.id.imageView_evalute);

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.evaluteBar.setRating(Float.parseFloat(evaluteDataItemList.get(position).score));
		vh.evalute_rate_grade.setText(evaluteDataItemList.get(position).score + "分");
		vh.custom_option_time.setText(evaluteDataItemList.get(position).create_date);
		vh.textView_evalute_content.setText(evaluteDataItemList.get(position).score_content);
		String mobile=evaluteDataItemList.get(position).mobile;
		vh.textView_ask_tel.setText(mobile.substring(0,3)+"****"+mobile.substring(7,mobile.length()));
		vh.textView_ask_name.setText(evaluteDataItemList.get(position).user_name);
		loadImage(activity.getcmpLog(),vh.imageView_evalute);
		vh.gridView_evalute_name.setAdapter(new GridAdapter(context,evaluteDataItemList.get(position).img));
		return convertView;
	}

	static class ViewHolder {
		RatingBar evaluteBar;
		TextView evalute_rate_grade;
		TextView custom_option_time;
		TextView textView_evalute_content;
		NoScrollGridView gridView_evalute_name;
		TextView textView_ask_tel;
		TextView textView_ask_name;
		CircleImageView imageView_evalute;

	}

	class GridAdapter extends BaseAdapter {

		private Context context;
		private List<String> imgList;

		public GridAdapter(Context context, List<String> img) {

			this.context = context;
			this.imgList = img;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imgList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return imgList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolderGrid vhGrid;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.grid_image_evalute, null);
				vhGrid=new ViewHolderGrid();
				vhGrid.imageItem=(ImageView) convertView.findViewById(R.id.imageView_evalute);
				convertView.setTag(vhGrid);
			} else {
				vhGrid = (ViewHolderGrid) convertView.getTag();
			}
			int maxMemory = (int) Runtime.getRuntime().maxMemory();
			int cacheSize = maxMemory / 8;
			String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/x-utils";
			BitmapUtils bitmapUtils = new BitmapUtils(context, path, cacheSize);
			bitmapUtils.configDefaultBitmapMaxSize(100, 100);
			bitmapUtils.display(vhGrid.imageItem, imgList.get(position));
			return convertView;
		}

		class ViewHolderGrid {

			ImageView imageItem;
		}

	}
	
	private void loadImage(String uri,final CircleImageView userLogo) {
		ImageLoader.getInstance().loadImage(uri, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

				userLogo.setImageBitmap(loadedImage);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String str = null;
				switch (failReason.getType()) {
				case IO_ERROR: // 文件I/O错误
					str = "Input/Output error";
					break;
				case DECODING_ERROR: // 解码错误
					str = "Image can't be decoded";
					break;
				case NETWORK_DENIED: // 网络延迟
					str = "Downloads are denied";
					break;
				case OUT_OF_MEMORY: // 内存不足
					str = "Out Of Memory error";
					break;
				case UNKNOWN: // 原因不明
					str = "Unknown error";
					break;
				}
			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
			}
		});
	}

}
