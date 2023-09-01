package com.example.daegurobus.network;

import android.content.Context;

import com.example.daegurobus.model.RequestBus;
import com.example.daegurobus.model.StationApi;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class Retrofit2Client {

    private static RetrofitInterface retrofitInterface;
    private static String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb2RlIjoiMTMwMDM0OSIsIklkIjoiZ210b3gxMDIxQGdtYWlsLmNvbSIsIklkR2JuIjoiQSIsIm1jb2RlIjoiMSIsInRlc3RHYm4iOiJSIiwibmFtZSI6IuyehOqyveuqqCIsInRlbG5vIjoiMDEwMzg3NDI5ODYiLCJqdGkiOiJmN2FkNmQwYy1mZmY5LTQ2ZGUtODE1Yi04MTYzNGU2MTJhYzgiLCJuYmYiOjE2OTI4MzcxMjksImV4cCI6MTY5NTUxNjEyOSwiaWF0IjoxNjkyODM3NzI5fQ.Rl7-eiMVZi9JzUUo0orugBlEreX_lJFOluiVZO-x1WM";

    public static RetrofitInterface getClient(Context context){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient builder = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);
                })
                .cookieJar(new JavaNetCookieJar(manager))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DEFINE.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder)
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        return retrofitInterface;

    }



    public interface RetrofitInterface {

        @POST("Main/Proc")
        Call<StationApi> busMainBookmark(@Body RequestBus requestBus);
        @POST("Main/Proc")
        Call<StationApi> getBusList(@Body RequestBus requestBus);

        //사용안함
//        @POST("Main/Proc")
//        Call<StationApi> busSearchResult(@Body RequestBus requestBus);

        @POST("Main/Proc")
        Call<StationApi> busStationAuto(@Body RequestBus requestBus);

        //사용안함
//        @POST("Main/Proc")
//        Call<StationApi> busStationResult(@Body RequestBus requestBus);

        @POST("Main/Proc")
        Call<StationApi> busStationDetail(@Body RequestBus requestBus);

        @POST("Main/Proc")
        Call<StationApi> busDetail(@Body RequestBus requestBus);

        @POST("Main/Proc")
        Call<StationApi> bookmarkInsert(@Body RequestBus requestBus);

        @POST("Main/Proc")
        Call<StationApi> bookmarkDelete(@Body RequestBus requestBus);

        @POST("Main/Proc")
        Call<StationApi> recentBusLocation(@Body RequestBus requestBus);

        @POST("Main/Proc")
        Call<StationApi> stationSurroundingInfo(@Body RequestBus requestBus);
        @POST("Main/Proc")
        Call<StationApi> getSearchPathV2(@Body RequestBus requestBus);

        @POST("Main/Proc")
        Call<StationApi> getNoticeList(@Body RequestBus requestBus);


    }
}
