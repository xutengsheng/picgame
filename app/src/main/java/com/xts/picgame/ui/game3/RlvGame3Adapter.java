package com.xts.picgame.ui.game3;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xts.picgame.R;
import com.xts.picgame.model.bean.Bean;

import java.util.List;

public class RlvGame3Adapter extends BaseQuickAdapter<Bean, BaseViewHolder> {
    private final int mGameType;
    private boolean mIsTip;
    private Bean mTarget;

    public RlvGame3Adapter(int layoutResId, @Nullable List<Bean> data, int gameType) {
        super(layoutResId, data);
        mGameType = gameType;
    }

    @Override
    protected void convert(BaseViewHolder helper, Bean item) {
        ImageView view = (ImageView) helper.getView(R.id.iv);
        Glide.with(mContext).load(item.resId).into(view);
        if (mGameType == Game3Activity.TYPE_EQUAL){
            if (mIsTip && !(mTarget.resId == item.resId)) {
                view.setAlpha(0.5f);
            }else {
                view.setAlpha(1.0f);
            }
        }else {
            if (mIsTip && !(mTarget.type == item.type)) {
                view.setAlpha(0.5f);
            }else {
                view.setAlpha(1.0f);
            }
        }

    }

    public void setTip(boolean isTip) {
        mIsTip = isTip;
        notifyDataSetChanged();
    }

    public void setTarget(Bean target) {
        this.mTarget = target;
    }
}
