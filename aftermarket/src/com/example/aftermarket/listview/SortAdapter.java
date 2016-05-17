package com.example.aftermarket.listview;

import java.util.List;

import com.example.aftermarket.R;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter implements SectionIndexer{
	private List<SortModel> list = null;
	private Context mContext;
	
	public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}
	
	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.car_category_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.car_title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.car_catalog);
			viewHolder.carImage=(ImageView) view.findViewById(R.id.car_img);
			viewHolder.line=view.findViewById(R.id.listview_car_line);
			viewHolder.textLine=view.findViewById(R.id.textview_line);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//����position��ȡ���������ĸ��Char asciiֵ
		int section = getSectionForPosition(position);
		
		//�����ǰλ�õ��ڸ÷�������ĸ��Char��λ�� ������Ϊ�ǵ�һ�γ���
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.textLine.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
			viewHolder.line.setVisibility(View.GONE);
			viewHolder.textLine.setVisibility(View.GONE);
		}
	
		viewHolder.tvTitle.setText(this.list.get(position).getName());
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/x-utils";
		BitmapUtils bitmapUtils = new BitmapUtils(mContext, path, cacheSize);
		bitmapUtils.configDefaultBitmapMaxSize(100, 100);
		bitmapUtils.display(viewHolder.carImage, this.list.get(position).getImg());
		//bitmapUtils.display(viewHolder.carImage, "http://jk1.cpioc.com/Upload/ad/start.jpg");		
		//viewHolder.carImage.setImageDrawable(this.list.get(position).getDraw());
		return view;

	}
	


	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		ImageView carImage;
		View line;
		View textLine;
	}


	/**
	 * ����ListView�ĵ�ǰλ�û�ȡ���������ĸ��Char asciiֵ
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * ���ݷ��������ĸ��Char asciiֵ��ȡ���һ�γ��ָ�����ĸ��λ��
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * ��ȡӢ�ĵ�����ĸ����Ӣ����ĸ��#���档
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}