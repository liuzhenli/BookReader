package com.liuzhenli.common.observer;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author Liuzhenli
 * @since 2019-10-24 10:35
 */

public abstract class MyObserver<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }
    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {

    }


}
