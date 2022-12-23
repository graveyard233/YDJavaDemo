package com.lyd.yingdijava.Entity.Comment;

import android.util.Log;

import java.util.List;

public class CommentList<T> {
    private List<T> hot_comments;

    private int hot_num;

    private List<T> normal_comments;

    public CommentList(List<T> hot_comments, int hot_num, List<T> normal_comments) {
        this.hot_comments = hot_comments;
        this.hot_num = hot_num;
        this.normal_comments = normal_comments;
    }

    public List<T> getHot_comments() {
        if (hot_comments != null && hot_comments.size() > 0)
            return hot_comments;
        else
            return null;
    }

    public void setHot_comments(List<T> hot_comments) {
        this.hot_comments = hot_comments;
    }

    public int getHot_num() {
        return hot_num;
    }

    public void setHot_num(int hot_num) {
        this.hot_num = hot_num;
    }

    public List<T> getNormal_comments() {
        if (normal_comments != null && normal_comments.size() > 0)
            return normal_comments;
        else
            return null;
    }

    public void setNormal_comments(List<T> normal_comments) {
        this.normal_comments = normal_comments;
    }

    public void printHotComments(List<CommentsNode<CommentItem>> hot){
        for (CommentsNode<CommentItem> item :
                hot) {
            Log.i("HOT", item.getMain_comment().getComment().toString());
            if (item.getReply_comments() != null){
                for (CommentItem reply :
                        item.getReply_comments()) {
                    Log.d("HOT", "    " + reply.getComment().toString());
                }
            }
        }
    }

    public void printNormalComments(List<CommentsNode<CommentItem>> normal){
        for (CommentsNode<CommentItem> item:
                normal) {
            Log.i("NORMAL", item.getMain_comment().getComment().toString());
            if (item.getReply_comments()!=null){
                for (CommentItem reply :
                        item.getReply_comments()) {
                    Log.d("NORMAL", "    " + reply.getComment().toString());
                }
            }
        }
    }
}
