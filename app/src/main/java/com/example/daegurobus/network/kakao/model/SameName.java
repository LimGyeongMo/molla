package com.example.daegurobus.network.kakao.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SameName {
    @SerializedName("keyword")
    private String keyword;

    @SerializedName("region")
    private ArrayList<String> region;

    @SerializedName("selected_region")
    private String selectedRegion;

    public String getKeyword() {
        if (keyword == null) {
            keyword = "";
        }

        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ArrayList<String> getRegion() {
        if (region == null) {
            region = new ArrayList<>();
        }

        return region;
    }

    public void setRegion(ArrayList<String> region) {
        this.region = region;
    }

    public String getSelectedRegion() {
        if (selectedRegion == null) {
            selectedRegion = "";
        }

        return selectedRegion;
    }

    public void setSelectedRegion(String selectedRegion) {
        this.selectedRegion = selectedRegion;
    }
}
