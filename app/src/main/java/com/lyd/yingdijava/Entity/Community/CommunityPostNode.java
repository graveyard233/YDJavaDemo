package com.lyd.yingdijava.Entity.Community;

import com.lyd.yingdijava.Entity.Vote.VoteMsg;

import java.util.List;

public class CommunityPostNode extends BaseCommunityNode{

    private List<String> postImgList;

    private String deckInfo;
    private String deckTag;

    private String titleImgUrl;

    private VoteMsg voteMsg;


    public void setPostImgList(List<String> postImgList) {
        this.postImgList = postImgList;
    }

    public void setDeckInfo(String deckInfo) {
        this.deckInfo = deckInfo;
    }
    public void setDeckTag(String deckTag) {
        this.deckTag = deckTag;
    }

    public void setTitleImgUrl(String titleImgUrl) {
        this.titleImgUrl = titleImgUrl;
    }

    public void setVoteMsg(VoteMsg voteMsg) {
        this.voteMsg = voteMsg;
    }

    public List<String> getPostImgList() {
        if (postImgList != null)
            return postImgList;
        else
            return null;
    }

    public String getDeckInfo() {
        if (deckInfo == null || deckInfo.equals(""))
            return null;
        else
            return deckInfo;
    }
    public String getDeckTag() {
        return deckTag;
    }

    public String getTitleImgUrl() {
        return titleImgUrl;
    }

    public VoteMsg getVoteMsg() {
        return voteMsg;
    }
}
