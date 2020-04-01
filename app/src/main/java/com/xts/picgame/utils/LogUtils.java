package com.xts.picgame.utils;

import android.util.Log;

import com.xts.picgame.common.Constant;


public class LogUtils {
    public static void print(String msg) {
        if (Constant.DEBUG) {
            System.out.println(msg);
        }
    }

    public static void logD(String tag, String msg) {
        if (Constant.DEBUG) {
            Log.d(tag, msg);
        }
    }

}
