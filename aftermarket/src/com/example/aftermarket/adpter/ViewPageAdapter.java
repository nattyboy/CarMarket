package com.example.aftermarket.adpter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPageAdapter extends PagerAdapter {
	ArrayList<View> views;

	public ViewPageAdapter(ArrayList<View> views) {
		this.views = views;

	}

	@Override
	public int getCount() {
		return views.size()>0?views.size():0;
	}

	// 实例化选项卡
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View v = views.get(position);
		container.addView(v);
		return v;
	}

	// 删除选项卡
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(views.get(position));
	}

	// 判断视图是否为返回的对象
	@Override
	public boolean isViewFromObject(View view, Object o) {
		return view == o;
	}

}
