package com.liuzhenli.reader.ui.presenter;

import com.liuzhenli.common.utils.RxUtil;
import com.liuzhenli.greendao.SearchHistoryBeanDao;
import com.liuzhenli.reader.base.RxPresenter;
import com.liuzhenli.reader.observer.SampleProgressObserver;
import com.liuzhenli.reader.ui.contract.SearchContract;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.bean.SearchHistoryBean;
import com.micoredu.readerlib.helper.BookshelfHelper;
import com.micoredu.readerlib.helper.DbHelper;
import com.micoredu.readerlib.model.SearchBookModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observers.DisposableObserver;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/2
 * Email: 848808263@qq.com
 */
public class SearchPresenter extends RxPresenter<SearchContract.View> implements SearchContract.Presenter<SearchContract.View> {

    private SearchBookModel searchBookModel;

    @Inject
    public SearchPresenter() {
    }

    @Override
    public void addToSearchHistory(int type, String searchKey) {

        Observable<SearchHistoryBean> searchHistoryBeanObservable = Observable.create(new ObservableOnSubscribe<SearchHistoryBean>() {
            @Override
            public void subscribe(ObservableEmitter<SearchHistoryBean> emitter) throws Exception {
                SearchHistoryBeanDao searchHistoryBeanDao = DbHelper.getDaoSession().getSearchHistoryBeanDao();

                List<SearchHistoryBean> list = searchHistoryBeanDao.queryBuilder()
                        .where(SearchHistoryBeanDao.Properties.Type.eq(type), SearchHistoryBeanDao.Properties.Content.eq(searchKey))
                        .limit(1).build().list();
                if (null != list && list.size() > 0) {
                    SearchHistoryBean searchHistoryBean = list.get(0);
                    searchHistoryBean.setDate(System.currentTimeMillis());
                    searchHistoryBeanDao.update(searchHistoryBean);
                } else {
                    SearchHistoryBean searchHistoryBean = new SearchHistoryBean(type, searchKey, System.currentTimeMillis());
                    searchHistoryBeanDao.insertOrReplaceInTx(searchHistoryBean);
                }
            }
        });
        DisposableObserver subscribe = RxUtil.subscribe(searchHistoryBeanObservable, new SampleProgressObserver<SearchHistoryBean>() {
            @Override
            public void onNext(SearchHistoryBean searchHistoryBean) {
                mView.addHistorySuccess();
            }
        });
        addSubscribe(subscribe);

    }

    @Override
    public void clearSearchHistory() {
        SearchHistoryBeanDao searchHistoryBeanDao = DbHelper.getDaoSession().getSearchHistoryBeanDao();
        searchHistoryBeanDao.deleteAll();
        mView.addHistorySuccess();
    }

    @Override
    public void getSearchHistory() {
        SearchHistoryBeanDao searchHistoryBeanDao = DbHelper.getDaoSession().getSearchHistoryBeanDao();
        List<SearchHistoryBean> searchHistoryBeans = searchHistoryBeanDao.loadAll();
        mView.showSearchHistory(searchHistoryBeans);
    }

    @Override
    public void search(int type, int page, String key) {
        searchBookModel = new SearchBookModel(new SearchBookModel.OnSearchListener() {
            @Override
            public void refreshSearchBook() {

            }

            @Override
            public void refreshFinish(Boolean isAll) {
            }

            @Override
            public void loadMoreFinish(Boolean isAll) {

            }

            @Override
            public void loadMoreSearchBook(List<SearchBookBean> searchBookBeanList) {
                mView.showSearchResult(key, searchBookBeanList);
            }

            @Override
            public void searchBookError(Throwable throwable) {

            }

            @Override
            public int getItemCount() {
                return 0;
            }
        });
        List<BookShelfBean> bookshelf = BookshelfHelper.getAllBook();
        long searchTime = System.currentTimeMillis();
        searchBookModel.setSearchTime(searchTime);
        searchBookModel.search(key, searchTime, bookshelf, false);
    }

    @Override
    public void stopSearch() {
        if (searchBookModel != null) {
            searchBookModel.stopSearch();
        }
    }
}
