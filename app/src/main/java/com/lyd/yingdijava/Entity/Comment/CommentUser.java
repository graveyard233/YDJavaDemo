package com.lyd.yingdijava.Entity.Comment;

public class CommentUser {
    private String portrait_url;

    private String name;

    private String toWho;//仅用于回复

    private String level;

    public String getPortrait_url() {
        return portrait_url;
    }

    public void setPortrait_url(String portrait_url) {
        this.portrait_url = portrait_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToWho() {
        if (toWho != null && toWho.length() > 0)
            return toWho;
        else
            return null;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "CommentUser{" +
                "portrait_url='" + portrait_url + '\'' +
                ", name='" + name + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}
