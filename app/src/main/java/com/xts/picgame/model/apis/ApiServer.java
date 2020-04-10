package com.xts.picgame.model.apis;



import com.xts.picgame.model.bean.NetBean;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiServer {

    @GET("games/img/getImgList")
    Flowable<NetBean> getImages();

    @GET
    Flowable<ResponseBody> downloadVoice(@Url String urlPath);
}
