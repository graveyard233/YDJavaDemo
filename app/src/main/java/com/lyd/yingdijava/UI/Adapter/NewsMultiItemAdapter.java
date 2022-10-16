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

import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.google.android.material.card.MaterialCardView;
import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.R;

import java.util.List;
/*
* 以后需要多布局的时候才用得到，先放着当作demo，以后可以参照这个来写
*
* */
public class NewsMultiItemAdapter extends BaseMultiItemAdapter<NewsNode> {

    private static final int BANNER_TYPE = 0;
    private static final int NEWS_TYPE = 1;

    public NewsMultiItemAdapter(@NonNull List<? extends NewsNode> items) {
        super(items);
        OnMultiItemAdapterListener<NewsNode,NewsItemVH> listenerNews = new OnMultiItemAdapterListener<NewsNode, NewsItemVH>() {
            @NonNull
            @Override
            public NewsItemVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new NewsItemVH(LayoutInflater.from(context).inflate(R.layout.item_news_list,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull NewsItemVH newsItemVH, int i, @Nullable NewsNode newsNode) {

            }
        };
        OnMultiItemAdapterListener<NewsNode,BannerItemVH> listenerBanner = new OnMultiItemAdapterListener<NewsNode, BannerItemVH>() {
            @NonNull
            @Override
            public BannerItemVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new BannerItemVH(LayoutInflater.from(context).inflate(R.layout.item_news_banner,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull BannerItemVH bannerItemVH, int i, @Nullable NewsNode newsNode) {

            }
        };
        addItemType(NEWS_TYPE,NewsItemVH.class, listenerNews);
        addItemType(BANNER_TYPE,BannerItemVH.class,listenerBanner);
        onItemViewType(new OnItemViewTypeListener<NewsNode>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends NewsNode> list) {
                if (list.get(i).getNewsNodeFoot() == null)
                    return BANNER_TYPE;
                if (list.get(i).getNewsNodeFoot() != null)
                    return NEWS_TYPE;
                else return 1;
            }
        });
    }

    protected static class NewsItemVH extends RecyclerView.ViewHolder{
        TextView title;
        TextView reply;
        ImageView img;
        public NewsItemVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_news_title);
            reply = itemView.findViewById(R.id.item_news_reply);
            img = itemView.findViewById(R.id.item_news_img);
        }
    }

    protected static class BannerItemVH extends RecyclerView.ViewHolder{
        TextView title;
        MaterialCardView cardView;
        ImageView img;
        public BannerItemVH(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_banner_cardView);
            title = itemView.findViewById(R.id.item_banner_title);
            img = itemView.findViewById(R.id.item_banner_img);
        }
    }
}
