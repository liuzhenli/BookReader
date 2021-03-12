package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.greendao.SearchBookBeanDao;
import com.liuzhenli.common.base.RxPresenter;
import com.liuzhenli.common.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.ChangeSourceContract;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.bean.SearchBookBean;
import com.micoredu.reader.content.UpLastChapterModel;
import com.micoredu.reader.helper.DbHelper;
import com.micoredu.reader.model.BookSourceManager;
import com.micoredu.reader.model.SearchBookModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-14 11:00
 */
public class ChangeSourcePresenter extends RxPresenter<ChangeSourceContract.View> implements ChangeSourceContract.Presenter<ChangeSourceContract.View> {

    private String bookTag;

    @Inject
    public ChangeSourcePresenter() {

    }

    @Override
    public void getSearchBookInDatabase(BookShelfBean bookShelfBean, SearchBookModel searchBookModel) {

        if (bookShelfBean != null && bookShelfBean.getBookInfoBean() != null) {
            String bookName = bookShelfBean.getBookInfoBean().getName();
            String bookAuthor = bookShelfBean.getBookInfoBean().getAuthor();
            bookTag = bookShelfBean.getBookInfoBean().getTag();

            Observable<List<SearchBookBean>> objectObservable = Observable.create(emitter -> {

                List<SearchBookBean> searchBookBeans = DbHelper.getDaoSession().getSearchBookBeanDao().queryBuilder()
                        .where(SearchBookBeanDao.Properties.Name.eq(bookName), SearchBookBeanDao.Properties.Author.eq(bookAuthor)).build().list();
                if (searchBookBeans == null) searchBookBeans = new ArrayList<>();
                List<SearchBookBean> searchBookList = new ArrayList<>();
                List<BookSourceBean> bookSourceList = new ArrayList<>(BookSourceManager.getSelectedBookSource());
                if (bookSourceList.size() > 0) {
                    for (BookSourceBean bookSourceBean : BookSourceManager.getSelectedBookSource()) {
                        boolean hasSource = false;
                        for (SearchBookBean searchBookBean : new ArrayList<>(searchBookBeans)) {
                            if (Objects.equals(searchBookBean.getTag(), bookSourceBean.getBookSourceUrl())) {
                                bookSourceList.remove(bookSourceBean);
                                searchBookList.add(searchBookBean);
                                hasSource = true;
                                break;
                            }
                        }
                        if (hasSource) {
                            bookSourceList.remove(bookSourceBean);
                        }
                    }
                    searchBookModel.searchReNew();
                    searchBookModel.initSearchEngineS(bookSourceList);
                    long startThisSearchTime = System.currentTimeMillis();
                    searchBookModel.setSearchTime(startThisSearchTime);
                    List<BookShelfBean> bookList = new ArrayList<>();
                    bookList.add(bookShelfBean);
                    searchBookModel.search(bookName, startThisSearchTime, bookList, false);
                    UpLastChapterModel.getInstance().startUpdate(searchBookList);
                }
                if (searchBookList.size() > 0) {
                    for (SearchBookBean searchBookBean : searchBookList) {
                        if (searchBookBean.getTag().equals(bookShelfBean.getTag())) {
                            searchBookBean.setIsCurrentSource(true);
                        } else {
                            searchBookBean.setIsCurrentSource(false);
                        }
                    }
                    Collections.sort(searchBookList, this::compareSearchBooks);
                }
                emitter.onNext(searchBookList);
            });
            addSubscribe(RxUtil.subscribe(objectObservable, new SampleProgressObserver<List<SearchBookBean>>() {
                @Override
                public void onNext(@NonNull List<SearchBookBean> searchBookBeans) {
                    mView.showSearchBook(searchBookBeans);
                }
            }));

        }
    }

    @Override
    public int compareSearchBooks(SearchBookBean s1, SearchBookBean s2) {
        boolean s1tag = s1.getTag().equals(bookTag);
        boolean s2tag = s2.getTag().equals(bookTag);
        if (s2tag && !s1tag)
            return 1;
        else if (s1tag && !s2tag)
            return -1;
        int result = Long.compare(s2.getAddTime(), s1.getAddTime());
        if (result != 0)
            return result;
        result = Integer.compare(s2.getLastChapterNum(), s1.getLastChapterNum());
        if (result != 0)
            return result;
        return Integer.compare(s2.getWeight(), s1.getWeight());
    }
}
