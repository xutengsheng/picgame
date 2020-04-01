package com.xts.picgame.ui.game3;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xts.picgame.R;
import com.xts.picgame.model.bean.Bean;

import java.util.List;

public class RlvGame3Adapter2 extends BaseQuickAdapter<Bean, BaseViewHolder> {

    private boolean mIsTip;
    private Bean mTarget;

    public RlvGame3Adapter2(int layoutResId, @Nullable List<Bean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bean item) {
        ImageView view = (ImageView) helper.getView(R.id.iv);
        Glide.with(mContext).load(item.resId).into(view);

        if (mIsTip && !(mTarget.resId == item.resId)) {
            view.setAlpha(0.5f);
        } else {
            view.setAlpha(1.0f);
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
