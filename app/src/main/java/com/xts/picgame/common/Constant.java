package com.xts.picgame.common;


import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;

import java.io.File;

public interface Constant {
    boolean DEBUG = true;

    //String PATH_DATA = BaseApp.sBaseApp.getCacheDir().getAbsolutePath();
    String PATH_DATA = BaseApp.sBaseApp.getExternalCacheDir().getAbsolutePath();

    String VOICE_PATH = PATH_DATA + "/voice/";

    //商城的基础地址
    String BASE_SHOP_URL = "http://47.110.151.50:8080/";

    String TOKEN = "token";
    int SUCCESS_CODE = 200;

    int TYPE_APPLE = 1;
    int TYPE_COOKIES = 2;
    int TYPE_CATS = 3;
    int TYPE_BIRDS = 4;
    int TYPE_AIRPLANES = 5;
    int TYPE_CARS = 6;
    int TYPE_SHOES = 7;
    int TYPE_CANDIES = 8;
    int TYPE_CHAIRS = 9;
    int TYPE_BEDS = 10;
    int TYPE_BIKES = 11;
    int TYPE_BALLS = 12;
    int TYPE_CUP = 13;
    int TYPE_SPOONS = 14;
    int TYPE_FLOWERS = 15;
    String APPLE = BaseApp.getRes().getString(R.string.apple);
    String COOKIES = BaseApp.getRes().getString(R.string.cookies);
    String CAT = BaseApp.getRes().getString(R.string.cat);
    String BIRD = BaseApp.getRes().getString(R.string.bird);
    String AIRPLANE = BaseApp.getRes().getString(R.string.airplane);
    String CAR = BaseApp.getRes().getString(R.string.car);
    String SHOE = BaseApp.getRes().getString(R.string.shoe);
    String CANDY = BaseApp.getRes().getString(R.string.candy);
    String CHAIR = BaseApp.getRes().getString(R.string.chair);
    String BED = BaseApp.getRes().getString(R.string.bed);
    String BIKE = BaseApp.getRes().getString(R.string.bike);
    String BALL = BaseApp.getRes().getString(R.string.ball);
    String CUP = BaseApp.getRes().getString(R.string.cup);
    String SPOON = BaseApp.getRes().getString(R.string.spoon);
    String FLOWER = BaseApp.getRes().getString(R.string.flower);

    //游戏2图片数量,最大为6,最小为2,默认为4
    String EXPRESSIVE_PIC_NUMBER = "expressivePicNumber";
    //游戏3图片数量,最大为6,最小为2,默认为4
    String MATCH_PIC_NUMBER = "identicalPicNumber";
    //游戏4图片数量,最大为6,最小为2,默认为4
    String SIMILAR_PIC_NUMBER = "similarPicNumber";
    //游戏3错误重复的次数,默认是1
    String MATCH_PIC_REPEAT = "identicalRepeat";
    //游戏4错误重复的次数,默认是1
    String SIMILAR_PIC_REPEAT = "similarRepeat";

    String DATA = "data";
    String GAME_TYPE = "gameType";
    String url = "";
    //post
    //http://47.110.151.50:8080/games/img/getImgListByType?typeid=1

}
