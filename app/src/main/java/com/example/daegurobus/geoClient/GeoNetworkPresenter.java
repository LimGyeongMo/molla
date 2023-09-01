package com.example.daegurobus.geoClient;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GeoNetworkPresenter {
    private Context context;

    public GeoNetworkPresenter(Context context) {
        this.context = context;
    }

    public void RouteLine(String query, StationLocationInterface anInterface) {

        GeoRetrofitClient
                .getClient(context)
                .RouteLine(query)
                .enqueue(new Callback<RouteLines>() {
                    @Override
                    public void onResponse(Call<RouteLines> call, Response<RouteLines> response) {


                        try {
                            RouteLines result = response.body();

                            ArrayList<ArrayList<Double>> routeAll = new ArrayList<>();
                            ArrayList<ArrayList<Double>> Downward = new ArrayList<>();
                            ArrayList<ArrayList<Double>> uphill = new ArrayList<>();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                routeAll = response.body().getData().get(i).getCoordinates();
                                Downward = response.body().getData().get(0).getCoordinates();
                                uphill = response.body().getData().get(1).getCoordinates();

                            }

                            anInterface.success(routeAll, Downward, uphill);

                        } catch (Exception e) {

                            anInterface.success(null, null, response.body().getData().get(1).getCoordinates());
                        }
                    }


                    @Override
                    public void onFailure(Call<RouteLines> call, Throwable t) {
                        System.out.println("왜안됨");
                    }
                });
    }
}