package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;
import com.micoredu.readerlib.bean.BookShelfBean;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.model.SearchBookModel;

import java.util.List;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-14 11:00
 */
public class ChangeSourceContract {
    public interface View extends BaseContract.BaseView {
        void showSearchBook(List<SearchBookBean> bookList);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getSearchBookInDatabase(BookShelfBean bookShelfBean, SearchBookModel searchBookModel);

         int compareSearchBooks(SearchBookBean s1, SearchBookBean s2);
    }
}
