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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.constant.RegexConstants;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyd.yingdijava.Entity.Comment.CommentItem;
import com.lyd.yingdijava.Entity.Comment.CommentsNode;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Adapter.CallBack.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CommentsMultiItemAdapter extends BaseMultiItemAdapter<CommentsNode<CommentItem>> {

    private static final int IS_HOT = 0;
    private static final int NOT_HOT = 1;


    public CommentsMultiItemAdapter(@NonNull List<? extends CommentsNode<CommentItem>> items, ItemClickListener clickListener) {
        super(items);
        OnMultiItemAdapterListener<CommentsNode<CommentItem>, CommentVH> listenerComment = new OnMultiItemAdapterListener<CommentsNode<CommentItem>, CommentVH>() {
            @NonNull
            @Override
            public CommentVH onCreate(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
                return new CommentVH(LayoutInflater.from(context).inflate(R.layout.item_comment,viewGroup,false));
            }

            @Override
            public void onBind(@NonNull CommentVH commentVH, int i, @Nullable CommentsNode<CommentItem> commentItemCommentsNode) {
                //设置用户栏
                if (commentItemCommentsNode.getMain_comment().getCommentUser() != null){
                    commentVH.userName.setText(commentItemCommentsNode.getMain_comment().getCommentUser().getName());
                    commentVH.userLevel.setText(commentItemCommentsNode.getMain_comment().getCommentUser().getLevel());
                    Glide.with(getContext())
                            .load(commentItemCommentsNode.getMain_comment().getCommentUser().getPortrait_url())
                            .placeholder(R.drawable.img_loading)
                            .error(R.drawable.img_load_error)
                            .into(commentVH.userImg);
                }
                //评论文本
                commentVH.comment.setText(commentItemCommentsNode.getMain_comment().getComment().getText());
                //评论图片
                if (commentVH.imgBarContainer.getChildCount() > 0){
                    commentVH.imgBarContainer.removeAllViews();//先进行清理
                }
                if (commentItemCommentsNode.getMain_comment().getComment().getImg_url_list() != null
                        && commentItemCommentsNode.getMain_comment().getComment().getImg_url_list().size() > 0){
                    //不为空才进行bar的初始化，加载图片，否则不做任何事情，节约资源
                    LinearLayout imgsView = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.bar_comment_img,null,false);
                    commentVH.imgBarContainer.addView(imgsView);

                    for (int j = 0; j < commentItemCommentsNode.getMain_comment().getComment().getImg_url_list().size(); j++) {
                        Glide.with(getContext())
                                .load(commentItemCommentsNode.getMain_comment().getComment().getImg_url_list().get(j))
                                .placeholder(R.drawable.img_loading)
                                .error(R.drawable.img_load_error)
                                .into((ImageView) imgsView.getChildAt(j));
                        final int finalJ = j;
                        imgsView.getChildAt(j).setOnClickListener(view -> { clickListener.onClickImageForComments(i, finalJ,true,0); });
                    }
                }
                //评论底部部分
                commentVH.footTime.setText(commentItemCommentsNode.getMain_comment().getCommentInfo().getTime());
                commentVH.footIP.setText(commentItemCommentsNode.getMain_comment().getCommentInfo().getIP());
                if (commentItemCommentsNode.getMain_comment().getCommentInfo().getLike().matches(RegexConstants.REGEX_POSITIVE_INTEGER)){
                    commentVH.footLike.setText("赞: " + commentItemCommentsNode.getMain_comment().getCommentInfo().getLike());
                } else {
                    commentVH.footLike.setText(commentItemCommentsNode.getMain_comment().getCommentInfo().getLike());
                }
                if (commentItemCommentsNode.getMain_comment().getCommentInfo().getReplyNum().matches(RegexConstants.REGEX_POSITIVE_INTEGER)){
                    commentVH.footReply.setText("回复: " + commentItemCommentsNode.getMain_comment().getCommentInfo().getReplyNum());
                } else {
                    commentVH.footReply.setText(commentItemCommentsNode.getMain_comment().getCommentInfo().getReplyNum());
                }

                CommentReplyAdapter adapter = new CommentReplyAdapter();
                commentVH.replyRecycle.setAdapter(adapter);
                if (commentItemCommentsNode.hasReply()){
                    commentVH.replyRecycle.setLayoutManager(new LinearLayoutManager(getContext()));

                    adapter.addReplyItemClickListener(new ItemClickListener() {
                        @Override
                        public void onClickImageForComments(int itemPosition, int imagePosition, boolean isMainComment, int replyPosition) {
                            clickListener.onClickImageForComments(i,imagePosition,false,replyPosition);//记得这里的itemPosition要变成i
                        }
                    });
                    adapter.addOnItemChildLongClickListener(R.id.item_comment_reply_text, new OnItemChildLongClickListener<CommentItem>() {
                        @Override
                        public boolean onItemLongClick(@NonNull BaseQuickAdapter<CommentItem, ?> baseQuickAdapter, @NonNull View view, int i) {
                            ClipboardUtils.copyText(baseQuickAdapter.getItem(i).getComment().getText());
                            ToastUtils.make().show("已复制: " + ClipboardUtils.getText());
                            return false;
                        }
                    });
                    commentVH.replyRecycle.setAdapter(adapter);
                    adapter.submitList(commentItemCommentsNode.getReply_comments());
                } else {
                    adapter.submitList(new ArrayList<>());
                }
            }
        };

        addItemType(NOT_HOT, CommentVH.class,listenerComment);
        onItemViewType(new OnItemViewTypeListener<CommentsNode<CommentItem>>() {
            @Override
            public int onItemViewType(int i, @NonNull List<? extends CommentsNode<CommentItem>> list) {
                // TODO: 2023/1/1 现在想到新品种的评论了，屏蔽功能，给item加个tag，是不是被屏蔽的用户，屏蔽列表写在数据库然后导入全局变量。以后加了功能再说
                if (list.get(i).isHot)
                    return NOT_HOT;
                else
                    return NOT_HOT;//为假，这个不是热评
            }
        });
    }


    protected class CommentVH extends RecyclerView.ViewHolder{

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

        public CommentVH(@NonNull View itemView) {
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
