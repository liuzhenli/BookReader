package com.liuzhenli.common.utils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Liuzhenli
 * @since 2019-07-07 03:16
 */
public class RxUtil {

    public static <T> DisposableObserver subscribe(Observable<T> observable, DisposableObserver<T> observer) {
        return observable.compose(rxSchedulerHelper()).subscribeWith(observer);
    }

    /**
     * 统一线程处理
     */
    private static <T> ObservableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static <T> SingleSource<T> toSimpleSingle(Single<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableSource<T> toSimpleSingle(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T, R> TwoTuple<T, R> twoTuple(T first, R second) {
        return new TwoTuple<T, R>(first, second);
    }


    public static class TwoTuple<A, B> {
        public final A first;
        public final B second;

        public TwoTuple(A a, B b) {
            this.first = a;
            this.second = b;
        }
    }

}
