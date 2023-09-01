package com.example.daegurobus.network.resultInterface;

import com.example.daegurobus.model.stationResult;

import java.util.ArrayList;

public interface BusStationResultInterface {

    void success(ArrayList<stationResult> busStationResult);

    void error(String message);
}
