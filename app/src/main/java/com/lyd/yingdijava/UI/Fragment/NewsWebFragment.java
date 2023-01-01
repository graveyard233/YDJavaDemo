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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bytedance.scene.Scene;
import com.bytedance.scene.ktx.NavigationSceneExtensionsKt;
import com.bytedance.scene.navigation.OnBackPressedListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.lyd.yingdijava.Entity.Comment.CommentItem;
import com.lyd.yingdijava.Entity.Comment.CommentList;
import com.lyd.yingdijava.Entity.Comment.CommentsNode;
import com.lyd.yingdijava.Entity.Comment.CommentsNodeBuilder;
import com.lyd.yingdijava.Info.UrlInfo;
import com.lyd.yingdijava.JsFile.MyJs;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Adapter.CallBack.ItemClickListener;
import com.lyd.yingdijava.UI.Adapter.CommentsMultiItemAdapter;
import com.lyd.yingdijava.UI.Widget.SpacesItemDecoration;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class NewsWebFragment extends Scene {

    private static final int GET_COMMENT_DELAY = 100;

    private AgentWeb webView;
    private LinearLayout linear;
    private DrawerLayout drawerLayout;
    private CoordinatorLayout commentsContainer;

    private boolean lock_comment;//控制评论的锁，只能触发一次
    private int commentCheckTimes;//<=5

    //因为数据就在这个webView产生，而不是在仓库层使用专属请求产生，所以就在这里进行数据post和监听变化
    private MutableLiveData<CommentList<CommentsNode<CommentItem>>> commentLiveData = new MutableLiveData<>();
    private RecyclerView commentRecycle;
    private CommentsMultiItemAdapter adapter;


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

        commentRecycle = findViewById(R.id.fragment_webView_comment_recycle);
        adapter = new CommentsMultiItemAdapter(new ArrayList<>(), new ItemClickListener(){
            @Override
            public void onClickImageForComments(int itemPosition, int imagePosition, boolean isMainComment, int replyPosition) {
                super.onClickImageForComments(itemPosition, imagePosition, isMainComment, replyPosition);
                Bundle bundle = new Bundle();
                if (isMainComment){
                    bundle.putStringArrayList("LIST", (ArrayList<String>) adapter.getItem(itemPosition).getMain_comment().getComment().getImg_url_list());
                } else {
                    bundle.putStringArrayList("LIST", (ArrayList<String>) adapter.getItem(itemPosition).getReply_comments().get(replyPosition).getComment().getImg_url_list());
                }
                bundle.putInt("POSITION",imagePosition);
                ImgGalleryFragment imgGalleryFragment = new ImgGalleryFragment();
                imgGalleryFragment.setArguments(bundle);
                NavigationSceneExtensionsKt.getNavigationScene(NewsWebFragment.this)
                        .push(imgGalleryFragment);
            }
        });



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

        initRecycle();
        commentLiveData.observe(this, new Observer<CommentList<CommentsNode<CommentItem>>>() {
            @Override
            public void onChanged(CommentList<CommentsNode<CommentItem>> commentsNodeCommentList) {
                List<CommentsNode<CommentItem>> lastList;
                if (commentsNodeCommentList.getHot_comments() != null){
                    lastList = commentsNodeCommentList.getHot_comments();
                    lastList.addAll(commentsNodeCommentList.getNormal_comments());
                    SpacesItemDecoration itemDecoration = new SpacesItemDecoration(commentRecycle.getContext(),SpacesItemDecoration.VERTICAL,
                            commentsNodeCommentList.getHot_num() - 1,
                            lastList.size() - commentsNodeCommentList.getHot_num() )
                            .setParam(R.color.春梅红,5,30,30);
                    commentRecycle.addItemDecoration(itemDecoration);
                } else {
                    lastList = commentsNodeCommentList.getNormal_comments();
                }

                adapter.submitList(lastList);
            }
        });
    }

    private void initRecycle(){
        adapter.setEmptyViewEnable(true);
        adapter.setEmptyViewLayout(this.requireSceneContext(),R.layout.layout_load_error);
        commentRecycle.setLayoutManager(new LinearLayoutManager(requireSceneContext()));
        // TODO: 2022/12/30 这里以后需要做一个判断，长按复制整个文本，或者是用户自己复制，这些交给用户选择，以后可以写在配置项中，目前先长按复制
        adapter.addOnItemChildLongClickListener(R.id.item_comment_text, new BaseQuickAdapter.OnItemChildLongClickListener<CommentsNode<CommentItem>>() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter<CommentsNode<CommentItem>, ?> baseQuickAdapter, @NonNull View view, int i) {
                ClipboardUtils.copyText(baseQuickAdapter.getItem(i).getMain_comment().getComment().getText());
                ToastUtils.make().show("已经复制: " + ClipboardUtils.getText());
                return false;
            }
        });
        commentRecycle.setAdapter(adapter);
    }

    private void setWebView(String url){
        Log.d("Web", "setWebView: " + url);
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
            if (request.getUrl().toString().startsWith("wanxiu://innerlink")){
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

                                    commentLiveData.postValue(commentList);

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

                            CommentList<CommentsNode<CommentItem>> commentList = new CommentList<>(toHotEntity(getHotCommentsList(boxes)),//先拿热评，热评在第一个box里面
                                    toHotEntity(getHotCommentsList(boxes)).size(),
                                    toEntity(getNormalCommentsList(comments)));

                            commentLiveData.postValue(commentList);
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

    /**
     * @param comment_item_list 可靠的comment-item-component的list
     * */
    private List<CommentsNode<CommentItem>> toHotEntity(Elements comment_item_list){
        List<CommentsNode<CommentItem>> tempList = new ArrayList<>();

        for (Element e :
                comment_item_list) {
            tempList.add(CommentsNodeBuilder.getInstance()
                    .initHotBaseNode()
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
                default:break;
            }
        }
    };


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
