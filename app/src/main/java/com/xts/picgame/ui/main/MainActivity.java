package com.xts.picgame.ui.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.common.Constant;
import com.xts.picgame.ui.game1.Game1Activity;
import com.xts.picgame.ui.game2.Game2Activity;
import com.xts.picgame.ui.game3.Game3Activity;
import com.xts.picgame.ui.game3.Game3Activity2;
import com.xts.picgame.ui.game5.Game5Activity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rlv)
    RecyclerView mRlv;
    private PopupWindow mPopupWindow;
    private RlvMainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initPers();
    }

    private void initPers() {
        String[] pers = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this,pers,100);
    }

    private void initView() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add(BaseApp.getRes().getString(R.string.game1));
        titles.add(BaseApp.getRes().getString(R.string.game2));
        titles.add(BaseApp.getRes().getString(R.string.game3));
        titles.add(BaseApp.getRes().getString(R.string.game4));
        titles.add(BaseApp.getRes().getString(R.string.game5));
        titles.add(BaseApp.getRes().getString(R.string.game6));
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_apple_001);
        images.add(R.drawable.ic_apple_002);
        images.add(R.drawable.ic_apple_003);
        images.add(R.drawable.ic_apple_004);
        images.add(R.drawable.ic_apple_005);
        images.add(R.drawable.ic_apple_006);
        mRlv.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new RlvMainAdapter(R.layout.item_main, titles, images);
        mAdapter.bindToRecyclerView(mRlv);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showPop(position);
            }
        });
    }

    private void showPop(int position) {
        if (mPopupWindow == null) {
            View inflate = LayoutInflater.from(this).inflate(R.layout.pop_main, null);
            mPopupWindow = new PopupWindow(inflate, (int) (BaseApp.mWidthPixels * 0.7f),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable());
            mPopupWindow.setOutsideTouchable(true);


        }

        View contentView = mPopupWindow.getContentView();
        ImageView iv = contentView.findViewById(R.id.iv);
        contentView.findViewById(R.id.btn_start_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2Game(position);
                mPopupWindow.dismiss();
            }
        });
        Glide.with(this).load(mAdapter.mImages.get(position)).into(iv);
        mPopupWindow.showAtLocation(mRlv, Gravity.CENTER, 0, 0);
    }

    private void go2Game(int position) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.putExtra(Constant.DATA,Game1Activity.TYPE_WHAT);
                intent.setClass(this, Game1Activity.class);
                break;
            case 1:
                intent.setClass(this, Game2Activity.class);
                break;
            case 2:
                /*intent.putExtra(Constant.DATA,Game3Activity2.TYPE_EQUAL);
                intent.setClass(this, Game3Activity2.class);*/
                intent.putExtra(Constant.DATA,Game3Activity.TYPE_EQUAL);
                intent.setClass(this, Game3Activity.class);
                break;
            case 3:
                intent.putExtra(Constant.DATA,Game3Activity.TYPE_SAME);
                intent.setClass(this, Game3Activity.class);
                break;
            case 4:
                intent.setClass(this, Game5Activity.class);
                break;
            case 5:
                intent.putExtra(Constant.DATA,Game1Activity.TYPE_PRACTICE);
                intent.setClass(this, Game1Activity.class);
                break;
        }
        startActivity(intent);
    }
}
