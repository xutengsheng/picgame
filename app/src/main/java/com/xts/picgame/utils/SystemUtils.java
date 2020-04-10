package com.xts.picgame.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;

import com.xts.picgame.apps.BaseApp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class SystemUtils {

    /**
     * 检查是否有网络
     * @return
     */
    public static boolean checkNetWork(){
        ConnectivityManager manager = (ConnectivityManager) BaseApp.sBaseApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        return manager.getActiveNetworkInfo() != null;
    }

    /**
     * 当前是否是wifi链接
     * @return
     */
    public static boolean isWifiConnected(){
        ConnectivityManager manager = (ConnectivityManager) BaseApp.sBaseApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info != null;
    }

    /**
     * 检查手机（4,3,2）G是否链接
     */
    public static boolean isMobileNetworkConnected(){
        ConnectivityManager manager = (ConnectivityManager) BaseApp.sBaseApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return info != null;
    }

    public static long getSystemTime(){
        return System.currentTimeMillis();
    }

    /**
     * 获取屏幕的dpi
     * @param at
     * @return
     */
    public static int getDpi(Activity at){
        DisplayMetrics dm = new DisplayMetrics();
        at.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }


    /**
     * 获取包名
     * @param context
     * @return
     */
    public static String getPgName(Context context){
        return context.getPackageName();
    }

    /**
     * 获取版本号
     * @param context
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public static Long getVersionCode(Context context, String pg){
        PackageInfo pgInfo = null;
        try {
            pgInfo = context.getPackageManager().getPackageInfo(pg,0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return Long.valueOf(pgInfo.versionCode);
        }else{
            return pgInfo.getLongVersionCode();
        }
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        //加盐加密,可以不加,盐可以是任何字符串
        String salt = "我是盐";
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string+salt).getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            //转换为大写字母
            return result.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
