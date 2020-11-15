package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.BookSourceContract;
import com.liuzhenli.reader.utils.ThreadUtils;
import com.micoredu.readerlib.bean.BookSourceBean;
import com.micoredu.readerlib.model.BookSourceManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


/**
 * Description:
 *
 * @author liuzhenli 2020/11/9
 * Email: 848808263@qq.com
 */
public class BookSourcePresenter extends RxPresenter<BookSourceContract.View> implements BookSourceContract.Presenter<BookSourceContract.View> {
    @Inject
    public BookSourcePresenter() {
    }

    @Override
    public void getLocalBookSource() {

        DisposableObserver subscribe = RxUtil.subscribe(Observable.create(emitter -> {
            //获取全部书源
            emitter.onNext(BookSourceManager.getAllBookSource());
        }), new SampleProgressObserver<List<BookSourceBean>>() {
            @Override
            public void onNext(List<BookSourceBean> list) {
                mView.showLocalBookSource(list);
            }
        });
        addSubscribe(subscribe);
    }

    @Override
    public void setEnable(BookSourceBean bookSource, boolean enable) {

    }

    @Override
    public void setTop(BookSourceBean bookSource, boolean enable) {
        addSubscribe(RxUtil.subscribe(Observable.create(emitter -> {
            BookSourceManager.toTop(bookSource);
            //get all of the book sources
            emitter.onNext(BookSourceManager.getAllBookSource());
        }), new SampleProgressObserver<List<BookSourceBean>>() {
            @Override
            public void onNext(List<BookSourceBean> list) {
                mView.showLocalBookSource(list);
            }
        }));
    }

    public void saveData(List<BookSourceBean> data) {
        ThreadUtils.getInstance().getExecutorService().execute(() -> BookSourceManager.saveBookSource(data));
    }

    public void saveData(BookSourceBean data) {
        ThreadUtils.getInstance().getExecutorService().execute(() -> BookSourceManager.saveBookSource(data));
    }

    public void delData(BookSourceBean data) {
        ThreadUtils.getInstance().getExecutorService().execute(() -> BookSourceManager.deleteBookSource(data));
    }
}
