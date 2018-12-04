package com.genius.memecreator.appFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.genius.memecreator.R;
import com.genius.memecreator.appInterfaces.FragmentRefreshser;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends SuperFragment {

    private ArrayList<String> fragmentNames;
    private ArrayList<Fragment> fragmentslist;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private TrendingMemesFragment trendingMemesFragment;
    private EditedMemesFragment editedMemesFragment;
//    private SavedMemesFragment savedMemesFragment;

    private FragmentRefreshser fragmentRefreshser;

    @Override
    protected View inflateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        return createView(layoutInflater.inflate(R.layout.fragment_home, container, false));
    }

    public View createView(View view) {

        fragmentNames = new ArrayList<>();
        fragmentslist = new ArrayList<>();

        tabLayout = view.findViewById(R.id.home_tab);
        viewPager = view.findViewById(R.id.home_view_pager);

        if(trendingMemesFragment == null) {
            trendingMemesFragment = new TrendingMemesFragment();
        }

        if(editedMemesFragment == null) {
            editedMemesFragment = new EditedMemesFragment();
        }

//        if(savedMemesFragment == null) {
//            savedMemesFragment = new SavedMemesFragment();
//        }

        if(fragmentNames.isEmpty()) {
            fragmentNames.add(getString(R.string.trending));
            fragmentNames.add(getString(R.string.edited));
//            fragmentNames.add(getString(R.string.saved));
        }

        if(fragmentslist.isEmpty()) {
            fragmentslist.add(trendingMemesFragment);
            fragmentslist.add(editedMemesFragment);
//            fragmentslist.add(savedMemesFragment);
        }

        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentslist.get(position);
            }

            @Override
            public int getCount() {
                return fragmentslist.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return fragmentNames.get(position);
            }
        };

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapterForTab();
    }

    private void setAdapterForTab() {
        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            switch (i) {
                case 0:
                    TabLayout.Tab tabOne = tabLayout.getTabAt(i);
                    Objects.requireNonNull(tabOne).setIcon(R.drawable.tab_one_icon);
                    break;
                case 1:
                    TabLayout.Tab tabTwo = tabLayout.getTabAt(i);
                    Objects.requireNonNull(tabTwo).setIcon(R.drawable.tab_three_icon);
                    break;
//                case 2:
//                    TabLayout.Tab tabThree = tabLayout.getTabAt(i);
//                    Objects.requireNonNull(tabThree).setIcon(R.drawable.tab_two_icon);
//                    break;
            }
        }
    }

    public void refreshHomeFragment() {
        if (trendingMemesFragment != null) {
            if (trendingMemesFragment.isVisible()) {
                trendingMemesFragment.refreshTrendingMemes();
            }
        }

        if(editedMemesFragment != null) {
            if(editedMemesFragment.isVisible()) {
                editedMemesFragment.refreshEditedMemes();
            }
        }

//        if (savedMemesFragment != null) {
//            if (savedMemesFragment.isVisible()) {
//                savedMemesFragment.refreshSavedMemes();
//            }
//        }
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


