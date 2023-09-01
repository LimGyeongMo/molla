package com.example.daegurobus.network.resultInterface;

import com.example.daegurobus.model.StationDetailInfo;
import com.example.daegurobus.model.StationDetailInfoSub;
import com.example.daegurobus.model.StationDetailTop;

import java.util.ArrayList;

public interface BusStationDetailInterface {
    void success(StationDetailTop stationDetailTop, ArrayList<StationDetailInfo> stationDetailInfo, ArrayList<StationDetailInfoSub> stationDetailInfoSub);
}
