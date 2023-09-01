package com.example.daegurobus.network.naver.model.reversegeocode;

import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("code")
    private int code;

    @SerializedName("name")
    private String name;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
