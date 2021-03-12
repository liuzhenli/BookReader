package com.micoredu.reader.ui.contract;

import android.content.Context;

import com.liuzhenli.common.base.BaseBean;
import com.liuzhenli.common.base.BaseContract;
import com.micoredu.reader.bean.BookSourceBean;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/9
 * Email: 848808263@qq.com
 */
public class BookSourceContract {
    public interface View extends BaseContract.BaseView {
        /**
         * present local book source
         *
         * @param list book sources
         */
        void showLocalBookSource(List<BookSourceBean> list);

        void showAddNetSourceResult(List<BookSourceBean> list);

        void shoDeleteBookSourceResult();

        void showCheckBookSourceResult(BaseBean data);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        /**
         * get bookSource data from database
         *
         * @param groupKey the searchKey
         */
        void getLocalBookSource(String groupKey);

        /**
         * set book source enable or disable
         *
         * @param enable     true or false
         * @param bookSource book source
         */
        void setEnable(BookSourceBean bookSource, boolean enable);

        /**
         * set book source to top
         *
         * @param enable     true or false
         * @param bookSource book source
         */
        void setTop(BookSourceBean bookSource, boolean enable);

        void getNetSource(String url);

        /**
         * delete book source
         */
        void deleteSelectedSource(List<BookSourceBean> bookSource);

        void checkBookSource(Context context, List<BookSourceBean> selectedBookSource);

        void loadBookSourceFromFile(String filePath);
    }
}
