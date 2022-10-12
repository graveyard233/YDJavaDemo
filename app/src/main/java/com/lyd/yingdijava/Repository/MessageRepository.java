package com.lyd.yingdijava.Repository;

import android.util.Log;

import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.NetWork.NetWorkManager;
import com.lyd.yingdijava.Repository.InterFace.IMessage;
import com.lyd.yingdijava.ViewModel.CallBack.SimpleListCallBack;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageRepository {
    private static final String TAG = "MessageRepository";

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
