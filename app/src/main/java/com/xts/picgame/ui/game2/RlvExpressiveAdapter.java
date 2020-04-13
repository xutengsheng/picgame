package com.xts.picgame.ui.game2;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xts.picgame.R;
import com.xts.picgame.model.bean.DataBean;

import java.util.List;

public class RlvExpressiveAdapter extends BaseQuickAdapter<DataBean, BaseViewHolder> {
    public RlvExpressiveAdapter(int layoutResId, @Nullable List<DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean item) {
        Glide.with(mContext).load(item.getUrl()).into((ImageView) helper.getView(R.id.iv));
    }
}
