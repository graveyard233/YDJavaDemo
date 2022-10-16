package com.lyd.yingdijava.UI.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.card.MaterialCardView;
import com.lyd.yingdijava.Entity.Banner.BannerNode;
import com.lyd.yingdijava.R;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class NewsBannerAdapter extends BannerAdapter<BannerNode,NewsBannerAdapter.BannerViewHolder> {
    private Context context;
    public NewsBannerAdapter(List<BannerNode> datas) {
        super(datas);
    }

    @Override
    public NewsBannerAdapter.BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BannerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_banner,parent,false));
    }

    @Override
    public void onBindView(NewsBannerAdapter.BannerViewHolder holder, BannerNode data, int position, int size) {
        holder.title.setText(data.getTitle());
        Glide.with(context)
                .load(data.getImg())
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_load_error)
                .into(holder.img);

    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        MaterialCardView cardView;
        ImageView img;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_banner_cardView);
            title = itemView.findViewById(R.id.item_banner_title);
            img = itemView.findViewById(R.id.item_banner_img);
        }
    }
}
