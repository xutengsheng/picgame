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
import com.xts.picgame.ui.game1.PicIdentifyActivity;
import com.xts.picgame.utils.LogUtils;
import com.xts.picgame.utils.RandomAllImageUtil;
import com.xts.picgame.utils.SpUtils;
import com.xts.picgame.utils.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PicIdentifySettingActivity extends AppCompatActivity {

    @BindView(R.id.toolBar)
    Toolbar mToolBar;

    @BindView(R.id.rlv)
    RecyclerView mRlv;
    private String mTitle;
    private String mSetting = BaseApp.getRes().getString(R.string.settings);
    private DataBeanDao mDataBeanDao;
    private RlvChoosePicAdapter mAdapter;
    private int mGameType;

    /**
     *
     * @param context
     * @param title
     * @param type PicIdentifyActivity.TYPE_PIC_IDENTIFY,TYPE_RECEPTIVE
     */
    public static void startAct(Context context, String title,int type) {
        Intent intent = new Intent(context, PicIdentifySettingActivity.class);
        intent.putExtra(Constant.DATA, title);
        intent.putExtra(Constant.GAME_TYPE,type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBeanDao = BaseApp.sBaseApp.getDaoSession().getDataBeanDao();
        setContentView(R.layout.activity_pic_identify_settings);
        ButterKnife.bind(this);
        mTitle = getIntent().getStringExtra(Constant.DATA);
        mGameType = getIntent().getIntExtra(Constant.GAME_TYPE, 0);

        mToolBar.setTitle(mSetting + " -- " + mTitle);
        setSupportActionBar(mToolBar);


        choosePics();

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
        if (mGameType == PicIdentifyActivity.TYPE_PIC_IDENTIFY){
            mAdapter = new RlvChoosePicAdapter(R.layout.item_choose_pic,
                    list, RlvChoosePicAdapter.TYPE_IDENTIFY);
        }else {
            mAdapter = new RlvChoosePicAdapter(R.layout.item_choose_pic,
                    list, RlvChoosePicAdapter.TYPE_RECEPTIVE);
        }
        mRlv.setAdapter(mAdapter);
        mRlv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataBeanDao.updateInTx(mAdapter.getData());
    }
}
