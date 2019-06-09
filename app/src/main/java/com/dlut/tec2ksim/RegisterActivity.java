package com.dlut.tec2ksim;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
    EditText et_r0;
    EditText et_r1;
    EditText et_r2;
    EditText et_r3;
    EditText et_r4;
    EditText et_r5;
    EditText et_r6;
    EditText et_r7;
    EditText et_r8;
    EditText et_r9;
    EditText et_r10;
    EditText et_r11;
    EditText et_r12;
    EditText et_r13;
    EditText et_r14;
    EditText et_r15;

    Button changeRegister;
    Button returnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final ArrayList<String> registers = getIntent().getStringArrayListExtra(
                "Registers");

        et_r0 = (EditText) findViewById(R.id.et_r0);
        et_r1 = (EditText) findViewById(R.id.et_r1);
        et_r2 = (EditText) findViewById(R.id.et_r2);
        et_r3 = (EditText) findViewById(R.id.et_r3);
        et_r4 = (EditText) findViewById(R.id.et_r4);
        et_r5 = (EditText) findViewById(R.id.et_r5);
        et_r6 = (EditText) findViewById(R.id.et_r6);
        et_r7 = (EditText) findViewById(R.id.et_r7);
        et_r8 = (EditText) findViewById(R.id.et_r8);
        et_r9 = (EditText) findViewById(R.id.et_r9);
        et_r10 = (EditText) findViewById(R.id.et_r10);
        et_r11 = (EditText) findViewById(R.id.et_r11);
        et_r12 = (EditText) findViewById(R.id.et_r12);
        et_r13 = (EditText) findViewById(R.id.et_r13);
        et_r14 = (EditText) findViewById(R.id.et_r14);
        et_r15 = (EditText) findViewById(R.id.et_r15);

        changeRegister = (Button) findViewById(R.id.bt_changeRegister);
        returnRegister = (Button) findViewById(R.id.bt_return);

        et_r0.setText(registers.get(0));
        et_r1.setText(registers.get(1));
        et_r2.setText(registers.get(2));
        et_r3.setText(registers.get(3));
        et_r4.setText(registers.get(4));
        et_r5.setText(registers.get(5));
        et_r6.setText(registers.get(6));
        et_r7.setText(registers.get(7));
        et_r8.setText(registers.get(8));
        et_r9.setText(registers.get(9));
        et_r10.setText(registers.get(10));
        et_r11.setText(registers.get(11));
        et_r12.setText(registers.get(12));
        et_r13.setText(registers.get(13));
        et_r14.setText(registers.get(14));
        et_r15.setText(registers.get(15));

        changeRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                registers.set(0, et_r0.getText().toString());
                registers.set(1, et_r1.getText().toString());
                registers.set(2, et_r2.getText().toString());
                registers.set(3, et_r3.getText().toString());
                registers.set(4, et_r4.getText().toString());
                registers.set(5, et_r5.getText().toString());
                registers.set(6, et_r6.getText().toString());
                registers.set(7, et_r7.getText().toString());
                registers.set(8, et_r8.getText().toString());
                registers.set(9, et_r9.getText().toString());
                registers.set(10, et_r10.getText().toString());
                registers.set(11, et_r11.getText().toString());
                registers.set(12, et_r12.getText().toString());
                registers.set(13, et_r13.getText().toString());
                registers.set(14, et_r14.getText().toString());
                registers.set(15, et_r15.getText().toString());

                Intent data = new Intent();
                data.putStringArrayListExtra("RegistersBack", registers);
                RegisterActivity.this.setResult(RESULT_OK, data);
                finish();
            }
        });

        returnRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });
    }
}
