package com.liuzhenli.reader.ui.presenter;

import androidx.annotation.NonNull;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.BookShelfContract;
import com.liuzhenli.common.utils.ThreadUtils;
import com.micoredu.reader.bean.BookChapterBean;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.helper.AppReaderDbHelper;
import com.micoredu.reader.model.WebBookModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * describe:
 *
 * @author Liuzhenli on 2020-01-11 15:26
 */
public class BookShelfPresenter extends RxPresenter<BookShelfContract.View> implements BookShelfContract.Presenter<BookShelfContract.View> {
    private int threadsNum = 6;
    private int refreshIndex;
    private List<BookShelfBean> bookShelfBeans;
    private int group;
    private boolean hasUpdate = false;
    private List<String> errBooks = new ArrayList<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public BookShelfPresenter() {
    }

    @Override
    public void queryBooks(Boolean needRefresh, int group) {
        this.group = group;
        if (needRefresh) {
            hasUpdate = false;
            errBooks.clear();
        }
        Observable<List<BookShelfBean>> observable = Observable.create((ObservableOnSubscribe<List<BookShelfBean>>) e -> {
            List<BookShelfBean> bookShelfList;
            if (group == 0) {
                //获取数据库中,书架的所有书籍
                bookShelfList = BookshelfHelper.getAllBook();
            } else {
                //按分类获取
                bookShelfList = BookshelfHelper.getBooksByGroup(group - 1);
            }
            e.onNext(bookShelfList == null ? new ArrayList<>() : bookShelfList);
            e.onComplete();
        });

        addSubscribe(RxUtil.subscribe(observable, new SampleProgressObserver<List<BookShelfBean>>() {
            @Override
            public void onNext(List<BookShelfBean> value) {
                if (null != value) {
                    bookShelfBeans = value;
                    mView.showBooks(bookShelfBeans);
                    if (needRefresh && NetworkUtils.isNetWorkAvailable(BaseApplication.getInstance())) {
                        startRefreshBookShelf();
                    }
                }
            }
        }));

    }

    @Override
    public void removeFromBookShelf(BookShelfBean bookShelfBean) {
        BookshelfHelper.removeFromBookShelf(bookShelfBean);
        mView.onBookRemoved(bookShelfBean);
    }

    private void startRefreshBookShelf() {
        //6线程同时开启
        refreshIndex = -1;
        for (int i = 0; i < 6; i++) {
            refreshBookShelf();
        }
    }

    @Override
    public void refreshBookShelf() {
        refreshIndex++;
        //没有书或者更新完成
        if (bookShelfBeans == null || bookShelfBeans.size() <= refreshIndex) {
            //更新完成
            queryBooks(false, group);
            return;
        }
        BookShelfBean bookShelfBean = bookShelfBeans.get(refreshIndex);
        //判断是不是本地书
        if (bookShelfBean.getTag().equals(BookShelfBean.LOCAL_TAG)) {
            refreshBookShelf();
            return;
        }
        //用户设置了不用更新
        if (!bookShelfBean.getAllowUpdate()) {
            refreshBookShelf();
            return;
        }
        if (AppSharedPreferenceHelper.isRefreshBookShelf()) {
            //view 显示更新状态
            bookShelfBean.setLoading(true);
            mView.setRefreshingBook(bookShelfBean);
            //更新前,章节数
            int chapterNum = bookShelfBean.getChapterListSize();
            Observable<BookShelfBean> chapterList = WebBookModel.getInstance().getChapterList(bookShelfBean)
                    //保存数据
                    .flatMap(new Function<List<BookChapterBean>, ObservableSource<BookShelfBean>>() {
                        @Override
                        public ObservableSource<BookShelfBean> apply(List<BookChapterBean> chapterBeanList) throws Exception {
                            return saveBookToShelfO(bookShelfBean, chapterBeanList);
                        }
                    });
            addSubscribe(RxUtil.subscribe(chapterList, new DisposableObserver<BookShelfBean>() {
                @Override
                public void onNext(@NonNull BookShelfBean data) {
                    //更新结束
                    if (chapterNum < data.getChapterListSize()) {
                    }
                    data.setLoading(false);
                    mView.setRefreshingBook(data);
                }

                @Override
                public void onError(Throwable e) {
                    bookShelfBean.setLoading(false);
                    mView.setRefreshingBook(bookShelfBean);
                }

                @Override
                public void onComplete() {
                    bookShelfBean.setLoading(false);
                    mView.setRefreshingBook(bookShelfBean);
                }
            }));
        }
    }

    @Override
    public void updateBookInfo(BookShelfBean book) {
        ThreadUtils.getInstance().getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                BookshelfHelper.saveBookToShelf(book);
            }
        });
    }

    /**
     * 保存数据
     */
    private Observable<BookShelfBean> saveBookToShelfO(BookShelfBean bookShelfBean, List<BookChapterBean> chapterBeanList) {
        return Observable.create(e -> {
            if (!chapterBeanList.isEmpty()) {
                BookshelfHelper.delChapterList(bookShelfBean.getNoteUrl());
                BookshelfHelper.saveBookToShelf(bookShelfBean);
                AppReaderDbHelper.getInstance().getDatabase().getBookChapterDao().insertOrReplaceInTx(chapterBeanList);
            }
            e.onNext(bookShelfBean);
            e.onComplete();
        });
    }


}
