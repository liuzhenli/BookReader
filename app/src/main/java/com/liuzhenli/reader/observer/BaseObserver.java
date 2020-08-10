package com.liuzhenli.reader.observer;


import com.liuzhenli.reader.exception.ApiException;
import com.liuzhenli.reader.exception.ExceptionEngine;

import io.reactivex.observers.DisposableObserver;

/**
 * @author Liuzhenli
 * @since 2019-07-06 10:35
 */
public abstract class BaseObserver<T> extends DisposableObserver<T> {
    @Override
    public void onError(Throwable e) {
        e = ExceptionEngine.handleException(e);
        if (e != null) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(123, e));
        }
    }

    protected abstract void onError(ApiException e);
}
