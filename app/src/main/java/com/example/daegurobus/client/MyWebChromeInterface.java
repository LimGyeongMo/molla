package com.example.daegurobus.client;

import android.webkit.JsResult;
import android.webkit.WebView;

public interface MyWebChromeInterface {
    void webViewLoading(int progress);

    void webViewLoadFinish();

    void onJsAlert(WebView view, String url, String message, JsResult result);

    void onJsConfirm(WebView view, String url, String message, JsResult result);
}