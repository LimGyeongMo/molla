package com.example.daegurobus.network.resultInterface;

import com.example.daegurobus.model.StationNearByInfo;

import java.util.ArrayList;

public interface StationSurroundInfo {

    void success(ArrayList<StationNearByInfo> items);

    void error(String message);
}
