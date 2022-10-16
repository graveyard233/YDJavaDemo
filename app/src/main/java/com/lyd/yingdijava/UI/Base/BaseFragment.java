package com.lyd.yingdijava.UI.Base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    protected View contentView;

    protected String dataTag;

    protected abstract void initViews();

    protected abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayoutId(),container,false);
        initViews();
        return contentView;
    }

    protected <T extends View> T find(@IdRes int id){
        return contentView.findViewById(id);
    }

    public void setDataTag(String dataTag) {
        this.dataTag = dataTag;
    }

    public String getDataTag() {
        return dataTag == null ? "null" : dataTag;
    }
}