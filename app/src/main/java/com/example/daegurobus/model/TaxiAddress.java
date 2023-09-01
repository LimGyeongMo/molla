package com.example.daegurobus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TaxiAddress implements Parcelable {
    @SerializedName("areaCode")
    private String areaCode;

    @SerializedName("areaName")
    private String areaName;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("placeName")
    private String placeName;

    @SerializedName("jibunAddress")
    private String jibunAddress;

    @SerializedName("roadAddress")
    private String roadAddress;

    @SerializedName("detailAddress")
    private String detailAddress;

    @SerializedName("distance")
    private String distance; // 거리(meter 단위)

    @SerializedName("pnuCode")
    private String pnuCode;

    @SerializedName("isSelected")
    private boolean isSelected;

    public TaxiAddress() {

    }

    protected TaxiAddress(Parcel in) {
        areaCode = in.readString();
        areaName = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        placeName = in.readString();
        jibunAddress = in.readString();
        roadAddress = in.readString();
        detailAddress = in.readString();
        distance = in.readString();
        pnuCode = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<TaxiAddress> CREATOR = new Creator<TaxiAddress>() {
        @Override
        public TaxiAddress createFromParcel(Parcel in) {
            return new TaxiAddress(in);
        }

        @Override
        public TaxiAddress[] newArray(int size) {
            return new TaxiAddress[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(areaCode);
        dest.writeString(areaName);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(placeName);
        dest.writeString(jibunAddress);
        dest.writeString(roadAddress);
        dest.writeString(detailAddress);
        dest.writeString(distance);
        dest.writeString(pnuCode);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    public String getAreaCode() {
        if (areaCode == null) {
            areaCode = "";
        }

        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        if (areaName == null) {
            areaName = "";
        }

        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLatitude() {
        if (latitude == null) {
            latitude = "";
        }

        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        if (longitude == null) {
            longitude = "";
        }

        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getJibunAddress() {
        if (jibunAddress == null) {
            jibunAddress = "";
        }

        return jibunAddress;
    }

    public void setJibunAddress(String jibunAddress) {
        this.jibunAddress = jibunAddress;
    }

    public String getRoadAddress() {
        if (roadAddress == null) {
            roadAddress = "";
        }

        return roadAddress;
    }

    public void setRoadAddress(String roadAddress) {
        this.roadAddress = roadAddress;
    }

    public String getDetailAddress() {
        if (detailAddress == null) {
            detailAddress = "";
        }

        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
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

    public String getPnuCode() {
        if (pnuCode == null) {
            pnuCode = "";
        }

        return pnuCode;
    }

    public void setPnuCode(String pnuCode) {
        this.pnuCode = pnuCode;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}