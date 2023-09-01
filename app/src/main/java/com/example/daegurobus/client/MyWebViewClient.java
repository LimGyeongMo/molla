package com.example.daegurobus.client;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.widget.Toast;

import com.example.daegurobus.util.LogUtil;

import java.net.URISyntaxException;

public class MyWebViewClient extends android.webkit.WebViewClient {
    private Context context;

    public MyWebViewClient(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url != null && !url.equals("about:blank")) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                if (url.contains("http://market.android.com") ||
                        url.contains("http://m.ahnlab.com/kr/site/download") ||
                        url.endsWith(".apk")) {
                    return url_scheme_intent(view, url);
                } else {
                    view.loadUrl(url);
                }
            } else if (url.startsWith("mailto:")) {
                return false;
            } else if (url.startsWith("tel:")) {
                return false;
            } else {
                return url_scheme_intent(view, url);
            }
        } else if (url != null && url.startsWith("intent://")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                Intent existPackage = context.getPackageManager().getLaunchIntentForPackage(intent.getPackage());

                if (existPackage != null) {
                    context.startActivity(intent);
                } else {
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW);
                    marketIntent.setData(Uri.parse("market://details?id=" + intent.getPackage()));
                    context.startActivity(marketIntent);
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (url != null && url.startsWith("market://")) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                if (intent != null) {
                    context.startActivity(intent);
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        view.loadUrl(url);
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    private boolean url_scheme_intent(WebView view, String url) {
        LogUtil.d("[PayDemoActivity] called__test - url=[" + url + "]");

        //chrome 버젼 방식 : 2014.01 추가
        if (url.startsWith("intent")) {
            Intent intent = null;

            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            } catch (URISyntaxException ex) {
                LogUtil.d("[PayDemoActivity] URISyntaxException=[" + ex.getMessage() + "]");
                return false;
            }

            // 앱설치 체크를 합니다.
            if (context.getPackageManager().resolveActivity(intent, 0) == null) {
                String packagename = intent.getPackage();

                if (packagename != null && !"net.nshc.droidx3web".equals(packagename)) {    // droidx3web 왜자꾸 뜨냐
                    //context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:" + packagename)));
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packagename)));
                    return true;
                }
            }

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intent.getDataString()));

            try {
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                LogUtil.d("[PayDemoActivity] ActivityNotFoundException=[" + e.getMessage() + "]");
                return false;
            }
        }
        // 기존 방식
        else {
              /*
             * 아래 주석된 내용은 업체에서 필요에 따라 설정하는 부분입니다.
             * KCP에서는 웹에서 지원될수 있도록 처리가 되어 있습니다.
             * 어플에서 판단이 필요할경우 추가해주세요.
             * 단. 아래 주석 내용은 공지없이 KCP 에서 변경이 가능합니다.
             *
            if ( url.startsWith( "ispmobile" ) )
            {
                if( !new PackageState( this ).getPackageDownloadInstallState( "kvp.jjy.MispAndroid" ) )
                {
                    startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse( "market://details?id=kvp.jjy.MispAndroid320" ) ) );

                    return true;
                }
            }

            // 삼성과 같은 경우 어플이 없을 경우 마켓으로 이동 할수 있도록 넣은 샘플 입니다.
            // 실제 구현시 업체 구현 여부에 따라 삭제 처리 하시는것이 좋습니다.
            if ( url.startsWith( "mpocket.online.ansimclick" ) )
            {
                if( !new PackageState( this ).getPackageDownloadInstallState( "kr.co.samsungcard.mpocket" ) )
                {
                    Toast.makeText(this, "어플을 설치 후 다시 시도해 주세요.", Toast.LENGTH_LONG).show();

                    startActivity( new Intent(Intent.ACTION_VIEW, Uri.parse( "market://details?id=kr.co.samsungcard.mpocket" ) ) );

                    return true;
                }
            }
            */

            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                // 어플이 설치 안되어 있을경우 오류 발생. 해당 부분은 업체에 맞게 구현
                Toast.makeText(context, "해당 어플을 설치해 주세요.", Toast.LENGTH_LONG).show();
            }
        }

        return true;
    }
}