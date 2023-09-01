package com.example.daegurobus.model;

import java.io.Serializable;

public class BusDetailInfo implements Serializable {

    private String routeId;
    private String stationId;
    private String stationName;
    private String stationNo;
    private String updownCd;
    private String stationOrd;
    private String stationDirection;
    private String stationLon;
    private String stationLat;
    private String routeDate;
    private String startvehicleTime;
    private String endvehicleTime;
    private String subwayNum;
    private String trafficLevel;
    private String bookmarkYn;
    private String stationWifiYn;
    public String BusNum;

    public String RouteTp;
    public  int distance;

    public  String BusVisbleYn;

    public int selectedPosition;


    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

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

    public String getUpdownCd() {
        return updownCd;
    }

    public void setUpdownCd(String updownCd) {
        this.updownCd = updownCd;
    }

    public String getStationOrd() {
        return stationOrd;
    }

    public void setStationOrd(String stationOrd) {
        this.stationOrd = stationOrd;
    }

    public String getStationDirection() {
        return stationDirection;
    }

    public void setStationDirection(String stationDirection) {
        this.stationDirection = stationDirection;
    }

    public String getStationLon() {
        return stationLon;
    }

    public void setStationLon(String stationLon) {
        this.stationLon = stationLon;
    }

    public String getStationLat() {
        return stationLat;
    }

    public void setStationLat(String stationLat) {
        this.stationLat = stationLat;
    }

    public String getRouteDate() {
        return routeDate;
    }

    public void setRouteDate(String routeDate) {
        this.routeDate = routeDate;
    }

    public String getStartvehicleTime() {
        return startvehicleTime;
    }

    public void setStartvehicleTime(String startvehicleTime) {
        this.startvehicleTime = startvehicleTime;
    }

    public String getEndvehicleTime() {
        return endvehicleTime;
    }

    public void setEndvehicleTime(String endvehicleTime) {
        this.endvehicleTime = endvehicleTime;
    }

    public String getSubwayNum() {
        return subwayNum;
    }

    public void setSubwayNum(String subwayNum) {
        this.subwayNum = subwayNum;
    }

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    public String getBookmarkYn() {
        return bookmarkYn;
    }

    public void setBookmarkYn(String bookmarkYn) {
        this.bookmarkYn = bookmarkYn;
    }

    public String getStationWifiYn() {
        return stationWifiYn;
    }

    public void setStationWifiYn(String busWifiYn) {
        this.stationWifiYn = busWifiYn;
    }

    public String getBusNum() {
        return BusNum;
    }

    public void setBusNum(String busNum) {
        BusNum = busNum;
    }

    public double distanceTo(double lon, double lat) {

        if (lon > 0 && lat > 00 && stationLon.length() > 1 && stationLat.length() > 1) {
            return Math.sqrt(Math.pow(lon - Double.parseDouble(stationLon), 2) + Math.pow(lat - Double.parseDouble(stationLat), 2));
        }
        else
        {
            return Double.MAX_VALUE;
        }
    }
}
