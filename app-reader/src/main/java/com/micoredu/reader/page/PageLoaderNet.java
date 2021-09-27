package com.micoredu.reader.page;

import android.annotation.SuppressLint;

import com.liuzhenli.common.BaseApplication;
import com.liuzhenli.common.observer.MyObserver;
import com.liuzhenli.common.utils.NetworkUtils;
import com.liuzhenli.common.utils.RxUtil;
import com.micoredu.reader.bean.BookChapterBean;
import com.micoredu.reader.bean.BookContentBean;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.content.VipThrowable;
import com.micoredu.reader.content.WebBook;
import com.micoredu.reader.helper.BookshelfHelper;
import com.micoredu.reader.model.WebBookModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络页面加载器
 */

public class PageLoaderNet extends PageLoader {
    private static final String TAG = "PageLoaderNet";
    private List<String> downloadingChapterList = new ArrayList<>();
    private ExecutorService executorService;
    private Scheduler scheduler;

    public PageLoaderNet(PageView pageView, BookShelfBean bookShelfBean, Callback callback) {
        super(pageView, bookShelfBean, callback);
        executorService = Executors.newFixedThreadPool(20);
        scheduler = Schedulers.from(executorService);
    }

    @Override
    public void refreshChapterList() {
        if (!mCallback.getChapterList().isEmpty()) {
            isChapterListPrepare = true;
            // 打开章节
            skipToChapter(mBookShelfBean.getDurChapter(), mBookShelfBean.getDurChapterPage());
        } else {
            WebBookModel.getInstance().getChapterList(mBookShelfBean)
                    .compose(RxUtil::toSimpleSingle)
                    .subscribe(new MyObserver<List<BookChapterBean>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onNext(List<BookChapterBean> chapterBeanList) {
                            isChapterListPrepare = true;
                            // 目录加载完成
                            if (!chapterBeanList.isEmpty()) {
                                BookshelfHelper.delChapterList(mBookShelfBean.getNoteUrl());
                                mCallback.onCategoryFinish(chapterBeanList);
                            }
                            // 加载并显示当前章节
                            skipToChapter(mBookShelfBean.getDurChapter(), mBookShelfBean.getDurChapterPage());
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof WebBook.NoSourceThrowable) {
                                mPageView.autoChangeSource();
                            } else {
                                durChapterError(e.getMessage());
                            }
                        }
                    });
        }
    }


    public void changeSourceFinish(BookShelfBean bookShelfBean) {
        if (bookShelfBean == null) {
            openChapter(mBookShelfBean.getDurChapter());
        } else {
            this.mBookShelfBean = bookShelfBean;
            refreshChapterList();
        }
    }

    @SuppressLint("DefaultLocale")
    private synchronized void loadContent(final int chapterIndex) {
        if (downloadingChapterList.size() >= 20) return;
        if (chapterIndex >= mCallback.getChapterList().size()
                || DownloadingList(listHandle.CHECK, mCallback.getChapterList().get(chapterIndex).getDurChapterUrl()))
            return;
        if (null != mBookShelfBean && mCallback.getChapterList().size() > 0) {
            Observable.create((ObservableOnSubscribe<Integer>) e -> {
                if (shouldRequestChapter(chapterIndex)) {
                    DownloadingList(listHandle.ADD, mCallback.getChapterList().get(chapterIndex).getDurChapterUrl());
                    e.onNext(chapterIndex);
                }
                e.onComplete();
            })
                    .flatMap(index -> WebBookModel.getInstance().getBookContent(mBookShelfBean, mCallback.getChapterList().get(chapterIndex), null))
                    .subscribeOn(scheduler)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MyObserver<BookContentBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @SuppressLint("DefaultLocale")
                        @Override
                        public void onNext(BookContentBean bookContentBean) {
                            DownloadingList(listHandle.REMOVE, bookContentBean.getDurChapterUrl());
                            finishContent(bookContentBean.getDurChapterIndex());
                        }

                        @Override
                        public void onError(Throwable e) {
                            DownloadingList(listHandle.REMOVE, mCallback.getChapterList().get(chapterIndex).getDurChapterUrl());
                            if (chapterIndex == mBookShelfBean.getDurChapter()) {
                                if (e instanceof WebBook.NoSourceThrowable) {
                                    mPageView.autoChangeSource();
                                } else if (e instanceof VipThrowable) {
                                    mCallback.vipPop();
                                } else {
                                    durChapterError(e.getMessage());
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 编辑下载列表
     */
    private synchronized boolean DownloadingList(listHandle editType, String value) {
        if (editType == listHandle.ADD) {
            downloadingChapterList.add(value);
            return true;
        } else if (editType == listHandle.REMOVE) {
            downloadingChapterList.remove(value);
            return true;
        } else {
            return downloadingChapterList.indexOf(value) != -1;
        }
    }

    /**
     * 章节下载完成
     */
    private void finishContent(int chapterIndex) {
        if (chapterIndex == mCurChapterPos) {
            super.parseCurChapter();
        }
        if (chapterIndex == mCurChapterPos - 1) {
            super.parsePrevChapter();
        }
        if (chapterIndex == mCurChapterPos + 1) {
            super.parseNextChapter();
        }
    }

    /**
     * 刷新当前章节
     */
    @SuppressLint("DefaultLocale")
    public void refreshDurChapter() {
        if (mCallback.getChapterList().isEmpty()) {
            updateChapter();
            return;
        }
        if (mCallback.getChapterList().size() - 1 < mCurChapterPos) {
            mCurChapterPos = mCallback.getChapterList().size() - 1;
        }
        BookshelfHelper.delChapter(BookshelfHelper.getCachePathName(mBookShelfBean.getBookInfoBean().getName(), mBookShelfBean.getTag()),
                mCurChapterPos, mCallback.getChapterList().get(mCurChapterPos).getDurChapterName());
        skipToChapter(mCurChapterPos, 0);
    }

    @Override
    protected String getChapterContent(BookChapterBean chapter) {
        return BookshelfHelper.getChapterCache(mBookShelfBean, chapter);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected boolean noChapterData(BookChapterBean chapter) {
        return !BookshelfHelper.isChapterCached(mBookShelfBean.getBookInfoBean().getName(), mBookShelfBean.getTag(), chapter, mBookShelfBean.isAudio());
    }

    private boolean shouldRequestChapter(Integer chapterIndex) {
        return NetworkUtils.isNetWorkAvailable(BaseApplication.getInstance()) && noChapterData(mCallback.getChapterList().get(chapterIndex));
    }

    // 装载上一章节的内容
    @Override
    void parsePrevChapter() {
        if (mCurChapterPos >= 1) {
            loadContent(mCurChapterPos - 1);
        }
        super.parsePrevChapter();
    }

    // 装载当前章内容。
    @Override
    void parseCurChapter() {
        for (int i = mCurChapterPos; i < Math.min(mCurChapterPos + 5, mBookShelfBean.getChapterListSize()); i++) {
            loadContent(i);
        }
        super.parseCurChapter();
    }

    // 装载下一章节的内容
    @Override
    void parseNextChapter() {
        for (int i = mCurChapterPos; i < Math.min(mCurChapterPos + 5, mBookShelfBean.getChapterListSize()); i++) {
            loadContent(i);
        }
        super.parseNextChapter();
    }

    @Override
    public void updateChapter() {
        mPageView.getActivity().toast("目录更新中");
        WebBookModel.getInstance().getChapterList(mBookShelfBean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookChapterBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<BookChapterBean> chapterBeanList) {
                        isChapterListPrepare = true;

                        if (chapterBeanList.size() > mCallback.getChapterList().size()) {
                            mPageView.getActivity().toast("更新完成,有新章节");
                            mCallback.onCategoryFinish(chapterBeanList);
                        } else {
                            mPageView.getActivity().toast("更新完成,无新章节");
                        }

                        // 加载并显示当前章节
                        skipToChapter(mBookShelfBean.getDurChapter(), mBookShelfBean.getDurChapterPage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        durChapterError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void closeBook() {
        super.closeBook();
        executorService.shutdown();
    }

    public enum listHandle {
        ADD, REMOVE, CHECK
    }
}
