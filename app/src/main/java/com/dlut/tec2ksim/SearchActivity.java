package com.dlut.tec2ksim;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SearchActivity extends Activity {
    private ListView lv_instruction;
    private List<String> instructions;
    private List<String> explanations;
    private ImageButton btn_serach;
    private EditText et_serach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        btn_serach = (ImageButton) findViewById(R.id.btn_serach);
        et_serach = (EditText) findViewById(R.id.et_search);
        lv_instruction = (ListView) findViewById(R.id.lv_instruction);

        instructions = InstructionDB.getInstructions();
        explanations = InstructionDB.getExplanations();
        final InstructionAdapter instructionAdapter = new InstructionAdapter(
                this, instructions, explanations);
        lv_instruction.setAdapter(instructionAdapter);
        lv_instruction.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TextView tv_instruction = (TextView) view
                        .findViewById(R.id.tv_instruction);
                String instruction = tv_instruction.getText().toString();
                ArrayList<String> instructionMore = (ArrayList<String>) InstructionDB.getInstructionMore(instruction);
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this,
                        DialogMoreInfoActivity.class);
                intent.putStringArrayListExtra("instructionMore", instructionMore);
                startActivity(intent);

            }
        });

        btn_serach.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchString = et_serach.getText().toString();
                if (searchString != "") {
                    instructions = InstructionDB
                            .getSearchInstruction(searchString);
                    explanations = InstructionDB
                            .getSearchExplanation(searchString);
                    instructionAdapter.notifyDataSetChanged(instructions,
                            explanations);
                } else {
                    instructions = InstructionDB.getInstructions();
                    explanations = InstructionDB.getExplanations();
                    instructionAdapter.notifyDataSetChanged(instructions,
                            explanations);
                }
                et_serach.setText("");
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(SearchActivity.this
                                .getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

    }
}
