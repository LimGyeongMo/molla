package com.example.daegurobus.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CommonAddress implements Parcelable {
    public static final String GROUP_TYPE_HOME = "1";
    public static final String GROUP_TYPE_COMPANY = "3";
    public static final String GROUP_TYPE_CUSTOM_NAME = "4";
    public static final String GROUP_TYPE_ETC = "5";

    @SerializedName("customName")
    private String customName;

    @SerializedName("sido")
    private String sido;

    @SerializedName("gungu")
    private String gungu;

    @SerializedName("dong")
    private String dong;    // 필드명은 '동' 이지만 '동, 읍, 면, 리'가 다 이 필드에 쓰인다.

    @SerializedName("hangDong")
    private String hangDong;

    @SerializedName("ri")
    private String ri;  // 실제 데이터 통신에서는 안씀

    @SerializedName("jibun")
    private String jibun;   // 필드명은 '지번'이지만 '산'주소가 있을경우 '산 지번'으로 저장됨

    @SerializedName("roadName")
    private String roadName;

    @SerializedName("roadNum")
    private String roadNum;

    @SerializedName("roadDestBuilding")
    private String roadDestBuilding;

    @SerializedName("detail")
    private String detail;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("latitude")
    private double latitude;

    //private boolean isFavorite; // 주소목록 즐겨찾기여부 판별에 사용
    private String date;    // 주소목록 정렬에 사용

    @SerializedName("groupType")
    private String groupType = GROUP_TYPE_ETC; // 주소 구분자에 사용(집인지 회사인지 둘 다 아닌지 판별)

    @SerializedName("isSelected")
    private boolean isSelected; // 주소 선택여부 체크하기 위해

    public CommonAddress() {
    }

    protected CommonAddress(Parcel in) {
        customName = in.readString();
        sido = in.readString();
        gungu = in.readString();
        dong = in.readString();
        hangDong = in.readString();
        ri = in.readString();
        jibun = in.readString();
        roadName = in.readString();
        roadNum = in.readString();
        roadDestBuilding = in.readString();
        detail = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
       // isFavorite = in.readByte() != 0;
        date = in.readString();
        groupType = in.readString();
        isSelected = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customName);
        dest.writeString(sido);
        dest.writeString(gungu);
        dest.writeString(dong);
        dest.writeString(hangDong);
        dest.writeString(ri);
        dest.writeString(jibun);
        dest.writeString(roadName);
        dest.writeString(roadNum);
        dest.writeString(roadDestBuilding);
        dest.writeString(detail);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
     //   dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeString(date);
        dest.writeString(groupType);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @SuppressWarnings("unused")
    public static final Creator<CommonAddress> CREATOR = new Creator<CommonAddress>() {
        @Override
        public CommonAddress createFromParcel(Parcel in) {
            return new CommonAddress(in);
        }

        @Override
        public CommonAddress[] newArray(int size) {
            return new CommonAddress[size];
        }
    };

    public String getCustomName() {
        if (customName == null) {
            customName = "";
        }

        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public String getSido() {
        if (sido == null) {
            sido = "";
        }

        return sido;
    }

    public void setSido(String sido) {
        this.sido = sido;
    }

    public String getGungu() {
        if (gungu == null) {
            gungu = "";
        }

        return gungu;
    }

    public void setGungu(String gungu) {
        this.gungu = gungu;
    }

    public String getDong() {
        if (dong == null) {
            dong = "";
        }

        return dong;
    }

    public void setDong(String dong) {
        this.dong = dong;
    }

    public String getHangDong() {
        if (hangDong == null) {
            hangDong = "";
        }

        return hangDong;
    }

//    public String getHangDongRi() {
//        if (hangDong == null) {
//            hangDong = "";
//        }
//
//        if (ri == null) {
//            ri = "";
//        }
//
//        return (hangDong + " " + ri).trim();
//    }

    public void setHangDong(String hangDong) {
        this.hangDong = hangDong;
    }

    public String getRi() {
        if (ri == null) {
            ri = "";
        }

        return ri;
    }

    public void setRi(String ri) {
        this.ri = ri;
    }

    public String getJibun() {
        if (jibun == null) {
            jibun = "";
        }

        return jibun;
    }

    public void setJibun(String jibun) {
        this.jibun = jibun;
    }

    public String getRoadName() {
        if (roadName == null) {
            roadName = "";
        }

        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getRoadNum() {
        if (roadNum == null) {
            roadNum = "";
        }

        return roadNum;
    }

    public void setRoadNum(String roadNum) {
        this.roadNum = roadNum;
    }

    public String getRoadDestBuilding() {
        if (roadDestBuilding == null) {
            roadDestBuilding = "";
        }

        return roadDestBuilding;
    }

    public void setRoadDestBuilding(String roadDestBuilding) {
        this.roadDestBuilding = roadDestBuilding;
    }

    public String getDetail() {
        if (detail == null) {
            detail = "";
        }

        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double lon) {
        this.longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double lat) {
        this.latitude = lat;
    }

//    public boolean isFavorite() {
//        return isFavorite;
//    }
//
//    public void setFavorite(boolean favorite) {
//        isFavorite = favorite;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGroupType() {
        if (groupType == null) {
            groupType = GROUP_TYPE_ETC;
        }

        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}