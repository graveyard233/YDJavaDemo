package com.lyd.yingdijava.UI.Fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lyd.yingdijava.Entity.Community.CommunityPostNode;
import com.lyd.yingdijava.UI.Adapter.CommunityMultiItemAdapter;
import com.lyd.yingdijava.UI.Base.BaseFragment;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.ViewModel.MessageViewModel;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

public class CommunityFragment extends BaseFragment {
    private static final String TAG = "CommunityFragment";

    private MessageViewModel messageViewModel;

    private SmartRefreshLayout refreshLayout;

    private RecyclerView recyclerView;

    private FloatingActionButton floatButton;


    @Override
    protected void initViews() {
        Log.i("TAG", "CommunityFragment: " + getDataTag());

        initViewModel();
        observeLiveData();

        recyclerView = find(R.id.community_recyclerView);
        refreshLayout = find(R.id.community_refresh_layout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getListByHot();
            }
        });
    }

    private void initViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        messageViewModel = viewModelProvider.get(MessageViewModel.class);
    }

    private void getListByHot(){
        messageViewModel.getCommunityPostListByHotFromModel(getDataTag());
    }

    private void observeLiveData() {
        messageViewModel.getCommunityPostList().observe(CommunityFragment.this, new Observer<List<CommunityPostNode>>() {
            @Override
            public void onChanged(List<CommunityPostNode> communityPostNodes) {
                for (CommunityPostNode node :
                        communityPostNodes) {
                    Log.i(TAG, "Type: " + node.getPostType().toString() + ",Title: " + node.getTitle());
                }
                CommunityMultiItemAdapter adapter = new CommunityMultiItemAdapter(communityPostNodes);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
                refreshLayout.finishRefresh(true);
            }
        });
        messageViewModel.getCommunityPostErrorLiveData().observe(CommunityFragment.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });
    }



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }
}
