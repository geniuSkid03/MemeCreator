package com.genius.memecreator.appViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.genius.memecreator.R;

public class ImageHolderView extends RelativeLayout {

    public ImageHolderView(Context context) {
        super(context);
        init(context);
    }

    public ImageHolderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImageHolderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.trending_image_item_view, this, true);
        setClipToPadding(false);
    }
}
