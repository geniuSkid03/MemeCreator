package com.genius.memecreator.appAdapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyViewPagerAdapter extends FragmentPagerAdapter {


    private ArrayList<String> fragmentNames;
    private ArrayList<Fragment> fragmentArrayList;

    public MyViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        fragmentNames = new ArrayList<>();
        fragmentArrayList = new ArrayList<>();
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentNames.get(position);
    }

    public void addFragment(String fragmentName, Fragment fragment) {
        fragmentNames.add(fragmentName);
        fragmentArrayList.add(fragment);
    }
}
