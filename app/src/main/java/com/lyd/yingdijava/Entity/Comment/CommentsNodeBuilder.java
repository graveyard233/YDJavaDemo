package com.lyd.yingdijava.Entity.Comment;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class CommentsNodeBuilder {

    private static volatile CommentsNodeBuilder instances;

    private CommentsNode<CommentItem> commentsNode;

    private CommentItem mainComment;

    private CommentsNodeBuilder() {}

    public static CommentsNodeBuilder getInstance() {
        if (instances == null){
            synchronized (CommentsNodeBuilder.class){
                if (instances == null){
                    instances = new CommentsNodeBuilder();
                }
            }
        }
        return instances;
    }

    //我需要准备好node的所有子类，将其new出来然后外部set好，然后返回这个builder类，这个类专门用于构建单个评论节点

    /**
     * 初始化基点
     * */
    public CommentsNodeBuilder initBaseNode(){
        commentsNode = new CommentsNode<>();
        mainComment = new CommentItem();

        commentsNode.setMain_comment(mainComment);
        return instances;
    }

    /**
     * 只能传递 class="comment-item-component" 的element
     * */
    public CommentsNodeBuilder buildMain(Element element){
        initOneComment(mainComment);
        buildHead(element,mainComment);
        buildMid(element,mainComment);
        buildFoot(element,mainComment);

        return instances;
    }

    public CommentsNode<CommentItem> getNode(){
        return commentsNode;
    }

    /**
     * 初始化评论类
     * */
    private void initOneComment(CommentItem item) {
        CommentUser commentUser = new CommentUser();
        Comment comment = new Comment();
        CommentInfo commentInfo = new CommentInfo();

        item.setCommentUser(commentUser);
        item.setComment(comment);
        item.setCommentInfo(commentInfo);
    }


    private void buildHead(Element element,CommentItem item) {
        //写的详细点可以减少不必要的bug
        //设置用户名字
        item.getCommentUser().setName(element.child(0).select("div.relative").select("div.user-title-area").first()
                .select("div.user-info").first()
                .select("h4.name").text());
        //设置用户等级
        item.getCommentUser().setLevel(element.child(0).select("div.relative").select("div.user-title-area").first()
                .select("div.user-info").first()
                .select("div.honor").first().select("div.level").first().text());
        //设置用户头像url
        item.getCommentUser().setPortrait_url(element.child(0).select("div.relative").select("div.user-title-area").first()
                .select("div.user-head").first()
                .select("img.avatar").first().attr("src"));
    }

    private void buildMid(Element element,CommentItem item) {
        item.getComment().setText(element.child(0).select("div.item-content-area").first().child(0)
                .select("div.text-base").first().text());
        if (element.child(0).select("div.item-content-area").first()
                .select("div.item-content-imgs").size() != 0){//如果评论有图片的话
            List<String> tempList = new ArrayList<>();
            for (Element e :
                    element.child(0).select("div.item-content-area").first()
                            .select("div.item-content-imgs").first()
                            .select("div.img-item")) {
                StringBuffer sb = new StringBuffer(e.attr("style"));
                sb.delete(0,sb.indexOf("http"));
                sb.delete(sb.length() - 3,sb.length());
                tempList.add(sb.toString());
            }
            item.getComment().setImg_url_list(tempList);
        }
    }

    private void buildFoot(Element element,CommentItem item) {
        item.getCommentInfo().setTime(element.child(0).select("div.item-content-area").first().child(0)
                .select("div.foot").first()
                .select("div.time-box").first()
                .child(0).text());
        item.getCommentInfo().setIP(element.child(0).select("div.item-content-area").first().child(0)
                .select("div.foot").first()
                .select("div.time-box").first()
                .child(1).text());
        item.getCommentInfo().setLike(element.child(0).select("div.item-content-area").first().child(0)
                .select("div.foot").first()
                .child(1).child(0).select("span.cursor-pointer").first()
                .select("span.inline-block").text());
        item.getCommentInfo().setReplyNum(element.child(0).select("div.item-content-area").first().child(0)
                .select("div.foot").first()
                .child(1).child(1).select("span.inline-block").first().text());
    }

    public CommentsNodeBuilder buildReply(Element element){
        if (element.child(0).select("div.item-content-area").first()
                .select("div.children-content-area").size() != 0){//不等于0就是有回复
            List<CommentItem> tempList = new ArrayList<>();
            for (Element e :
                    element.child(0).select("div.item-content-area").first()
                            .select("div.children-content-area").first()
                            .select("div.comment-childs").first()
                            .select("div.comment-child-box")) {
                CommentItem tempItem = new CommentItem();
                initOneComment(tempItem);
                buildReply_itemHead(e,tempItem);
                buildReply_itemMid(e,tempItem);
                buildReply_itemFoot(e,tempItem);
                tempList.add(tempItem);
            }
            commentsNode.setReply_comments(tempList);
        }
        return instances;
    }


    private void buildReply_itemHead(Element element,CommentItem item){
        item.getCommentUser().setName(element.select("div.user-box").first().select("div.user-title-area").first()
                .select("div.user-info").first()
                .select("h4.name").first().text());
        item.getCommentUser().setPortrait_url(element.select("div.user-box").first().select("div.user-title-area").first()
                .select("div.user-head").first()
                .select("img.avatar").first().attr("src"));
    }

    private void buildReply_itemMid(Element element,CommentItem item){
        item.getComment().setText(element.select("div.child-content").first()
                .select("p.text-base").first().text());
        if (element.select("div.child-content").first()
                .select("div.images-box").size() != 0){
            List<String> tempList = new ArrayList<>();
            for (Element e :
                    element.select("div.child-content").first()
                            .select("div.images-box").first()
                            .select("div.img-item")) {
                StringBuffer sb = new StringBuffer(e.attr("style"));
                sb.delete(0,sb.indexOf("http"));
                sb.delete(sb.length() - 3,sb.length());
                tempList.add(sb.toString());
            }
            item.getComment().setImg_url_list(tempList);
        }
    }

    private void buildReply_itemFoot(Element element,CommentItem item){
        item.getCommentInfo().setTime(element.select("div.child-content").first()
                .select("div.foot").first()
                .select("div.time-box").first()
                .child(0).text());
        item.getCommentInfo().setIP(element.select("div.child-content").first()
                .select("div.foot").first()
                .select("div.time-box").first()
                .child(1).text());
        item.getCommentInfo().setLike(element.select("div.child-content").first()
                .select("div.foot").first()
                .child(1).child(0).select("span.cursor-pointer").first()
                .select("span.inline-block").text());
        item.getCommentInfo().setReplyNum(element.select("div.child-content").first()
                .select("div.foot").first()
                .child(1).child(1).select("span.inline-block").first().text());
    }



}
