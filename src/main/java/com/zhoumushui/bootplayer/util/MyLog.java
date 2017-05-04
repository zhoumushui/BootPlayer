package com.zhoumushui.bootplayer.util;

import android.util.Log;

public class MyLog {

    private static final boolean isDebug = true;

    private static final String TAG_DEFAULT = "AZ";

    private static String MODULE = "[AutoPlayer]";

    public static void e(String log) {
        if (isDebug)
            Log.e(TAG_DEFAULT, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

    public static void e(String tag, String log) {
        if (isDebug)
            Log.e(tag, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

    public static void v(String log) {
        if (isDebug)
            Log.v(TAG_DEFAULT, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

    public static void v(String tag, String log) {
        if (isDebug)
            Log.v(tag, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

    public static void d(String log) {
        if (isDebug)
            Log.d(TAG_DEFAULT, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

    public static void d(String tag, String log) {
        if (isDebug)
            Log.d(tag, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

    public static void i(String log) {
        if (isDebug)
            Log.i(TAG_DEFAULT, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

    public static void i(String tag, String log) {
        if (isDebug)
            Log.i(tag, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

    public static void w(String log) {
        if (isDebug)
            Log.w(TAG_DEFAULT, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

    public static void w(String tag, String log) {
        if (isDebug)
            Log.w(tag, DateUtil.getTimeStr("HH:mm:ss") + MODULE + log);
    }

}
