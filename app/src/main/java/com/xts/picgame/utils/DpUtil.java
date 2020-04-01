package com.xts.picgame.utils;


import com.xts.picgame.apps.BaseApp;

public class DpUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        final float scale = BaseApp.sBaseApp.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        final float scale = BaseApp.sBaseApp.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        float scaledDensity = BaseApp.sBaseApp.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * scaledDensity + 0.5F);
    }

    public static int px2sp(float pxValue) {
        float scaledDensity = BaseApp.sBaseApp.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue / scaledDensity + 0.5F);
    }
}
