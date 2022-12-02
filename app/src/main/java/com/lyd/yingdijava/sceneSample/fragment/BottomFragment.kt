package com.lyd.yingdijava.sceneSample.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bytedance.scene.Scene
import com.bytedance.scene.ui.template.AppCompatScene
import com.bytedance.scene.ui.template.BottomNavigationViewScene
import com.lyd.yingdijava.R
import java.util.LinkedHashMap

class BottomFragment : BottomNavigationViewScene(){

    companion object{
        fun newInstance(index: Int): BottomFragment {
            return BottomFragment().apply {
                val bundle = Bundle()
                bundle.putInt("index",index)
                setArguments(bundle)
            }
        }
    }

    override fun getMenuResId(): Int {
        return R.menu.bottom_navigation
    }

    override fun getSceneMap(): LinkedHashMap<Int, Scene> {
        val linkedHashMap  = LinkedHashMap<Int, Scene>()
        linkedHashMap.put(R.id.bottom_news, TabChildScene.newInstance(0))
        linkedHashMap.put(R.id.bottom_community, TabChildScene.newInstance(1))
        return linkedHashMap
    }


}


class TabChildScene : AppCompatScene() {
    companion object {
        fun newInstance(index: Int): TabChildScene {
            return TabChildScene().apply {
                val bundle = Bundle()
                bundle.putInt("index", index)
                setArguments(bundle)
            }
        }
    }

    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
        return TextView(requireSceneContext()).apply {
            val index = arguments!!["index"] as Int
//            setBackgroundColor(ColorUtil.getMaterialColor(resources, index))
            gravity = Gravity.CENTER
            text = "Child Scene #$index"
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar?.visibility = View.GONE
    }
}