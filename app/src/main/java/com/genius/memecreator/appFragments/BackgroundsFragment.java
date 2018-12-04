package com.genius.memecreator.appFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.genius.memecreator.R;
import com.genius.memecreator.appInterfaces.FragmentRefreshser;

public class BackgroundsFragment extends SuperFragment {

    private FragmentRefreshser fragmentRefreshser;

    private GridView backgroundsGv;
    private TextView noBgTv;

    @Override
    protected View inflateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(layoutInflater.inflate(R.layout.fragment_backgrounds, container, false));
    }

    private View initView(View view) {

        return view;
    }

    public void refreshBackgrounds() {

    }

    public void setFragmentRefreshser(FragmentRefreshser fragmentRefreshser) {
        this.fragmentRefreshser = fragmentRefreshser;
    }

    public FragmentRefreshser getFragmentRefreshser() {
        return fragmentRefreshser;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
            if(getFragmentRefreshser() != null) {
                getFragmentRefreshser().onRefresh();
            }
        }
    }
}
