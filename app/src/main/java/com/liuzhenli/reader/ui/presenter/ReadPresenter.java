package com.liuzhenli.reader.ui.presenter;


import android.os.AsyncTask;

import com.hwangjr.rxbus.RxBus;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.network.Api;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.ReadContract;
import com.liuzhenli.reader.utils.ThreadUtils;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.micoredu.readerlib.helper.DbHelper;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DisposableObserver;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-10 12:32
 * @since 1.0.0
 */
public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter<ReadContract.View> {


    private Api mApi;
    private List<BookChapterBean> chapterList = new ArrayList<>();

    @Inject
    public ReadPresenter(Api api) {
        this.mApi = api;
    }

    @Override
    public void getBookInfo(String url) {
        Observable<BookShelfBean> observable = Observable.create(new ObservableOnSubscribe<BookShelfBean>() {
            @Override
            public void subscribe(ObservableEmitter<BookShelfBean> emitter) throws Exception {
                //1.数据库的书
                //2.本地的书  根据存储地址
                BookShelfBean book = BookshelfHelper.getBook(url);
                //3.服务器的书
                emitter.onNext(book);
            }
        });


        DisposableObserver subscribe = RxUtil.subscribe(observable, new SampleProgressObserver<BookShelfBean>() {
            @Override
            public void onNext(BookShelfBean book) {

            }
        });
        addSubscribe(subscribe);
    }

    @Override
    public void saveProgress(BookShelfBean bookShelf) {
        if (bookShelf != null) {
            ThreadUtils.getInstance().getExecutorService().execute(new Runnable() {
                @Override
                public void run() {
                    bookShelf.setFinalDate(System.currentTimeMillis());
                    bookShelf.setHasUpdate(false);
                    DbHelper.getDaoSession().getBookShelfBeanDao().insertOrReplace(bookShelf);
                    RxBus.get().post(RxBusTag.UPDATE_BOOK_PROGRESS, bookShelf);
                }
            });
        }
    }

    public void setChapterList(List<BookChapterBean> chapters) {
        this.chapterList = chapters;
    }

    public List<BookChapterBean> getChapterList() {
        return chapterList;
    }

}
