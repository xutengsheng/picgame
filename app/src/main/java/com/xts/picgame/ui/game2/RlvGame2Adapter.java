package com.xts.picgame.ui.game2;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xts.picgame.R;
import com.xts.picgame.model.bean.Bean;

import java.util.List;

public class RlvGame2Adapter extends BaseQuickAdapter<Bean, BaseViewHolder> {
    public RlvGame2Adapter(int layoutResId, @Nullable List<Bean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Bean item) {
        Glide.with(mContext).load(item.resId).into((ImageView) helper.getView(R.id.iv));
    }
}
