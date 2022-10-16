package com.lyd.yingdijava.UI.Widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;

public class HorizontalInterceptionViewPager extends ViewPager {


    public HorizontalInterceptionViewPager(@NonNull Context context) {
        super(context);
    }

    public HorizontalInterceptionViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    private int mLastY;
    private int mLastX;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 外部拦截法
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (mLastY < getHeight() / 5  ){//如果落点是在banner里面就不用拦截
                    break;
                }
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {//水平滑动，拦截
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
