package com.jnu.booktrace.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.booktrace.R;
import com.jnu.booktrace.bean.Drift;
import com.jnu.booktrace.bean.TagInfo;
import com.jnu.booktrace.utils.ImageUtil;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;

import java.util.HashMap;
import java.util.List;

public class DriftAdapter extends RecyclerView.Adapter {

    private List<Drift> drifts;
    private Context context;
    private int[] colors;
    HashMap<String, Drawable> imgCache;     // 图片缓存
    HashMap<Integer, TagInfo> tag_map;      // TagInfo缓存
    ImageUtil loader;                // 异步加载图片类

    public DriftAdapter(Context context,List<Drift> drifts) {
        this.context =context;
        this.drifts = drifts;
        this.colors = new int[]{context.getColor(R.color.bottle_1),
                context.getColor(R.color.bottle_2),
                context.getColor(R.color.bottle_3),
                context.getColor(R.color.bottle_4),
                context.getColor(R.color.bottle_5)
        };
        imgCache = new HashMap<String, Drawable>();
        loader = new ImageUtil();
        tag_map = new HashMap<Integer, TagInfo>();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_drift,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        Drift drift = drifts.get(position);
        viewHolder.drift_author.setText(drift.getAuthor_name()+"");
        viewHolder.rootView.setCardBackgroundColor(colors[position%colors.length]);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MessageDialog.show("漂流瓶详情", null, "加入收藏", "取消", "删除")
                            .setCustomView(new OnBindView<MessageDialog>(R.layout.dialog_layout_bottle_detail_view) {
                                @Override
                                public void onBind(MessageDialog dialog, View v) {
                                    ImageView image = v.findViewById(R.id.iv_item_book_image);
                                    TextView title = v.findViewById(R.id.tv_item_book_title);
                                    TextView author = v.findViewById(R.id.tv_item_book_author);
                                    TextView recommend = v.findViewById(R.id.recommend_reason);

                                    //显示标题、作者、封面、推荐理由
                                    title.setText(drift.getTitle());
                                    author.setText(drift.getBook_Author());
                                    recommend.setText(drift.getRecommend());

                                    String imgurl = drift.getBook_image();   // 得到该项所代表的url地址
                                    Drawable drawable = imgCache.get(imgurl);       // 先去缓存中找

                                    TagInfo tag = new TagInfo();
                                    tag.url = drift.getBook_image();

                                    if (null != drawable) {                         // 找到了直接设置为图像
                                        image.setImageDrawable(drawable);
                                    }
                                    else {                                      // 没找到则开启异步线程
                                        drawable = loader.loadDrawableByTag(tag, new ImageUtil.ImageCallBack() {
                                            @Override
                                            public void obtainImage(TagInfo ret_info) {
                                                imgCache.put(ret_info.url, ret_info.drawable);    // 首先把获取的图片放入到缓存中
                                                // 通过返回的TagInfo去Tag缓存中找，然后再通过找到的Tag来获取到所对应的ImageView
                                                image.setImageDrawable(ret_info.drawable);
                                            }
                                        });
                                    }
                                }
                            }).setOkButton(new OnDialogButtonClickListener<MessageDialog>() {
                        @Override
                        public boolean onClick(MessageDialog baseDialog, View v) {
                            //TODO
                            //加入收藏
                            //new LibraryFragment().mBookList.add(drift.getBook());
                            return false;
                        }
                    }).setOtherButton(new OnDialogButtonClickListener<MessageDialog>() {
                        @Override
                        public boolean onClick(MessageDialog baseDialog, View v) {
                            //删除按钮
                            //TODO
                            return false;
                        }
                    });
                }
            });
    }

    @Override
    public int getItemCount() {
        return drifts.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView drift_author;
        CardView rootView;

        public MyViewHolder(View view) {
            super(view);
            drift_author = view.findViewById(R.id.drift_author);
            rootView = view.findViewById(R.id.root_item_drift);
        }
    }
}
