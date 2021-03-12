package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.bean.BookShelfBean;

import java.util.List;

/**
 * describe:
 *
 * @author Liuzhenli on 2020-01-11 15:15
 */
public class BookShelfContract {
    public interface View extends BaseContract.BaseView {
        /**
         * 展示书架信息
         *
         * @param bookShelfBeanList 书架书列表
         */
        public void showBooks(List<BookShelfBean> bookShelfBeanList);

        /**
         * 书移除书架
         *
         * @param bookShelfBean 移除的书信息
         */
        void onBookRemoved(BookShelfBean bookShelfBean);

        /**
         * 显示正在更新
         * @param bookShelfBean
         */
        void setRefreshingBook(BookShelfBean bookShelfBean);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        public void queryBooks(final Boolean needRefresh, final int group);

        void removeFromBookShelf(BookShelfBean bookShelfBean);

        /**
         * 更新书信息
         */
        void refreshBookShelf();

        void updateBookInfo(BookShelfBean book);
    }
}
