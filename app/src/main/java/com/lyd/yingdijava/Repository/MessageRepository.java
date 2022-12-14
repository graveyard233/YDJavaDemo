package com.lyd.yingdijava.Repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lyd.yingdijava.Entity.Banner.BannerNode;
import com.lyd.yingdijava.Entity.Banner.BannerRoot;
import com.lyd.yingdijava.Entity.Community.BaseCommunityNode;
import com.lyd.yingdijava.Entity.Community.CommunityPostFoot;
import com.lyd.yingdijava.Entity.Community.CommunityPostNode;
import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.Entity.News.NewsNodeFoot;
import com.lyd.yingdijava.Entity.User.User;
import com.lyd.yingdijava.Entity.Vote.VoteMsg;
import com.lyd.yingdijava.Info.UrlInfo;
import com.lyd.yingdijava.Utils.TextUtils;
import com.lyd.yingdijava.ViewModel.CallBack.SimpleListCallBack;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MessageRepository {
    private static final String TAG = "MessageRepository";

    private static final String mySign  = "LYDSIGN";

    private static MessageRepository instance;

    private OkHttpClient okHttpClient;

    private MessageRepository(){
        okHttpClient = new OkHttpClient();
    }

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

                        //????????????????????????html????????????????????????
//                        callBack.onError("success: " +tempHTML);
                        Document doc = Jsoup.parse(tempHTML);
                        try {
                            Elements postList = doc.select("section.fine-list").first()
                                    .select("div.post-item");
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
                            callBack.onError("empty");
                            return;
                        }

                        stringBuffer.insert(11,"\"").insert(0,"\"");//???top_content????????????

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

    public void getCommunityPostList(String tagName,String order, SimpleListCallBack<CommunityPostNode> callBack){
        String url = UrlInfo.getInstance().getUrlByKey(tagName) + order;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callBack.onError("error");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200){
                    if (response.body() != null){
                        String tempPostList;
                        try {
                            tempPostList = response.body().string();
                        } catch (IOException e) {
                            callBack.onError("error");
                            return;
                        }
                        Document doc = Jsoup.parse(tempPostList);
                        Element script = doc.select("script").get(5);
                        Elements postList_html = doc.select("div.post-list-component").first().children();

                        List<CommunityPostNode> nodeList = new ArrayList<>();
                        for (Element e :
                                postList_html) {
                            try {
                                CommunityPostNode node = new CommunityPostNode();
                                node.setTitle(e.select("div.title").text());
                                if (e.firstElementChild().tagName().equals("a")){
    //                                Log.d(TAG, "???????????????: " + e.select("div.title").text());
                                    node.setPostType(BaseCommunityNode.PostType.ArticlePost);
                                    node.setTitleImgUrl(e.select("img.cover").first().attr("src"));
                                } else if (e.select("div.mt-10").size() == 0 && e.select("div.vote-result").size() == 0){
//                                    Log.i(TAG, "???????????????,????????????: " + e.select("div.title").text());
                                    node.setPostType(BaseCommunityNode.PostType.RoutinePost);
                                    node.setText_preView(e.select("div.desc").first().text());
                                    List<String> tempImgList = new ArrayList<>();
                                    if (e.select("ul.imgs-area").size() > 0){
                                        if (e.selectFirst("ul.imgs-area").select("div.shade").size() > 0){
                                            //?????????????????????div???????????????????????????????????????3????????????script??????
                                            StringBuffer sb = new StringBuffer(script.toString());
                                            sb.delete(0,sb.indexOf("postsList"))
                                                    .delete(0,sb.indexOf(node.getTitle()))
                                                    .delete(0,sb.indexOf("imgs:"))
                                                    .delete(0,6)
                                                    .delete(sb.indexOf("\",cover:"),sb.length());
                                            tempImgList.addAll(Arrays.asList(TextUtils.unicodeStr2String(sb.toString()).split("`")));
                                        } else {
                                            for (Element div :
                                                    e.select("div.img-item")) {
                                                tempImgList.add(TextUtils.getImageUrlFromStyle(div.attr("style")));
                                            }
                                        }
                                        node.setPostImgList(tempImgList);
                                    }
                                    //??????????????????
                                    node.setUser(new User());
                                    node.getUser().setPortrait_url(e.selectFirst("img.avatar").attr("src"));
                                    node.getUser().setName(e.selectFirst("a.name").selectFirst("span").text());
                                    node.getUser().setLevel(e.selectFirst("li.level").text());
                                } else if (e.select("div.deck-item").size() > 0){
                                    node.setPostType(BaseCommunityNode.PostType.DeskPost);
                                    if (e.select("div.desc").size() > 0)//???????????????????????????
                                        node.setText_preView(e.select("div.desc").first().text());
                                    //????????????????????????json?????????
                                    StringBuffer sb = new StringBuffer(script.toString());
                                    sb.delete(0,sb.indexOf("postsList"))
                                            .delete(0,sb.indexOf(node.getTitle()))
                                            .delete(0,sb.indexOf("deck_info_json"))//??????
                                            .delete(0,16)
                                            .delete(sb.indexOf("\",url:"),sb.length());//??????
                                    String deckInfoJson = TextUtils.unicodeStr2String(sb.toString())
                                            .replaceAll("\\\\\\\\\\\\",mySign)//??????????????????????????????????????????????????????????????????????????????string?????????
                                            .replaceAll("\\\\","")//????????????????????????Java????????????????????????????????????
                                            .replaceAll(mySign,"\\\\\\\\\\\\");//?????????????????????????????????
//                                      Log.i(TAG, "finalJson: " + finalJson);
                                    Log.i(TAG, "deckInfoJson: " + deckInfoJson);
                                    //??????????????????tag???????????????????????????????????????
                                    node.setDeckTag(tagName);
                                    node.setDeckInfo(deckInfoJson);//??????adapter????????????????????????????????????
//                                          Log.w(TAG, "???????????????: " + e.select("div.title").text());
                                    //??????????????????
                                    node.setUser(new User());
                                    node.getUser().setPortrait_url(e.selectFirst("img.avatar").attr("src"));
                                    node.getUser().setName(e.selectFirst("a.name").selectFirst("span").text());
                                    node.getUser().setLevel(e.selectFirst("li.level").text());

                                } else if (e.select("div.vote-result").size() > 0){
                                    node.setPostType(BaseCommunityNode.PostType.VotePost);
                                    //??????????????????
                                    node.setUser(new User());
                                    node.getUser().setPortrait_url(e.selectFirst("img.avatar").attr("src"));
                                    node.getUser().setName(e.selectFirst("a.name").selectFirst("span").text());
                                    node.getUser().setLevel(e.selectFirst("li.level").text());
                                    //??????????????????
                                    node.setText_preView(e.select("div.desc").first().text());
                                    //??????????????????
                                    VoteMsg voteMsg = new VoteMsg();
                                    voteMsg.setViewPoint1(e.select("p.vote-text").first().text());
                                    voteMsg.setViewPoint2(e.select("p.vote-text").get(1).text());
                                    voteMsg.setViewPoint_line1(e.select("div.vote-line").first().select("span").first().text());
                                    voteMsg.setViewPoint_line2(e.select("div.vote-line").get(1).select("span").first().text());
                                    node.setVoteMsg(voteMsg);

    //                                Log.e(TAG, "?????????: " + e.select("div.title").text());
                                }
                                //????????????foot
                                List<String> tagsList = new ArrayList<>();
                                CommunityPostFoot foot = new CommunityPostFoot();
                                for (Element tagElement :
                                        e.selectFirst("div.tags").select("span")) {
                                    tagsList.add(tagElement.text());
                                }
                                foot.setTagList(tagsList);
                                if (e.selectFirst("div.info").getElementsByTag("span").size() >= 5){
                                    foot.setLikeNum(e.selectFirst("div.info").getElementsByTag("span").get(0).text());
                                    foot.setReplyNum(e.selectFirst("div.info").getElementsByTag("span").get(2).text());
                                    foot.setTime(e.selectFirst("div.info").getElementsByTag("span").get(4).text());
                                }
                                node.setFoot(foot);

                                if (node.getPostType().equals(BaseCommunityNode.PostType.ArticlePost)){//??????????????????????????????????????????????????????
                                    node.setUrl(e.selectFirst("a.block").attr("href"));//???????????????????????????article???????????????????????????????????????????????????????????????
                                } else {
                                    node.setUrl(e.selectFirst("div.feed-list-common").child(1).attr("href"));
                                }
                                nodeList.add(node);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }
                        callBack.onSuccess(nodeList);
                    }
                } else {
                    callBack.onError("error");
                }
            }
        });
    }
}
