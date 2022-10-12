package com.lyd.yingdijava.NetWork;

import android.util.Log;

import androidx.annotation.NonNull;

import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWorkManager {
    private static final String TAG = "NetWorkManager";

    private static volatile NetWorkManager instances;
    private static volatile OkHttpClient okHttpClient;
    private static volatile Retrofit retrofit;

    private static final int TIME_OUT = 30;//超时时间

    private NetWorkManager() {}

    public static NetWorkManager getInstances() {
        if (instances == null){
            synchronized (NetWorkManager.class){
                if (instances == null){
                    instances = new NetWorkManager();
                }
            }
        }
        return instances;
    }

    public static void setNull(){
        instances = null;
        retrofit = null;
        System.gc();
    }

    private OkHttpClient initClient(){
        if (okHttpClient == null){
            synchronized (NetWorkManager.class){
                if (okHttpClient == null){
                    //请求日志打印
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(@NonNull String s) {
                            try {

                                s = s.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                                Log.e(TAG, URLDecoder.decode(s,"utf-8"));

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "catch log: " + s );
                            }
                        }
                    });
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    //创建okhttpClient
                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .addInterceptor(loggingInterceptor)
                            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    public Retrofit initRetrofit(String baseUrl){
        if (retrofit == null){
            synchronized (NetWorkManager.class){
                if (retrofit == null){
                    //创建retrofit
                    retrofit = new Retrofit.Builder()
                            .client(initClient())
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public Retrofit initRetrofitWithRxJava(String baseUrl){
        if (retrofit == null){
            synchronized (NetWorkManager.class){
                if (retrofit == null){
                    retrofit = new Retrofit.Builder()
                            .client(initClient())
                            .baseUrl(baseUrl)
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
