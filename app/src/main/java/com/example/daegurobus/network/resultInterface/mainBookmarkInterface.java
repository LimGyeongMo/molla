package com.example.daegurobus.network.resultInterface;

import com.example.daegurobus.model.StationApi;

import retrofit2.Response;

public interface mainBookmarkInterface {

    void success(Response<StationApi> response);
}
