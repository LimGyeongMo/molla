package com.example.daegurobus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.daegurobus.R;
import com.example.daegurobus.databinding.BusFilterListBinding;

public class BusFliterFragment extends Fragment {

    private BusFilterListBinding binding;

    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedIns) {
        binding = DataBindingUtil.inflate(inflater, R.layout.bus_filter_list, container, false);


        View view = binding.getRoot();
        return view;

    }
}
