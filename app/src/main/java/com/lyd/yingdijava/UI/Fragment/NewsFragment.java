package com.lyd.yingdijava.UI.Fragment;

import android.util.Log;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lyd.yingdijava.UI.Base.BaseFragment;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.ViewModel.MessageViewModel;

public class NewsFragment extends BaseFragment{
    private final String TAG = "NewsFragment-" + getDataTag();

    private MessageViewModel messageViewModel;

    @Override
    protected void initViews() {
        Log.i("TAG", "NewsFragment: " + getDataTag());
        initViewModel();
        observeLiveData();
        if (getDataTag().equals("炉石")){
            getList();
        }
    }

    private void initViewModel() {
        ViewModelProvider viewModelProvider = new ViewModelProvider(this);
        messageViewModel = viewModelProvider.get(MessageViewModel.class);
    }

    private void getList(){
        messageViewModel.getNewsListFromModel("111");
    }

    private void observeLiveData(){
        messageViewModel.getErrorLiveData().observe(NewsFragment.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i(TAG, "onChanged: " + s);
            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }


}
