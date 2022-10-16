package com.lyd.yingdijava.UI.Fragment;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.youth.banner.indicator.BaseIndicator;
import com.youth.banner.indicator.CircleIndicator;

import java.util.List;
import java.util.Objects;

public class NewsFragment extends BaseFragment{
    private final String TAG = "NewsFragment";

    private MessageViewModel messageViewModel;

    private SmartRefreshLayout refreshLayout;

    private RecyclerView recyclerView;

    private Banner banner;

    // todo 处理refresh滑动冲突，需要重写事件分发
    @Override
    protected void initViews() {
        Log.i("TAG", "NewsFragment: " + getDataTag());

        initViewModel();
        observeLiveData();

        refreshLayout = find(R.id.news_refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getList();
                getBanner();
            }
        });

        recyclerView = find(R.id.news_recyclerView);
        banner = find(R.id.news_banner);
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
                NewsRecyclerViewAdapter adapter = new NewsRecyclerViewAdapter(newsNodes);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                refreshLayout.finishRefresh(true);
            }
        });
        messageViewModel.getErrorLiveData().observe(NewsFragment.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                NewsRecyclerViewAdapter adapter = new NewsRecyclerViewAdapter();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



                recyclerView.setAdapter(adapter);
                adapter.setEmptyViewEnable(true);
                adapter.setEmptyViewLayout(NewsFragment.this.requireContext(),R.layout.layout_load_error);
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
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }


}
