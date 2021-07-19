package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.micoredu.reader.bean.BaseChapterBean;
import com.micoredu.reader.bean.BookChapterBean;

import java.util.List;

/**
 * Description:
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface BookChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplaceInTx(List<BookChapterBean> chapterBeans);

    @Query("DELETE from bookChapters")
    void deleteAll();

    @Query("select * from bookchapters where noteUrl =:noteUrl order by durChapterUrl")
    List<BookChapterBean> getChapterList(String noteUrl);

    @Query("delete from bookchapters where noteUrl is:noteUrl")
    void delChapterList(String noteUrl);

    @Query("select * from bookchapters where noteUrl =:noteUrl and durChapterIndex = :chapterIndex")
    BaseChapterBean getChapter(String noteUrl, int chapterIndex);

    @Query("select * from bookchapters where noteUrl is:noteUrl")
    BookChapterBean load(String noteUrl);
}
