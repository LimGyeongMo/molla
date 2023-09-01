package com.example.daegurobus.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CommonPagerAdapter extends FragmentPagerAdapter {
    private Fragment[] fragments;
    private String[] tabTags;

    private int currentPosition;

    public CommonPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int index) {
        return fragments[index];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTags[position];
    }

    public void setTabTags(String[] tabTags) {
        this.tabTags = tabTags;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }
}