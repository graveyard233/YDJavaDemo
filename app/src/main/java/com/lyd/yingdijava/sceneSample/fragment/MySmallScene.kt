package com.lyd.yingdijava.sceneSample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.bytedance.scene.group.GroupScene
import com.bytedance.scene.group.UserVisibleHintGroupScene
import com.lyd.yingdijava.R
import com.lyd.yingdijava.sceneSample.adapter.SceneAdapter


class MySmallScene : UserVisibleHintGroupScene() {
    private lateinit var mViewPage : ViewPager2
    override fun onCreateView(p0: LayoutInflater, p1: ViewGroup, p2: Bundle?): ViewGroup {
        return p0.inflate(R.layout.fragment_small_scene,p1,false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewPage = view.findViewById(R.id.small_scene_vp2)
        mViewPage.adapter = MyDoubleAdapter(this)
    }

    fun choiceLeft():Unit{
        mViewPage.currentItem = 0
    }

    fun choiceRight(){
        mViewPage.currentItem = 1
    }

    fun getCurrentFragment() :Int{
        return mViewPage.currentItem
    }
}

class MyDoubleAdapter(groupScene: GroupScene) : SceneAdapter(groupScene){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createScene(position: Int): UserVisibleHintGroupScene {
        val scene = ColorScene()
        scene.setArguments(Bundle().apply { putInt("object",position) })
        return scene
//        return ColorScene().apply { setArguments(Bundle().apply { putInt("object",position) }) }
    }


}

