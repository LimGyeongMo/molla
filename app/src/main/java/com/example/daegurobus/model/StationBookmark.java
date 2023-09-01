package com.example.daegurobus.model;

import java.util.ArrayList;

public class StationBookmark {

    private String stationId;
    private String stationName;
    private String stationNo;
    private String stationDirection;
    private String subwayNum;

    public ArrayList<RouteStationBookmark> routeStationBookmarks;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getStationDirection() {
        return stationDirection;
    }

    public void setStationDirection(String stationDirection) {
        this.stationDirection = stationDirection;
    }

    public String getSubwayNum() {
        if (subwayNum ==null){
         subwayNum = "";
        }return subwayNum;
    }

    public void setSubwayNum(String subwayNum) {
        this.subwayNum = subwayNum;
    }

    public ArrayList<RouteStationBookmark> getRouteStationBookmarks() {
        return routeStationBookmarks;
    }

    public void setRouteStationBookmarks(ArrayList<RouteStationBookmark> routeStationBookmarks) {
        this.routeStationBookmarks = routeStationBookmarks;
    }
}
