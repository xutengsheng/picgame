package com.xts.picgame.utils;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import androidx.annotation.IdRes;

import com.xts.picgame.apps.BaseApp;

import java.io.File;
import java.io.IOException;

public class MediaPlayerUtil implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private static volatile MediaPlayerUtil sMediaPlayerUtil = null;
    private final MediaPlayer mMediaPlayer;

    private MediaPlayerUtil(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }
    public static MediaPlayerUtil getInstance(){
        if (sMediaPlayerUtil == null){
            synchronized (MediaPlayerUtil.class){
                if (sMediaPlayerUtil == null){
                    sMediaPlayerUtil = new MediaPlayerUtil();
                }
            }
        }
        return sMediaPlayerUtil;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mMediaPlayer.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        LogUtils.print("error:"+what+",extra:"+extra);
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    /**
     * 设置播放资源
     * @param url ,网络路径
     */
    public void setData(String url){
        String path = DownLoadUtil.getPath(url);
        File file = new File(path);
        if (!file.exists()){
            //播放网络音频
            path = url;

            //下载
            DownLoadUtil.downVoice(url, new DownLoadUtil.ResultCallBack() {
                @Override
                public void onFail(String message) {

                }

                @Override
                public void onProgress(long progress, long max) {

                }

                @Override
                public void onSuccess(String path) {
                    LogUtils.print("voice path:" + path);
                }
            });
        }
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            LogUtils.print(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 播放本地raw中的资源
     * @param resId
     */
    public void setData( int resId){
        try {
            mMediaPlayer.reset();
            AssetFileDescriptor afd = BaseApp.getRes().openRawResourceFd(resId);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            LogUtils.print(e.toString());
            e.printStackTrace();
        }
    }
}
