package com.xts.picgame.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.common.Constant;
import com.xts.picgame.model.bean.DataBean;
import com.xts.picgame.ui.game3.MatchActivity;
import com.xts.picgame.utils.RandomAllImageUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SortSettingsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rlv)
    RecyclerView mRlv;
    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    private RlvChoosePicAdapter mAdapter;
    private String mTitle;
    private String mSetting = BaseApp.getRes().getString(R.string.settings);

    public static void startAct(Context context, String title) {
        Intent intent = new Intent(context, SortSettingsActivity.class);
        intent.putExtra(Constant.DATA, title);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_settings);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTitle = getIntent().getStringExtra(Constant.DATA);

        mToolbar.setTitle(mSetting+" -- "+mTitle);
        setSupportActionBar(mToolbar);
        mTvTitle.setText(R.string.choose_pic_type);

        choosePics();
    }

    //选择游戏图片
    private void choosePics() {
        SparseArray<ArrayList<DataBean>> all = RandomAllImageUtil.getInstance()
                .getAll();
        ArrayList<Integer> types = RandomAllImageUtil.getInstance()
                .getType();
        ArrayList<DataBean> list = new ArrayList<>();
        for (int i = 0; i < types.size(); i++) {
            ArrayList<DataBean> dataBeans = all.get(types.get(i));
            list.add(dataBeans.get(0));
        }

        mRlv.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new RlvChoosePicAdapter(R.layout.item_choose_pic,
                list,RlvChoosePicAdapter.TYPE_SORT);
        mRlv.setAdapter(mAdapter);
        mRlv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }
}
