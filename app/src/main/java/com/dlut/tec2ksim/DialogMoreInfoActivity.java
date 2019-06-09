package com.dlut.tec2ksim;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DialogMoreInfoActivity extends Activity {
    TextView tv_format;
    TextView tv_search_instruction;
    TextView tv_search_operate_num;
    TextView tv_search_CZVS;
    TextView tv_search_style;
    TextView tv_search_explanation;
    LinearLayout lt_more_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_more_info);
        tv_format = (TextView) findViewById(R.id.tv_format);
        tv_search_instruction = (TextView) findViewById(R.id.tv_search_instruction);
        tv_search_operate_num = (TextView) findViewById(R.id.tv_search_operate_num);
        tv_search_CZVS = (TextView) findViewById(R.id.tv_search_CZVS);
        tv_search_style = (TextView) findViewById(R.id.tv_search_style);
        tv_search_explanation = (TextView) findViewById(R.id.tv_search_explanation);
        lt_more_info = (LinearLayout) findViewById(R.id.lt_more_info);

        ArrayList<String> moreInfo = getIntent().getStringArrayListExtra("instructionMore");

        tv_format.setText(moreInfo.get(0));
        tv_search_instruction.setText(moreInfo.get(1));
        tv_search_operate_num.setText(moreInfo.get(2));
        tv_search_CZVS.setText(moreInfo.get(3));
        tv_search_style.setText(moreInfo.get(5) + "组指令");
        tv_search_explanation.setText(moreInfo.get(4));
        lt_more_info.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return false;
            }
        });

    }
}
