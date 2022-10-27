package com.lyd.yingdijava.UI.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.google.android.material.card.MaterialCardView;
import com.lyd.yingdijava.Entity.Community.BaseCommunityNode;
import com.lyd.yingdijava.Entity.Community.CommunityPostNode;
import com.lyd.yingdijava.R;

import java.util.List;

// TODO: 2022/10/23 准备测试多布局 
public class CommunityMultiItemAdapter extends BaseMultiItemAdapter<CommunityPostNode> {

    private static final int ROUTINE = 0;
    private static final int ARTICLE = 1;

    public CommunityMultiItemAdapter(@NonNull List<? extends CommunityPostNode> items) {
        super(items);
        OnMultiItemAdapterListener<CommunityPostNode,RoutineVH> listenerRoutine = new OnMultiItemAdapterListener<CommunityPostNode, RoutineVH>() {
            @NonNull
            @Override
            public RoutineVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new RoutineVH(LayoutInflater.from(context).inflate(R.layout.item_post_routine,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull RoutineVH routineVH, int i, @Nullable CommunityPostNode communityPostNode) {
                routineVH.title.setText(communityPostNode.getTitle());
                routineVH.textPreview.setText(communityPostNode.getText_preView());
                if (communityPostNode.getPostImgList() != null){
                    try {
                        Glide.with(getContext())
                                .load(communityPostNode.getPostImgList().get(0))
                                .placeholder(R.drawable.img_loading)
                                .error(R.drawable.img_load_error)
                                .into(routineVH.img);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };
        OnMultiItemAdapterListener<CommunityPostNode, ArticleVH> listenerArticle = new OnMultiItemAdapterListener<CommunityPostNode, ArticleVH>() {
            @NonNull
            @Override
            public ArticleVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new ArticleVH(LayoutInflater.from(context).inflate(R.layout.item_post_article,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull ArticleVH articleVH, int i, @Nullable CommunityPostNode communityPostNode) {
                articleVH.title.setText(communityPostNode.getTitle());
//                articleVH.img.setBackgroundColor(Color.GREEN);
                Glide.with(getContext())
                        .load(communityPostNode.getTitleImgUrl())
                        .placeholder(R.drawable.img_loading)
                        .error(R.drawable.img_load_error)
                        .into(articleVH.img);
            }
        };
        addItemType(ROUTINE,RoutineVH.class,listenerRoutine);
        addItemType(ARTICLE,ArticleVH.class,listenerArticle);
        onItemViewType(new OnItemViewTypeListener<CommunityPostNode>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends CommunityPostNode> list) {
                switch (list.get(i).getPostType()){
                    case ArticlePost:
                        return ARTICLE;
                    default:
                        return ROUTINE;
                }
            }
        });
    }

    protected class RoutineVH extends RecyclerView.ViewHolder{
        TextView title;
        TextView textPreview;
        ImageView img;
        public RoutineVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_routine_title);
            textPreview = itemView.findViewById(R.id.item_post_routine_text);
            img = itemView.findViewById(R.id.item_post_routine_img1);
        }
    }

    protected class ArticleVH extends RecyclerView.ViewHolder{
        TextView title;
        ImageView img;
        public ArticleVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_article_title);
            img = itemView.findViewById(R.id.item_post_article_img);
        }
    }
}
