package com.example.daegurobus.client;

import android.content.Context;
import android.os.Message;
import android.webkit.JsResult;
import android.webkit.WebView;

import com.example.daegurobus.widget.CommonNoticeDialog;

public class MyWebChromeClient extends android.webkit.WebChromeClient {
    private Context context;
    private MyWebChromeInterface webViewInterface;

    public MyWebChromeClient(Context context, MyWebChromeInterface webViewInterface) {
        super();
        this.context = context;
        this.webViewInterface = webViewInterface;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);

        webViewInterface.webViewLoading(newProgress);

        if (newProgress >= 100) {
            webViewInterface.webViewLoadFinish();
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        new CommonNoticeDialog(context)
                .setMessage(message)
                .setCancelPossible(false)
                .setCallbackListener(new CommonNoticeDialog.CallbackListener() {
                    @Override
                    public void positive() {
                        result.confirm();
                    }

                    @Override
                    public void negative() {
                        result.cancel();
                    }
                })
                .show();
        webViewInterface.onJsAlert(view, url, message, result);
        return true;
        //      return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        new CommonNoticeDialog(context)
                .setMessage(message)
                .setCancelPossible(false)
                .setCallbackListener(new CommonNoticeDialog.CallbackListener() {
                    @Override
                    public void positive() {
                        result.confirm();
                    }

                    @Override
                    public void negative() {
                        result.cancel();
                    }
                })
                .show();
        webViewInterface.onJsConfirm(view, url, message, result);
        return true;
        //   return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
//        WebView.HitTestResult result = view.getHitTestResult();
//        String url = result.getExtra();
//
//        if (url != null && url.indexOf("___target=_blank") > -1) {
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            i.setData(Uri.parse(url));
//            context.startActivity(i);
//            return true;
//        }
//        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);

        WebView newWebView = new WebView(view.getContext());

        WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
        transport.setWebView(newWebView);
        resultMsg.sendToTarget();
        return true;

    }
}