package com.example.daegurobus.network.naver.response;

import com.google.gson.annotations.SerializedName;
import com.example.daegurobus.network.naver.model.reversegeocode.NaverAddress;
import com.example.daegurobus.network.naver.model.reversegeocode.Status;

import java.util.ArrayList;

public class ResponseReverseGeocode {
    @SerializedName("status")
    private Status status;

    @SerializedName("results")
    private ArrayList<NaverAddress> results;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<NaverAddress> getResults() {
        return results;
    }

    public void setResults(ArrayList<NaverAddress> results) {
        this.results = results;
    }
}
