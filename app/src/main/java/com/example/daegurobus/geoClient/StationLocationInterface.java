package com.example.daegurobus.geoClient;

import java.util.ArrayList;

public interface StationLocationInterface {

    void success(ArrayList<ArrayList<Double>> routeAll, ArrayList<ArrayList<Double>> Downward, ArrayList<ArrayList<Double>> uphill);

}
