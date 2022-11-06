package com.lyd.yingdijava.UI.Adapter;

import android.content.Context;
import android.util.Log;
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
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.google.gson.Gson;
import com.lyd.yingdijava.Entity.Community.CommunityPostNode;
import com.lyd.yingdijava.Entity.Deck.HsDeckInfo;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.Utils.TextUtils;

import java.util.HashMap;
import java.util.List;

// TODO: 2022/10/23 准备测试多布局 
public class CommunityMultiItemAdapter extends BaseMultiItemAdapter<CommunityPostNode> {

    private static final int ROUTINE = 0;
    private static final int ARTICLE = 1;
    private static final int DECK = 2;

    public CommunityMultiItemAdapter(@NonNull List<? extends CommunityPostNode> items) {
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
                imgsView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.routine_imgs_bar,null,false);
                routineVH.imgBar.addView(imgsView);

                if (communityPostNode.getPostImgList() != null && communityPostNode.getPostImgList().size() > 0){
                    int imgNum = communityPostNode.getPostImgList().size();
                    LinearLayout targetBar = (LinearLayout) imgsView.getChildAt(imgNum - 1);
                    for (int j = 0; j < targetBar.getChildCount(); j++) {
                        Glide.with(getContext())
                                .load(communityPostNode.getPostImgList().get(j))
                                .placeholder(R.drawable.img_loading)
                                .error(R.drawable.img_load_error)
                                .into((ImageView) targetBar.getChildAt(j));
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
        addItemType(ROUTINE,RoutineVH.class,listenerRoutine);
        addItemType(ARTICLE,ArticleVH.class,listenerArticle);
        addItemType(DECK,DeckHsVH.class,listenerDeckHs);
        onItemViewType(new OnItemViewTypeListener<CommunityPostNode>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends CommunityPostNode> list) {
                switch (list.get(i).getPostType()){
                    case ArticlePost:
                        return ARTICLE;
                    case DeskPost:
                        return DECK;
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
        public RoutineVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_routine_title);
            textPreview = itemView.findViewById(R.id.item_post_routine_text);
            imgBar = itemView.findViewById(R.id.item_post_routine_imgBar);
            userImg = itemView.findViewById(R.id.user_img_portrait);
            userName = itemView.findViewById(R.id.user_text_name);
            userLevel = itemView.findViewById(R.id.user_text_level);
        }
    }

    protected class ArticleVH extends RecyclerView.ViewHolder{
        TextView title;
        ImageView img;
        LinearLayout root;
        public ArticleVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_article_title);
            img = itemView.findViewById(R.id.item_post_article_img);
            root = itemView.findViewById(R.id.item_post_Article_linear);
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
        public DeckHsVH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_post_deck_title);
            textPreview = itemView.findViewById(R.id.item_post_deck_text);
            imgBar = itemView.findViewById(R.id.item_post_deck_imgBar);
            postLinear = itemView.findViewById(R.id.item_post_deck_linear);
            userImg = itemView.findViewById(R.id.user_img_portrait);
            userName = itemView.findViewById(R.id.user_text_name);
            userLevel = itemView.findViewById(R.id.user_text_level);
        }
    }
}
