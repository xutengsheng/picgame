package com.xts.picgame.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.common.Constant;
import com.xts.picgame.db.DataBeanDao;
import com.xts.picgame.model.bean.DataBean;
import com.xts.picgame.utils.LogUtils;
import com.xts.picgame.utils.RandomAllImageUtil;
import com.xts.picgame.utils.SpUtils;
import com.xts.picgame.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpressiveSettingActivity extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_num_minus)
    TextView mTvNumMinus;
    @BindView(R.id.tv_number)
    TextView mTvNumber;
    @BindView(R.id.tv_num_plus)
    TextView mTvNumPlus;
    @BindView(R.id.ll_container)
    ConstraintLayout mLlContainer;
    @BindView(R.id.rlv)
    RecyclerView mRlv;
    private String mTitle;
    private String mSetting = BaseApp.getRes().getString(R.string.settings);
    private int minNumber = 2;
    private int maxNumber = 6;
    private int mNumber;
    private String strMax = BaseApp.getRes().getString(R.string.max_number);
    private String strMin = BaseApp.getRes().getString(R.string.min_number);
    private DataBeanDao mDataBeanDao;
    private RlvChoosePicAdapter mAdapter;

    public static void startAct(Context context, String title) {
        Intent intent = new Intent(context, ExpressiveSettingActivity.class);
        intent.putExtra(Constant.DATA, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBeanDao = BaseApp.sBaseApp.getDaoSession().getDataBeanDao();
        setContentView(R.layout.activity_expressive_settings);
        ButterKnife.bind(this);
        mTitle = getIntent().getStringExtra(Constant.DATA);

        mToolBar.setTitle(mSetting+" -- "+mTitle);
        setSupportActionBar(mToolBar);

        mNumber = SpUtils.getInstance().getInt(Constant.EXPRESSIVE_PIC_NUMBER);
        if (mNumber == 0){
            mNumber = 4;
        }
        mTvNumber.setText(mNumber+"");

        choosePics();

    }

    @OnClick({R.id.tv_num_minus, R.id.tv_num_plus})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_num_minus:
                minus();
                break;
            case R.id.tv_num_plus:
                plus();
                break;
        }
    }

    private void plus() {
        if (mNumber>=maxNumber){
            ToastUtil.showToast(strMax+maxNumber);
        }else {
            mNumber++;
            mTvNumber.setText(mNumber+"");
            save();
        }
    }

    private void save() {
        SpUtils.getInstance().setValue(Constant.EXPRESSIVE_PIC_NUMBER,mNumber);
    }

    private void minus() {
        if (mNumber<=minNumber){
            ToastUtil.showToast(strMin+minNumber);
        }else {
            mNumber--;
            mTvNumber.setText(mNumber+"");
            save();
        }
    }

    //选择游戏图片
    private void choosePics() {
        SparseArray<ArrayList<DataBean>> all = RandomAllImageUtil.getInstance()
                .getAll();
        ArrayList<DataBean> list = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            ArrayList<DataBean> dataBeans = all.valueAt(i);
            list.addAll(dataBeans);
        }

        mRlv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RlvChoosePicAdapter(R.layout.item_choose_pic,
                list,RlvChoosePicAdapter.TYPE_EXPRESSIVE);
        mRlv.setAdapter(mAdapter);
        mRlv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBeanDao.updateInTx(mAdapter.getData());
    }
}
