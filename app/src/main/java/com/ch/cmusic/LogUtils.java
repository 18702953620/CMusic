package com.ch.cmusic;

import android.text.TextUtils;
import android.util.Log;

/**
 * 作者： ch
 * 时间： 2019/3/22 0022-下午 4:06
 * 描述：
 * 来源：
 */

public class LogUtils {

    private static String TAG = "LogUtils";


    public static void log(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Log.e(TAG, msg);
    }

    public static void log(String tag, String msg) {
        if (TextUtils.isEmpty(msg) || TextUtils.isEmpty(tag)) {
            return;
        }
        Log.e(tag, msg);
    }
}
