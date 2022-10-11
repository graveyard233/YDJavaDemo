package com.lyd.yingdijava.UI.Fragment;

import android.util.Log;

import com.lyd.yingdijava.UI.Base.BaseFragment;
import com.lyd.yingdijava.R;

public class NewsFragment extends BaseFragment{

    @Override
    protected void initViews() {
        Log.i("TAG", "NewsFragment: " + getDataTag());


    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
    }


}
