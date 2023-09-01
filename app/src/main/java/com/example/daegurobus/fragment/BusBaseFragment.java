package com.example.daegurobus.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.daegurobus.network.NetworkPresenter;
import com.example.daegurobus.constant.LaunchIntent;

public class BusBaseFragment extends BaseFragment{

    protected NetworkPresenter busNetworkPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projectType = LaunchIntent.PROJECT_BUS;
        busNetworkPresenter = new NetworkPresenter(getContext());
    }
}
