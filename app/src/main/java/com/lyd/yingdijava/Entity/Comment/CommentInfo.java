package com.lyd.yingdijava.Entity.Comment;

public class CommentInfo {
    private String time;

    private String IP;

    private String like;

    private String hate;

    private String replyNum;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getHate() {
        return hate;
    }

    public void setHate(String hate) {
        this.hate = hate;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    @Override
    public String toString() {
        return "CommentInfo{" +
                "time='" + time + '\'' +
                ", IP='" + IP + '\'' +
                ", like='" + like + '\'' +
                ", replyNum='" + replyNum + '\'' +
                '}';
    }
}
