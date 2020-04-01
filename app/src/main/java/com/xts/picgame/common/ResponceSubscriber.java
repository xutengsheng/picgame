package com.xts.picgame.common;


import com.xts.picgame.base.IBaseView;
import com.xts.picgame.utils.LogUtils;

import io.reactivex.subscribers.ResourceSubscriber;

public class ResponceSubscriber<T> extends ResourceSubscriber<T> {
    private IBaseView mView;

    public ResponceSubscriber(IBaseView view){

        mView = view;
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable t) {
        LogUtils.print(t.toString());
        if (mView != null){
            mView.showTips(t.toString());
        }
    }

    @Override
    public void onComplete() {

    }
}
