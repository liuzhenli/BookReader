package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.bean.Book;
import com.micoredu.reader.bean.SearchBook;
import com.micoredu.reader.model.webBook.SearchModel;

import java.util.List;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-14 11:00
 */
public class ChangeSourceContract {
    public interface View extends BaseContract.BaseView {
        void showSearchBook(List<SearchBook> bookList);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getSearchBookInDatabase(Book Book, SearchModel searchBookModel);

         int compareSearchBooks(SearchBook s1, SearchBook s2);
    }
}
