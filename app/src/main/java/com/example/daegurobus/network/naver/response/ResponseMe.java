package com.example.daegurobus.network.naver.response;

import com.google.gson.annotations.SerializedName;
import com.example.daegurobus.network.naver.model.me.NaverAccount;

public class ResponseMe {
    public static final String SUCCESS_CODE = "00";

    @SerializedName("resultcode")
    private String resultCode;

    @SerializedName("message")
    private String message;

    @SerializedName("response")
    private NaverAccount naverAccount;

    public String getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public NaverAccount getNaverAccount() {
        return naverAccount;
    }
}
