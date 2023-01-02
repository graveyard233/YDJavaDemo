package com.lyd.yingdijava.UI.Activity;

import androidx.annotation.NonNull;


import com.bytedance.scene.Scene;
import com.bytedance.scene.ui.SceneActivity;
import com.lyd.yingdijava.UI.Fragment.CardSearchFragment;


public class MainActivity extends SceneActivity {

    @NonNull
    @Override
    protected Class<? extends Scene> getHomeSceneClass() {
        return MainScene.class;// TODO: 2023/1/2 现在是测试页面，做好之后要改回来 CardSearchFragment.class
    }

    @Override
    protected boolean supportRestore() {
        return false;
    }
}