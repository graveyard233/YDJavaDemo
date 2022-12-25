package com.lyd.yingdijava.Entity.Comment;

import java.util.List;

public class CommentsNode<T> {
    private T main_comment;

    private List<T> reply_comments;

    public T getMain_comment() {
        return main_comment;
    }

    public void setMain_comment(T main_comment) {
        this.main_comment = main_comment;
    }

    public boolean hasReply(){
        return getReply_comments() != null;
    }

    public List<T> getReply_comments() {
        if (reply_comments != null && reply_comments.size() > 0)
            return reply_comments;
        else
            return null;
    }

    public void setReply_comments(List<T> reply_comments) {
        this.reply_comments = reply_comments;
    }
}
