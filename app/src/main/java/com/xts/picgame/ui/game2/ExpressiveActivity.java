package com.xts.picgame.ui.game2;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.xts.picgame.model.bean.DataBean;
import com.xts.picgame.ui.settings.RlvChoosePicAdapter;
import com.xts.picgame.utils.LogUtils;
import com.xts.picgame.utils.RandomAllImageUtil;
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

//expressivelabeling 认知考核
public class ExpressiveActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_skip)
    TextView mTvSkip;
    @BindView(R.id.rlv)
    RecyclerView mRlv;
    private int mNumber;
    private String mStr;
    private ArrayList<DataBean> mList;
    private String mStrPre = BaseApp.getRes().getString(R.string.find);
    private RlvExpressiveAdapter mAdapter;
    private int mType;
    private String mNotQuite = BaseApp.getRes().getString(R.string.not_quite);
    private int mFailColor = BaseApp.getRes().getColor(R.color.red);
    private String mRight = BaseApp.getRes().getString(R.string.that_is_right);
    private int mRightColor = BaseApp.getRes().getColor(R.color.green);
    private Disposable mDis;
    private boolean mAlreadyClick = false;


    private SpeechSynthesizer mTts;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private String voicer = "xiaoyan";
    private RandomImageUtil mRandomImageUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2);
        mRandomImageUtil = new RandomImageUtil(RlvChoosePicAdapter.TYPE_EXPRESSIVE);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);

        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        mNumber = SpUtils.getInstance().getInt(Constant.EXPRESSIVE_PIC_NUMBER);
        if (mNumber == 0) {
            mNumber = 4;
        }
        mList = new ArrayList<>();

        mRlv.setLayoutManager(new GridLayoutManager(this, 2));

        mAdapter = new RlvExpressiveAdapter(R.layout.item_game2, mList);
        mAdapter.bindToRecyclerView(mRlv);

        initGame();

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!mAlreadyClick){
                    DataBean bean = mList.get(position);
                    TextView tv = (TextView) mAdapter.getViewByPosition(position, R.id.tv);
                    tv.setVisibility(View.VISIBLE);
                    if (bean.getTypeid() == mType) {
                        tv.setText(mRight);
                        tv.setBackgroundColor(mRightColor);
                        tvDissmiss(tv, true);
                        voice(mRight);
                    } else {
                        tv.setText(mNotQuite);
                        tv.setBackgroundColor(mFailColor);
                        tvDissmiss(tv, false);
                        voice(mNotQuite);
                    }
                }
                mAlreadyClick = true;

            }
        });
    }

    private void tvDissmiss(TextView tv, boolean isRight) {
        mDis = Observable.interval(1, TimeUnit.SECONDS)
                .take(3)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        LogUtils.print("long:" + aLong+",right:"+isRight);
                        if (aLong >= 2) {
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
        DataBean target = mRandomImageUtil
                .random();
        mType = target.getTypeid();
        mList.add(target);

        mStr = mRandomImageUtil.getTypeString(target.getTypeid());

        mTvTitle.setText(mStrPre + " "+mStr);

        voice(mStrPre + " "+mStr);

        addData(mNumber - 1);
    }

    private void addData(int num) {
        for (int i = 0; i < num; i++) {
            mList.add(mRandomImageUtil.random());
        }
        Collections.shuffle(mList);
        mAdapter.notifyDataSetChanged();
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
