package com.genius.memecreator.appViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.genius.memecreator.R;

public class EditingOptionsView extends RelativeLayout {


    public EditingOptionsView(Context context) {
        super(context);
        initViews(context);
    }

    public EditingOptionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public EditingOptionsView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.editing_menu_items, this, true);
        setClipToPadding(false);
    }
}
