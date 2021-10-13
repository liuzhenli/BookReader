package com.micoredu.reader.ui.presenter;


import com.hwangjr.rxbus.RxBus;
import com.liuzhenli.common.constant.RxBusTag;
import com.liuzhenli.common.utils.FileUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.common.utils.ThreadUtils;
import com.liuzhenli.common.utils.filepicker.util.DateUtils;
import com.micoredu.reader.bean.BookInfoBean;
import com.micoredu.reader.bean.ReadHistory;
import com.micoredu.reader.dao.ReadHistoryDao;
import com.micoredu.reader.ui.contract.ReadContract;
import com.liuzhenli.common.utils.ToastUtil;
import com.micoredu.reader.bean.BookChapterBean;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.helper.ChangeSourceHelper;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.helper.DocumentHelper;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-11-10 12:32
 * @since 1.0.0
 */
public class ReadPresenter extends RxPresenter<ReadContract.View> implements ReadContract.Presenter<ReadContract.View> {


    private List<BookChapterBean> chapterList = new ArrayList<>();
    private ChangeSourceHelper changeSourceHelp;
    private BookInfoBean bookInfo;
    private ReadHistoryDao readHistoryDao;
    private long mReadStartTime = System.currentTimeMillis();
    private ReadHistory mReadHistory = new ReadHistory();

    @Inject
    public ReadPresenter() {

    }


    @Override
    public void getBookInfo(String url) {
        Observable<BookShelfBean> observable = Observable.create(new ObservableOnSubscribe<BookShelfBean>() {
            @Override
            public void subscribe(ObservableEmitter<BookShelfBean> emitter) throws Exception {
                BookShelfBean bookShelf = mView.getBookShelf();

                emitter.onNext(null);
            }
        });


        addSubscribe(RxUtil.subscribe(observable, new SampleProgressObserver<BookShelfBean>() {
            @Override
            public void onNext(BookShelfBean book) {
                mView.showBookInfo(book);
            }
        }));
    }

    @Override
    public void updateBookInfo(BookInfoBean bookInfo) {
        this.bookInfo = bookInfo;
        readHistoryDao = AppReaderDbHelper.getInstance().getDatabase().getReadHistoryDao();
        ReadHistory readHistory = readHistoryDao.getByBookName(bookInfo.getName(), DateUtils.getToadyMillis());
        if (readHistory != null) {
            mReadHistory = readHistory;
        } else {
            mReadHistory.bookName = bookInfo.getName();
            mReadHistory.authorName = bookInfo.getAuthor();
            mReadHistory.bookCover = bookInfo.getCoverUrl();
            mReadHistory.noteUrl = bookInfo.getNoteUrl();
            mReadHistory.type = bookInfo.getBookSourceType();
            mReadHistory.dayMillis = DateUtils.getToadyMillis();
        }
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

    @Override
    public void changeBookSource() {
        if (changeSourceHelp == null) {
            changeSourceHelp = new ChangeSourceHelper();
        }
        changeSourceHelp.autoChange(mView.getBookShelf(), new ChangeSourceHelper.ChangeSourceListener() {

            @Override
            public void finish(BookShelfBean bookShelfBean, List<BookChapterBean> chapterBeanList) {
                if (!chapterBeanList.isEmpty()) {
                    RxBus.get().post(RxBusTag.HAD_REMOVE_BOOK, mView.getBookShelf());
                    RxBus.get().post(RxBusTag.HAD_ADD_BOOK, bookShelfBean);
                    chapterList = chapterBeanList;
                    mView.showChangeBookSourceResult(bookShelfBean);
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
            readHistoryDao.insertOrReplace(mReadHistory);
            mReadStartTime = System.currentTimeMillis();
        });

    }

    public void setChapterList(List<BookChapterBean> chapters) {
        this.chapterList = chapters;
        if (chapterList != null && chapterList.size() > 0) {
            ThreadUtils.getInstance().getExecutorService().execute(() -> AppReaderDbHelper.getInstance().getDatabase().getBookChapterDao().insertOrReplaceInTx(chapterList));
        }
    }

    public List<BookChapterBean> getChapterList() {
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
