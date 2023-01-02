package com.lyd.yingdijava.UI.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ColorUtils;
import com.bytedance.scene.Scene;
import com.bytedance.scene.group.UserVisibleHintGroupScene;
import com.bytedance.scene.ktx.NavigationSceneExtensionsKt;

public class ColorFragment extends UserVisibleHintGroupScene {
    private Integer mId;

    @NonNull
    @Override
    public ViewGroup onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
        TextView textView = new TextView(requireSceneContext());
        mId = View.generateViewId();
        textView.setText(String.valueOf(mId));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationSceneExtensionsKt.getNavigationScene(ColorFragment.this)
                        .push(new EmptyScene());
            }
        });
        FrameLayout frameLayout = new FrameLayout(requireSceneContext());
        frameLayout.setId(mId);
        frameLayout.setBackgroundColor(ColorUtils.getRandomColor());
        frameLayout.addView(textView);
        return frameLayout;
    }

    private final class EmptyScene extends Scene{

        @NonNull
        @Override
        public View onCreateView(@NonNull LayoutInflater layoutInflater, @NonNull ViewGroup viewGroup, @Nullable Bundle bundle) {
            View view = new View(getActivity());

            view.setBackgroundColor(ColorUtils.getRandomColor());
            return view;
        }
    }
}
