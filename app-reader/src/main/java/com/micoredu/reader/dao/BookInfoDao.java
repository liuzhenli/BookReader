package com.micoredu.reader.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jayway.jsonpath.Criteria;
import com.micoredu.reader.bean.BookInfoBean;

import java.util.List;

/**
 * Description: bookInfo
 *
 * @author liuzhenli 2021/6/28
 * Email: 848808263@qq.com
 */
@Dao
public interface BookInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(BookInfoBean bookInfoBean);

    @Query("select * from bookInfo where noteUrl is:noteUrl limit 1")
    BookInfoBean getFirstByNoteUrl(String noteUrl);

    @Query("select * from bookInfo where name is:bookName limit 1")
    BookInfoBean getFirstByBookName(String bookName);

    @Query("select * from bookinfo where name like '%'||:key||'%'")
    List<BookInfoBean> searchBookInfo(String key);

    @Query("delete from bookInfo")
    void deleteAll();

    @Query("delete from bookinfo where noteUrl is:noteUrl")
    void deleteByNoteUrl(String noteUrl);


}
