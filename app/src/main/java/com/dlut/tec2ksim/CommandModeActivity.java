package com.dlut.tec2ksim;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dlut.tec2kSimVM.Tec2kVM16;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CommandModeActivity extends Activity {
    private Button send;
    public TextView tv;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commandmode);
        send = (Button) findViewById(R.id.bt_send);
        tv = (TextView) findViewById(R.id.tv_show);
        et = (EditText) findViewById(R.id.et);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        final Tec2kVM16 tec = new Tec2kVM16();
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    if (tv.getText().toString().split("\n").length == 11)
                        tv.setGravity(Gravity.BOTTOM);
                    Log.d("CommandModeActivity","result is "+tec.getOutString().toString());
                    tv.setText(tec.getOutString());
                    msg.what = 0;
                }
            }
        };
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

                tec.setHandler(handler);
                tec.Run(0);
            }

        });
        thread.start();

        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String text = et.getText().toString() + (char) 0xd;
                for (int i = 0; i < text.length(); i++) {
                    while (tec.isHasUnreadKey() == true) {
                    }
                    tec.KeyPressed((char) (text.charAt(i) & 0xff));
                }
                et.setText("");
            }
        });

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
