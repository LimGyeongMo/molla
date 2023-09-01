package com.example.daegurobus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.daegurobus.R;

public class Fragment_2 extends Fragment {


    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bus_schedule_list, container, false);

        setInit(view);

        return view;
    }

    private void setInit(View _view) {

        LinearLayout linearLayout = _view.findViewById(R.id.schedule_alarm);
    }
}
