package com.lyd.yingdijava.sceneSample.activity

import com.bytedance.scene.Scene
import com.bytedance.scene.ui.SceneActivity
import com.lyd.yingdijava.sceneSample.fragment.MainScene

class MainActivity : SceneActivity() {
    override fun getHomeSceneClass(): Class<out Scene> {
        return MainScene::class.java
    }

    override fun supportRestore(): Boolean {
        return false
    }
}

//class MainScene : AppCompatScene() {
//    private lateinit var mButton: Button
//    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
//        val frameLayout = FrameLayout(requireSceneContext())
//        mButton = Button(requireSceneContext())
//        mButton.text = "Click"
//        frameLayout.addView(mButton, FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
//        return frameLayout
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        setTitle("Main")
//        toolbar?.navigationIcon = null
//        mButton.setOnClickListener {
//            navigationScene?.push(SecondScene())
//        }
//    }
//}
//
//class SecondScene : AppCompatScene() {
//    private val mId: Int by lazy { View.generateViewId() }
//
//    override fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View? {
//        val frameLayout = FrameLayout(requireSceneContext())
//        frameLayout.id = mId
//        return frameLayout
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        setTitle("Second")
//        add(mId, ChildScene(), "TAG")
//    }
//}
//
//class ChildScene : Scene() {
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle?): View {
//        val view = View(requireSceneContext())
//        view.setBackgroundColor(Color.GREEN)
//        return view
//    }
//}