package com.genius.memecreator.appViews;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BottomPaddingDecoration extends RecyclerView.ItemDecoration {
    private final int bottomPadding;

    public BottomPaddingDecoration(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, 0, 0, bottomPadding);
        }
    }
}
