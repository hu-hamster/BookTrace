package com.jnu.booktrace.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jnu.booktrace.MainActivity;
import com.jnu.booktrace.R;
import com.jnu.booktrace.activity.StartPageActivity;
import com.jnu.booktrace.bean.Person;
import com.jnu.booktrace.database.DBManager;
import com.jnu.booktrace.database.DatabaseManager;
import com.jnu.booktrace.filehandle.FileHandle;

import java.util.concurrent.TimeUnit;

/**
 * 作用：此activity用于实现BookTrace的登录功能
 * 功能：登录界面判断，添加用户页面跳转
 * */
public class LoginActivity extends Activity implements View.OnClickListener {
    private static int USER_EXiST=1;
    private EditText nameEt, passwordEt;
    private Button confirmBt;
    private Boolean exist = false,finish = false;
    private LinearLayout processBar;
    private Person person;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComp();   //实现控件的初始化
        if(DBManager.JudgePersonIsNotNull()){
            Intent intent = new Intent(this, MainActivity.class);
            String name = DBManager.GetNameFromPersontb();
            nameEt.setText(name);
            passwordEt.setText("********");
            intent.putExtra("name",name);
            Log.i("标志位",  "用户为："+name);
            startActivity(intent);
        }
        String account = String.valueOf(nameEt.getText());
        confirmBt.setOnClickListener(this);
    }

    /**初始化控件*/
    private void initComp() {
        nameEt = findViewById(R.id.login_name);
        passwordEt = findViewById(R.id.login_pwd);
        confirmBt = findViewById(R.id.login_confirm);
        processBar = findViewById(R.id.login_processBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_confirm:

                String name = nameEt.getText().toString();
                String password = passwordEt.getText().toString();
                if(name.equals("")){//登录成功
                    Toast.makeText(LoginActivity.this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){ //密码为空
                    Toast.makeText(LoginActivity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                }else{
                    processBar.setVisibility(View.VISIBLE);
                    Boolean JudgePeopleInSqlite = DBManager.JudgePersonExist(name,password);
                    if(!JudgePeopleInSqlite){
                        JudgePeopleFromMysql(name,password);
                    }else{
                        finish = true;
                        exist = true;
                    }
                    if(finish){
                        if(exist){

                            if(!JudgePeopleInSqlite){
                                GetPeopleFromMysql(name);
                                FileHandle.WriteToFile(LoginActivity.this,"avatar.txt",person.getAvatar());
                                DBManager.insertPersontb(person);
                            }
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("name",name);

                            Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                        }else{
                            processBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this,"密码错误或用户不存在！",Toast.LENGTH_SHORT).show();
                            nameEt.setText("");
                            passwordEt.setText("");
                        }
                    }
                }
                break;
            case R.id.login_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    void JudgePeopleFromMysql(String name, String password){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Boolean judge = DatabaseManager.judgeLogin(name,password);
                Message message = new Message();
                if(judge){
                    message.what = USER_EXiST;
                    exist = true;
                }
                finish = true;
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
    }
    void GetPeopleFromMysql(String name){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                person = DatabaseManager.getPersonFromName(name);
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
    }

}

