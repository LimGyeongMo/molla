package com.example.daegurobus.model;

import com.google.gson.annotations.SerializedName;

public class StationApi {

    @SerializedName("seq")
    private int seq;

    @SerializedName("resultCode")
    private String resultCode;

    @SerializedName("resultMessage")
    private String resultMessage;
    @SerializedName("resultData")
    private String resultData;

    @SerializedName("rowCount")
    private String rowCount;

    @SerializedName("dataType")
    private String dataType;

    @SerializedName("columnName")
    private String columnName;

    @SerializedName("columnType")
    private String columnType;


    private String routeCode;
    private String routeId;

    public String getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(String routeNum) {
        this.routeNum = routeNum;
    }

    public String getStartNodeName() {
        return startNodeName;
    }

    public void setStartNodeName(String startNodeName) {
        this.startNodeName = startNodeName;
    }

    private String routeNum;
    private String routeNum2;
    private String startNodeName;
    private String endNodeName;

    @Override
    public String toString() {
        return "StationApi{" +
                "seq=" + seq +
                ", resultCode='" + resultCode + '\'' +
                ", resultMessage='" + resultMessage + '\'' +
                ", resultData='" + resultData + '\'' +
                ", rowCount='" + rowCount + '\'' +
                ", dataType='" + dataType + '\'' +
                ", columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                '}';
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}