package com.genius.memecreator.appViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class CustomGrids extends GridView {


    public CustomGrids(Context context) {
        super(context);
    }

    public CustomGrids(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomGrids(Context context, AttributeSet attributeSet, int style){
        super(context, attributeSet, style);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, wrapHeight);
    }
}
