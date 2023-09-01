package com.example.daegurobus.network.naver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.example.daegurobus.network.naver.request.RequestGeocoding;
import com.example.daegurobus.network.naver.request.RequestMe;
import com.example.daegurobus.network.naver.request.RequestReverseGeocoding;
import com.example.daegurobus.network.naver.response.ResponseGeocode;
import com.example.daegurobus.network.naver.response.ResponseMe;
import com.example.daegurobus.network.naver.response.ResponseReverseGeocode;
import com.example.daegurobus.network.naver.resultInterface.GeocodingInterface;
import com.example.daegurobus.network.naver.resultInterface.MeInterface;
import com.example.daegurobus.network.naver.resultInterface.ReverseGeocodingInterface;
import com.example.daegurobus.network.naver.resultInterface.StaticMapInterface;
import com.example.daegurobus.util.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NetworkPresenter implements NetworkPresenterInterface {
    private final Context context;

    public NetworkPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void getReverseGeocoding(RequestReverseGeocoding request, ReverseGeocodingInterface anInterface) {
        Map<String, String> options = new HashMap<>();
        options.put("coords", request.getLongitude() + "," + request.getLatitude());
        options.put("output", "json");
        options.put("orders", "addr,admcode,roadaddr");

        RetrofitClient
                .getClient(context)
                .getReverseGeocoding(options)
                .enqueue(new Callback<ResponseReverseGeocode>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseReverseGeocode> call, @NotNull retrofit2.Response<ResponseReverseGeocode> response) {
                        try {
                            if (!response.isSuccessful()) {
                                anInterface.error("undefined error\nerror code : " + response.code());
                                return;
                            }

                            ResponseReverseGeocode item = response.body();
                            LogUtil.i("naver: " + new Gson().toJson(item));

                            anInterface.success(item.getResults());
                        } catch (Exception e) {
                            anInterface.error(e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseReverseGeocode> call, @NotNull Throwable t) {
                        anInterface.error(t.toString());
                    }
                });
    }

    @Override
    public void getGeocoding(RequestGeocoding request, GeocodingInterface anInterface) {
        Map<String, String> options = new HashMap<>();
        options.put("coordinate", request.getLongitude() + "," + request.getLatitude());
        options.put("query", request.getQuery());

        RetrofitClient
                .getClient(context)
                .getGeocoding(options)
                .enqueue(new Callback<ResponseGeocode>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseGeocode> call, @NotNull retrofit2.Response<ResponseGeocode> response) {
                        try {
                            if (response.isSuccessful()) {
                                ResponseGeocode item = response.body();
                                anInterface.success(item.getAddresses());
                            } else {
                                anInterface.error("주소검색에 실패하였습니다.");
                            }
                        } catch (Exception e) {
                            anInterface.error("주소검색에 실패하였습니다.");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseGeocode> call, @NotNull Throwable t) {
                        anInterface.error("주소검색에 실패하였습니다.");
                    }
                });
    }

    @Override
    public void getStaticMap(String center, int width, int height, int level, StaticMapInterface anInterface) {
        RetrofitClient
                .getClient(context)
                .getStaticMap(center, width, height, level)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            InputStream is = response.body().byteStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            anInterface.success(bitmap);
                        } else {
                            anInterface.error(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        anInterface.error(t.toString());
                    }
                });
    }

    @Override
    public void me(RequestMe request, MeInterface anInterface) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + request.getToken());

        RetrofitClient
                .getClient(context)
                .me(headers)
                .enqueue(new Callback<ResponseMe>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseMe> call, @NotNull retrofit2.Response<ResponseMe> response) {
                        try {
                            if (!response.isSuccessful()) {
                                anInterface.error("undefined error");
                                return;
                            }

                            ResponseMe item = response.body();

                            if (!ResponseMe.SUCCESS_CODE.equals(item.getResultCode())) {
                                anInterface.error(item.getMessage());
                                return;
                            }

                            anInterface.success(item.getNaverAccount());
                        } catch (Exception e) {
                            anInterface.error(e.toString());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseMe> call, @NotNull Throwable t) {
                        anInterface.error(t.toString());
                    }
                });
    }
}
