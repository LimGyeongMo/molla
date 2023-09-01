package com.example.daegurobus.network.kakao;

import android.content.Context;

import com.example.daegurobus.R;
import com.example.daegurobus.network.kakao.response.ResponseKeyword;

import java.net.CookieManager;
import java.net.CookiePolicy;

import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitClient {
    private static final String API_URL = "http://dapi.kakao.com/v2/local/search/";

    private static RetrofitInterface retrofitInterface;

    static RetrofitInterface getClient(Context context) {
        if (retrofitInterface == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor interceptor = chain -> {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", context.getString(R.string.kakao_rest_key))
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
        // 명칭검색
        @GET("keyword.json")
        Call<ResponseKeyword> keyword(@Query("query") String query, @Query("page") int page, @Query("size") int size, @Query("x") double x, @Query("y") double y);
    }
}
