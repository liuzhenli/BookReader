//package com.liuzhenli.reader.ui.presenter;
//
//import android.text.TextUtils;
//
//import com.liuzhenli.common.utils.RxUtil;
//import com.liuzhenli.common.base.RxPresenter;
//import com.liuzhenli.common.observer.SampleProgressObserver;
//import com.liuzhenli.reader.ui.activity.SearchActivity;
//import com.liuzhenli.reader.ui.contract.SearchContract;
//import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
//import com.micoredu.reader.model.SearchBookModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import io.reactivex.Observable;
//import io.reactivex.ObservableEmitter;
//import io.reactivex.ObservableOnSubscribe;
//import io.reactivex.observers.DisposableObserver;
//
///**
// * Description:
// *
// * @author liuzhenli 2020/11/2
// * Email: 848808263@qq.com
// */
//public class SearchPresenter extends RxPresenter<SearchContract.View> implements SearchContract.Presenter<SearchContract.View> {
//
//    private SearchBookModel searchBookModel;
//    public boolean isSearching;
//
//    @Inject
//    public SearchPresenter() {
//    }
//
//    /**
//     * 添加到数据库
//     *
//     * @param type      {@link SearchActivity.SearchType}
//     * @param searchKey the words users wants to search
//     */
//    @Override
//    public void addToSearchHistory(int type, String searchKey) {
//
//        Observable<SearchHistoryBean> searchHistoryBeanObservable = Observable.create(new ObservableOnSubscribe<SearchHistoryBean>() {
//            @Override
//            public void subscribe(ObservableEmitter<SearchHistoryBean> emitter) throws Exception {
//                SearchHistoryDao searchHistoryBeanDao = AppReaderDbHelper.getInstance().getDatabase().getSearchHistoryDao();
//
//                List<SearchHistoryBean> list = searchHistoryBeanDao.getHistoryByType(type, searchKey);
//                if (null != list && list.size() > 0) {
//                    SearchHistoryBean searchHistoryBean = list.get(0);
//                    searchHistoryBean.setDate(System.currentTimeMillis());
//                    searchHistoryBeanDao.update(searchHistoryBean);
//                } else {
//                    SearchHistoryBean searchHistoryBean = new SearchHistoryBean(type, searchKey, System.currentTimeMillis());
//                    searchHistoryBeanDao.insertOrReplaceInTx(searchHistoryBean);
//                }
//            }
//        });
//        DisposableObserver subscribe = RxUtil.subscribe(searchHistoryBeanObservable, new SampleProgressObserver<SearchHistoryBean>() {
//            @Override
//            public void onNext(SearchHistoryBean searchHistoryBean) {
//                mView.addHistorySuccess();
//            }
//        });
//        addSubscribe(subscribe);
//
//    }
//
//    @Override
//    public void clearSearchHistory() {
//        SearchHistoryDao searchHistoryBeanDao = AppReaderDbHelper.getInstance().getDatabase().getSearchHistoryDao();
//        searchHistoryBeanDao.deleteAll();
//        mView.addHistorySuccess();
//    }
//
//    @Override
//    public void removeSearchHistoryItem(SearchHistoryBean data) {
//        SearchHistoryDao searchHistoryBeanDao = AppReaderDbHelper.getInstance().getDatabase().getSearchHistoryDao();
//        searchHistoryBeanDao.delete(data);
//    }
//
//    @Override
//    public void getSearchHistory() {
//        SearchHistoryDao searchHistoryBeanDao = AppReaderDbHelper.getInstance().getDatabase().getSearchHistoryDao();
//        List<SearchHistoryBean> searchHistoryBeans = searchHistoryBeanDao.all();
//        mView.showSearchHistory(searchHistoryBeans);
//    }
//
//    @Override
//    public void search(int type, int page, String key, RecyclerArrayAdapter<SearchBook> mAdapter) {
//        isSearching = true;
//        searchBookModel = new SearchBookModel(new SearchBookModel.OnSearchListener() {
//            @Override
//            public void refreshSearchBook() {
//
//            }
//
//            @Override
//            public void refreshFinish(Boolean isAll) {
//            }
//
//            @Override
//            public void loadMoreFinish(Boolean isAll) {
//
//            }
//
//            @Override
//            public void loadMoreSearchBook(List<SearchBook> searchBookBeanList) {
//                configSearchResult(key, searchBookBeanList, mAdapter);
//            }
//
//            @Override
//            public void searchBookError(Throwable throwable) {
//
//            }
//
//            @Override
//            public int getItemCount() {
//                return 0;
//            }
//        });
//        List<Book> bookshelf = BookHelp.getAllBook();
//        long searchTime = System.currentTimeMillis();
//        searchBookModel.setSearchTime(searchTime);
//        searchBookModel.search(key, searchTime, bookshelf, false);
//    }
//
//
//    private synchronized void configSearchResult(String key, List<SearchBook> searchResult, RecyclerArrayAdapter<SearchBook> mAdapter) {
//
//        List<SearchBook> bookList = new ArrayList<>();
//        Observable<List<SearchBook>> listObservable = Observable.create(new ObservableOnSubscribe<List<SearchBook>>() {
//            @Override
//            public void subscribe(ObservableEmitter<List<SearchBook>> emitter) throws Exception {
//                //添加数据
//                ArrayList<SearchBook> allBooks = new ArrayList<>(mAdapter.getRealAllData());
//                //搜索结果存入数据库
//                AppReaderDbHelper.getInstance().getDatabase().getSearchBookDao().insertOrReplaceInTx(searchResult);
//
//                //去重
//                for (int i = 0; i < searchResult.size(); i++) {
//                    boolean hasSameBook = false;
//                    for (int j = 0; j < allBooks.size(); j++) {
//                        //作者名和书名都相同,视为同一本书  同一本书的书源+1
//                        if (TextUtils.equals(allBooks.get(j).getName(), searchResult.get(i).getName())
//                                && TextUtils.equals(allBooks.get(j).getAuthor(), searchResult.get(i).getAuthor())) {
//                            allBooks.get(j).addOriginUrl(searchResult.get(i).getTag());
//                            hasSameBook = true;
//                            break;
//                        }
//                    }
//
//                    if (!hasSameBook) {
//                        allBooks.add(searchResult.get(i));
//                    }
//                }
//                //处理数据的逻辑:1.书名和搜索的词语相同的放最上面  2.作者和搜索词相同的其次  3.标签和搜索词相同
//                if (allBooks.size() > 0) {
//                    for (int i = 0; i < allBooks.size(); i++) {
//                        if (TextUtils.equals(allBooks.get(i).getName(), key)) {
//                            bookList.add(allBooks.get(i));
//                            allBooks.remove(i);
//                            i--;
//                        }
//                    }
//                    for (int i = 0; i < allBooks.size(); i++) {
//                        if (TextUtils.equals(allBooks.get(i).getAuthor(), key)) {
//                            bookList.add(allBooks.get(i));
//                            allBooks.remove(i);
//                            i--;
//                        }
//                    }
//                    bookList.addAll(allBooks);
//                    emitter.onNext(bookList);
//                }
//
//            }
//        });
//
//        DisposableObserver subscribe = RxUtil.subscribe(listObservable, new SampleProgressObserver<List<SearchBook>>() {
//            @Override
//            public void onNext(List<SearchBook> searchBookBeans) {
//                mView.showSearchResult(key, searchBookBeans);
//            }
//        });
//        addSubscribe(subscribe);
//    }
//
//    @Override
//    public void stopSearch() {
//        isSearching = false;
//        if (searchBookModel != null) {
//            searchBookModel.stopSearch();
//        }
//    }
//
//    @Override
//    public void checkBookSource() {
//        Observable<Boolean> observable = Observable.create(new ObservableOnSubscribe<Boolean>() {
//            @Override
//            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
//                List<BookSource> allBookSource = BookSourceManager.getAllBookSource();
//                emitter.onNext(allBookSource == null || allBookSource.size() == 0);
//
//            }
//        });
//
//        DisposableObserver subscribe = RxUtil.subscribe(observable, new SampleProgressObserver<Boolean>() {
//            @Override
//            public void onNext(Boolean aBoolean) {
//                mView.showCheckBookSourceResult(aBoolean);
//            }
//        });
//
//        addSubscribe(subscribe);
//
//    }
//}
