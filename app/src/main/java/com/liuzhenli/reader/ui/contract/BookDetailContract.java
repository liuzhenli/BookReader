package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;
import com.micoredu.readerlib.bean.BookChapterBean;
import com.micoredu.readerlib.bean.BookInfoBean;
import com.micoredu.readerlib.bean.BookShelfBean;

import java.util.List;

/**
 * describe:
 *
 * @author Liuzhenli on 2019-12-14 11:00
 */
public class BookDetailContract {
    public interface View extends BaseContract.BaseView {
        void showBookInfo(BookInfoBean data, List<BookChapterBean> bookChapterBeans);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getBookInfo(BookShelfBean bookShelfBean, boolean isInBookShelf);

        /**
         * 从本地数据库获取书源
         */
        void getBookSource(BookShelfBean bookShelfBean);
    }


}
