package com.example.daegurobus.model;

public class BusResultSearch {



    private String routeCode;
    private String routeId;
    private String route;
    private String routeNum;
    private String routeNum2;
    private String routeTp;
    private String startNodeName;
    private String endNodeName;
    private String BookMark_yn;

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

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getRouteNum2() {
        if (routeNum2 == null){
            routeNum2 = "";
        }
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

    public String getStartNodeName() {
        return startNodeName;
    }

    public void setStartNodeName(String startNodeName) {
        this.startNodeName = startNodeName;
    }

    public String getEndNodeName() {
        return endNodeName;
    }

    public void setEndNodeName(String endNodeName) {
        this.endNodeName = endNodeName;
    }

    public String getBookMark_yn() {
        return BookMark_yn;
    }

    public void setBookMark_yn(String bookMark_yn) {
        BookMark_yn = bookMark_yn;
    }
}
