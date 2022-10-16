package com.lyd.yingdijava.Repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lyd.yingdijava.Entity.Banner.BannerNode;
import com.lyd.yingdijava.Entity.Banner.BannerRoot;
import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.Entity.News.NewsNodeFoot;
import com.lyd.yingdijava.Info.UrlInfo;
import com.lyd.yingdijava.NetWork.NetWorkManager;
import com.lyd.yingdijava.Repository.InterFace.IMessage;
import com.lyd.yingdijava.Utils.TextUtils;
import com.lyd.yingdijava.ViewModel.CallBack.SimpleListCallBack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MessageRepository {
    private static final String TAG = "MessageRepository";

    private static MessageRepository instance;

    private OkHttpClient okHttpClient;

    private MessageRepository(){ okHttpClient = new OkHttpClient(); }

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
        String url = UrlInfo.getInstance().getUrlByKey(tagName);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Mobile Safari/537.36")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Accept-Language","zh-CN,zh;q=0.9")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callBack.onError("error: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200){
                    if (response.body() != null){
                        String tempHTML;
                        try {
                            tempHTML = response.body().string();
                        } catch (Exception e){
                            callBack.onError("error: " + e.getMessage());
                            return;
                        }

                        //在这里处理，解析html获取官方推流列表
//                        callBack.onError("success: " +tempHTML);
                        Document doc = Jsoup.parse(tempHTML);
                        try {
                            Elements postList = doc.select("section.fine-list").first()
                                    .select("div.post-item");
                            Log.i(TAG, "onResponse: " + postList.size());
                            List<NewsNode> tempNewsList = new ArrayList<>();
                            if (postList.size() > 0){
                                for (Element item:
                                     postList) {

                                    try {
                                        tempNewsList.add(new NewsNode(
                                                item.getElementsByTag("a").first().attr("href"),
                                                item.select("div.title").first().text(),
                                                TextUtils.getImageUrlFromStyle(item.select("div.photo").first().attr("style")),
                                                new NewsNodeFoot(item.select("div.review").first().getElementsByTag("span").first().text(),
                                                        null)
                                        ));
                                    } catch (Exception e) {
                                        Log.e(TAG, "onResponse: " + e.getMessage());
                                    }
                                }
                                callBack.onSuccess(tempNewsList);
                            }
                        } catch (Exception e){
                            callBack.onError(e.getMessage());
                        }
                    }
                }
            }
        });
    }

    public void getBannerList(String tagName,SimpleListCallBack<BannerNode> callBack){
        String url = UrlInfo.getInstance().getUrlByKey(tagName);
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callBack.onError(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200){
                    if (response.body() != null){
                        String tempBanner;
                        try {
                            tempBanner = response.body().string();
                        } catch (Exception e) {
                            callBack.onError("error");
                            return;
                        }
                        Document doc = Jsoup.parse(tempBanner);
                        Element script = doc.select("script").get(5);
                        StringBuffer stringBuffer = new StringBuffer(script.toString());

                        stringBuffer.delete(0,stringBuffer.indexOf("top_content"))
                                .delete(stringBuffer.indexOf("filledIdList") - 2,stringBuffer.length());

                        if (stringBuffer.toString().length() < 20){
                            Log.i(TAG, "onResponse: empty");
                            return;
                        }

                        stringBuffer.insert(11,"\"").insert(0,"\"");//给top_content加双引号

                        List<String> parList = new ArrayList<>();
                        parList.add("ad_id:");
                        parList.add("img:");
                        parList.add("url:");
                        parList.add("title:");
                        int count = TextUtils.getCountFromString(stringBuffer.toString(),parList.get(1));
                        for (int i = 0; i < parList.size(); i++) {
                            String par = parList.get(i);
                            for (int j = 0; j < count; j++) {
                                stringBuffer.insert(stringBuffer.indexOf(par),"\"")
                                        .insert(stringBuffer.indexOf(par) + par.length() - 1, "\"");
                            }
                        }
                        stringBuffer.insert(0,"{").append("}");
//                        Log.i(TAG, "onResponse: " + stringBuffer);
                        Gson gson = new Gson();
                        BannerRoot root = gson.fromJson(stringBuffer.toString(),BannerRoot.class);
                        callBack.onSuccess(root.getTop_content());
                    }
                }
            }
        });
    }
}
