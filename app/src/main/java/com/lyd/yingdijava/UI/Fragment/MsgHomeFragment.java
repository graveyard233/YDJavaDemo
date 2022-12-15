package com.lyd.yingdijava.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.bytedance.scene.group.GroupScene;
import com.bytedance.scene.group.UserVisibleHintGroupScene;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lyd.yingdijava.R;
import com.lyd.yingdijava.UI.Adapter.SceneAdapter;

import java.util.ArrayList;

public class MsgHomeFragment extends UserVisibleHintGroupScene {

    private ViewPager2 mViewPage;

    @NonNull
    @Override
    public ViewGroup onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        return (ViewGroup) layoutInflater.inflate(R.layout.fragment_msg_home,viewGroup,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPage = view.findViewById(R.id.fragment_msg_home_vp2);
        mViewPage.setAdapter(new MyDoubleAdapter(this));
    }

    public void choiceLeft(){
        mViewPage.setCurrentItem(0);
    }

    public void choiceRight(){
        mViewPage.setCurrentItem(1);
    }

    public int getCurrentFragment(){
        return mViewPage.getCurrentItem();
    }

    private final class MyDoubleAdapter extends SceneAdapter{

        public MyDoubleAdapter(@NonNull GroupScene groupScene) {
            super(groupScene);
        }

        @NonNull
        @Override
        public UserVisibleHintGroupScene createScene(int position) {
            ColorFragment colorFragment = new ColorFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("object",position);
            colorFragment.setArguments(bundle);
            return colorFragment;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}