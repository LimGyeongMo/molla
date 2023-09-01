package com.example.daegurobus.network.naver;

import android.content.Context;

import com.example.daegurobus.R;
import com.example.daegurobus.network.naver.response.ResponseGeocode;
import com.example.daegurobus.network.naver.response.ResponseMe;
import com.example.daegurobus.network.naver.response.ResponseReverseGeocode;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public class RetrofitClient {
    private static final String API_URL = "https://naveropenapi.apigw.ntruss.com/";

    private static RetrofitInterface retrofitInterface;

    static RetrofitInterface getClient(Context context) {
        if (retrofitInterface == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor interceptor = chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("X-NCP-APIGW-API-KEY-ID", context.getString(R.string.naver_platform_id))
                        .addHeader("X-NCP-APIGW-API-KEY", context.getString(R.string.naver_platform_secret_key))
                        .build();

                return chain.proceed(newRequest);
            };

            CookieManager manager = new CookieManager();
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            OkHttpClient builder = new OkHttpClient
                    .Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(interceptor)
                    .cookieJar(new JavaNetCookieJar(manager))
                    .build();

            Retrofit retrofit;

            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder)
                    .build();

            retrofitInterface = retrofit.create(RetrofitInterface.class);
        }

        return retrofitInterface;
    }

    public interface RetrofitInterface {
        // 좌표값으로 주소찾기
        @GET("map-reversegeocode/v2/gc")
        Call<ResponseReverseGeocode> getReverseGeocoding(@QueryMap Map<String, String> options);

        // 키워드로 주소찾기
        @GET("map-geocode/v2/geocode")
        Call<ResponseGeocode> getGeocoding(@QueryMap Map<String, String> options);

        // 주소 스냅샷 가져오기
        @GET("map-static/v2/raster")
        Call<ResponseBody> getStaticMap(@Query("center") String center, @Query("w") int w, @Query("h") int h, @Query("level") int level);

        // 내 계정정보 가져오기
        @GET("https://openapi.naver.com/v1/nid/me")
        Call<ResponseMe> me(@HeaderMap Map<String, String> headers);
    }
}
