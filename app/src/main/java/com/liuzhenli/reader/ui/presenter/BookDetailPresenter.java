/*
package com.liuzhenli.reader.ui.presenter;


import androidx.annotation.NonNull;

import com.hwangjr.rxbus.RxBus;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.BookDetailContract;
import com.micoredu.reader.bean.Book;
import com.micoredu.reader.bean.BookChapter;
import com.micoredu.reader.model.webBook.WebBook;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

*/
/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-14 11:00
 *//*

public class BookDetailPresenter extends RxPresenter<BookDetailContract.View> implements BookDetailContract.Presenter<BookDetailContract.View> {

    @Inject
    public BookDetailPresenter() {
    }

    @Override
    public void getBookInfo(Book book, boolean isInBookShelf) {
        Observable<List<BookChapter>> listObservable;
        //local book
        if (book.isLocal()) {
            listObservable = Observable.create((ObservableOnSubscribe<List<BookChapter>>) emitter -> {
                List<BookChapter> chapterList = AppReaderDbHelper.getInstance().getDatabase()
                        .getBookChapterDao().getChapterList(book.getBookUrl());
                emitter.onNext(chapterList);
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            //net book
        } else {
            listObservable = WebBook.INSTANCE.getBookInfo(book)
                    .flatMap(Book1 ->  WebBook.INSTANCE.getChapterList(Book1))
                    .flatMap((Function<List<BookChapter>, ObservableSource<List<BookChapter>>>)
                            BookChapters -> saveBookToShelfO(book, BookChapters, isInBookShelf));
        }

        addSubscribe(RxUtil.subscribe(listObservable, new SampleProgressObserver<List<BookChapter>>() {
            @Override
            public void onNext(@NonNull List<BookChapter> BookChapters) {
                book.setTotalChapterNum(BookChapters.size());
                mView.showBookInfo(book.getBookInfoBean(), BookChapters);
            }
        }));
    }

    @Override
    public void getBookSource(Book Book) {

    }

    */
/**
     * 保存数据
     *//*

    private Observable<List<BookChapter>> saveBookToShelfO(Book book,
                                                               List<BookChapter> chapterBeans,
                                                               boolean isInBookShelf) {
        return Observable.create(e -> {
            if (isInBookShelf) {
                BookHelp.saveBookToShelf(book);
                if (!chapterBeans.isEmpty()) {
                    BookHelp.delChapterList(book.getBookUrl());
                    AppReaderDbHelper.getInstance().getDatabase().getBookChapterDao().insertOrReplaceInTx(chapterBeans);
                }
                RxBus.get().post(RxBusTag.HAD_ADD_BOOK, book);
            }
            e.onNext(chapterBeans);
            e.onComplete();
        });
    }
}
*/
