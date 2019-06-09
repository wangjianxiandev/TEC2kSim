package com.dlut.tec2ksim;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.dlut.tec2kSimVM.Tec2kVM16;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditeTextModeActivity extends Activity {
    Button bt_run;
    Button bt_viewRegister;
    Button bt_clean;
    EditText et_codeInput;
    TextView tv_result;
    Tec2kVM16 tec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edite_text_mode);
        bt_run = (Button) findViewById(R.id.bt_run);
        bt_viewRegister = (Button) findViewById(R.id.bt_viewRegister);
        bt_clean = (Button) findViewById(R.id.bt_clean);

        et_codeInput = (EditText) findViewById(R.id.et_codeInput);
        tv_result = (TextView) findViewById(R.id.tv_result);

        tv_result.setMovementMethod(ScrollingMovementMethod.getInstance());
        et_codeInput.setMovementMethod(ScrollingMovementMethod.getInstance());

        tec = new Tec2kVM16();
        // final Handler handler = new Handler() {
        // @Override
        // public void handleMessage(Message msg) {
        // super.handleMessage(msg);
        // if (msg.what == 1) {
        // et_codeInput.setText(tec.getOutString());
        // msg.what = 0;
        // }
        // }
        // };
        final Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                InputStream abpath = getClass().getResourceAsStream(
                        "/assets/MONITOR16.COD");
                byte[] byteFile = null;
                try {
                    byteFile = InputStreamToByte(abpath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tec.WriteToMemory(0, byteFile.length, byteFile);

                // tec.setHandler(handler);
                tec.Run(0);
            }

        });
        thread.start();

        bt_run.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String text = et_codeInput.getText().toString().toLowerCase();
                text = text.replace("\b", " ");
                String[] s = text.split("\n");
                text = "";
                for (int i = 0; i < s.length; i++) {
                    text += s[i].trim() + "\n";
                }
                int addr = 0;
                try {
                    addr = Integer.parseInt(s[0].trim().substring(2));
                } catch (Exception e) {
                    text = "a 2000\n" + text;
                    addr = 2000;
                }

                String deal = "";
                String[] split = text.split("\n");
                for (int i = 0; i < split.length; i++) {
                    if (split[i].lastIndexOf(";") != -1)
                        deal += split[i].substring(0, split[i].lastIndexOf(";")).trim() + "\n";
                    else
                        deal += split[i].trim() + "\n";

                }
                deal += "\n\ng " + addr + "\n";

                for (int i = 0; i < deal.length(); i++) {
                    if (deal.charAt(i) == '\n') {
                        while (tec.isHasUnreadKey() == true) {
                        }
                        tec.setHasUnreadKey(true);
                        tec.KeyPressed((char) 0xd);

                    } else {
                        while (tec.isHasUnreadKey() == true) {
                        }
                        tec.setHasUnreadKey(true);
                        tec.KeyPressed((char) (deal.charAt(i) & 0xff));
                    }
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String out = tec.getOutString().toString();
//				Log.e("mr", "out:" + out);
//				tv_result.setText(out);
                out = out.substring(0, out.length() - 160);
                try {
                    tv_result.setText(out.substring(out.lastIndexOf(">")));
                } catch (Exception e) {
                    Toast.makeText(EditeTextModeActivity.this,
                            "程序跑飞了！（忘了RET？）", 0).show();
                }

            }
        });

        bt_viewRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> registers = new ArrayList<String>();
                for (int i = 0; i < 16; i++) {
                    registers.add(Integer.toHexString(tec.getMemery(9742 + i)));
                }
                Intent intent = new Intent(EditeTextModeActivity.this,
                        RegisterActivity.class);
                intent.putExtra("Registers", registers);
                startActivityForResult(intent, 1);
            }
        });

        bt_clean.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                et_codeInput.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                ArrayList<String> registersBack = data.getStringArrayListExtra("RegistersBack");
                for (int i = 0; i < registersBack.size(); i++) {
//				Log.e("mr", "registersBack[i]:"+registersBack.get(i));
                    int num = Integer.parseInt(registersBack.get(i), 16);
                    tec.setMemery((9742 + i), num);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte data[] = bytestream.toByteArray();
        bytestream.close();
        return data;
    }
}
