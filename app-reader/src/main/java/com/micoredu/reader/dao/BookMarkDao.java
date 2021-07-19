package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jayway.jsonpath.Criteria;
import com.micoredu.reader.bean.BookmarkBean;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface BookMarkDao {
    @Delete
    void delete(BookmarkBean bookmarkBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(BookmarkBean bookmarkBean);

    @Query("select * from bookmarks where bookName is:bookName order by chapterIndex asc")
    List<BookmarkBean> getBookmarkList(String bookName);
}
