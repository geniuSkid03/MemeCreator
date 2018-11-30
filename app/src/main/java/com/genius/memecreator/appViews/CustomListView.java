package com.genius.memecreator.appViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustomListView extends ListView {

    /*This list view will wrap up the content and set its height according to it*/

    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CustomListView(Context context, AttributeSet attributeSet, int style){
        super(context, attributeSet, style);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wrapHeight = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, wrapHeight);
    }
}
