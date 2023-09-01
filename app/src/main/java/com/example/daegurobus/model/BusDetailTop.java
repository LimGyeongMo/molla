package com.example.daegurobus.model;


import java.io.Serializable;

public class BusDetailTop implements Serializable {
    public String routeCode;
    public String routeId;
    public String routeNum;
    public String routeNum2;
    public String routeTp;
    public String routeTp1;
    public String routeDirection;
    public String startnodeName;
    public String endnodeName;
    public String startVihicleTime;
    public String endVihicleTime;
    public String intervalTime;
    public String intervalSaturday;
    public String intervalSunday;
    public String intervalHoliday;
    public String bookmarkYn;



    public String getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(String routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
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

    public String getRouteTp1() {
        return routeTp1;
    }
    public void setRouteTp1(String routeTp1) {
        this.routeTp1 = routeTp1;
    }

    public String getRouteDirection() {
        return routeDirection;
    }

    public void setRouteDirection(String routeDirection) {
        this.routeDirection = routeDirection;
    }
    public String getStartVihicleTime() {
        return startVihicleTime;
    }

    public void setStartVihicleTime(String startVihicleTime) {
        this.startVihicleTime = startVihicleTime;
    }

    public String getEndVihicleTime() {
        return endVihicleTime;
    }

    public void setEndVihicleTime(String endVihicleTime) {
        this.endVihicleTime = endVihicleTime;
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

    public String getBookmarkYn() {
        return bookmarkYn;
    }

    public void setBookmarkYn(String bookmarkYn) {
        this.bookmarkYn = bookmarkYn;
    }
}
