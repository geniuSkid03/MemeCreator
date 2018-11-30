package com.genius.memecreator.appAdapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedHashMap;

public class MyPagerAdapter extends FragmentPagerAdapter {

    private LinkedHashMap<String, Fragment> fragmentLinkedHashMap;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

        fragmentLinkedHashMap = new LinkedHashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentLinkedHashMap.get(getPageTitle(position));
    }

    public void addFragment(String fragmentName, Fragment fragment) {
        fragmentLinkedHashMap.put(fragmentName, fragment);
    }

    @Override
    public int getCount() {
        return fragmentLinkedHashMap.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return (CharSequence) fragmentLinkedHashMap.keySet().toArray()[position];
    }
}
