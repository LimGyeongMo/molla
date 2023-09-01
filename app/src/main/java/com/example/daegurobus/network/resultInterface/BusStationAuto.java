package com.example.daegurobus.network.resultInterface;

import com.example.daegurobus.model.stationResult;

import java.util.ArrayList;

public interface BusStationAuto {

    void success(ArrayList<stationResult> stationList);

    void error(String msg);
}
