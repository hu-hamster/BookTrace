package com.jnu.booktrace.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.jnu.booktrace.R;
import com.jnu.booktrace.adapter.PostAdapter;
import com.jnu.booktrace.bean.Post;
import com.jnu.booktrace.bean.Topic;
import com.jnu.booktrace.utils.AndroidBarUtils;

import java.util.ArrayList;

//单一话题页，展示该话题下的相关主题帖
public class FreeTalkTopicActivity extends AppCompatActivity {
    private ArrayList<Post> posts;
    private int[] colors;
    //因为setExpanded会调用事件监听，所以通过标志过滤掉
    public static int expendedtag=2;
    private TabLayout tabLayout;
    private Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_talk_topic);

        initColors();
        initToolbar();
        initData();
        initTabLayout();

        //状态栏颜色更改
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //状态栏文字图标颜色为黑色
        AndroidBarUtils.setBarDarkMode(this,true);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //设置RecyclerView内容
        PostAdapter adapter = new PostAdapter(this, posts);
        recyclerView.setAdapter(adapter);
    }

    private void initColors(){
        this.colors = new int[]{getColor(R.color.topic_blue),
                getColor(R.color.topic_brown),
                getColor(R.color.topic_green),
                getColor(R.color.topic_grey)
        };
    }

    private void initTabLayout(){
        String[] titles = new String[]{"最新"};
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab()); //只有一个标签
        for(int i=0;i<titles.length;i++){
            tabLayout.getTabAt(i).setText(titles[i]);
        }
    }

    private void initToolbar(){
        Intent intent = getIntent();
        topic = (Topic)intent.getParcelableExtra("topic");
        int position = intent.getIntExtra("position",0);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar_layout);
        //设置折叠布局内部的toolbar在折叠时可充当ActionBar标题栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        if(null != actionBar){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getWindow().setStatusBarColor(colors[position%colors.length]);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            //verticalOffset是当前appbarLayout的高度与最开始appbarlayout高度的差，向上滑动的话是负数
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                Log.e("count"," "+verticalOffset);
//                Log.e("count","getsupportActionbar "+getSupportActionBar().getHeight()
//                +" collapsing "+collapsingToolbarLayout.getHeight());
                if(getSupportActionBar().getHeight() - collapsingToolbarLayout.getHeight() == verticalOffset){
                    //折叠监听
                    //Toast.makeText(FreeTalkTopicActivity.this,"折叠了",Toast.LENGTH_SHORT).show();
                    toolbar.setTitle(topic.getTitle());
                }
                if(expendedtag==2&&verticalOffset==0){
                    //展开监听
                    //Toast.makeText(FreeTalkTopicActivity.this, "展开了",Toast.LENGTH_SHORT).show();
                    toolbar.setTitle("");
                }
//                if(expendedtag!=2&&verticalOffset==0){
//                    expendedtag++;
//                }
            }
        });
        TextView tvTopicTitle = findViewById(R.id.tv_topic_title);
        tvTopicTitle.setText(topic.getTitle());
        //设置折叠标题栏的内容
        TextView tvTopicContent = findViewById(R.id.tv_topic_content);
        tvTopicContent.setText(topic.getContent());
        //设置折叠标题栏的背景颜色
        collapsingToolbarLayout.setBackgroundColor(colors[position%colors.length]);
        //设置顶部标题栏的背景色
        toolbar.setBackgroundColor(colors[position%colors.length]);
    }

    private void initData(){
        posts = new ArrayList<>();
        posts.add(new Post(R.drawable.pic1,"少女漫步世界","社恐是借口\n" +
                "就是想要手里时时刻刻捧着书",topic.getTitle()));
        posts.add(new Post(R.drawable.pic2,"サボテン","《罪与罚》拉斯柯尔尼科夫。目前为止唯一和我没有交流障碍的人。",topic.getTitle()));
        posts.add(new Post(R.drawable.pic3,"花城出版社","你那么憎恨那些人，跟他们斗了那么久，最终却变得和他们一样。人世间没有任何理想值得以这样的沉沦为代价。",topic.getTitle()));
        posts.add(new Post(R.drawable.pic4,"Asuraa","一定是《飘》里的郝思嘉\uD83D\uDE0A当然不是那般美貌，而是即便世界坍塌，也有勇气和决心，白地重建的生命力。",topic.getTitle()));
        posts.add(new Post(R.drawable.pic5,"要下雨了","我惧怕自己不是美玉，因而刻意不去刻苦打磨；我又对自己会成为美玉尚存半分希望，因而也无法庸庸碌碌地与瓦砾为伍，我渐渐远离俗世，疏远世人，结果，愤懑、羞惭和愤恨渐渐滋长了我内心怯懦的自尊。",topic.getTitle()));
        posts.add(new Post(R.drawable.pic6,"！","我惧怕自己不是美玉，因而刻意不去刻苦打磨；我又对自己会成为美玉尚存半分希望，因而也无法庸庸碌碌地与瓦砾为伍，我渐渐远离俗世，疏远世人，结果，愤懑、羞惭和愤恨渐渐滋长了我内心怯懦的自尊。",topic.getTitle()));
        posts.add(new Post(R.drawable.pic7,"锋少","我惧怕自己不是美玉，因而刻意不去刻苦打磨；我又对自己会成为美玉尚存半分希望，因而也无法庸庸碌碌地与瓦砾为伍，我渐渐远离俗世，疏远世人，结果，愤懑、羞惭和愤恨渐渐滋长了我内心怯懦的自尊。",topic.getTitle()));
        posts.add(new Post(R.drawable.pic8,"V","我真的很想说是堂吉诃德，但是那么喜欢他的我，更像桑丘吧。",topic.getTitle()));
    }
}