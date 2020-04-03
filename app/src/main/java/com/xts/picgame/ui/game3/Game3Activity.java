package com.xts.picgame.ui.game3;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
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

    private SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private String voicer = "xiaoyan";
    private String mMatch;
    private String mSame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game3);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        mGameType = getIntent().getIntExtra(Constant.DATA, 0);
        if (mGameType == TYPE_EQUAL) {
            mMatch = BaseApp.getRes().getString(R.string.match_pic);
            mTvTitle.setText(mMatch);
        } else {
            mSame = BaseApp.getRes().getString(R.string.put_same);
            mTvTitle.setText(mSame);
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

                //可以对比图片的中心点
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
            voice(mGoodJob);
        } else {
            voice(mTryAgain);
            mTvResult.setText(mTryAgain);
            mTvResult.setBackgroundResource(R.color.red);
        }
        mDis = Observable.interval(1, TimeUnit.SECONDS)
                .take(2)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.print("long:" + aLong + ",right:" + isRight);
                        if (aLong >= 1) {
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
        String voice = mSame;
        if (mGameType == TYPE_EQUAL) {
            mList.add(mTarget);
            voice = mMatch;
        }
        mAdapter.setTarget(mTarget);

        voice(voice);

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

    //播放正确读音
    private void voice(String str) {
        // 移动数据分析，收集开始合成事件
        /*FlowerCollector.onEvent(TtsDemo.this, "tts_play");*/

        // 设置参数
        setParam();
        int code = mTts.startSpeaking(str, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            showTip("语音合成失败,错误码: " + code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        }
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码："+code+",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            Log.e("MscSpeechLog_", "percent =" + percent);

        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            System.out.println("oncompleted");
            if (error == null) {

            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);

            }



        }
    };

    private void showTip(final String str) {
        Toast.makeText(this,str,Toast.LENGTH_SHORT);
    }

    /**
     * 参数设置
     * @return
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            //	mTts.setParameter(SpeechConstant.TTS_BUFFER_TIME,"1");

            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");

        }

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/tts.pcm");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( null != mTts ){
            mTts.stopSpeaking();
            // 退出时释放连接
            mTts.destroy();
        }
    }
}
