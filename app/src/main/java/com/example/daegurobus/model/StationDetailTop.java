package com.example.daegurobus.model;

import java.io.Serializable;

public class StationDetailTop implements Serializable {

    private String stationCode;
    private String stationId;
    private String stationName;
    private String stationNo;
    private String stationDirection;
    private String keyword;
    private String subwayNum;
    private String stationLon;
    private String stationLat;
    private String stationWifiYn;
    private String bookmarkYN;
    private String comingSoon;

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
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

    public String getStationDirection() {
        return stationDirection;
    }

    public void setStationDirection(String stationDirection) {
        this.stationDirection = stationDirection;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSubwayNum() {
        return subwayNum;
    }

    public void setSubwayNum(String subwayNum) {
        this.subwayNum = subwayNum;
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

    public String getStationWifiYn() {
        return stationWifiYn;
    }

    public void setStationWifiYn(String stationWifiYn) {
        this.stationWifiYn = stationWifiYn;
    }

    public String getBookmarkYN() {
        return bookmarkYN;
    }

    public void setBookmarkYN(String bookmarkYN) {
        this.bookmarkYN = bookmarkYN;
    }

    public String getComingSoon() {
        if (comingSoon == null) {
            comingSoon = "";
        }return comingSoon;
    }

    public void setComingSoon(String comingSoon) {
        this.comingSoon = comingSoon;
    }
}
