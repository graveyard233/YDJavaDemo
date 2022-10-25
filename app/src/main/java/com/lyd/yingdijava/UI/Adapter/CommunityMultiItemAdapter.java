package com.lyd.yingdijava.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.lyd.yingdijava.Entity.Community.BaseCommunityNode;
import com.lyd.yingdijava.Entity.Community.CommunityPostNode;
import com.lyd.yingdijava.R;

import java.util.List;

// TODO: 2022/10/23 准备测试多布局 
public class CommunityMultiItemAdapter extends BaseMultiItemAdapter<CommunityPostNode> {

    public CommunityMultiItemAdapter(@NonNull List<? extends CommunityPostNode> items) {
        super(items);
        OnMultiItemAdapterListener<CommunityPostNode,RoutineVH> listenerRoutine = new OnMultiItemAdapterListener<CommunityPostNode, RoutineVH>() {
            @NonNull
            @Override
            public RoutineVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new RoutineVH(LayoutInflater.from(context).inflate(R.layout.item_news_list,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull RoutineVH routineVH, int i, @Nullable CommunityPostNode communityPostNode) {
                routineVH.title.setText(communityPostNode.getTitle());
                routineVH.reply.setText(communityPostNode.getPostType().toString());
                routineVH.img.setBackgroundColor(Color.BLUE);
            }
        };
        OnMultiItemAdapterListener<CommunityPostNode, ArticleVH> listenerArticle = new OnMultiItemAdapterListener<CommunityPostNode, ArticleVH>() {
            @NonNull
            @Override
            public ArticleVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new ArticleVH(LayoutInflater.from(context).inflate(R.layout.item_news_list,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull ArticleVH articleVH, int i, @Nullable CommunityPostNode communityPostNode) {
                articleVH.title.setText(communityPostNode.getTitle());
                articleVH.reply.setText(communityPostNode.getPostType().toString());
                articleVH.img.setBackgroundColor(Color.GREEN);
            }
        };
        addItemType(0,RoutineVH.class,listenerRoutine);
        addItemType(1,ArticleVH.class,listenerArticle);
        onItemViewType(new OnItemViewTypeListener<CommunityPostNode>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends CommunityPostNode> list) {
                switch (list.get(i).getPostType()){
                    case ArticlePost:
                        return 1;
                    default:
                        return 0;
                }
//                return 0;
            }
        });
    }

    protected class RoutineVH extends RecyclerView.ViewHolder{
        TextView title;
        TextView reply;
        ImageView img;
        public RoutineVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_news_title);
            reply = itemView.findViewById(R.id.item_news_reply);
            img = itemView.findViewById(R.id.item_news_img);
        }
    }

    protected class ArticleVH extends RecyclerView.ViewHolder{
        TextView title;
        TextView reply;
        ImageView img;
        public ArticleVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_news_title);
            reply = itemView.findViewById(R.id.item_news_reply);
            img = itemView.findViewById(R.id.item_news_img);
        }
    }
}
