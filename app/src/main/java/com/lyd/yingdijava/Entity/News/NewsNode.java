package com.lyd.yingdijava.Entity.News;

public class NewsNode {
    private String targetUrl;

    private String title;

    private String imgUrl;

    private NewsNodeFoot newsNodeFoot;

    public NewsNode(String targetUrl, String title, String imgUrl, NewsNodeFoot newsNodeFoot) {
        this.targetUrl = targetUrl;
        this.title = title;
        this.imgUrl = imgUrl;
        this.newsNodeFoot = newsNodeFoot;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public NewsNodeFoot getNewsNodeFoot() {
        return newsNodeFoot;
    }

    public void setNewsNodeFoot(NewsNodeFoot newsNodeFoot) {
        this.newsNodeFoot = newsNodeFoot;
    }

    @Override
    public String toString() {
        return "NewsNode{" +
                "targetUrl='" + targetUrl + '\'' +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", newsNodeFoot=" + newsNodeFoot +
                '}';
    }
}
