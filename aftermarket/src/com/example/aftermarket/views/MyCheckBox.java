package com.example.aftermarket.views;

import com.example.aftermarket.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by EASTMOOD on 2016/3/19.
 */
public class MyCheckBox extends RelativeLayout {

	Context context;
	AttributeSet attrs;
	CheckBox checkBox;
	ImageView imageView;
	String checkBoxText = null;
	

	public MyCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		this.attrs = attrs;
		init();
	}

	private void init() {
		View rootView = inflate(getContext(), R.layout.my_checkbox, this);
		int resourceId = -1;
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCheckBox);

		checkBox = (CheckBox) rootView.findViewById(R.id.checkBox);
		imageView = (ImageView) rootView.findViewById(R.id.imageView);

		for (int i = 0; i < typedArray.getIndexCount(); i++) {
			int attr = typedArray.getIndex(i);
			switch (attr) {
			case R.styleable.MyCheckBox_text:
				resourceId = typedArray.getResourceId(R.styleable.MyCheckBox_text, 0);
				checkBox.setText(resourceId > 0 ? typedArray.getResources().getText(resourceId)
						: typedArray.getString(R.styleable.MyCheckBox_text));
				break;
			case R.styleable.MyCheckBox_checked:
				resourceId = typedArray.getResourceId(R.styleable.MyCheckBox_checked, 0);
				checkBox.setChecked(resourceId > 0 ? typedArray.getResources().getBoolean(resourceId)
						: typedArray.getBoolean(R.styleable.MyCheckBox_checked, false));
				imageView.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
				break;
			}
		}

		rootView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("点击", checkBox.getText().toString());
				checkBox.setChecked(!checkBox.isChecked());
				imageView.setVisibility(checkBox.isChecked() ? View.VISIBLE : View.GONE);
			}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	public boolean isChecked() {
		return checkBox.isChecked();
	}

	public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
		checkBox.setOnCheckedChangeListener(listener);
	}

	public String getText() {

		return checkBox.getText().toString();

	}

	public void setText(String string) {

		checkBox.setText(string);
	}

	

}
