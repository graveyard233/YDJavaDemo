package com.lyd.yingdijava.ViewModel;

import java.util.List;

public interface SimpleListCallBack<T> {
    void onSuccess(List<T> list);

    void onError(String msg);
}
