package com.lyd.yingdijava.UI.Fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lyd.yingdijava.Entity.Community.CommunityPostNode;
import com.lyd.yingdijava.UI.Adapter.CommunityMultiItemAdapter;
import com.lyd.yingdijava.UI.Base.BaseFragment;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Widget.SpacesItemDecoration;
import com.lyd.yingdijava.ViewModel.MessageViewModel;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends BaseFragment {
    private static final String TAG = "CommunityFragment";

    private static final String byHot = "?page=post&order=hot";
    private static final String byTime = "?page=post&order=created";

    private String mOrder = byHot.toString();

    private MessageViewModel messageViewModel;

    private SmartRefreshLayout refreshLayout;

    private RecyclerView recyclerView;
    private CommunityMultiItemAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private FloatingActionButton floatButton;


    @Override
    protected void initViews() {
        Log.i("TAG", "CommunityFragment: " + getDataTag());

        initViewModel();
        observeLiveData();
        initRecyclerView();

        refreshLayout = find(R.id.community_refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                String orderS;
                if (mOrder.equals(byHot))
                    orderS = "热度";
                else
                    orderS = "发帖时间";
                ToastUtils.getDefaultMaker().show("按[" + orderS + "]检索");
                getListByOrder();
            }
        });


        floatButton = find(R.id.community_floatingActionButton);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOrder.equals(byHot))
                    mOrder = byTime;
                else
                    mOrder = byHot;

                refreshLayout.autoRefresh();
            }
        });
    }

    private void initViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        messageViewModel = viewModelProvider.get(MessageViewModel.class);
    }

    private void getListByOrder(){
        messageViewModel.getCommunityPostListFromModel(getDataTag(),mOrder);
    }

    private void observeLiveData() {
        messageViewModel.getCommunityPostList().observe(CommunityFragment.this, new Observer<List<CommunityPostNode>>() {
            @Override
            public void onChanged(List<CommunityPostNode> communityPostNodes) {
//                for (CommunityPostNode node :
//                        communityPostNodes) {
//                    Log.i(TAG, "Type: " + node.getPostType().toString() + ",Title: " + node.getTitle());
//                }
                adapter.submitList(communityPostNodes);
                refreshLayout.finishRefresh(true);
            }
        });
        messageViewModel.getCommunityPostErrorLiveData().observe(CommunityFragment.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                adapter.submitList(new ArrayList<>());
                refreshLayout.finishRefresh(false);
            }
        });
    }

    private void initRecyclerView(){
        recyclerView = find(R.id.community_recyclerView);
        adapter = new CommunityMultiItemAdapter(new ArrayList<>());
        adapter.setEmptyViewEnable(true);
        adapter.setEmptyViewLayout(CommunityFragment.this.requireContext(),R.layout.layout_load_error);

        linearLayoutManager = new LinearLayoutManager(getContext());
        SpacesItemDecoration itemDecoration = new SpacesItemDecoration(recyclerView.getContext(),SpacesItemDecoration.VERTICAL,0,1)
                .setParam(R.color.分割线_半透明灰,3,30,30);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                if (newState == RecyclerView.SCROLL_STATE_IDLE){//空闲时加载图片，滚动时停止加载
//                    Glide.with(CommunityFragment.this.requireContext()).resumeRequests();
//                } else {
//                    Glide.with(CommunityFragment.this.requireContext()).pauseRequests();
//                }
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });
        recyclerView.setAdapter(adapter);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }
}
