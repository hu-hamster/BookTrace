package com.jnu.booktrace.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.jnu.booktrace.Login.LoginActivity;
import com.jnu.booktrace.R;
import com.jnu.booktrace.bean.Drift;
import com.jnu.booktrace.database.DBManager;
import com.jnu.booktrace.database.DatabaseManager;
import com.jnu.booktrace.filehandle.FileHandle;

import java.io.File;
import java.util.List;

public class StartPageActivity extends Activity {
    private String Avatar_Filename = "avatar.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);


        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(1000);
                    List<Drift> drifts = DatabaseManager.GetDrift();
                    DBManager.UpdateDrift(drifts);
                    Intent it = new Intent(StartPageActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
//        while(true){
//            try {
//                myThread.join();
//                break;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
}