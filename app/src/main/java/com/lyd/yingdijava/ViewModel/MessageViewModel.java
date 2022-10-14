package com.lyd.yingdijava.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.Repository.MessageRepository;
import com.lyd.yingdijava.ViewModel.CallBack.SimpleListCallBack;

import java.util.HashMap;
import java.util.List;

public class MessageViewModel extends ViewModel {
    private final String TAG = "MessageViewModel";

    private MutableLiveData<List<NewsNode>> newsList;

    private MutableLiveData<String> errorMessage;

    public MessageViewModel(){
        newsList = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public void getNewsListFromModel(String tagName){
        MessageRepository.getInstance().getNewsList(tagName,new SimpleListCallBack<NewsNode>() {
            @Override
            public void onSuccess(List<NewsNode> list) {
                newsList.postValue(list);
            }

            @Override
            public void onError(String msg) {
                errorMessage.postValue(msg);
            }
        });
    }

    public LiveData<String> getErrorLiveData(){
        return errorMessage;
    }

}
