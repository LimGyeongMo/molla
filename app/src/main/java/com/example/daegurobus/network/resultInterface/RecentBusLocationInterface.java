package com.example.daegurobus.network.resultInterface;

import com.example.daegurobus.model.BusDetailInfo1;

import java.util.ArrayList;

public interface RecentBusLocationInterface {

    void success (ArrayList<BusDetailInfo1> items);

    void error(String msg);
}
