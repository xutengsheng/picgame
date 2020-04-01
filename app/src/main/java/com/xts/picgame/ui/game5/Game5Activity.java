package com.xts.picgame.ui.game5;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.common.CustomImageView;
import com.xts.picgame.model.bean.Bean;
import com.xts.picgame.model.bean.LocationBean;
import com.xts.picgame.ui.game2.RlvGame2Adapter;
import com.xts.picgame.utils.LogUtils;
import com.xts.picgame.utils.RandomImageUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Game5Activity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_result)
    TextView mTvResult;
    @BindView(R.id.rlv1)
    RecyclerView mRlv1;
    @BindView(R.id.rlv2)
    RecyclerView mRlv2;
    @BindView(R.id.iv_target)
    CustomImageView mIvTarget;
    @BindView(R.id.btn_left)
    Button mBtnLeft;
    @BindView(R.id.btn_right)
    Button mBtnRight;
    private ArrayList<Bean> mList1;
    private ArrayList<Bean> mList2;
    private RlvGame2Adapter mAdapter1;
    private RlvGame2Adapter mAdapter2;
    private LocationBean mRlv1Location;
    private LocationBean mRlv2Location;
    private int mType1;
    private int mType2;
    private Bean mRandom;
    private Disposable mDis;

    private String mGoodJob = BaseApp.getRes().getString(R.string.good_job);
    private String mTryAgain = BaseApp.getRes().getString(R.string.try_again);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game5);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mList1 = new ArrayList<>();
        mList2 = new ArrayList<>();

        mRlv1.setLayoutManager(new GridLayoutManager(this, 2));
        mRlv2.setLayoutManager(new GridLayoutManager(this, 2));


        mAdapter1 = new RlvGame2Adapter(R.layout.item_game3, mList1);
        mAdapter2 = new RlvGame2Adapter(R.layout.item_game3, mList2);
        mAdapter1.bindToRecyclerView(mRlv1);
        mAdapter2.bindToRecyclerView(mRlv2);

        initGame();
        //控件绘制完成监听
        mRlv1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取相对于屏幕的view的矩形视图区域
                mRlv1.getGlobalVisibleRect(rect);
                LogUtils.print("rect left:" + rect.left + ",top:" + rect.top +
                        ",right:" + rect.right + ",bottom:" + rect.bottom);
                mRlv1Location = new LocationBean(1, rect.left, rect.top, rect.right, rect.bottom);

                //可能多次触发,拿到后移除监听
                mRlv1.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        //控件绘制完成监听
        mRlv2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取相对于屏幕的view的矩形视图区域
                mRlv2.getGlobalVisibleRect(rect);
                LogUtils.print("rect left:" + rect.left + ",top:" + rect.top +
                        ",right:" + rect.right + ",bottom:" + rect.bottom);
                mRlv2Location = new LocationBean(2, rect.left, rect.top, rect.right, rect.bottom);

                //可能多次触发,拿到后移除监听
                mRlv2.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        mIvTarget.setOnMoveListener(new CustomImageView.OnMoveListener() {
            @Override
            public void onMove(int l, int t, int r, int b) {

                //判断目标图片和列表图片哪个重叠
                //1.判断的时候只要判断目标图片四个角和列表图片左上角的距离即可,小于某个值表示重叠
                //2.不满足条件1,但是满足,target左上角在list图片右下角
                // 且target右下角在list图片左上角(target图片较小,list图片可包裹target图片)
                //3.不满足条件1,2,满足list图片左上角在target图片左上角的右下角,
                // 且list图片右下角在target图片右下角的左上角(list图片较小,target可以完全包裹list图片)

                LocationBean locationBean;
                if (l < BaseApp.mWidthPixels / 2 - (r - l)) {
                    //靠左
                    locationBean = mRlv1Location;
                } else {
                    //靠右
                    locationBean = mRlv2Location;
                }
                int dl = Math.abs(locationBean.l - l);
                int dt = Math.abs(locationBean.t - t);
                int dr = Math.abs(locationBean.r - r);
                int db = Math.abs(locationBean.b - b);

                boolean b1 = (dt < 20 && dl < 20)
                        || (dl < 20 && db < 20)
                        || (dr < 20 && dt < 20)
                        || (dr < 20 && db < 20);
                boolean b2 = l > locationBean.l && t > locationBean.t &&
                        locationBean.r > r && locationBean.b > b;
                boolean b3 = locationBean.l > l && locationBean.t > t &&
                        r > locationBean.r && b > locationBean.b;

                LogUtils.print("b1:" + b1 + ",b2:" + b2 + ",b3:" + b3);

                if (b1 || b2 || b3) {
                    check(locationBean.index);
                }


            }
        });
    }

    /**
     * //校验传递图片和目标位置种类是否一致
     *
     * @param index 目标位置,1,左边,2右边
     */
    private void check(int index) {
        int type;
        if (index == 1) {
            type = mType1;
        } else {
            type = mType2;
        }
        if (mRandom.type == type) {
            //成功,
            if (index == 1) {
                mAdapter1.addData(mRandom);
            } else {
                mAdapter2.addData(mRandom);
            }
            //某种类别的图片达到一定数量后重新开始下一局
            if (mList2.size() >= 6 || mList1.size() >= 6) {
                initGame();
            } else {
                randomTargetPic();
            }
            tvDissmiss(true,index);
        } else {
            //失败
            tvDissmiss(false,index);
        }
    }

    private void tvDissmiss(boolean isRight,int index) {
        mTvResult.setVisibility(View.VISIBLE);
        if (isRight) {
            mTvResult.setText(mGoodJob);
            mTvResult.setBackgroundResource(R.color.green);
            mRlv1.setBackgroundResource(R.drawable.bg_rlv_notips);
            mRlv2.setBackgroundResource(R.drawable.bg_rlv_notips);
        } else {
            mTvResult.setText(mTryAgain);
            mTvResult.setBackgroundResource(R.color.red);
            if (index == 1){
                mRlv1.setBackgroundResource(R.drawable.bg_rlv_tips);
            }else {
                mRlv2.setBackgroundResource(R.drawable.bg_rlv_tips);
            }
        }
        mDis = Observable.interval(1, TimeUnit.SECONDS)
                .take(1)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.print("long:" + aLong + ",right:" + isRight);
                        if (aLong >= 0) {
                            mTvResult.setVisibility(View.INVISIBLE);
                            mDis.dispose();
                        }
                    }
                });

    }

    private void initGame() {
        mList1.clear();
        mList2.clear();

        initSort();
        addLeftData();
        addRightData();
        randomTargetPic();
    }

    private void randomTargetPic() {
        int randomNumber = RandomImageUtil.getInstance().getRandomNumber(2);
        if (randomNumber == 0) {
            mRandom = RandomImageUtil.getInstance().random(mType1, true);
        } else {
            mRandom = RandomImageUtil.getInstance().random(mType2, true);
        }
        Glide.with(this).load(mRandom.resId).into(mIvTarget);
    }

    private void addRightData() {
        Bean random = RandomImageUtil.getInstance().random(mType2, true);
        mAdapter2.addData(random);
    }

    private void addLeftData() {
        Bean random = RandomImageUtil.getInstance().random(mType1, true);
        mAdapter1.addData(random);
    }

    private void initSort() {
        mType1 = RandomImageUtil.getInstance().getRandomTypeInt();
        mType2 = RandomImageUtil.getInstance().getRandomTypeInt();
        while (mType1 == mType2) {
            mType2 = RandomImageUtil.getInstance().getRandomTypeInt();
        }

        String typeString = RandomImageUtil.getInstance().getTypeString(mType1);
        mBtnLeft.setText(typeString);
        String typeString1 = RandomImageUtil.getInstance().getTypeString(mType2);
        mBtnRight.setText(typeString1);
        LogUtils.print("type1:"+mType1+","+typeString+",type2:"+mType2+","+typeString1);
    }
}
