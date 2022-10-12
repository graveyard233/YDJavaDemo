package com.lyd.yingdijava.Repository.InterFace;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IMessage {
    //这里不能用接口来拿网页数据，就算是string也接收不了数据，只能靠okhttp读流
}
