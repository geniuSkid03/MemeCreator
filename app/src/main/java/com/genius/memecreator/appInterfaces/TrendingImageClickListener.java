package com.genius.memecreator.appInterfaces;

import android.widget.ImageView;

public interface TrendingImageClickListener {
    void onShareClicked(ImageView trendingIv, int position);
    void onEditClicked(int position);
}
