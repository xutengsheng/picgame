package com.xts.picgame.base;

public interface IBasePresenter<V extends IBaseView> {

    void attachView(V view);
    void detachView();
}
