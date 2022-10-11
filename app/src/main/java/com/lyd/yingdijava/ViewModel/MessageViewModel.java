package com.lyd.yingdijava.ViewModel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lyd.yingdijava.Entity.News.NewsNode;

import java.util.List;

public class MessageViewModel extends ViewModel {
    private MutableLiveData<List<NewsNode>> newsList;



}
