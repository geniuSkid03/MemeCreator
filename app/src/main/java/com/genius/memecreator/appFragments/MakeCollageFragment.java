package com.genius.memecreator.appFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.genius.memecreator.R;
import com.genius.memecreator.appAdapters.ImgCollageAdapter;

public class MakeCollageFragment extends SuperFragment {

    private GridView collageGv;

    private ImgCollageAdapter imgCollageAdapter;
    private Integer[] gridItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gridItems = new Integer[]{R.drawable.sample_1, R.drawable.sample_2};
        imgCollageAdapter = new ImgCollageAdapter(getContext(), gridItems, new ImgCollageAdapter.OnGridItemClicked() {
            @Override
            public void itemClicked(int position) {
                showToast(getContext(), "Clicked: "+position);
                openGridWith(gridItems[position]);
            }
        });
    }

    private void openGridWith(Integer gridItem) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        collageGv.setAdapter(imgCollageAdapter);
    }

    @Override
    protected View inflateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(layoutInflater.inflate(R.layout.fragment_make_collage, container, false));
    }

    private View initView(View view) {

        collageGv = view.findViewById(R.id.collage_grid_views);

        return view;
    }
}
