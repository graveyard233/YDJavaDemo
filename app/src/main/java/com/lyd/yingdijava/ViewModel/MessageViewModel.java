package com.lyd.yingdijava.ViewModel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.Repository.MessageRepository;

import java.util.List;

public class MessageViewModel extends ViewModel {
    private MutableLiveData<List<NewsNode>> newsList;

    private MutableLiveData<String> errorMessage;

    public void getNewsListFromModel(String tagName){
        MessageRepository.getInstance().getNewsList(tagName,new SimpleListCallBack<NewsNode>() {
            @Override
            public void onSuccess(List<NewsNode> list) {

            }

            @Override
            public void onError(String msg) {

            }
        });
    }

}
