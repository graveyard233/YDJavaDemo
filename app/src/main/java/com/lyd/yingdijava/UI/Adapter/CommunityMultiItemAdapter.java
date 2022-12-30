package com.lyd.yingdijava.UI.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.google.gson.Gson;
import com.lyd.yingdijava.Entity.Community.CommunityPostNode;
import com.lyd.yingdijava.Entity.Deck.HsDeckInfo;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Adapter.CallBack.ItemClickListener;
import com.lyd.yingdijava.Utils.TextUtils;

import java.util.HashMap;
import java.util.List;

public class CommunityMultiItemAdapter extends BaseMultiItemAdapter<CommunityPostNode> {

    private static final int ROUTINE = 0;
    private static final int ARTICLE = 1;
    private static final int DECK = 2;
    private static final int VOTE = 3;

    public CommunityMultiItemAdapter(@NonNull List<? extends CommunityPostNode> items, ItemClickListener clickListener) {
        super(items);
        OnMultiItemAdapterListener<CommunityPostNode,RoutineVH> listenerRoutine = new OnMultiItemAdapterListener<CommunityPostNode, RoutineVH>() {
            @NonNull
            @Override
            public RoutineVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new RoutineVH(LayoutInflater.from(context).inflate(R.layout.item_post_routine,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull RoutineVH routineVH, int i, @Nullable CommunityPostNode communityPostNode) {
                routineVH.title.setText(communityPostNode.getTitle());
                routineVH.textPreview.setText(communityPostNode.getText_preView());

                LinearLayout imgsView = null;
                if (routineVH.imgBar.getChildCount() > 0){//已经加载过图片了，需要移除之前的图片
//                    imgsView = (LinearLayout) routineVH.imgBar.getChildAt(0);//必须确保只有一个
//                    for (int j = 0; j < imgsView.getChildCount(); j++) {
//                        LinearLayout smallBar = (LinearLayout) imgsView.getChildAt(j);
//                        for (int k = 0; k < smallBar.getChildCount(); k++) {
//                            ImageView targetImg = (ImageView) smallBar.getChildAt(k);
//                            targetImg.setImageBitmap(null);
//                            Glide.with(getContext()).clear(targetImg);
//                        }
//                    }
                    routineVH.imgBar.removeAllViews();
                }
                //没有，则为第一次创建，需要初始化并注入
                imgsView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.bar_routine_imgs,null,false);
                routineVH.imgBar.addView(imgsView);

                if (communityPostNode.getPostImgList() != null && communityPostNode.getPostImgList().size() > 0){
                    int imgNum = communityPostNode.getPostImgList().size();
                    if (imgNum > 3) imgNum = 3;
                    LinearLayout targetBar = (LinearLayout) imgsView.getChildAt(imgNum - 1);
                    for (int j = 0; j < targetBar.getChildCount(); j++) {
                        Glide.with(getContext())
                                .load(communityPostNode.getPostImgList().get(j))
                                .placeholder(R.drawable.img_loading)
                                .error(R.drawable.img_load_error)
                                .into((ImageView) targetBar.getChildAt(j));
                        final int finalJ = j;
                        targetBar.getChildAt(j).setOnClickListener(view -> { clickListener.onClickImage(i, finalJ); });
                    }
                }
                if (communityPostNode.getUser() != null){
                    routineVH.userName.setText(communityPostNode.getUser().getName());
                    routineVH.userLevel.setText(communityPostNode.getUser().getLevel());
                    Glide.with(getContext())
                            .load(communityPostNode.getUser().getPortrait_url())
                            .placeholder(R.drawable.img_loading)
                            .error(R.drawable.img_load_error)
                            .into(routineVH.userImg);
                }
                if (communityPostNode.getFoot() != null){
                    routineVH.footTag.setText("#" + communityPostNode.getFoot().getTagList().get(0));
                    routineVH.footLike.setText(communityPostNode.getFoot().getLikeNum());
                    routineVH.footReply.setText(communityPostNode.getFoot().getReplyNum());
                    routineVH.footTime.setText(communityPostNode.getFoot().getTime());
                }



            }
        };
        OnMultiItemAdapterListener<CommunityPostNode, ArticleVH> listenerArticle = new OnMultiItemAdapterListener<CommunityPostNode, ArticleVH>() {
            @NonNull
            @Override
            public ArticleVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new ArticleVH(LayoutInflater.from(context).inflate(R.layout.item_post_article,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull ArticleVH articleVH, int i, @Nullable CommunityPostNode communityPostNode) {
                communityPostNode = getItems().get(i);
                articleVH.title.setText(communityPostNode.getTitle());
                Glide.with(getContext())
                        .load(communityPostNode.getTitleImgUrl())
                        .placeholder(R.drawable.img_loading)
                        .error(R.drawable.img_load_error)
                        .into(articleVH.img);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                final int ten = ConvertUtils.px2dp(60f);
                lp.setMargins(ten,ten,ten,ten);
                if (communityPostNode.getUser() == null){
                    articleVH.root.getChildAt(0).setVisibility(View.GONE);
                    lp.topMargin += ConvertUtils.px2dp(48f);
                } else {
                    articleVH.root.getChildAt(0).setVisibility(View.VISIBLE);
                }
                articleVH.root.setLayoutParams(lp);
                if (communityPostNode.getFoot() != null){
                    articleVH.footTag.setText("#" + communityPostNode.getFoot().getTagList().get(0));
                    articleVH.footLike.setText(communityPostNode.getFoot().getLikeNum());
                    articleVH.footReply.setText(communityPostNode.getFoot().getReplyNum());
                    articleVH.footTime.setText(communityPostNode.getFoot().getTime());
                }
            }
        };
        OnMultiItemAdapterListener<CommunityPostNode,DeckHsVH> listenerDeckHs = new OnMultiItemAdapterListener<CommunityPostNode, DeckHsVH>() {
            @NonNull
            @Override
            public DeckHsVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new DeckHsVH(LayoutInflater.from(context).inflate(R.layout.item_post_deck,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull DeckHsVH deckHsVH, int i, @Nullable CommunityPostNode communityPostNode) {
                deckHsVH.title.setText(communityPostNode.getTitle());
                deckHsVH.textPreview.setText(communityPostNode.getText_preView());
//                CardView cardView = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.item_deck_preview,getRecyclerView());
                if (communityPostNode.getUser() != null){
                    deckHsVH.userName.setText(communityPostNode.getUser().getName());
                    deckHsVH.userLevel.setText(communityPostNode.getUser().getLevel());
                    Glide.with(getContext())
                            .load(communityPostNode.getUser().getPortrait_url())
                            .placeholder(R.drawable.img_loading)
                            .error(R.drawable.img_load_error)
                            .into(deckHsVH.userImg);
                }
                if (communityPostNode.getFoot() != null){
                    deckHsVH.footTag.setText("#" + communityPostNode.getFoot().getTagList().get(0));
                    deckHsVH.footLike.setText(communityPostNode.getFoot().getLikeNum());
                    deckHsVH.footReply.setText(communityPostNode.getFoot().getReplyNum());
                    deckHsVH.footTime.setText(communityPostNode.getFoot().getTime());
                }
                if (communityPostNode.getDeckInfo() == null)
                    return;
                switch (communityPostNode.getDeckTag()){
                    case "炉石":
                        HsDeckInfo deckInfo = new Gson().fromJson(communityPostNode.getDeckInfo(),HsDeckInfo.class);
                        View deckView = null;/*(View) LayoutInflater.from(getContext()).inflate(R.layout.item_deck_preview,null,false);*/

                        if (deckHsVH.postLinear.getChildCount() == 4){//代表这个卡组view已经加载了，直接更新最后一个
                            deckView = deckHsVH.postLinear.getChildAt(deckHsVH.postLinear.getChildCount() - 1);
                        } else {//这个view还没初始化
                            deckView =(View) LayoutInflater.from(getContext()).inflate(R.layout.item_deck_preview,null,false);
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            int ten = ConvertUtils.px2dp(60);
                            lp.setMargins(ten,ten,ten,ten);
                            deckHsVH.postLinear.addView(deckView,lp);
                        }

                        TextView deckTitle = deckView.findViewById(R.id.item_deck_preview_title);
                        TextView deckCost = deckView.findViewById(R.id.item_deck_preview_cost);
                        TextView deckLegend = deckView.findViewById(R.id.item_deck_preview_legend_num);
                        TextView deckEpic = deckView.findViewById(R.id.item_deck_preview_epic_num);
                        TextView deckRare = deckView.findViewById(R.id.item_deck_preview_rare_num);
                        TextView deckCommon = deckView.findViewById(R.id.item_deck_preview_common_num);
                        ImageView deckBackground = deckView.findViewById(R.id.item_deck_preview_background);

                        deckTitle.setText("[" + deckInfo.getFormat() + "] " + deckInfo.getName());
                        deckCost.setText(String.valueOf(deckInfo.getPrice()));
                        HashMap<String,String> hashMap = new HashMap<>();
                        for (String needToSplit :
                                deckInfo.getRarityInfo().replaceAll("\\{", "").replaceAll("\\}", "").replaceAll("\\\\", "").split("\\,")) {
                            String[] tempS = needToSplit.replaceAll("\"", "").split("\\:");
                            hashMap.put(tempS[0],tempS[1]);
                        }
                        deckLegend.setText(hashMap.get("传说"));
                        deckEpic.setText(hashMap.get("史诗"));
                        deckRare.setText(hashMap.get("稀有"));
                        deckCommon.setText(hashMap.get("普通"));
                        Glide.with(getContext())
                                .load("https://static.iyingdi.cn/yingdiWeb/images/tools/decks/hearthstone/faction/back/" + TextUtils.upperFirstCase(deckInfo.getFaction()) + ".png")
                                .placeholder(R.drawable.img_loading)
                                .error(R.drawable.img_load_error)
                                .into(deckBackground);

                        break;
                    default:break;
                }
            }
        };
        OnMultiItemAdapterListener<CommunityPostNode,VoteVH> listenerVote = new OnMultiItemAdapterListener<CommunityPostNode, VoteVH>() {
            @NonNull
            @Override
            public VoteVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new VoteVH(LayoutInflater.from(context).inflate(R.layout.item_post_vote,viewGroup,false));
        }

            @Override
            public void onBind(@NonNull VoteVH voteVH, int i, @Nullable CommunityPostNode communityPostNode) {
                voteVH.title.setText(communityPostNode.getTitle());
                voteVH.textPreview.setText(communityPostNode.getText_preView());
                if (communityPostNode.getUser() != null){
                    voteVH.userName.setText(communityPostNode.getUser().getName());
                    voteVH.userLevel.setText(communityPostNode.getUser().getLevel());
                    Glide.with(getContext())
                            .load(communityPostNode.getUser().getPortrait_url())
                            .placeholder(R.drawable.img_loading)
                            .error(R.drawable.img_load_error)
                            .into(voteVH.userImg);
                }
                if (communityPostNode.getFoot() != null){
                    voteVH.footTag.setText("#" + communityPostNode.getFoot().getTagList().get(0));
                    voteVH.footLike.setText(communityPostNode.getFoot().getLikeNum());
                    voteVH.footReply.setText(communityPostNode.getFoot().getReplyNum());
                    voteVH.footTime.setText(communityPostNode.getFoot().getTime());
                }
                if (communityPostNode.getVoteMsg() == null)
                    return;
                voteVH.viewPoint1.setText(communityPostNode.getVoteMsg().getViewPoint1());
                voteVH.viewPoint2.setText(communityPostNode.getVoteMsg().getViewPoint2());
                voteVH.viewPoint_line1.setText(communityPostNode.getVoteMsg().getViewPoint_line1());
                voteVH.viewPoint_line2.setText(communityPostNode.getVoteMsg().getViewPoint_line2());
                voteVH.viewPoint_line1.getLayoutParams().width = (int) (ScreenUtils.getScreenWidth() / 2 * Float.parseFloat(communityPostNode.getVoteMsg().getViewPoint_line1().replaceAll("%", "")) / 100);
                voteVH.viewPoint_line2.getLayoutParams().width = (int) (ScreenUtils.getScreenWidth() / 2 * Float.parseFloat(communityPostNode.getVoteMsg().getViewPoint_line2().replaceAll("%", "")) / 100);
            }
        };
        addItemType(ROUTINE,RoutineVH.class,listenerRoutine);
        addItemType(ARTICLE,ArticleVH.class,listenerArticle);
        addItemType(DECK,DeckHsVH.class,listenerDeckHs);
        addItemType(VOTE,VoteVH.class,listenerVote);
        onItemViewType(new OnItemViewTypeListener<CommunityPostNode>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends CommunityPostNode> list) {
                switch (list.get(i).getPostType()){
                    case ArticlePost:
                        return ARTICLE;
                    case DeskPost:
                        return DECK;
                    case VotePost:
                        return VOTE;
                    default:
                        return ROUTINE;
                }
            }
        });
    }

    protected class RoutineVH extends RecyclerView.ViewHolder{
        TextView title;
        TextView textPreview;
        LinearLayout imgBar;
        ImageView userImg;
        TextView userName;
        TextView userLevel;
        TextView footTag;
        TextView footLike;
        TextView footReply;
        TextView footTime;
        public RoutineVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_routine_title);
            textPreview = itemView.findViewById(R.id.item_post_routine_text);
            imgBar = itemView.findViewById(R.id.item_post_routine_imgBar);
            userImg = itemView.findViewById(R.id.user_img_portrait);
            userName = itemView.findViewById(R.id.user_text_name);
            userLevel = itemView.findViewById(R.id.user_text_level);
            footTag = itemView.findViewById(R.id.bar_item_foot_tagName);
            footLike = itemView.findViewById(R.id.bar_item_foot_likeNum);
            footReply = itemView.findViewById(R.id.bar_item_foot_replyNum);
            footTime = itemView.findViewById(R.id.bar_item_foot_time);
        }
    }

    protected class ArticleVH extends RecyclerView.ViewHolder{
        TextView title;
        ImageView img;
        LinearLayout root;
        TextView footTag;
        TextView footLike;
        TextView footReply;
        TextView footTime;
        public ArticleVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_article_title);
            img = itemView.findViewById(R.id.item_post_article_img);
            root = itemView.findViewById(R.id.item_post_Article_linear);
            footTag = itemView.findViewById(R.id.bar_item_foot_tagName);
            footLike = itemView.findViewById(R.id.bar_item_foot_likeNum);
            footReply = itemView.findViewById(R.id.bar_item_foot_replyNum);
            footTime = itemView.findViewById(R.id.bar_item_foot_time);
        }
    }

    protected class DeckHsVH extends RecyclerView.ViewHolder{
        TextView title;
        TextView textPreview;
        LinearLayout imgBar;
        LinearLayout postLinear;
        ImageView userImg;
        TextView userName;
        TextView userLevel;
        TextView footTag;
        TextView footLike;
        TextView footReply;
        TextView footTime;
        public DeckHsVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_deck_title);
            textPreview = itemView.findViewById(R.id.item_post_deck_text);
            imgBar = itemView.findViewById(R.id.item_post_deck_imgBar);
            postLinear = itemView.findViewById(R.id.item_post_deck_linear);
            userImg = itemView.findViewById(R.id.user_img_portrait);
            userName = itemView.findViewById(R.id.user_text_name);
            userLevel = itemView.findViewById(R.id.user_text_level);
            footTag = itemView.findViewById(R.id.bar_item_foot_tagName);
            footLike = itemView.findViewById(R.id.bar_item_foot_likeNum);
            footReply = itemView.findViewById(R.id.bar_item_foot_replyNum);
            footTime = itemView.findViewById(R.id.bar_item_foot_time);
        }
    }

    protected class VoteVH extends RecyclerView.ViewHolder{
        TextView title;
        TextView textPreview;
        TextView viewPoint1;
        TextView viewPoint2;
        TextView viewPoint_line1;
        TextView viewPoint_line2;

        ImageView userImg;
        TextView userName;
        TextView userLevel;
        TextView footTag;
        TextView footLike;
        TextView footReply;
        TextView footTime;
        public VoteVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_vote_title);
            textPreview = itemView.findViewById(R.id.item_post_vote_text);
            viewPoint1 = itemView.findViewById(R.id.item_post_vote_viewPoint1_text);
            viewPoint2 = itemView.findViewById(R.id.item_post_vote_viewPoint2_text);
            viewPoint_line1 = itemView.findViewById(R.id.item_post_vote_viewPoint_line1);
            viewPoint_line2 = itemView.findViewById(R.id.item_post_vote_viewPoint_line2);

            userImg = itemView.findViewById(R.id.user_img_portrait);
            userName = itemView.findViewById(R.id.user_text_name);
            userLevel = itemView.findViewById(R.id.user_text_level);
            footTag = itemView.findViewById(R.id.bar_item_foot_tagName);
            footLike = itemView.findViewById(R.id.bar_item_foot_likeNum);
            footReply = itemView.findViewById(R.id.bar_item_foot_replyNum);
            footTime = itemView.findViewById(R.id.bar_item_foot_time);
        }
    }

}
