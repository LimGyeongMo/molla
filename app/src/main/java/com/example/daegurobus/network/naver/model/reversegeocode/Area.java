package com.example.daegurobus.network.naver.model.reversegeocode;

import com.google.gson.annotations.SerializedName;

public class Area {
    @SerializedName("name")
    private String name;

    @SerializedName("alias")
    private String alias;

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}
