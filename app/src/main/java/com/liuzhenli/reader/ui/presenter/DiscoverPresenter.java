package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.DiscoverContract;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.model.BookSourceManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DisposableObserver;

/**
 * Description:
 *
 * @author liuzhenli 2020/9/12
 * Email: 848808263@qq.com
 */
public class DiscoverPresenter extends RxPresenter<DiscoverContract.View> implements DiscoverContract.Presenter<DiscoverContract.View> {

    private Api mApi;

    @Inject
    public DiscoverPresenter(Api api) {
        this.mApi = api;
    }


    @Override
    public void getSource() {

        Observable<List<BookSourceBean>> listObservable = Observable.create(new ObservableOnSubscribe<List<BookSourceBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<BookSourceBean>> emitter) throws Exception {
                List<BookSourceBean> allBookSource = BookSourceManager.getRuleFindEnable();
                emitter.onNext(allBookSource);

            }
        });

        DisposableObserver subscribe = RxUtil.subscribe(listObservable, new SampleProgressObserver<List<BookSourceBean>>() {
            @Override
            public void onNext(List<BookSourceBean> bookSourceBeans) {
                mView.showSource(bookSourceBeans);
            }
        });
        addSubscribe(subscribe);
    }
}
