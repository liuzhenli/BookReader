package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;
import com.micoredu.readerlib.bean.BookSourceBean;

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
    }
}
