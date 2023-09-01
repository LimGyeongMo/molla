package com.example.daegurobus.model;

public class StationDetailInfo {

    private String routeId;
    private String stationId;
    private String upDownCd;
    private String stationOrd;
    private String routeNum;
    private String routeNum2;
    private String routeTp;
    private String routeDirection;
    private String startnodeName;
    private String endnodeName;
    private String startVehicleTime;
    private String endVehicleTime;
    private String intervalTime;
    private String intervalSaturday;
    private String intervalSunday;
    private String intervalHoliday;
    private String busBookmarkYN;
    private String routeInfo;
    private String minArrivalTime;
    private int remainingTime;
    private int remainingTime1;

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

    public String getUpDownCd() {
        return upDownCd;
    }

    public void setUpDownCd(String upDownCd) {
        this.upDownCd = upDownCd;
    }

    public String getStationOrd() {
        return stationOrd;
    }

    public void setStationOrd(String stationOrd) {
        this.stationOrd = stationOrd;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getRouteNum2() {
        return routeNum2;
    }

    public void setRouteNum2(String routeNum2) {
        this.routeNum2 = routeNum2;
    }

    public String getRouteTp() {
        return routeTp;
    }

    public void setRouteTp(String routeTp) {
        this.routeTp = routeTp;
    }

    public String getRouteDirection() {
        return routeDirection;
    }

    public void setRouteDirection(String routeDirection) {
        this.routeDirection = routeDirection;
    }

    public String getStartnodeName() {
        return startnodeName;
    }

    public void setStartnodeName(String startnodeName) {
        this.startnodeName = startnodeName;
    }

    public String getEndnodeName() {
        return endnodeName;
    }

    public void setEndnodeName(String endnodeName) {
        this.endnodeName = endnodeName;
    }

    public String getStartVehicleTime() {
        return startVehicleTime;
    }

    public void setStartVehicleTime(String startVehicleTime) {
        this.startVehicleTime = startVehicleTime;
    }

    public String getEndVehicleTime() {
        return endVehicleTime;
    }

    public void setEndVehicleTime(String endVehicleTime) {
        this.endVehicleTime = endVehicleTime;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getIntervalSaturday() {
        return intervalSaturday;
    }

    public void setIntervalSaturday(String intervalSaturday) {
        this.intervalSaturday = intervalSaturday;
    }

    public String getIntervalSunday() {
        return intervalSunday;
    }

    public void setIntervalSunday(String intervalSunday) {
        this.intervalSunday = intervalSunday;
    }

    public String getIntervalHoliday() {
        return intervalHoliday;
    }

    public void setIntervalHoliday(String intervalHoliday) {
        this.intervalHoliday = intervalHoliday;
    }

    public String getBusBookmarkYN() {
        return busBookmarkYN;
    }

    public void setBusBookmarkYN(String busBookmarkYN) {
        this.busBookmarkYN = busBookmarkYN;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String getMinArrivalTime() {
        if (minArrivalTime == null)
            minArrivalTime = "";
        return minArrivalTime;
    }

    public void setMinArrivalTime(String minArrivalTime) {
        this.minArrivalTime = minArrivalTime;
    }
}
