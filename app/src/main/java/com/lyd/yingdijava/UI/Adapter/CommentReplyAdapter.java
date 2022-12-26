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

import com.blankj.utilcode.constant.RegexConstants;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyd.yingdijava.Entity.Comment.CommentItem;
import com.lyd.yingdijava.R;

import java.util.List;

public class CommentReplyAdapter extends BaseQuickAdapter<CommentItem, CommentReplyAdapter.ReplyVH> {


    public CommentReplyAdapter(@NonNull List<? extends CommentItem> items) {
        super(items);
    }

    public CommentReplyAdapter() {
        super();
    }

    @Override
    protected void onBindViewHolder(@NonNull ReplyVH replyVH, int i, @Nullable CommentItem commentItem) {

        if (commentItem.getCommentUser().getToWho() != null){
            replyVH.userName.setText(commentItem.getCommentUser().getName() + " -> " + commentItem.getCommentUser().getToWho());
        } else {
            replyVH.userName.setText(commentItem.getCommentUser().getName());
        }
        replyVH.userLevel.setText(commentItem.getCommentUser().getLevel());
        Glide.with(getContext())
                .load(commentItem.getCommentUser().getPortrait_url())
                .placeholder(R.drawable.img_loading)
                .error(R.drawable.img_load_error)
                .into(replyVH.userImg);

        replyVH.comment.setText(commentItem.getComment().getText());
        if (replyVH.imgBarContainer.getChildCount() > 0){
            replyVH.imgBarContainer.removeAllViews();
        }
        if (commentItem.getComment().getImg_url_list() != null
                && commentItem.getComment().getImg_url_list().size() > 0){
            LinearLayout imgsView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.bar_comment_img,null,false);
            replyVH.imgBarContainer.addView(imgsView);
            for (int j = 0; j < commentItem.getComment().getImg_url_list().size(); j++) {
                Glide.with(getContext())
                        .load(commentItem.getComment().getImg_url_list().get(j))
                        .placeholder(R.drawable.img_loading)
                        .error(R.drawable.img_load_error)
                        .into((ImageView) imgsView.getChildAt(j));
            }
        }

        replyVH.footTime.setText(commentItem.getCommentInfo().getTime());
        replyVH.footIP.setText(commentItem.getCommentInfo().getIP());
        if (commentItem.getCommentInfo().getLike().matches(RegexConstants.REGEX_POSITIVE_INTEGER)){
            replyVH.footLike.setText("赞:" + commentItem.getCommentInfo().getLike());
        } else {
            replyVH.footLike.setText(commentItem.getCommentInfo().getLike());
        }

    }

    @NonNull
    @Override
    protected ReplyVH onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new ReplyVH(LayoutInflater.from(context).inflate(R.layout.item_comment_reply,viewGroup,false));
    }

    protected class ReplyVH extends RecyclerView.ViewHolder{
        ImageView userImg;
        TextView userName;
        TextView userLevel;

        TextView comment;
        LinearLayout imgBarContainer;

        TextView footTime;
        TextView footIP;
        TextView footLike;
//        TextView footReply;
        public ReplyVH(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_img_portrait);
            userName = itemView.findViewById(R.id.user_text_name);
            userLevel = itemView.findViewById(R.id.user_text_level);

            comment = itemView.findViewById(R.id.item_comment_reply_text);
            imgBarContainer = itemView.findViewById(R.id.item_comment_reply_imgBar_container);

            footTime = itemView.findViewById(R.id.bar_comment_foot_time);
            footIP = itemView.findViewById(R.id.bar_comment_foot_IP);
            footLike = itemView.findViewById(R.id.bar_comment_foot_likeNum);
//            footReply = itemView.findViewById(R.id.bar_comment_foot_replyNum);
        }
    }
}
