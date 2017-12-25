package com.jianyuyouhun.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * viewPager封装
 * Created by WangYu on 2016/10/22.
 */
public class NestScrollViewPager extends ViewPager {

    private boolean isScrollAble = true;//是否可滑动

    public NestScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestScrollViewPager(Context context) {
        this(context, null);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollAble) {
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (isScrollAble) {
            super.scrollTo(x, y);
        } else {
            super.scrollTo(0, 0);
        }
    }

    public boolean isScrollAble() {
        return isScrollAble;
    }

    public void setScrollAble(boolean scrollAble) {
        isScrollAble = scrollAble;
    }
}
