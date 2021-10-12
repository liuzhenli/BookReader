package com.liuzhenli.reader.ui.presenter;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.hwangjr.rxbus.RxBus;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.BookDetailContract;
import com.micoredu.reader.bean.BookChapterBean;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.model.WebBookModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-14 11:00
 */
public class BookDetailPresenter extends RxPresenter<BookDetailContract.View> implements BookDetailContract.Presenter<BookDetailContract.View> {

    @Inject
    public BookDetailPresenter() {
    }

    @Override
    public void getBookInfo(BookShelfBean bookShelfBean, boolean isInBookShelf) {
        Observable<List<BookChapterBean>> listObservable;
        //local book
        if (TextUtils.equals(bookShelfBean.getTag(), BookShelfBean.LOCAL_TAG)) {
            listObservable = Observable.create((ObservableOnSubscribe<List<BookChapterBean>>) emitter -> {
                List<BookChapterBean> chapterList = AppReaderDbHelper.getInstance().getDatabase()
                        .getBookChapterDao().getChapterList(bookShelfBean.getNoteUrl());
                emitter.onNext(chapterList);
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            //net book
        } else {
            listObservable = WebBookModel.getInstance().getBookInfo(bookShelfBean)
                    .flatMap(bookShelfBean1 -> WebBookModel.getInstance().getChapterList(bookShelfBean1))
                    .flatMap((Function<List<BookChapterBean>, ObservableSource<List<BookChapterBean>>>)
                            bookChapterBeans -> saveBookToShelfO(bookShelfBean, bookChapterBeans, isInBookShelf));
        }

        addSubscribe(RxUtil.subscribe(listObservable, new SampleProgressObserver<List<BookChapterBean>>() {
            @Override
            public void onNext(@NonNull List<BookChapterBean> bookChapterBeans) {
                bookShelfBean.setChapterListSize(bookChapterBeans.size());
                mView.showBookInfo(bookShelfBean.getBookInfoBean(), bookChapterBeans);
            }
        }));
    }

    @Override
    public void getBookSource(BookShelfBean bookShelfBean) {

    }

    /**
     * 保存数据
     */
    private Observable<List<BookChapterBean>> saveBookToShelfO(BookShelfBean bookShelfBean,
                                                               List<BookChapterBean> chapterBeans,
                                                               boolean isInBookShelf) {
        return Observable.create(e -> {
            if (isInBookShelf) {
                BookshelfHelper.saveBookToShelf(bookShelfBean);
                if (!chapterBeans.isEmpty()) {
                    BookshelfHelper.delChapterList(bookShelfBean.getNoteUrl());
                    AppReaderDbHelper.getInstance().getDatabase().getBookChapterDao().insertOrReplaceInTx(chapterBeans);
                }
                RxBus.get().post(RxBusTag.HAD_ADD_BOOK, bookShelfBean);
            }
            e.onNext(chapterBeans);
            e.onComplete();
        });
    }
}
