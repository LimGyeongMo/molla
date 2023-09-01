package com.example.daegurobus.geoClient;

import android.content.Context;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GeoRetrofitClient {

    private static GeoRetrofitInterface retrofitInterface1;

    private static String BASEURL = "https://busnodelink.daeguro.co.kr:45023/";

    public static GeoRetrofitInterface getClient(Context context){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

       OkHttpClient builder = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .build();
                    return chain.proceed(newRequest);
                })
                .cookieJar(new JavaNetCookieJar(manager))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder)
                .build();

        retrofitInterface1 = retrofit.create(GeoRetrofitInterface.class);

        return retrofitInterface1;

    }

    public interface GeoRetrofitInterface {
        @GET("MapRouterAddress/GetNodeLink")
        Call<RouteLines> RouteLine(@Query("routeId") String routeId);

    }
}
