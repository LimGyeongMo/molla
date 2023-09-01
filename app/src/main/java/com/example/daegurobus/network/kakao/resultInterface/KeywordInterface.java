package com.example.daegurobus.network.kakao.resultInterface;

import com.example.daegurobus.network.kakao.response.ResponseKeyword;

public interface KeywordInterface {
    void success(ResponseKeyword response);

    void error(String message);
}
