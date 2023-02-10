package com.micoredu.reader.ui.presenter;


import androidx.annotation.NonNull;

import com.hwangjr.rxbus.RxBus;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.Constant;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.common.utils.ThreadUtils;
import com.liuzhenli.common.utils.filepicker.util.DateUtils;
import com.micoredu.reader.bean.Book;
import com.micoredu.reader.bean.BookChapter;
import com.micoredu.reader.model.webBook.BookInfo;
import com.micoredu.reader.ui.contract.ReadContract;
import com.liuzhenli.common.utils.ToastUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-10 12:32
 * @since 1.0.0
 */

/*
public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter<ReadContract.View> {


    private List<BookChapter> chapterList = new ArrayList<>();
    private ChangeSourceHelper changeSourceHelp;
    private BookInfo bookInfo;
    private ReadHistoryDao readHistoryDao;
    private long mReadStartTime = System.currentTimeMillis();
    private ReadHistory mReadHistory = new ReadHistory();

    @Inject
    public ReadPresenter() {

    }


    @Override
    public void getBookInfo(String url) {
        Observable<Book> observable = Observable.create(emitter -> {
            Book bookShelf = mView.getBookShelf();
            emitter.onNext(null);
        });

        addSubscribe(RxUtil.subscribe(observable, new SampleProgressObserver<Book>() {
            @Override
            public void onNext(@NonNull Book book) {
                mView.showBookInfo(book);
            }
        }));
    }

    @Override
    public void updateBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
        readHistoryDao = AppReaderDbHelper.getInstance().getDatabase().getReadHistoryDao();
        ReadHistory readHistory = readHistoryDao.getByBookName(bookInfo.getName(), DateUtils.getToadyMillis());
        if (readHistory != null) {
            mReadHistory = readHistory;
        } else {
            mReadHistory.bookName = bookInfo.getName();
            mReadHistory.authorName = bookInfo.getAuthor();
            mReadHistory.bookCover = bookInfo.getCoverUrl();
            mReadHistory.noteUrl = bookInfo.getBookUrl();
            mReadHistory.type = bookInfo.getBookSourceType();
            mReadHistory.dayMillis = DateUtils.getToadyMillis();
        }
    }

    @Override
    public void saveProgress(Book bookShelf) {
        if (bookShelf != null) {
            ThreadUtils.getInstance().getExecutorService().execute(new Runnable() {
                @Override
                public void run() {
                    bookShelf.setFinalDate(System.currentTimeMillis());
                    bookShelf.setHasUpdate(false);
                    BookHelp.saveBookToShelf(bookShelf);
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
            File file = new File(Constant.FONT_PATH);
            File[] files = file.listFiles(pathName -> pathName.getName().toLowerCase().matches(".*\\.[ot]tf"));
            mView.showFontFile(files);
        } catch (Exception e) {
            e.printStackTrace();
            mView.showFontFile(null);
        }
    }

    @Override
    public void changeBookSource() {
        if (changeSourceHelp == null) {
            changeSourceHelp = new ChangeSourceHelper();
        }
        changeSourceHelp.autoChange(mView.getBookShelf(), new ChangeSourceHelper.ChangeSourceListener() {

            @Override
            public void finish(Book Book, List<BookChapter> chapterBeanList) {
                if (!chapterBeanList.isEmpty()) {
                    RxBus.get().post(RxBusTag.HAD_REMOVE_BOOK, mView.getBookShelf());
                    RxBus.get().post(RxBusTag.HAD_ADD_BOOK, Book);
                    chapterList = chapterBeanList;
                    mView.showChangeBookSourceResult(Book);
                } else {
                    mView.showChangeBookSourceResult(null);
                }
            }

            @Override
            public void error(Throwable throwable) {
                ToastUtil.showToast(throwable.getMessage());
                mView.showChangeBookSourceResult(null);
            }
        });
    }

    @Override
    public void saveReadHistory() {
        ThreadUtils.getInstance().getExecutorService().execute(() -> {
            if (System.currentTimeMillis() - mReadStartTime > 20 * 60 * 1000) {
                mReadHistory.sumTime += 20 * 60 * 1000;
            } else {
                mReadHistory.sumTime += (System.currentTimeMillis() - mReadStartTime);
            }
            RxBus.get().post(RxBusTag.UPDATE_READ, true);
            ReadHistory byBookName = readHistoryDao.getByBookName(mReadHistory.bookName, mReadHistory.dayMillis);
            if (byBookName == null) {
                readHistoryDao.insertOrReplace(mReadHistory);
            } else {
                readHistoryDao.update(mReadHistory);
            }
            mReadStartTime = System.currentTimeMillis();
        });

    }

    public void setChapterList(List<BookChapter> chapters) {
        this.chapterList = chapters;
        if (chapterList != null && chapterList.size() > 0) {
            ThreadUtils.getInstance().getExecutorService().execute(() ->
                    AppReaderDbHelper.getInstance().getDatabase().getBookChapterDao().insertOrReplaceInTx(chapterList));
        }
    }

    public List<BookChapter> getChapterList() {
        return chapterList;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (changeSourceHelp != null) {
            changeSourceHelp.stopSearch();
        }
    }
}


 */
