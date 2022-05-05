package com.jnu.booktrace.person;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.jnu.booktrace.Login.LoginActivity;
import com.jnu.booktrace.MainActivity;
import com.jnu.booktrace.R;
import com.jnu.booktrace.database.DBManager;
import com.jnu.booktrace.database.DatabaseManager;
import com.jnu.booktrace.utils.AndroidBarUtils;
import com.mysql.jdbc.MiniAdmin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*
 * 个人中心
 * 功能：修改密码、修改个人介绍、修改昵称
 **/
public class PersonInfoActivity extends AppCompatActivity {
    private TextView gender, birth, nickname,description;
    private Intent intent;
    private Button changePassword, leave;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        intent = getIntent();

        //状态栏颜色更改
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        AndroidBarUtils.setBarDarkMode(this,true); //状态栏文字图标颜色为黑色

        ActionBar actionBar = getSupportActionBar();     //隐藏自带的标题栏
        if(actionBar!=null){
            actionBar.hide();
        }
        initFrag();
        setListener();
        initToolbar();
    }

    //重写返回事件，返回更新数据库
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //updatePerson();
    }

    private void initFrag() {
        nickname = findViewById(R.id.person_info_tv_nickname2);
        nickname.setText(MainActivity.person.getNickName());
        gender = findViewById(R.id.person_info_tv_gender2);
        gender.setText(MainActivity.person.getGender());
        birth = findViewById(R.id.person_info_tv_birth2);
        birth.setText(MainActivity.person.getBirth());
        description = findViewById(R.id.person_info_tv_description2);
        description.setText(MainActivity.person.getDescription());
        leave = findViewById(R.id.person_info_bt_leave);
        changePassword = findViewById(R.id.person_info_change_password);
    }

    private void initToolbar(){
        //隐藏默认actionbar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        //获取toolbar
        toolbar = findViewById(R.id.toolbar);
//        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        toolbar.setTitle("个人资料");
        //用toolbar替换actionbar
        setSupportActionBar(toolbar);
        //左侧按钮：可见+更换图标+点击监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示toolbar的返回按钮
        //toolBar.setNavigationIcon(R.mipmap.back_white);
        //返回键功能点击事件在这
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updatePerson();
                finish();
            }
        });
    }

    private void setListener() {
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!nickname.getText().toString().equals(MainActivity.person.getNickName())) {
                    MainActivity.person.setNickName(nickname.getText().toString());
                }
            }
        });
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDescriptionDialog();
            }
        });
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderDialog();
            }
        });
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBirthDialog();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(PersonInfoActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBManager.deletePersontb(); // 删除用户数据
                Intent intent = new Intent(PersonInfoActivity.this, LoginActivity.class);
                Toast.makeText(PersonInfoActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });
    }

    //更新个人数据
//    private void updatePerson(){
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                DatabaseManager.updatePersontb(MainActivity.person);
//
//            }
//        });
//        thread.start();
//        //明显卡顿感，不等了
//        while (true){
//            try {
//                thread.join();
//                break;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    //选择性别对话框
    private void showGenderDialog(){
        List<String> items = new ArrayList<>();
        items.add("未知");
        items.add("男");
        items.add("女");
        OptionsPickerView optionsPickerView = new OptionsPickerBuilder(PersonInfoActivity.this, new OnOptionsSelectListener(){
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                gender.setText(items.get(options1));
                MainActivity.person.setGender(items.get(options1));
            }
        }).setSelectOptions(0)
                .setOutSideCancelable(false)
                .build();
        optionsPickerView.setPicker(items);
        optionsPickerView.show();
    }

    //选择生日对话框
    private void showBirthDialog(){
        TimePickerView pvTime = new TimePickerBuilder(PersonInfoActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                birth.setText(format.format(date));
                MainActivity.person.setBirth(format.format(date));
            }
        }).build();
        pvTime.show();
    }

    public void showDescriptionDialog(){
        DescriptionDialog descriptionDialog = new DescriptionDialog(this);
        descriptionDialog.show();
        descriptionDialog.setEditText(MainActivity.person.getDescription());
        descriptionDialog.setDialogSize();
        descriptionDialog.setOnEnsureListener(new DescriptionDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String text = descriptionDialog.getEditText();  //获取输入的数据
                if(!TextUtils.isEmpty(text)){
                    description.setText(text);
                    MainActivity.person.setDescription(text);
                }
                descriptionDialog.cancel();
            }
        });
    }

    public void onClick(View view) { //设计头像的点击事件
        switch (view.getId()){
            case R.id.person_info_tv_avatar:
                intent = new Intent(PersonInfoActivity.this,ChangeAvatarActivity.class);
                startActivity(intent);
                break;
        }
    }
}