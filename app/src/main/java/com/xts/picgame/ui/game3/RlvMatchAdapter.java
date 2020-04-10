package com.xts.picgame.ui.game3;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xts.picgame.R;
import com.xts.picgame.model.bean.DataBean;

import java.util.List;

public class RlvMatchAdapter extends BaseQuickAdapter<DataBean, BaseViewHolder> {
    private final int mGameType;
    private boolean mIsTip;
    private DataBean mTarget;

    public RlvMatchAdapter(int layoutResId, @Nullable List<DataBean> data, int gameType) {
        super(layoutResId, data);
        mGameType = gameType;
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean item) {
        ImageView view = (ImageView) helper.getView(R.id.iv);
        Glide.with(mContext).load(item.getUrl()).into(view);
        if (mGameType == MatchActivity.TYPE_MATCH){
            if (mIsTip && !(mTarget.getImageid() == item.getImageid())) {
                view.setAlpha(0.3f);
            }else {
                view.setAlpha(1.0f);
            }
        }else {
            if (mIsTip && !(mTarget.getTypeid() == item.getTypeid())) {
                view.setAlpha(0.3f);
            }else {
                view.setAlpha(1.0f);
            }
        }

    }

    public void setTip(boolean isTip) {
        mIsTip = isTip;
        notifyDataSetChanged();
    }

    public void setTarget(DataBean target) {
        this.mTarget = target;
    }
}
