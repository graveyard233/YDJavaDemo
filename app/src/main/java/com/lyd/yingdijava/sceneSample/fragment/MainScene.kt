package com.lyd.yingdijava.sceneSample.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bytedance.scene.Scene
import com.bytedance.scene.ktx.requireNavigationScene
import com.bytedance.scene.navigation.NavigationScene
import com.bytedance.scene.navigation.NavigationSceneOptions
import com.bytedance.scene.navigation.OnBackPressedListener
import com.lyd.yingdijava.R
import com.lyd.yingdijava.sceneSample.adapter.MySceneInstanceUtility
import java.util.LinkedHashMap
import java.util.concurrent.TimeUnit

class MainScene : TryNavScene()/*NavigationViewScene()*/{
    override fun getLeftMenuResId(): Int {
        return R.menu.main_navigation
    }

    override fun getRightMenuResId(): Int {
        return R.menu.bottom_navigation
    }

//    override fun getMenuResId(): Int {
//        return R.menu.main_navigation
//    }

    private fun getBundle(index: Int): Bundle {
        val bundle = Bundle()
        bundle.putInt("index", index)
        return bundle
    }

    override fun getSceneMap(): LinkedHashMap<Int, Scene> {
        val map  = LinkedHashMap<Int, Scene>()
//        map[R.id.news] = NewsFragment.newInstance(1)
//        map[R.id.shoucang] = NewsFragment.newInstance(2)
//        map[R.id.setting] = NewsFragment.newInstance(3)
        val bundle = Bundle()
        bundle.putInt("index",0)
        var mNavigationScene = MySceneInstanceUtility.getInstanceFromClass(
            NavigationScene::class.java,
            NavigationSceneOptions(NewsFragment::class.java,getBundle(0)).toBundle()
        ) as NavigationScene
        map[R.id.news] = mNavigationScene
        mNavigationScene = MySceneInstanceUtility.getInstanceFromClass(
            NavigationScene::class.java,
            NavigationSceneOptions(NewsFragment::class.java,getBundle(1)).toBundle()
        ) as NavigationScene
        map[R.id.shoucang] = mNavigationScene
        mNavigationScene = MySceneInstanceUtility.getInstanceFromClass(
            NavigationScene::class.java,
            NavigationSceneOptions(NewsFragment::class.java,getBundle(2)).toBundle()
        ) as NavigationScene
        map[R.id.setting] = mNavigationScene
//        map[R.id.news] = NewsFragment.newInstance(1)
//        map[R.id.shoucang] = NewsFragment.newInstance(2)
//        map[R.id.setting] = NewsFragment.newInstance(3)
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
        requireNavigationScene().addOnBackPressedListener(this,object : OnBackPressedListener {
            internal var time: Long = 0

            override fun onBackPressed(): Boolean {
                if (time == 0L || TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - time) > 2) {
                    Toast.makeText(activity,"再按一次就退出~", Toast.LENGTH_SHORT).show()
                    time = System.currentTimeMillis()
                    return true
                }
                return false
            }
        })
    }


}