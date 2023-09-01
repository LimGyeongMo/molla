package com.example.daegurobus.network.naver;

import com.example.daegurobus.network.naver.request.RequestGeocoding;
import com.example.daegurobus.network.naver.request.RequestMe;
import com.example.daegurobus.network.naver.request.RequestReverseGeocoding;
import com.example.daegurobus.network.naver.resultInterface.GeocodingInterface;
import com.example.daegurobus.network.naver.resultInterface.MeInterface;
import com.example.daegurobus.network.naver.resultInterface.ReverseGeocodingInterface;
import com.example.daegurobus.network.naver.resultInterface.StaticMapInterface;

public interface NetworkPresenterInterface {
    void getReverseGeocoding(RequestReverseGeocoding request, ReverseGeocodingInterface anInterface);

    void getGeocoding(RequestGeocoding request, GeocodingInterface anInterface);

    void getStaticMap(String center, int width, int height, int level, StaticMapInterface anInterface);

    void me(RequestMe request, MeInterface anInterface);
}
