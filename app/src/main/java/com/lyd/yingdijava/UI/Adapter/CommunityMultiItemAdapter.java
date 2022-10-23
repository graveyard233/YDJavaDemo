package com.lyd.yingdijava.UI.Adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.lyd.yingdijava.Entity.Community.CommunityPostNode;

// TODO: 2022/10/23 准备测试多布局 
public class CommunityMultiItemAdapter extends BaseMultiItemAdapter<CommunityPostNode> {

    protected static class RoutineVH extends RecyclerView.ViewHolder{

        public RoutineVH(@NonNull View itemView) {
            super(itemView);
        }
    }

    protected static class ArticlePost extends RecyclerView.ViewHolder{
        
        public ArticlePost(@NonNull View itemView) {
            super(itemView);
        }
    }
}
