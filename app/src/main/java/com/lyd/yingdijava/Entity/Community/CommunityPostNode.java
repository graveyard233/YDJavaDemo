package com.lyd.yingdijava.Entity.Community;

import org.jsoup.nodes.Element;

import java.util.List;

public class CommunityPostNode extends BaseCommunityNode{

    private List<String> postImgList;

    private String deskInfo;

    private String titleImgUrl;

    private String voteMsg;


    public void setPostImgList(List<String> postImgList) {
        this.postImgList = postImgList;
    }

    public void setDeskInfo(String deskInfo) {
        this.deskInfo = deskInfo;
    }

    public void setTitleImgUrl(String titleImgUrl) {
        this.titleImgUrl = titleImgUrl;
    }

    public void setVoteMsg(String voteMsg) {
        this.voteMsg = voteMsg;
    }

    public List<String> getPostImgList() {
        if (postImgList != null)
            return postImgList;
        else
            return null;
    }

    public String getDeskInfo() {
        return deskInfo;
    }

    public String getTitleImgUrl() {
        return titleImgUrl;
    }

    public String getVoteMsg() {
        return voteMsg;
    }
}
