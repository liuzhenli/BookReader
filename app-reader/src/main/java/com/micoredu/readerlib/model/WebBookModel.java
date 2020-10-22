package com.micoredu.readerlib.model;

import android.annotation.SuppressLint;

import com.hwangjr.rxbus.RxBus;
import com.liuzhenli.common.constant.RxBusTag;
import com.micoredu.readerlib.bean.BaseChapterBean;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.bean.BookContentBean;
import com.micoredu.readerlib.bean.BookInfoBean;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.content.WebBook;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.micoredu.readerlib.helper.DbHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

import static android.text.TextUtils.isEmpty;

import com.liuzhenli.common.constant.AppConstant;

/***处理书籍信息*/
public class WebBookModel {

    public static WebBookModel getInstance() {
        return new WebBookModel();
    }

    /**
     * 网络请求并解析书籍信息
     * return BookShelfBean
     */
    public Observable<BookShelfBean> getBookInfo(BookShelfBean bookShelfBean) {
        return WebBook.getInstance(bookShelfBean.getTag())
                .getBookInfo(bookShelfBean)
                .timeout(AppConstant.TIME_OUT, TimeUnit.SECONDS);
    }

    /**
     * 网络解析图书目录
     * return BookShelfBean
     */
    public Observable<List<BookChapterBean>> getChapterList(final BookShelfBean bookShelfBean) {
        return WebBook.getInstance(bookShelfBean.getTag())
                .getChapterList(bookShelfBean)
                .flatMap((chapterList) -> upChapterList(bookShelfBean, chapterList))
                .timeout(AppConstant.TIME_OUT, TimeUnit.SECONDS);
    }

    /**
     * 章节缓存
     */
    public Observable<BookContentBean> getBookContent(BookShelfBean bookShelfBean, BaseChapterBean chapterBean, BaseChapterBean nextChapterBean) {
        return WebBook.getInstance(chapterBean.getTag())
                .getBookContent(chapterBean, nextChapterBean, bookShelfBean)
                .flatMap((bookContentBean -> saveContent(bookShelfBean.getBookInfoBean(), chapterBean, bookContentBean)))
                .timeout(AppConstant.TIME_OUT, TimeUnit.SECONDS);
    }

    /**
     * 搜索
     */
    public Observable<List<SearchBookBean>> searchBook(String content, int page, String tag) {
        return WebBook.getInstance(tag)
                .searchBook(content, page)
                .timeout(AppConstant.TIME_OUT, TimeUnit.SECONDS);
    }

    /**
     * 发现页
     */
    public Observable<List<SearchBookBean>> findBook(String url, int page, String tag) {
        return WebBook.getInstance(tag)
                .findBook(url, page)
                .timeout(AppConstant.TIME_OUT, TimeUnit.SECONDS);
    }

    /**
     * 更新目录
     */
    private Observable<List<BookChapterBean>> upChapterList(BookShelfBean bookShelfBean, List<BookChapterBean> chapterList) {
        return Observable.create(e -> {
            for (int i = 0; i < chapterList.size(); i++) {
                BookChapterBean chapter = chapterList.get(i);
                chapter.setDurChapterIndex(i);
                chapter.setTag(bookShelfBean.getTag());
                chapter.setNoteUrl(bookShelfBean.getNoteUrl());
            }
            if (bookShelfBean.getChapterListSize() < chapterList.size()) {
                bookShelfBean.setHasUpdate(true);
                bookShelfBean.setFinalRefreshData(System.currentTimeMillis());
                bookShelfBean.getBookInfoBean().setFinalRefreshData(System.currentTimeMillis());
            }
            if (!chapterList.isEmpty()) {
                bookShelfBean.setChapterListSize(chapterList.size());
                bookShelfBean.setDurChapter(Math.min(bookShelfBean.getDurChapter(), bookShelfBean.getChapterListSize() - 1));
                bookShelfBean.setDurChapterName(chapterList.get(bookShelfBean.getDurChapter()).getDurChapterName());
                bookShelfBean.setLastChapterName(chapterList.get(chapterList.size() - 1).getDurChapterName());
            }
            e.onNext(chapterList);
            e.onComplete();
        });
    }

    /**
     * 保存章节
     */
    @SuppressLint("DefaultLocale")
    private Observable<BookContentBean> saveContent(BookInfoBean infoBean, BaseChapterBean chapterBean, BookContentBean bookContentBean) {
        return Observable.create(e -> {
            bookContentBean.setNoteUrl(chapterBean.getNoteUrl());
            if (isEmpty(bookContentBean.getDurChapterContent())) {
                e.onError(new Throwable("下载章节出错"));
            } else if (infoBean.isAudio()) {
                bookContentBean.setTimeMillis(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1));
                DbHelper.getDaoSession().getBookContentBeanDao().insertOrReplace(bookContentBean);
                e.onNext(bookContentBean);
            } else if (BookshelfHelper.saveChapterInfo(infoBean.getName() + "-" + chapterBean.getTag(), chapterBean.getDurChapterIndex(),
                    chapterBean.getDurChapterName(), bookContentBean.getDurChapterContent())) {
                RxBus.get().post(RxBusTag.CHAPTER_CHANGE, chapterBean);
                e.onNext(bookContentBean);
            } else {
                e.onError(new Throwable("保存章节出错"));
            }
            e.onComplete();
        });
    }
}
