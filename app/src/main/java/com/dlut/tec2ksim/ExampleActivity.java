package com.dlut.tec2ksim;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ExampleActivity extends Activity {
    ListView lv_example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        lv_example = (ListView) findViewById(R.id.lv_example);
        List<String> examples = InstructionDB.getExampleTitles();
        ;
        lv_example.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examples));

        lv_example.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ArrayList<String> instructionMore = (ArrayList<String>) InstructionDB.getSpecificExample(position + 1);
                Intent intent = new Intent();
                intent.setClass(ExampleActivity.this,
                        SpecificExampleActivity.class);
                intent.putStringArrayListExtra("specificExamples", instructionMore);
                startActivity(intent);

            }
        });
    }
}
