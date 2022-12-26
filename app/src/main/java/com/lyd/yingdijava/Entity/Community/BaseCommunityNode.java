package com.lyd.yingdijava.Entity.Community;

import com.lyd.yingdijava.Entity.User.User;
/**
 * 社区帖子基类
 * 营地现在暂时有四个类
 * 1.常规帖子 带title和预览文本，还有可能带有图片，预览时最多三张，超过会显示还有多少张，所有的图片URL在script中
 * 2.卡组帖子 和常规帖子属性基本一致，还多出一个卡组信息，在web端上不会附带这个帖子的图片URL，需要去script去拿数据，同时卡组数据也在那里
 * 3.文章类帖子 写成了文章，带文章的题图，title和预览文本
 * 4.投票帖 带基础title，预览文本和一个投票比例
 * */
public abstract class BaseCommunityNode {
    public enum PostType{
        RoutinePost, DeskPost,ArticlePost,VotePost
    }
    private PostType postType;

    private String targetUrl;

    private User user;

    private String title;

    private String text_preView;

    private CommunityPostFoot foot;

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public String getUrl() {
        return targetUrl;
    }

    public void setUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText_preView() {
        return text_preView;
    }

    public void setText_preView(String text_preView) {
        this.text_preView = text_preView;
    }

    public CommunityPostFoot getFoot() {
        return foot;
    }

    public void setFoot(CommunityPostFoot foot) {
        this.foot = foot;
    }
}
