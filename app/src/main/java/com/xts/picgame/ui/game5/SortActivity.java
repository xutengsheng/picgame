package com.xts.picgame.ui.game5;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
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
import com.xts.picgame.common.CustomImageView;
import com.xts.picgame.model.bean.DataBean;
import com.xts.picgame.model.bean.LocationBean;
import com.xts.picgame.ui.game2.RlvExpressiveAdapter;
import com.xts.picgame.ui.settings.RlvChoosePicAdapter;
import com.xts.picgame.utils.LogUtils;
import com.xts.picgame.utils.RandomAllImageUtil;
import com.xts.picgame.utils.RandomImageUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


///sorting,图片分类
public class SortActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_result)
    TextView mTvResult;
    @BindView(R.id.rlv1)
    RecyclerView mRlv1;
    @BindView(R.id.rlv2)
    RecyclerView mRlv2;
    @BindView(R.id.iv_target)
    CustomImageView mIvTarget;
    @BindView(R.id.btn_left)
    Button mBtnLeft;
    @BindView(R.id.btn_right)
    Button mBtnRight;
    private ArrayList<DataBean> mList1;
    private ArrayList<DataBean> mList2;
    private RlvExpressiveAdapter mAdapter1;
    private RlvExpressiveAdapter mAdapter2;
    private LocationBean mRlv1Location;
    private LocationBean mRlv2Location;
    private int mType1;
    private int mType2;
    private DataBean mRandom;
    private Disposable mDis;
    private String mFailVoice = BaseApp.getRes().getString(R.string.fail_o);

    private SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private String voicer = "xiaoyan";

    private String mGoodJob = BaseApp.getRes().getString(R.string.good_job);
    private String mTryAgain = BaseApp.getRes().getString(R.string.try_again);
    private RandomImageUtil mRandomImageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game5);
        mRandomImageUtil = new RandomImageUtil(RlvChoosePicAdapter.TYPE_SORT);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mList1 = new ArrayList<>();
        mList2 = new ArrayList<>();

        mRlv1.setLayoutManager(new GridLayoutManager(this, 2));
        mRlv2.setLayoutManager(new GridLayoutManager(this, 2));


        mAdapter1 = new RlvExpressiveAdapter(R.layout.item_game5, mList1);
        mAdapter2 = new RlvExpressiveAdapter(R.layout.item_game5, mList2);
        mAdapter1.bindToRecyclerView(mRlv1);
        mAdapter2.bindToRecyclerView(mRlv2);

        initGame();
        //控件绘制完成监听
        mRlv1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取相对于屏幕的view的矩形视图区域
                mRlv1.getGlobalVisibleRect(rect);
                LogUtils.print("rect left:" + rect.left + ",top:" + rect.top +
                        ",right:" + rect.right + ",bottom:" + rect.bottom);
                mRlv1Location = new LocationBean(1, rect.left, rect.top, rect.right, rect.bottom);

                //可能多次触发,拿到后移除监听
                mRlv1.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
        //控件绘制完成监听
        mRlv2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取相对于屏幕的view的矩形视图区域
                mRlv2.getGlobalVisibleRect(rect);
                LogUtils.print("rect left:" + rect.left + ",top:" + rect.top +
                        ",right:" + rect.right + ",bottom:" + rect.bottom);
                mRlv2Location = new LocationBean(2, rect.left, rect.top, rect.right, rect.bottom);

                //可能多次触发,拿到后移除监听
                mRlv2.getViewTreeObserver().removeOnGlobalLayoutListener(this);

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

                LocationBean locationBean;
                if (l < BaseApp.mWidthPixels / 2 - (r - l)) {
                    //靠左
                    locationBean = mRlv1Location;
                } else {
                    //靠右
                    locationBean = mRlv2Location;
                }
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
                }
            }
        });
    }

    /**
     * //校验传递图片和目标位置种类是否一致
     *
     * @param index 目标位置,1,左边,2右边
     */
    private void check(int index) {
        int type;
        if (index == 1) {
            type = mType1;
        } else {
            type = mType2;
        }
        if (mRandom.getTypeid() == type) {
            //成功,
            if (index == 1) {
                mAdapter1.addData(mRandom);
            } else {
                mAdapter2.addData(mRandom);
            }
            //某种类别的图片达到一定数量后重新开始下一局
            if (mList2.size() >= 6 || mList1.size() >= 6) {
                initGame();
            } else {
                randomTargetPic();
            }
            tvDissmiss(true,index);
        } else {
            //失败
            tvDissmiss(false,index);
        }
    }

    private void tvDissmiss(boolean isRight,int index) {
        mTvResult.setVisibility(View.VISIBLE);
        if (isRight) {
            mTvResult.setText(mGoodJob);
            mTvResult.setBackgroundResource(R.color.green);
            mRlv1.setBackgroundResource(R.drawable.bg_rlv_notips);
            mRlv2.setBackgroundResource(R.drawable.bg_rlv_notips);
            voice(mGoodJob);
        } else {
            voice(mTryAgain);
            mTvResult.setText(mTryAgain);
            mTvResult.setBackgroundResource(R.color.red);
            if (index == 1){
                mRlv2.setBackgroundResource(R.drawable.bg_rlv_tips);
            }else {
                mRlv1.setBackgroundResource(R.drawable.bg_rlv_tips);
            }
            voice(mFailVoice);
        }
        mDis = Observable.interval(1, TimeUnit.SECONDS)
                .take(1)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.print("long:" + aLong + ",right:" + isRight);
                        if (aLong >= 0) {
                            mTvResult.setVisibility(View.INVISIBLE);
                            mDis.dispose();
                        }
                    }
                });

    }

    private void initGame() {
        mList1.clear();
        mList2.clear();

        initSort();
        addLeftData();
        addRightData();
        randomTargetPic();
    }

    private void randomTargetPic() {
        int randomNumber = mRandomImageUtil.getRandomNumber(2);
        if (randomNumber == 0) {
            mRandom = mRandomImageUtil.random(mType1, true);
        } else {
            mRandom = mRandomImageUtil.random(mType2, true);
        }
        Glide.with(this).load(mRandom.getUrl()).into(mIvTarget);
    }

    private void addRightData() {
        DataBean random = mRandomImageUtil.random(mType2, true);
        mAdapter2.addData(random);
    }

    private void addLeftData() {
        DataBean random = mRandomImageUtil.random(mType1, true);
        mAdapter1.addData(random);
    }

    private void initSort() {
        mType1 = mRandomImageUtil.getRandomTypeInt();
        mType2 = mRandomImageUtil.getRandomTypeInt();
        while (mType1 == mType2) {
            mType2 = mRandomImageUtil.getRandomTypeInt();
        }

        String typeString = mRandomImageUtil.getTypeString(mType1);
        mBtnLeft.setText(typeString);
        String typeString1 = mRandomImageUtil.getTypeString(mType2);
        mBtnRight.setText(typeString1);
        LogUtils.print("type1:"+mType1+","+typeString+",type2:"+mType2+","+typeString1);
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
