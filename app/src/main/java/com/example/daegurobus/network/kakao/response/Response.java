package com.example.daegurobus.network.kakao.response;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("errorType")
    private String errorType;

    @SerializedName("message")
    private String message;

    public String getErrorType() {
        if (errorType == null) {
            errorType = "";
        }

        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        if (message == null) {
            message = "";
        }

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}