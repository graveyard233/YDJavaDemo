package com.lyd.yingdijava.UI.Fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.lyd.yingdijava.Base.BaseFragment;
import com.lyd.yingdijava.R;

import java.util.ArrayList;
import java.util.List;

public class MessageHomeFragment extends BaseFragment {

    private ViewPager2 viewPager;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void initViews() {
        viewPager = find(R.id.home_viewPager);
        fragments.add(new NewsFragment());
        fragments.add(new CommunityFragment());

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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_home;
    }
}
