package com.xts.picgame.utils;

import android.util.SparseArray;

import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.db.DataBeanDao;
import com.xts.picgame.model.bean.DataBean;
import com.xts.picgame.ui.settings.RlvChoosePicAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomImageUtil {
    SparseArray<String> strType = new SparseArray<>();
    SparseArray<ArrayList<DataBean>> all = new SparseArray<>();
    private final Random mRandom;
    private final int mTotalSize;
    private DataBeanDao mDataBeanDao;
    private ArrayList<Integer> mTypeList = new ArrayList<>();

    /**
     * 根据类型处理数据
     * @param type 在RlvChoosePicAdapter中
     */
    public RandomImageUtil(int type) {
        mDataBeanDao = BaseApp.sBaseApp.getDaoSession().getDataBeanDao();
        addAll(type);
        mRandom = new Random();
        mTotalSize = all.size();
    }

    //处理数据,将数据库数据处理成嵌套的集合
    private void addAll(int type) {
        List<DataBean> list = null;
        switch (type){
            case RlvChoosePicAdapter.TYPE_IDENTIFY:
                list = mDataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Identify.eq(true)).list();
                break;
            case RlvChoosePicAdapter.TYPE_EXPRESSIVE:
                list = mDataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Expressive.eq(true)).list();
                break;
            case RlvChoosePicAdapter.TYPE_MATCH:
                list = mDataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Match.eq(true)).list();
                break;
            case RlvChoosePicAdapter.TYPE_SIMILAR:
                list = mDataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Similar.eq(true)).list();
                break;
            case RlvChoosePicAdapter.TYPE_SORT:
                list = mDataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Sort.eq(true)).list();
                break;
            case RlvChoosePicAdapter.TYPE_RECEPTIVE:
                list = mDataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Receptive.eq(true)).list();
                break;
        }

        //分类,里面放置的是各类的集合

        if (list != null && list.size()>0){
            for (int i = 0; i < list.size(); i++) {
                DataBean dataBean = list.get(i);

                int typeid = dataBean.getTypeid();
                if (!mTypeList.contains(typeid)){
                    mTypeList.add(typeid);
                    all.append(typeid,new ArrayList());
                    strType.append(typeid,dataBean.getTname());
                }

                //将数据添加到集合中
                all.get(typeid).add(dataBean);
            }

        }
    }

    /**
     * 生成一个随机图片,
     *
     * @param type 制定图片的类型
     * @param flag true,与type种类一样的图片,false,与type种类不一样的图片
     * @return
     */
    public DataBean random(int type, boolean flag) {
        if (flag) {
            int index = getRandomNumber(all.get(type).size());
            return all.get(type).get(index);
        } else {
            int typeNumber = getRandomNumber(mTotalSize);
            while (mTypeList.get(typeNumber) == type) {
                typeNumber = getRandomNumber(mTotalSize);
            }

            int randomNumber = getRandomNumber(all.get(mTypeList.get(typeNumber)).size());
            return all.get(mTypeList.get(typeNumber)).get(randomNumber);
        }
    }


    /**
     * //获取一个与传递的图片不一样的同类型图片
     *
     * @return
     */
    public DataBean random(DataBean bean) {
        int size = all.get(bean.getTypeid()).size();
        int randomNumber = getRandomNumber(size);
        DataBean ran = all.get(bean.getTypeid()).get(randomNumber);
        while (ran.getImageid() == bean.getImageid()) {
            randomNumber = getRandomNumber(size);
            ran = all.get(bean.getTypeid()).get(randomNumber);
        }

        return ran;
    }

    /**
     * 随机获取一个图片
     *
     * @return
     */
    public DataBean random() {
        int type = mRandom.nextInt(mTotalSize);
        int index = mRandom.nextInt(all.get(mTypeList.get(type)).size());
        return all.get(mTypeList.get(type)).get(index);
    }

    /**
     * 生成一个与传递图片不一样的图片
     *
     * @param bean
     * @return
     */
    public DataBean randomNotEqual(DataBean bean) {
        int type = mRandom.nextInt(mTotalSize);
        int index = mRandom.nextInt(all.get(mTypeList.get(type)).size());
        DataBean bean1 = all.get(mTypeList.get(type)).get(index);
        if (bean1.getImageid() != bean.getImageid()) {
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
        return strType.get(type);
    }

    /**
     * 获取随机的类型,string
     * @return
     */
    public String getRandomTypeString(){
        int randomNumber = getRandomNumber(mTotalSize);
        return strType.get(mTypeList.get(randomNumber));
    }

    /**
     * 获取随机的类型,int
     * @return
     */
    public int getRandomTypeInt(){
        int randomNumber = getRandomNumber(mTotalSize);
        return mTypeList.get(randomNumber);
    }

    public SparseArray<ArrayList<DataBean>> getAll(){
        return all;
    }
}
