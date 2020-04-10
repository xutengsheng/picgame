package com.xts.picgame.utils;

import com.xts.picgame.R;
import com.xts.picgame.common.Constant;
import com.xts.picgame.model.bean.Bean;

import java.util.ArrayList;
import java.util.Random;

public class RandomImageUtil2 {
    private volatile static RandomImageUtil2 sRandomImageUtil = null;
    ArrayList<Bean> apples = new ArrayList<>();
    ArrayList<Bean> cookies = new ArrayList<>();
    ArrayList<Bean> cats = new ArrayList<>();
    ArrayList<Bean> birds = new ArrayList<>();
    ArrayList<Bean> airplanes = new ArrayList<>();
    ArrayList<Bean> cars = new ArrayList<>();
    ArrayList<Bean> shoes = new ArrayList<>();
    ArrayList<Bean> candies = new ArrayList<>();
    ArrayList<Bean> chairs = new ArrayList<>();
    ArrayList<Bean> beds = new ArrayList<>();
    ArrayList<Bean> bikes = new ArrayList<>();
    ArrayList<Bean> balls = new ArrayList<>();
    ArrayList<Bean> cup = new ArrayList<>();
    ArrayList<Bean> spoons = new ArrayList<>();
    ArrayList<Bean> flowers = new ArrayList<>();
    ArrayList<ArrayList<Bean>> all = new ArrayList<>();
    ArrayList<String> strType = new ArrayList<>();
    private final Random mRandom;
    private final int mTotalSize;

    private RandomImageUtil2() {
        addAll();
        addApples();
        addCookies();
        addCats();
        addBirds();
        addAirplanes();
        addCars();
        addShoes();
        addCandies();
        addChairs();
        addBeds();
        addBikes();
        addBalls();
        addCup();
        addSpoons();
        addFlowers();
        mRandom = new Random();
        mTotalSize = all.size();
        initStrType();
    }

    private void initStrType() {
        strType.add(Constant.APPLE);
        strType.add(Constant.COOKIES);
        strType.add(Constant.CAT);
        strType.add(Constant.BIRD);
        strType.add(Constant.AIRPLANE);
        strType.add(Constant.CAR);
        strType.add(Constant.SHOE);
        strType.add(Constant.CANDY);
        strType.add(Constant.CHAIR);
        strType.add(Constant.BED);
        strType.add(Constant.BIKE);
        strType.add(Constant.BALL);
        strType.add(Constant.CUP);
        strType.add(Constant.SPOON);
        strType.add(Constant.FLOWER);
    }

    private void addFlowers() {
        flowers.add(new Bean(R.drawable.ic_floor_001, Constant.TYPE_FLOWERS));
        flowers.add(new Bean(R.drawable.ic_floor_002, Constant.TYPE_FLOWERS));
        flowers.add(new Bean(R.drawable.ic_floor_003, Constant.TYPE_FLOWERS));
        flowers.add(new Bean(R.drawable.ic_floor_004, Constant.TYPE_FLOWERS));
        flowers.add(new Bean(R.drawable.ic_floor_005, Constant.TYPE_FLOWERS));
        flowers.add(new Bean(R.drawable.ic_floor_006, Constant.TYPE_FLOWERS));

    }

    private void addSpoons() {
        spoons.add(new Bean(R.drawable.ic_spoon_001, Constant.TYPE_SPOONS));
        spoons.add(new Bean(R.drawable.ic_spoon_002, Constant.TYPE_SPOONS));
        spoons.add(new Bean(R.drawable.ic_spoon_003, Constant.TYPE_SPOONS));
        spoons.add(new Bean(R.drawable.ic_spoon_004, Constant.TYPE_SPOONS));
        spoons.add(new Bean(R.drawable.ic_spoon_005, Constant.TYPE_SPOONS));
        spoons.add(new Bean(R.drawable.ic_spoon_006, Constant.TYPE_SPOONS));

    }

    private void addCup() {
        cup.add(new Bean(R.drawable.ic_cup_001, Constant.TYPE_CUP));
        cup.add(new Bean(R.drawable.ic_cup_002, Constant.TYPE_CUP));
        cup.add(new Bean(R.drawable.ic_cup_003, Constant.TYPE_CUP));
        cup.add(new Bean(R.drawable.ic_cup_004, Constant.TYPE_CUP));
        cup.add(new Bean(R.drawable.ic_cup_005, Constant.TYPE_CUP));
        cup.add(new Bean(R.drawable.ic_cup_006, Constant.TYPE_CUP));

    }

    private void addBalls() {
        balls.add(new Bean(R.drawable.ic_ball_001, Constant.TYPE_BALLS));
        balls.add(new Bean(R.drawable.ic_ball_002, Constant.TYPE_BALLS));
        balls.add(new Bean(R.drawable.ic_ball_003, Constant.TYPE_BALLS));
        balls.add(new Bean(R.drawable.ic_ball_004, Constant.TYPE_BALLS));
        balls.add(new Bean(R.drawable.ic_ball_005, Constant.TYPE_BALLS));
        balls.add(new Bean(R.drawable.ic_ball_006, Constant.TYPE_BALLS));

    }

    private void addBikes() {
        bikes.add(new Bean(R.drawable.ic_bike_001, Constant.TYPE_BIKES));
        bikes.add(new Bean(R.drawable.ic_bike_002, Constant.TYPE_BIKES));
        bikes.add(new Bean(R.drawable.ic_bike_003, Constant.TYPE_BIKES));
        bikes.add(new Bean(R.drawable.ic_bike_004, Constant.TYPE_BIKES));
        bikes.add(new Bean(R.drawable.ic_bike_005, Constant.TYPE_BIKES));
        bikes.add(new Bean(R.drawable.ic_bike_006, Constant.TYPE_BIKES));

    }

    private void addBeds() {
        beds.add(new Bean(R.drawable.ic_bed_001, Constant.TYPE_BEDS));
        beds.add(new Bean(R.drawable.ic_bed_002, Constant.TYPE_BEDS));
        beds.add(new Bean(R.drawable.ic_bed_003, Constant.TYPE_BEDS));
        beds.add(new Bean(R.drawable.ic_bed_004, Constant.TYPE_BEDS));
        beds.add(new Bean(R.drawable.ic_bed_005, Constant.TYPE_BEDS));
        beds.add(new Bean(R.drawable.ic_bed_006, Constant.TYPE_BEDS));
    }

    private void addChairs() {
        chairs.add(new Bean(R.drawable.ic_chair_001, Constant.TYPE_CHAIRS));
        chairs.add(new Bean(R.drawable.ic_chair_002, Constant.TYPE_CHAIRS));
        chairs.add(new Bean(R.drawable.ic_chair_003, Constant.TYPE_CHAIRS));
        chairs.add(new Bean(R.drawable.ic_chair_004, Constant.TYPE_CHAIRS));
        chairs.add(new Bean(R.drawable.ic_chair_005, Constant.TYPE_CHAIRS));
        chairs.add(new Bean(R.drawable.ic_chair_006, Constant.TYPE_CHAIRS));
    }

    private void addCandies() {
        candies.add(new Bean(R.drawable.ic_sweets_001, Constant.TYPE_CANDIES));
        candies.add(new Bean(R.drawable.ic_sweets_002, Constant.TYPE_CANDIES));
        candies.add(new Bean(R.drawable.ic_sweets_003, Constant.TYPE_CANDIES));
        candies.add(new Bean(R.drawable.ic_sweets_004, Constant.TYPE_CANDIES));
        candies.add(new Bean(R.drawable.ic_sweets_005, Constant.TYPE_CANDIES));
        candies.add(new Bean(R.drawable.ic_sweets_006, Constant.TYPE_CANDIES));
    }

    private void addShoes() {
        shoes.add(new Bean(R.drawable.ic_shoe_001, Constant.TYPE_SHOES));
        shoes.add(new Bean(R.drawable.ic_shoe_002, Constant.TYPE_SHOES));
        shoes.add(new Bean(R.drawable.ic_shoe_003, Constant.TYPE_SHOES));
        shoes.add(new Bean(R.drawable.ic_shoe_004, Constant.TYPE_SHOES));
        shoes.add(new Bean(R.drawable.ic_shoe_005, Constant.TYPE_SHOES));
        shoes.add(new Bean(R.drawable.ic_shoe_006, Constant.TYPE_SHOES));
    }

    private void addCars() {
        cars.add(new Bean(R.drawable.ic_car_001, Constant.TYPE_CARS));
        cars.add(new Bean(R.drawable.ic_car_002, Constant.TYPE_CARS));
        cars.add(new Bean(R.drawable.ic_car_003, Constant.TYPE_CARS));
        cars.add(new Bean(R.drawable.ic_car_004, Constant.TYPE_CARS));
        cars.add(new Bean(R.drawable.ic_car_005, Constant.TYPE_CARS));
        cars.add(new Bean(R.drawable.ic_car_006, Constant.TYPE_CARS));
    }

    private void addAirplanes() {
        airplanes.add(new Bean(R.drawable.ic_airplane_001, Constant.TYPE_AIRPLANES));
        airplanes.add(new Bean(R.drawable.ic_airplane_002, Constant.TYPE_AIRPLANES));
        airplanes.add(new Bean(R.drawable.ic_airplane_003, Constant.TYPE_AIRPLANES));
        airplanes.add(new Bean(R.drawable.ic_airplane_004, Constant.TYPE_AIRPLANES));
        airplanes.add(new Bean(R.drawable.ic_airplane_005, Constant.TYPE_AIRPLANES));
        airplanes.add(new Bean(R.drawable.ic_airplane_006, Constant.TYPE_AIRPLANES));
    }

    private void addBirds() {
        birds.add(new Bean(R.drawable.ic_bird_001, Constant.TYPE_BIRDS));
        birds.add(new Bean(R.drawable.ic_bird_002, Constant.TYPE_BIRDS));
        birds.add(new Bean(R.drawable.ic_bird_003, Constant.TYPE_BIRDS));
        birds.add(new Bean(R.drawable.ic_bird_004, Constant.TYPE_BIRDS));
        birds.add(new Bean(R.drawable.ic_bird_005, Constant.TYPE_BIRDS));
        birds.add(new Bean(R.drawable.ic_bird_006, Constant.TYPE_BIRDS));
    }

    private void addCats() {
        cats.add(new Bean(R.drawable.ic_cat_001, Constant.TYPE_CATS));
        cats.add(new Bean(R.drawable.ic_cat_002, Constant.TYPE_CATS));
        cats.add(new Bean(R.drawable.ic_cat_003, Constant.TYPE_CATS));
        cats.add(new Bean(R.drawable.ic_cat_004, Constant.TYPE_CATS));
        cats.add(new Bean(R.drawable.ic_cat_005, Constant.TYPE_CATS));
        cats.add(new Bean(R.drawable.ic_cat_006, Constant.TYPE_CATS));
    }

    private void addCookies() {
        cookies.add(new Bean(R.drawable.ic_cookie_001, Constant.TYPE_COOKIES));
        cookies.add(new Bean(R.drawable.ic_cookie_002, Constant.TYPE_COOKIES));
        cookies.add(new Bean(R.drawable.ic_cookie_003, Constant.TYPE_COOKIES));
        cookies.add(new Bean(R.drawable.ic_cookie_004, Constant.TYPE_COOKIES));
        cookies.add(new Bean(R.drawable.ic_cookie_005, Constant.TYPE_COOKIES));
        cookies.add(new Bean(R.drawable.ic_cookie_006, Constant.TYPE_COOKIES));
    }

    private void addApples() {
        apples.add(new Bean(R.drawable.ic_apple_001, Constant.TYPE_APPLE));
        apples.add(new Bean(R.drawable.ic_apple_002, Constant.TYPE_APPLE));
        apples.add(new Bean(R.drawable.ic_apple_003, Constant.TYPE_APPLE));
        apples.add(new Bean(R.drawable.ic_apple_004, Constant.TYPE_APPLE));
        apples.add(new Bean(R.drawable.ic_apple_005, Constant.TYPE_APPLE));
        apples.add(new Bean(R.drawable.ic_apple_006, Constant.TYPE_APPLE));
    }

    private void addAll() {
        all.add(apples);
        all.add(cookies);
        all.add(cats);
        all.add(birds);
        all.add(airplanes);
        all.add(cars);
        all.add(shoes);
        all.add(candies);
        all.add(chairs);
        all.add(beds);
        all.add(bikes);
        all.add(balls);
        all.add(cup);
        all.add(spoons);
        all.add(flowers);
    }

    public static RandomImageUtil2 getInstance() {
        if (sRandomImageUtil == null) {
            synchronized (RandomImageUtil2.class) {
                if (sRandomImageUtil == null) {
                    sRandomImageUtil = new RandomImageUtil2();
                }
            }
        }

        return sRandomImageUtil;
    }


    /**
     * 生成一个随机图片,
     *
     * @param type 制定图片的类型
     * @param flag true,与type种类一样的图片,false,与type种类不一样的图片
     * @return
     */
    public Bean random(int type, boolean flag) {
        type -=1;
        if (flag) {
            int index = getRandomNumber(all.get(type).size());
            return all.get(type).get(index);
        } else {
            int typeNumber = getRandomNumber(mTotalSize);
            while (typeNumber == type) {
                typeNumber = getRandomNumber(mTotalSize);
            }

            int randomNumber = getRandomNumber(all.get(typeNumber).size());
            return all.get(typeNumber).get(randomNumber);
        }
    }


    /**
     * //获取一个与传递的图片不一样的同类型图片
     *
     * @return
     */
    public Bean random(Bean bean) {
        int i = bean.type - 1;
        int size = all.get(i).size();
        int randomNumber = getRandomNumber(size);
        Bean ran = all.get(i).get(randomNumber);
        while (ran.resId == bean.resId) {
            randomNumber = getRandomNumber(size);
            ran = all.get(i).get(randomNumber);
        }

        return ran;
    }

    /**
     * 随机获取一个图片
     *
     * @return
     */
    public Bean random() {
        int type = mRandom.nextInt(mTotalSize);
        int index = mRandom.nextInt(all.get(type).size());
        return all.get(type).get(index);
    }

    /**
     * 生成一个与传递图片不一样的图片
     *
     * @param bean
     * @return
     */
    public Bean randomNotEqual(Bean bean) {
        int type = mRandom.nextInt(mTotalSize);
        int index = mRandom.nextInt(all.get(type).size());
        Bean bean1 = all.get(type).get(index);
        if (bean1.resId != bean.resId) {
            return bean1;
        }else {
            return randomNotEqual(bean);
        }
    }


    public int getRandomNumber(int num) {
        return mRandom.nextInt(num);
    }

    /**
     * 根据int类型获取对应类型的string
     *
     * @param type
     * @return
     */
    public String getTypeString(int type) {
        return strType.get(type - 1);
    }

    /**
     * 获取随机的类型,string
     * @return
     */
    public String getRandomTypeString(){
        int randomNumber = getRandomNumber(mTotalSize);
        return strType.get(randomNumber);
    }

    /**
     * 获取随机的类型,int
     * @return
     */
    public int getRandomTypeInt(){
        int randomNumber = getRandomNumber(mTotalSize);
        return randomNumber+1;
    }

}
