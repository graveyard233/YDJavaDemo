package com.lyd.yingdijava.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.R;

import java.util.List;

public class NewsRecyclerViewAdapter extends BaseQuickAdapter<NewsNode, NewsRecyclerViewAdapter.NewsViewHolder> {

    public NewsRecyclerViewAdapter(@NonNull List<? extends NewsNode> items) {
        super(items);
    }

    public NewsRecyclerViewAdapter() {
    }

    @Override
    protected void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i, @Nullable NewsNode newsNode) {
        newsViewHolder.title.setText(newsNode.getTitle());
        newsViewHolder.reply.setText("回复: " + newsNode.getNewsNodeFoot().getReplyNum());
        Glide.with(getContext())
                .load(newsNode.getImgUrl())
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_load_error)
                .into(newsViewHolder.img);

    }

    @NonNull
    @Override
    protected NewsViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news_list,viewGroup,false));
    }

    protected  class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView reply;
        ImageView img;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_news_title);
            reply = itemView.findViewById(R.id.item_news_reply);
            img = itemView.findViewById(R.id.item_news_img);
        }
    }
}
