package com.xts.picgame.ui.settings;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xts.picgame.R;
import com.xts.picgame.model.bean.DataBean;

import java.util.List;

public class RlvChoosePicAdapter extends BaseQuickAdapter<DataBean, BaseViewHolder> {
    public static final int TYPE_IDENTIFY = 0;//识别图片
    public static final int TYPE_EXPRESSIVE = 1;//认知考核
    public static final int TYPE_MATCH = 2;//完成匹配
    public static final int TYPE_SIMILAR = 3;//相似匹配
    public static final int TYPE_SORT = 4;//图片分类
    public static final int TYPE_RECEPTIVE = 5;//分类辨识
    private int mCurrentType;

    public RlvChoosePicAdapter(int layoutResId, @Nullable List<DataBean> data, int type) {
        super(layoutResId, data);
        this.mCurrentType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean item) {
        ImageView iv = helper.getView(R.id.iv);
        TextView tvName = helper.getView(R.id.tv_name);
        CheckBox cb = helper.getView(R.id.cb);

        Glide.with(mContext).load(item.getUrl()).into(iv);
        tvName.setText(item.getTname());

        boolean isChecked = true;
        switch (mCurrentType){
            case TYPE_IDENTIFY:
                isChecked = item.getIdentify();
                break;
            case TYPE_EXPRESSIVE:
                isChecked = item.getExpressive();
                break;
            case TYPE_MATCH:
                isChecked = item.getMatch();
                break;
            case TYPE_SIMILAR:
                isChecked = item.getSimilar();
                break;
            case TYPE_SORT:
                isChecked = item.getSort();
                break;
            case TYPE_RECEPTIVE:
                isChecked = item.getReceptive();
                break;
        }
        cb.setChecked(isChecked);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()){
                    switch (mCurrentType) {
                        case TYPE_IDENTIFY:
                            item.setIdentify(isChecked);
                            break;
                        case TYPE_EXPRESSIVE:
                            item.setExpressive(isChecked);
                            break;
                        case TYPE_MATCH:
                            item.setMatch(isChecked);
                            break;
                        case TYPE_SIMILAR:
                            item.setSimilar(isChecked);
                            break;
                        case TYPE_SORT:
                            item.setSort(isChecked);
                            break;
                        case TYPE_RECEPTIVE:
                            item.setReceptive(isChecked);
                            break;
                    }
                }
            }
        });
    }
}
