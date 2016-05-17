package com.example.aftermarket;


import com.example.aftermarket.views.PullToRefreshLayout;
import com.example.aftermarket.views.PullToRefreshLayout.OnRefreshListener;

import android.os.Handler;
import android.os.Message;

public class MyListener implements OnRefreshListener {

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		// ����ˢ�²���
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// ǧ������˸��߿ؼ�ˢ�������Ŷ��
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 5000);
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		// ���ز���
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// ǧ������˸��߿ؼ����������Ŷ��
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 5000);
	}

}
