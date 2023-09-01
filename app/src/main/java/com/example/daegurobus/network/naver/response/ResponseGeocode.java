package com.example.daegurobus.network.naver.response;

import com.google.gson.annotations.SerializedName;
import com.example.daegurobus.network.naver.model.geocode.Meta;
import com.example.daegurobus.network.naver.model.geocode.NaverGeoAddress;

import java.util.ArrayList;

public class ResponseGeocode {
    @SerializedName("status")
    private String status;

    @SerializedName("meta")
    private Meta meta;

    @SerializedName("addresses")
    private ArrayList<NaverGeoAddress> addresses;

    @SerializedName("errorMessage")
    private String errorMessage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public ArrayList<NaverGeoAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<NaverGeoAddress> addresses) {
        this.addresses = addresses;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
