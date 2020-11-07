package com.liuzhenli.reader.ui.contract;

import com.liuzhenli.reader.base.BaseContract;
import com.micoredu.readerlib.bean.BookInfoBean;
import com.micoredu.readerlib.bean.SearchBookBean;
import com.micoredu.readerlib.bean.SearchHistoryBean;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2020/11/2
 * Email: 848808263@qq.com
 */
public class SearchContract {
    public interface View extends BaseContract.BaseView {

        /**
         * 成功插入一条历史数据
         */
        void addHistorySuccess();

        void showSearchHistory(List<SearchHistoryBean> data);

        void showSearchResult(String key, List<SearchBookBean> bookList);
    }

    public interface Presenter<T> extends BaseContract.BasePresenter<T> {
        /**
         * ¬插入一条历史数据
         */
        void addToSearchHistory(int type, String searchKey);

        void clearSearchHistory();

        /***删除搜索历史item*/
        void removeSearchHistoryItem(SearchHistoryBean data);

        void getSearchHistory();

        void search(int type, int page, String key);

        void stopSearch();
    }
}
