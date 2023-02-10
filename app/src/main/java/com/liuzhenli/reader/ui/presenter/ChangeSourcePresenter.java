//package com.liuzhenli.reader.ui.presenter;
//
//import com.liuzhenli.common.BaseApplication;
//import com.liuzhenli.common.utils.RxUtil;
//import com.liuzhenli.common.base.RxPresenter;
//import com.liuzhenli.common.observer.SampleProgressObserver;
//import com.liuzhenli.reader.ui.contract.ChangeSourceContract;
//import com.micoredu.reader.bean.Book;
//import com.micoredu.reader.bean.BookSource;
//import com.micoredu.reader.bean.SearchBook;
//import com.micoredu.reader.dao.AppDatabase;
//import com.micoredu.reader.model.webBook.SearchModel;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Objects;
//
//import javax.inject.Inject;
//
//import io.reactivex.Observable;
//import io.reactivex.annotations.NonNull;
//
///**
// * describe:
// *
// * @author Liuzhenli on 2019-12-14 11:00
// */
//public class ChangeSourcePresenter extends RxPresenter<ChangeSourceContract.View> implements ChangeSourceContract.Presenter<ChangeSourceContract.View> {
//
//    private String bookTag;
//
//    @Inject
//    public ChangeSourcePresenter() {
//
//    }
//
//    @Override
//    public void getSearchBookInDatabase(Book Book, SearchModel searchBookModel) {
//
//        if (Book != null && Book.getBookInfoBean() != null) {
//            String bookName = Book.getBookInfoBean().getName();
//            String bookAuthor = Book.getBookInfoBean().getAuthor();
//            bookTag = Book.getBookInfoBean().getTag();
//
//            Observable<List<SearchBook>> objectObservable = Observable.create(emitter -> {
//                List<SearchBook> searchBookBeans = AppDatabase.Companion.createDatabase(BaseApplication.Companion.getInstance()).getSearchBookDao().getBookList(bookName, bookAuthor);
//                if (searchBookBeans == null) searchBookBeans = new ArrayList<>();
//                List<SearchBook> searchBookList = new ArrayList<>();
//                List<BookSource> bookSourceList = new ArrayList<>(BookSourceManager.getSelectedBookSource());
//                if (bookSourceList.size() > 0) {
//                    for (BookSource BookSource : BookSourceManager.getSelectedBookSource()) {
//                        boolean hasSource = false;
//                        for (SearchBook SearchBook : new ArrayList<>(searchBookBeans)) {
//                            if (Objects.equals(SearchBook.getTag(), BookSource.getBookSourceUrl())) {
//                                bookSourceList.remove(BookSource);
//                                searchBookList.add(SearchBook);
//                                hasSource = true;
//                                break;
//                            }
//                        }
//                        if (hasSource) {
//                            bookSourceList.remove(BookSource);
//                        }
//                    }
//                    searchBookModel.searchReNew();
//                    searchBookModel.initSearchEngineS(bookSourceList);
//                    long startThisSearchTime = System.currentTimeMillis();
//                    searchBookModel.setSearchTime(startThisSearchTime);
//                    List<Book> bookList = new ArrayList<>();
//                    bookList.add(Book);
//                    searchBookModel.search(bookName, startThisSearchTime, bookList, false);
//                    UpLastChapterModel.getInstance().startUpdate(searchBookList);
//                }
//                if (searchBookList.size() > 0) {
//                    for (SearchBook SearchBook : searchBookList) {
//                        if (SearchBook.getTag().equals(Book.getTag())) {
//                            SearchBook.setIsCurrentSource(true);
//                        } else {
//                            SearchBook.setIsCurrentSource(false);
//                        }
//                    }
//                    Collections.sort(searchBookList, this::compareSearchBooks);
//                }
//                emitter.onNext(searchBookList);
//            });
//            addSubscribe(RxUtil.subscribe(objectObservable, new SampleProgressObserver<List<SearchBook>>() {
//                @Override
//                public void onNext(@NonNull List<SearchBook> searchBookBeans) {
//                    mView.showSearchBook(searchBookBeans);
//                }
//            }));
//
//        }
//    }
//
//    @Override
//    public int compareSearchBooks(SearchBook s1, SearchBook s2) {
//        boolean s1tag = s1.getTag().equals(bookTag);
//        boolean s2tag = s2.getTag().equals(bookTag);
//        if (s2tag && !s1tag)
//            return 1;
//        else if (s1tag && !s2tag)
//            return -1;
//        int result = Long.compare(s2.getAddTime(), s1.getAddTime());
//        if (result != 0)
//            return result;
//        result = Integer.compare(s2.getLastChapterNum(), s1.getLastChapterNum());
//        if (result != 0)
//            return result;
//        return Integer.compare(s2.getWeight(), s1.getWeight());
//    }
//}
