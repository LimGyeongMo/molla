package com.example.daegurobus.geoClient;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RouteGeo implements Parcelable {



    private double routeLat;
    private double routeLon;

    public RouteGeo(){

    }


    protected RouteGeo(Parcel in) {
        routeLat = in.readDouble();
        routeLon = in.readDouble();
    }

    public static final Creator<RouteGeo> CREATOR = new Creator<RouteGeo>() {
        @Override
        public RouteGeo createFromParcel(Parcel in) {
            return new RouteGeo(in);
        }

        @Override
        public RouteGeo[] newArray(int size) {
            return new RouteGeo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(routeLat);
        dest.writeDouble(routeLon);
    }

    public double getRouteLat() {
        return routeLat;
    }

    public void setRouteLat(double routeLat) {
        this.routeLat = routeLat;
    }

    public double getRouteLon() {
        return routeLon;
    }

    public void setRouteLon(double routeLon) {
        this.routeLon = routeLon;
    }
}
