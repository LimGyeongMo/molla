package com.example.daegurobus.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import com.example.daegurobus.app.MyPreferencesManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.naver.maps.geometry.LatLng;

import java.util.Timer;
import java.util.TimerTask;

public class GpsUtil {
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final int TIMEOUT = 10000;

    private MyPreferencesManager myPreferencesManager;
    private Context context;
    private Callback callback;

    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private int priority = LocationRequest.PRIORITY_HIGH_ACCURACY;

    protected Timer serverTextTimer;

    public GpsUtil(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        myPreferencesManager = MyPreferencesManager.getInstance(context);
        initLocationUpdate();
    }

    public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
        double R = 6378.137;
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }

    @SuppressLint("MissingPermission")
    private void initLocationUpdate() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                callback.getLastLocation(new LatLng(location.getLatitude(), location.getLongitude()));
            } else {
                callback.getLastLocation(null);
            }
        });

        mSettingsClient = LocationServices.getSettingsClient(context);

        // 위치수신시 호출됨
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                clearTimeoutTimer();

                mCurrentLocation = locationResult.getLastLocation();
                myPreferencesManager.setCustomerLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));

                callback.getCurrentLocation(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                stopLocationUpdate();
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(UPDATE_INTERVAL_IN_MILLISECONDS / 2);
        mLocationRequest.setPriority(priority);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        startTimeoutLimitTimer();

        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            }
        }).addOnFailureListener(e -> {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());// ※ S20이상의 최신 폰에서 발생하였는데 RESOLUTION_REQUIRED가 떳을 경우에도 위치 업데이트를 사용하도록 코드 추가
        });
    }

    private void stopLocationUpdate() {
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }

        callback.stopLocationUpdate();
    }

    private void startTimeoutLimitTimer() {
        clearTimeoutTimer();

        serverTextTimer = new Timer();
        serverTextTimer.schedule(new ServerTextTask(), TIMEOUT);
    }

    private void clearTimeoutTimer() {
        if (serverTextTimer != null) {
            serverTextTimer.cancel();
            serverTextTimer = null;
        }
    }

    public static double getDistanceMeter(double lat1, double lon1, double lat2, double lon2) {
        double theta;
        double dist;

        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        dist = dist * 1609.344;
        return dist;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public interface Callback {
        void getLastLocation(LatLng latLng);

        void getCurrentLocation(LatLng latLng);

        void getCurrentLocationTimeout();

        void stopLocationUpdate();
    }

    class ServerTextTask extends TimerTask {
        public void run() {
            callback.getCurrentLocationTimeout();
        }
    }
}