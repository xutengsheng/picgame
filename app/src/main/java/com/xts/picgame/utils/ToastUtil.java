package com.xts.picgame.utils;

import android.widget.Toast;

import com.xts.picgame.apps.BaseApp;


public class ToastUtil {
    public static void showToast(String msg){
        Toast.makeText(BaseApp.sBaseApp,msg,Toast.LENGTH_SHORT).show();
    }
}
