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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.RegexConstants;
import com.blankj.utilcode.util.RegexUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.lyd.yingdijava.Entity.Comment.CommentItem;
import com.lyd.yingdijava.Entity.Comment.CommentsNode;
import com.lyd.yingdijava.R;

import java.util.ArrayList;
import java.util.List;

public class CommentsMultiItemAdapter extends BaseMultiItemAdapter<CommentsNode<CommentItem>> {

    private static final int NO_REPLY = 0;
    private static final int HAS_REPLY = 1;

    public CommentsMultiItemAdapter(@NonNull List<? extends CommentsNode<CommentItem>> items) {
        super(items);
        OnMultiItemAdapterListener<CommentsNode<CommentItem>,NoReplyVH> listenerNoReply = new OnMultiItemAdapterListener<CommentsNode<CommentItem>, NoReplyVH>() {
            @NonNull
            @Override
            public NoReplyVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new NoReplyVH(LayoutInflater.from(context).inflate(R.layout.item_comment_no_reply,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull NoReplyVH noReplyVH, int i, @Nullable CommentsNode<CommentItem> commentItemCommentsNode) {
                //设置用户栏
                if (commentItemCommentsNode.getMain_comment().getCommentUser() != null){
                    noReplyVH.userName.setText(commentItemCommentsNode.getMain_comment().getCommentUser().getName());
                    noReplyVH.userLevel.setText(commentItemCommentsNode.getMain_comment().getCommentUser().getLevel());
                    Glide.with(getContext())
                            .load(commentItemCommentsNode.getMain_comment().getCommentUser().getPortrait_url())
                            .placeholder(R.drawable.img_loading)
                            .error(R.drawable.img_load_error)
                            .into(noReplyVH.userImg);
                }
                //评论文本
                noReplyVH.comment.setText(commentItemCommentsNode.getMain_comment().getComment().getText());
                //评论图片
                if (noReplyVH.imgBarContainer.getChildCount() > 0){
                    noReplyVH.imgBarContainer.removeAllViews();//先进行清理
                }
                if (commentItemCommentsNode.getMain_comment().getComment().getImg_url_list() != null
                        && commentItemCommentsNode.getMain_comment().getComment().getImg_url_list().size() > 0){
                    //不为空才进行bar的初始化，加载图片，否则不做任何事情，节约资源
                    LinearLayout imgsView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.bar_comment_img,null,false);
                    noReplyVH.imgBarContainer.addView(imgsView);

                    for (int j = 0; j < commentItemCommentsNode.getMain_comment().getComment().getImg_url_list().size(); j++) {
                        Glide.with(getContext())
                                .load(commentItemCommentsNode.getMain_comment().getComment().getImg_url_list().get(j))
                                .placeholder(R.drawable.img_loading)
                                .error(R.drawable.img_load_error)
                                .into((ImageView) imgsView.getChildAt(j));
                    }
                }
                //评论底部部分
                noReplyVH.footTime.setText(commentItemCommentsNode.getMain_comment().getCommentInfo().getTime());
                noReplyVH.footIP.setText(commentItemCommentsNode.getMain_comment().getCommentInfo().getIP());
                if (commentItemCommentsNode.getMain_comment().getCommentInfo().getLike().matches(RegexConstants.REGEX_POSITIVE_INTEGER)){
                    noReplyVH.footLike.setText("赞: " + commentItemCommentsNode.getMain_comment().getCommentInfo().getLike());
                } else {
                    noReplyVH.footLike.setText(commentItemCommentsNode.getMain_comment().getCommentInfo().getLike());
                }
                if (commentItemCommentsNode.getMain_comment().getCommentInfo().getReplyNum().matches(RegexConstants.REGEX_POSITIVE_INTEGER)){
                    noReplyVH.footReply.setText("回复: " + commentItemCommentsNode.getMain_comment().getCommentInfo().getReplyNum());
                } else {
                    noReplyVH.footReply.setText(commentItemCommentsNode.getMain_comment().getCommentInfo().getReplyNum());
                }


                if (commentItemCommentsNode.hasReply()){
                    noReplyVH.replyRecycle.setLayoutManager(new LinearLayoutManager(getContext()));

                    noReplyVH.replyRecycle.setAdapter(new CommentReplyAdapter(commentItemCommentsNode.getReply_comments()));
                } else {
                    CommentReplyAdapter adapter = new CommentReplyAdapter();
                    noReplyVH.replyRecycle.setAdapter(adapter);
                    adapter.submitList(new ArrayList<>());
                }
            }
        };

        addItemType(NO_REPLY,NoReplyVH.class,listenerNoReply);
        onItemViewType(new OnItemViewTypeListener<CommentsNode<CommentItem>>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends CommentsNode<CommentItem>> list) {
                if (list.get(i).hasReply())
                    return NO_REPLY;// todo 为真，这里应该返回有回复的版本
                else
                    return NO_REPLY;//为假，这个没有子评论
            }
        });
    }


    protected class NoReplyVH extends RecyclerView.ViewHolder{

        ImageView userImg;
        TextView userName;
        TextView userLevel;

        TextView comment;
        LinearLayout imgBarContainer;

        TextView footTime;
        TextView footIP;
        TextView footLike;
        TextView footReply;

        RecyclerView replyRecycle;

        public NoReplyVH(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_img_portrait);
            userName = itemView.findViewById(R.id.user_text_name);
            userLevel = itemView.findViewById(R.id.user_text_level);

            comment = itemView.findViewById(R.id.item_comment_text);
            imgBarContainer = itemView.findViewById(R.id.item_comment_imgBar_container);

            footTime = itemView.findViewById(R.id.bar_comment_foot_time);
            footIP = itemView.findViewById(R.id.bar_comment_foot_IP);
            footLike = itemView.findViewById(R.id.bar_comment_foot_likeNum);
            footReply = itemView.findViewById(R.id.bar_comment_foot_replyNum);

            replyRecycle = itemView.findViewById(R.id.item_comment_reply_recycle);
        }
    }


}
