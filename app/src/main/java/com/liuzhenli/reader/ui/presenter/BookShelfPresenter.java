//package com.liuzhenli.reader.ui.presenter;
//
//import androidx.annotation.NonNull;
//
//import com.liuzhenli.common.BaseApplication;
//import com.liuzhenli.common.utils.AppSharedPreferenceHelper;
//import com.liuzhenli.common.utils.NetworkUtils;
//import com.liuzhenli.common.utils.RxUtil;
//import com.liuzhenli.common.base.RxPresenter;
//import com.liuzhenli.common.observer.SampleProgressObserver;
//import com.liuzhenli.reader.ui.contract.BookShelfContract;
//import com.liuzhenli.common.utils.ThreadUtils;
//import com.micoredu.reader.model.WebBookModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.ObservableSource;
//import io.reactivex.disposables.CompositeDisposable;
//import io.reactivex.functions.Function;
//import io.reactivex.observers.DisposableObserver;
//
///**
// * describe:
// *
// * @author Liuzhenli on 2020-01-11 15:26
// */
//public class BookShelfPresenter extends RxPresenter<BookShelfContract.View> implements BookShelfContract.Presenter<BookShelfContract.View> {
//    private int threadsNum = 6;
//    private int refreshIndex;
//    private List<Book> bookShelfBeans;
//    private int group;
//    private boolean hasUpdate = false;
//    private List<String> errBooks = new ArrayList<>();
//    private CompositeDisposable compositeDisposable = new CompositeDisposable();
//
//    @Inject
//    public BookShelfPresenter() {
//    }
//
//    @Override
//    public void queryBooks(Boolean needRefresh, int group) {
//        this.group = group;
//        if (needRefresh) {
//            hasUpdate = false;
//            errBooks.clear();
//        }
//        Observable<List<Book>> observable = Observable.create((ObservableOnSubscribe<List<Book>>) e -> {
//            List<Book> bookShelfList;
//            if (group == 0) {
//                //获取数据库中,书架的所有书籍
//                bookShelfList = BookHelp.getAllBook();
//            } else {
//                //按分类获取
//                bookShelfList = BookHelp.getBooksByGroup(group - 1);
//            }
//            e.onNext(bookShelfList == null ? new ArrayList<>() : bookShelfList);
//            e.onComplete();
//        });
//
//        addSubscribe(RxUtil.subscribe(observable, new SampleProgressObserver<List<Book>>() {
//            @Override
//            public void onNext(List<Book> value) {
//                if (null != value) {
//                    bookShelfBeans = value;
//                    mView.showBooks(bookShelfBeans);
//                    if (needRefresh && NetworkUtils.isNetWorkAvailable(BaseApplication.Companion.getInstance())) {
//                        startRefreshBookShelf();
//                    }
//                }
//            }
//        }));
//
//    }
//
//    @Override
//    public void removeFromBookShelf(Book Book) {
//        BookHelp.removeFromBookShelf(Book);
//        mView.onBookRemoved(Book);
//    }
//
//    private void startRefreshBookShelf() {
//        //6线程同时开启
//        refreshIndex = -1;
//        for (int i = 0; i < 6; i++) {
//            refreshBookShelf();
//        }
//    }
//
//    @Override
//    public void refreshBookShelf() {
//        refreshIndex++;
//        //没有书或者更新完成
//        if (bookShelfBeans == null || bookShelfBeans.size() <= refreshIndex) {
//            //更新完成
//            queryBooks(false, group);
//            return;
//        }
//        Book Book = bookShelfBeans.get(refreshIndex);
//        //判断是不是本地书
//        if (Book.getTag().equals(Book.LOCAL_TAG)) {
//            refreshBookShelf();
//            return;
//        }
//        //用户设置了不用更新
//        if (!Book.getAllowUpdate()) {
//            refreshBookShelf();
//            return;
//        }
//        if (AppSharedPreferenceHelper.isRefreshBookShelf()) {
//            //view 显示更新状态
//            Book.setLoading(true);
//            mView.setRefreshingBook(Book);
//            //更新前,章节数
//            int chapterNum = Book.getChapterListSize();
//            Observable<Book> chapterList = WebBookModel.getInstance().getChapterList(Book)
//                    //保存数据
//                    .flatMap(new Function<List<BookChapter>, ObservableSource<Book>>() {
//                        @Override
//                        public ObservableSource<Book> apply(List<BookChapter> chapterBeanList) throws Exception {
//                            return saveBookToShelfO(Book, chapterBeanList);
//                        }
//                    });
//            addSubscribe(RxUtil.subscribe(chapterList, new DisposableObserver<Book>() {
//                @Override
//                public void onNext(@NonNull Book data) {
//                    //更新结束
//                    if (chapterNum < data.getChapterListSize()) {
//                    }
//                    data.setLoading(false);
//                    mView.setRefreshingBook(data);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Book.setLoading(false);
//                    mView.setRefreshingBook(Book);
//                }
//
//                @Override
//                public void onComplete() {
//                    Book.setLoading(false);
//                    mView.setRefreshingBook(Book);
//                }
//            }));
//        }
//    }
//
//    @Override
//    public void updateBookInfo(Book book) {
//        ThreadUtils.getInstance().getExecutorService().execute(new Runnable() {
//            @Override
//            public void run() {
//                BookHelp.saveBookToShelf(book);
//            }
//        });
//    }
//
//    /**
//     * 保存数据
//     */
//    private Observable<Book> saveBookToShelfO(Book Book, List<BookChapter> chapterBeanList) {
//        return Observable.create(e -> {
//            if (!chapterBeanList.isEmpty()) {
//                BookHelp.delChapterList(Book.getBookUrl());
//                BookHelp.saveBookToShelf(Book);
//                AppReaderDbHelper.getInstance().getDatabase().getBookChapterDao().insertOrReplaceInTx(chapterBeanList);
//            }
//            e.onNext(Book);
//            e.onComplete();
//        });
//    }
//
//
//}
