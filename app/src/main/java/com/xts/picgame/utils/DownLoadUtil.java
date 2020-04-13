package com.xts.picgame.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.xts.picgame.common.Constant;
import com.xts.picgame.model.HttpManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class DownLoadUtil {
    private static final String TAG = "DownLoadUtil";

    /**
     * 保存文件
     *
     * @param inputStream
     * @param path
     * @param callBack
     * @param max
     */
    private static void saveFiles(InputStream inputStream, String path, ResultCallBack callBack, long max) {
        File file = new File(path);
        if (!file.getParentFile().exists()){
            boolean mkdir = file.getParentFile().mkdirs();
        }
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int len;
        byte[] bytes = new byte[4096];
        //读写的进度
        long count = 0;
        try {
            //输出流
            FileOutputStream fos = new FileOutputStream(file);
            while ((len = inputStream.read(bytes)) != -1) {
                fos.write(bytes, 0, len);
                count += len;
                //传递当前读写的进度
                if (callBack != null) {
                    callBack.onProgress(count, max);
                }
                Log.d(TAG, "progress: " + count);
            }
            fos.close();
            inputStream.close();
            //完成写入
            if (callBack != null) {
                callBack.onSuccess(path);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CheckResult")
    public static void downVoice(final String urlPath, final ResultCallBack callBack) {
        HttpManager.getInstance()
                .getApiService()
                .downloadVoice(urlPath)
                .compose(RxUtils.<ResponseBody>rxScheduler())
                .subscribeWith(new ResourceSubscriber<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        InputStream inputStream = responseBody.byteStream();
                        String path = getPath(urlPath);
                        saveFiles(inputStream, path, callBack, responseBody.contentLength());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * //获取保存语音的路径
     * @param urlPath 语音链接
     * @return
     */
    public static String getPath(String urlPath) {
        String path = SystemUtils.md5(urlPath) + ".mp3";
        return Constant.VOICE_PATH + path;
    }

    /**
     * 结果的回调
     */
    public interface ResultCallBack {
        void onFail(String message);

        void onProgress(long progress, long max);

        void onSuccess(String path);
    }
}
