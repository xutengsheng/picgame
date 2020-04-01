package com.xts.picgame.ui.game2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.common.Constant;
import com.xts.picgame.model.bean.Bean;
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

public class Game2Activity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_skip)
    TextView mTvSkip;
    @BindView(R.id.rlv)
    RecyclerView mRlv;
    private int mNumber;
    private String mStr;
    private ArrayList<Bean> mList;
    private String mStrPre = BaseApp.getRes().getString(R.string.find);
    private RlvGame2Adapter mAdapter;
    private int mType;
    private String mNotQuite = BaseApp.getRes().getString(R.string.not_quite);
    private int mFailColor = BaseApp.getRes().getColor(R.color.red);
    private String mRight = BaseApp.getRes().getString(R.string.that_is_right);
    private int mRightColor = BaseApp.getRes().getColor(R.color.green);
    private Disposable mDis;
    private boolean mAlreadyClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        mNumber = SpUtils.getInstance().getInt(Constant.GAME2_PIC_NUMBER);
        if (mNumber == 0) {
            mNumber = 4;
        }
        mList = new ArrayList<>();

        mRlv.setLayoutManager(new GridLayoutManager(this, 2));

        mAdapter = new RlvGame2Adapter(R.layout.item_game2, mList);
        mAdapter.bindToRecyclerView(mRlv);

        initGame();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!mAlreadyClick){
                    Bean bean = mList.get(position);
                    TextView tv = (TextView) mAdapter.getViewByPosition(position, R.id.tv);
                    tv.setVisibility(View.VISIBLE);
                    if (bean.type == mType) {
                        tv.setText(mRight);
                        tv.setBackgroundColor(mRightColor);
                        tvDissmiss(tv, true);
                    } else {
                        tv.setText(mNotQuite);
                        tv.setBackgroundColor(mFailColor);
                        tvDissmiss(tv, false);
                    }
                }
                mAlreadyClick = true;

            }
        });
    }

    private void tvDissmiss(TextView tv, boolean isRight) {
        mDis = Observable.interval(1, TimeUnit.SECONDS)
                .take(1)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.print("long:" + aLong+",right:"+isRight);
                        if (aLong >= 0) {
                            tv.setVisibility(View.INVISIBLE);
                            mDis.dispose();
                            if (isRight) {
                                initGame();
                            }
                            mAlreadyClick = false;
                        }
                    }
                });

    }

    private void initGame() {
        mAdapter.getData().clear();
        Bean target = RandomImageUtil.getInstance()
                .random();
        mType = target.type;
        mList.add(target);

        mStr = RandomImageUtil.getInstance().getTypeString(target.type);

        mTvTitle.setText(mStrPre + " "+mStr);

        voice();

        addData(mNumber - 1);
    }

    private void addData(int num) {
        for (int i = 0; i < num; i++) {
            mList.add(RandomImageUtil.getInstance().random());
        }
        Collections.shuffle(mList);
        mAdapter.notifyDataSetChanged();
    }

    private void voice() {

    }

    @OnClick({R.id.tv_skip})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_skip:
                initGame();
                break;
        }
    }
}
