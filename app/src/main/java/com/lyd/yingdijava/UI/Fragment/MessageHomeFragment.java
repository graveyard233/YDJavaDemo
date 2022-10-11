package com.lyd.yingdijava.UI.Fragment;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.lyd.yingdijava.UI.Base.BaseActivity;
import com.lyd.yingdijava.UI.Base.BaseFragment;
import com.lyd.yingdijava.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageHomeFragment extends BaseFragment {

    private ViewPager2 viewPager;

    private List<Fragment> fragments = new ArrayList<>();

    private NewsFragment newsFragment;

    private CommunityFragment communityFragment;

    @Override
    protected void initViews() {
        Log.i("TAG", "initMessageFragment: " + getDataTag());
        viewPager = find(R.id.home_viewPager);

        initChildrenFragment();

        viewPager.setAdapter(new FragmentStateAdapter(getParentFragmentManager(),getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });

        viewPager.setUserInputEnabled(false);
    }

    public void changeFragment(){
        viewPager.setCurrentItem(viewPager.getCurrentItem() == 0 ? 1 : 0);
    }

    public int getCurrentFragment(){
        return viewPager.getCurrentItem();
    }

    private void initChildrenFragment(){
        newsFragment = new NewsFragment();
        newsFragment.setDataTag(getDataTag());
        communityFragment = new CommunityFragment();
        communityFragment.setDataTag(getDataTag());
        fragments.add(newsFragment);
        fragments.add(communityFragment);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_home;
    }
}
