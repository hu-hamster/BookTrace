package com.jnu.booktrace.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jnu.booktrace.R;
import com.jnu.booktrace.bean.Person;
import com.jnu.booktrace.database.DBManager;
import com.jnu.booktrace.database.DatabaseManager;

import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

/**
 * 作用：此activity用于实现BookTrace的注册功能
 * 功能：实现注册逻辑，将用户添加到数据库中
 **/
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private static int USER_EXIT=1, USER_NOEXIT=0;
    private EditText register_name,register_password,register_password2;
    private Button register_confirm;
    private Boolean userExist=true, JudgeFinish=false;
    private LinearLayout processbar;
    private String name, password, password2;
    public Boolean TestJudge;
    public RegisterActivity(){

    }


    public void setTestDate(String name, String password, String password2){
        this.name = name;
        this.password = password;
        this.password2 = password2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initComp();  //初始化控件
        //设置按钮监听事件
        register_confirm.setOnClickListener(this);

    }

    /**初始化控件*/
    private void initComp() {
        register_name = findViewById(R.id.register_name);
        register_password = findViewById(R.id.register_password);
        register_password2 = findViewById(R.id.register_password2);
        register_confirm = findViewById(R.id.register_confirm);
        processbar = findViewById(R.id.register_processBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_confirm:   //注册按钮
                Confirm();
                break;
        }
    }

    public void Confirm() {
        Person person = new Person();
        Log.e("test222", "Confirm: 111");
        if(name ==null && password == null && password2 == null){
            name = register_name.getText().toString();
            password = register_password.getText().toString();
            password2 = register_password2.getText().toString();
        }
        if(name.equals("")||password.equals("")||password2.equals("")){
            Toast.makeText(RegisterActivity.this,"请输入完整的信息",Toast.LENGTH_SHORT).show();
            TestJudge= false;
        }
        else{
            if(!password.equals(password2)){//判断两次输入的密码是否一致
                Toast.makeText(RegisterActivity.this,"两次输入的密码不一致，请重新输入",
                        Toast.LENGTH_SHORT).show();
                TestJudge= false;
            } else{//判断现在添加的用户是否以及存在，不存在则添加
                processbar.setVisibility(View.VISIBLE);

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Boolean r  = DatabaseManager.judgePersonExist(name);
                        if(r){
                            userExist = true;
                        }else{
                            userExist =false;
                        }
                    }
                });
                thread.start();
                while (true){
                    try {
                        thread.join();
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(!userExist){
                    person.setName(name);
                    person.setPassword(password);
                    new Thread(()->{
                        DatabaseManager.insertPersontb(person);
                        DBManager.deletePersontb();
                        DBManager.insertPersontb(person);

                    }).start();
                    Toast.makeText(RegisterActivity.this,"注册成功",
                            Toast.LENGTH_LONG).show();
                    TestJudge= true;
                    Intent intent = new Intent(this,LoginActivity.class);
                    startActivity(intent);

                    finish();
                }else{
                    processbar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this,"用户存在，请重新输入",Toast.LENGTH_SHORT).show();
                    TestJudge= false;
                    register_name.setText("");
                    register_password.setText("");
                    register_password2.setText("");
                }
            }
        }
    }
}