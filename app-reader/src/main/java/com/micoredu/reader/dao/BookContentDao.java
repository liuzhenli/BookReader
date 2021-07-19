package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.micoredu.reader.bean.BookContentBean;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface BookContentDao {
    @Query("SELECT * FROM bookContents WHERE durChapterUrl is :durChapterUrl")
    BookContentBean load(String durChapterUrl);

    @Delete
    void delete(BookContentBean contentBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(BookContentBean bookContentBean);
}
