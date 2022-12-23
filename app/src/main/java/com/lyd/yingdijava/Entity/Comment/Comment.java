package com.lyd.yingdijava.Entity.Comment;

import java.util.List;

public class Comment {
    private String text;

    private List<String> img_url_list;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getImg_url_list() {
        return img_url_list;
    }

    public void setImg_url_list(List<String> img_url_list) {
        this.img_url_list = img_url_list;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "text='" + text + '\'' +
                ", img_url_list=" + img_url_list +
                '}';
    }
}
