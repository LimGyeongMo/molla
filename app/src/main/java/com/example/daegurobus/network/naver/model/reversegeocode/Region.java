package com.example.daegurobus.network.naver.model.reversegeocode;

import com.google.gson.annotations.SerializedName;

public class Region {
    @SerializedName("area0")
    private Area area0;

    // 시/도
    @SerializedName("area1")
    private Area area1;

    // 시/군/구
    @SerializedName("area2")
    private Area area2;

    // 읍/면/동
    @SerializedName("area3")
    private Area area3;

    // 리
    @SerializedName("area4")
    private Area area4;

    public Area getArea0() {
        if (area0 == null) {
            area0 = new Area();
        }

        return area0;
    }

    public Area getArea1() {
        if (area1 == null) {
            area1 = new Area();
        }

        return area1;
    }

    public Area getArea2() {
        if (area2 == null) {
            area2 = new Area();
        }

        return area2;
    }

    public Area getArea3() {
        if (area3 == null) {
            area3 = new Area();
        }

        return area3;
    }

    public Area getArea4() {
        if (area4 == null) {
            area4 = new Area();
        }

        return area4;
    }
}
