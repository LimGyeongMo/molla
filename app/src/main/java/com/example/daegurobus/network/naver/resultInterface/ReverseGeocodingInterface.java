package com.example.daegurobus.network.naver.resultInterface;

import com.example.daegurobus.network.naver.model.reversegeocode.NaverAddress;

import java.util.ArrayList;

public interface ReverseGeocodingInterface {
    void success(ArrayList<NaverAddress> naverAddresses);

    void error(String message);
}
