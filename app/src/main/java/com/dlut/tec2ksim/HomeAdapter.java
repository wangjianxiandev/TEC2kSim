package com.dlut.tec2ksim;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdapter extends BaseAdapter {

    private static final String[] names = {"命令行模式", "文本模式", "汇编指令查询", "教程实例",
            "系统说明", "退出系统"};

    private static final int[] icons = {R.drawable.ic_commandmode, R.drawable.ic_textmode, R.drawable.ic_search
            , R.drawable.ic_example, R.drawable.ic_info, R.drawable.ic_close};
    private Context context;


    public HomeAdapter(Context context) {
        this.context = context;
    }

    // 返回有多少个条目
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 返回每一个位置对应的view对象.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.home_meun_item, null);
        TextView tv_name = (TextView) view.findViewById(R.id.ItemText);
        ImageView iv_icon = (ImageView) view.findViewById(R.id.ItemImage);
        tv_name.setText(names[position]);
        iv_icon.setImageResource(icons[position]);


        return view;
    }

}