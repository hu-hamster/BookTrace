package com.jnu.booktrace.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.booktrace.R;
import com.jnu.booktrace.activity.FreeTalkPostActivity;
import com.jnu.booktrace.activity.FreeTalkTopicActivity;
import com.jnu.booktrace.bean.Book;
import com.jnu.booktrace.bean.Post;

import org.w3c.dom.Text;

import java.util.List;

//自由谈，话题内主题帖展示页
public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Post> adpList;
    private Context context;

    public PostAdapter(Context context, List<Post> postList){
        this.context = context;
        this.adpList = postList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_post,parent,false);
        return new PostHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PostHolder postHolder = (PostHolder) holder;
        Post post = adpList.get(position);
        postHolder.ivAvatar.setBackgroundResource(post.getUserAvatar());
        postHolder.tvUsername.setText(post.getUsername());
        postHolder.tvContent.setText(post.getContent());
        postHolder.tvDate.setText(post.getDate());
        postHolder.tvRelativeTopicTitle.setText(post.getRelativeTopic());
        postHolder.btnReply.setText(post.getReplyCount()+"");
        postHolder.btnLike.setText(post.getLikeCount()+"");
        postHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FreeTalkPostActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adpList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{
        View rootView;
        CardView relativeTopic;
        ImageView ivAvatar;
        TextView tvUsername, tvContent, tvDate, tvRelativeTopicTitle;
        Button btnReply, btnLike;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.item_post_root_view);
            ivAvatar = itemView.findViewById(R.id.iv_item_post_avatar);
            tvUsername = itemView.findViewById(R.id.tv_item_post_username);
            tvContent = itemView.findViewById(R.id.tv_item_post_content);
            tvDate = itemView.findViewById(R.id.tv_item_post_date);
            btnReply = itemView.findViewById(R.id.btn_item_post_reply);
            btnLike = itemView.findViewById(R.id.btn_item_post_like);
            relativeTopic = itemView.findViewById(R.id.cardView_item_post_relative_topic);
            tvRelativeTopicTitle = itemView.findViewById(R.id.tv_relativeTopic_title);
        }
    }
}
