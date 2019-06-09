package com.dlut.tec2ksim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class InfoActivity extends Activity {

    ListView commandList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        commandList = (ListView) findViewById(R.id.lv_commandList);
        String[] listString = new String[]{"1、R命令：寄存器察看和修改", "2、D命令：内存察看",
                "3、E命令：内存修改", "4、G命令：程序运行", "5、T命令：单步执行（跟踪进入子程序）",
                "6、A命令：汇编用户输入的程序", "7、U命令：反汇编内存中的程序"};

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();/* 在数组中存放数据 */
        for (int i = 0; i < listString.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemText", "\t" + listString[i]);
            listItem.add(map);
        }
        commandList.setAdapter(new SimpleAdapter(this, listItem,
                R.layout.command_list_item, new String[]{"ItemText"},
                new int[]{R.id.tv_commandList}));
    }
}
