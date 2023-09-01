package com.example.daegurobus.network.resultInterface;

import com.example.daegurobus.model.BusDetailInfo;
import com.example.daegurobus.model.BusDetailInfo1;
import com.example.daegurobus.model.BusDetailTop;

import java.util.ArrayList;

public interface BusDetailInterface {
    void success (BusDetailTop busDetailTop, ArrayList<BusDetailInfo> busDetailInfo, ArrayList<BusDetailInfo1> busDetailInfo1);
}
