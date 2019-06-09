package com.dlut.tec2ksim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;

public class LauncherActivity extends Activity {
    public static final String path = "/data/data/com.dlut.tec2ksim/files/AssemblyInstruction.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                File file = new File(path);
                if (!file.exists()) {
                    copyInstructionDB();
                }
                Intent intent = new Intent(LauncherActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(task, 1000);

    }

    /**
     * 释放 数据库文件到系统目录
     */
    private void copyInstructionDB() {
        final File file = new File(getFilesDir(), "AssemblyInstruction.db");
        if (file.exists() && file.length() > 0) {
            // do nothing
        } else {
            new Thread() {
                public void run() {
                    try {
                        InputStream is = getAssets().open(
                                "AssemblyInstruction.db");
                        File f = copyFile(is, file.getAbsolutePath());
                        if (f != null) {
                            // 拷贝成功.
                        } else {
                            // fail
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        // fail
                    }
                }

                ;
            }.start();
        }

    }

    public static File copyFile(InputStream is, String destPath) {
        try {
            File file = new File(destPath);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            is.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
