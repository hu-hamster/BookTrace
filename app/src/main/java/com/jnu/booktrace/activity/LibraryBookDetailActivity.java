package com.jnu.booktrace.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jnu.booktrace.R;
import com.jnu.booktrace.bean.Book;
import com.jnu.booktrace.bean.TagInfo;
import com.jnu.booktrace.utils.AndroidBarUtils;
import com.jnu.booktrace.utils.ImageUtil;

import org.w3c.dom.Text;

import java.util.HashMap;

import cn.carbs.android.expandabletextview.library.ExpandableTextView;

public class LibraryBookDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView ivBookImage,ivUserAvatar;
    private TextView tvBookTitle,tvBookAuthor;
    private ExpandableTextView tvBookSummary;
    private TextView tvReviewCount,tvUserName,tvReviewDate,tvReviewContent;
    private TextView tvPublisher,tvIsbn,tvPubdate,tvPrice,tvPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_book_detail);

        //状态栏颜色更改
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        AndroidBarUtils.setBarDarkMode(this,true); //状态栏文字图标颜色为黑色

        initToolbar();
        initView();
        initBookDetail();
    }

    /*初始化各控件*/
    private void initView(){
        ivBookImage = findViewById(R.id.iv_activity_book_detail_image);
        ivUserAvatar = findViewById(R.id.iv_part_book_detail_review_avatar);
        tvBookTitle = findViewById(R.id.iv_activity_book_detail_title);
        tvBookAuthor = findViewById(R.id.iv_activity_book_detail_author);
        tvBookSummary = findViewById(R.id.part_activity_book_detail_summary);
        tvReviewCount = findViewById(R.id.tv_part_book_detail_review_count);
        tvUserName = findViewById(R.id.tv_part_book_detail_review_username);
        tvReviewDate = findViewById(R.id.tv_part_book_detail_review_date);
        tvReviewContent = findViewById(R.id.tv_part_book_detail_review_content);
        tvPublisher = findViewById(R.id.tv_otherInfo_book_publisher);
        tvIsbn = findViewById(R.id.tv_otherInfo_book_isbn);
        tvPubdate = findViewById(R.id.tv_otherInfo_book_pubdate);
        tvPrice = findViewById(R.id.tv_otherInfo_book_price);
        tvPage = findViewById(R.id.tv_otherInfo_book_page);
    }

    /**
     * 获取从OverviewActivity传递过来的数据，并显示
     */
    private void initBookDetail(){

        /*接收book对象*/
        Intent intent = getIntent();
        Book book = (Book)intent.getParcelableExtra("book");

        if(null == book) {
            Toast.makeText(this, "获取书籍信息错误！请返回重试", Toast.LENGTH_SHORT).show();
        }
        else{
            loadBookImage(book); //获取书籍封面，代码较长，单独处理
            tvBookTitle.setText(book.getTitle());
            tvBookSummary.setText(book.getSummary());
            if(book.getTranslator()!=null && !book.getTranslator().equals("null")) {
                tvBookAuthor.setText("[著] "+ book.getAuthor()+"\n[译] " + book.getTranslator());
            }
            else tvBookAuthor.setText("[著] "+book.getAuthor());
            tvPublisher.setText(book.getPublisher());
            tvIsbn.setText(book.getIsbn10());
            tvPubdate.setText(book.getPubdate());
            tvPrice.setText(book.getPrice());
            tvPage.setText(book.getPages());
            //TODO:书评的处理
        }
    }

    private void loadBookImage(Book book){
        HashMap<String, Drawable> imgCache = new HashMap<>();     // 图片缓存
        HashMap<Integer, TagInfo> tag_map = new HashMap<>();      // TagInfo缓存
        ImageUtil loader = new ImageUtil();                // 异步加载图片类

        String imgurl = book.getImage();   // 得到该项所代表的url地址
        Drawable drawable = imgCache.get(imgurl);       // 先去缓存中找

        TagInfo tag = new TagInfo();
        tag.url = book.getImage();

        if (null != drawable) {                         // 找到了直接设置为图像
            ivBookImage.setImageDrawable(drawable);
        }
        else {                                      // 没找到则开启异步线程
            drawable = loader.loadDrawableByTag(tag, new ImageUtil.ImageCallBack() {
                @Override
                public void obtainImage(TagInfo ret_info) {
                    imgCache.put(ret_info.url, ret_info.drawable);    // 首先把获取的图片放入到缓存中
                    // 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
                    ivBookImage.setImageDrawable(ret_info.drawable);
                }
            });
        }
    }

    private void initToolbar(){
        //隐藏默认actionbar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        //获取toolbar
        toolbar = findViewById(R.id.toolbar_book_detail);
//        //主标题，必须在setSupportActionBar之前设置，否则无效，如果放在其他位置，则直接setTitle即可
        toolbar.setTitle("");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_detail_toolbar, menu);
        return true;
    }

    //设置菜单监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_book_detail_collect:
                Toast.makeText(this, "collect selected", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }
}