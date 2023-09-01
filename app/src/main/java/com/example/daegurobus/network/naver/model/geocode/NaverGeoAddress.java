package com.example.daegurobus.network.naver.model.geocode;

import com.google.gson.annotations.SerializedName;

public class NaverGeoAddress {
    @SerializedName("roadAddress")
    private String roadAddress;

    @SerializedName("jibunAddress")
    private String jibunAddress;

    @SerializedName("x")
    private String x;

    @SerializedName("y")
    private String y;

    public String getRoadAddress() {
        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getJibunAddress() {
        return jibunAddress;
    }

    public void setJibunAddress(String jibunAddress) {
        this.jibunAddress = jibunAddress;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}
