package com.jnu.booktrace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.booktrace.R;
import com.jnu.booktrace.bean.Post;
import com.jnu.booktrace.bean.Reply;

import java.util.List;

public class ReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final List<Reply> adpList;
    private Context context;

    public ReplyAdapter(Context context, List<Reply> replyList){
        this.context = context;
        this.adpList = replyList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_reply,parent,false);
        return new ReplyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ReplyHolder replyHolder = (ReplyHolder) holder;
        Reply reply = adpList.get(position);
        replyHolder.ivAvatar.setBackgroundResource(reply.getAvatar());
        replyHolder.tvUsername.setText(reply.getUsername());
        replyHolder.tvContent.setText(reply.getContent());
        replyHolder.tvDate.setText(reply.getDate());
        replyHolder.btnLike.setText(reply.getLikeCount()+"");
    }

    @Override
    public int getItemCount() {
        return adpList.size();
    }

    class ReplyHolder extends RecyclerView.ViewHolder{
        View rootView;
        ImageView ivAvatar;
        TextView tvUsername, tvContent, tvDate;
        Button btnLike;

        public ReplyHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.item_reply_root_view);
            ivAvatar = itemView.findViewById(R.id.iv_item_reply_avatar);
            tvUsername = itemView.findViewById(R.id.tv_item_reply_username);
            tvContent = itemView.findViewById(R.id.tv_item_reply_content);
            tvDate = itemView.findViewById(R.id.tv_item_reply_date);
            btnLike = itemView.findViewById(R.id.btn_item_reply_like);
        }
    }
}
