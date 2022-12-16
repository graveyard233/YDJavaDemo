package com.lyd.yingdijava.UI.Activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;

import com.bytedance.scene.Scene;
import com.bytedance.scene.ktx.NavigationSceneExtensionsKt;
import com.bytedance.scene.navigation.NavigationScene;
import com.bytedance.scene.navigation.NavigationSceneOptions;
import com.bytedance.scene.navigation.OnBackPressedListener;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Adapter.MySceneInstanceUtility;
import com.lyd.yingdijava.UI.Base.MainNavScene;
import com.lyd.yingdijava.UI.Fragment.ColorFragment;
import com.lyd.yingdijava.UI.Fragment.MainFragment;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

public class MainScene extends MainNavScene {

    @Override
    protected int getLeftMenuResId() {
        return R.menu.main_navigation;
    }

    @Override
    protected int getRightMenuResId() {
        return R.menu.bottom_navigation;
    }

    private Bundle getBundle(int index){
        Bundle bundle = new Bundle();
        bundle.putInt("index",index);
        return bundle;
    }

    @NonNull
    @Override
    protected LinkedHashMap<Integer, Scene> getSceneMap() {
        final LinkedHashMap<Integer, Scene> map = new LinkedHashMap<Integer,Scene>();
        Bundle bundle = new Bundle();
        bundle.putInt("index",0);
        NavigationScene mNavigationScene = (NavigationScene) MySceneInstanceUtility.getInstanceFromClass(
                NavigationScene.class,
                new NavigationSceneOptions(MainFragment.class,getBundle(0)).toBundle()
        );
        map.put(R.id.news,mNavigationScene);
        mNavigationScene = (NavigationScene) MySceneInstanceUtility.getInstanceFromClass(
                NavigationScene.class,
                new NavigationSceneOptions(ColorFragment.class,getBundle(1)).toBundle()
        );
        map.put(R.id.shoucang,mNavigationScene);
        mNavigationScene = (NavigationScene) MySceneInstanceUtility.getInstanceFromClass(
                NavigationScene.class,
                new NavigationSceneOptions(ColorFragment.class,getBundle(2)).toBundle()
        );
        map.put(R.id.setting,mNavigationScene);
        return map;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = requireActivity().getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        NavigationSceneExtensionsKt.requireNavigationScene(this)
                .addOnBackPressedListener((LifecycleOwner) this, new OnBackPressedListener() {
                    private long time;
                    @Override
                    public boolean onBackPressed() {
                        if (this.time != 0L && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.time) <= (long)2) {
                            return false;
                        } else {
                            Toast.makeText(MainScene.this.getActivity(), "再按一次就退出~", Toast.LENGTH_SHORT).show();
                            this.time = System.currentTimeMillis();
                            return true;
                        }
                    }
                });
    }
}
