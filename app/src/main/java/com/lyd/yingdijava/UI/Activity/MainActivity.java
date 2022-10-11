package com.lyd.yingdijava.UI.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.lyd.yingdijava.UI.Base.BaseActivity;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Fragment.MessageHomeFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener,ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private static final String[] titles = new String[] {"炉石","玩家杂谈"};
    private ViewPager viewPager;
    private SlidingTabLayout tabLayout;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void initViews() {
        bottomNavigation = findViewById(R.id.main_bottomNavigation);
        viewPager = findViewById(R.id.main_viewPager);
        tabLayout = findViewById(R.id.main_tabLayout);

        fragments.add(new MessageHomeFragment());
        fragments.add(new MessageHomeFragment());

        for (int i = 0; i < fragments.size(); i++) {
            //告诉每个fragment分别是什么区块
            MessageHomeFragment mFragment = (MessageHomeFragment) fragments.get(i);
            mFragment.setDataTag(titles[i]);
        }

        tabLayout.setViewPager(viewPager,titles,this,fragments);
        bottomNavigation.setOnItemSelectedListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        MessageHomeFragment fragment = (MessageHomeFragment) fragments.get(viewPager.getCurrentItem());
        fragment.changeFragment();
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
        return R.layout.activity_main;
    }
}