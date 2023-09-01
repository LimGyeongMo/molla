package com.example.daegurobus.network.naver.resultInterface;

import com.example.daegurobus.network.naver.model.geocode.NaverGeoAddress;

import java.util.ArrayList;

public interface GeocodingInterface {
    void success(ArrayList<NaverGeoAddress> naverAddresses);

    void error(String message);
}
