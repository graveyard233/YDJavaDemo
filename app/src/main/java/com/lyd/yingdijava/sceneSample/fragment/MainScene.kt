package com.lyd.yingdijava.sceneSample.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.bytedance.scene.Scene
import com.bytedance.scene.ui.template.NavigationViewScene
import com.lyd.yingdijava.R
import java.util.LinkedHashMap

class MainScene : NavigationViewScene(){
    override fun getMenuResId(): Int {
        return R.menu.main_navigation
    }

    override fun getSceneMap(): LinkedHashMap<Int, Scene> {
        val map  = LinkedHashMap<Int, Scene>()
        map[R.id.news] = NewsFragment.newInstance(1)
        map[R.id.shoucang] = NewsFragment.newInstance(2)
        map[R.id.setting] = NewsFragment.newInstance(3)
        return map
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val window = requireActivity().window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}