package com.lyd.yingdijava.Entity.Community;

import java.util.List;

public class CommunityPostFoot {
    private List<String> tagList;

    private String likeNum;

    private String replyNum;

    private String time;

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "CommunityPostFoot{" +
                "tagList=" + tagList +
                ", likeNum='" + likeNum + '\'' +
                ", replyNum='" + replyNum + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
