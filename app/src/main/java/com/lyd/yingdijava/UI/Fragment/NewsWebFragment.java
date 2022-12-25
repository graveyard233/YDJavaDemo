package com.lyd.yingdijava.UI.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.scene.Scene;
import com.bytedance.scene.ktx.NavigationSceneExtensionsKt;
import com.bytedance.scene.navigation.OnBackPressedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.lyd.yingdijava.Entity.Comment.CommentItem;
import com.lyd.yingdijava.Entity.Comment.CommentList;
import com.lyd.yingdijava.Entity.Comment.CommentsNode;
import com.lyd.yingdijava.Entity.Comment.CommentsNodeBuilder;
import com.lyd.yingdijava.Info.UrlInfo;
import com.lyd.yingdijava.JsFile.MyJs;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Adapter.CommentsMultiItemAdapter;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class NewsWebFragment extends Scene {

    private static final int GET_COMMENT_DELAY = 100;
    private static final int HAS_HOT = 101;
    private static final int WITHOUT_HOT = 102;
    private static final int EMPTY = 103;

    private AgentWeb webView;
    private LinearLayout linear;
    private DrawerLayout drawerLayout;
    private CoordinatorLayout commentsContainer;

    private boolean lock_comment;//控制评论的锁，只能触发一次
    private int commentCheckTimes;//<=5

    private Gson gson;


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_news_web,viewGroup,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linear = findViewById(R.id.fragment_webView_webScene_container);
        ToastUtils.make().show(getArguments().getString("URL"));
        commentsContainer = findViewById(R.id.fragment_webView_comment_container);
        ViewGroup.LayoutParams lp = commentsContainer.getLayoutParams();
        lp.width = ScreenUtils.getAppScreenWidth();

        drawerLayout = findViewById(R.id.fragment_webView_drawerLayout);

        gson = new Gson();



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //干活
        lock_comment = true;
        commentCheckTimes = 0;//初始化计数器
        setWebView(UrlInfo.getInstance().getUrlByKey("webView") + getArguments().getString("URL"));
        NavigationSceneExtensionsKt.requireNavigationScene(this)
                .addOnBackPressedListener(this, new OnBackPressedListener() {
                    @Override
                    public boolean onBackPressed() {
                        if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {//这里评论已经收起来了
                            //我需要判断这里的webView是否是最后一个
                            return webView.back();
                        } else {
                            Toast.makeText(NewsWebFragment.this.getActivity(), "再按一次就退出~", Toast.LENGTH_SHORT).show();
                            drawerLayout.closeDrawer(Gravity.RIGHT);
                            return true;
                        }
                    }
                });
    }

    private void setWebView(String url){
        webView = AgentWeb.with(requireActivity())
                .setAgentWebParent(linear,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setWebViewClient(webViewClient)
                .createAgentWeb()
                .ready()
                .go(url);

    }

    private WebViewClient webViewClient = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.i("TAG", "shouldOverrideUrlLoading: " + request.getUrl().toString());
            if (request.getUrl().toString().startsWith("wanxiu://innerlink?type=deck_detail")){
                Log.i("TAG", "shouldOverrideUrlLoading: scheme的营地APP跳转，直接拦截拒绝");
                return true;
            }

            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onLoadResource(WebView view, String url) {

            super.onLoadResource(view, url);
            view.evaluateJavascript(MyJs.removeFloatBar,
                    new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.e("TAG", "removeFloatBar finish: "+ value);
                        }
                    });

        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//            view.post(new Runnable() {
//                @Override
//                public void run() {
//                    Uri uri = Uri.parse(view.getUrl());
//                    Log.i("TAG", "shouldInterceptRequest: " + uri.getEncodedPath());
//                }
//            });


            if (request.getMethod().equals("POST")){
                // 这里获取时，有小概率会拿不到数据，需要反复异步请求
                if (lock_comment && request.getUrl().toString().startsWith("https://www.google")){
                    Log.i("TAG", "shouldInterGET_COMMENT_DELAYceptRequest: " + request.getUrl().toString());

                    handler.sendEmptyMessage(GET_COMMENT_DELAY);
                    lock_comment= false;//这里只处理一次评论数据，之后不论如何都不处理，只有进来那一次才能触发
                }

            }


            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {


            super.onPageFinished(view, url);
//            view.loadUrl(MyJs.getHtml);//加载js
            //注入js，但没调用
            view.evaluateJavascript(MyJs.getHtml, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {

                }
            });
            view.evaluateJavascript(MyJs.removeOpenApp, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.e("TAG", "removeOpenApp: "+ value);
                }
            });


        }
    };

    private void getComments(AgentWeb webView){
        webView.getJsAccessEntrace().quickCallJs("getHtmlCode", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if (value != null && !value.equals("null")){
                    String trueHtml = StringEscapeUtils.unescapeEcmaScript(value);
                    Document doc = Jsoup.parse(trueHtml);

                    try {
                        Element comments = doc.getElementById("viewComments");
//                                    Log.i("TAG", "comments: " + comments.toString());
                        Elements boxes = comments.select("div.comments-box");
//                                    Log.i("TAG", "boxes size: " + boxes.size());
                        if (boxes.size() == 1){// 等于1的话就有可能 1.没评论 2.网页端还没异步加载出来 3.没热评
                            if (boxes.get(0).select("span.select").get(0).text().equals("全部评论 0条")){
                                Log.e("TAG", "这个帖子没有评论");

                                handler.sendEmptyMessage(EMPTY);
//                                initFrameWithoutComment();
                            } else {
                                //有评论，分为两个情况来讨论，
                                // 一是没有热评，只有普通评论，也只有一个box，所以要看评论量，也可以直接找
                                // 二是但还没加载出来，所以要再等一下再获取
                                Elements div_empty = comments.select("div.m-40");
                                if (div_empty.size() == 0){//不存在div.m-40，评论已经加载好了，而且是没有热评的，这一块可以获取评论数据
                                    Log.i("TAG", "有数据了 空评论被移除了，可以拿数据，这个评论没有热评");
                                    CommentList<CommentsNode<CommentItem>> commentList = new CommentList<>(null,0,
                                            toEntity(getNormalCommentsList(comments)));
//                                    commentList.printNormalComments(commentList.getNormal_comments());

                                    Message msg = Message.obtain();
                                    msg.what = WITHOUT_HOT;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("WITHOUT_HOT",gson.toJson(commentList));
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);

//                                    initFrameWithoutHot(commentList);
                                } else if (div_empty.size() == 1){//div.m-40还存在，有评论但数据没有加载好，应该等一下再获取一次
                                    Log.i("TAG", "有评论但数据没有加载好，应该等一下再获取一次 times = " + commentCheckTimes);
                                    //注释掉，省的反复获取
                                    if (commentCheckTimes <= 5){
                                        handler.sendEmptyMessageDelayed(GET_COMMENT_DELAY,1000);
                                        commentCheckTimes ++;
                                    }

                                }

                            }
                        } else { //box 的size 不等于1，即这个帖子有评论了，可以拿其中的数据
                            Log.d("TAG", "带热评，有两个box数据来了");

                            CommentList<CommentsNode<CommentItem>> commentList = new CommentList<>(toEntity(getHotCommentsList(boxes)),//先拿热评，热评在第一个box里面
                                    toEntity(getHotCommentsList(boxes)).size(),
                                    toEntity(getNormalCommentsList(comments)));
//                            commentList.printHotComments(commentList.getHot_comments());

//                            Log.i("TAG", "  ");
//                            initFrameWithHot(commentList);

//                            commentList.printNormalComments(commentList.getNormal_comments());

                            Message msg = Message.obtain();
                            msg.what = HAS_HOT;
                            Bundle bundle = new Bundle();
                            bundle.putString("HAS_HOT",gson.toJson(commentList));
                            msg.setData(bundle);
                            handler.sendMessage(msg);

                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private Elements getHotCommentsList(Elements boxes){
        return boxes.get(0).select("div.px-30")
                .get(0).select("div.comment-item-component");
    }

    private Elements getNormalCommentsList(Element div){
        return div.select("div.comments-box").get(div.select("div.comments-box").size() == 2 ? 1 : 0)
                .nextElementSibling()
                .child(0).select("div.comment-item-component");
    }

    /**
     * @param comment_item_list 可靠的comment-item-component的list
     * */
    private List<CommentsNode<CommentItem>> toEntity(Elements comment_item_list){
        List<CommentsNode<CommentItem>> tempList = new ArrayList<>();

        for (Element e :
                comment_item_list) {
            tempList.add(CommentsNodeBuilder.getInstance()
                    .initBaseNode()
                    .buildMain(e)
                    .buildReply(e)
                    .getNode());
        }

        return tempList;
    }


    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case GET_COMMENT_DELAY:
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            getComments(webView);
                        }
                    }.start();
                    break;
                case EMPTY:
                    initFrameWithoutComment();
                    break;
                case HAS_HOT:
                    Bundle bundleHasHot = msg.getData();
                    CommentList<CommentsNode<CommentItem>> commentListHasHot = gson.fromJson(bundleHasHot.getString("HAS_HOT"),new TypeToken<CommentList<CommentsNode<CommentItem>>>(){}.getType());
                    commentListHasHot.printHotComments(commentListHasHot.getHot_comments());
                    initFrameWithHot(commentListHasHot);
                    break;
                case WITHOUT_HOT:
                    Bundle bundleWithoutHot = msg.getData();
                    CommentList<CommentsNode<CommentItem>> commentListWithoutHot = gson.fromJson(bundleWithoutHot.getString("WITHOUT_HOT"),new TypeToken<CommentList<CommentsNode<CommentItem>>>(){}.getType());
                    commentListWithoutHot.printNormalComments(commentListWithoutHot.getNormal_comments());
                    initFrameWithoutHot(commentListWithoutHot);
                    break;
                default:break;
            }
        }
    };



    /*--------------------构建评论界面START--------------------------*/

    private void initFrameWithoutHot(CommentList<CommentsNode<CommentItem>> commentList){//没热评的框架
        if (commentsContainer.getChildCount() > 0)
            commentsContainer.removeAllViews();
        View frameWithoutHot = LayoutInflater.from(getSceneContext()).inflate(R.layout.frame_comments_without_hot,null,false);
        commentsContainer.addView(frameWithoutHot);
        RecyclerView withoutHotRecycle = frameWithoutHot.findViewById(R.id.frame_withoutHot_recycle);
        CommentsMultiItemAdapter adapter = new CommentsMultiItemAdapter(commentList.getNormal_comments());
        withoutHotRecycle.setLayoutManager(new LinearLayoutManager(requireSceneContext()));
        withoutHotRecycle.setAdapter(adapter);

    }

    private void initFrameWithHot(CommentList<CommentsNode<CommentItem>> commentList){//有热评的框架
        if (commentsContainer.getChildCount() > 0)
            commentsContainer.removeAllViews();
        View frameWithHot = LayoutInflater.from(getSceneContext()).inflate(R.layout.frame_comments_with_hot,null,false);
        commentsContainer.addView(frameWithHot);
        RecyclerView hotRecycle = frameWithHot.findViewById(R.id.frame_withHot_recycle_hotComments);
        RecyclerView restRecycle = frameWithHot.findViewById(R.id.frame_withHot_recycle_restComments);
        CommentsMultiItemAdapter adapterHot = new CommentsMultiItemAdapter(commentList.getHot_comments());
        CommentsMultiItemAdapter adapterRest = new CommentsMultiItemAdapter(commentList.getNormal_comments());
        hotRecycle.setLayoutManager(new LinearLayoutManager(requireSceneContext()));
        hotRecycle.setAdapter(adapterHot);
        restRecycle.setLayoutManager(new LinearLayoutManager(requireSceneContext()));
        restRecycle.setAdapter(adapterRest);

    }

    private void initFrameWithoutComment(){//没评论的界面
        if (commentsContainer.getChildCount() > 0)
            commentsContainer.removeAllViews();
        TextView emptyText = new TextView(requireSceneContext());
        emptyText.setText("没有评论");
        emptyText.setTextSize(SizeUtils.px2dp(50f));
        emptyText.setGravity(View.TEXT_ALIGNMENT_CENTER);
        commentsContainer.addView(emptyText);
    }

    /*--------------------构建评论界面END--------------------------*/


    @Override
    public void onPause() {
        super.onPause();
        webView.getWebLifeCycle().onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.getWebLifeCycle().onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.getWebLifeCycle().onDestroy();
    }
}
