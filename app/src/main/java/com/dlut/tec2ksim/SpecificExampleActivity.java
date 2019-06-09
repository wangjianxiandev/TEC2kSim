package com.dlut.tec2ksim;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SpecificExampleActivity extends Activity {
    TextView tv_specificExample_title;
    TextView tv_specificExample_example;
    TextView tv_specificExample_explanation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_example);
        tv_specificExample_title = (TextView) findViewById(R.id.tv_specificExample_title);
        tv_specificExample_example = (TextView) findViewById(R.id.tv_specificExample_example);
        tv_specificExample_explanation = (TextView) findViewById(R.id.tv_specificExample_explanation);

        tv_specificExample_example.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_specificExample_explanation.setMovementMethod(ScrollingMovementMethod.getInstance());

        ArrayList<String> specificExamples = getIntent().getStringArrayListExtra("specificExamples");
        tv_specificExample_title.setText(specificExamples.get(1));
        tv_specificExample_example.setText(specificExamples.get(2));
        tv_specificExample_explanation.setText(specificExamples.get(3));

        // 长按复制到剪切板
        tv_specificExample_example.setOnLongClickListener(new OnLongClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setText(tv_specificExample_example.getText().toString());
                Toast.makeText(SpecificExampleActivity.this, "已复制到剪切板", 0).show();
                return false;
            }
        });


    }
}
