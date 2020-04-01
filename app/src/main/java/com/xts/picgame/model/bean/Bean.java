package com.xts.picgame.model.bean;

import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;

public class Bean {
    public int resId;
    public int type;

    public Bean(@DrawableRes int resId, int type) {
        this.resId = resId;
        this.type = type;
    }
}
