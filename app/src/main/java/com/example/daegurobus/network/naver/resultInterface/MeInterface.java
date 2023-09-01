package com.example.daegurobus.network.naver.resultInterface;

import com.example.daegurobus.network.naver.model.me.NaverAccount;

public interface MeInterface {
    void success(NaverAccount naverAccount);

    void error(String message);
}
