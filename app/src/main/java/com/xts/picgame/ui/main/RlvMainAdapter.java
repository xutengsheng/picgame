package com.xts.picgame.ui.main;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xts.picgame.R;

import java.util.List;

public class RlvMainAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public List<Integer> mImages;
    public RlvMainAdapter(int layoutResId, @Nullable List<String> data,List<Integer> images) {
        super(layoutResId, data);
        mImages = images;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv,item);

        Glide.with(mContext).load(mImages.get(helper.getLayoutPosition())).into((ImageView) helper.getView(R.id.iv));
    }
}
