package com.genius.memecreator.appHelpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {

    public OnItemClickListener onItemClickListener;
    public GestureDetector gestureDetector;

    public RecyclerViewItemClickListener(Context context, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View view = rv.findChildViewUnder(e.getX(), e.getY());
        if (view != null && onItemClickListener != null && gestureDetector.onTouchEvent(e)) {
            onItemClickListener.OnItemClick(view, rv.getChildPosition(view));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }
}
