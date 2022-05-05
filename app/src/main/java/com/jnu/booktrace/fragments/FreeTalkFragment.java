package com.jnu.booktrace.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jnu.booktrace.R;
import com.jnu.booktrace.activity.FreeTalkTopicActivity;
import com.jnu.booktrace.adapter.PostAdapter;
import com.jnu.booktrace.adapter.TopicAdapter;
import com.jnu.booktrace.bean.Topic;

import java.util.ArrayList;

public class FreeTalkFragment extends Fragment {
    private ArrayList<Topic> topics;
    private Toolbar toolbar;
    public FreeTalkFragment() {
    }

    public static FreeTalkFragment newInstance() {
        FreeTalkFragment fragment = new FreeTalkFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_free_talk, container, false);
        initData();
        initRecyclerView(rootView);
        initToolbar(rootView);

        return rootView;
    }

    private void initToolbar(View v){
        //获取toolbar
        toolbar = v.findViewById(R.id.toolbar);
//        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        toolbar.setTitle("话题广场");
        //用toolbar替换actionbar
        //setSupportActionBar(toolbar);
    }

    private void initData(){
        topics = new ArrayList<>();
        topics.add(new Topic("哪一本书中的人物让你看到了自己？","是夜色温柔的尼科尔，是红楼梦的鸳鸯，还是你好忧愁里的塞茜尔，从人物中你看到了自己，在自己中你终究会成为她吗？"));
        topics.add(new Topic("文学名著里伟大的开场白","有哪些作品的经典开场白？哪些开场白一下攥住了你的眼睛和心?"));
        topics.add(new Topic("阅读过的有趣题记","大多书籍都有一篇题记，或点题，或注释，或交代内容，或提出悬念。你读到过哪些有趣的题记？"));
        topics.add(new Topic("文学作品中的浪漫行为","有时候很想拥有一些浪漫的时刻，但是囿于自己的创意和想象不足，想要浪漫但是不知道如何浪漫。你读到过哪些书中的浪漫行为？"));
        topics.add(new Topic("那些值得一读的诺贝尔文学奖作品","作家当以作品闻名于世，古往今来的诺奖获得者们都有属于自己的代表作，你读过的诺奖作品里那些是值得一读的呢？"));
    }

    private void initRecyclerView(View rootView){
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        //设置RecyclerView内容
        TopicAdapter adapter = new TopicAdapter(getContext(), topics);
        recyclerView.setAdapter(adapter);
    }
}