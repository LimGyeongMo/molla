package com.example.daegurobus.network.resultInterface;

import com.example.daegurobus.model.SearchPathV2;

import java.util.ArrayList;

public interface GetSearchPathV2Interface {

    void success(ArrayList<SearchPathV2> searchPathV2List);

    void error(String msg);
}
