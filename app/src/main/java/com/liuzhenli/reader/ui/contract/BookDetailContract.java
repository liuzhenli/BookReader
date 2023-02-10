package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.bean.Book;
import com.micoredu.reader.bean.BookChapter;

import java.util.List;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-14 11:00
 */
public class BookDetailContract {
    public interface View extends BaseContract.BaseView {
        void showBookInfo(Book data, List<BookChapter> BookChapters);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getBookInfo(Book Book, boolean isInBookShelf);

        /**
         * 从本地数据库获取书源
         */
        void getBookSource(Book Book);
    }


}
