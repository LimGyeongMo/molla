package com.example.daegurobus.network.naver.resultInterface;

import android.graphics.Bitmap;

public interface StaticMapInterface {
    void success(Bitmap bitmap);

    void error(String message);
}
