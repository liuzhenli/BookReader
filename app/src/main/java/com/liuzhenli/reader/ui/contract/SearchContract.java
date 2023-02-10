/*
package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.common.base.BaseContract;
import com.liuzhenli.common.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.micoredu.reader.bean.SearchBook;

import java.util.List;

*/
/**
 * Description:
 *
 * @author liuzhenli 2020/11/2
 * Email: 848808263@qq.com
 *//*

public class SearchContract {
    public interface View extends BaseContract.BaseView {

        */
/**
         * 成功插入一条历史数据
         *//*

        void addHistorySuccess();

        void showSearchHistory(List<SearchHistoryBean> data);

        void showSearchResult(String key, List<SearchBook> bookList);

        */
/**
         * show check result
         *
         * @param noSourceAvailable true if book source is empty
         *//*


        void showCheckBookSourceResult(boolean noSourceAvailable);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        */
/**
         * ¬插入一条历史数据
         *//*

        void addToSearchHistory(int type, String searchKey);

        void clearSearchHistory();

        */
/***删除搜索历史item*//*

        void removeSearchHistoryItem(SearchHistoryBean data);

        void getSearchHistory();

        void search(int type, int page, String key, RecyclerArrayAdapter<SearchBook> mAdapter);

        void stopSearch();

        */
/**
         * check if book source is empty
         *//*

        void checkBookSource();
    }
}
*/
