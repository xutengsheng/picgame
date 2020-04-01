package com.xts.picgame.model;

import android.text.TextUtils;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Request;

public abstract class CallFactoryProxy implements Call.Factory {

    public static final String HEAD_URL = "head_url"; // 通过消息头确定当前的基础地址

    private final Call.Factory delegate;
    public static final String[] URLS = new String[]{"https://www.wanandroid.com/","https://cdwan.cn/api/"};

    public CallFactoryProxy(Call.Factory delegate){

        this.delegate = delegate;
    }

    @Override
    public Call newCall(Request request) {
        String url = request.header(HEAD_URL); //读取当前请求的头消息
        if(!TextUtils.isEmpty(url)){
            HttpUrl nowHttpUrl = getNowUrl(url,request);
            if(nowHttpUrl != null){
                Request newRequest = request.newBuilder().url(nowHttpUrl).build();
                return delegate.newCall(newRequest);
            }
        }
        return delegate.newCall(request);
    }

    protected abstract HttpUrl getNowUrl(String curUrl,Request request);
}
