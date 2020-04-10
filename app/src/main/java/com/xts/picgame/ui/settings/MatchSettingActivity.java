package com.xts.picgame.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.common.Constant;
import com.xts.picgame.db.DataBeanDao;
import com.xts.picgame.model.bean.DataBean;
import com.xts.picgame.ui.game3.MatchActivity;
import com.xts.picgame.utils.RandomAllImageUtil;
import com.xts.picgame.utils.SpUtils;
import com.xts.picgame.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * public static final int TYPE_MATCH = 0;//相同,目标图片肯定会在列表中有,identicalmatching
 * public static final int TYPE_SIMILAR = 1;//相似,目标图片不会再列表中出现,similarmatching
 */
public class MatchSettingActivity extends AppCompatActivity {

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
    @BindView(R.id.tv_name2)
    TextView mTvName2;
    @BindView(R.id.tv_num_minus2)
    TextView mTvNumMinus2;
    @BindView(R.id.tv_number2)
    TextView mTvNumber2;
    @BindView(R.id.tv_num_plus2)
    TextView mTvNumPlus2;
    @BindView(R.id.rlv)
    RecyclerView mRlv;
    private String mTitle;
    private String mSetting = BaseApp.getRes().getString(R.string.settings);
    private int minNumber = 2;
    private int maxNumber = 6;
    private int mNumber;
    private int minRepeatNumber = 1;
    private int maxRepeatNumber = 3;
    private int mRepeatNumber;
    private String strMax = BaseApp.getRes().getString(R.string.max_number);
    private String strMin = BaseApp.getRes().getString(R.string.min_number);
    private int mGameType;
    private DataBeanDao mDataBeanDao;
    private RlvChoosePicAdapter mAdapter;


    public static void startAct(Context context, String title, int gameType) {
        Intent intent = new Intent(context, MatchSettingActivity.class);
        intent.putExtra(Constant.DATA, title);
        intent.putExtra(Constant.GAME_TYPE, gameType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_settings);
        mDataBeanDao = BaseApp.sBaseApp.getDaoSession().getDataBeanDao();
        ButterKnife.bind(this);
        mTitle = getIntent().getStringExtra(Constant.DATA);
        mGameType = getIntent().getIntExtra(Constant.GAME_TYPE, MatchActivity.TYPE_MATCH);

        mToolBar.setTitle(mSetting + " -- " + mTitle);
        setSupportActionBar(mToolBar);

        if (mGameType == MatchActivity.TYPE_MATCH) {
            mNumber = SpUtils.getInstance().getInt(Constant.MATCH_PIC_NUMBER);
            mRepeatNumber = SpUtils.getInstance().getInt(Constant.MATCH_PIC_REPEAT);
        } else {
            mNumber = SpUtils.getInstance().getInt(Constant.SIMILAR_PIC_NUMBER);
            mRepeatNumber = SpUtils.getInstance().getInt(Constant.SIMILAR_PIC_REPEAT);
        }
        if (mNumber == 0) {
            mNumber = 4;
        }
        if (mRepeatNumber == 0){
            mRepeatNumber = 1;
        }
        mTvNumber.setText(mNumber + "");
        mTvNumber2.setText(mRepeatNumber+"");

        choosePics();
    }

    @OnClick({R.id.tv_num_minus, R.id.tv_num_plus,R.id.tv_num_minus2, R.id.tv_num_plus2,})
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
            case R.id.tv_num_minus2:
                minus2();
                break;
            case R.id.tv_num_plus2:
                plus2();
                break;
        }
    }

    private void plus2() {
        if (mRepeatNumber >= maxRepeatNumber) {
            ToastUtil.showToast(strMax + maxRepeatNumber);
        } else {
            mRepeatNumber++;
            mTvNumber2.setText(mRepeatNumber + "");
            saveRepeatNumber();
        }
    }

    private void minus2() {
        if (mRepeatNumber <= minRepeatNumber) {
            ToastUtil.showToast(strMin + minRepeatNumber);
        } else {
            mRepeatNumber--;
            mTvNumber2.setText(mRepeatNumber + "");
            saveRepeatNumber();
        }
    }

    private void saveRepeatNumber() {
        if (mGameType == MatchActivity.TYPE_MATCH) {
            SpUtils.getInstance().setValue(Constant.MATCH_PIC_REPEAT, mRepeatNumber);
        }else {
            SpUtils.getInstance().setValue(Constant.SIMILAR_PIC_REPEAT, mRepeatNumber);
        }
    }

    private void plus() {
        if (mNumber >= maxNumber) {
            ToastUtil.showToast(strMax + maxNumber);
        } else {
            mNumber++;
            mTvNumber.setText(mNumber + "");
            savePicNumber();
        }
    }

    private void minus() {
        if (mNumber <= minNumber) {
            ToastUtil.showToast(strMin + minNumber);
        } else {
            mNumber--;
            mTvNumber.setText(mNumber + "");
            savePicNumber();
        }
    }

    private void savePicNumber() {
        if (mGameType == MatchActivity.TYPE_MATCH) {
            SpUtils.getInstance().setValue(Constant.MATCH_PIC_NUMBER, mNumber);
        }else {
            SpUtils.getInstance().setValue(Constant.SIMILAR_PIC_NUMBER, mNumber);
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
        int type;
        if (mGameType == MatchActivity.TYPE_MATCH) {
            type = RlvChoosePicAdapter.TYPE_MATCH;
        }else {
            type = RlvChoosePicAdapter.TYPE_SIMILAR;
        }
        mAdapter = new RlvChoosePicAdapter(R.layout.item_choose_pic,
                list,type);
        mRlv.setAdapter(mAdapter);
        mRlv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBeanDao.updateInTx(mAdapter.getData());
    }
}
