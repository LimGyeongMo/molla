package com.example.daegurobus.network.naver.model.reversegeocode;

import com.google.gson.annotations.SerializedName;

public class NaverAddress {
    @SerializedName("name")
    private String name;

    @SerializedName("region")
    private Region region;

    @SerializedName("land")
    private Land land;

    public String getName() {
        return name;
    }

    public Region getRegion() {
        if (region == null) {
            region = new Region();
        }
        return region;
    }

    public Land getLand() {
        if (land == null) {
            land = new Land();
        }

        return land;
    }
}
