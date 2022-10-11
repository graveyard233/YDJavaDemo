package com.lyd.yingdijava.Entity.News;

public class NewsNodeFoot {
    private String replyNum;

    private String likeNum;

    public NewsNodeFoot(String replyNum, String likeNum) {
        this.replyNum = replyNum;
        this.likeNum = likeNum;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    @Override
    public String toString() {
        return "NewsNodeFoot{" +
                "replyNum='" + replyNum + '\'' +
                ", likeNum='" + likeNum + '\'' +
                '}';
    }
}
