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
public class Game3Activity2 extends AppCompatActivity {
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
    private int mType;//目标图片的类型
    private RlvGame3Adapter2 mAdapter;
    private Bean mTarget;
    private String mGoodJob = BaseApp.getRes().getString(R.string.good_job);
    private String mTryAgain = BaseApp.getRes().getString(R.string.try_again);
    private ArrayList<LocationBean> mLocationBeans = new ArrayList<>();

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

        mNumber = SpUtils.getInstance().getInt(Constant.GAME3_PIC_NUMBER);
        if (mNumber == 0) {
            mNumber = 6;
        }
        mList = new ArrayList<>();

        mRlv.setLayoutManager(new GridLayoutManager(this, 3));

        mAdapter = new RlvGame3Adapter2(R.layout.item_game3, mList);
        mAdapter.bindToRecyclerView(mRlv);

        initGame();

        mIvTarget.setOnMoveListener(new CustomImageView.OnMoveListener() {
            @Override
            public void onMove(int l, int t, int r, int b) {
                //手指抬起的时候触发,
                //在这里进行计算,看目标图片和列表图片是否重叠
                //目标图片l,t,r,b
                //列表图片的l,t,r,b有了在mLocationBeans
                //比较重叠,取3种
                //1.对点,左上角,左下角,右上角,右下角对上
                //2.目标图片远远比列表图片小,target.l>list.l && target.t>list.t&&target.r<list.r&&target.b<list.b
                //3.列表图片比目标图片小 target.l<list.l && target.t<list.t && target.r>list.r &&target.b>list.b

                //

                for (int i = 0; i < mLocationBeans.size(); i++) {
                    LocationBean bean = mLocationBeans.get(i);

                    int dl = Math.abs(l - bean.l);
                    int dt = Math.abs(t - bean.t);
                    int dr = Math.abs(r - bean.r);
                    int db = Math.abs(b - bean.b);

                    boolean b1 = (dl < 30 && dt < 30)
                            || (dl < 30 && db < 30)
                            || (dr < 30 && dt < 30)
                            || (dr < 30 && db < 30);
                    boolean b2 = l > bean.l && t > bean.t && r < bean.r && b < bean.b;
                    boolean b3 = l < bean.l && t < bean.t && r > bean.r && b > bean.b;

                    //图片重叠并满足大部分重叠
                    if (b1 || b2 || b3) {
                        check(bean.index);
                        break;
                    }
                }

            }
        });

        //不能在这里获取控件的可视矩形区域,因为控件要在onResume()之后才可以渲染成功
       /* View childAt = mRlv.getChildAt(0);
        childAt.getGlobalVisibleRect(rect)*/
        //ViewTree 视图树
        //完成渲染的监听
        mRlv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for (int i = 0; i < mList.size(); i++) {
                    View childAt = mRlv.getChildAt(i);
                    Rect rect = new Rect();
                    childAt.getGlobalVisibleRect(rect);
                    LogUtils.print("list l:" + rect.left + ",t:" + rect.top + ",r:" + rect.right + ",b:" + rect.bottom);
                    mLocationBeans.add(new LocationBean(i, rect.left, rect.top, rect.right, rect.bottom));
                }
            }
        });
    }

    private void check(int index) {
        Bean bean = mList.get(index);
        //相似匹配
        if (bean.type == mTarget.type) {
            //相同匹配
            //bean.resId = mTarget.resId;
            //成功
        }else {
            //失败了
        }
    }

    private void initGame() {
        mList.clear();
        mTarget = RandomImageUtil.getInstance()
                .random();
        Glide.with(this).load(mTarget.resId).into(mIvTarget);
        mType = mTarget.type;

        mList.add(mTarget);

        mAdapter.setTarget(mTarget);

        addData(mNumber - 1);

    }

    private void addData(int num) {
        for (int i = 0; i < num; i++) {
            mList.add(RandomImageUtil.getInstance().random());
        }
        Collections.shuffle(mList);
        mAdapter.notifyDataSetChanged();
    }
}
