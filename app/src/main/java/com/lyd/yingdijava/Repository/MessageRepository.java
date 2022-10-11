package com.lyd.yingdijava.Repository;

import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.ViewModel.SimpleListCallBack;

public class MessageRepository {
    private static MessageRepository instance;

    private MessageRepository(){ }

    public static MessageRepository getInstance(){
        if (instance == null){
            synchronized (MessageRepository.class){
                if (instance == null)
                    instance = new MessageRepository();
            }
        }
        return instance;
    }

    public void getNewsList(String tagName,SimpleListCallBack<NewsNode> callBack){

    }
}
