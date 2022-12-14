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

    private boolean lock_comment;//???????????????????????????????????????
    private int commentCheckTimes;//<=5

    //????????????????????????webView???????????????????????????????????????????????????????????????????????????????????????post???????????????
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
        //??????
        lock_comment = true;
        commentCheckTimes = 0;//??????????????????
        setWebView(UrlInfo.getInstance().getUrlByKey("webView") + getArguments().getString("URL"));
        NavigationSceneExtensionsKt.requireNavigationScene(this)
                .addOnBackPressedListener(this, new OnBackPressedListener() {
                    @Override
                    public boolean onBackPressed() {
                        if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {//??????????????????????????????
                            //????????????????????????webView?????????????????????
                            return webView.back();
                        } else {
                            Toast.makeText(NewsWebFragment.this.getActivity(), "?????????????????????~", Toast.LENGTH_SHORT).show();
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
                            .setParam(R.color.?????????,5,30,30);
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
        // TODO: 2022/12/30 ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        adapter.addOnItemChildLongClickListener(R.id.item_comment_text, new BaseQuickAdapter.OnItemChildLongClickListener<CommentsNode<CommentItem>>() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter<CommentsNode<CommentItem>, ?> baseQuickAdapter, @NonNull View view, int i) {
                ClipboardUtils.copyText(baseQuickAdapter.getItem(i).getMain_comment().getComment().getText());
                ToastUtils.make().show("????????????: " + ClipboardUtils.getText());
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
                Log.i("TAG", "shouldOverrideUrlLoading: scheme?????????APP???????????????????????????");
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
                // ???????????????????????????????????????????????????????????????????????????
                if (lock_comment && request.getUrl().toString().startsWith("https://www.google")){
                    Log.i("TAG", "shouldInterGET_COMMENT_DELAYceptRequest: " + request.getUrl().toString());

                    handler.sendEmptyMessage(GET_COMMENT_DELAY);
                    lock_comment= false;//??????????????????????????????????????????????????????????????????????????????????????????????????????
                }

            }


            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {


            super.onPageFinished(view, url);
//            view.loadUrl(MyJs.getHtml);//??????js
            //??????js???????????????
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
                        if (boxes.size() == 1){// ??????1?????????????????? 1.????????? 2.????????????????????????????????? 3.?????????
                            if (boxes.get(0).select("span.select").get(0).text().equals("???????????? 0???")){
                                Log.e("TAG", "????????????????????????");

//                                initFrameWithoutComment();
                            } else {
                                //??????????????????????????????????????????
                                // ?????????????????????????????????????????????????????????box?????????????????????????????????????????????
                                // ????????????????????????????????????????????????????????????
                                Elements div_empty = comments.select("div.m-40");
                                if (div_empty.size() == 0){//?????????div.m-40??????????????????????????????????????????????????????????????????????????????????????????
                                    Log.i("TAG", "???????????? ??????????????????????????????????????????????????????????????????");
                                    CommentList<CommentsNode<CommentItem>> commentList = new CommentList<>(null,0,
                                            toEntity(getNormalCommentsList(comments)));
//                                    commentList.printNormalComments(commentList.getNormal_comments());

                                    commentLiveData.postValue(commentList);

//                                    initFrameWithoutHot(commentList);
                                } else if (div_empty.size() == 1){//div.m-40??????????????????????????????????????????????????????????????????????????????
                                    Log.i("TAG", "?????????????????????????????????????????????????????????????????? times = " + commentCheckTimes);
                                    //??????????????????????????????
                                    if (commentCheckTimes <= 5){
                                        handler.sendEmptyMessageDelayed(GET_COMMENT_DELAY,1000);
                                        commentCheckTimes ++;
                                    }
                                }

                            }
                        } else { //box ???size ?????????1?????????????????????????????????????????????????????????
                            Log.d("TAG", "?????????????????????box????????????");

                            CommentList<CommentsNode<CommentItem>> commentList = new CommentList<>(toHotEntity(getHotCommentsList(boxes)),//?????????????????????????????????box??????
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
     * @param comment_item_list ?????????comment-item-component???list
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
     * @param comment_item_list ?????????comment-item-component???list
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
