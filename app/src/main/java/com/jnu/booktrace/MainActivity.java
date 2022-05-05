package com.jnu.booktrace;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jnu.booktrace.bean.Drift;
import com.jnu.booktrace.bean.Person;
import com.jnu.booktrace.database.DBManager;
import com.jnu.booktrace.database.DatabaseManager;
import com.jnu.booktrace.fragments.DriftFragment;
import com.jnu.booktrace.fragments.FreeTalkFragment;
import com.jnu.booktrace.fragments.LibraryFragment;
import com.jnu.booktrace.fragments.PersonFragment;
import com.jnu.booktrace.utils.AndroidBarUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMS_REQUEST_CODE = 100;

    BottomNavigationView bottomNavigationView;
    private List<Fragment> fragmentList;
    private LibraryFragment libraryFragment;
    private DriftFragment driftFragment;
    private FreeTalkFragment freeTalkFragment;
    private PersonFragment personFragment;
    private Intent intent;
    private String name;
    public static Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏颜色更改
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        AndroidBarUtils.setBarDarkMode(this,true); //状态栏文字图标颜色为黑色

        intent = getIntent();
        name = intent.getStringExtra("name");
        person = DBManager.getPersonFromName(name);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //每次更新drift数据库
                //if();
            }
        });
        thread.start();

        initFragment(); //将底部导航栏各按钮与对应Fragment绑定
        myRequestPermission(); //权限请求
    }



    private void myRequestPermission(){
        //6.0版本或以上需请求权限
        String[] permissions=new String[]{Manifest.permission.
                WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            requestPermissions(permissions,PERMS_REQUEST_CODE);
        }
    }

    private void initFragment() {
        bottomNavigationView = findViewById(R.id.bnv_menu);

        libraryFragment = new LibraryFragment();
        setFragment(libraryFragment);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.item_bottom_library){
                    if(libraryFragment == null){
                        libraryFragment = new LibraryFragment();
                    }
                    setFragment(libraryFragment);
                }
                else if(item.getItemId()==R.id.item_bottom_drift){
                    if(driftFragment == null){
                        driftFragment = new DriftFragment();
                    }
                    setFragment(driftFragment);
                }
                else if(item.getItemId()==R.id.item_bottom_freetalk){
                    if(freeTalkFragment == null){
                        freeTalkFragment = new FreeTalkFragment();
                    }
                    setFragment(freeTalkFragment);
                }
                else if(item.getItemId()==R.id.item_bottom_person){
                    if(personFragment == null){
                        personFragment = new PersonFragment();
                    }
                    setFragment(personFragment);
                }
                return true;
            }
        });
    }

    private void setFragment(Fragment fragment){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
//    //点击返回键直接退出程序
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (!isExit) {
//            isExit = true;
//            Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
//
//        } else {
//            this.finish();
//        }
//    }

}