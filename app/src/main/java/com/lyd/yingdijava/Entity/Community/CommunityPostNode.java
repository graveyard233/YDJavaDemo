package com.lyd.yingdijava.Entity.Community;

import java.util.List;

public class CommunityPostNode extends BaseCommunityNode implements CommunityPostTypeBuild{

    private List<String> postImgList;

    private String deskInfo;

    private String titleImgUrl;

    private String voteMsg;

    @Override
    public List<String> buildPostImgList() {

        return null;
    }

    @Override
    public String buildDeskInfo() {
        return null;
    }

    @Override
    public String buildTitleImg() {
        return null;
    }

    @Override
    public void buildVoteMsg() {

    }
}
