package com.lyd.yingdijava.UI.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.drakeet.drawer.FullDraggableContainer;

//解决在Android12上的一个bug
public class IgnoreFitsSystemWindowsFullyDraggableDrawerContentLayout extends FullDraggableContainer {
    public IgnoreFitsSystemWindowsFullyDraggableDrawerContentLayout(@NonNull Context context) {
        super(context);
    }

    public IgnoreFitsSystemWindowsFullyDraggableDrawerContentLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IgnoreFitsSystemWindowsFullyDraggableDrawerContentLayout(@NonNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        return insets;
    }
}