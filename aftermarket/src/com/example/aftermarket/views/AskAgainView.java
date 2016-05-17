package com.example.aftermarket.views;

import com.example.aftermarket.R;
import com.example.aftermarket.bean.AskAgain;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by EASTMOOD on 2016/3/16.
 */
public class AskAgainView  extends LinearLayout {

    TextView type,msg;
    Context context;
    AskAgain askAgain;

    public AskAgainView(Context context,AskAgain askAgain) {
        super(context);
        this.context = context;
        this.askAgain = askAgain;
        init();
    }

    private void init() {
        View rootView = inflate(getContext(), R.layout.item_ask_again, this);

        type = (TextView) rootView.findViewById(R.id.type);
        msg = (TextView) rootView.findViewById(R.id.msg);

        type.setText(askAgain.type == 0 ?"追问：":"追答：");
        type.setTextColor(getResources().getColor(askAgain.type == 0? R.color.zhuida_red  : R.color.zhuiwen_blue ));
        msg.setText(askAgain.msg);
    }

}
