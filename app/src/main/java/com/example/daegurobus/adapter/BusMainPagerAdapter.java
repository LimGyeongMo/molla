package com.example.daegurobus.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.daegurobus.fragment.BusEventFragment;
import com.example.daegurobus.fragment.BusHomeFragment;
import com.example.daegurobus.fragment.BusInquiryFragment;
import com.example.daegurobus.fragment.BusNoticelistFragment;

public class BusMainPagerAdapter extends FragmentPagerAdapter {
    public static final int TAB_HOME = 0;
    public static final int TAB_ANNOUNCEMENT = 1;
    public static final int TAB_EVENT = 2;
    public static final int TAB_INQUIRY = 3;
    public static final int FRAGMENT_COUNT = 4;

    private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
    private String[] tabTags;

    private int currentPosition;

    public BusMainPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments[TAB_HOME] = new BusHomeFragment();
        fragments[TAB_ANNOUNCEMENT] = new BusNoticelistFragment();
        fragments[TAB_EVENT] = new BusEventFragment();
        fragments[TAB_INQUIRY] = new BusInquiryFragment();
    }

    @Override
    public Fragment getItem(int index) {
        return fragments[index];
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
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
