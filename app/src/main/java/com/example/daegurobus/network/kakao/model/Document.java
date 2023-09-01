package com.example.daegurobus.network.kakao.model;

import com.google.gson.annotations.SerializedName;

public class Document {
    @SerializedName("address_name")
    private String addressName;

    @SerializedName("category_group_code")
    private String categoryGroupCode;

    @SerializedName("category_group_name")
    private String categoryGroupName;

    @SerializedName("distance")
    private String distance;

    @SerializedName("id")
    private String id;

    @SerializedName("phone")
    private String phone;

    @SerializedName("place_name")
    private String placeName;

    @SerializedName("place_url")
    private String placeUrl;

    @SerializedName("road_address_name")
    private String roadAddressName;

    @SerializedName("x")
    private String x;

    @SerializedName("y")
    private String y;

    public String getAddressName() {
        if (addressName == null) {
            addressName = "";
        }

        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getCategoryGroupCode() {
        if (categoryGroupCode == null) {
            categoryGroupCode = "";
        }

        return categoryGroupCode;
    }

    public void setCategoryGroupCode(String categoryGroupCode) {
        this.categoryGroupCode = categoryGroupCode;
    }

    public String getCategoryGroupName() {
        if (categoryGroupName == null) {
            categoryGroupName = "";
        }

        return categoryGroupName;
    }

    public void setCategoryGroupName(String categoryGroupName) {
        this.categoryGroupName = categoryGroupName;
    }

    public String getDistance() {
        if (distance == null) {
            distance = "";
        }

        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getId() {
        if (id == null) {
            id = "";
        }

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        if (phone == null) {
            phone = "";
        }

        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlaceName() {
        if (placeName == null) {
            placeName = "";
        }

        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceUrl() {
        if (placeUrl == null) {
            placeUrl = "";
        }

        return placeUrl;
    }

    public void setPlaceUrl(String placeUrl) {
        this.placeUrl = placeUrl;
    }

    public String getRoadAddressName() {
        if (roadAddressName == null) {
            roadAddressName = "";
        }

        return roadAddressName;
    }

    public void setRoadAddressName(String roadAddressName) {
        this.roadAddressName = roadAddressName;
    }

    public String getX() {
        if (x == null) {
            x = "";
        }

        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        if (y == null) {
            y = "";
        }

        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
}
