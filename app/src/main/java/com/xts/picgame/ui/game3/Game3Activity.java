package com.xts.picgame.ui.game3;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.common.Constant;
import com.xts.picgame.common.CustomImageView;
import com.xts.picgame.model.bean.Bean;
import com.xts.picgame.model.bean.LocationBean;
import com.xts.picgame.utils.LogUtils;
import com.xts.picgame.utils.RandomImageUtil;
import com.xts.picgame.utils.SpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 游戏3和4,传不同type即可
 */
public class Game3Activity extends AppCompatActivity {
    public static final int TYPE_EQUAL = 0;//相同,目标图片肯定会在列表中有
    public static final int TYPE_SAME = 1;//相似,目标图片不会再列表中出现

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_result)
    TextView mTvResult;
    @BindView(R.id.iv_target)
    CustomImageView mIvTarget;
    @BindView(R.id.rlv)
    RecyclerView mRlv;
    private int mNumber;
    private ArrayList<Bean> mList;
    private int mType;
    private RlvGame3Adapter mAdapter;
    private Bean mTarget;
    private ArrayList<LocationBean> mLocationList = new ArrayList<>();
    private Disposable mDis;
    private String mGoodJob = BaseApp.getRes().getString(R.string.good_job);
    private String mTryAgain = BaseApp.getRes().getString(R.string.try_again);
    private int mRepeat;
    private boolean mIsRight;
    private int mActualRepeatNum;
    private boolean mIsTip;
    private int mGameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        mGameType = getIntent().getIntExtra(Constant.DATA, 0);
        if (mGameType == TYPE_EQUAL) {
            mTvTitle.setText(BaseApp.getRes().getString(R.string.match_pic));
        } else {
            mTvTitle.setText(BaseApp.getRes().getString(R.string.put_same));
        }

        mNumber = SpUtils.getInstance().getInt(Constant.GAME3_PIC_NUMBER);
        mRepeat = SpUtils.getInstance().getInt(Constant.GAME3_PIC_REPEAT);
        if (mNumber == 0) {
            mNumber = 6;
        }
        if (mRepeat == 0) {
            mRepeat = 1;
        }
        mList = new ArrayList<>();

        mRlv.setLayoutManager(new GridLayoutManager(this, 3));

        mAdapter = new RlvGame3Adapter(R.layout.item_game3, mList,mGameType);
        mAdapter.bindToRecyclerView(mRlv);

        initGame();
        //控件绘制完成监听
        mRlv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < mList.size(); i++) {
                    View childAt = mRlv.getChildAt(i);
                    Rect rect = new Rect();
                    //获取相对于屏幕的view的矩形视图区域
                    childAt.getGlobalVisibleRect(rect);
                    LogUtils.print("rect left:" + rect.left + ",top:" + rect.top +
                            ",right:" + rect.right + ",bottom:" + rect.bottom);
                    mLocationList.add(new LocationBean(i, rect.left, rect.top, rect.right, rect.bottom));
                }
                //可能多次触发,拿到后移除监听
                mRlv.getViewTreeObserver().removeOnGlobalLayoutListener(this);

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
                for (int i = 0; i < mLocationList.size(); i++) {
                    LocationBean locationBean = mLocationList.get(i);
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
                        break;
                    }

                }
            }
        });
    }

    /**
     * //校验传递图片和目标图片是否一致
     *
     * @param index 列表图片的index
     */
    private void check(int index) {
        Bean bean = mList.get(index);
        if (mGameType == TYPE_EQUAL) {
            tvDissmiss(bean.resId == mTarget.resId);
        }else {
            tvDissmiss(bean.type == mTarget.type);
        }
    }

    private void tvDissmiss(boolean isRight) {
        mIsRight = isRight;
        mTvResult.setVisibility(View.VISIBLE);
        if (isRight) {
            mTvResult.setText(mGoodJob);
            mTvResult.setBackgroundResource(R.color.green);
        } else {
            mTvResult.setText(mTryAgain);
            mTvResult.setBackgroundResource(R.color.red);
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
                            if (isRight) {
                                //对了
                                if (mIsTip) {
                                    //提示状态
                                    if (mActualRepeatNum < mRepeat) {
                                        //将提示的次数提示完
                                        mActualRepeatNum++;
                                        mAdapter.setTip(mIsTip);
                                    } else {
                                        //提示完后要进行不提示测验
                                        mIsTip = false;
                                        mAdapter.setTip(mIsTip);
                                        mActualRepeatNum = 0;
                                    }
                                } else {
                                    //对了且为非提示状态说明一次性对了,进行下一局
                                    initGame();
                                }
                            } else {
                                //做错了,提示,并且算作第一次提示,之前提示作废
                                mIsTip = true;
                                mActualRepeatNum = 1;
                                mAdapter.setTip(mIsTip);
                            }
                        }
                    }
                });

    }

    private void initGame() {
        mList.clear();
        mTarget = RandomImageUtil.getInstance()
                .random();
        Glide.with(this).load(mTarget.resId).into(mIvTarget);
        mType = mTarget.type;
        if (mGameType == TYPE_EQUAL) {
            mList.add(mTarget);
        }
        mAdapter.setTarget(mTarget);

        voice();

        if (mGameType == TYPE_EQUAL) {
            addData(mNumber - 1);
        } else {
            addSame();
            addDataNotEqual(mNumber - 1);
        }
    }

    //添加同类型不同资源的图片
    private void addSame() {
        mList.add(RandomImageUtil.getInstance().random(mTarget));
    }

    //添加数据,不能和目标数据一样
    private void addDataNotEqual(int num) {
        for (int i = 0; i < num; i++) {
            mList.add(RandomImageUtil.getInstance().randomNotEqual(mTarget));
        }
        Collections.shuffle(mList);
        mAdapter.notifyDataSetChanged();
    }

    private void voice() {

    }

    private void addData(int num) {
        for (int i = 0; i < num; i++) {
            mList.add(RandomImageUtil.getInstance().random());
        }
        Collections.shuffle(mList);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.iv_target})
    public void click(View v) {


    }
}
