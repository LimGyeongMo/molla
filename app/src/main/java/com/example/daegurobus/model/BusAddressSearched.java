package com.example.daegurobus.model;

import com.google.gson.annotations.SerializedName;

public class BusAddressSearched {
    @SerializedName("placeName")
    private String placeName;

    @SerializedName("newAddress")
    private String newAddress;

    @SerializedName("oldAddress")
    private String oldAddress;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("direction")
    private String direction;

    public String getPlaceName() {
        if (placeName == null) {
            placeName = "";
        }

        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getNewAddress() {
        if (newAddress == null) {
            newAddress = "";
        }

        return newAddress;
    }

    public void setNewAddress(String newAddress) {
        this.newAddress = newAddress;
    }

    public String getOldAddress() {
        if (oldAddress == null) {
            oldAddress = "";
        }

        return oldAddress;
    }

    public void setOldAddress(String oldAddress) {
        this.oldAddress = oldAddress;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
