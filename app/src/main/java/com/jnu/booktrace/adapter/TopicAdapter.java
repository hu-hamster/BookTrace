package com.jnu.booktrace.adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.booktrace.R;
import com.jnu.booktrace.activity.FreeTalkTopicActivity;
import com.jnu.booktrace.bean.Topic;

import java.util.List;

//自由谈，可参与话题展示页
public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<Topic> adpList;
    private Context context;
    private int[] colors;

    public TopicAdapter(Context context, List<Topic> postList){
        this.context = context;
        this.adpList = postList;
        this.colors = new int[]{context.getColor(R.color.topic_blue),
                context.getColor(R.color.topic_brown),
                context.getColor(R.color.topic_green),
                context.getColor(R.color.topic_grey)
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_topic,parent,false);
        return new TopicHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TopicHolder topicHolder = (TopicHolder) holder;
        Topic topic = adpList.get(position);
        topicHolder.tvTitle.setText(topic.getTitle());
        topicHolder.tvContent.setText(topic.getContent());
        topicHolder.rootView.setCardBackgroundColor(colors[position%colors.length]);
        topicHolder.rootView.setRadius(15);
        topicHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FreeTalkTopicActivity.class);
                intent.putExtra("topic",(Parcelable)topic);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return adpList.size();
    }

    class TopicHolder extends RecyclerView.ViewHolder{
        CardView rootView;
        TextView tvTitle,tvContent;
        public TopicHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.root_item_topic);
            tvTitle = itemView.findViewById(R.id.tv_item_topic_title);
            tvContent = itemView.findViewById(R.id.tv_item_topic_content);
        }
    }
}
