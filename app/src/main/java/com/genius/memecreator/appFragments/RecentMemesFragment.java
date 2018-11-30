package com.genius.memecreator.appFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genius.memecreator.R;

public class RecentMemesFragment extends SuperFragment {

    @Override
    protected View inflateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(layoutInflater.inflate(R.layout.fragment_recent_memes, container, false));
    }

    private View initView(View view) {

        return view;
    }
}
