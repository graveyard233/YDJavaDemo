package com.lyd.yingdijava.Utils;

import com.lyd.yingdijava.Repository.SaveImageTask;

import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 只用于{@link SaveImageTask}
 * */
public abstract class BaseSubscriber<T> implements Observer<T>, FlowableSubscriber<T> {

    @Override
    public void onSubscribe(@NonNull Subscription s) {
        s.request(Integer.MAX_VALUE);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
