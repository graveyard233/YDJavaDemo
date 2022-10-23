package com.lyd.yingdijava.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lyd.yingdijava.Entity.Banner.BannerNode;
import com.lyd.yingdijava.Entity.Community.CommunityPostNode;
import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.Repository.MessageRepository;
import com.lyd.yingdijava.ViewModel.CallBack.SimpleListCallBack;

import java.util.List;

public class MessageViewModel extends ViewModel {
    private final String TAG = "MessageViewModel";

    private MutableLiveData<List<BannerNode>> bannerList;

    private MutableLiveData<List<NewsNode>> newsList;

    private MutableLiveData<String> newsError;

    private MutableLiveData<String> bannerError;

    private MutableLiveData<List<CommunityPostNode>> communityPostList;

    private MutableLiveData<String> communityPostError;

    public MessageViewModel(){
        newsList = new MutableLiveData<>();
        newsError = new MutableLiveData<>();
        bannerList = new MutableLiveData<>();
        bannerError = new MutableLiveData<>();
        communityPostList = new MutableLiveData<>();
        communityPostError = new MutableLiveData<>();
    }

    public void getNewsListFromModel(String tagName){
        MessageRepository.getInstance().getNewsList(tagName,new SimpleListCallBack<NewsNode>() {
            @Override
            public void onSuccess(List<NewsNode> list) {
                newsList.postValue(list);
            }

            @Override
            public void onError(String msg) {
                newsError.postValue(msg);
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
                bannerError.postValue(msg);
            }
        });
    }

    public void getCommunityPostListByHotFromModel(String tagName){
        MessageRepository.getInstance().getCommunityPostList(tagName, new SimpleListCallBack<CommunityPostNode>(){

            @Override
            public void onSuccess(List<CommunityPostNode> list) {
                communityPostList.postValue(list);
            }

            @Override
            public void onError(String msg) {
                communityPostError.postValue(msg);
            }
        });
    }

    public LiveData<String> getNewsErrorLiveData(){
        return newsError;
    }

    public LiveData<List<NewsNode>> getNewsList() {
        return newsList;
    }

    public LiveData<List<BannerNode>> getBannerList(){
        return bannerList;
    }

    public LiveData<String> getBannerErrorLiveData(){
        return bannerError;
    }

    public LiveData<List<CommunityPostNode>> getCommunityPostList(){
        return communityPostList;
    }

    public LiveData<String> getCommunityPostErrorLiveData(){
        return communityPostError;
    }
}
