/*
package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.DiscoverContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DisposableObserver;

*/
/**
 * Description:
 *
 * @author liuzhenli 2020/9/12
 * Email: 848808263@qq.com
 *//*

public class DiscoverPresenter extends RxPresenter<DiscoverContract.View> implements DiscoverContract.Presenter<DiscoverContract.View> {


    @Inject
    public DiscoverPresenter() {
    }


    @Override
    public void getSource() {

        Observable<List<BookSource>> listObservable = Observable.create(new ObservableOnSubscribe<List<BookSource>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookSource>> emitter) throws Exception {
                List<BookSource> allBookSource = BookSourceManager.getRuleFindEnable();
                emitter.onNext(allBookSource);
            }
        });

        DisposableObserver subscribe = RxUtil.subscribe(listObservable, new SampleProgressObserver<List<BookSource>>() {
            @Override
            public void onNext(List<BookSource> BookSources) {
                mView.showSource(BookSources);
            }
        });
        addSubscribe(subscribe);
    }
}
*/
