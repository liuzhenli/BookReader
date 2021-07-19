package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.micoredu.reader.bean.BookShelfBean;

import java.util.List;

/**
 * Description: bookShelf
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface BookShelfDao {
    @Query("SELECT * FROM bookShelf Order BY finalDate")
    List<BookShelfBean> allByFinalDate();

    @Query("select * from bookShelf where `group` =:group order by finalDate limit 1")
    List<BookShelfBean> getBooksByGroup(int group);

    @Delete
    void delete(BookShelfBean bookShelfBean);

    @Query("select * from bookShelf where noteUrl = :bookUrl limit 1")
    BookShelfBean getFirstByNoteUrl(String bookUrl);

    @Query("select * from bookShelf where noteUrl = :bookUrl")
    List<BookShelfBean> getByNoteUrl(String bookUrl);

    @Query("delete from bookshelf")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(BookShelfBean bookShelfBean);

    @Query("delete from bookshelf where noteUrl is:noteUrl")
    void deleteByNoteUrl(String noteUrl);
}
