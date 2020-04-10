package com.xts.picgame.ui.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.navigation.NavigationView;
import com.xts.picgame.R;
import com.xts.picgame.apps.BaseApp;
import com.xts.picgame.common.Constant;
import com.xts.picgame.db.DataBeanDao;
import com.xts.picgame.model.HttpManager;
import com.xts.picgame.model.bean.DataBean;
import com.xts.picgame.model.bean.NetBean;
import com.xts.picgame.ui.game1.PicIdentifyActivity;
import com.xts.picgame.ui.game3.MatchActivity;
import com.xts.picgame.ui.game5.SortActivity;
import com.xts.picgame.ui.settings.ExpressiveSettingActivity;
import com.xts.picgame.ui.settings.MatchSettingActivity;
import com.xts.picgame.ui.settings.PicIdentifySettingActivity;
import com.xts.picgame.utils.LogUtils;
import com.xts.picgame.utils.RxUtils;
import com.xts.picgame.utils.ThreadPoolManager;
import com.xts.picgame.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.subscribers.ResourceSubscriber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rlv)
    RecyclerView mRlv;
    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    @BindView(R.id.dl)
    DrawerLayout mDl;
    @BindView(R.id.nv)
    NavigationView mNv;
    private PopupWindow mPopupWindow;
    private RlvMainAdapter mAdapter;
    private ArrayList<String> mTitles;
    private ArrayList<Integer> mImages;
    private ResourceSubscriber<NetBean> mSubscriber;
    private DataBeanDao mDataBeanDao;
    RequestOptions options = new RequestOptions().onlyRetrieveFromCache(true);
    private ProgressBar mPb;
    private TextView mTvProgress;
    private int mProgress;
    private int mMax;
    private PopupWindow mProgressPop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataBeanDao = BaseApp.sBaseApp.getDaoSession().getDataBeanDao();
        initView();
        initPers();
        //down();
        initData();
        mRlv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                progressPop();
                mRlv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void progressPop() {
        View inflate = LayoutInflater.from(this).inflate(R.layout.pop_progress, null);
        mPb = inflate.findViewById(R.id.pb);
        mTvProgress = inflate.findViewById(R.id.tv_progress);

        mProgressPop = new PopupWindow(inflate,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mProgressPop.showAtLocation(mRlv, Gravity.CENTER_VERTICAL, 0, 0);
    }

    private void initData() {
        mSubscriber = HttpManager.getInstance()
                .getApiService()
                .getImages()
                .compose(RxUtils.<NetBean>rxScheduler())
                .subscribeWith(new ResourceSubscriber<NetBean>() {
                    @Override
                    public void onNext(NetBean netBean) {
                        LogUtils.print("onNext" + netBean.toString());
                        if (netBean.getStatus() == Constant.SUCCESS_CODE) {
                            saveDataAndDownImage(netBean);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void saveDataAndDownImage(NetBean netBean) {
        if (netBean.getData() != null && netBean.getData().size() > 0) {
            List<DataBean> data = netBean.getData();
            //下载图片+是否要插入数据库
            downImages(data);
            //下载语音
            downVoice();
        }
    }

    private void downVoice() {

    }

    private void downImages(List<DataBean> data) {
        mMax = data.size();
        for (int i = 0; i < data.size(); i++) {
            DataBean dataBean = data.get(i);
            List<DataBean> list = mDataBeanDao.queryBuilder().where(DataBeanDao.Properties.Imageid.eq(dataBean.getImageid())).list();
            if (list!= null && list.size()>0){
                //有数据不插入,避免把原来的状态弄没有了
            }else {
                //没有数据,插入
                mDataBeanDao.insertOrReplaceInTx(dataBean);
            }
            mPb.setMax(mMax);
            ThreadPoolManager.getInstance()
                    .execute(new Runnable() {
                        @Override
                        public void run() {
                            if (!isImageDownloaded(dataBean.getUrl())) {
                                down(dataBean.getUrl());
                            }
                        }
                    });
        }
    }

    public void setProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPb.setProgress(mProgress);
                mTvProgress.setText(mProgress + " / " + mMax);

                if (mProgress == mMax) {
                    mProgressPop.dismiss();
                    ToastUtil.showToast("数据加载成功");
                }
            }
        });
    }

    //判断图片是否下载
    private boolean isImageDownloaded(String url) {
        try {
            File file = Glide.with(BaseApp.sBaseApp).downloadOnly().apply(options).load(url).submit().get();
            if (file != null) {
                LogUtils.print("cache:" + file.getAbsolutePath());
                mProgress++;
                setProgress();
                return true;
            } else {
                return false;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    //下载图片
    private void down(String url) {
        FutureTarget<File> target = Glide.with(BaseApp.sBaseApp)
                .asFile()
                .load(url)
                .submit();
        try {
            final File imageFile = target.get();
            mProgress++;
            setProgress();
            LogUtils.print("download:" + imageFile.getAbsolutePath());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initPers() {
        String[] pers = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ActivityCompat.requestPermissions(this, pers, 100);
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDl, mToolbar, R.string.app_name, R.string.app_name);
        mDl.addDrawerListener(toggle);
        toggle.syncState();

        initRlv();
        initListener();
    }

    private void initListener() {
        mNv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.pic_identify:
                        PicIdentifySettingActivity.startAct(MainActivity.this,mTitles.get(0),PicIdentifyActivity.TYPE_PIC_IDENTIFY);
                        break;
                    case R.id.expressive_label:
                        ExpressiveSettingActivity.startAct(MainActivity.this, mTitles.get(1));
                        break;
                    case R.id.identical_match:
                        MatchSettingActivity.startAct(MainActivity.this, mTitles.get(2), MatchActivity.TYPE_MATCH);
                        break;
                    case R.id.similar_match:
                        MatchSettingActivity.startAct(MainActivity.this, mTitles.get(3), MatchActivity.TYPE_SIMILAR);
                        break;
                    case R.id.sort:
                        //ExpressiveSettingActivity.startAct(MainActivity.this,mTitles.get(4));
                        ToastUtil.showToast(BaseApp.getRes().getString(R.string.no_settings));
                        break;
                    case R.id.receptive_label:
                        PicIdentifySettingActivity.startAct(MainActivity.this,mTitles.get(5),PicIdentifyActivity.TYPE_RECEPTIVE);
                        break;
                }
                return true;
            }
        });
    }

    private void initRlv() {
        mTitles = new ArrayList<>();
        mTitles.add(BaseApp.getRes().getString(R.string.pic_identify));
        mTitles.add(BaseApp.getRes().getString(R.string.expressive_label));
        mTitles.add(BaseApp.getRes().getString(R.string.identical_match));
        mTitles.add(BaseApp.getRes().getString(R.string.similar_match));
        mTitles.add(BaseApp.getRes().getString(R.string.sort));
        mTitles.add(BaseApp.getRes().getString(R.string.receptive_label));
        mImages = new ArrayList<>();
        mImages.add(R.drawable.pictureidentification);
        mImages.add(R.drawable.expressivelabeling);
        mImages.add(R.drawable.identicalmatching);
        mImages.add(R.drawable.similarmatching);
        mImages.add(R.drawable.sorting);
        mImages.add(R.drawable.receptivelabeling);
        mRlv.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter = new RlvMainAdapter(R.layout.item_main, mTitles, mImages);
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
                intent.putExtra(Constant.DATA, PicIdentifyActivity.TYPE_PIC_IDENTIFY);
                intent.setClass(this, PicIdentifyActivity.class);
                break;
            case 1:
                intent.setClass(this, com.xts.picgame.ui.game2.ExpressiveActivity.class);
                break;
            case 2:
                /*intent.putExtra(Constant.DATA,Game3Activity2.TYPE_MATCH);
                intent.setClass(this, Game3Activity2.class);*/
                intent.putExtra(Constant.DATA, MatchActivity.TYPE_MATCH);
                intent.setClass(this, MatchActivity.class);
                break;
            case 3:
                intent.putExtra(Constant.DATA, MatchActivity.TYPE_SIMILAR);
                intent.setClass(this, MatchActivity.class);
                break;
            case 4:
                intent.setClass(this, SortActivity.class);
                break;
            case 5:
                intent.putExtra(Constant.DATA, PicIdentifyActivity.TYPE_RECEPTIVE);
                intent.setClass(this, PicIdentifyActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriber.dispose();
    }
}
