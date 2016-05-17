package com.example.aftermarket.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * descreption:
 * company:
 * Created by vince on 15/2/2.
 */
public class NewListView extends ListView{

    public NewListView(Context context) {
        super(context);
    }

    public NewListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
