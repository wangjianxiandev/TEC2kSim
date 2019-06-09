package com.dlut.tec2ksim;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InstructionAdapter extends BaseAdapter {
    private Context context;
    private List<String> instructions;
    private List<String> explanations;

    public InstructionAdapter(Context context, List<String> instructions, List<String> explanations) {
        this.context = context;
        this.instructions = instructions;
        this.explanations = explanations;
    }

    // 返回有多少个条目
    @Override
    public int getCount() {
        return instructions.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public void notifyDataSetChanged(List<String> instructions, List<String> explanations) {
        this.instructions = instructions;
        this.explanations = explanations;
        super.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 返回每一个位置对应的view对象.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.instruction_list_item, null);
        TextView tv_instruction = (TextView) view.findViewById(R.id.tv_instruction);
        TextView tv_explanation = (TextView) view.findViewById(R.id.tv_explanation);
        tv_instruction.setText(instructions.get(position));
        tv_explanation.setText(explanations.get(position));

        return view;
    }

}