package com.example.daegurobus.model;

import com.google.gson.annotations.SerializedName;

public class BusSearchApi {

    @SerializedName("seq")
    private int seq;

    @SerializedName("resultCode")
    private String resultCode;

    @SerializedName("resultMessage")
    private String resultMessage;

    @SerializedName("resultData")
    private String resultData;

    @SerializedName("rowCount")
    private int rowCount;

    @SerializedName("dataType")
    private int dataType;

    @SerializedName("columnName")
    private String columnName;

    @SerializedName("columnType")
    private String columnType;



    @Override
    public String toString() {
        return "BusSearchApi{" +
                "seq=" + seq +
                ",resultCode=" + resultCode +  '\'' +
                ",resultMessage=" + resultMessage +  '\'' +
                ",resultData=" + resultData +  '\'' +
                ",rowCount=" + rowCount +
                ",dataType=" + dataType +
                ",columnName=" + columnName +  '\'' +
                ",columnType" + columnType +  '\'' +
                '}';
    }
}
