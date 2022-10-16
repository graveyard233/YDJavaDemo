package com.lyd.yingdijava.Entity.Banner;

public class BannerNode {
    private String ad_id;
    private String img;
    private String url;
    private String title;

    @Override
    public String toString() {
        return "BannerNode{" +
                "ad_id='" + ad_id + '\'' +
                ", img='" + img + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BannerNode() {  }
    public BannerNode(String ad_id, String img, String url, String title) {
        this.ad_id = ad_id;
        this.img = img;
        this.url = url;
        this.title = title;
    }
}
