package com.jnu.booktrace.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jnu.booktrace.R;
import com.jnu.booktrace.adapter.PostAdapter;
import com.jnu.booktrace.adapter.ReplyAdapter;
import com.jnu.booktrace.bean.Post;
import com.jnu.booktrace.bean.Post;
import com.jnu.booktrace.bean.Reply;
import com.jnu.booktrace.utils.AndroidBarUtils;

import java.util.ArrayList;
import java.util.List;

public class FreeTalkPostActivity extends AppCompatActivity {
    private List<Reply> replyList;
    private int mCurrentPosition = 0;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private ReplyAdapter adapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_talk_post);

        //状态栏颜色更改
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        AndroidBarUtils.setBarDarkMode(this,true); //状态栏文字图标颜色为黑色

        initToolbar();
        initData();
        initPoster();

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //设置RecyclerView内容
        adapter = new ReplyAdapter(this, replyList);
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar(){
        //隐藏默认actionbar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        //获取toolbar
        toolbar = findViewById(R.id.toolbar);
        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        toolbar.setTitle("详情");
        //用toolbar替换actionbar
        setSupportActionBar(toolbar);
        //左侧按钮：可见+更换图标+点击监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示toolbar的返回按钮
        //toolBar.setNavigationIcon(R.mipmap.back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData(){
        replyList = new ArrayList<>();
        replyList.add(new Reply(R.drawable.pic9,"缄言","这是书店日记吗","2021-12-19"));
        replyList.add(new Reply(R.drawable.pic10,"碎","哈哈哈哈书店四季？","2021-12-18"));
        replyList.add(new Reply(R.drawable.pic11,"Shirley\uD83C\uDF6D","貌似手裡有書，心不慌！","2021-12-18"));
        replyList.add(new Reply(R.drawable.pic12,"狗腿子","为猫仔来一通乱赞\uD83D\uDC4F","2021-12-19"));
        replyList.add(new Reply(R.drawable.pic13,"冬妮娅","旁边的面包看着好好吃","2021-12-16"));
        replyList.add(new Reply(R.drawable.pic14,"记忆中的夏天","我手中的书估计你不会想要","2021-12-13"));
        replyList.add(new Reply(R.drawable.pic15,"悬铃木下","书店四季","2021-12-22"));
        replyList.add(new Reply(R.drawable.pic16,"米小姐","哇，肉桂卷","2021-12-22"));
        replyList.add(new Reply(R.drawable.pic17,"walkingcell","虽然不是同一本 今天刚刚看了《书店日记》就刷到了！","2021-12-13"));
        replyList.add(new Reply(R.drawable.pic18, "亿万千张丝","看起来很像周建生","2021-12-26"));
    }
    private void initPoster(){ //初始化并设置楼主的数据
        //要从外面传进来一个Post，但先不管了
        Post post = new Post(R.drawable.default_avatar, "缄言",
                "这是书店日记吗","哪一本书中的人物让你看到了自己？");
        ImageView avatar = findViewById(R.id.iv_activity_post_avatar);
        TextView username = findViewById(R.id.tv_activity_post_username);
        TextView content = findViewById(R.id.tv_activity_post_content);
        TextView date = findViewById(R.id.tv_activity_post_date);
        TextView replyCount = findViewById(R.id.tv_activity_post_reply_count);

        avatar.setImageDrawable(this.getDrawable(R.drawable.default_avatar));
        username.setText(post.getUsername());
        content.setText(post.getContent());
        date.setText(post.getDate());
        replyCount.setText("共"+replyList.size()+"条回复");
    }
}