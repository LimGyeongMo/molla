package com.example.daegurobus.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CommonSearchKeyword implements Parcelable {
    private String keyword;
    private String date;

    public CommonSearchKeyword(String keyword, String date) {
        this.keyword = keyword;
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(keyword);
        dest.writeString(date);
    }

    public CommonSearchKeyword(Parcel in) {
        keyword = in.readString();
        date = in.readString();
    }

    public static final Creator<CommonSearchKeyword> CREATOR = new Creator<CommonSearchKeyword>() {
        @Override
        public CommonSearchKeyword createFromParcel(Parcel in) {
            return new CommonSearchKeyword(in);
        }

        @Override
        public CommonSearchKeyword[] newArray(int size) {
            return new CommonSearchKeyword[size];
        }
    };

    public String getKeyword() {
        if (keyword == null) {
            keyword = "";
        }

        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDate() {
        if (date == null) {
            date = "";
        }

        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}