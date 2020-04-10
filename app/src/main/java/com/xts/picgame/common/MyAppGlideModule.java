package com.xts.picgame.common;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //sdcard/Android/data/包名/cache/
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(
                context,"images",1024*1024*1024));
    }
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
    }
}