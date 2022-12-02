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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lyd.yingdijava.R
import com.lyd.yingdijava.sceneSample.adapter.SceneAdapter

class NewsFragment : GroupScene(){

    private lateinit var mViewPage : ViewPager2
    private val titleList : Array<String> = arrayOf("a","b","c")
    private lateinit var bottomNav : BottomNavigationView

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
        val mAdapter = MyCollectionAdapter(this)
        mViewPage.adapter = mAdapter

        bottomNav = view.findViewById(R.id.news_bottom)
        bottomNav.setOnItemSelectedListener (){
            val mSmall = mAdapter.getmScenes().get(mAdapter.getItemId(mViewPage.currentItem)) as MySmallScene
            when(it.itemId){
                R.id.bottom_news -> mSmall.choiceLeft()
                R.id.bottom_community -> mSmall.choiceRight()
            }
            true
        }

        mViewPage.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                val mSmall = mAdapter.getmScenes().get(mAdapter.getItemId(mViewPage.currentItem))
                mSmall?.let { it as MySmallScene }?.getCurrentFragment()
                    ?.let { bottomNav.menu.getItem(it).isChecked = true}
            }
        })

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
        return MySmallScene().apply {
            setArguments(Bundle().apply { putInt(ARG_OBJECT, position) })
        }
    }
}

private const val ARG_OBJECT = "object"
