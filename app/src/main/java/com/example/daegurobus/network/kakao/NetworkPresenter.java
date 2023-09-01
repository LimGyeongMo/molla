package com.example.daegurobus.network.kakao;

import android.content.Context;

import com.example.daegurobus.R;
import com.example.daegurobus.network.kakao.response.ResponseKeyword;
import com.example.daegurobus.network.kakao.resultInterface.KeywordInterface;

import org.jetbrains.annotations.NotNull;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;

public class NetworkPresenter implements NetworkPresenterInterface {
    private final Context context;

    public NetworkPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void keyword(String query, int page, int size, double x, double y, KeywordInterface anInterface) {
        RetrofitClient
                .getClient(context)
                .keyword(query, page, size, x, y)
                .enqueue(new Callback<ResponseKeyword>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseKeyword> call, @NotNull retrofit2.Response<ResponseKeyword> response) {
                        try {
                            if (!response.isSuccessful()) {
                                anInterface.error("undefined error\nerror code : " + response.code());
                                return;
                            }

                            ResponseKeyword item = response.body();

                            anInterface.success(item);
                        } catch (Exception e) {
                            anInterface.error("키워드 조회에 실패하였습니다.");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseKeyword> call, @NotNull Throwable t) {
                        if (t instanceof UnknownHostException) {
                            anInterface.error(context.getString(R.string.msg_failure_network));
                        } else {
                            anInterface.error("키워드 조회에 실패하였습니다.");
                        }
                    }
                });
    }
}
