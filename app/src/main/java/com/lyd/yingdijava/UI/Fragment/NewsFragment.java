package com.lyd.yingdijava.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.scene.group.UserVisibleHintGroupScene;
import com.bytedance.scene.ktx.NavigationSceneExtensionsKt;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyd.yingdijava.Entity.Banner.BannerNode;
import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Adapter.NewsBannerAdapter;
import com.lyd.yingdijava.UI.Adapter.NewsRecyclerViewAdapter;
import com.lyd.yingdijava.ViewModel.MessageViewModel;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends UserVisibleHintGroupScene {

    private final String TAG = "NewsScene";
    private MessageViewModel messageViewModel;

    private SmartRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private Banner banner;

    private String newsTag;
    private String getDataTag() {
        return newsTag;
    }

    @NonNull
    @Override
    public ViewGroup onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        //这里是放layout
        newsTag = getArguments().getString("TAG","炉石");
        return (ViewGroup) layoutInflater.inflate(R.layout.fragment_news,viewGroup,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //这里进行初始化View工作
        initViewModel();
        initBanner();
        initRecyclerView();

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //这里实现业务逻辑？一般是viewCreate先，然后再到这个
        observeLiveData();
        refreshLayout = findViewById(R.id.news_refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getList();
                getBanner();
            }
        });
    }

    private void initViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        messageViewModel = viewModelProvider.get(MessageViewModel.class);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.news_recyclerView);

        adapter = new NewsRecyclerViewAdapter();
        adapter.setEmptyViewEnable(true);
        adapter.setEmptyViewLayout(this.requireSceneContext(),R.layout.layout_load_error);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<NewsNode>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<NewsNode, ?> baseQuickAdapter, @NonNull View view, int i) {
//                ToastUtils.getDefaultMaker().show(baseQuickAdapter.getItem(i).getTitle());
                Bundle bundle = new Bundle();
                bundle.putString("TITLE",baseQuickAdapter.getItem(i).getTitle());
                bundle.putString("URL",baseQuickAdapter.getItem(i).getTargetUrl());
                NewsWebFragment webFragment = new NewsWebFragment();
                webFragment.setArguments(bundle);
                NavigationSceneExtensionsKt.getNavigationScene(NewsFragment.this)
                        .push(webFragment);
            }
        });

        linearLayoutManager = new LinearLayoutManager(requireSceneContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initBanner(){
        banner = findViewById(R.id.news_banner);
    }



    private void getBanner(){
        messageViewModel.getBannerListFromModel(getDataTag());
    }

    private void getList(){
        messageViewModel.getNewsListFromModel(getDataTag() + "mob");
    }

    private void observeLiveData(){
        messageViewModel.getNewsList().observe(NewsFragment.this, new Observer<List<NewsNode>>() {
            @Override
            public void onChanged(List<NewsNode> newsNodes) {
                adapter.submitList(newsNodes);
                refreshLayout.finishRefresh(true);
            }
        });
        messageViewModel.getNewsErrorLiveData().observe(NewsFragment.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                adapter.submitList(new ArrayList<>());
                refreshLayout.finishRefresh(false);
            }
        });
        messageViewModel.getBannerList().observe(NewsFragment.this, new Observer<List<BannerNode>>() {
            @Override
            public void onChanged(List<BannerNode> bannerNodes) {
//                for (int i = 0; i < 3; i++) {
//                    bannerNodes.add(new BannerNode(bannerNodes.get(0).getAd_id(),
//                            bannerNodes.get(0).getImg(),
//                            bannerNodes.get(0).getUrl(),
//                            bannerNodes.get(0).getTitle()));
//                }
                banner.setAdapter(new NewsBannerAdapter(bannerNodes),true)
                        .addBannerLifecycleObserver(getUserVisibleHintLifecycleOwner())
                        .setOnBannerListener(new OnBannerListener<BannerNode>() {
                            @Override
                            public void OnBannerClick(BannerNode data, int position) {
                                Bundle bundle = new Bundle();
                                bundle.putString("TITLE",data.getTitle());
                                bundle.putString("URL",data.getUrl().replace("https://www.iyingdi.com/tz",""));//这里要剔除前面的东西
                                NewsWebFragment webFragment = new NewsWebFragment();
                                webFragment.setArguments(bundle);
                                NavigationSceneExtensionsKt.getNavigationScene(NewsFragment.this)
                                        .push(webFragment);
                            }
                        })
                        .setIndicator(new CircleIndicator(getSceneContext()))
                        .setLoopTime(10000);
            }
        });
        messageViewModel.getBannerErrorLiveData().observe(NewsFragment.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                List<String> errorList = new ArrayList<>();
                errorList.add(s);
                banner.setAdapter(new BannerImageAdapter<String>(errorList){
                    @Override
                    public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        if (data.equals("empty"))
                            holder.imageView.setImageResource(R.drawable.img_is_empty);
                        else
                            holder.imageView.setImageResource(R.drawable.img_load_error);
                    }
                });
            }
        });
    }

}
