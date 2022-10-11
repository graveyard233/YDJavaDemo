package com.lyd.yingdijava.UI.Fragment;

import android.util.Log;

import com.lyd.yingdijava.UI.Base.BaseFragment;
import com.lyd.yingdijava.R;

public class CommunityFragment extends BaseFragment {
    @Override
    protected void initViews() {
        Log.i("TAG", "CommunityFragment: " + getDataTag());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_community;
    }
}
