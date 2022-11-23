package com.lyd.yingdijava.UI.Fragment;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Base.BaseFragment;
import com.lyd.yingdijava.UI.Widget.HorizontalInterceptionViewPager;

import java.util.ArrayList;

public class MainFragment extends BaseFragment implements NavigationBarView.OnItemSelectedListener, ViewPager.OnPageChangeListener {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static final String[] titles = new String[] {"炉石","万智牌","玩家杂谈"};
    private HorizontalInterceptionViewPager viewPager;
    private SlidingTabLayout tabLayout;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void initViews() {
        bottomNavigation = find(R.id.fragment_main_bottomNavigation);
        viewPager = find(R.id.fragment_main_viewPager);
        tabLayout = find(R.id.fragment_main_tabLayout);

//        fragments.clear();
        if (fragments.size() == 0){
            for (int i = 0; i < titles.length; i++) {
                fragments.add(new MessageHomeFragment());
            }
        }


        for (int i = 0; i < fragments.size(); i++) {
            //告诉每个fragment分别是什么区块
            MessageHomeFragment mFragment = (MessageHomeFragment) fragments.get(i);
            mFragment.setDataTag(titles[i]);
        }

        tabLayout.setViewPager(viewPager,titles,getActivity(),fragments);
        bottomNavigation.setOnItemSelectedListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MessageHomeFragment fragment = (MessageHomeFragment) fragments.get(viewPager.getCurrentItem());
        switch (item.getItemId()){
            case R.id.action_News:
                fragment.chooseNews();
                break;
            case R.id.action_Community:
                fragment.chooseCommunity();
                break;
            default:break;
        }
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        MessageHomeFragment fragment = (MessageHomeFragment) fragments.get(viewPager.getCurrentItem());
        bottomNavigation.getMenu().getItem(fragment.getCurrentFragment()).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LYD", "onDestroy: MainFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("LYD", "onPause: MainFragment");
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragments.size(); i++) {
            fragmentTransaction.remove(fragments.get(i));
        }
        fragmentTransaction.commitAllowingStateLoss();
    }
}
