package com.lyd.yingdijava.ViewModel.CallBack;

import java.util.List;

public interface SimpleListCallBack<T> {
    void onSuccess(List<T> list);

    void onError(String msg);
}
