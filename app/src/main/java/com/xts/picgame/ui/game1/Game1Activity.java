package com.xts.picgame.ui.game1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.model.bean.Bean;
import com.xts.picgame.utils.RandomImageUtil;
import com.xts.picgame.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Game1Activity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv)
    ImageView mIv;
    @BindView(R.id.iv_arrow_left)
    ImageView mIvArrowLeft;
    @BindView(R.id.iv_arrow_right)
    ImageView mIvArrowRight;
    private ArrayList<Bean> mBeans;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        mBeans = new ArrayList<>();
        addRandom();
        addRandom();
        showPic(mCurrentPosition);
    }

    private void showPic(int position) {
        Glide.with(this).load(mBeans.get(position).resId).into(mIv);
    }

    private void addRandom() {
        Bean random = RandomImageUtil.getInstance().random();
        mBeans.add(random);
    }

    @OnClick({R.id.iv, R.id.iv_arrow_left, R.id.iv_arrow_right})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.iv:
                voice();
                break;
            case R.id.iv_arrow_left:
                pre();
                break;
            case R.id.iv_arrow_right:
                next();
                break;
        }
    }

    //下一个
    private void next() {
        if (mCurrentPosition < mBeans.size()-1){
            mCurrentPosition++;
            showPic(mCurrentPosition);
        }else {
            addRandom();
            next();
        }
    }

    //上一个
    private void pre() {
        if (mCurrentPosition > 0){
            mCurrentPosition--;
            showPic(mCurrentPosition);
        }else {
            ToastUtil.showToast(BaseApp.getRes().getString(R.string.already_is_first));
        }
    }

    //播放正确读音
    private void voice() {

    }
}
