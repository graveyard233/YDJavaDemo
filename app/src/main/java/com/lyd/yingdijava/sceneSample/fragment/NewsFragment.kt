package com.lyd.yingdijava.sceneSample.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import androidx.viewpager2.widget.ViewPager2
import com.bytedance.scene.group.GroupScene
import com.bytedance.scene.group.UserVisibleHintGroupScene
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lyd.yingdijava.R
import com.lyd.yingdijava.sceneSample.adapter.SceneAdapter

class NewsFragment : GroupScene(){

    private lateinit var mViewPage : ViewPager2
    private val titleList : Array<String> = arrayOf("a","b","c")

    companion object{
        fun newInstance(index:Int): NewsFragment {
            return NewsFragment().apply {
                val bundle = Bundle()
                bundle.putInt("index",index)
                setArguments(bundle)
            }
        }
    }

    override fun onCreateView(p0: LayoutInflater, p1: ViewGroup, p2: Bundle?): ViewGroup {
        return p0.inflate(R.layout.fragment_news_scene,p1,false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewPage = view.findViewById(R.id.news_vp)
        mViewPage.adapter = MyCollectionAdapter(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val tableLayout = requireViewById<TabLayout>(R.id.news_tabLayout)
//        tableLayout.setTabTextColors(R.color.tab_normal,R.color.tab_select)
        tableLayout.setTabTextColors(Color.parseColor("#c79100"),Color.parseColor("#fff350"))
        TabLayoutMediator(tableLayout,mViewPage){tab,position ->
            tab.text = titleList[position]
        }.attach()
    }
}

class MyCollectionAdapter(groupScene: GroupScene) : SceneAdapter(groupScene) {
    override fun getItemCount(): Int = 3

    override fun createScene(position: Int): UserVisibleHintGroupScene {
        val scene = DemoObjectScene()
        scene.setArguments(Bundle().apply {
            putInt(ARG_OBJECT, position)
        })
        return scene
    }
}

private const val ARG_OBJECT = "object"

class DemoObjectScene : UserVisibleHintGroupScene() {
    private var value: Int = 0
    private val mId:Int by lazy { View.generateViewId() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedInstanceState: Bundle?
    ): ViewGroup {
        val frame =  FrameLayout(requireSceneContext())
        frame.id = mId
//        val bottom = BottomFragment.newInstance(1)
//        frame.addView(bottom as ViewGroup,FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
        //        frameLayout.id = mId
//        return frame
        return frame
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        value = arguments?.getInt(ARG_OBJECT)!!
        add(mId, BottomFragment.newInstance(1),"TAG")
//        val textView: TextView = view.findViewById(android.R.id.text1)
//        textView.text = value.toString()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        Log.d("DemoObjectScene $value", "setUserVisibleHint: $isVisibleToUser")
    }
}
