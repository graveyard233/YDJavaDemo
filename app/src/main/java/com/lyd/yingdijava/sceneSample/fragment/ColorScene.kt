package com.lyd.yingdijava.sceneSample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.bytedance.scene.Scene
import com.bytedance.scene.group.UserVisibleHintGroupScene
import com.lyd.yingdijava.R

class ColorScene : UserVisibleHintGroupScene() {
    private val mId:Int by lazy { View.generateViewId() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): ViewGroup {
        val textView = TextView(requireSceneContext()).apply {
            this.text = mId.toString()
        }
        val frame =  FrameLayout(requireSceneContext()).apply {
            this.id = mId
            this.setBackgroundColor(resources.getColor(R.color.blue_grey_300))
            this.addView(textView)
        }
        return frame
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}

class MyEmptyScene : Scene() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        val view = View(activity)
        view.setBackgroundColor(resources.getColor(R.color.indigo_300))

//        arguments?.getInt("color",2)
//            ?.let { ColorUtil.getMaterialColor(resources, it) }?.let { view.setBackgroundColor(it) }
        return view
    }
}