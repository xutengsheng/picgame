package com.xts.picgame.base;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter<V extends IBaseView> implements IBasePresenter<V> {
    CompositeDisposable mCompositeDisposable;

    public V mView;
    private WeakReference<V> mReference;

    @Override
    public void attachView(V view) {
        mReference = new WeakReference<>(view);
        mView = mReference.get();
    }

    @Override
    public void detachView() {
        mView = null;
        if (mCompositeDisposable != null){
            mCompositeDisposable.clear();
        }
    }

    public void addDisposable(Disposable d){
        if (mCompositeDisposable == null){
            mCompositeDisposable = new CompositeDisposable();
        }

        mCompositeDisposable.add(d);
    }
}
