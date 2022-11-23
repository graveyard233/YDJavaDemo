package com.lyd.yingdijava.UI.Fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lyd.yingdijava.Entity.Banner.BannerNode;
import com.lyd.yingdijava.Entity.News.NewsNode;
import com.lyd.yingdijava.UI.Adapter.NewsBannerAdapter;
import com.lyd.yingdijava.UI.Adapter.NewsRecyclerViewAdapter;
import com.lyd.yingdijava.UI.Base.BaseFragment;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.ViewModel.MessageViewModel;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment{
    private final String TAG = "NewsFragment";

    private MessageViewModel messageViewModel;

    private SmartRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private Banner banner;

    // todo 处理refresh滑动冲突，需要重写事件分发
    @Override
    protected void initViews() {
        Log.i("TAG", "NewsFragment: " + getDataTag());

        initViewModel();
        observeLiveData();

        initRecyclerView();
        initBanner();
        refreshLayout = find(R.id.news_refresh_layout);
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
                        .addBannerLifecycleObserver(getViewLifecycleOwner())
                        .setIndicator(new CircleIndicator(getContext()))
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

    private void initRecyclerView() {
        recyclerView = find(R.id.news_recyclerView);

        adapter = new NewsRecyclerViewAdapter();
        adapter.setEmptyViewEnable(true);
        adapter.setEmptyViewLayout(NewsFragment.this.requireContext(),R.layout.layout_load_error);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<NewsNode>() {
            @Override
            public void onClick(@NonNull BaseQuickAdapter<NewsNode, ?> baseQuickAdapter, @NonNull View view, int i) {
                ToastUtils.getDefaultMaker().show(baseQuickAdapter.getItem(i).getTitle());
                nav().navigate(R.id.newsWebFragment);
            }
        });

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void initBanner(){
        banner = find(R.id.news_banner);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }


}
