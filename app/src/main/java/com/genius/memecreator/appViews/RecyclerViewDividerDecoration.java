package com.genius.memecreator.appViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/*
 *
 * This class is used to insert divider in between recycler items
 * and divider at the end was removed
 *
 */

public class RecyclerViewDividerDecoration extends DividerItemDecoration {

    private Drawable mDivider;
    private final Rect mBounds = new Rect();

    public RecyclerViewDividerDecoration(Context context, int dividerAlignment, Drawable drawable) {
        super(context, dividerAlignment);
        mDivider = drawable;
        setDrawable(mDivider);
        setOrientation(dividerAlignment);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i <= childCount - 2; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            outRect.setEmpty();
        } else
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
    }

    //    @Override
//    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
//        canvas.save();
//        final int left;
//        final int right;
//        if (parent.getClipToPadding()) {
//            left = parent.getPaddingLeft();
//            right = parent.getWidth() - parent.getPaddingRight();
//            canvas.clipRect(left, parent.getPaddingTop(), right,
//                    parent.getHeight() - parent.getPaddingBottom());
//        } else {
//            left = 0;
//            right = parent.getWidth();
//        }
//
//        final int childCount = parent.getChildCount();
//        for (int i = 0; i < childCount - 1; i++) {
//            final View child = parent.getChildAt(i);
//            parent.getDecoratedBoundsWithMargins(child, mBounds);
//            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
//            final int top = bottom - mDivider.getIntrinsicHeight();
//            mDivider.setBounds(left, top, right, bottom);
//            mDivider.draw(canvas);
//        }
//        canvas.restore();
//    }
//
//    @Override
//    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//
//        if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
//            outRect.setEmpty();
//        } else
//            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
//    }
}

//
//    public RecyclerViewDividerDecoration(Drawable drawable) {
//        mDivider = drawable;
//    }
//
