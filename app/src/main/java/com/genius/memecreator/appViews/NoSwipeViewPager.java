package com.genius.memecreator.appViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class NoSwipeViewPager extends ViewPager {

    private String name = "mScroller";
    private Context context;

    public NoSwipeViewPager(Context context) {
        super(context);
        this.context = context;
        setUpScroller();
    }

    public NoSwipeViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setUpScroller();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    private void setUpScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField(name);
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {

        MyScroller(Context context) {
            super(context);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350);
        }
    }
}
