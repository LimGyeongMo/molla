package com.example.daegurobus.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class LogUtil {
    private static boolean IS_DEBUG_MODE = false;
    private static String TAG = "LogUtil";
    private static int MAX_LOG_LENGTH = 2000;

    private static final int TYPE_D = 1;
    private static final int TYPE_E = 2;
    private static final int TYPE_I = 3;
    private static final int TYPE_V = 4;
    private static final int TYPE_W = 5;
    private static final int TYPE_WTF = 6;

    /** Block Create Instance **/
    private LogUtil() {
    }

    public static void init(Context context, String tag) {
        PackageManager packageManager = context.getPackageManager();

        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            IS_DEBUG_MODE = (0 != (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (tag != null) {
            TAG = tag;
        }
    }

    public static void setTag(String tag) {
        TAG = tag;
    }

    public static String getTag() {
        return TAG;
    }

    public static void setMaxLogLength(int maxLogLength) {
        MAX_LOG_LENGTH = maxLogLength;
    }

    public static int getMaxLogLength() {
        return MAX_LOG_LENGTH;
    }

    /** Debug **/
    public static void d(String message) {
        if (IS_DEBUG_MODE) {
            print(TYPE_D, message);
        }
    }

    /** Error **/
    public static void e(String message) {
        if (IS_DEBUG_MODE) {
            print(TYPE_E, message);
        }
    }

    /** Information **/
    public static void i(String message) {
        if (IS_DEBUG_MODE) {
            print(TYPE_I, message);
        }
    }

    /** Verbose **/
    public static void v(String message) {
        if (IS_DEBUG_MODE) {
            print(TYPE_V, message);
        }
    }

    /** Warning **/
    public static void w(String message) {
        if (IS_DEBUG_MODE) {
            print(TYPE_W, message);
        }
    }

    /** What The Fuck **/
    public static void wtf(String message) {
        if (IS_DEBUG_MODE) {
            print(TYPE_WTF, message);
        }
    }

    /** Log Print **/
    private static void print(int logType, String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(ste.getFileName().replace(".java", ""));
        sb.append("::");
        sb.append(ste.getMethodName());
        sb.append("] ");
        sb.append(message);

        String convertedMessage = sb.toString();
        int convertedMessageLength = convertedMessage.length();
        int printCount = (convertedMessageLength / MAX_LOG_LENGTH);

        if (convertedMessageLength - (MAX_LOG_LENGTH * printCount) > 0) {
            printCount += 1;
        }

        for (int i = 0; i < printCount; i++) {
            int start = (i * MAX_LOG_LENGTH);
            int end = ((i + 1) * MAX_LOG_LENGTH);

            if(end > convertedMessageLength){
                end = convertedMessageLength;
            }

            switch (logType) {
                case TYPE_D:
                    android.util.Log.d(TAG, convertedMessage.substring(start, end));
                    break;

                case TYPE_E:
                    android.util.Log.e(TAG, convertedMessage.substring(start, end));
                    break;

                case TYPE_I:
                    android.util.Log.i(TAG, convertedMessage.substring(start, end));
                    break;

                case TYPE_V:
                    android.util.Log.v(TAG, convertedMessage.substring(start, end));
                    break;

                case TYPE_W:
                    android.util.Log.w(TAG, convertedMessage.substring(start, end));
                    break;

                case TYPE_WTF:
                    android.util.Log.wtf(TAG, convertedMessage.substring(start, end));
                    break;

                default:
                    android.util.Log.e(TAG, "Log type error!");
                    return;
            }
        }
    }
}

