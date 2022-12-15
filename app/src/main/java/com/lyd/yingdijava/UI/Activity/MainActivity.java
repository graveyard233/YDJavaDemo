package com.lyd.yingdijava.UI.Activity;

import androidx.annotation.NonNull;


import com.bytedance.scene.Scene;
import com.bytedance.scene.ui.SceneActivity;



public class MainActivity extends SceneActivity {

    @NonNull
    @Override
    protected Class<? extends Scene> getHomeSceneClass() {
        return MainScene.class;
    }

    @Override
    protected boolean supportRestore() {
        return false;
    }
}