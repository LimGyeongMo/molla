package com.example.daegurobus.util;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.daegurobus.util.LogUtil;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017-07-07.
 */

public class BasicUtil {
    private BasicUtil() {
        // 인스턴스 생성불가 처리
    }

    /**
     * 전화걸기(전화번호에서 문자만 추출)
     * permissions: <uses-permission android:name="android.permission.CALL_PHONE" />
     **/
    public static void sendCall(Context context, String tel) throws Exception {
        if (tel.length() == 0) {
            throw new Exception("number not exist");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("tel:");
        sb.append(exportOnlyNumber(tel));

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse(sb.toString()));

        context.startActivity(intent);
    }

    /**
     * SMS 전송
     **/
    public static void sendSms(Context context, String tel, String message) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("tel:");
            sb.append(tel);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
            intent.putExtra("address", tel);
            intent.putExtra("sms_body", message);
            intent.setType("vnd.android-dir/mms-sms");

            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 이메일 전송
     **/
    public static void sendEmail(Context context, String email, String subject, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("plain/contents");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT, text);

            context.startActivity(Intent.createChooser(intent, "이메일 전송"));
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 공유하기(문자열 공유)
     **/
    public static void sharing(Context context, String text) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.setType("text/plain");

            Intent chooserIntent = Intent.createChooser(intent, "공유하기");
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(chooserIntent);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 웹사이트 실행
     **/
    public static void launchWebsite(Context context, String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("http://");
                sb.append(url);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
                context.startActivity(intent);
            } catch (Exception e2) {
                Toast.makeText(context, e2.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 플레이스토어 실행
     **/
    public static void launchPlayStore(Context context, String url) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("market://details?id=");
            sb.append(url);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 현재앱의 플레이스토어 실행
     **/
    public static void launchMyPlayStore(Context context) {
        launchPlayStore(context, context.getPackageName());
    }

    /**
     * 맵 실행
     **/
    public static void launchMaps(Context context, double latitude, double longitude, String title) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            String data = String.format("geo:0,0?q=" + latitude + ", " + longitude);

            intent.setData(Uri.parse(data));
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void launchMapFirstNaver(Context context, double latitude, double longitude, String title) {
        try {
            //  if (context.getPackageManager().getLaunchIntentForPackage("com.nhn.android.nmap") != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("navermaps://?menu=location&pinType=place&lat=" + latitude + "&lng=" + longitude + "&title=" + title));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
//            } else {
//                launchMaps(context, latitude, longitude, title);
//            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
            launchMaps(context, latitude, longitude, title);
        }
    }

//    public static void routeMapFirstNaver(Context context, double latitude, double longitude, String title) {
//        try {
//            //  if (context.getPackageManager().getLaunchIntentForPackage("com.nhn.android.nmap") != null) {
//
//            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("navermaps://?menu=route&routeType=2&elat=" + latitude + "&elon=" + longitude + "&etitle=" + title + "&showMap=true"));
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("nmap://route/public?dlat=" + latitude + "&dlng=" + longitude + "&dname=" + title));   //  + "&appname=com.insungdata.daegudelivery"
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//
////            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.map.naver.com/rrouth.nhn?menu=route&ex=" + latitude + "&ey=" + longitude + "&ename=" + title + "&pathType=1&showMap=true"));
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            context.startActivity(intent);
//
////            } else {
////                launchMaps(context, latitude, longitude, title);
////            }
//        } catch (Exception e) {
//            LogUtil.e(e.toString());
//            launchMaps(context, latitude, longitude, title);
//        }
//    }

    /**
     * 생년월일 포멧으로 변경
     **/
    public static String convertFormatBirth(String date) {
        if ((date == null) || (date.length() != 8)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(date);
        sb.insert(4, ".");
        sb.insert(7, ".");
        return sb.toString();
    }

    /**
     * 전화번호 포멧으로 변경
     **/
    public static String convertFormatTel(String tel) {
        try {
            if ((tel == null) || (tel.length() < 4)) {
                return tel;
            }

            if (safeParseLong(tel) == 0) {
                return tel;
            }

            switch (tel.length()) {
                case 5:
                    return String.format("%s-%s", tel.substring(0, 1), tel.substring(1, 5));

                case 6:
                    return String.format("%s-%s", tel.substring(0, 2), tel.substring(2, 6));

                case 7:
                    return String.format("%s-%s", tel.substring(0, 3), tel.substring(3, 7));

                case 8:
                    return String.format("%s-%s", tel.substring(0, 4), tel.substring(4, 8));

                case 9:
                    return String.format("%s-%s-%s", tel.substring(0, 2), tel.substring(2, 5), tel.substring(5, 9));

                case 10:
                    return String.format("%s-%s-%s", tel.substring(0, 3), tel.substring(3, 6), tel.substring(6, 10));

                case 12:
                    return String.format("%s-%s-%s", tel.substring(0, 3), tel.substring(3, 8), tel.substring(8, 12));

                default:
                    return String.format("%s-%s-%s", tel.substring(0, tel.length() - 8), tel.substring(tel.length() - 8, tel.length() - 4), tel.substring(tel.length() - 4));
            }
        } catch (Exception e) {
            return tel;
        }
    }

    public static String phoneNumberHyphenAdd(String num) {
        String formatNum = "";

        if (TextUtils.isEmpty(num)) {
            return formatNum;
        }

        num = num.replaceAll("-", "");

        if (num.length() == 11) {
            formatNum = num.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        } else if (num.length() >= 7) {
            formatNum = num.replaceAll("(\\d{3})(\\d{3})(\\d{1,3})", "$1-$2-$3");
        } else if (num.length() >= 4) {
            formatNum = num.replaceAll("(\\d{3})(\\d{1,4})", "$1-$2");
        } else {
            formatNum = num.replaceAll("(\\d{3})(\\d{3,4})(\\d{4})", "$1-$2-$3");
        }

        return formatNum;
    }

    public static String businessNumberHyphenAdd(String businessNumber) {
        String formatBusinessNumber = "";

        if (TextUtils.isEmpty(businessNumber)) {
            return formatBusinessNumber;
        }

        businessNumber = businessNumber.replaceAll("-", "");

        if (businessNumber.length() == 10) {
            formatBusinessNumber = businessNumber.replaceAll("(\\d{3})(\\d{2})(\\d{5})", "$1-$2-$3");
        } else if (businessNumber.length() >= 6) {
            formatBusinessNumber = businessNumber.replaceAll("(\\d{3})(\\d{2})(\\d{1,4})", "$1-$2-$3");
        } else if (businessNumber.length() >= 4) {
            formatBusinessNumber = businessNumber.replaceAll("(\\d{3})(\\d{1,2})", "$1-$2");
        } else {
            formatBusinessNumber = businessNumber.replaceAll("(\\d{3})(\\d{2})(\\d{5})", "$1-$2-$3");
        }

        return formatBusinessNumber;
    }

    /**
     * 사업자번호 포멧으로 변경
     **/
    public static String convertFormatBusinessNumber(String businessNumber) {
        try {
            businessNumber = exportOnlyNumber(businessNumber);

            if (businessNumber != null && businessNumber.length() == 10) {
                return String.format("%s-%s-%s", businessNumber.substring(0, 3), businessNumber.substring(3, 5), businessNumber.substring(5, 10));
            } else {
                return businessNumber;
            }
        } catch (Exception e) {
            return businessNumber;
        }
    }

    public static String addComma(Object obj) {
        try {
            String money = String.valueOf(obj);

            DecimalFormat df = new DecimalFormat("#,###,###,###,###,###,###,###,##0");
            BigDecimal bd = new BigDecimal(String.valueOf(money));
            return df.format(bd.doubleValue());
        } catch (Exception e) {
            return "0";
        }
    }

    public static String convertBizNumberFormat(String bizNumber) {
        try {
            StringBuilder sb = new StringBuilder(bizNumber);
            sb.insert(3, '-');
            sb.insert(6, '-');
            return sb.toString();
        } catch (Exception e) {
            return bizNumber;
        }
    }

    public static String convertBankNumberFormat(String bankName, String accountNumber) {
        try {
            StringBuilder sb = new StringBuilder(accountNumber);

            switch (bankName) {
                case "기업":  // 14자리
                    sb.insert(3, '-');
                    sb.insert(10, '-');
                    sb.insert(13, '-');
                    return sb.toString();
            }
        } catch (Exception e) {
        }

        return accountNumber;
    }

    /**
     * 금액 포멧으로 변경
     **/
    public static String convertFormatMoney(int money) {
        return convertFormatMoney(String.valueOf(money));
    }

    public static String convertFormatMoney(String money) {
        try {
            DecimalFormat df = new DecimalFormat("#,###,###,###,###,###,###,###,##0");
            BigDecimal bd = new BigDecimal(money);
            return df.format(bd.doubleValue());
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 문자열에서 숫자만 추출
     **/
    public static String exportOnlyNumber(String str) {
        return str.replaceAll("[^0-9]", "");
    }

    /**
     * 디바이스 전화번호 가져오기
     **/
    /**public static String getLine1Number(Context context) throws Exception {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String line1Number = mTelephonyManager.getLine1Number();
        return line1Number.replace("+82", "0");
    }**/

    /**
     * DP를 PX값으로 변환
     **/
    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * PX를 DP로 변환
     **/
    public static float pxToDp(Context context, float px) {
        return px / (context.getResources().getDisplayMetrics().densityDpi / 160f);
    }

    /**
     * 권한 승인여부 체크
     **/
    public static boolean isPermissionGranted(Context context, String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * int로 형변환
     **/
    public static int safeParseInt(String str) {
        return safeParseInt(str, 0);
    }

    public static int safeParseInt(String str, int defaultValue) {
        int value = defaultValue;

        try {
            value = Integer.parseInt(str);
        } catch (Exception e) {
        }

        return value;
    }

    /**
     * int로 형변환
     **/
    public static long safeParseLong(String str) {
        long value = 0;

        try {
            value = Long.parseLong(str);
        } catch (Exception e) {
        }

        return value;
    }

    public static float safeParseFloat(String str) {
        return safeParseFloat(str, 0.0f);
    }

    public static float safeParseFloat(String str, float defaultValue) {
        float value = defaultValue;

        try {
            value = Float.parseFloat(str);
        } catch (Exception e) {
        }

        return value;
    }

    /**
     * dobule로 형변환
     **/
    public static double safeParseDouble(String str) {
        return safeParseDouble(str, 0.0f);
    }

    public static double safeParseDouble(String s, double defaultValue) {
        double value = defaultValue;

        try {
            value = Double.parseDouble(s);
        } catch (Exception e) {
        }

        return value;
    }

    public static int calcDistance(int nDestLon, int nDestLat, int nSrcLon, int nSrcLat) {
        final double RG_METERPER_LON = 0.245;
        final double RG_METERPER_LAT = 0.300;

        float fDeltaX = 0.0f;
        float fDeltaY = 0.0f;
        int nDistance = 0;

        if (nDestLon == 0 || nDestLat == 0 || nSrcLon == 0 || nSrcLat == 0)
            return 0;

        fDeltaX = (float) (Math.abs((int) (nSrcLon - nDestLon)) * RG_METERPER_LON);
        fDeltaY = (float) (Math.abs((int) (nSrcLat - nDestLat)) * RG_METERPER_LAT);

        double fSquareX = fDeltaX * fDeltaX;
        double fSquareY = fDeltaY * fDeltaY;

        nDistance = (int) Math.sqrt(fSquareX + fSquareY);

        return nDistance;
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboard(Context context, View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            LogUtil.i(e.toString());
        }
    }

    public static short htonl(short x) {
        return (short) ((x << 8) | ((x >> 8) & 0xff));
    }

    public static int htonl(int x) {
        return (int) ((htonl((short) x) << 16) | (htonl((short) (x >> 16)) & 0xffff));
    }

    public static void intToByte(byte Data[], int nDataSize, int addData) {
        Data[nDataSize++] = (byte) (addData & 0xFF);
        Data[nDataSize++] = (byte) ((addData & 0xFF00) >> 8);
        Data[nDataSize++] = (byte) ((addData & 0xFF0000) >> 16);
        Data[nDataSize++] = (byte) ((addData & 0xFF000000) >> 24);
    }

    public static void stringToByte(byte Data[], int nDataSize, String addData) {
        try {
            byte Temp[] = addData.getBytes("ksc5601");

            for (int i = 0; i < Temp.length; i++) {
                Data[nDataSize++] = Temp[i];
            }
        } catch (Exception e) {
        }
    }

    public static String getYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "";
    }

    public static String getMonth(int Month) {
        Calendar calendar = Calendar.getInstance();

        String sMonth = "";
        if (Month >= 0) {
            sMonth = (Month + 1) + "";
        } else {
            sMonth = (calendar.get(Calendar.MONTH) + 1) + "";
        }

        if (sMonth.length() == 1) sMonth = "0" + sMonth;

        return sMonth;
    }

    public static String getDay(int Day) {
        Calendar calendar = Calendar.getInstance();

        String sDay = "";
        if (Day >= 0) {
            sDay = Day + "";
        } else {
            sDay = calendar.get(Calendar.DAY_OF_MONTH) + "";
        }

        if (sDay.length() == 1) sDay = "0" + sDay;

        return sDay;
    }

    public static String getHour(int Hour) {
        Calendar calendar = Calendar.getInstance();

        String sHour = "";
        if (Hour >= 0) {
            sHour = Hour + "";
        } else {
            sHour = calendar.get(Calendar.HOUR_OF_DAY) + "";
        }

        if (sHour.length() == 1) sHour = "0" + sHour;

        return sHour;
    }

    public static String getMinute(int Minute) {
        Calendar calendar = Calendar.getInstance();

        String sMinute = "";

        if (Minute >= 0) {
            sMinute = Minute + "";
        } else {
            sMinute = calendar.get(Calendar.MINUTE) + "";
        }

        if (sMinute.length() == 1) sMinute = "0" + sMinute;

        return sMinute;
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static String getTopPackageName(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT > 20) {
            return mActivityManager.getRunningAppProcesses().get(0).processName;
        } else {
            return mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
        }
    }

    public static boolean isDebugable(Context context) {
        boolean isDebug = false;
        PackageManager packageManager = context.getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            isDebug = (0 != (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {

        }

        return isDebug;
    }

    public static String getFullPathFromURI(Context context, Uri uri) {
        int columnIndex = 0;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor.moveToFirst()) {
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(columnIndex);
    }

    public static Uri getURIFromFullPath(Context context, String fullPath) {
        long photoId;
        Uri photoUri = MediaStore.Images.Media.getContentUri("external");
        String[] projection = {MediaStore.Images.ImageColumns._ID};
        Cursor cursor = context.getContentResolver().query(photoUri, projection, MediaStore.Images.ImageColumns.DATA + " LIKE ?", new String[]{fullPath}, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        photoId = cursor.getLong(columnIndex);

        cursor.close();
        return Uri.parse(photoUri.toString() + "/" + photoId);
    }

    public static ArrayList<String> getAppSignatures(Context context) {
        ArrayList<String> appCodes = new ArrayList<>();

        try {
            String packageName = context.getPackageName();
            PackageManager packageManager = context.getPackageManager();
            Signature[] signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;

            for (Signature signature : signatures) {
                String hash = getHash(packageName, signature.toCharsString());

                if (hash != null) {
                    appCodes.add(String.format("%s", hash));
                }

                LogUtil.i("hash: " + hash);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }

        return appCodes;
    }

    private static String getHash(String packageName, String signature) {
        String appInfo = packageName + " " + signature;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));

            byte[] hashSignature = messageDigest.digest();

            hashSignature = Arrays.copyOfRange(hashSignature, 0, 9);

            String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
            base64Hash = base64Hash.substring(0, 11);
            return base64Hash;
        } catch (NoSuchAlgorithmException e) {

        }

        return null;
    }

    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();

        try {
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void showToast(Context context, LayoutInflater inflater, String message) {
//        CommonToastBinding binding = DataBindingUtil.inflate(inflater, R.layout.common_toast, null, false);
//        binding.tvMessage.setText(message.replace(" ", "\u00A0"));
//
//        Toast toast = new Toast(context);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.BOTTOM, 0, (int) dpToPx(context, 76));
//        toast.setView(binding.getRoot());
//        toast.show();
//    }

    // 앞, 뒤 공백 제거. 중간의 여러개의 공백 1개로 치환 후 반환
    public static String getMeaningfulKeyword(String keyword) {
        return keyword.trim().replaceAll(" +", " ");
    }

    public static SpannableStringBuilder convertHighlightText(String originText, String keyword, int colorRes) {
        try {
            SpannableStringBuilder ssb = new SpannableStringBuilder(originText);

            String keywords[] = keyword.split(" ", -1);

            for (int i = 0; i < keywords.length; i++) {
                String word = keywords[i];
                int startIndex;
                int endIndex = 0;

                while (true) {
                    startIndex = originText.indexOf(word, endIndex);
                    endIndex = startIndex + word.length();

                    if (startIndex >= 0) {
                        ssb.setSpan(new ForegroundColorSpan(colorRes), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        break;
                    }
                }
            }

            return ssb;
        } catch (Exception e) {

        }

        return new SpannableStringBuilder();
    }

    public static String encodeUTF8(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            return "";
        }
    }


   /** public static void showPopupReview(Context context, View view, ReviewCallbackListener callbackListener) {
        try {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View popup = layoutInflater.inflate(R.layout.common_popup_review, null);

            PopupWindow popupWindow = new PopupWindow(popup, (int) BasicUtil.dpToPx(context, 144), LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setElevation(10);
            popupWindow.showAtLocation(popup, Gravity.NO_GRAVITY, location[0] + (int) BasicUtil.dpToPx(context, 20), location[1] - (int) BasicUtil.dpToPx(context, 8));

            TextView tvReportReview = popup.findViewById(R.id.tv_report_review);
            TextView tvReportUser = popup.findViewById(R.id.tv_report_user);

            tvReportReview.setOnClickListener(v -> {
                callbackListener.blockReview();
                popupWindow.dismiss();
            });
            tvReportUser.setOnClickListener(v -> {
                callbackListener.blockUser();
                popupWindow.dismiss();
            });
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
    }**/

   /** public static void showTaxiCardInfoDialog(Context context, View view, String sub, String info, String contents) {
        try {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View popup = layoutInflater.inflate(R.layout.taxi_common_popup, null);

            TextView tvTitleSub = popup.findViewById(R.id.title_sub);
            TextView tvCardInfo = popup.findViewById(R.id.info_message);
            TextView tvContents = popup.findViewById(R.id.contents);

            tvTitleSub.setText(sub);
            tvCardInfo.setText(info);

            tvContents.setText(contents.replace(" ", "\u00A0"));

            PopupWindow popupWindow = new PopupWindow(popup, (int) BasicUtil.dpToPx(context, 197), LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAtLocation(popup, Gravity.NO_GRAVITY, location[0] + (int) BasicUtil.dpToPx(context, 20), location[1] - (int) BasicUtil.dpToPx(context, 8));
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
    }**/

    public interface OrderCallbackListener {
        void moveOrderDetail();

        void moveShopDetail();

        void removeOrder();
    }

    public interface ReviewCallbackListener {
        void blockReview();

        void blockUser();
    }

    public static void copyClipboard(Context context, String str) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", str);
        clipboardManager.setPrimaryClip(clipData);
    }

    // Unicode
//    int unicode =0x1F607;
//    String emoji = getEmojiByUnicode(unicode);
//        binding.tvName.setText(emoji);
    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }

    public static void setLayoutSize(View view, int widthPixel, int heightPixel) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = widthPixel;
        layoutParams.height = heightPixel;
        view.setLayoutParams(layoutParams);
    }

    public static String reverseYn(String yn) {
        switch (yn) {
            case "Y":
                return "N";

            case "N":
                return "Y";
        }

        return yn;
    }

    public static String reverse10(String str10) {
        switch (str10) {
            case "1":
                return "0";

            case "0":
                return "1";
        }

        return str10;
    }

    public static double convertStringToDouble(String value) {
        return Double.parseDouble(value);
    }

    public static String convertDoubleToString(double value) {
        return String.valueOf(value);
    }

    public static void moveEditTextCursorToLast(EditText editText) {
        editText.setSelection(editText.getText().length());
    }

    public static String replaceThumbUrl(String url) {
        String replaceUrl = "";

        try {
            int slashLast = url.lastIndexOf("/");
            replaceUrl = url.substring(0, slashLast) + "/thumb" + url.substring(slashLast, url.length());
        } catch (Exception e) {

        }

        return replaceUrl;
    }

    public static String countDisplayFormat(int count) {
        int length = String.valueOf(count).length();

        if (length < 3) {
            return String.valueOf(count);
        } else {
            int cut = (int) Math.pow(10, length - 1);
            return addComma(count / cut * cut) + "+";
        }
    }

    public static String getTopActivityClassName(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String topActivityClassName = "";

        if (manager != null) {
            List<ActivityManager.AppTask> tasks = manager.getAppTasks();

            if (tasks != null && !tasks.isEmpty()) {
                topActivityClassName = tasks.get(0).getTaskInfo().topActivity.getClassName();
            }
        }

        return topActivityClassName;
    }

    public static String priceToText(int input) {
        int price = input;
        int price_10000 = price / 10000;
        int price_1000 = price % 10000 / 1000;
        int price_100 = price % 1000 / 100;
        int price_10 = price % 100 / 10;
        int price_remain = price % 10;

        String priceText = "";

        if (price_10000 > 0) {
            priceText += price_10000 + "만";
        }

        if (price_1000 > 0) {
            priceText += price_1000 + "천";
        }

        if (price_100 > 0) {
            priceText += price_100 + "백";
        }

        if (price_10 > 0) {
            priceText += price_10 + "십";
        }

        if (price_remain > 0) {
            priceText += price_remain;
        }

        return priceText + "원";
    }
}