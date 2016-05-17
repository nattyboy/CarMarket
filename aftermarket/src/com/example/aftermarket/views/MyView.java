package com.example.aftermarket.views;

import com.example.aftermarket.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MyView extends RelativeLayout {

	private ImageView ivHead;
	private ImageView ivContent;
	
	private OnItemClickListener mOnItemClickListener=null;
	private OnItemeditClickListener mOnItemeditClickListener=null;

	public MyView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		LayoutInflater mInflater = LayoutInflater.from(context);
		mInflater.inflate(R.layout.upload_view, this, true); // ע��˴����һ������Ϊ true
		ivHead = (ImageView) findViewById(R.id.imageView_head);
		ivContent = (ImageView) findViewById(R.id.imageView_content);
	}

	/**
	 * ����ͼƬ��Դ
	 */
	public void setImageResource(int resId) {
		ivContent.setImageResource(resId);
	}
	
	
	public void setOneditClickListener(OnItemeditClickListener listener)
    {
        this.mOnItemeditClickListener = listener;
        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	mOnItemeditClickListener.onImageHeadClick();
            }
        });
    }

	public void setOnClickListener(OnItemClickListener listener)
    {
        this.mOnItemClickListener = listener;
        ivContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	mOnItemClickListener.onImageContentClick();
            }
        });
    }
	/**
	 * ����¼��ӿ�
	 * 
	 * @author linc
	 * 
	 */
	public interface OnItemClickListener {
		public void onImageContentClick();
	}
	
	public interface OnItemeditClickListener {
		public void onImageHeadClick();
	}
}
