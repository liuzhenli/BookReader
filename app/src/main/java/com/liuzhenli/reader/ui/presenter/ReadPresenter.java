package com.liuzhenli.reader.ui.presenter;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.hwangjr.rxbus.RxBus;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.FileUtils;
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
import com.micoredu.readerlib.helper.DocumentHelper;


import java.io.File;
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
                BookShelfBean bookShelf = mView.getBookShelf();
                if (bookShelf == null) {
                    bookShelf = BookshelfHelper.getBook(url);
                }

                if (bookShelf == null) {
                    List<BookShelfBean> allBook = BookshelfHelper.getAllBook();
                    if (allBook != null && allBook.size() > 0) {
                        bookShelf = allBook.get(0);
                    }
                }

                if (bookShelf != null && chapterList.isEmpty()) {
                    chapterList = BookshelfHelper.getChapterList(bookShelf.getNoteUrl());
                }

                emitter.onNext(bookShelf);
            }
        });


        DisposableObserver subscribe = RxUtil.subscribe(observable, new SampleProgressObserver<BookShelfBean>() {
            @Override
            public void onNext(BookShelfBean book) {
                mView.showBookInfo(book);
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
                    BookshelfHelper.saveBookToShelf(bookShelf);
                    RxBus.get().post(RxBusTag.UPDATE_BOOK_PROGRESS, bookShelf);
                }
            });
        }
    }

    @Override
    public void getFontFile() {
        String fontPath = FileUtils.getSdCardPath() + "/Fonts";
        try {
            DocumentHelper.createDirIfNotExist(fontPath);
            File file = new File(fontPath);
            File[] files = file.listFiles(pathName -> pathName.getName().toLowerCase().matches(".*\\.[ot]tf"));
            mView.showFontFile(files);
        } catch (Exception e) {
            e.printStackTrace();
            mView.showFontFile(null);
        }
    }

    public void setChapterList(List<BookChapterBean> chapters) {
        this.chapterList = chapters;
        if (chapterList != null && chapterList.size() > 0) {
            ThreadUtils.getInstance().getExecutorService().execute(() -> DbHelper.getDaoSession().getBookChapterBeanDao().insertOrReplaceInTx(chapterList));
        }
    }

    public List<BookChapterBean> getChapterList() {
        return chapterList;
    }

}
