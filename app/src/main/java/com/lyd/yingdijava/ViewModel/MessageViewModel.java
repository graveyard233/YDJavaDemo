package com.lyd.yingdijava.ViewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lyd.yingdijava.Entity.Banner.BannerNode;
import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.Repository.MessageRepository;
import com.lyd.yingdijava.ViewModel.CallBack.SimpleListCallBack;

import java.util.HashMap;
import java.util.List;

public class MessageViewModel extends ViewModel {
    private final String TAG = "MessageViewModel";

    private MutableLiveData<List<BannerNode>> bannerList;

    private MutableLiveData<List<NewsNode>> newsList;

    private MutableLiveData<String> errorMessage;

    public MessageViewModel(){
        newsList = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        bannerList = new MutableLiveData<>();
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

    public void getBannerListFromModel(String tagName){
        MessageRepository.getInstance().getBannerList(tagName, new SimpleListCallBack<BannerNode>() {
            @Override
            public void onSuccess(List<BannerNode> list) {
                bannerList.postValue(list);
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

    public LiveData<List<NewsNode>> getNewsList() {
        return newsList;
    }

    public LiveData<List<BannerNode>> getBannerList(){
        return bannerList;
    }
}
