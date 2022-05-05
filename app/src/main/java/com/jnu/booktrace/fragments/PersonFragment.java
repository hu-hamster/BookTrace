package com.jnu.booktrace.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnu.booktrace.MainActivity;
import com.jnu.booktrace.R;
import com.jnu.booktrace.bean.Person;
import com.jnu.booktrace.database.DBManager;
import com.jnu.booktrace.database.DatabaseManager;
import com.jnu.booktrace.filehandle.FileHandle;
import com.jnu.booktrace.imagehandle.ImageHandle;
import com.jnu.booktrace.person.PersonInfoActivity;


public class PersonFragment extends Fragment implements View.OnClickListener {
    private TextView person_tv_name, person_tv_description;
    private ImageView person_iv_avatar;
    private LinearLayout person_bt_person, person_bt_drift,person_bt_topic,person_bt_collect;
    private Toolbar toolbar;

    @Override
    public void onResume() {
        super.onResume();
        setTopName();
        String avatar = FileHandle.ReadFromFile("avatar.txt");
        if(avatar.equals("")){
//            if(MainActivity.person.getGender().equals("女")){
//                person_iv_avatar.setImageResource(R.drawable.woman);
//            }else {
//                person_iv_avatar.setImageResource(R.drawable.man);
//            }
        }else{
            person_iv_avatar.setImageBitmap(ImageHandle.stringToBitmap(avatar));
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseManager.updatePersontb(MainActivity.person);
                DBManager.updatePersontb(MainActivity.person);
            }
        });
        thread.start();

//        while (true){
//            try {
//                thread.join();
//                break;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }

    public PersonFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        initFrag(view); //绑定组件
        setTopName();  //设置头顶姓名和个人描述
        setButtonClick(); //设置imageButton监听
        initToolbar(view);
        return view;
    }

    private void initToolbar(View v){
        //获取toolbar
        toolbar = v.findViewById(R.id.toolbar);
        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        toolbar.setTitle("");
        //用toolbar替换actionbar
        //getActivity().setSupportActionBar(toolbar);
    }

    private void setButtonClick() {
        person_bt_person.setOnClickListener(this);
        person_bt_drift.setOnClickListener(this);
        person_bt_topic.setOnClickListener(this);
        person_bt_collect.setOnClickListener(this);
    }

    private void setTopName() {
        if(MainActivity.person.getNickName().equals("")){
            person_tv_name.setText("未设置昵称");
        }else{
            person_tv_name.setText(MainActivity.person.getNickName());
        }

        person_tv_description.setText(MainActivity.person.getDescription());
//        //设置头像
//        if(MainActivity.person.getAvatar().equals("")){
//            if(MainActivity.person.getGender().equals("女")){
//                person_iv_avatar.setImageResource(R.drawable.woman);
//            }else {
//                person_iv_avatar.setImageResource(R.drawable.man);
//            }
//        }else{
//           person_iv_avatar.setImageBitmap(ImageHandle.stringToBitmap(MainActivity.person.getAvatar()));
//        }
        //设置头像
        String avatar = FileHandle.ReadFromFile("avatar.txt");
//        if(avatar.equals("")){
//            if(MainActivity.person.getGender().equals("女")){
//                person_iv_avatar.setImageResource(R.drawable.woman);
//            }else {
//                person_iv_avatar.setImageResource(R.drawable.man);
//            }
//        }else{
//           person_iv_avatar.setImageBitmap(ImageHandle.stringToBitmap(avatar));
//        }

//        Bitmap bitmap = BitmapFactory.decodeFile(MainActivity.person.getAvatar());
//        person_iv_avatar.setImageBitmap(bitmap);

    }

    private void initFrag(View view) {
        person_tv_name = view.findViewById(R.id.person_tv_name);
        person_tv_description =view.findViewById(R.id.person_tv_description);
        person_tv_description.setText(MainActivity.person.getName());
        person_bt_person = view.findViewById(R.id.person_bt_person);
        person_bt_drift = view.findViewById(R.id.person_bt_drift);
        person_bt_topic = view.findViewById(R.id.person_bt_topic);
        person_bt_collect = view.findViewById(R.id.person_bt_collect);
        person_iv_avatar = view.findViewById(R.id.person_iv_avatar);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.person_bt_person:
                intent = new Intent(PersonFragment.this.getContext(), PersonInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.person_bt_drift:
                break;
            case R.id.person_bt_topic:
                break;
            case R.id.person_bt_collect:
                break;
        }
    }
}