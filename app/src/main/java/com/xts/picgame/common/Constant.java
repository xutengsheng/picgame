package com.xts.picgame.common;


import com.xts.picgame.apps.BaseApp;

import java.io.File;

public interface Constant {
    boolean DEBUG = true;

    String PATH_DATA = BaseApp.sBaseApp.getCacheDir().getAbsolutePath() + File.separator + "data";

    String PATH_CACHE = PATH_DATA + "/shopapp";

    //商城的基础地址
    String BASE_SHOP_URL = "https://cdwan.cn/api/";

    String TOKEN = "token";
    int SUCCESS_CODE = 0;

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
    String APPLE = "apple";
    String COOKIES = "cookies";
    String CAT = "cat";
    String BIRD = "bird";
    String AIRPLANE = "airplane";
    String CAR = "car";
    String SHOE = "shoe";
    String CANDY = "candy";
    String CHAIR = "chair";
    String BED = "bed";
    String BIKE = "bike";
    String BALL = "ball";
    String CUP = "cup";
    String SPOON = "spoon";
    String FLOWER = "flower";

    //游戏2图片数量,最大为6,最小为2,默认为4
    String GAME2_PIC_NUMBER = "game2PicNumber";
    String GAME3_PIC_NUMBER = "game3PicNumber";
    //错误重复的次数,默认是1
    String GAME3_PIC_REPEAT = "game3Repeat";

    String DATA = "data";
}
