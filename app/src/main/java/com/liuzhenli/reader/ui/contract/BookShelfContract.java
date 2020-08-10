package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;
import com.micoredu.readerlib.bean.BookShelfBean;

import java.util.List;

/**
 * describe:
 *
 * @author Liuzhenli on 2020-01-11 15:15
 */
public class BookShelfContract {
    public interface View extends BaseContract.BaseView {
        public void showBooks(List<BookShelfBean> bookShelfBeanList);

        void onBookRemoved(BookShelfBean bookShelfBean);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        public void queryBooks(final Boolean needRefresh, final int group);

        void removeFromBookShelf(BookShelfBean bookShelfBean);
    }
}
