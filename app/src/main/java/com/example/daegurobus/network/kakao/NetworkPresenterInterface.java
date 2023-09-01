package com.example.daegurobus.network.kakao;

import com.example.daegurobus.network.kakao.resultInterface.KeywordInterface;

public interface NetworkPresenterInterface {
    void keyword(String query, int page, int size, double x, double y, KeywordInterface anInterface);
}
