package com.micoredu.reader.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.micoredu.reader.bean.BookChapterBean;
import com.micoredu.reader.bean.BookContentBean;
import com.micoredu.reader.bean.BookInfoBean;
import com.micoredu.reader.bean.BookShelfBean;
import com.micoredu.reader.bean.BookSourceBean;
import com.micoredu.reader.bean.BookmarkBean;
import com.micoredu.reader.bean.CookieBean;
import com.micoredu.reader.bean.ReadHistory;
import com.micoredu.reader.bean.ReplaceRuleBean;
import com.micoredu.reader.bean.SearchBookBean;
import com.micoredu.reader.bean.SearchHistoryBean;
import com.micoredu.reader.bean.TxtChapterRuleBean;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/26
 * Email: 848808263@qq.com
 */

@Database(entities = {BookContentBean.class, BookChapterBean.class, BookInfoBean.class,
        BookmarkBean.class, BookShelfBean.class, BookSourceBean.class, CookieBean.class,
        ReadHistory.class, ReplaceRuleBean.class, SearchBookBean.class,
        SearchHistoryBean.class, TxtChapterRuleBean.class}, version = 3, exportSchema = false)
public abstract class AppReaderDatabase extends RoomDatabase {

    public AppReaderDatabase() {

    }


    public abstract BookChapterDao getBookChapterDao();

    public abstract BookContentDao getBookContentDao();

    public abstract BookInfoDao getBookInfoDao();

    public abstract BookMarkDao getBookMarkDao();

    public abstract BookShelfDao getBookShelfDao();

    public abstract BookSourceDao getBookSourceDao();

    public abstract CookieDao getCookieDao();

    public abstract ReadHistoryDao getReadHistoryDao();

    public abstract ReplaceRuleDao getReplaceRuleDao();

    public abstract SearchBookDao getSearchBookDao();

    public abstract SearchHistoryDao getSearchHistoryDao();

    public abstract TxtChapterRuleDao getTxtChapterRuleDao();
}
