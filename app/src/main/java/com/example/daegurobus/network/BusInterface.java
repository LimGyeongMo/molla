package com.example.daegurobus.network;

import com.example.daegurobus.model.BusResultSearch;

import java.util.ArrayList;

public interface BusInterface {
    void sucsess(ArrayList<BusResultSearch> atList);

    void error(String message);
}
