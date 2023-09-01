package com.example.daegurobus.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daegurobus.model.RecentKeyword;
import com.example.daegurobus.model.RecentStationKeyword;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.naver.maps.geometry.LatLng;

import java.util.ArrayList;

public class MyPreferencesManager {

    private static MyPreferencesManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final int PRIVATE_MODE = Context.MODE_PRIVATE;
    private static String PREFERENCE_NAME = "prefs";

    private MyPreferencesManager(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public static synchronized MyPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new MyPreferencesManager(context);
        }

        return instance;
    }

    public static final String PRIVATE_IP = "PRIVATE_IP";

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public static String BUS_SEARCH = "BUS_SEARCH";
    public static String BUS_STATION_SEARCH = "BUS_STATION_SEARCH";

    public ArrayList<RecentKeyword> getRecentSearchBus(){
        ArrayList<RecentKeyword> items;

        try {
            items = new Gson().fromJson(sharedPreferences.getString(BUS_SEARCH, ""), new TypeToken<ArrayList<RecentKeyword>>() {
            }.getType());

            if (items == null) {
                items = new ArrayList<>();
            }
        } catch (Exception e) {
            items = new ArrayList<>();
        }

        return items;
    }


    public void setRecentSearchBus(ArrayList<RecentKeyword> items) {
        editor.putString(BUS_SEARCH, new Gson().toJson(items));
        editor.apply();
    }

    public ArrayList<RecentStationKeyword> getRecentStationSearchBus(){
        ArrayList<RecentStationKeyword> items;

        try {
            items = new Gson().fromJson(sharedPreferences.getString(BUS_STATION_SEARCH, ""), new TypeToken<ArrayList<RecentStationKeyword>>() {
            }.getType());

            if (items == null) {
                items = new ArrayList<>();
            }
        } catch (Exception e) {
            items = new ArrayList<>();
        }

        return items;
    }


    public void setRecentStationSearchBus(ArrayList<RecentStationKeyword> items) {
        editor.putString(BUS_STATION_SEARCH, new Gson().toJson(items));
        editor.apply();
    }

    public static final String CUSTOMER_LAT_LNG = "CUSTOMER_LAT_LNG";

    public void setCustomerLatLng(LatLng latLng) {
        try {
            String json = new Gson().toJson(latLng);

            editor.putString(CUSTOMER_LAT_LNG, json);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
