package com.lyd.yingdijava.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.lyd.yingdijava.Base.BaseActivity;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Fragment.MessageHomeFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

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

        tabLayout.setViewPager(viewPager,titles,this,fragments);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // TODO: 2022/10/10 有点bug 要确保切换时能够换回正确的底部导航栏图标
                MessageHomeFragment fragment = (MessageHomeFragment) fragments.get(viewPager.getCurrentItem());
                fragment.changeFragment();
                return true;
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}