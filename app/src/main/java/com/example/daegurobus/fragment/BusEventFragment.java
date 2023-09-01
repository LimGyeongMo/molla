package com.example.daegurobus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.example.daegurobus.R;
import com.example.daegurobus.client.MyWebChromeClient;
import com.example.daegurobus.client.MyWebChromeInterface;
import com.example.daegurobus.client.MyWebViewClient;
import com.example.daegurobus.databinding.BusFragmentMainEventBinding;
import com.example.daegurobus.widget.CommonNoticeDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class BusEventFragment extends BusBaseFragment{
    private BusFragmentMainEventBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bus_fragment_main_event, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding = BusFragmentMainEventBinding.bind(getView());


        initLayout();
    }

    private void initLayout() {
        initTitlebarLayout();
        initWebViewLayout();
    }
    private void initTitlebarLayout() {

    }
    private void initWebViewLayout() {

        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        binding.webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        binding.webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webView.setWebViewClient(new MyWebViewClient(getContext()));
        binding.webView.setWebChromeClient(new MyWebChromeClient(getContext(), new MyWebChromeInterface() {
            @Override
            public void webViewLoading(int progress) {
                //   binding.webViewProgressBar.setProgress(progress);
            }

            @Override
            public void webViewLoadFinish() {

            }

            @Override
            public void onJsAlert(WebView view, String url, String message, JsResult result) {

            }

            @Override
            public void onJsConfirm(WebView view, String url, String message, JsResult result) {

            }
        }));
        binding.webView.addJavascriptInterface(new AndroidBridge(), "daeguroApp");
        binding.webView.addJavascriptInterface(new AndroidBridgeAppCallback(), "appCallback");
    }

    public class AndroidBridge {
        @JavascriptInterface
        public void result(String json) {
            getActivity().runOnUiThread(() -> {
//                Response<CommonEvent> response = new Gson().fromJson(json, new TypeToken<Response<CommonEvent>>() {
//
//
//                }.getType());
            });
        }
    }

    public class AndroidBridgeAppCallback {
        @JavascriptInterface
        public void result(String json) {
            getActivity().runOnUiThread(() -> {



            });
        }
    }

    public void pageSelected() {
        requestBoard();
    }

    private void requestBoard() {


    }


}
