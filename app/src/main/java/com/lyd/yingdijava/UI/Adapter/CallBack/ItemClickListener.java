package com.lyd.yingdijava.UI.Adapter.CallBack;

/**
 * 公共的自定义点击方法，统一管理
 * */
public abstract class ItemClickListener {
    /**
     * 常规的帖子中的图片的点击事件
     *
     * @param itemPosition  item的位置下标
     * @param imagePosition 图片url在list中的位置
     */
    public void onClickImage(final int itemPosition, final int imagePosition) {

    }

    /**
     * 专门给评论中的图片用的点击事件
     * @param itemPosition 主评论的下标
     * @param imagePosition 图片的位置
     * @param isMainComment 是不是主评论
     * @param replyPosition 评论回复的下标,假如isMainComment=true，则内置为-1
     * */
    public void onClickImageForComments(final int itemPosition, final int imagePosition, boolean isMainComment, int replyPosition){
        if (isMainComment){
            replyPosition = -1;
        }
    }

}
