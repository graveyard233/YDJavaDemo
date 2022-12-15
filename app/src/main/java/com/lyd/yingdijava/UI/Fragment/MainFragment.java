package com.lyd.yingdijava.UI.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bytedance.scene.Scene;
import com.bytedance.scene.group.GroupScene;
import com.bytedance.scene.group.UserVisibleHintGroupScene;
import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Adapter.SceneAdapter;
import com.lyd.yingdijava.UI.Base.BaseFragment;
import com.lyd.yingdijava.UI.Base.MainNavScene;
import com.lyd.yingdijava.UI.Widget.HorizontalInterceptionViewPager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainFragment extends GroupScene {
    private ViewPager2 mViewPage;
    private static final String[] titleList = new String[] {"炉石","万智牌","玩家杂谈"};
    private BottomNavigationView bottomNav;
    private static final String ARG_OBJECT = "object";


    @NonNull
    @Override
    public ViewGroup onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        return (ViewGroup) layoutInflater.inflate(R.layout.fragment_main_,viewGroup,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPage = view.findViewById(R.id.fragment_main__vp);
        MyCollectionAdapter mAdapter = new MyCollectionAdapter(this);
        mViewPage.setAdapter(mAdapter);
        mViewPage.setOffscreenPageLimit(1);
        bottomNav = view.findViewById(R.id.fragment_main__bottom);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                MsgHomeFragment msgHomeFragment = (MsgHomeFragment) mAdapter.getmScenes()
                        .get(mAdapter.getItemId(mViewPage.getCurrentItem()));
                switch (item.getItemId()){
                    case R.id.bottom_news:
                        msgHomeFragment.choiceLeft();
                        break;
                    case R.id.bottom_community:
                        msgHomeFragment.choiceRight();
                        break;
                    default:break;
                }
                return true;
            }
        });
        mViewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                UserVisibleHintGroupScene msgHomeFragment = mAdapter.getmScenes()
                        .get(mAdapter.getItemId(mViewPage.getCurrentItem()));
                if (msgHomeFragment != null){
                    MsgHomeFragment asMsg = (MsgHomeFragment) msgHomeFragment;

                    bottomNav.getMenu().getItem(asMsg.getCurrentFragment()).setChecked(true);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TabLayout tabLayout = requireViewById(R.id.fragment_main__tabLayout);
        tabLayout.setTabTextColors(Color.parseColor("#c79100"),Color.parseColor("#fff350"));
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, mViewPage, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titleList[position]);
            }
        });
        mediator.attach();
    }

    private class MyCollectionAdapter extends SceneAdapter {

        public MyCollectionAdapter(@NonNull GroupScene groupScene) {
            super(groupScene);
        }

        @NonNull
        @Override
        public UserVisibleHintGroupScene createScene(int position) {
            MsgHomeFragment msgHomeFragment = new MsgHomeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_OBJECT,position);
            msgHomeFragment.setArguments(bundle);
            return msgHomeFragment;
        }

        @Override
        public int getItemCount() {
            return titleList.length;
        }
    }
}

//public class MainFragment extends BaseFragment implements NavigationBarView.OnItemSelectedListener, ViewPager.OnPageChangeListener {
//
//    private ArrayList<Fragment> fragments = new ArrayList<>();
//    private static final String[] titles = new String[] {"炉石","万智牌","玩家杂谈"};
//    private HorizontalInterceptionViewPager viewPager;
//    private SlidingTabLayout tabLayout;
//
//    private BottomNavigationView bottomNavigation;
//
//    @Override
//    protected void initViews() {
//        bottomNavigation = find(R.id.fragment_main_bottomNavigation);
//        viewPager = find(R.id.fragment_main_viewPager);
//        tabLayout = find(R.id.fragment_main_tabLayout);
//
////        fragments.clear();
//        if (fragments.size() == 0){
//            for (int i = 0; i < titles.length; i++) {
//                fragments.add(new MessageHomeFragment());
//            }
//        }
//
//
//        for (int i = 0; i < fragments.size(); i++) {
//            //告诉每个fragment分别是什么区块
//            MessageHomeFragment mFragment = (MessageHomeFragment) fragments.get(i);
//            mFragment.setDataTag(titles[i]);
//        }
//
//        tabLayout.setViewPager(viewPager,titles,getActivity(),fragments);
//        bottomNavigation.setOnItemSelectedListener(this);
//        viewPager.addOnPageChangeListener(this);
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        MessageHomeFragment fragment = (MessageHomeFragment) fragments.get(viewPager.getCurrentItem());
//        switch (item.getItemId()){
//            case R.id.action_News:
//                fragment.chooseNews();
//                break;
//            case R.id.action_Community:
//                fragment.chooseCommunity();
//                break;
//            default:break;
//        }
//        return true;
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        MessageHomeFragment fragment = (MessageHomeFragment) fragments.get(viewPager.getCurrentItem());
//        bottomNavigation.getMenu().getItem(fragment.getCurrentFragment()).setChecked(true);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_main;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.i("LYD", "onDestroy: MainFragment");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.i("LYD", "onPause: MainFragment");
//        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//        for (int i = 0; i < fragments.size(); i++) {
//            fragmentTransaction.remove(fragments.get(i));
//        }
//        fragmentTransaction.commitAllowingStateLoss();
//    }
//}
