package com.lyd.yingdijava.sceneSample.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bytedance.scene.Scene;
import com.bytedance.scene.group.GroupScene;
import com.bytedance.scene.navigation.NavigationSceneGetter;
import com.bytedance.scene.navigation.OnBackPressedListener;
import com.bytedance.scene.ui.GroupSceneUIUtility;
import com.google.android.material.navigation.NavigationView;
import com.lyd.yingdijava.R;

import java.util.LinkedHashMap;

public abstract class TryNavScene extends GroupScene {
    private Toolbar mToolbar;
    private NavigationView mNaviLeft;
    private NavigationView mNaviRight;
    private DrawerLayout mDrawerLayout;

    @NonNull
    @Override
    public ViewGroup onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        return (ViewGroup) layoutInflater.inflate(R.layout.try_nav_layout,viewGroup,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mDrawerLayout = findViewById(R.id.try_nav_drawerLayout);
        this.mToolbar = findViewById(R.id.try_nav_toolbar);
        this.mNaviLeft = findViewById(R.id.try_nav_navigation_left);
        this.mNaviRight = findViewById(R.id.try_nav_navigation_right);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(),mDrawerLayout,mToolbar,0,0);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        this.mNaviLeft.inflateMenu(getLeftMenuResId());
        this.mNaviRight.inflateMenu(getRightMenuResId());
        GroupSceneUIUtility.setupWithNavigationView(this.mDrawerLayout,this.mNaviLeft,this,R.id.try_nav_scene_container,getSceneMap(),
                new NavigationView.OnNavigationItemSelectedListener(){
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        mToolbar.setTitle(item.getTitle());
                        return false;
                    }
                });
        this.mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        NavigationSceneGetter.requireNavigationScene(this).addOnBackPressedListener(this, new OnBackPressedListener() {
            @Override
            public boolean onBackPressed() {
                if (mDrawerLayout.isDrawerOpen(mNaviLeft)) {
                    mDrawerLayout.closeDrawer(mNaviLeft);
                    return true;
                }
                return false;
            }
        });
    }

    @MenuRes
    protected abstract int getLeftMenuResId();

    @MenuRes
    protected abstract int getRightMenuResId();

    @NonNull
    protected abstract LinkedHashMap<Integer, Scene> getSceneMap();
}
