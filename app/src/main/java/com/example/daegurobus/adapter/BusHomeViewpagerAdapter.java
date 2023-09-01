package com.example.daegurobus.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.daegurobus.databinding.BusScheduleListBinding;
import com.example.daegurobus.fragment.BusHomeFragment;
import com.example.daegurobus.fragment.Fragment_2;


public class BusHomeViewpagerAdapter extends FragmentStateAdapter {

    public int mCount;
    public BusScheduleListBinding binding;

    public BusHomeViewpagerAdapter(BusHomeFragment fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);

        if(index==0) return new Fragment_2();
        else if(index==1) return new Fragment_2();
        else if(index==2) return new Fragment_2();
        else return new Fragment_2();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public int getRealPosition(int position) { return position % mCount; }

}